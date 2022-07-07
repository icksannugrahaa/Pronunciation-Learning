package com.sh.prolearn.core.data.source.remote.response.module

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Requirements(

	@field:SerializedName("level")
	val level: Int? = null,

	@field:SerializedName("lesson")
	val lesson: String? = null
) : Parcelable