package com.sh.prolearn.app.modules

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.domain.model.Module
import com.sh.prolearn.core.domain.model.ProgressData
import com.sh.prolearn.core.domain.model.ProgressStore
import com.sh.prolearn.core.domain.model.ScoreBoard
import com.sh.prolearn.core.domain.usecase.module.ModuleUseCase

class ModuleViewModel(private val moduleUseCase: ModuleUseCase) : ViewModel() {
    fun moduleIndex(search: String, find: String): LiveData<Resource<List<Module>>> =
        moduleUseCase.moduleIndex(search, find).asLiveData()

    fun moduleReview(token: String, lesson: String, comment: String, rating: String): LiveData<Resource<String>> =
        moduleUseCase.moduleReview(token, lesson, comment, rating).asLiveData()

    fun progressIndex(token: String): LiveData<Resource<ProgressData>> =
        moduleUseCase.progressIndex(token).asLiveData()

    fun scoreboardIndex(token: String, search: String, find: String): LiveData<Resource<List<ScoreBoard>>> =
    moduleUseCase.scoreboardIndex(token, search, find).asLiveData()

    fun progressStore(
        token: String,
        time: Int,
        score: Double,
        lesson: String,
        status: String,
        progress: String,
        quest: String,
        answer: String
    ): LiveData<Resource<ProgressStore>> =
        moduleUseCase.progressStore(token, time, score, lesson, status, progress, quest, answer).asLiveData()
}