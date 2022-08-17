package com.sh.prolearn.core.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sh.prolearn.core.R
import com.sh.prolearn.core.databinding.ItemListAchievementBinding
import com.sh.prolearn.core.domain.model.*

class AchievementListAdapter : RecyclerView.Adapter<AchievementListAdapter.ItemViewHolder>() {
    private var listData = ArrayList<Achievement>()
    private var userData = Account()

    var onItemClick: ((Achievement) -> Unit)? = null

    fun setData(newListData: List<Achievement>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
    }

    fun setUser(user: Account) {
        userData = user
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_achievement, parent, false))

    override fun getItemCount() = listData.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListAchievementBinding.bind(itemView)
        fun bind(data: Achievement) {
            with(binding) {
                tvTitle.text = data.name
                var totalArchived = 0
                data.level?.forEachIndexed { index, achievementLevel ->
                    userData.achievement?.forEach { ulevel ->
                        if(data.name == ulevel?.name) {
                            ulevel?.level?.forEach {
                                if (achievementLevel?.level == it?.level) {
                                    totalArchived += 1
                                }
                            }
                        }
                    }
                }
                tvPercentage.text = "${((totalArchived.toDouble()/data.level?.size!!.toDouble())*100).toInt()}% sudah berhasil diselesaikan!"

                Log.d("TAG_DATA_ACHIEVEMENT_PERCENTAGE", ((totalArchived.toDouble()/data.level.size.toDouble())*100).toString())
                Log.d("TAG_DATA_ACHIEVEMENT_GET", totalArchived.toString())
                Log.d("TAG_DATA_ACHIEVEMENT_TOTAL", data.level.size.toString())
                lavIcon.setAnimation("thropy-inactive.json")
                when(data.level.size) {
                    5 -> {
                        lavBadgeOne.setAnimation("badge-achievement-inactive.json")
                        lavBadgeTwo.setAnimation("badge-achievement-inactive.json")
                        lavBadgeThree.setAnimation("badge-achievement-inactive.json")
                        lavBadgeFour.setAnimation("badge-achievement-inactive.json")
                        lavBadgeFive.setAnimation("badge-achievement-inactive.json")
                    }
                    4 -> {
                        lavBadgeOne.setAnimation("badge-achievement-inactive.json")
                        lavBadgeTwo.setAnimation("badge-achievement-inactive.json")
                        lavBadgeThree.setAnimation("badge-achievement-inactive.json")
                        lavBadgeFour.setAnimation("badge-achievement-inactive.json")
                        lavBadgeFive.setAnimation("invisible.json")
                    }
                    3 -> {
                        lavBadgeOne.setAnimation("badge-achievement-inactive.json")
                        lavBadgeTwo.setAnimation("badge-achievement-inactive.json")
                        lavBadgeThree.setAnimation("badge-achievement-inactive.json")
                        lavBadgeFour.setAnimation("invisible.json")
                        lavBadgeFive.setAnimation("invisible.json")
                    }
                    2 -> {
                        lavBadgeOne.setAnimation("badge-achievement-inactive.json")
                        lavBadgeTwo.setAnimation("badge-achievement-inactive.json")
                        lavBadgeThree.setAnimation("invisible.json")
                        lavBadgeFour.setAnimation("invisible.json")
                        lavBadgeFive.setAnimation("invisible.json")
                    }
                    1 -> {
                        lavBadgeOne.setAnimation("badge-achievement-inactive.json")
                        lavBadgeTwo.setAnimation("invisible.json")
                        lavBadgeThree.setAnimation("invisible.json")
                        lavBadgeFour.setAnimation("invisible.json")
                        lavBadgeFive.setAnimation("invisible.json")
                    }
                }
                when (totalArchived) {
                    5 -> {
                        lavIcon.setAnimation("thropy-active.json")
                        lavBadgeOne.setAnimation("badge-achievement-active.json")
                        lavBadgeTwo.setAnimation("badge-achievement-active.json")
                        lavBadgeThree.setAnimation("badge-achievement-active.json")
                        lavBadgeFour.setAnimation("badge-achievement-active.json")
                        lavBadgeFive.setAnimation("badge-achievement-active.json")
                    }
                    4 -> {
                        lavBadgeOne.setAnimation("badge-achievement-active.json")
                        lavBadgeTwo.setAnimation("badge-achievement-active.json")
                        lavBadgeThree.setAnimation("badge-achievement-active.json")
                        lavBadgeFour.setAnimation("badge-achievement-active.json")
                    }
                    3 -> {
                        lavBadgeOne.setAnimation("badge-achievement-active.json")
                        lavBadgeTwo.setAnimation("badge-achievement-active.json")
                        lavBadgeThree.setAnimation("badge-achievement-active.json")
                    }
                    2 -> {
                        lavBadgeOne.setAnimation("badge-achievement-active.json")
                        lavBadgeTwo.setAnimation("badge-achievement-active.json")
                    }
                    1 -> {
                        lavBadgeOne.setAnimation("badge-achievement-active.json")
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