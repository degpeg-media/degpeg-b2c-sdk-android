package com.degpeg.utility

internal object Constants {
    const val TEST_LIVE_STREAM_URL =
        "https://cph-p2p-msl.akamaized.net/hls/live/2000341/test/master.m3u8"
    //    "rtmp://prod.mediaserver.degpeg.com/live/"

    const val TEST_ON_DEMAND_VIDEO =
        "https://multiplatform-f.akamaihd.net/i/multi/will/bunny/big_buck_bunny_,640x360_400,640x360_700,640x360_1000,950x540_1500,.f4v.csmil/master.m3u8"

    const val SEARCH_REQUEST_DELAY = 1000L

    internal const val MOBILE_SOURCE = "mobile"

    val mention: String = "mention"
    val posts: String = "posts"
    val user: String = "user"
    val id: String = "id"

    const val EXTRA_TAG_LIST = "extra_tag_list"

    const val ACTION_FOR_BIDDEN_RESPONSE = "action_for_bidden_response"
    const val ACTION_FOR_BLOCKED = "action_for_blocked"
}
