package com.sh.prolearn.core.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Progress(
	val scores: Scores? = null,
	val lesson: String? = null,
	val progress: String? = null,
	val status: String? = null
) : Parcelable