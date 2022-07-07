package com.sh.prolearn.core.domain.usecase.module

import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.domain.model.Account
import com.sh.prolearn.core.domain.model.Module
import com.sh.prolearn.core.domain.repository.IAccountRepository
import com.sh.prolearn.core.domain.repository.IModuleRepository
import kotlinx.coroutines.flow.Flow

class ModuleInteractor(private val moduleRepository: IModuleRepository) : ModuleUseCase {
    override fun moduleIndex(search: String): Flow<Resource<List<Module>>> = moduleRepository.moduleIndex(search)
}