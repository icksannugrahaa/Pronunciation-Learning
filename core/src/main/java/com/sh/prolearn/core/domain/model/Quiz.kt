package com.sh.prolearn.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Quiz(
	val image: String? = null,
	val answer: List<String>? = null,
	val question: String? = null,
	val title: String? = null,
	val order: Int? = null
) : Parcelable