package com.sh.prolearn.di

import com.sh.prolearn.app.authentication.AuthenticationViewModel
import com.sh.prolearn.app.lesson.PredictViewModel
import com.sh.prolearn.app.lesson.UploadViewModel
import com.sh.prolearn.app.modules.ModuleViewModel
import com.sh.prolearn.core.domain.usecase.account.AccountInteractor
import com.sh.prolearn.core.domain.usecase.account.AccountUseCase
import com.sh.prolearn.core.domain.usecase.module.ModuleInteractor
import com.sh.prolearn.core.domain.usecase.module.ModuleUseCase
import com.sh.prolearn.core.domain.usecase.predict.PredictInteractor
import com.sh.prolearn.core.domain.usecase.predict.PredictUseCase
import com.sh.prolearn.core.domain.usecase.upload.UploadInteractor
import com.sh.prolearn.core.domain.usecase.upload.UploadUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val useCaseModule = module {
        factory<AccountUseCase> { AccountInteractor(get()) }
        factory<ModuleUseCase> { ModuleInteractor(get()) }
        factory<UploadUseCase> { UploadInteractor(get()) }
        factory<PredictUseCase> { PredictInteractor(get()) }
    }

    val viewModelModule = module {
        viewModel { ModuleViewModel(get()) }
        viewModel { AuthenticationViewModel(get()) }
        viewModel { UploadViewModel(get()) }
        viewModel { PredictViewModel(get()) }
    }
}