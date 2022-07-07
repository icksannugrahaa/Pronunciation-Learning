package com.sh.prolearn.core.data.source.remote.response.module

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Summary(

	@field:SerializedName("song")
	val song: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("text")
	val text: String? = null,

	@field:SerializedName("title")
	val title: String? = null
) : Parcelable