package com.sh.prolearn.core.utils

import android.content.Context
import android.widget.Toast

object ToastUtils {
    fun showToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}