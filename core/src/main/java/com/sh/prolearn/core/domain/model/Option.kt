package com.sh.prolearn.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Option(
	val quiz: Boolean? = null,
	val summary: Boolean? = null,
	val quizType: String? = null,
	val summaryType: String? = null,
	val theoryType: String? = null
) : Parcelable