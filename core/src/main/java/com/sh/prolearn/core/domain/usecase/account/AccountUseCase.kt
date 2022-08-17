package com.sh.prolearn.core.domain.usecase.account

import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.domain.model.Account
import com.sh.prolearn.core.domain.model.Achievement
import kotlinx.coroutines.flow.Flow

interface AccountUseCase {
    fun loginAccount(email: String, password: String): Flow<Resource<Account>>
    fun sendCodeAccount(token: String, email: String?): Flow<Resource<Boolean>>
    fun verifyAccount(code: String): Flow<Resource<Boolean>>
    fun registerAccount(email: String, name: String, password: String, cpassword: String): Flow<Resource<String>>
    fun logoutAccount(token: String): Flow<Resource<Boolean>>
    fun accountGet(token: String): Flow<Resource<Account>>
    fun expireLogin(token: String): Flow<Resource<Boolean>>
    fun updateAccount(token: String, account: Account, currentPassword: String): Flow<Resource<Account>>
    fun changePasswordAccount(token: String, currentPassword: String, newPassword: String, cPassword: String): Flow<Resource<Boolean>>
    fun achievementIndex(token: String): Flow<Resource<List<Achievement>>>
}
