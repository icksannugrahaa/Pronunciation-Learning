package com.sh.prolearn.core.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sh.prolearn.core.R
import com.sh.prolearn.core.databinding.ItemListModuleLessonDetailBinding
import com.sh.prolearn.core.domain.model.Lesson
import com.sh.prolearn.core.domain.model.Module
import com.sh.prolearn.core.domain.model.Progress
import com.sh.prolearn.core.utils.TransitionUtils.setCardTransition

class LessonListAdapter : RecyclerView.Adapter<LessonListAdapter.ItemViewHolder>() {
    private var listData = ArrayList<Lesson>()
    private var progressData = ArrayList<Progress>()

    var onItemClick: ((Lesson, List<Progress>?) -> Unit)? = null

    fun setData(newListData: List<Lesson>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
    }

    fun setProgress(progress: List<Progress>) {
        progressData.clear()
        progressData.addAll(progress)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_module_lesson_detail, parent, false))

    override fun getItemCount() = listData.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListModuleLessonDetailBinding.bind(itemView)
        fun bind(data: Lesson) {
            with(binding) {
                tvLessonTitle.text = data.name
                tvLessonDesc.text = data.description
                badge.maxWidth = 70
                badge.maxHeight = 70
                val myProgress = progressData.filter {
                    val progressOrder = it.lesson!!.split("-")
                    data.order == progressOrder[1].toInt()
                }
                if(myProgress.isEmpty()) {
                    badge.setAnimation("locked-lesson.json")
                    badge.maxWidth = 50
                    badge.maxHeight = 50
                    badge.visibility = View.VISIBLE
                } else {
                    myProgress.forEachIndexed { index, it ->
                        val checkProgressType = it.progress!!.split("-")
                        if(checkProgressType[0] == "s" && it.status == "done") {
                            badge.setAnimation("done-lesson.json")
                            badge.visibility = View.VISIBLE
                        } else if(checkProgressType[0] == "t" && it.status == "done" && index > 0 || checkProgressType[0] == "q" && it.status == "done" && index > 0) {
                            badge.setAnimation("inprogress-lesson.json")
                            badge.visibility = View.VISIBLE
                        } else if(checkProgressType[0] == "t" && it.status == "new" && index > 1 || checkProgressType[0] == "q" && it.status == "new" && index > 1 || checkProgressType[0] == "s" && it.status == "new" && index > 1) {
                            badge.setAnimation("inprogress-lesson.json")
                            badge.visibility = View.VISIBLE
                        } else {
                            badge.setAnimation("new-lesson.json")
                            badge.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
        init {
            binding.root.setOnClickListener{
                onItemClick?.invoke(listData.filter{it.order == listData[bindingAdapterPosition].order!!}[0], if(progressData.size > 0) progressData else ArrayList<Progress>())
            }
            binding.btnPlay.setOnClickListener {
                onItemClick?.invoke(listData.filter{it.order == listData[bindingAdapterPosition].order!!}[0], if(progressData.size > 0) progressData else ArrayList<Progress>())
            }
        }
    }
}