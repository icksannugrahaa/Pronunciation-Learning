package com.sh.prolearn.core.data.source.remote.network

import com.sh.prolearn.core.data.source.remote.response.account.ResponseAccount
import com.sh.prolearn.core.data.source.remote.response.auth.ResponseAuth
import com.sh.prolearn.core.data.source.remote.response.general.ResponseDefault
import com.sh.prolearn.core.data.source.remote.response.module.ModuleData
import com.sh.prolearn.core.data.source.remote.response.module.ResponseModules
import com.sh.prolearn.core.utils.Consts.ACCOUNT_PATH
import com.sh.prolearn.core.utils.Consts.AUTH_LOGIN_EXPIRED
import com.sh.prolearn.core.utils.Consts.AUTH_LOGIN_PATH
import com.sh.prolearn.core.utils.Consts.AUTH_LOGOUT_PATH
import com.sh.prolearn.core.utils.Consts.MODULES_DATA_PATH
import retrofit2.http.*

interface ApiService {
//    AUTH
    @FormUrlEncoded
    @POST(AUTH_LOGIN_PATH)
    suspend fun authLogin(@Field("email") email: String, @Field("password") password: String): ResponseAuth

    @POST(AUTH_LOGOUT_PATH)
    suspend fun authLogout(@Header("Authorization") token: String): ResponseDefault

    @POST(AUTH_LOGIN_EXPIRED)
    suspend fun authExpire(@Header("Authorization") token: String): ResponseDefault

//    ACCOUNT
    @FormUrlEncoded
    @POST(ACCOUNT_PATH)
    suspend fun accountUpdate(
        @Field("token") token: String,
        @Field("name") name: String,
        @Field("password") password: String,
        @Field("email") email: String,
        @Field("phoneNumber") phoneNumber: String,
        @Field("avatarUrl") avatar: String,
        @Field("platNomor") plat: String,
        @Field("wifiPackageStatus") wfps: Boolean,
        @Field("parkingPackageStatus") pkps: Boolean
    ): ResponseAccount

    @POST(ACCOUNT_PATH)
    suspend fun accountMe(@Header("Authorization") token: String): ResponseAccount

//    MODULES
    @FormUrlEncoded
    @POST(MODULES_DATA_PATH)
    suspend fun modulesIndex(@Field("search") search: String): ResponseModules

//    @Multipart
//    @POST(UPLOAD_PATH)
//    suspend fun uploadFile(
//        @Part file: MultipartBody.Part,
//        @Part("path") path: RequestBody
//    ): ResponseFileUpload

}