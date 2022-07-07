package com.sh.prolearn.core.data.source.remote.response.module

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TheoryItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("question")
	val question: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("order")
	val order: Int? = null
) : Parcelable