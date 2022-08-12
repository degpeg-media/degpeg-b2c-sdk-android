package com.degpeg.model

internal data class ChannelDetail(
    val contentProviderId: String,
    val description: String,
    val id: String,
    val isAutoLogin: Boolean,
    val isSocialChannel: Boolean,
    val logo: String,
    val name: String,
    val rtmpUrls: List<String>,
    val selectedRtmpUrl: String
)