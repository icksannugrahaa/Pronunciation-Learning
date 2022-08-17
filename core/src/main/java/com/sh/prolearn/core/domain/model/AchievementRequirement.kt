package com.sh.prolearn.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AchievementRequirement(
    val theory: Int? = null,
    val quizz: Int? = null,
    val level: Int? = null,
    val levelName: String? = null,
    val exp: Int? = null,
    val lesson: Int? = null,
    val module: Int? = null
) : Parcelable
