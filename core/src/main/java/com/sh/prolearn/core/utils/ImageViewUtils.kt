package com.sh.prolearn.core.utils

import android.annotation.SuppressLint
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.RequestOptions
import com.sh.prolearn.core.R

object ImageViewUtils {
    fun ImageView.loadImageFromLocal(id: Int?) {
        Glide.with(this.context)
            .load(id)
            .loadPlaceholder()
            .into(this)
    }

    fun ImageView.loadImageFromServer(url: String?) {
        Glide.with(this.context)
            .load(url)
            .loadPlaceholder()
            .into(this)
    }

    @SuppressLint("CheckResult", "ObsoleteSdkInt")
    private fun <TranscodeType> RequestBuilder<TranscodeType>.loadPlaceholder(): RequestBuilder<TranscodeType> {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            return this.apply {
                RequestOptions.placeholderOf(R.drawable.ic_loading)
                    .error(R.drawable.ic_broken_image_white)
            }
        }
        return this
    }
}