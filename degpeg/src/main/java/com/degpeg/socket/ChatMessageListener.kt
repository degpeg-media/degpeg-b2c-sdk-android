package com.degpeg.socket

import com.degpeg.model.ChatItem
import com.degpeg.utility.Log
import com.degpeg.utility.fromJson

internal class ChatMessageListener : SocketEmitter() {
    private val callbacks = HashMap<String, ChatMessageInterface>()

    override fun onResponse(value: String) {
        Log.e("Socket -> ChatMessageListener : $value")
        callbacks.values.forEach { it.onNewMessage(value.fromJson(ChatItem::class.java)) }
    }

    fun setCallback(tag: String, callback: ChatMessageInterface) {
        callbacks[tag] = callback
    }

    fun removeCallback(tag: String) {
        callbacks.remove(tag)
    }

    interface ChatMessageInterface {
        fun onNewMessage(chatItem: ChatItem)
    }
}