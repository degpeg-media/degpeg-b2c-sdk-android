package com.degpeg.network

internal enum class ApiStatus {
    SUCCESS,
    ERROR
}

internal fun ApiStatus.typeCall(
    success: () -> Unit,
    error: () -> Unit,
) {
    when (this) {
        ApiStatus.SUCCESS -> success.invoke()
        ApiStatus.ERROR -> error.invoke()
    }
}