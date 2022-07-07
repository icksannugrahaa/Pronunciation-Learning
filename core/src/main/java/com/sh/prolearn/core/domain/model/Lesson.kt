package com.sh.prolearn.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lesson(
    val quiz: List<com.sh.prolearn.core.domain.model.Quiz?>? = null,
    val summary: com.sh.prolearn.core.domain.model.Summary? = null,
    val requirements: com.sh.prolearn.core.domain.model.Requirement? = null,
    val level: Int? = null,
    val name: String? = null,
    val description: String? = null,
    val enableSkip: Boolean? = null,
    val exp: Int? = null,
    val theory: List<com.sh.prolearn.core.domain.model.Theory?>? = null,
    val option: com.sh.prolearn.core.domain.model.Option? = null,
    val order: Int? = null,
    val status: Boolean? = null
) : Parcelable