package com.sh.prolearn.core.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sh.prolearn.core.R
import com.sh.prolearn.core.databinding.ItemListModuleLessonBinding
import com.sh.prolearn.core.domain.model.Lesson

class ModuleLessonAdapter : RecyclerView.Adapter<ModuleLessonAdapter.ItemViewHolder>() {
    private var listData = ArrayList<Lesson>()

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
                tvClassDate.text = data.exp.toString()
            }
        }
    }
}