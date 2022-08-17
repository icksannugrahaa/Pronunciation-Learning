package com.sh.prolearn.core.data.repository

import com.sh.prolearn.core.data.NetworkOnlyResource
import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.data.source.remote.RemoteDataSource
import com.sh.prolearn.core.data.source.remote.network.ApiResponse
import com.sh.prolearn.core.data.source.remote.response.account.ResponseAccount
import com.sh.prolearn.core.data.source.remote.response.achievement.ResponseAchievement
import com.sh.prolearn.core.data.source.remote.response.auth.ResponseAuth
import com.sh.prolearn.core.data.source.remote.response.general.ResponseDefault
import com.sh.prolearn.core.data.source.remote.response.progress.ResponseProgress
import com.sh.prolearn.core.domain.model.Account
import com.sh.prolearn.core.domain.model.Achievement
import com.sh.prolearn.core.domain.model.ProgressData
import com.sh.prolearn.core.domain.repository.IAccountRepository
import com.sh.prolearn.core.utils.Consts
import com.sh.prolearn.core.utils.Consts.MESSAGE_500
import com.sh.prolearn.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AccountRepository(
    private val remoteDataSource: RemoteDataSource
) : IAccountRepository {
    override fun loginAccount(email: String, password: String): Flow<Resource<Account>> =
        object: NetworkOnlyResource<Account, ResponseAuth>() {
            override suspend fun createCall(): Flow<ApiResponse<ResponseAuth>> = remoteDataSource.loginAccount(email, password)
            override fun transformData(param: ResponseAuth): Flow<Resource<Account>> = flow {
                emit(Resource.Success(DataMapper.mapAccountResponsesToDomain(param.data, param.token), param.message ?: MESSAGE_500))
            }
        }.asFlow()

    override fun registerAccount(email: String, name: String, password: String, cpassword: String): Flow<Resource<String>> =
        object: NetworkOnlyResource<String, ResponseDefault>() {
            override suspend fun createCall(): Flow<ApiResponse<ResponseDefault>> = remoteDataSource.registerAccount(email, name, password, cpassword)
            override fun transformData(param: ResponseDefault): Flow<Resource<String>> = flow {
                emit(Resource.Success(param.status ?: "success", param.message ?: MESSAGE_500))
            }
        }.asFlow()

    override fun changePasswordAccount(email: String, currentPassword: String, newPassword: String, cPassword: String): Flow<Resource<Boolean>> =
        object: NetworkOnlyResource<Boolean, ResponseDefault>() {
            override suspend fun createCall(): Flow<ApiResponse<ResponseDefault>> = remoteDataSource.changePasswordAccount(email, currentPassword, newPassword, cPassword)
            override fun transformData(param: ResponseDefault): Flow<Resource<Boolean>> = flow {
                emit(Resource.Success(param.status == "success", param.message ?: MESSAGE_500))
            }
        }.asFlow()

    override fun sendCodeAccount(token: String, email: String?): Flow<Resource<Boolean>> =
        object : NetworkOnlyResource<Boolean, ResponseDefault>() {
            override suspend fun createCall(): Flow<ApiResponse<ResponseDefault>> = remoteDataSource.sendCodeAccount(token, email)
            override fun transformData(param: ResponseDefault): Flow<Resource<Boolean>> = flow {
                if(param.status == "success") emit(Resource.Success((param.status == "true"), param.message ?: MESSAGE_500))
            }
        }.asFlow()

    override fun verifyAccount(code: String): Flow<Resource<Boolean>> =
        object : NetworkOnlyResource<Boolean, ResponseDefault>() {
            override suspend fun createCall(): Flow<ApiResponse<ResponseDefault>> = remoteDataSource.verifyAccount(code)
            override fun transformData(param: ResponseDefault): Flow<Resource<Boolean>> = flow {
                if(param.status == "success") emit(Resource.Success((param.status == "true"), param.message ?: MESSAGE_500))
            }
        }.asFlow()

    override fun logoutAccount(token: String): Flow<Resource<Boolean>> =
        object : NetworkOnlyResource<Boolean, ResponseDefault>() {
            override suspend fun createCall(): Flow<ApiResponse<ResponseDefault>> = remoteDataSource.logoutAccount(token)
            override fun transformData(param: ResponseDefault): Flow<Resource<Boolean>> = flow {
                if(param.status == "success") emit(Resource.Success((param.status == "success"), param.message ?: MESSAGE_500))
            }
        }.asFlow()

    override fun accountGet(token: String): Flow<Resource<Account>> =
        object: NetworkOnlyResource<Account, ResponseAccount>() {
            override suspend fun createCall(): Flow<ApiResponse<ResponseAccount>> = remoteDataSource.accountGet(token)
            override fun transformData(param: ResponseAccount): Flow<Resource<Account>> = flow {
                emit(Resource.Success(DataMapper.mapAccountResponsesToDomain(param.data, param.data?.token), param.message ?: MESSAGE_500))
            }
        }.asFlow()

    override fun expireLogin(token: String): Flow<Resource<Boolean>> =
        object : NetworkOnlyResource<Boolean, ResponseDefault>() {
            override suspend fun createCall(): Flow<ApiResponse<ResponseDefault>> = remoteDataSource.expireLogin(token)
            override fun transformData(param: ResponseDefault): Flow<Resource<Boolean>> = flow {
                if(param.status == "success") emit(Resource.Success((param.status == "success"), param.message ?: MESSAGE_500))
            }
        }.asFlow()

    override fun updateAccount(token: String, account: Account, currentPassword: String): Flow<Resource<Account>> =
        object: NetworkOnlyResource<Account, ResponseAuth>() {
            override suspend fun createCall(): Flow<ApiResponse<ResponseAuth>> = remoteDataSource.updateAccount(token, account, currentPassword)
            override fun transformData(param: ResponseAuth): Flow<Resource<Account>> = flow {
                emit(Resource.Success(DataMapper.mapAccountResponsesToDomain(param.data, param.data?.token), param.message ?: MESSAGE_500))
            }
        }.asFlow()

    override fun achievementIndex(token: String): Flow<Resource<List<Achievement>>> =
        object : NetworkOnlyResource<List<Achievement>, ResponseAchievement>() {
            override suspend fun createCall(): Flow<ApiResponse<ResponseAchievement>> =
                remoteDataSource.achievementIndex(token)

            override fun transformData(param: ResponseAchievement): Flow<Resource<List<Achievement>>> =
                flow {
                    emit(
                        Resource.Success(
                            DataMapper.mapAchievementResponsesToDomain(param.data) as List<Achievement>,
                            param.message ?: MESSAGE_500
                        )
                    )
                }
        }.asFlow()
}