package com.sh.prolearn.app.lesson

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.domain.model.Predict
import com.sh.prolearn.core.domain.model.TextToSpeech
import com.sh.prolearn.core.domain.model.Upload
import com.sh.prolearn.core.domain.usecase.predict.PredictUseCase
import com.sh.prolearn.core.domain.usecase.upload.UploadUseCase

class PredictViewModel(private val predictUseCase: PredictUseCase) : ViewModel() {
    fun predictFile(filePath: String, destinationPath: String, token: String): LiveData<Resource<Predict>> = predictUseCase.predictFile(filePath, destinationPath, token).asLiveData()
    fun predictTTS(token: String, filename: String, text: String): LiveData<Resource<TextToSpeech>> = predictUseCase.predictTTS(token, filename, text).asLiveData()
}