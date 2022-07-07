package com.sh.prolearn.core.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sh.prolearn.core.R
import com.sh.prolearn.core.databinding.ItemListModuleBinding
import com.sh.prolearn.core.domain.model.Lesson
import com.sh.prolearn.core.domain.model.Module
import com.sh.prolearn.core.utils.TransitionUtils.setCardTransition

class ModuleListAdapter : RecyclerView.Adapter<ModuleListAdapter.ItemViewHolder>() {
    private var listData = ArrayList<Module>()

    var onItemClick: ((Module) -> Unit)? = null

    fun setData(newListData: List<Module>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_module, parent, false))

    override fun getItemCount() = listData.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListModuleBinding.bind(itemView)
        fun bind(data: Module) {
            with(binding) {
                val lessonAdapter = ModuleLessonAdapter()
                lessonAdapter.setData(data.lessons as List<Lesson>?)

                rvLessonList.layoutManager = LinearLayoutManager(itemView.context)
                rvLessonList.setHasFixedSize(true)
                rvLessonList.adapter = lessonAdapter

                tvClassTitle.text = data.name
                tvClassTech.text = data.description
                btnExpand.setOnClickListener {
                    if(layoutBody.visibility == View.GONE) {
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
        init {
            binding.root.setOnClickListener{
                onItemClick?.invoke(listData[bindingAdapterPosition])
            }
        }
    }
}