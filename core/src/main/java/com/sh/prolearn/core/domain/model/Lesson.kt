package com.sh.prolearn.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lesson(
    val quiz: List<Quiz?>? = null,
    val summary: Summary? = null,
    val requirements: Requirement? = null,
    val level: Int? = null,
    val name: String? = null,
    val description: String? = null,
    val enableSkip: Boolean? = null,
    val exp: Int? = null,
    val theory: List<Theory?>? = null,
    val option: Option? = null,
    val order: Int? = null,
    val status: Boolean? = null
) : Parcelable