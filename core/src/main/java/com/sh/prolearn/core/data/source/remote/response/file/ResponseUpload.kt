package com.sh.prolearn.core.data.source.remote.response.file

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseUpload(

	@field:SerializedName("download")
	val download: String? = null,

	@field:SerializedName("view")
	val view: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
) : Parcelable
