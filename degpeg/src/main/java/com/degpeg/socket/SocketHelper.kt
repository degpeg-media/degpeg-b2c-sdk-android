package com.degpeg.socket

internal object SocketHelper {
    private const val BASE_URL = "interactionserver.degpeg.com"
    private const val NAME_SPACE = "content-publisher"

    // <port> 9014 for dev.
    const val DEV_URL = "https://dev.${BASE_URL}:9014/${NAME_SPACE}"

    private const val PROD_URL = "https://prod.${BASE_URL}:9014/${NAME_SPACE}"
    private const val STAGING_URL = "https://staging.${BASE_URL}:9014/${NAME_SPACE}"
    private const val DEMO_URL = "https://demo.${BASE_URL}:9014/${NAME_SPACE}"

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