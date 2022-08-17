package com.sh.prolearn.core.data.source.remote.response.achievement

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LevelItem(

	@field:SerializedName("requirements")
	val requirements: Requirements? = null,

	@field:SerializedName("level")
	val level: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("exp")
	val exp: Int? = null
) : Parcelable