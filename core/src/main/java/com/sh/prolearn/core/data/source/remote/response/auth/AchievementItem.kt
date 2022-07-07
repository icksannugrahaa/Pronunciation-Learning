package com.sh.prolearn.core.data.source.remote.response.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AchievementItem(

    @field:SerializedName("image")
	val image: String? = null,

    @field:SerializedName("level")
	val level: List<LevelItem?>? = null,

    @field:SerializedName("name")
	val name: String? = null,

    @field:SerializedName("description")
	val description: String? = null
) : Parcelable