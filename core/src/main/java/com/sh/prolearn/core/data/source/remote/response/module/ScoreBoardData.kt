package com.sh.prolearn.core.data.source.remote.response.module

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScoreBoardData(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("lesson")
	val lesson: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("avatar")
	val avatar: String? = null,

	@field:SerializedName("time")
	val time: Int? = null,

	@field:SerializedName("email")
	val email: String? = null
) : Parcelable