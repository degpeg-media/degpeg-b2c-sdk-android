package com.degpeg.socket

import com.degpeg.utility.Log
import io.socket.emitter.Emitter

internal class DisconnectedListener : Emitter.Listener {
    override fun call(vararg arg: Any?) {
        Log.e("Socket", "Disconnected")
        SocketIO.status = SocketStatus.DISCONNECTED
    }
}