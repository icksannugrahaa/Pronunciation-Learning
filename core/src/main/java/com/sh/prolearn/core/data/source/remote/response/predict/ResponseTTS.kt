package com.sh.prolearn.core.data.source.remote.response.predict

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseTTS(

	@field:SerializedName("view")
	val view: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("error")
	val error: String? = null,

	@field:SerializedName("status")
	val status: String? = null

) : Parcelable