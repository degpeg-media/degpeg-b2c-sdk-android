package com.degpeg.socket

import com.degpeg.enumuration.AppConfig
import com.degpeg.network.NetworkURL

internal object SocketHelper {
    private const val BASE_URL = "interactionserver.degpeg.com"
    internal const val NS_PUBLISHER = "content-publisher"
    internal const val NS_PROVIDER = "content-provider"

    val SOCKET_URL: String
        get() {
            return when (NetworkURL.appConfig) {
                AppConfig.DEV -> "https://dev.${BASE_URL}:9014/"
                AppConfig.STAGING -> "https://staging.${BASE_URL}:9014/"
                AppConfig.PRODUCTION -> "https://prod1.${BASE_URL}:9015/"
                else -> "https://demo.${BASE_URL}:9014/"
            }
        }


    const val JOIN_SESSION = "join"
    const val LEAVE_SESSION = "leave"
    const val CHAT_MESSAGE = "chat_message"

    const val UPDATE_VIEW = "update_view"
    const val VIEW_COUNT_UPDATED = "view_count_updated"

    const val UPDATE_LIKE = "update_like"
    const val LIKE_COUNT_UPDATED = "like_count_updated"

    const val UPDATE_SHARE = "update_share"
    const val SHARE_COUNT_UPDATED = "share_count_updated"

    const val UPDATE_PURCHASE = "update_purchase"
    const val PURCHASE_COUNT_UPDATED = "purchase_count_updated"

}