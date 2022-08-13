package com.sh.prolearn.core.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Scores(
	val score: Double? = null,
	val quest: String? = null,
	val answer: String? = null,
	val time: Int? = null
) : Parcelable