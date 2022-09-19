package com.degpeg.model

import com.google.gson.annotations.SerializedName

internal data class ChatItem(
    val id: String,
    @SerializedName("session_id", alternate = ["liveSessionId"])
    val liveSessionId: String,
    val message: String,
    @SerializedName("time_stamp")
    val timeStamp: String,
    val isPinned: Boolean,
    val userId: String?,
    val userName: String?
) {
    fun getInitials(): String? {
        var str = userName
        if (str.isNullOrEmpty()) str = userId
        return str?.split(' ')
            ?.mapNotNull { it.firstOrNull()?.toString() }
            ?.reduce { acc, s -> acc + s }?.uppercase()
    }

    fun getNonNullUserName(): String {
        return if (userName.isNullOrEmpty()) userId.takeIf { !userId.isNullOrEmpty() } ?: "Unknown"
        else userName
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ChatItem
        if (id != other.id) return false
        if (timeStamp != other.timeStamp) return false
        if (userId != other.userId) return false
        if (userName != other.userName) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + timeStamp.hashCode()
        result = 31 * result + (userId?.hashCode() ?: 0)
        result = 31 * result + (userName?.hashCode() ?: 0)
        return result
    }

}