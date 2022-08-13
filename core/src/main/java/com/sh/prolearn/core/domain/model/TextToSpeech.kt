package com.sh.prolearn.core.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TextToSpeech(
	val view: String? = null,
	val message: String? = null,
	val error: String? = null,
	val status: String? = null
) : Parcelable