package com.degpeg.socket

import com.degpeg.socket.CountModelListener.Companion.LIKE_COUNT
import com.degpeg.socket.CountModelListener.Companion.PURCHASE_COUNT
import com.degpeg.socket.CountModelListener.Companion.SHARE_COUNT
import com.degpeg.socket.CountModelListener.Companion.VIEW_COUNT
import com.degpeg.socket.SocketHelper.CHAT_MESSAGE
import com.degpeg.socket.SocketHelper.DEV_URL
import com.degpeg.socket.SocketHelper.LEAVE_SESSION
import com.degpeg.socket.SocketHelper.LIKE_COUNT_UPDATED
import com.degpeg.socket.SocketHelper.PURCHASE_COUNT_UPDATED
import com.degpeg.socket.SocketHelper.SHARE_COUNT_UPDATED
import com.degpeg.socket.SocketHelper.VIEW_COUNT_UPDATED
import com.degpeg.utility.Log
import com.degpeg.videoplayer.PlayerContentActivity
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

internal object SocketIO {

    private const val SOCKET_URL = DEV_URL

    private val ioOptions: IO.Options
        get() {
            val option = IO.Options.builder().build()
            option.reconnectionAttempts = Integer.MAX_VALUE
            option.reconnection = true
            return option
        }

    private var socket: Socket = IO.socket(SOCKET_URL)
    var status = SocketStatus.DISCONNECTED

    internal var currentSessionId: String = ""
    fun init(contentProviderId: String, sessionId: String) {
        val ioOptions = ioOptions
        this.currentSessionId = sessionId
        ioOptions.query = "contentPublisherId=$contentProviderId"
        Log.d("Socket Url : $SOCKET_URL")
        Log.d("Socket Url : ${ioOptions.query}")
        socket = IO.socket(SOCKET_URL, ioOptions)
    }

    fun connect() {
        if (socket.connected()) return
        if (status == SocketStatus.CONNECTED || status == SocketStatus.CONNECTING) return
        status = SocketStatus.CONNECTING
        setListener()
        socket.connect()
    }

    fun disconnect() {
        if (!socket.connected()) return
        emit(LEAVE_SESSION, JSONObject().put("room", currentSessionId))
        currentSessionId = ""
        socket.off()
        socket.disconnect()
        status = SocketStatus.DISCONNECTED
    }

    private fun setListener() {
        socket.on(Socket.EVENT_CONNECT, connectedListener)
        socket.on(Socket.EVENT_DISCONNECT, DisconnectedListener())
        socket.on(CHAT_MESSAGE, chatMessageListener)
        socket.on(VIEW_COUNT_UPDATED, viewCountListener)
        socket.on(LIKE_COUNT_UPDATED, likeCountListener)
        socket.on(PURCHASE_COUNT_UPDATED, purchaseCountListener)
        socket.on(SHARE_COUNT_UPDATED, shareCountListener)
    }

    fun emit(event: String, jsonObject: JSONObject) {
        if (status == SocketStatus.DISCONNECTED) connect()
        socket.emit(event, jsonObject)
        Log.d("Emitted - $event ==> $jsonObject")
    }

    fun emit(event: String, jsonObject: JSONObject, callback: (value: String?) -> Unit) {
        socket.once(event, object : SocketEmitter() {
            override fun onResponse(value: String) {
                callback.invoke(value)
            }
        })
        emit(event, jsonObject)
    }

    /**
     * event listeners
     * */
    val connectedListener = ConnectedListener()
    val chatMessageListener = ChatMessageListener()
    val viewCountListener = CountModelListener(VIEW_COUNT)
    val likeCountListener = CountModelListener(LIKE_COUNT)
    val purchaseCountListener = CountModelListener(PURCHASE_COUNT)
    val shareCountListener = CountModelListener(SHARE_COUNT)
}