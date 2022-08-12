package com.degpeg.network

internal object NetworkURL {
    const val isDevelopment = true
    private const val DEV_BASE_URL = "https://dev1.api.degpeg.com/"
    private const val LIVE_BASE_URL = "https://dev1.api.degpeg.com/"

    var ROOT_URL = if (isDevelopment) DEV_BASE_URL else LIVE_BASE_URL

    // network constants
    const val DEVICE_TYPE_ANDROID = "Android"
    const val DEVICE_TYPE_IOS = "iOS"

    //headers
    const val HEADER_AUTHORIZATION = "Authorization"
    const val QUERY_AUTHORIZATION = "api_token"
    const val BEARER = "Bearer"
    const val CONTENT_TYPE = "Content-Type"
    const val CONTENT_TYPE_JSON = "application/json"
    const val CONTENT_TYPE_URL_ENCODED = "application/x-www-form-urlencoded"

    // response code
    const val RESPONSE_OK = 200
    const val RESPONSE_CREATED = 201
    const val RES_NOT_FOUND = 404
    const val RES_UNAUTHORISED = 401
    const val RES_BLOCKED = 402
    const val RES_FORBIDDEN = 403
    const val RES_UNPROCESSABLE_ENTITY = 422
    const val RES_SERVER_ERROR = 500
}