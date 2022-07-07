package com.sh.prolearn.core.domain.repository

import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.domain.model.Account
import com.sh.prolearn.core.domain.model.Module
import kotlinx.coroutines.flow.Flow

interface IModuleRepository {
    fun moduleIndex(search: String): Flow<Resource<List<Module>>>
}