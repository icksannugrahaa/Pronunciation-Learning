package com.sh.prolearn.core.data.source.remote.response.module

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommentsItem(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("rating")
    val rating: Double? = null,

    @field:SerializedName("comment")
    val comment: String? = null,

    @field:SerializedName("createdAt")
    val created_at: String? = null,

    @field:SerializedName("avatar")
    val avatar: String? = null
) : Parcelable