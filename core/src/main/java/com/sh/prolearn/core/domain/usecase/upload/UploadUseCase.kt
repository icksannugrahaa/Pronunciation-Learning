package com.sh.prolearn.core.domain.usecase.upload

import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.domain.model.Upload
import kotlinx.coroutines.flow.Flow

interface UploadUseCase {
    fun uploadFile(filePath: String, destinationPath: String): Flow<Resource<Upload>>
}
