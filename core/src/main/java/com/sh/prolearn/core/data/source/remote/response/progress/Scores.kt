package com.sh.prolearn.core.data.source.remote.response.progress

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Scores(

	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("answer")
	val answer: String? = null,

	@field:SerializedName("quest")
	val quest: String? = null,

	@field:SerializedName("time")
	val time: Int? = null
) : Parcelable