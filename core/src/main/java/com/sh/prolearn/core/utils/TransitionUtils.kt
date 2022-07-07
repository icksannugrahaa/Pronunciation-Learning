package com.sh.prolearn.core.utils

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup

object TransitionUtils {
    fun setCardTransition(view: View) {
        TransitionManager.beginDelayedTransition(
            view as ViewGroup,
            AutoTransition()
        )
    }
}