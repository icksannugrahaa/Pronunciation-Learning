package com.sh.prolearn.core.data.repository

import com.sh.prolearn.core.data.NetworkOnlyResource
import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.data.source.remote.RemoteDataSource
import com.sh.prolearn.core.data.source.remote.network.ApiResponse
import com.sh.prolearn.core.data.source.remote.response.account.ResponseAccount
import com.sh.prolearn.core.data.source.remote.response.module.ResponseModules
import com.sh.prolearn.core.domain.model.Account
import com.sh.prolearn.core.domain.model.Module
import com.sh.prolearn.core.domain.repository.IModuleRepository
import com.sh.prolearn.core.utils.Consts
import com.sh.prolearn.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ModuleRepository(
    private val remoteDataSource: RemoteDataSource
) : IModuleRepository {
    override fun moduleIndex(search: String): Flow<Resource<List<Module>>> =
        object: NetworkOnlyResource<List<Module>, ResponseModules>() {
            override suspend fun createCall(): Flow<ApiResponse<ResponseModules>> = remoteDataSource.modulesIndex(search)
            override fun transformData(param: ResponseModules): Flow<Resource<List<Module>>> = flow {
                emit(Resource.Success(DataMapper.mapModuleResponsesToDomain(param.data), param.message ?: Consts.MESSAGE_500))
            }
        }.asFlow()
}