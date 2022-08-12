package com.degpeg.network

internal data class Resource<out T>(
    val status: ApiStatus,
    val data: T? = null,
    val message: String = "",
    val errorCode: Int = 0
) {
    companion object {
        fun <T> success(data: T): Resource<T> = Resource(ApiStatus.SUCCESS, data)
        fun <T> error(message: String, errorCode: Int = 0): Resource<T> =
            Resource(ApiStatus.ERROR, message = message, errorCode = errorCode)
    }
}