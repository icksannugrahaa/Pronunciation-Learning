package com.sh.prolearn.core.data.source.local.entity

//import androidx.room.ColumnInfo
//import androidx.room.Entity
//import androidx.room.PrimaryKey

//@Entity(tableName = "options")
data class OptionEntities(
//	@PrimaryKey
//	@ColumnInfo(name = "id")
	val id: String? = null,
//	@ColumnInfo(name = "quiz")
	val quiz: Boolean? = null,
//	@ColumnInfo(name = "summary")
	val summary: Boolean? = null,
//	@ColumnInfo(name = "quizType")
	val quizType: String? = null,
//	@ColumnInfo(name = "summaryType")
	val summaryType: String? = null,
//	@ColumnInfo(name = "theoryType")
	val theoryType: String? = null
)