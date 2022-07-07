package com.sh.prolearn.core.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.widget.ImageView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sh.prolearn.core.R
import com.sh.prolearn.core.utils.ImageViewUtils.loadImageFromServer

object DialogUtils {
    lateinit var dialog: Dialog
    fun showConfirmationDialog(message: String, context: Context, myfunc: (Boolean) -> Unit) {
        MaterialAlertDialogBuilder(context)
            .setCancelable(false)
            .setTitle(context.resources.getString(R.string.are_you_sure))
            .setMessage(message)
            .setNegativeButton(context.resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
                myfunc(true)
            }
            .setPositiveButton(context.resources.getString(R.string.yes)) { dialog, _ ->
                dialog.dismiss()
                myfunc(false)
            }
            .show()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setCustomDialog(context: Context, layoutID: Int, isCancelAble: Boolean) {
        dialog = Dialog(context)
        dialog.setContentView(layoutID)
        dialog.window?.setBackgroundDrawable(context.resources?.getDrawable(R.color.transparent, context.theme))
        dialog.setCanceledOnTouchOutside(isCancelAble)
        dialog.create()
    }

    fun showImageDialog(state: Boolean, image: String) {
        if(state) {
            dialog.show()
            dialog.findViewById<ImageView>(R.id.iv_content).loadImageFromServer(image)
        } else {
            dialog.dismiss()
        }
    }

    fun showCustomDialog(state: Boolean) {
        if(state) {
            dialog.show()
        } else {
            dialog.dismiss()
        }
    }
}