package com.sh.prolearn.core.data.source.remote.response.achievement

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.sh.prolearn.core.data.source.remote.response.auth.AchievementItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseAchievement(

	@field:SerializedName("data")
	val data: List<AchievementItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable