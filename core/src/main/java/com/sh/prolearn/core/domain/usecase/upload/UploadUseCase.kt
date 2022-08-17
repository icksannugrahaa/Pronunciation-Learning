package com.sh.prolearn.core.domain.usecase.upload

import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.domain.model.Upload
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UploadUseCase {
    fun uploadFile(file: File, destinationPath: String): Flow<Resource<Upload>>
}
