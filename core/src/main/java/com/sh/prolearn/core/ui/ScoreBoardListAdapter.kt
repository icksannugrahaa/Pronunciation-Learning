package com.sh.prolearn.core.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sh.prolearn.core.R
import com.sh.prolearn.core.databinding.ItemListModuleLessonDetailBinding
import com.sh.prolearn.core.databinding.ItemListReviewBinding
import com.sh.prolearn.core.databinding.ItemListScoreboardBinding
import com.sh.prolearn.core.domain.model.*
import com.sh.prolearn.core.utils.GeneralUtils.getTimeFromInt
import com.sh.prolearn.core.utils.ImageViewUtils.loadImageFromServer
import com.sh.prolearn.core.utils.TransitionUtils.setCardTransition

class ScoreBoardListAdapter : RecyclerView.Adapter<ScoreBoardListAdapter.ItemViewHolder>() {
    private var listData = ArrayList<ScoreBoard>()
    private var authData = Account()

    var onItemClick: ((ScoreBoard) -> Unit)? = null

    fun setData(newListData: List<ScoreBoard>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
    }

    fun setAuth(auth: Account) {
        authData = auth
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_scoreboard, parent, false))

    override fun getItemCount() = listData.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListScoreboardBinding.bind(itemView)
        fun bind(data: ScoreBoard) {
            with(binding) {
                if(authData.email == data.email) {
                    layoutScoreboardItem.setBackgroundColor(Color.parseColor("#66BB6A"))
                    tvScore.setTextColor(Color.parseColor("#FFFFFF"))
                    tvPosition.setTextColor(Color.parseColor("#FFFFFF"))
                    tvTime.setTextColor(Color.parseColor("#FFFFFF"))
                    tvUsername.setTextColor(Color.parseColor("#FFFFFF"))
                }
                tvScore.text = String.format("%.1f", data.score)
                tvPosition.text = "${bindingAdapterPosition + 1}. "
                tvTime.text = getTimeFromInt(data.time!!)
                tvUsername.text = data.name
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