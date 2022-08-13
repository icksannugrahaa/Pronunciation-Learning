package com.sh.prolearn.core.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Predict(
    val download: String? = null,
    val view: String? = null,
    val wrongScore: Double? = null,
    val textQuest: String? = null,
    val outWordScore: Double? = null,
    val right: Int? = null,
    val textPredict: String? = null,
    val totalScore: Double? = null,
    val outWord: Int? = null,
    var finalText: String? = null,
    val rightScore: Double? = null,
    val wrong: Int? = null
) : Parcelable