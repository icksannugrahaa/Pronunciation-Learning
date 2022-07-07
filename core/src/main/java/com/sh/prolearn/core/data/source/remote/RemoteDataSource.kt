package com.sh.prolearn.core.data.source.remote

import com.sh.prolearn.core.data.source.remote.network.ApiResponse
import com.sh.prolearn.core.data.source.remote.network.ApiService
import com.sh.prolearn.core.data.source.remote.response.account.ResponseAccount
import com.sh.prolearn.core.data.source.remote.response.auth.ResponseAuth
import com.sh.prolearn.core.data.source.remote.response.general.ResponseDefault
import com.sh.prolearn.core.data.source.remote.response.module.ModuleData
import com.sh.prolearn.core.data.source.remote.response.module.ResponseModules
import com.sh.prolearn.core.utils.Consts.MESSAGE_500
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val apiService: ApiService) {
//    AUTH RDS
    suspend fun loginAccount(regNumber: String, password: String): Flow<ApiResponse<ResponseAuth>> =
        flow {
            try {
                val response = apiService.authLogin(regNumber, password)
                if (response.status == "success") {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Error(response.message ?: MESSAGE_500))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)

    suspend fun logoutAccount(token: String): Flow<ApiResponse<ResponseDefault>> = flow {
        try {
            val response = apiService.authLogout(token)
            if (response.status == "success") {
                emit(ApiResponse.Success(response))
            } else {
                emit(ApiResponse.Error(response.message ?: MESSAGE_500))
            }
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun expireLogin(token: String): Flow<ApiResponse<ResponseDefault>> = flow {
        try {
            val response = apiService.authExpire(token)
            if (response.status == "success") {
                emit(ApiResponse.Success(response))
            } else {
                emit(ApiResponse.Error(response.message ?: MESSAGE_500))
            }
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

//    ACCOUNT RDS
//    suspend fun accountUpdate(token: String, account: Account): Flow<ApiResponse<ResponseAccount>> =
//        flow {
//            try {
//                val response = apiService.accountUpdate(
//                    token,
//                    account.name ?: "",
//                    account.password ?: "",
//                    account.email ?: "",
//                    account.phoneNumber ?: "",
//                    account.avatarUrl ?: "",
//                    account.platNomor ?: "",
//                    account.wifiPackageStatus ?: false,
//                    account.parkingPackageStatus ?: false
//                )
//                if (!response.results.id.isNullOrEmpty()) {
//                    emit(ApiResponse.Success(response))
//                } else {
//                    emit(ApiResponse.Error(response.message ?: SERVER_ERR_EMPTY))
//                }
//            } catch (e: Exception) {
//                emit(ApiResponse.Error(e.toString()))
//            }
//        }.flowOn(Dispatchers.IO)

    suspend fun accountGet(token: String): Flow<ApiResponse<ResponseAccount>> = flow {
        try {
            val response = apiService.accountMe(token)
            if (response.status == "success") {
                emit(ApiResponse.Success(response))
            } else {
                emit(ApiResponse.Error(response.message ?: MESSAGE_500))
            }
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

//    MODULES RDS
    suspend fun modulesIndex(search: String): Flow<ApiResponse<ResponseModules>> = flow {
        try {
            val response = apiService.modulesIndex(search)
            if (response.status == "success") {
                emit(ApiResponse.Success(response))
            } else {
                emit(ApiResponse.Error(response.message ?: MESSAGE_500))
            }
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)
//    suspend fun uploadFile(filePath: String, destinationPath: String): Flow<ApiResponse<ResponseFileUpload>> =
//        flow {
//            try {
//                val file = File(filePath)
//                val fileBody: MultipartBody.Part = MultipartBody.Part.createFormData(
//                    "file",
//                    file.name.toString(),
//                    file.asRequestBody("images/*".toMediaType())
//                )
//                val pathBody = destinationPath.toRequestBody("text/plain".toMediaTypeOrNull())
//                val response = apiService.uploadFile(fileBody, pathBody)
//
//                if (!response.imageUrl.isNullOrEmpty()) {
//                    emit(ApiResponse.Success(response))
//                } else {
//                    emit(ApiResponse.Error(response.message ?: SERVER_ERR_EMPTY))
//                }
//            } catch (e: Exception) {
//                emit(ApiResponse.Error(e.message.toString()))
//            }
//        }.flowOn(Dispatchers.IO)
}