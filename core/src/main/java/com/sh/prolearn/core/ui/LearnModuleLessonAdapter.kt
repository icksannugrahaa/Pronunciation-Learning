package com.sh.prolearn.core.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sh.prolearn.core.R
import com.sh.prolearn.core.databinding.ItemListModuleLessonBinding
import com.sh.prolearn.core.domain.model.Account
import com.sh.prolearn.core.domain.model.Lesson
import com.sh.prolearn.core.domain.model.Progress
import com.sh.prolearn.core.domain.model.ProgressData
import com.sh.prolearn.core.utils.Consts
import com.sh.prolearn.core.utils.ToastUtils
import java.io.Serializable

class LearnModuleLessonAdapter : RecyclerView.Adapter<LearnModuleLessonAdapter.ItemViewHolder>() {
    private var listData = ArrayList<Lesson>()

    var onItemClick: ((Int) -> Unit)? = null

    fun setData(newListData: List<Lesson>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_module_lesson, parent, false))

    override fun getItemCount() = listData.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListModuleLessonBinding.bind(itemView)
        fun bind(data: Lesson) {
            with(binding) {
                tvQuestTitle.text = data.name
                tvClassDate.text = "Selesai"
            }
        }
        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(listData[bindingAdapterPosition].order!!)
            }
        }
    }

}