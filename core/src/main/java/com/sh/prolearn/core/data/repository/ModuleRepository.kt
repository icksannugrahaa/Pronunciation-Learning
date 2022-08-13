package com.sh.prolearn.core.data.repository

import com.sh.prolearn.core.data.NetworkOnlyResource
import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.data.source.remote.RemoteDataSource
import com.sh.prolearn.core.data.source.remote.network.ApiResponse
import com.sh.prolearn.core.data.source.remote.response.account.ResponseAccount
import com.sh.prolearn.core.data.source.remote.response.general.ResponseDefault
import com.sh.prolearn.core.data.source.remote.response.module.ResponseModules
import com.sh.prolearn.core.data.source.remote.response.module.ResponseScoreBoard
import com.sh.prolearn.core.data.source.remote.response.module.ScoreBoardData
import com.sh.prolearn.core.data.source.remote.response.progress.ResponseProgress
import com.sh.prolearn.core.data.source.remote.response.progress.ResponseProgressStore
import com.sh.prolearn.core.domain.model.*
import com.sh.prolearn.core.domain.repository.IModuleRepository
import com.sh.prolearn.core.utils.Consts
import com.sh.prolearn.core.utils.Consts.MESSAGE_201
import com.sh.prolearn.core.utils.Consts.MESSAGE_500
import com.sh.prolearn.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ModuleRepository(
    private val remoteDataSource: RemoteDataSource
) : IModuleRepository {
    override fun moduleIndex(search: String, find: String): Flow<Resource<List<Module>>> =
        object : NetworkOnlyResource<List<Module>, ResponseModules>() {
            override suspend fun createCall(): Flow<ApiResponse<ResponseModules>> =
                remoteDataSource.modulesIndex(search, find)

            override fun transformData(param: ResponseModules): Flow<Resource<List<Module>>> =
                flow {
                    emit(
                        Resource.Success(
                            DataMapper.mapModuleResponsesToDomain(param.data),
                            param.message ?: Consts.MESSAGE_500
                        )
                    )
                }
        }.asFlow()

    override fun moduleReview(token: String, lesson: String, comment: String, rating: String): Flow<Resource<String>> =
        object : NetworkOnlyResource<String, ResponseDefault>() {
            override suspend fun createCall(): Flow<ApiResponse<ResponseDefault>> =
                remoteDataSource.moduleReview(token, lesson, comment, rating)

            override fun transformData(param: ResponseDefault): Flow<Resource<String>> =
                flow {
                    emit(
                        Resource.Success(
                            param.message ?: MESSAGE_201,
                            param.message ?: MESSAGE_500
                        )
                    )
                }
        }.asFlow()

    override fun scoreboardIndex(token: String, search: String, find: String): Flow<Resource<List<ScoreBoard>>> =
        object : NetworkOnlyResource<List<ScoreBoard>, ResponseScoreBoard>() {
            override suspend fun createCall(): Flow<ApiResponse<ResponseScoreBoard>> =
                remoteDataSource.scoreboardIndex(token, search, find)

            override fun transformData(param: ResponseScoreBoard): Flow<Resource<List<ScoreBoard>>> =
                flow {
                    emit(
                        Resource.Success(
                            DataMapper.mapScoreboardResponsesToDomain(param.data as List<ScoreBoardData>?),
                            param.message ?: Consts.MESSAGE_500
                        )
                    )
                }
        }.asFlow()

    override fun progressIndex(token: String): Flow<Resource<ProgressData>> =
        object : NetworkOnlyResource<ProgressData, ResponseProgress>() {
            override suspend fun createCall(): Flow<ApiResponse<ResponseProgress>> =
                remoteDataSource.progressIndex(token)

            override fun transformData(param: ResponseProgress): Flow<Resource<ProgressData>> =
                flow {
                    emit(
                        Resource.Success(
                            DataMapper.mapProgressDataResponsesToDomain(param.data!![0]!!),
                            param.message ?: Consts.MESSAGE_500
                        )
                    )
                }
        }.asFlow()

    override fun progressStore(
        token: String,
        time: Int,
        score: Double,
        lesson: String,
        status: String,
        progress: String,
        quest: String,
        answer: String
    ): Flow<Resource<ProgressStore>> =
        object : NetworkOnlyResource<ProgressStore, ResponseProgressStore>() {
            override suspend fun createCall(): Flow<ApiResponse<ResponseProgressStore>> =
                remoteDataSource.progressStore(token, time, score, lesson, status, progress, quest, answer)

            override fun transformData(param: ResponseProgressStore): Flow<Resource<ProgressStore>> =
                flow {
                    emit(
                        Resource.Success(
                            DataMapper.mapProgressStoreDataResponsesToDomain(param), param.message ?: Consts.MESSAGE_500
                        )
                    )
                }
        }.asFlow()
}