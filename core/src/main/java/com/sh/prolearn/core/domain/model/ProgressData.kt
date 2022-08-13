package com.sh.prolearn.core.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProgressData(
	val lastLearn: LastLearn? = null,
	val id: String? = null,
	val email: String? = null,
	val allProgress: List<AllProgress?>? = null
) : Parcelable