package com.sh.prolearn.core.domain.usecase.account

import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.domain.model.Account
import com.sh.prolearn.core.domain.model.Achievement
import com.sh.prolearn.core.domain.repository.IAccountRepository
import kotlinx.coroutines.flow.Flow

class AccountInteractor(private val accountRepository: IAccountRepository) : AccountUseCase {
    override fun loginAccount(email: String, password: String) = accountRepository.loginAccount(email, password)
    override fun sendCodeAccount(token: String, email: String?) = accountRepository.sendCodeAccount(token, email)
    override fun verifyAccount(code: String) = accountRepository.verifyAccount(code)
    override fun registerAccount(email: String, name: String, password: String, cpassword: String) = accountRepository.registerAccount(email, name, password, cpassword)
    override fun logoutAccount(token: String): Flow<Resource<Boolean>> = accountRepository.logoutAccount(token)
    override fun accountGet(token: String): Flow<Resource<Account>> = accountRepository.accountGet(token)
    override fun expireLogin(token: String): Flow<Resource<Boolean>> = accountRepository.expireLogin(token)
    override fun updateAccount(token: String, account: Account, currentPassword: String): Flow<Resource<Account>> = accountRepository.updateAccount(token, account, currentPassword)
    override fun changePasswordAccount(token: String, currentPassword: String, newPassword: String, cPassword: String): Flow<Resource<Boolean>> = accountRepository.changePasswordAccount(token, currentPassword, newPassword, cPassword)
    override fun achievementIndex(token: String): Flow<Resource<List<Achievement>>> = accountRepository.achievementIndex(token)
}