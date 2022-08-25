package com.degpeg.socket

import com.degpeg.socket.SocketHelper.JOIN_SESSION
import com.degpeg.utility.Log
import com.degpeg.utility.Utils
import io.socket.emitter.Emitter
import org.json.JSONObject

internal class ConnectedListener : Emitter.Listener {
    private val callbacks = HashMap<String, ConnectInterface>()

    override fun call(vararg arg: Any?) {
        Log.e("Socket", "Connected")
        SocketIO.status = SocketStatus.CONNECTED
//        SocketIO.emitObj(JOIN_SESSION, JSONObject().put("room", SocketIO.currentSessionId))
        SocketIO.emit(JOIN_SESSION, hashMapOf(Pair("room", SocketIO.currentSessionId)))
        Utils.executeDelay({
            callbacks.values.forEach { it.onConnectAndJoined() }
        }, 1000)
    }

    fun setCallback(tag: String, callback: ConnectInterface) {
        callbacks[tag] = callback
    }

    fun removeCallback(tag: String) {
        callbacks.remove(tag)
    }

    interface ConnectInterface {
        fun onConnectAndJoined()
    }
}
