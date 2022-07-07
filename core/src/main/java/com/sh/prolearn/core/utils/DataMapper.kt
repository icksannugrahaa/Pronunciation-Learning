package com.sh.prolearn.core.utils

import com.sh.prolearn.core.data.source.remote.response.auth.AccountData
import com.sh.prolearn.core.data.source.remote.response.auth.AchievementItem
import com.sh.prolearn.core.data.source.remote.response.auth.LevelItem
import com.sh.prolearn.core.data.source.remote.response.module.*
import com.sh.prolearn.core.data.source.remote.response.module.Option
import com.sh.prolearn.core.data.source.remote.response.module.Summary
import com.sh.prolearn.core.domain.model.*

object DataMapper {
    fun mapModuleResponsesToDomain(input: List<ModuleData>?): List<Module> =
        input!!.map {
            Module(
                id = it.id,
                requirement = mapModuleRequirementResponsesToDomain(it.requirements),
                comments = mapModuleCommentResponsesToDomain(it.comments),
                status = it.status,
                description = it.description,
                level = it.level,
                name = it.name,
                highscore = it.highscore,
                lessons = mapModuleLessonResponsesToDomain(it.lessons),
                order = it.order,
                rating = it.rating,
                required = it.required,
                successful = it.successful
            )
        }

    private fun mapModuleRequirementResponsesToDomain(input: Requirements?) = Requirement(
        level = input?.level,
        lesson = input?.lesson
    )

    private fun mapModuleCommentResponsesToDomain(input: List<CommentsItem?>?): List<Comment?>? =
        input?.map{
            Comment(
                name = it?.name,
                rating = it?.rating,
                comment = it?.comment
            )
        }

    private fun mapModuleLessonResponsesToDomain(input: List<LessonsItem?>?): List<Lesson?>? =
        input?.map{
            Lesson(
                name = it?.name,
                quiz = mapModuleQuizResponsesToDomain(it?.quiz),
                level = it?.level,
                order = it?.order,
                description = it?.description,
                exp = it?.exp,
                enableSkip = it?.enableSkip,
                option = mapModuleOptionResponsesToDomain(it?.option),
                requirements = mapModuleRequirementResponsesToDomain(it?.requirements),
                summary = mapModuleSummaryResponsesToDomain(it?.summary),
                theory = mapModuleTheoryResponsesToDomain(it?.theory)
            )
        }

    private fun mapModuleQuizResponsesToDomain(input: List<QuizItem?>?): List<Quiz?>? =
        input?.map{
            Quiz(
                order = it?.order,
                image = it?.image,
                answer = it?.answer,
                question = it?.question,
                title = it?.title
            )
        }

    private fun mapModuleOptionResponsesToDomain(input: Option?) = com.sh.prolearn.core.domain.model.Option(
        summary = input?.summary,
        quiz = input?.quiz,
        quizType = input?.quizType,
        summaryType = input?.summaryType,
        theoryType = input?.theoryType
    )

    private fun mapModuleSummaryResponsesToDomain(input: Summary?) = com.sh.prolearn.core.domain.model.Summary(
        image = input?.image,
        song = input?.song,
        text = input?.text,
        title = input?.title
    )

    private fun mapModuleTheoryResponsesToDomain(input: List<TheoryItem?>?): List<Theory?>? =
        input?.map{
            Theory(
                order = it?.order,
                image = it?.image,
                question = it?.question,
                title = it?.title
            )
        }

    fun mapAccountResponsesToDomain(input: AccountData?, token: String?) = Account(
        id = input?.id,
        phoneNumber = input?.phoneNumber,
        gender = input?.gender,
        achievement = mapAchievementResponsesToDomain(input?.achievement),
        level = input?.level,
        biodata = input?.biodata,
        name = input?.name,
        googleSignIn = input?.googleSignIn,
        avatar = input?.avatar,
        exp = input?.exp,
        email = input?.email,
        status = input?.status,
        token = token
    )

    private fun mapAchievementResponsesToDomain(input: List<AchievementItem?>?): List<Achievement?>? =
        input?.map {
            Achievement(
                name = it?.name,
                image = it?.image,
                level = mapAchievementLevelResponsesToDomain(it?.level),
                description = it?.description
            )
        }

    private fun mapAchievementLevelResponsesToDomain(input: List<LevelItem?>?): List<AchievementLevel?>? =
        input?.map{
            AchievementLevel(
                name = it?.name,
                image = it?.image,
                status = it?.status
            )
        }
//    fun mapDomainToEntity(input: Tourism) = TourismEntity(
//        tourismId = input.tourismId,
//        description = input.description,
//        name = input.name,
//        address = input.address,
//        latitude = input.latitude,
//        longitude = input.longitude,
//        like = input.like,
//        image = input.image,
//        isFavorite = input.isFavorite
//    )
}