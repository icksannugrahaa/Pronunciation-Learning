package com.sh.prolearn.core.domain.repository

import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.domain.model.*
import kotlinx.coroutines.flow.Flow

interface IModuleRepository {
    fun moduleIndex(search: String, find: String): Flow<Resource<List<Module>>>
    fun moduleReview(token: String, lesson: String, comment: String, rating: String): Flow<Resource<String>>
    fun scoreboardIndex(
        token: String,
        search: String,
        find: String
    ): Flow<Resource<List<ScoreBoard>>>

    fun progressIndex(token: String): Flow<Resource<ProgressData>>
    fun progressStore(
        token: String,
        time: Int,
        score: Double,
        lesson: String,
        status: String,
        progress: String,
        quest: String,
        answer: String
    ): Flow<Resource<ProgressStore>>
}