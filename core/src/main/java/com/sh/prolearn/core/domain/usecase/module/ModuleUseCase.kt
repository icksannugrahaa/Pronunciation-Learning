package com.sh.prolearn.core.domain.usecase.module

import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.domain.model.Account
import com.sh.prolearn.core.domain.model.Module
import kotlinx.coroutines.flow.Flow

interface ModuleUseCase {
    fun moduleIndex(search: String): Flow<Resource<List<Module>>>
}
