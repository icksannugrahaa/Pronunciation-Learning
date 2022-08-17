package com.sh.prolearn.core.data.repository

import com.sh.prolearn.core.data.NetworkOnlyResource
import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.data.source.remote.RemoteDataSource
import com.sh.prolearn.core.data.source.remote.network.ApiResponse
import com.sh.prolearn.core.data.source.remote.response.file.ResponseUpload
import com.sh.prolearn.core.domain.model.Upload
import com.sh.prolearn.core.domain.repository.IUploadRepository
import com.sh.prolearn.core.utils.Consts.MESSAGE_500
import com.sh.prolearn.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

class UploadRepository(
    private val remoteDataSource: RemoteDataSource
) : IUploadRepository {
    override fun uploadFile(file: File, destinationPath: String): Flow<Resource<Upload>> =
        object: NetworkOnlyResource<Upload, ResponseUpload>() {
            override suspend fun createCall(): Flow<ApiResponse<ResponseUpload>> = remoteDataSource.uploadFile(file, destinationPath)
            override fun transformData(param: ResponseUpload): Flow<Resource<Upload>> = flow {
                emit(Resource.Success(DataMapper.mapUploadResponsesToDomain(param), param.message ?: MESSAGE_500))
            }
        }.asFlow()

}