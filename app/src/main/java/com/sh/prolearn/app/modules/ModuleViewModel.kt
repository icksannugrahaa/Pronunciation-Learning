package com.sh.prolearn.app.modules

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.domain.model.Module
import com.sh.prolearn.core.domain.usecase.module.ModuleUseCase

class ModuleViewModel(private val moduleUseCase: ModuleUseCase) : ViewModel() {
    fun moduleIndex(search: String): LiveData<Resource<List<Module>>> = moduleUseCase.moduleIndex(search).asLiveData()
//    val allPresence: LiveData<Resource<List<Presence>>> = scheduleUseCase.getPresenceAll().asLiveData()
}