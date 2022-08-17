package com.sh.prolearn.core.data.source.remote.response.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.sh.prolearn.core.data.source.remote.response.achievement.LevelItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class AchievementItem(

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("level")
    val level: List<LevelItem?>? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("_id")
    val id: String? = null

) : Parcelable