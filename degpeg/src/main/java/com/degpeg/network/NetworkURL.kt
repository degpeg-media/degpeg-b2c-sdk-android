package com.degpeg.network

import com.degpeg.b2csdk.DegpegSDKProvider.PROVIDER_ID
import com.degpeg.b2csdk.DegpegSDKProvider.PUBLISHER_ID
import com.degpeg.b2csdk.DegpegSDKProvider.USER_ROLE
import com.degpeg.b2csdk.UserRole.PROVIDER
import com.degpeg.enumuration.AppConfig.*

internal object NetworkURL {
    val appConfig = PRODUCTION
    val ROOT_URL: String
        get() {
            return when (appConfig) {
                DEV -> "https://dev1.api.degpeg.com/"
                STAGING -> "https://dev1.api.degpeg.com/"
                PRODUCTION -> "https://prod1.api.degpeg.com/"
            }
        }

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

    fun getShareUrl(sessionId: String): String {
        val id = if (USER_ROLE == PROVIDER) PROVIDER_ID else PUBLISHER_ID
        return when (appConfig) {
            DEV -> "https://dev.client.degpeg.com/?id=${id}&session=${sessionId}"
            STAGING -> "https://staging.client.degpeg.com/?id=${id}&session=${sessionId}"
            PRODUCTION -> "https://client.degpeg.com/?id=${id}&session=${sessionId}"
        }
    }
}