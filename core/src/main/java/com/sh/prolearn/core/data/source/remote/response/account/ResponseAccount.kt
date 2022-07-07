package com.sh.prolearn.core.data.source.remote.response.account

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.sh.prolearn.core.data.source.remote.response.auth.AccountData
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseAccount(

	@field:SerializedName("data")
	val data: AccountData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable