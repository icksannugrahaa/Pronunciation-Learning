package com.sh.prolearn.core.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sh.prolearn.core.R
import com.sh.prolearn.core.databinding.ItemListModuleLessonDetailBinding
import com.sh.prolearn.core.databinding.ItemListReviewBinding
import com.sh.prolearn.core.domain.model.Comment
import com.sh.prolearn.core.domain.model.Lesson
import com.sh.prolearn.core.domain.model.Module
import com.sh.prolearn.core.domain.model.Progress
import com.sh.prolearn.core.utils.ImageViewUtils.loadImageFromServer
import com.sh.prolearn.core.utils.TransitionUtils.setCardTransition

class ReviewListAdapter : RecyclerView.Adapter<ReviewListAdapter.ItemViewHolder>() {
    private var listData = ArrayList<Comment>()

    var onItemClick: ((Comment) -> Unit)? = null

    fun setData(newListData: List<Comment>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_review, parent, false))

    override fun getItemCount() = listData.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListReviewBinding.bind(itemView)
        fun bind(data: Comment) {
            with(binding) {
                tvReview.text = data.comment
                tvUsername.text = data.name
                rbRating.rating = data.rating!!.toFloat()
                tvDate.text = "  |  ${data.created_at}"
                civUser.loadImageFromServer(data.avatar)
            }
        }
        init {
            binding.root.setOnClickListener{
                onItemClick?.invoke(listData[bindingAdapterPosition])
            }
        }
    }
}