package com.sh.prolearn.app.progress

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sh.prolearn.R
import com.sh.prolearn.app.lesson.ResultActivity
import com.sh.prolearn.app.modules.ModuleDetailActivity
import com.sh.prolearn.app.modules.ModuleViewModel
import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.data.preferences.AuthPreferences
import com.sh.prolearn.core.domain.model.Progress
import com.sh.prolearn.core.ui.LessonHistoriesAdapter
import com.sh.prolearn.core.ui.ModuleListAdapter
import com.sh.prolearn.core.utils.Consts
import com.sh.prolearn.core.utils.Consts.ARG_LESSON_CODE
import com.sh.prolearn.core.utils.ToastUtils
import com.sh.prolearn.databinding.FragmentAboutBinding
import com.sh.prolearn.databinding.FragmentLearnHistoryBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.Serializable

class LearnHistoryFragment : Fragment() {
    private var _binding: FragmentLearnHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ModuleViewModel by viewModel()
    private lateinit var lessonHistoryAdapter: LessonHistoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLearnHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            moduleGet()
            val refreshLayout = binding.swipeToRefreshLayout
            refreshLayout.setColorSchemeColors(
                R.color.green_200,
                R.color.purple_200,
                R.color.teal_200,
                R.color.red_200
            )
            refreshLayout.setOnRefreshListener {
                moduleGet()
                refreshLayout.isRefreshing = false
            }
        }
    }

    private fun moduleGet() {
        var authData = AuthPreferences(requireContext()).authData
        val authToken = AuthPreferences(requireContext()).authToken
        lessonHistoryAdapter = LessonHistoriesAdapter()

        viewModel.moduleIndex("null", "null").observe(viewLifecycleOwner) { data ->
            if (data != null) {
                when (data) {
                    is Resource.Loading<*> -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.viewError.root.visibility = View.GONE
                    }
                    is Resource.Success<*> -> {
                        binding.progressBar.visibility = View.GONE
                        binding.viewError.root.visibility = View.GONE

                        if (authData != null) {
                            viewModel.progressIndex(authToken).observe(viewLifecycleOwner) { progress ->
                                if (progress != null) {
                                    when (progress) {
                                        is Resource.Loading<*> -> {
                                            binding.progressBar.visibility = View.VISIBLE
                                            binding.viewError.root.visibility = View.GONE
                                        }
                                        is Resource.Success<*> -> {
                                            binding.progressBar.visibility = View.GONE
                                            binding.viewError.root.visibility = View.GONE
                                            progress.data?.let { lessonHistoryAdapter.setProgress(it) }
                                            authData.let {
                                                if (it != null) {
                                                    lessonHistoryAdapter.setAuth(it)
                                                }
                                            }
                                            lessonHistoryAdapter.setData(data.data)
                                        }
                                        is Resource.Error<*> -> {
                                            val message = progress.message.toString()
                                            if(message.contains("401", ignoreCase = true)) {
                                                AuthPreferences(requireContext()).saveAuthData(null)
                                                authData = null
                                                ToastUtils.showToast(
                                                    getString(R.string.login_expired),
                                                    requireContext()
                                                )
                                                lessonHistoryAdapter.setData(data.data)
                                                binding.progressBar.visibility = View.GONE
                                                binding.viewError.root.visibility = View.GONE
                                            } else {
                                                binding.progressBar.visibility = View.GONE
                                                binding.viewError.root.visibility = View.VISIBLE
                                                binding.viewError.tvError.text = message
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            lessonHistoryAdapter.setData(data.data)
                        }
                    }
                    is Resource.Error<*> -> {
                        binding.progressBar.visibility = View.GONE
                        binding.viewError.root.visibility = View.VISIBLE
                        binding.viewError.tvError.text =
                            data.message ?: getString(R.string.something_wrong)
                    }
                }
            }
        }

        if (authData != null) {
            lessonHistoryAdapter.onLessonClick = {
                Log.d("TAG_CLIKER", it)
                Intent(requireContext(), ResultActivity::class.java).apply {
                    this.putExtra(ARG_LESSON_CODE, it)
                    startActivity(this)
                }
            }
        }

        with(binding.rvLearnHistories) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = lessonHistoryAdapter
        }
    }
}