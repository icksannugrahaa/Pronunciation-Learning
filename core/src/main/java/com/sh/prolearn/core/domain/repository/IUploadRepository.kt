package com.sh.prolearn.core.domain.repository

import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.domain.model.Account
import com.sh.prolearn.core.domain.model.Upload
import kotlinx.coroutines.flow.Flow

interface IUploadRepository {
    fun uploadFile(filePath: String, destinationPath: String): Flow<Resource<Upload>>
}