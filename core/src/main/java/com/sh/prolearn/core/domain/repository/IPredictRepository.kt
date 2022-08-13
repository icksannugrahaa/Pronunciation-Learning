package com.sh.prolearn.core.domain.repository

import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.domain.model.Predict
import com.sh.prolearn.core.domain.model.TextToSpeech
import kotlinx.coroutines.flow.Flow

interface IPredictRepository {
    fun predictFile(filePath: String, destinationPath: String, token: String): Flow<Resource<Predict>>
    fun predictTTS(token: String, filename: String, text: String): Flow<Resource<TextToSpeech>>
}