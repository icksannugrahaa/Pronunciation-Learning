package com.sh.prolearn.core.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.sh.prolearn.core.domain.model.Predict
import com.sh.prolearn.core.domain.model.Progress
import com.sh.prolearn.core.domain.model.Scores
import com.sh.prolearn.core.utils.Consts.SHARED_PREFS_CURRENT_PREDICT
import com.sh.prolearn.core.utils.Consts.SHARED_PREFS_CURRENT_PROGRESS
import com.sh.prolearn.core.utils.Consts.SHARED_PREFS_CURRENT_SCORE
import com.sh.prolearn.core.utils.Consts.SHARED_PREFS_LOGIN
import com.sh.prolearn.core.utils.Consts.SHARED_PREFS_PLAYER_ANSWER
import com.sh.prolearn.core.utils.Consts.SHARED_PREFS_PLAYER_QUEST
import com.sh.prolearn.core.utils.Consts.SHARED_PREFS_RECORD_FILE
import com.sh.prolearn.core.utils.Consts.SHARED_PREFS_RECORD_STATE

class ProgressPreferences(context: Context) {
    private var preference: SharedPreferences

    init {
        preference = context.getSharedPreferences(SHARED_PREFS_CURRENT_PROGRESS, Context.MODE_PRIVATE)
    }

    val progressData: Progress?
        get() = Gson().fromJson(
            preference.getString(SHARED_PREFS_CURRENT_PROGRESS, null),
            Progress::class.java
        ) ?: null

    val scoreData: Scores?
        get() = Gson().fromJson(
            preference.getString(SHARED_PREFS_CURRENT_SCORE, null),
            Scores::class.java
        ) ?: null

    val predictData: Predict?
        get() = Gson().fromJson(
            preference.getString(SHARED_PREFS_CURRENT_PREDICT, null),
            Predict::class.java
        ) ?: null

    val currentRecordStatus: String
        get() {
            return preference.getString(SHARED_PREFS_RECORD_STATE, "null") ?: "null"
        }

    val currentRecordFilename: String
        get() {
            return preference.getString(SHARED_PREFS_RECORD_FILE, "null") ?: "null"
        }

    val playerQuestState: String
        get() {
            return preference.getString(SHARED_PREFS_PLAYER_QUEST, "null") ?: "null"
        }

    val playerAnswerState: String
        get() {
            return preference.getString(SHARED_PREFS_PLAYER_ANSWER, "null") ?: "null"
        }

    fun saveCurrentProgress(
        progress: Progress,
        score: Scores
    ) {
        val editor = preference.edit()
        val progressData = Gson().toJson(progress)
        val scoreData = Gson().toJson(score)
        editor.putString(SHARED_PREFS_CURRENT_PROGRESS, progressData)
        editor.putString(SHARED_PREFS_CURRENT_SCORE, scoreData)
        editor.apply()
    }

    fun saveCurrentPredict(
        predict: Predict
    ) {
        val editor = preference.edit()
        val predictData = Gson().toJson(predict)
        editor.putString(SHARED_PREFS_CURRENT_PREDICT, predictData)
        editor.apply()
    }

    fun saveRecordFilename(
        recordFilename: String
    ) {
        val editor = preference.edit()
        editor.putString(SHARED_PREFS_RECORD_FILE, recordFilename)
        editor.apply()
    }

    fun saveRecordStatus(
        recordStatus: String
    ) {
        val editor = preference.edit()
        editor.putString(SHARED_PREFS_RECORD_STATE, recordStatus)
        editor.apply()
    }

    fun savePlayerQuest(
        recordStatus: String
    ) {
        val editor = preference.edit()
        editor.putString(SHARED_PREFS_PLAYER_QUEST, recordStatus)
        editor.apply()
    }

    fun savePlayerAnswer(
        recordStatus: String
    ) {
        val editor = preference.edit()
        editor.putString(SHARED_PREFS_PLAYER_ANSWER, recordStatus)
        editor.apply()
    }
}