package com.sh.prolearn.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Theory(
	val image: String? = null,
	val question: String? = null,
	val title: String? = null,
	val order: Int? = null
) : Parcelable