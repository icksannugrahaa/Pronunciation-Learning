package com.sh.prolearn.core.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sh.prolearn.core.R
import com.sh.prolearn.core.utils.ImageViewUtils.loadGIFDrawable
import com.sh.prolearn.core.utils.ImageViewUtils.loadImageFromServer

class DialogUtils {
    lateinit var dialog: Dialog

    fun showAlertExitDialog(context: Context, activity: Activity) {
        MaterialAlertDialogBuilder(context)
            .setCancelable(false)
            .setTitle(context.resources.getString(R.string.are_you_sure))
            .setMessage(context.resources.getString(R.string.leave_lesson_message))
            .setNegativeButton(context.resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(context.resources.getString(R.string.yes)) { dialog, _ ->
                dialog.dismiss()
                activity.finish()
            }
            .show()
    }

    fun showConfirmationDialog(message: String, context: Context, myFun: (Boolean) -> Unit) {
        MaterialAlertDialogBuilder(context)
            .setCancelable(false)
            .setTitle(context.resources.getString(R.string.are_you_sure))
            .setMessage(message)
            .setNegativeButton(context.resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
                myFun(true)
            }
            .setPositiveButton(context.resources.getString(R.string.yes)) { dialog, _ ->
                dialog.dismiss()
                myFun(false)
            }
            .show()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setCustomDialog(
        context: Context,
        layoutID: Int,
        isCancelAble: Boolean,
        // if countdown dialog
        isCountDown: Boolean = false,
        countDownState: Boolean = false,
        //if predict dialog
        isPredict: Boolean = false
    ) {
        dialog = Dialog(context)
        dialog.setContentView(layoutID)
        if (isCountDown) {
            dialog.findViewById<ImageView>(R.id.img_countdown).loadGIFDrawable(
                if (countDownState) R.drawable.countdown else R.drawable.countdown_end,
                false
            )
            dialog.findViewById<TextView>(R.id.tv_dialog_title).text =
                if (countDownState) context.resources.getString(R.string.getting_ready) else context.resources.getString(
                    R.string.time_is_up
                )
        } else if (isPredict) {
            dialog.findViewById<ImageView>(R.id.img_countdown).loadGIFDrawable(
                R.drawable.anim_wait_hour,
                false
            )
            dialog.findViewById<TextView>(R.id.tv_dialog_title).text =
                context.resources.getString(R.string.wait_for_score)
        }
        dialog.window?.setBackgroundDrawable(
            context.resources?.getDrawable(
                R.color.transparent,
                context.theme
            )
        )
        dialog.setCanceledOnTouchOutside(isCancelAble)
        dialog.create()
    }

    fun showImageDialog(state: Boolean, image: String) {
        if (state) {
            dialog.show()
            dialog.findViewById<ImageView>(R.id.iv_content).loadImageFromServer(image)
        } else {
            dialog.dismiss()
        }
    }

    fun showCustomDialog(state: Boolean) {
        if (state) {
            dialog.show()
        } else {
            dialog.dismiss()
        }
    }

    fun showLevelUpDialog(context: Context, isLevelUp: Boolean) {
        if(isLevelUp) {
            setCustomDialog(context, R.layout.dialog_level_up, isCancelAble = true)
            showCustomDialog(true)
        }
    }

    fun showNewAchievementDialog(context: Context, isNew: Boolean) {
        if(isNew) {
            setCustomDialog(context, R.layout.dialog_new_achievement, isCancelAble = true)
            showCustomDialog(true)
        }
    }
}