package com.sh.prolearn.app.lesson

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.domain.model.Upload
import com.sh.prolearn.core.domain.usecase.upload.UploadUseCase

class UploadViewModel(private val uploadUseCase: UploadUseCase) : ViewModel() {
    fun uploadFIle(filePath: String, destinationPath: String): LiveData<Resource<Upload>> = uploadUseCase.uploadFile(filePath, destinationPath).asLiveData()
}