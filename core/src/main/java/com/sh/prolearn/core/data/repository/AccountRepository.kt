package com.sh.prolearn.core.data.repository

import com.sh.prolearn.core.data.NetworkOnlyResource
import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.data.source.remote.RemoteDataSource
import com.sh.prolearn.core.data.source.remote.network.ApiResponse
import com.sh.prolearn.core.data.source.remote.response.account.ResponseAccount
import com.sh.prolearn.core.data.source.remote.response.auth.ResponseAuth
import com.sh.prolearn.core.data.source.remote.response.general.ResponseDefault
import com.sh.prolearn.core.domain.model.Account
import com.sh.prolearn.core.domain.repository.IAccountRepository
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
                emit(Resource.Success(DataMapper.mapAccountResponsesToDomain(param.data, token), param.message ?: MESSAGE_500))
            }
        }.asFlow()

    override fun expireLogin(token: String): Flow<Resource<Boolean>> =
        object : NetworkOnlyResource<Boolean, ResponseDefault>() {
            override suspend fun createCall(): Flow<ApiResponse<ResponseDefault>> = remoteDataSource.expireLogin(token)
            override fun transformData(param: ResponseDefault): Flow<Resource<Boolean>> = flow {
                if(param.status == "success") emit(Resource.Success((param.status == "success"), param.message ?: MESSAGE_500))
            }
        }.asFlow()

//    override fun accountUpdate(token: String, account: Account): Flow<Resource<Account>> =
//        object: NetworkOnlyResource<Account, ResponseAccount>() {
//            override suspend fun createCall(): Flow<ApiResponse<ResponseAccount>> = remoteDataSource.accountUpdate(token,account)
//            override fun transformData(param: ResponseAccount): Flow<Resource<Account>> = flow {
//                emit(Resource.Success(DataMapperUtils.mapAccountResponsesToDomain(param.results, param.token), param.message ?: SERVER_ERR_EMPTY))
//            }
//        }.asFlow()
//
//    override fun uploadImage(path: String): Flow<Resource<Upload>> =
//        object: NetworkOnlyResource<Upload, ResponseFileUpload>() {
//            override suspend fun createCall(): Flow<ApiResponse<ResponseFileUpload>> = remoteDataSource.uploadFile(path, "users")
//            override fun transformData(param: ResponseFileUpload): Flow<Resource<Upload>> = flow {
//                emit(Resource.Success(DataMapperUtils.mapUploadResponsesToDomain(param), param.message ?: SERVER_ERR_EMPTY))
//            }
//        }.asFlow()

}