package com.sh.prolearn.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Summary(
	val song: String? = null,
	val image: String? = null,
	val text: String? = null,
	val title: String? = null
) : Parcelable