package com.sh.prolearn.core.data.source.local.entity

//import androidx.room.ColumnInfo
//import androidx.room.Entity
//import androidx.room.PrimaryKey

//@Entity(tableName = "modules")
data class ModuleEntities(
//	@PrimaryKey
//	@ColumnInfo(name = "id")
	val id: String? = null,
//	@ColumnInfo(name = "requirement_id")
	val requirementId: Int? = null,
//	@ColumnInfo(name = "comment_id")
	val commentId: Int? = null,
//	@ColumnInfo(name = "level")
	val level: Int? = null,
//	@ColumnInfo(name = "rating")
	val rating: Int? = null,
//	@ColumnInfo(name = "description")
	val description: String? = null,
//	@ColumnInfo(name = "required")
	val required: Boolean? = null,
//	@ColumnInfo(name = "highscore")
	val highscore: Int? = null,
//	@ColumnInfo(name = "name")
	val name: String? = null,
//	@ColumnInfo(name = "lesson_id")
	val lessonId: Int? = null,
//	@ColumnInfo(name = "order")
	val order: Int? = null,
//	@ColumnInfo(name = "status")
	val status: Boolean? = null,
//	@ColumnInfo(name = "successful")
	val successful: Int? = null
)