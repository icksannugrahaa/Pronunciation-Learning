package com.sh.prolearn.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AllProgress(
	val currentProgress: String? = null,
	val progress: List<Progress?>? = null,
	val order: Int? = null,
	val status: String? = null
) : Parcelable