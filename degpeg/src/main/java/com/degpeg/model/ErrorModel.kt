package com.degpeg.model

internal data class ErrorModel(
    var message: String,
    val name: String,
    var statusCode: Int
) {
    val code: String = ""
}