package com.degpeg.socket

import com.degpeg.utility.Log

internal class CommonEmitter : SocketEmitter() {
    override fun onResponse(value: String) {
        Log.e("Socket -> CommonEmitter : $value")
    }
}