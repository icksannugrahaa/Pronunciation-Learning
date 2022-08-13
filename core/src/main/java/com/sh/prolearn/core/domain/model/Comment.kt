package com.sh.prolearn.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comment(
	val name: String? = null,
	val rating: Double? = null,
	val comment: String? = null,
	val created_at: String? = null,
	val avatar: String? = null
) : Parcelable