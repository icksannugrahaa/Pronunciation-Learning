package com.sh.prolearn.core.domain.model

import android.os.Parcelable
import com.sh.prolearn.core.data.source.remote.response.achievement.LevelItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class Achievement(
    val image: String? = null,
    val level: List<AchievementLevel?>? = null,
    val name: String? = null,
    val id: String? = null
) : Parcelable