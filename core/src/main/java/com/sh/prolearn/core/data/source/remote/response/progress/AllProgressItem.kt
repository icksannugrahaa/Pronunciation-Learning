package com.sh.prolearn.core.data.source.remote.response.progress

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AllProgressItem(

	@field:SerializedName("currentProgress")
	val currentProgress: String? = null,

	@field:SerializedName("progress")
	val progress: List<ProgressItem?>? = null,

	@field:SerializedName("order")
	val order: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable