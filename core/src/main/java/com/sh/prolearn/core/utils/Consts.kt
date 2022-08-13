package com.sh.prolearn.core.utils

object Consts {
    //API
//    const val BASE_URL = "http://10.0.2.2:5000" // for local device
//    const val BASE_API_URL = "http://10.0.2.2:5000/api/"
    const val BASE_URL = "http://192.168.100.164:5000" // for local network
    const val BASE_API_URL = "http://192.168.100.164:5000/api/"
    const val AUTH_LOGIN_PATH = "auth/login"
    const val AUTH_REGISTER_PATH = "auth/register"
    const val AUTH_LOGOUT_PATH = "auth/logout"
    const val AUTH_LOGIN_EXPIRED = "auth/expire"
    const val ACCOUNT_PATH = "auth/me"
    const val UPLOAD_PATH = "file/upload"
    const val PREDICT_PATH = "predict/get"
    const val TTS_PATH = "predict/tts"
    const val MODULES_DATA_PATH = "modules/data"
    const val MODULES_STORE_REVIEW_PATH = "modules/store-review"
    const val MODULES_UPDATE_REVIEW_PATH = "modules/update-review"
    const val SCOREBOARD_DATA_PATH = "modules/scoreboard"
    const val PROGRESS_DATA_PATH = "progress/data"
    const val PROGRESS_STORE_PATH = "progress/store"

    const val DEFAULT_USER_IMAGE = "/api/file/show?filename=guest.png&path=/images"

//    COMPANION OBJECT
    const val EXTRA_MODULE_ID = "extra_module_id"
    const val EXTRA_MODULE_DATA = "extra_module_data"
    const val EXTRA_LESSON_DATA = "extra_lesson_data"
    const val EXTRA_QUESTION_DATA = "extra_quest_data"
    const val EXTRA_MODULE_PROGRESS_DATA = "extra_module_progress_data"
    const val ARG_LESSON_DATA = "lesson_data"
    const val ARG_THEORY_DATA = "theory_data"
    const val ARG_QUIZ_DATA = "quiz_data"
    const val ARG_SUMMARY_DATA = "summary_data"
    const val ARG_LESSON_PROGRESS = "lesson_progress"
    const val ARG_LEVEL_UP = "level_up"
    const val ARG_REVIEW_DATA = "review_data"
    const val ARG_OPTION_DATA = "option_data"
    const val ARG_LESSON_TYPE = "type_data"
    const val ARG_LESSON_CODE = "code_lesson_data"
    const val SAVED_STATE_CURRENT_TAB_KEY = "state_current_tab_key"

    //SHARED PREFERENCE
    const val SHARED_PREFS_LOGIN = "pref_login"
    const val SHARED_PREFS_LOGIN_DATE = "pref_login_date"
    const val SHARED_PREFS_LOGIN_EXPIRE = "pref_login_expire"
    const val SHARED_PREFS_LOGIN_DEVICE = "pref_login_device"
    const val SHARED_PREFS_LOGIN_API = "pref_login_device"
    const val SHARED_PREFS_LOGIN_MODEL = "pref_login_device"
    const val SHARED_PREFS_LOGIN_PRODUCT = "pref_login_device"
    const val SHARED_PREFS_LOGIN_IP = "pref_login_ip"
    const val SHARED_PREFS_LOGIN_ACCOUNT = "pref_login_account"
    const val SHARED_PREFS_RECORD_STATE = "pref_record_state"
    const val SHARED_PREFS_RECORD_FILE = "pref_record_file"
    const val SHARED_PREFS_CURRENT_PROGRESS = "pref_current_progress"
    const val SHARED_PREFS_CURRENT_SCORE = "pref_current_score"
    const val SHARED_PREFS_CURRENT_PREDICT = "pref_current_predict"
    const val SHARED_PREFS_CURRENT_LESSON = "pref_lesson"
    const val SHARED_PREFS_CURRENT_LESSON_PROGRESS = "pref_lesson_progress"
    const val SHARED_PREFS_CURRENT_LESSON_STATUS = "pref_lesson_status"
    const val SHARED_PREFS_CURRENT_LESSON_TIME = "pref_lesson_time"
    const val SHARED_PREFS_CURRENT_LESSON_SCORE = "pref_lesson_score"
    const val SHARED_PREFS_CURRENT_LESSON_SCORE_CODE = "pref_lesson_code"
    const val SHARED_PREFS_LAST_LEARN = "pref_last_learn"
    const val SHARED_PREFS_LAST_LEARN_STATUS = "pref_last_learn_status"
    const val SHARED_PREFS_PLAYER_QUEST = "pref_player_quest"
    const val SHARED_PREFS_PLAYER_ANSWER = "pref_player_answer"

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
    const val MESSAGE_201 = "Data berhasil dimasukan!"

    //FIELD MESSAGE
    const val FIELD_REQUIRED = "Field tidak boleh kosong"
    const val FIELD_PASSOWRD_MUST_SAME = "Field password dan konfirmasi harus sama"
    const val FIELD_EMAIL = "Field Email tidak valid"
}