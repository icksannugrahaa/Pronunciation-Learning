package com.sh.prolearn.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comment(
	val name: String? = null,
	val rating: Int? = null,
	val comment: String? = null
) : Parcelable