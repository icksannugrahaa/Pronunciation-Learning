package com.sh.prolearn.core.utils

import com.sh.prolearn.core.data.source.remote.response.achievement.LevelItem
import com.sh.prolearn.core.data.source.remote.response.auth.AccountData
import com.sh.prolearn.core.data.source.remote.response.auth.AchievementItem
import com.sh.prolearn.core.data.source.remote.response.file.ResponseUpload
import com.sh.prolearn.core.data.source.remote.response.module.*
import com.sh.prolearn.core.data.source.remote.response.module.Option
import com.sh.prolearn.core.data.source.remote.response.module.Summary
import com.sh.prolearn.core.data.source.remote.response.predict.PredictData
import com.sh.prolearn.core.data.source.remote.response.predict.ResponsePredict
import com.sh.prolearn.core.data.source.remote.response.predict.ResponseTTS
import com.sh.prolearn.core.data.source.remote.response.progress.*
import com.sh.prolearn.core.data.source.remote.response.progress.LastLearn
import com.sh.prolearn.core.data.source.remote.response.progress.ProgressData
import com.sh.prolearn.core.data.source.remote.response.progress.Scores
import com.sh.prolearn.core.domain.model.*

object DataMapper {
    //    MODULE MAPPER
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
                successful = it.successful,
                image = it.image
            )
        }

    fun mapScoreboardResponsesToDomain(input: List<ScoreBoardData>?): List<ScoreBoard> =
        input!!.map {
            ScoreBoard(
                id = it.id,
                name = it.name,
                avatar = it.avatar,
                score = it.score,
                lesson = it.lesson,
                time = it.time,
                email = it.email,
                createdAt = it.createdAt
            )
        }

    private fun mapModuleRequirementResponsesToDomain(input: Requirements?) = Requirement(
        level = input?.level,
        lesson = input?.lesson
    )

    private fun mapModuleCommentResponsesToDomain(input: List<CommentsItem?>?): List<Comment?>? =
        input?.map {
            Comment(
                name = it?.name,
                rating = it?.rating,
                comment = it?.comment,
                avatar = it?.avatar,
                created_at = it?.created_at
            )
        }

    private fun mapModuleLessonResponsesToDomain(input: List<LessonsItem?>?): List<Lesson?>? =
        input?.map {
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
        input?.map {
            Quiz(
                order = it?.order,
                image = it?.image,
                answer = it?.answer,
                question = it?.question,
                title = it?.title
            )
        }

    private fun mapModuleOptionResponsesToDomain(input: Option?) =
        com.sh.prolearn.core.domain.model.Option(
            summary = input?.summary,
            quiz = input?.quiz,
            quizType = input?.quizType,
            summaryType = input?.summaryType,
            theoryType = input?.theoryType
        )

    private fun mapModuleSummaryResponsesToDomain(input: Summary?) =
        com.sh.prolearn.core.domain.model.Summary(
            image = input?.image,
            song = input?.song,
            text = input?.text,
            title = input?.title
        )

    private fun mapModuleTheoryResponsesToDomain(input: List<TheoryItem?>?): List<Theory?>? =
        input?.map {
            Theory(
                order = it?.order,
                image = it?.image,
                question = it?.question,
                title = it?.title
            )
        }

    //    ACCOUNT MAPPER
    fun mapAccountResponsesToDomain(input: AccountData?, token: String?) = Account(
        id = input?.id,
        phoneNumber = input?.phoneNumber,
        gender = input?.gender,
        achievement = mapAchievementResponsesToDomain(input?.achievement),
        level = input?.level,
        levelName = input?.levelName,
        biodata = input?.biodata,
        name = input?.name,
        googleSignIn = input?.googleSignIn,
        avatar = input?.avatar,
        exp = input?.exp,
        expNext = input?.expNext,
        email = input?.email,
        status = input?.status,
        token = token,
        createdAt = input?.createdAt,
        updatedAt = input?.updatedAt
    )

    fun mapAchievementResponsesToDomain(input: List<AchievementItem?>?): List<Achievement?>? =
        input?.map {
            Achievement(
                id = it?.id,
                name = it?.name,
                image = it?.image,
                level = mapAchievementLevelResponsesToDomain(it?.level)
            )
        }

    private fun mapAchievementLevelResponsesToDomain(input: List<LevelItem?>?): List<AchievementLevel?>? =
        input?.map {
            AchievementLevel(
                name = it?.name,
                level = it?.level,
                description = it?.description,
                exp = it?.exp,
                requirements = mapAchievementRequirementResponsesToDomain(it?.requirements)
            )
        }

    private fun mapAchievementRequirementResponsesToDomain(input: com.sh.prolearn.core.data.source.remote.response.achievement.Requirements?) =
        AchievementRequirement(
            level = input?.level,
            theory = input?.theory,
            quizz = input?.quizz,
            exp = input?.exp,
            lesson = input?.lesson,
            module = input?.module,
            levelName = input?.levelName
        )
    //        MAPPER PROGRESS
    fun mapProgressDataResponsesToDomain(input: ProgressData) =
        com.sh.prolearn.core.domain.model.ProgressData(
            lastLearn = mapLastLearnResponsesToDomain(input.lastLearn),
            id = input.id,
            email = input.email,
            allProgress = mapAllProgressResponsesToDomain(input.allProgress)
        )

    private fun mapLastLearnResponsesToDomain(input: LastLearn?) =
        com.sh.prolearn.core.domain.model.LastLearn(
            lesson = input?.lesson,
            status = input?.status
        )

    private fun mapAllProgressResponsesToDomain(input: List<AllProgressItem?>?): List<AllProgress>? =
        input?.map {
            AllProgress(
                status = it?.status,
                currentProgress = it?.currentProgress,
                progress = mapProgressResponsesToDomain(it?.progress),
                order = it?.order
            )
        }

    private fun mapProgressResponsesToDomain(input: List<ProgressItem?>?): List<Progress>? =
        input?.map {
            Progress(
                status = it?.status,
                scores = mapScoreResponsesToDomain(it?.scores),
                lesson = it?.lesson,
                progress = it?.progress
            )
        }

    private fun mapScoreResponsesToDomain(input: Scores?) =
        com.sh.prolearn.core.domain.model.Scores(
            score = input?.score,
            time = input?.time,
            answer = input?.answer,
            quest = input?.quest
        )

    fun mapProgressStoreDataResponsesToDomain(input: ResponseProgressStore) =
        ProgressStore(
            levelUp = input.levelUp,
            newAchievement = input.newAchievement,
            newAchievementMsg = input.newAchievementMsg,
            status = input.status
        )

    //    FILE MAPPER
    fun mapUploadResponsesToDomain(param: ResponseUpload): Upload =
        Upload(
            download = param.download,
            view = param.view,
            status = param.status
        )

    //    PREDICT MAPPER
    fun mapPredictResponsesToDomain(param: PredictData): Predict =
        Predict(
            download = param.download,
            view = param.view,
            wrong = param.wrong,
            wrongScore = param.wrongScore,
            textQuest = param.textQuest,
            textPredict = param.textPredict,
            finalText = param.finalText,
            outWord = param.outWord,
            outWordScore = param.outWordScore,
            right = param.right,
            rightScore = param.rightScore,
            totalScore = param.totalScore
        )

    fun mapPredictTTSResponsesToDomain(param: ResponseTTS): TextToSpeech =
        TextToSpeech(
            view = param.view,
            status = param.status
        )
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