package com.sh.prolearn.core.data.source.remote.response.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccountData(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("phoneNumber")
	val phoneNumber: String? = null,

    @field:SerializedName("gender")
	val gender: String? = null,

    @field:SerializedName("achievement")
	val achievement: List<AchievementItem?>? = null,

    @field:SerializedName("level")
	val level: Int? = null,

    @field:SerializedName("biodata")
	val biodata: String? = null,

    @field:SerializedName("name")
	val name: String? = null,

    @field:SerializedName("googleSignIn")
	val googleSignIn: Boolean? = null,

    @field:SerializedName("avatar")
	val avatar: String? = null,

    @field:SerializedName("exp")
	val exp: Int? = null,

    @field:SerializedName("email")
	val email: String? = null,

    @field:SerializedName("status")
	val status: Boolean? = null
) : Parcelable