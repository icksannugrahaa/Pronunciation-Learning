package com.sh.prolearn.core.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.sh.prolearn.core.domain.model.Account
import com.sh.prolearn.core.utils.Consts.SHARED_PREFS_LOGIN
import com.sh.prolearn.core.utils.Consts.SHARED_PREFS_LOGIN_ACCOUNT
import com.sh.prolearn.core.utils.Consts.SHARED_PREFS_LOGIN_API
import com.sh.prolearn.core.utils.Consts.SHARED_PREFS_LOGIN_DATE
import com.sh.prolearn.core.utils.Consts.SHARED_PREFS_LOGIN_DEVICE
import com.sh.prolearn.core.utils.Consts.SHARED_PREFS_LOGIN_EXPIRE
import com.sh.prolearn.core.utils.Consts.SHARED_PREFS_LOGIN_IP
import com.sh.prolearn.core.utils.Consts.SHARED_PREFS_LOGIN_MODEL
import com.sh.prolearn.core.utils.Consts.SHARED_PREFS_LOGIN_PRODUCT

class AuthPreferences(context: Context) {
    private var preference: SharedPreferences

    init {
        preference = context.getSharedPreferences(SHARED_PREFS_LOGIN, Context.MODE_PRIVATE)
    }

    val authData: Account?
        get() = Gson().fromJson(
            preference.getString(SHARED_PREFS_LOGIN_ACCOUNT, null),
            Account::class.java
        ) ?: null

    val authToken: String
        get() {
            val token = if (Gson().fromJson(
                    preference.getString(SHARED_PREFS_LOGIN_ACCOUNT, null),
                    Account::class.java
                ) != null
            ) {
                "Bearer ${
                    Gson().fromJson(
                        preference.getString(SHARED_PREFS_LOGIN_ACCOUNT, null),
                        Account::class.java
                    ).token
                }"
            } else "null"
            return token
        }

    fun saveAuthDetail(
        ip: String?,
        date: String?,
        device: String?,
        api: String?,
        model: String?,
        product: String?
    ) {
        val editor = preference.edit()
        editor.putString(SHARED_PREFS_LOGIN_IP, ip)
        editor.putString(SHARED_PREFS_LOGIN_DATE, date)
        editor.putString(SHARED_PREFS_LOGIN_DEVICE, device)
        editor.putString(SHARED_PREFS_LOGIN_API, api)
        editor.putString(SHARED_PREFS_LOGIN_MODEL, model)
        editor.putString(SHARED_PREFS_LOGIN_PRODUCT, product)
        editor.apply()
    }

    fun saveAuthData(account: Account?) {
        val editor = preference.edit()
        val accData = Gson().toJson(account)
        editor.putString(SHARED_PREFS_LOGIN_ACCOUNT, accData)
        editor.apply()
    }
}