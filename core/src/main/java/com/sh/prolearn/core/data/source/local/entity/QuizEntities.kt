package com.sh.prolearn.core.data.source.local.entity

//import androidx.room.ColumnInfo
//import androidx.room.Entity
//import androidx.room.PrimaryKey

//@Entity(tableName = "quiz")
data class QuizEntities(
//	@PrimaryKey
//	@ColumnInfo(name = "id")
	val id: String? = null,
//	@ColumnInfo(name = "image")
	val image: String? = null,
//	@ColumnInfo(name = "answer")
	val answer: String? = null,
//	@ColumnInfo(name = "question")
	val question: String? = null,
//	@ColumnInfo(name = "title")
	val title: String? = null,
//	@ColumnInfo(name = "order")
	val order: Int? = null
)