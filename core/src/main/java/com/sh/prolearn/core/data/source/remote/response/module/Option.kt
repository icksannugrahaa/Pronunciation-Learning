package com.sh.prolearn.core.data.source.remote.response.module

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Option(

	@field:SerializedName("quiz")
	val quiz: Boolean? = null,

	@field:SerializedName("summary")
	val summary: Boolean? = null,

	@field:SerializedName("quiz_type")
	val quizType: String? = null,

	@field:SerializedName("summary_type")
	val summaryType: String? = null,

	@field:SerializedName("theory_type")
	val theoryType: String? = null
) : Parcelable