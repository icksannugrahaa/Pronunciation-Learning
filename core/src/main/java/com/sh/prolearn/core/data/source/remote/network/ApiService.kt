package com.sh.prolearn.core.data.source.remote.network

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
import com.sh.prolearn.core.utils.Consts.ACCOUNT_CHANGE_PASSWORD_PATH
import com.sh.prolearn.core.utils.Consts.ACCOUNT_PATH
import com.sh.prolearn.core.utils.Consts.ACCOUNT_UPDATE_PATH
import com.sh.prolearn.core.utils.Consts.ACCOUNT_VERIFY
import com.sh.prolearn.core.utils.Consts.ACHIEVEMENT_PATH
import com.sh.prolearn.core.utils.Consts.AUTH_LOGIN_EXPIRED
import com.sh.prolearn.core.utils.Consts.AUTH_LOGIN_PATH
import com.sh.prolearn.core.utils.Consts.AUTH_LOGOUT_PATH
import com.sh.prolearn.core.utils.Consts.AUTH_REGISTER_PATH
import com.sh.prolearn.core.utils.Consts.AUTH_SEND_CODE
import com.sh.prolearn.core.utils.Consts.MODULES_DATA_PATH
import com.sh.prolearn.core.utils.Consts.MODULES_STORE_REVIEW_PATH
import com.sh.prolearn.core.utils.Consts.PREDICT_PATH
import com.sh.prolearn.core.utils.Consts.PROGRESS_DATA_PATH
import com.sh.prolearn.core.utils.Consts.PROGRESS_STORE_PATH
import com.sh.prolearn.core.utils.Consts.SCOREBOARD_DATA_PATH
import com.sh.prolearn.core.utils.Consts.TTS_PATH
import com.sh.prolearn.core.utils.Consts.UPLOAD_PATH
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    //    AUTH
    @FormUrlEncoded
    @POST(AUTH_LOGIN_PATH)
    suspend fun authLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): ResponseAuth

    @FormUrlEncoded
    @POST(AUTH_SEND_CODE)
    suspend fun authSendCode(@Header("Authorization") token: String, @Field("email") email: String?): ResponseDefault

    @FormUrlEncoded
    @POST(AUTH_REGISTER_PATH)
    suspend fun authRegister(
        @Field("email") email: String,
        @Field("name") name: String,
        @Field("password") password: String,
        @Field("cPassword") cpassword: String
        ): ResponseDefault

    @POST(AUTH_LOGOUT_PATH)
    suspend fun authLogout(@Header("Authorization") token: String): ResponseDefault

    @POST(AUTH_LOGIN_EXPIRED)
    suspend fun authExpire(@Header("Authorization") token: String): ResponseDefault

    //    ACCOUNT
    @FormUrlEncoded
    @POST(ACCOUNT_UPDATE_PATH)
    suspend fun updateAccount(
        @Header("Authorization") token: String,
        @Field("email") email: String,
        @Field("name") name: String,
        @Field("phoneNumber") phoneNumber: String,
        @Field("gender") gender: String,
        @Field("biodata") biodata: String,
        @Field("avatar") avatar: String,
        @Field("currentPassword") currentPassword: String
    ): ResponseAuth

    @FormUrlEncoded
    @POST(ACCOUNT_CHANGE_PASSWORD_PATH)
    suspend fun changePasswordAccount(
        @Header("Authorization") token: String,
        @Field("currentPassword") currentPassword: String,
        @Field("newPassword") newPassword: String,
        @Field("cPassword") cPassword: String
    ): ResponseDefault


    @GET(ACCOUNT_VERIFY)
    suspend fun accountVerify(
        @Query("code") code: String
    ): ResponseDefault

    @POST(ACCOUNT_PATH)
    suspend fun accountMe(@Header("Authorization") token: String): ResponseAccount

    //    MODULES
    @FormUrlEncoded
    @POST(MODULES_DATA_PATH)
    suspend fun modulesIndex(
        @Field("search") search: String,
        @Field("find") find: String
    ): ResponseModules

    @FormUrlEncoded
    @POST(MODULES_STORE_REVIEW_PATH)
    suspend fun modulesStoreReview(
        @Header("Authorization") token: String,
        @Field("lesson") lesson: String,
        @Field("comment") comment: String,
        @Field("rating") rating: String
    ): ResponseDefault

    @FormUrlEncoded
    @POST(SCOREBOARD_DATA_PATH)
    suspend fun scoreboardIndex(
        @Header("Authorization") token: String,
        @Field("search") search: String,
        @Field("find") find: String,
        @Field("limit") limit: Int = 100
    ): ResponseScoreBoard

    //    PROGRESS
    @POST(PROGRESS_DATA_PATH)
    suspend fun progressIndex(@Header("Authorization") token: String): ResponseProgress

    @POST(ACHIEVEMENT_PATH)
    suspend fun achievementIndex(@Header("Authorization") token: String): ResponseAchievement

    @FormUrlEncoded
    @POST(PROGRESS_STORE_PATH)
    suspend fun progressStore(
        @Header("Authorization") token: String,
        @Field("time") time: Int,
        @Field("score") score: Double,
        @Field("lesson") lesson: String,
        @Field("status") status: String,
        @Field("progress") progress: String,
        @Field("quest") quest: String,
        @Field("answer") answer: String
    ): ResponseProgressStore

    //    FILES
    @Multipart
    @POST(UPLOAD_PATH)
    suspend fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part("path") path: RequestBody
    ): ResponseUpload

    //    PREDICT
    @Multipart
    @POST(PREDICT_PATH)
    suspend fun predictFile(
        @Part file: MultipartBody.Part,
        @Part("path") path: RequestBody,
        @Header("Authorization") token: String
    ): ResponsePredict

    @FormUrlEncoded
    @POST(TTS_PATH)
    suspend fun predictTTS(
        @Header("Authorization") token: String,
        @Field("filename") filename: String,
        @Field("text") text: String
    ): ResponseTTS
}