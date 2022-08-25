package com.degpeg.model

data class User(
    val userName: String,
    val userId: String? = null
) {
    fun getNonNullUserId(): String {
        if (userId.isNullOrEmpty()) return userName
        return userId
    }
}