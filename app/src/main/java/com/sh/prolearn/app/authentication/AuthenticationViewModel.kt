package com.sh.prolearn.app.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.sh.prolearn.core.domain.usecase.account.AccountUseCase

class AuthenticationViewModel(private val accountUseCase: AccountUseCase) : ViewModel() {
    fun authLogin(email: String, password: String) = accountUseCase.loginAccount(email, password).asLiveData()
    fun authRegister(email: String, name: String, password: String, cpassword: String) = accountUseCase.registerAccount(email, name, password, cpassword).asLiveData()
    fun authLogout(token: String) = accountUseCase.logoutAccount(token).asLiveData()
    fun authMe(token: String) = accountUseCase.accountGet(token).asLiveData()
//    fun uploadImage(path: String) = accountUseCase.uploadImage(path).asLiveData()
//    fun accountUpdate(token: String,account: Account) = accountUseCase.accountUpdate(token,account).asLiveData()
}