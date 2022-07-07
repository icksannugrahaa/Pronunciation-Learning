package com.sh.prolearn.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AchievementLevel(
	val image: String? = null,
	val name: String? = null,
	val status: Boolean? = null
) : Parcelable