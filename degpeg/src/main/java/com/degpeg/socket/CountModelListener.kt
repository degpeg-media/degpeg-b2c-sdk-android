package com.degpeg.socket

import com.degpeg.model.CountModel
import com.degpeg.utility.Log
import com.degpeg.utility.fromJson

internal class CountModelListener(val type: String) : SocketEmitter() {
    private val callbacks = HashMap<String, CountModelInterface>()

    override fun onResponse(value: String) {
        Log.e("Socket -> CountModelListener : $value")
        callbacks.values.forEach { it.onCountUpdate(value.fromJson(CountModel::class.java), type) }
    }

    fun setCallback(tag: String, callback: CountModelInterface) {
        callbacks[tag] = callback
    }

    fun removeCallback(tag: String) {
        callbacks.remove(tag)
    }

    interface CountModelInterface {
        fun onCountUpdate(countModel: CountModel, type: String)
    }

    companion object {
        const val VIEW_COUNT = "view_count"
        const val LIKE_COUNT = "like_count"
        const val SHARE_COUNT = "share_count"
        const val PURCHASE_COUNT = "purchase_count"
    }
}