package com.sh.prolearn.core.domain.usecase.predict

import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.domain.model.Predict
import com.sh.prolearn.core.domain.model.TextToSpeech
import com.sh.prolearn.core.domain.repository.IPredictRepository
import kotlinx.coroutines.flow.Flow

class PredictInteractor(private val predictRepository: IPredictRepository) : PredictUseCase {
    override fun predictFile(filePath: String, destinationPath: String, token: String): Flow<Resource<Predict>> = predictRepository.predictFile(filePath, destinationPath, token)
    override fun predictTTS(
        token: String,
        filename: String,
        text: String
    ): Flow<Resource<TextToSpeech>> = predictRepository.predictTTS(token, filename, text)
}