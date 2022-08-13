package com.sh.prolearn.core.data.source.remote.response.predict

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PredictData(

	@field:SerializedName("download")
	val download: String? = null,

	@field:SerializedName("view")
	val view: String? = null,

	@field:SerializedName("wrongScore")
	val wrongScore: Double? = null,

	@field:SerializedName("textQuest")
	val textQuest: String? = null,

	@field:SerializedName("outWordScore")
	val outWordScore: Double? = null,

	@field:SerializedName("right")
	val right: Int? = null,

	@field:SerializedName("textPredict")
	val textPredict: String? = null,

	@field:SerializedName("totalScore")
	val totalScore: Double? = null,

	@field:SerializedName("outWord")
	val outWord: Int? = null,

	@field:SerializedName("finalText")
	val finalText: String? = null,

	@field:SerializedName("rightScore")
	val rightScore: Double? = null,

	@field:SerializedName("wrong")
	val wrong: Int? = null
) : Parcelable