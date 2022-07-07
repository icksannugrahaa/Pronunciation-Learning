package com.sh.prolearn.core.data.source.local.entity

//import androidx.room.ColumnInfo
//import androidx.room.Entity
//import androidx.room.PrimaryKey

//@Entity(tableName = "comments")
data class CommentEntities(
//	@PrimaryKey
//	@ColumnInfo(name = "id")
	val id: String? = null,
//	@ColumnInfo(name = "name")
	val name: String? = null,
//	@ColumnInfo(name = "rating")
	val rating: Int? = null,
//	@ColumnInfo(name = "comment")
	val comment: String? = null
)