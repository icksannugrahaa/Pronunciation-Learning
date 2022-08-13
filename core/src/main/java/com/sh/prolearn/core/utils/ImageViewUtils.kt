package com.sh.prolearn.core.utils

import android.annotation.SuppressLint
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.sh.prolearn.core.R
import com.sh.prolearn.core.utils.Consts.BASE_API_URL
import com.sh.prolearn.core.utils.Consts.BASE_URL

object ImageViewUtils {
    fun ImageView.loadImageFromLocal(id: Int?) {
        Glide.with(this.context)
            .load(id)
            .loadPlaceholder()
            .into(this)
    }

    fun ImageView.loadImageFromServer(url: String?) {
        Glide.with(this.context)
            .load("$BASE_URL$url")
            .loadPlaceholder()
            .into(this)
    }

    fun ImageView.loadGIFDrawable(id: Int, loop: Boolean) {
        Glide.with(this.context)
            .asGif()
            .load(id)
            .listener(object : RequestListener<GifDrawable> {
                override fun onResourceReady(
                    resource: GifDrawable?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    if(!loop) resource?.setLoopCount(1)
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
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