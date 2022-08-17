package com.sh.prolearn.core.data.source.remote.response.achievement

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Requirements(

	@field:SerializedName("theory")
	val theory: Int? = null,

	@field:SerializedName("quizz")
	val quizz: Int? = null,

	@field:SerializedName("level")
	val level: Int? = null,

	@field:SerializedName("levelName")
	val levelName: String? = null,

	@field:SerializedName("exp")
	val exp: Int? = null,

	@field:SerializedName("lesson")
	val lesson: Int? = null,

	@field:SerializedName("module")
	val module: Int? = null
) : Parcelable