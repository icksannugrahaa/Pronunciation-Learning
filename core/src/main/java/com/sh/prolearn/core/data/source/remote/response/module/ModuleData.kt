package com.sh.prolearn.core.data.source.remote.response.module

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ModuleData(

	@field:SerializedName("requirements")
	val requirements: Requirements? = null,

	@field:SerializedName("comments")
	val comments: List<CommentsItem?>? = null,

	@field:SerializedName("level")
	val level: Int? = null,

	@field:SerializedName("rating")
	val rating: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("required")
	val required: Boolean? = null,

	@field:SerializedName("highscore")
	val highscore: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("lessons")
	val lessons: List<LessonsItem?>? = null,

	@field:SerializedName("order")
	val order: Int? = null,

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("successful")
	val successful: Int? = null
) : Parcelable