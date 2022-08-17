package com.sh.prolearn.core.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sh.prolearn.core.R
import com.sh.prolearn.core.databinding.ItemListModuleBinding
import com.sh.prolearn.core.domain.model.Account
import com.sh.prolearn.core.domain.model.Lesson
import com.sh.prolearn.core.domain.model.Module
import com.sh.prolearn.core.domain.model.ProgressData
import com.sh.prolearn.core.utils.Consts
import com.sh.prolearn.core.utils.TransitionUtils.setCardTransition

class LessonHistoriesAdapter : RecyclerView.Adapter<LessonHistoriesAdapter.ItemViewHolder>() {
    private var listData = ArrayList<Module>()
    private var accountData: Account? = null
    var progressData: ProgressData? = null
    var lessonSelected = "1-1"

    var onLessonClick: ((String) -> Unit)? = null

    fun setData(newListData: List<Module>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    fun setAuth(account: Account) {
        accountData = account
    }

    fun setProgress(progress: ProgressData) {
        progressData = progress
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_module, parent, false)
        )

    override fun getItemCount() = listData.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListModuleBinding.bind(itemView)
        fun bind(data: Module) {
            with(binding) {
                var moduleProgress = 0
                var totalLesson = 0
                var totalLessonDone = 0
                val listLesson = ArrayList<Lesson>()
                for (lesson in data.lessons!!) totalLesson += (((lesson?.quiz?.size ?: 0) + lesson?.theory?.size!!)) + 1
                if (accountData != null) {
                    if (bindingAdapterPosition < progressData?.allProgress?.size!!) {
                        val currentLesson = progressData?.allProgress?.get(bindingAdapterPosition)
                        if (currentLesson?.order == data.order) {
                            for (lessonData in currentLesson?.progress!!) {
                                if(lessonData?.status == "done") {
                                    totalLessonDone += 1
                                }
                                if(lessonData?.status == "done" && lessonData.progress?.split("-")!![0] == "s") {
                                    listLesson.add(data.lessons[(currentLesson.order!!-1)]!!)
                                }
                            }
                            if (currentLesson.status == "done") {
                                badge.setAnimation("done-lesson.json")
                                badge.visibility = View.VISIBLE
                            } else if (currentLesson.status == "in_progress") {
                                badge.setAnimation("inprogress-lesson.json")
                                badge.visibility = View.VISIBLE
                            } else if (accountData!!.level!! >= data.level!!) {
                                badge.setAnimation("new-lesson.json")
                                badge.visibility = View.VISIBLE
                            } else {
                                badge.setAnimation("locked-lesson.json")
                                badge.visibility = View.VISIBLE
                            }
                        } else {
                            badge.setAnimation("locked-lesson.json")
                            badge.visibility = View.VISIBLE
                        }
                    } else if (accountData!!.level!! >= data.level!!) {
                        badge.setAnimation("new-lesson.json")
                        badge.visibility = View.VISIBLE
                    } else {
                        badge.setAnimation("locked-lesson.json")
                        badge.visibility = View.VISIBLE
                    }
                } else {
                    badge.setAnimation("locked-lesson.json")
                    badge.visibility = View.VISIBLE
                }
                pbClassProgress.progress = ((totalLessonDone.toDouble() / totalLesson.toDouble())*100).toInt()
                val lessonAdapter = LearnModuleLessonAdapter()
                lessonAdapter.setData(listLesson)

                rvLessonList.layoutManager = LinearLayoutManager(itemView.context)
                rvLessonList.setHasFixedSize(true)
                rvLessonList.adapter = lessonAdapter

                lessonAdapter.onItemClick =  { order ->
                    lessonSelected = "${data.order}-$order"
                    val intent = Intent(binding.root.context, Class.forName("com.sh.prolearn.app.lesson.ResultActivity"))
                    intent.putExtra(Consts.ARG_LESSON_CODE, lessonSelected)
                    binding.root.context.startActivity(intent)
                }

                tvClassTitle.text = data.name
                tvClassTech.text = data.description
                btnExpand.setOnClickListener {
                    if (layoutBody.visibility == View.GONE) {
                        setCardTransition(layoutBody)
                        layoutBody.visibility = View.VISIBLE
                        btnExpand.setImageResource(R.drawable.ic_expand_more)
                    } else {
                        setCardTransition(layoutBody)
                        layoutBody.visibility = View.GONE
                        btnExpand.setImageResource(R.drawable.ic_expand_less)
                    }
                }
                root.setOnClickListener {
                    if (layoutBody.visibility == View.GONE) {
                        setCardTransition(layoutBody)
                        layoutBody.visibility = View.VISIBLE
                        btnExpand.setImageResource(R.drawable.ic_expand_more)
                    } else {
                        setCardTransition(layoutBody)
                        layoutBody.visibility = View.GONE
                        btnExpand.setImageResource(R.drawable.ic_expand_less)
                    }
                }
            }
        }
    }
}