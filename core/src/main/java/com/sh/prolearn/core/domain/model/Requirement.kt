package com.sh.prolearn.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Requirement(
	val level: Int? = null,
	val lesson: String? = null
) : Parcelable