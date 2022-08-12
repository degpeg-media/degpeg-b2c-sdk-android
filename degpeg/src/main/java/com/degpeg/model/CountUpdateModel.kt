package com.degpeg.model

import com.google.gson.annotations.SerializedName

internal data class CountUpdateModel(
    val id: String,
    val liveSessionId: String,
    val source: String,
    @SerializedName("time_stamp")
    val timeStamp: String,
    val userId: String
)