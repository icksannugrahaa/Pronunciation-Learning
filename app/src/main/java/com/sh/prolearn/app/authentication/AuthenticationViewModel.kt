package com.sh.prolearn.app.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.sh.prolearn.core.domain.model.Account
import com.sh.prolearn.core.domain.usecase.account.AccountUseCase

class AuthenticationViewModel(private val accountUseCase: AccountUseCase) : ViewModel() {
    fun authLogin(email: String, password: String) = accountUseCase.loginAccount(email, password).asLiveData()
    fun authSendCode(token: String, email: String?) = accountUseCase.sendCodeAccount(token, email).asLiveData()
    fun verifyAccount(code: String) = accountUseCase.verifyAccount(code).asLiveData()
    fun authRegister(email: String, name: String, password: String, cpassword: String) = accountUseCase.registerAccount(email, name, password, cpassword).asLiveData()
    fun authLogout(token: String) = accountUseCase.logoutAccount(token).asLiveData()
    fun authMe(token: String) = accountUseCase.accountGet(token).asLiveData()
    fun achievementIndex(token: String) = accountUseCase.achievementIndex(token).asLiveData()
    fun changePassword(token: String, currentPassword: String, newPassword: String, cPassword: String) = accountUseCase.changePasswordAccount(token, currentPassword, newPassword, cPassword).asLiveData()
    fun accountUpdate(token: String, account: Account, currentPassword: String) = accountUseCase.updateAccount(token, account, currentPassword).asLiveData()
}