package com.sh.prolearn.core.domain.repository

import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.domain.model.Account
import com.sh.prolearn.core.domain.model.Upload
import kotlinx.coroutines.flow.Flow
import java.io.File

interface IUploadRepository {
    fun uploadFile(file: File, destinationPath: String): Flow<Resource<Upload>>
}