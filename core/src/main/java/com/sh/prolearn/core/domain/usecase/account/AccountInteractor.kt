package com.sh.prolearn.core.domain.usecase.account

import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.domain.model.Account
import com.sh.prolearn.core.domain.repository.IAccountRepository
import kotlinx.coroutines.flow.Flow

class AccountInteractor(private val accountRepository: IAccountRepository) : AccountUseCase {
    override fun loginAccount(email: String, password: String) = accountRepository.loginAccount(email, password)
    override fun registerAccount(email: String, name: String, password: String, cpassword: String) = accountRepository.registerAccount(email, name, password, cpassword)
    override fun logoutAccount(token: String): Flow<Resource<Boolean>> = accountRepository.logoutAccount(token)
    override fun accountGet(token: String): Flow<Resource<Account>> = accountRepository.accountGet(token)
    override fun expireLogin(token: String): Flow<Resource<Boolean>> = accountRepository.expireLogin(token)
//    override fun accountUpdate(token: String, account: Account): Flow<Resource<Account>> = accountRepository.accountUpdate(token,account)
//    override fun uploadImage(path: String): Flow<Resource<Upload>> = accountRepository.uploadImage(path)
}