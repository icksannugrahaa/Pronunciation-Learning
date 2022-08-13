package com.sh.prolearn.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Account(
    val id: String? = null,
    val phoneNumber: String? = null,
    val gender: String? = null,
    val achievement: List<Achievement?>? = null,
    val level: Int? = null,
    val levelName: String? = null,
    val biodata: String? = null,
    val name: String? = null,
    val googleSignIn: Boolean? = null,
    val avatar: String? = null,
    val exp: Int? = null,
    val expNext: Int? = null,
    val email: String? = null,
    val status: Boolean? = null,
    val token: String? = null,
) : Parcelable