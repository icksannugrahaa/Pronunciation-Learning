package com.sh.prolearn.core.data.source.local.entity

//import androidx.room.ColumnInfo
//import androidx.room.Entity
//import androidx.room.PrimaryKey
//
//@Entity(tableName = "summary")
data class Summary(
//	@PrimaryKey
//	@ColumnInfo(name = "id")
	val id: String? = null,
//	@ColumnInfo(name = "song")
	val song: String? = null,
//	@ColumnInfo(name = "image")
	val image: String? = null,
//	@ColumnInfo(name = "text")
	val text: String? = null,
//	@ColumnInfo(name = "title")
	val title: String? = null
)