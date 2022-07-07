package com.sh.prolearn.core.domain.repository

import com.sh.prolearn.core.data.Resource
import com.sh.prolearn.core.domain.model.Account
import kotlinx.coroutines.flow.Flow

interface IAccountRepository {
    fun loginAccount(email: String, password: String): Flow<Resource<Account>>
    fun logoutAccount(token: String): Flow<Resource<Boolean>>
    fun accountGet(token: String): Flow<Resource<Account>>
    fun expireLogin(token: String): Flow<Resource<Boolean>>
//    fun accountUpdate(token: String, account: Account): Flow<Resource<Account>>
//    fun uploadImage(path: String): Flow<Resource<Upload>>
}