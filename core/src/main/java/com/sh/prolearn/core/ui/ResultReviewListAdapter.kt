package com.sh.prolearn.core.ui

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sh.prolearn.core.R
import com.sh.prolearn.core.databinding.ItemListResultReviewBinding
import com.sh.prolearn.core.domain.model.*
import com.sh.prolearn.core.utils.GeneralUtils.getTimeFromInt

class ResultReviewListAdapter : RecyclerView.Adapter<ResultReviewListAdapter.ItemViewHolder>() {
    private var listData = ArrayList<Progress>()
    private var lessonCode = ""

    var onIbPlayerAnswerClick: ((Progress, String) -> Unit)? = null
    var onIbPlayerQuestClick: ((Progress, String) -> Unit)? = null

    fun setData(newListData: List<Progress>) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
    }

    fun setLessonCode(lessonCode: String) {
        this.lessonCode = lessonCode
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_result_review, parent, false))

    override fun getItemCount() = listData.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListResultReviewBinding.bind(itemView)
        fun bind(data: Progress) {
            with(binding) {
                val progressSplit = data.progress!!.split('-')
                tvTimeSpent.text = getTimeFromInt(data.scores?.time!!)
                tvScore.text = String.format("%.1f", data.scores.score)
                tvAnswer.text = Html.fromHtml(data.scores.answer, 1)
                tvQuest.text = data.scores.quest
                tvTitle.text = if (progressSplit[0] == "q") "Kuis - ${progressSplit[1]}" else if (progressSplit[0] == "t") "Teori - ${progressSplit[1]}" else "Rangkuman"
            }
        }
        init {
            binding.ibPlayerAnswer.setOnClickListener{
                onIbPlayerAnswerClick?.invoke(listData[bindingAdapterPosition], binding.tvAnswer.text.toString())
            }
            binding.ibPlayerQuest.setOnClickListener{
                onIbPlayerQuestClick?.invoke(listData[bindingAdapterPosition], binding.tvQuest.text.toString())
            }
        }
    }
}