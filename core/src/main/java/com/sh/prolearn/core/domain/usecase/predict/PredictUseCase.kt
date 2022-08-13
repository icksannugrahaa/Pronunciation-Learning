package com.sh.prolearn.core.domain.usecase.predict

import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.domain.model.Predict
import com.sh.prolearn.core.domain.model.TextToSpeech
import com.sh.prolearn.core.domain.model.Upload
import kotlinx.coroutines.flow.Flow

interface PredictUseCase {
    fun predictFile(filePath: String, destinationPath: String, token: String): Flow<Resource<Predict>>
    fun predictTTS(token: String, filename: String, text: String): Flow<Resource<TextToSpeech>>
}
