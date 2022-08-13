package com.sh.prolearn.app.modules

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sh.prolearn.R
import com.sh.prolearn.app.lesson.LessonActivity
import com.sh.prolearn.app.lesson.ResultActivity
import com.sh.prolearn.core.domain.model.Lesson
import com.sh.prolearn.core.domain.model.Progress
import com.sh.prolearn.core.ui.LessonListAdapter
import com.sh.prolearn.core.utils.Consts
import com.sh.prolearn.core.utils.Consts.ARG_LESSON_CODE
import com.sh.prolearn.core.utils.Consts.ARG_LESSON_DATA
import com.sh.prolearn.core.utils.Consts.ARG_LESSON_PROGRESS
import com.sh.prolearn.core.utils.Consts.ARG_OPTION_DATA
import com.sh.prolearn.core.utils.Consts.EXTRA_QUESTION_DATA
import com.sh.prolearn.core.utils.ToastUtils
import com.sh.prolearn.core.utils.ToastUtils.showToast
import com.sh.prolearn.databinding.FragmentHomeBinding
import com.sh.prolearn.databinding.FragmentMaterialBinding

class MaterialFragment : Fragment() {
    private var _binding: FragmentMaterialBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMaterialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            val lessonData = arguments?.getSerializable(ARG_LESSON_DATA) as List<Lesson>
            val lessonProgress = arguments?.getSerializable(ARG_LESSON_PROGRESS) as List<Progress>
            val lessonCode = arguments?.getString(ARG_LESSON_CODE)
            val lessonAdapter = LessonListAdapter()
            lessonAdapter.setData(lessonData)
            lessonAdapter.setProgress(lessonProgress)
            lessonAdapter.onItemClick = { lesson, progress ->
                if (progress?.size!! > 0) {
                    val progressData = progress.filter { item ->
                        item.lesson == "$lessonCode-${lesson.order}"
                    }
                    if(progressData.isNotEmpty()) {
                        for (index in progressData.indices) {
                            val item = progressData[index]
                            Log.d("TAG_PROGRESSES1", item.lesson.toString())
                            Log.d("TAG_PROGRESSES2", "$lessonCode-${lesson.order}")
                            if (item.lesson == "$lessonCode-${lesson.order}") {
                                Log.d("TAG_PROGRESSES_STATUS", item.status.toString())
                                Log.d("TAG_PROGRESSES_TYPE_STATUS", item.progress!!.split("-")[0])
                                if (item.status == "new" || item.status == "in_progress") {
                                    Intent(requireContext(), LessonActivity::class.java).apply {
                                        this.putExtra(EXTRA_QUESTION_DATA, lesson)
                                        this.putExtra(ARG_LESSON_PROGRESS, item)
                                        this.putExtra(
                                            ARG_LESSON_CODE,
                                            "$lessonCode-${lesson.order}"
                                        )
                                        startActivity(this)
                                    }
                                    break
                                } else if (item.status == "done" && item.progress!!.split("-")[0] == "s") {
                                    Intent(requireContext(), ResultActivity::class.java).apply {
                                        this.putExtra(
                                            ARG_LESSON_CODE,
                                            "$lessonCode-${lesson.order}"
                                        )
                                        startActivity(this)
                                    }
                                    break
                                }
                            } else {
                                showToast(
                                    getString(R.string.complete_material_before),
                                    requireContext()
                                )
                            }
                        }
                    } else {
                        showToast(
                            getString(R.string.complete_material_before),
                            requireContext()
                        )
                    }
                }
            }
            with(binding.rvLesson) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = lessonAdapter
            }
        }
    }
}