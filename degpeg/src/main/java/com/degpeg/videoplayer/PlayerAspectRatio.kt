package com.degpeg.videoplayer

import java.util.*

internal enum class PlayerAspectRatio(val id: Int, val value: String) {

    RESIZE_MODE_FIT(0, "Default"),
    RESIZE_MODE_FIXED_WIDTH(1, "Fixed Width"),
    RESIZE_MODE_FIXED_HEIGHT(2, "Fixed Height"),
    RESIZE_MODE_FILL(3, "Fill Screen"),
    RESIZE_MODE_ZOOM(4, "Zoom");

    companion object {
        private var ENUM_MAP_ID: Map<Int, PlayerAspectRatio>? = null
        private var ENUM_MAP_NAME: Map<String, PlayerAspectRatio>? = null

        operator fun get(name: String?): PlayerAspectRatio {
            if (name == null) return RESIZE_MODE_FIT
            return ENUM_MAP_NAME!![name] ?: RESIZE_MODE_FIT
        }

        operator fun get(id: Int): PlayerAspectRatio {
            return ENUM_MAP_ID!![id] ?: RESIZE_MODE_FIT
        }

        init {
            val map_id: HashMap<Int, PlayerAspectRatio> = HashMap()
            val map_name: HashMap<String, PlayerAspectRatio> = HashMap()

            for (value in values()) {
                map_id[value.id] = value
                map_name[value.value] = value
            }

            ENUM_MAP_ID = Collections.unmodifiableMap(map_id)
            ENUM_MAP_NAME = Collections.unmodifiableMap(map_name)
        }
    }
}