package com.sh.prolearn.core.utils

object Consts {
    //API
//    const val BASE_URL = "http://10.0.2.2:5000/" // for local device
//    const val BASE_API_URL = "http://10.0.2.2:5000/api/"
    const val BASE_URL = "http://192.168.100.88:5000/" // for local network
    const val BASE_API_URL = "http://192.168.100.88:5000/api/"
    const val AUTH_LOGIN_PATH = "auth/login"
    const val AUTH_LOGOUT_PATH = "auth/logout"
    const val AUTH_LOGIN_EXPIRED = "auth/expire"
    const val ACCOUNT_PATH = "auth/me"
    const val UPLOAD_PATH = "uploads"
    const val MODULES_DATA_PATH = "modules/data"

    //SHARED PREFERENCE
    const val SHARED_PREFS_LOGIN = "pref_login"
    const val SHARED_PREFS_LOGIN_DATE = "pref_login_date"
    const val SHARED_PREFS_LOGIN_EXPIRE = "pref_login_expire"
    const val SHARED_PREFS_LOGIN_IP = "pref_login_ip"
    const val SHARED_PREFS_LOGIN_ACCOUNT = "pref_login_account"

    //CONFIG
    const val DEFAULT_DATE_FORMAT = "dd/MM/yyyy"
    const val DATE_FORMAT_FOR_USER = "dd MMMM yyyy"

    //API MESSAGE
    const val LOGIN_ERR_ID = "Email tidak cocok!"
    const val LOGIN_ERR_PASS = "Password tidak cocok!"
    const val LOGOUT_ERROR = "Akun anda sedang bermasalah, segera kontak developer!"
    const val LOGOUT_SUCCESS = "Logout berhasil!"
    const val MESSAGE_500 = "Server sedang bermasalah, coba lagi nanti!"
    const val MESSAGE_400 = "Tolong masukan data yang sesuai!"
    const val MESSAGE_403 = "Akun anda tidak memiliki akses!"
    const val MESSAGE_401 = "Tolong login terlebih dahulu!"
    const val MESSAGE_200 = "Data ditemukan!"

    //FIELD MESSAGE
    const val FIELD_REQUIRED = "Field tidak boleh kosong"
    const val FIELD_EMAIL = "Field Email tidak valid"
}