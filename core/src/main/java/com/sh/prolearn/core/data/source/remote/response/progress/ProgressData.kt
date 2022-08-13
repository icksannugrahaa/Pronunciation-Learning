package com.sh.prolearn.core.data.source.remote.response.progress

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProgressData(

	@field:SerializedName("lastLearn")
	val lastLearn: LastLearn? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("allProgress")
	val allProgress: List<AllProgressItem?>? = null
) : Parcelable