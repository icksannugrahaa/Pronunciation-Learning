package com.sh.prolearn.core.domain.usecase.upload

import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.domain.model.Module
import com.sh.prolearn.core.domain.model.ProgressData
import com.sh.prolearn.core.domain.model.Upload
import com.sh.prolearn.core.domain.repository.IModuleRepository
import com.sh.prolearn.core.domain.repository.IUploadRepository
import kotlinx.coroutines.flow.Flow
import java.io.File

class UploadInteractor(private val uploadRepository: IUploadRepository) : UploadUseCase {
    override fun uploadFile(file: File, destinationPath: String): Flow<Resource<Upload>> = uploadRepository.uploadFile(file, destinationPath)
}