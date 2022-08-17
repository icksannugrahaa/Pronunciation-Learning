package com.sh.prolearn.core.data.source.remote

import com.sh.prolearn.core.data.source.remote.network.ApiResponse
import com.sh.prolearn.core.data.source.remote.network.ApiService
import com.sh.prolearn.core.data.source.remote.response.account.ResponseAccount
import com.sh.prolearn.core.data.source.remote.response.achievement.ResponseAchievement
import com.sh.prolearn.core.data.source.remote.response.auth.ResponseAuth
import com.sh.prolearn.core.data.source.remote.response.file.ResponseUpload
import com.sh.prolearn.core.data.source.remote.response.general.ResponseDefault
import com.sh.prolearn.core.data.source.remote.response.module.ModuleData
import com.sh.prolearn.core.data.source.remote.response.module.ResponseModules
import com.sh.prolearn.core.data.source.remote.response.module.ResponseScoreBoard
import com.sh.prolearn.core.data.source.remote.response.predict.ResponsePredict
import com.sh.prolearn.core.data.source.remote.response.predict.ResponseTTS
import com.sh.prolearn.core.data.source.remote.response.progress.ResponseProgress
import com.sh.prolearn.core.data.source.remote.response.progress.ResponseProgressStore
import com.sh.prolearn.core.domain.model.Account
import com.sh.prolearn.core.utils.Consts.MESSAGE_500
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

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

    suspend fun registerAccount(email: String, name: String, password: String, cpassword: String): Flow<ApiResponse<ResponseDefault>> =
        flow {
            try {
                val response = apiService.authRegister(email, name, password, cpassword)
                if (response.status == "success") {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Error(response.message ?: MESSAGE_500))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)

    suspend fun sendCodeAccount(token: String, email: String?): Flow<ApiResponse<ResponseDefault>> = flow {
        try {
            val response = apiService.authSendCode(token, email)
            if (response.status == "success") {
                emit(ApiResponse.Success(response))
            } else {
                emit(ApiResponse.Error(response.message ?: MESSAGE_500))
            }
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun changePasswordAccount(token: String, currentPassword: String, newPassword: String, cPassword: String): Flow<ApiResponse<ResponseDefault>> = flow {
        try {
            val response = apiService.changePasswordAccount(token, currentPassword, newPassword, cPassword)
            if (response.status == "success") {
                emit(ApiResponse.Success(response))
            } else {
                emit(ApiResponse.Error(response.message ?: MESSAGE_500))
            }
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message.toString()))
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
    suspend fun updateAccount(token: String, account: Account, currentPassword: String): Flow<ApiResponse<ResponseAuth>> =
        flow {
            try {
                val response = apiService.updateAccount(
                    token,
                    account.email ?: "",
                    account.name ?: "",
                    account.phoneNumber ?: "",
                    account.gender ?: "",
                    account.biodata ?: "",
                    account.avatar ?: "",
                    currentPassword
                )
                if (response.status == "success") {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Error(response.message ?: MESSAGE_500))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)

    suspend fun verifyAccount(code: String): Flow<ApiResponse<ResponseDefault>> = flow {
        try {
            val response = apiService.accountVerify(code)
            if (response.status == "success") {
                emit(ApiResponse.Success(response))
            } else {
                emit(ApiResponse.Error(response.message ?: MESSAGE_500))
            }
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

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
    suspend fun modulesIndex(search: String, find: String): Flow<ApiResponse<ResponseModules>> =
        flow {
            try {
                val response = apiService.modulesIndex(search, find)
                if (response.status == "success") {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Error(response.message ?: MESSAGE_500))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)

    suspend fun moduleReview(token: String, lesson: String, comment: String, rating: String): Flow<ApiResponse<ResponseDefault>> =
        flow {
            try {
                val response = apiService.modulesStoreReview(token, lesson, comment, rating)
                if (response.status == "success") {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Error(response.message ?: MESSAGE_500))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)

    suspend fun scoreboardIndex(token: String, search: String, find: String): Flow<ApiResponse<ResponseScoreBoard>> =
        flow {
            try {
                val response = apiService.scoreboardIndex(token, search, find)
                if (response.status == "success") {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Error(response.message ?: MESSAGE_500))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)

    //    PROGRESS RDS
    suspend fun progressIndex(token: String): Flow<ApiResponse<ResponseProgress>> = flow {
        try {
            val response = apiService.progressIndex(token)
            if (response.status == "success") {
                emit(ApiResponse.Success(response))
            } else {
                emit(ApiResponse.Error(response.message ?: MESSAGE_500))
            }
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun achievementIndex(token: String): Flow<ApiResponse<ResponseAchievement>> = flow {
        try {
            val response = apiService.achievementIndex(token)
            if (response.status == "success") {
                emit(ApiResponse.Success(response))
            } else {
                emit(ApiResponse.Error(response.message ?: MESSAGE_500))
            }
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun progressStore(
        token: String,
        time: Int,
        score: Double,
        lesson: String,
        status: String,
        progress: String,
        quest: String,
        answer: String
    ): Flow<ApiResponse<ResponseProgressStore>> = flow {
        try {
            val response = apiService.progressStore(token, time, score, lesson, status, progress, quest, answer)
            if (response.status == "success") {
                emit(ApiResponse.Success(response))
            } else {
                emit(ApiResponse.Error(response.message ?: MESSAGE_500))
            }
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    //    UPLOAD RDS
    suspend fun uploadFile(
        file: File,
        destinationPath: String
    ): Flow<ApiResponse<ResponseUpload>> =
        flow {
            try {
                val fileBody: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "file",
                    file.name.toString(),
                    file.asRequestBody("images/*".toMediaType())
                )
                val pathBody = destinationPath.toRequestBody("text/plain".toMediaTypeOrNull())
                val response = apiService.uploadFile(fileBody, pathBody)

                if (response.status == true) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Error(response.message ?: MESSAGE_500))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)

    //    PREDICT RDS
    suspend fun predictFile(
        filePath: String,
        destinationPath: String,
        token: String
    ): Flow<ApiResponse<ResponsePredict>> =
        flow {
            try {
                val file = File(filePath)
                val fileBody: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "file",
                    file.name.toString(),
                    file.asRequestBody("audio/*".toMediaType())
                )
                val pathBody = destinationPath.toRequestBody("text/plain".toMediaTypeOrNull())
                val response = apiService.predictFile(fileBody, pathBody, token)
                if (response.status == "success") {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Error(response.message ?: MESSAGE_500))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)

    suspend fun predictTTS(
        token: String,
        filename: String,
        text: String
    ): Flow<ApiResponse<ResponseTTS>> = flow {
        try {
            val response = apiService.predictTTS(token, filename, text)
            if (response.status == "success") {
                emit(ApiResponse.Success(response))
            } else {
                emit(ApiResponse.Error(response.message ?: MESSAGE_500))
            }
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)
}