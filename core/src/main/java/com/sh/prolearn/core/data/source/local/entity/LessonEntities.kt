package com.sh.prolearn.core.data.source.local.entity

//import androidx.room.ColumnInfo
//import androidx.room.Entity
//import androidx.room.PrimaryKey

//@Entity(tableName = "lessons")
data class LessonEntities(
//    @PrimaryKey
//    @ColumnInfo(name = "id")
    val id: String? = null,
//    @ColumnInfo(name = "quiz_id")
    val quizId: Int? = null,
//    @ColumnInfo(name = "summary_id")
    val summaryId: Int? = null,
//    @ColumnInfo(name = "requirement_id")
    val requirementId: Int? = null,
//    @ColumnInfo(name = "level")
    val level: Int? = null,
//    @ColumnInfo(name = "name")
    val name: String? = null,
//    @ColumnInfo(name = "description")
    val description: String? = null,
//    @ColumnInfo(name = "enableSkip")
    val enableSkip: Boolean? = null,
//    @ColumnInfo(name = "exp")
    val exp: Int? = null,
//    @ColumnInfo(name = "theory_id")
    val theoryId: Int? = null,
//    @ColumnInfo(name = "option_id")
    val optionId: OptionEntities? = null,
//    @ColumnInfo(name = "order")
    val order: Int? = null
)