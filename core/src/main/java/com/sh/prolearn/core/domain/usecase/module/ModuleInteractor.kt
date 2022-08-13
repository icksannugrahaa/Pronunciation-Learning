package com.sh.prolearn.core.domain.usecase.module

import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.domain.model.Module
import com.sh.prolearn.core.domain.model.ProgressData
import com.sh.prolearn.core.domain.model.ProgressStore
import com.sh.prolearn.core.domain.model.ScoreBoard
import com.sh.prolearn.core.domain.repository.IModuleRepository
import kotlinx.coroutines.flow.Flow

class ModuleInteractor(private val moduleRepository: IModuleRepository) : ModuleUseCase {
    override fun moduleIndex(search: String, find: String): Flow<Resource<List<Module>>> = moduleRepository.moduleIndex(search, find)
    override fun moduleReview(token: String, lesson: String, comment: String, rating: String): Flow<Resource<String>> = moduleRepository.moduleReview(token, lesson, comment, rating)
    override fun scoreboardIndex(token: String, search: String, find: String): Flow<Resource<List<ScoreBoard>>> = moduleRepository.scoreboardIndex(token, search, find)
    override fun progressIndex(token: String): Flow<Resource<ProgressData>> = moduleRepository.progressIndex(token)
    override fun progressStore(
        token: String,
        time: Int,
        score: Double,
        lesson: String,
        status: String,
        progress: String,
        quest: String,
        answer: String
    ): Flow<Resource<ProgressStore>> = moduleRepository.progressStore(token, time, score, lesson, status, progress, quest, answer)
}