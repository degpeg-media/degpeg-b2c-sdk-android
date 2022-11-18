package com.degpeg.model

class AppUiConfig() {
    var isChatEnable: Boolean = true
    var isLikeEnable: Boolean = true
    var isProductEnable: Boolean = true
    var isShareEnable: Boolean = true
    var isMuteEnable: Boolean = true

    constructor(
        isChatEnable: Boolean,
        isLikeEnable: Boolean,
        isProductEnable: Boolean,
        isShareEnable: Boolean,
        isMuteEnable: Boolean
    ) : this() {
        this.isChatEnable = isChatEnable
        this.isLikeEnable = isLikeEnable
        this.isProductEnable = isProductEnable
        this.isShareEnable = isShareEnable
        this.isMuteEnable = isMuteEnable
    }
}