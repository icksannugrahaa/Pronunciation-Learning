package com.sh.prolearn.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Module(
    val requirement: Requirement? = null,
    val comments: List<Comment?>? = null,
    val level: Int? = null,
    val rating: Int? = null,
    val description: String? = null,
    val required: Boolean? = null,
    val highscore: Int? = null,
    val name: String? = null,
    val image: String? = null,
    val id: String? = null,
    val lessons: List<Lesson?>? = null,
    val order: Int? = null,
    val status: Boolean? = null,
    val successful: Int? = null
) : Parcelable