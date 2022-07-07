package com.sh.prolearn.di

import com.sh.prolearn.app.authentication.AuthenticationViewModel
import com.sh.prolearn.app.modules.ModuleViewModel
import com.sh.prolearn.core.domain.usecase.account.AccountInteractor
import com.sh.prolearn.core.domain.usecase.account.AccountUseCase
import com.sh.prolearn.core.domain.usecase.module.ModuleInteractor
import com.sh.prolearn.core.domain.usecase.module.ModuleUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val useCaseModule = module {
        factory<AccountUseCase> { AccountInteractor(get()) }
        factory<ModuleUseCase> { ModuleInteractor(get()) }
    }

    val viewModelModule = module {
        viewModel { ModuleViewModel(get()) }
        viewModel { AuthenticationViewModel(get()) }
//        viewModel { FilterNewsViewModel(get()) }
//        viewModel { LoginViewModel(get()) }
//        viewModel { AccountViewModel(get(), get(), get()) }
//        viewModel { PresenceViewModel(get()) }
//        viewModel { WifiViewModel(get()) }
    }
}