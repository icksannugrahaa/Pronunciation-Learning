package com.sh.prolearn.core.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.sh.prolearn.core.data.source.remote.response.achievement.Requirements
import kotlinx.parcelize.Parcelize

@Parcelize
data class AchievementLevel(
	val requirements: AchievementRequirement? = null,
	val level: Int? = null,
	val name: String? = null,
	val description: String? = null,
	val exp: Int? = null
) : Parcelable