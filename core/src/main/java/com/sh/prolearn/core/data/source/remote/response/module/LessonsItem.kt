package com.sh.prolearn.core.data.source.remote.response.module

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LessonsItem(

	@field:SerializedName("quiz")
	val quiz: List<QuizItem?>? = null,

	@field:SerializedName("summary")
	val summary: Summary? = null,

	@field:SerializedName("requirements")
	val requirements: Requirements? = null,

	@field:SerializedName("level")
	val level: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("enableSkip")
	val enableSkip: Boolean? = null,

	@field:SerializedName("exp")
	val exp: Int? = null,

	@field:SerializedName("theory")
	val theory: List<TheoryItem?>? = null,

	@field:SerializedName("option")
	val option: Option? = null,

	@field:SerializedName("order")
	val order: Int? = null
) : Parcelable