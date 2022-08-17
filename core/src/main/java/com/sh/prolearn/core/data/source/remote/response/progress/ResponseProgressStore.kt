package com.sh.prolearn.core.data.source.remote.response.progress

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseProgressStore(

	@field:SerializedName("levelUp")
	val levelUp: Boolean? = null,

	@field:SerializedName("newAchievement")
	val newAchievement: Boolean? = null,

	@field:SerializedName("newAchievementMsg")
	val newAchievementMsg: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable