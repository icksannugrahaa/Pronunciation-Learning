package com.sh.prolearn.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProgressStore(
    val levelUp: Boolean? = null,
    val newAchievement: Boolean? = null,
    val newAchievementMsg: String? = null,
    val message: String? = null,
    val status: String? = null
) : Parcelable