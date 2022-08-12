package com.degpeg.socket

import com.degpeg.utility.Log
import com.degpeg.utility.gson
import com.degpeg.utility.toJson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import io.socket.emitter.Emitter
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener

internal abstract class SocketEmitter : Emitter.Listener {
    abstract fun onResponse(value: String)
    override fun call(vararg args: Any?) {
        Log.i("Socket : Emitter.Listener", args.toJson())
        for (arg in args) onResponse(arg.toString())
    }

    data class Event<T>(
        val event_type: String,
        val action_type: String,
        @SerializedName("data") val t: T
    ) {

        fun isArray(): Boolean {
            return try {
                JSONTokener(gson.toJson(t)).nextValue() is JSONArray
            } catch (e: java.lang.Exception) {
                false
            }
        }

        fun isObject(): Boolean {
            return try {
                JSONTokener(gson.toJson(t)).nextValue() is JSONObject
            } catch (e: java.lang.Exception) {
                false
            }
        }

        companion object {
            const val TYPE_TASK = "property"

            fun getJson(any: Any?): String? {
                return try {
                    JSONObject(any.toString()).toString()
                } catch (ex: Exception) {
                    JSONArray(any.toJson()).getString(0)
                }
            }

        }

        fun getObjectId(): Long {
            val map: Map<String, String> =
                gson.fromJson(gson.toJson(t), object : TypeToken<Map<String, String>>() {}.type)
            return JSONObject(t.toString()).optLong("id")
        }
    }
}