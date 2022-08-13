package com.sh.prolearn.core.data.repository

import com.sh.prolearn.core.data.NetworkOnlyResource
import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.data.source.remote.RemoteDataSource
import com.sh.prolearn.core.data.source.remote.network.ApiResponse
import com.sh.prolearn.core.data.source.remote.response.file.ResponseUpload
import com.sh.prolearn.core.data.source.remote.response.predict.ResponsePredict
import com.sh.prolearn.core.data.source.remote.response.predict.ResponseTTS
import com.sh.prolearn.core.domain.model.Predict
import com.sh.prolearn.core.domain.model.TextToSpeech
import com.sh.prolearn.core.domain.model.Upload
import com.sh.prolearn.core.domain.repository.IPredictRepository
import com.sh.prolearn.core.domain.repository.IUploadRepository
import com.sh.prolearn.core.utils.Consts.MESSAGE_500
import com.sh.prolearn.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PredictRepository(
    private val remoteDataSource: RemoteDataSource
) : IPredictRepository {
    override fun predictFile(filePath: String, destinationPath: String, token: String): Flow<Resource<Predict>> =
        object: NetworkOnlyResource<Predict, ResponsePredict>() {
            override suspend fun createCall(): Flow<ApiResponse<ResponsePredict>> = remoteDataSource.predictFile(filePath, destinationPath, token)
            override fun transformData(param: ResponsePredict): Flow<Resource<Predict>> = flow {
                emit(Resource.Success(DataMapper.mapPredictResponsesToDomain(param.data!!), param.message ?: MESSAGE_500))
            }
        }.asFlow()

    override fun predictTTS(token: String, filename: String, text: String): Flow<Resource<TextToSpeech>> =
        object: NetworkOnlyResource<TextToSpeech, ResponseTTS>() {
            override suspend fun createCall(): Flow<ApiResponse<ResponseTTS>> = remoteDataSource.predictTTS(token, filename, text)
            override fun transformData(param: ResponseTTS): Flow<Resource<TextToSpeech>> = flow {
                emit(Resource.Success(DataMapper.mapPredictTTSResponsesToDomain(param), param.message ?: MESSAGE_500))
            }
        }.asFlow()
}