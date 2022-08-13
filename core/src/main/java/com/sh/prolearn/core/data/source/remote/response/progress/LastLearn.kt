package com.sh.prolearn.core.data.source.remote.response.progress

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LastLearn(

	@field:SerializedName("lesson")
	val lesson: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable