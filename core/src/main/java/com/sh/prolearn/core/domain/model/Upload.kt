package com.sh.prolearn.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Upload(
    val download: String? = null,
    val view: String? = null,
    val status: Boolean? = null
) : Parcelable