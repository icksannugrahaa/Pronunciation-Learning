package com.sh.prolearn.core.domain.usecase.module

import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.domain.model.Module
import com.sh.prolearn.core.domain.model.ProgressData
import com.sh.prolearn.core.domain.model.ProgressStore
import com.sh.prolearn.core.domain.model.ScoreBoard
import kotlinx.coroutines.flow.Flow

interface ModuleUseCase {
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
