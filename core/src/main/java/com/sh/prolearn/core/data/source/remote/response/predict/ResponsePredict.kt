package com.sh.prolearn.core.data.source.remote.response.predict

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponsePredict(

	@field:SerializedName("data")
	val data: PredictData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable