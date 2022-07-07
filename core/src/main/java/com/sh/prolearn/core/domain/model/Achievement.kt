package com.sh.prolearn.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Achievement(
    val image: String? = null,
    val level: List<AchievementLevel?>? = null,
    val name: String? = null,
    val description: String? = null
) : Parcelable