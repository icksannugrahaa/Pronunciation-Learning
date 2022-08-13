package com.sh.prolearn.core.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScoreBoard(
	val createdAt: String? = null,
	val score: Double? = null,
	val lesson: String? = null,
	val name: String? = null,
	val id: String? = null,
	val avatar: String? = null,
	val time: Int? = null,
	val email: String? = null
) : Parcelable