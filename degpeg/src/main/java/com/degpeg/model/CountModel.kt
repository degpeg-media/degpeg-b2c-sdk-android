package com.degpeg.model

import com.degpeg.utility.formatAsViewCount
import com.google.gson.annotations.SerializedName

internal class CountModel() {
    var count: Long = 0

    @SerializedName("session_id")
    var sessionId: String = ""

    constructor(count : Long, sessionId: String):this(){
        this.count = count
        this.sessionId = sessionId
    }

    fun getFormatted(): String {
        return count.formatAsViewCount()
    }
}