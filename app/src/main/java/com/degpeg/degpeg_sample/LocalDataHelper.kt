package com.degpeg.degpeg_sample

import android.content.Context
import android.content.SharedPreferences
import com.degpeg.Controller
import com.degpeg.b2csdk.UserRole
import com.degpeg.model.User

internal object LocalDataHelper {
    private var preference = Controller.instance.getSharedPreferences(
        "DegPeg_Sample",
        Context.MODE_PRIVATE
    )

    private fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    fun clearPreference() {
        val editor = preference.edit()
        editor.clear()
        editor.apply()
    }

    var appId: String
        get() = preference.getString("appId", "degpegdegpeg _mediaXuUwyvni") ?: "degpegdegpeg _mediaXuUwyvni"
        set(value) = preference.edit { it.putString("appId", value) }

    var secretKey: String
        get() = preference.getString("secretKey", "Nnra8P2iGqT2uJFU") ?: "Nnra8P2iGqT2uJFU"
        set(value) = preference.edit { it.putString("secretKey", value) }

    var publisherId: String
        get() = preference.getString("publisherId", "6007cf41f2895e2eabcc2ac2") ?: "6007cf41f2895e2eabcc2ac2"
        set(value) = preference.edit { it.putString("publisherId", value) }

    var providerId: String
        get() = preference.getString("providerId", "62da7cd386d452b1b9cf5f3e") ?: "62da7cd386d452b1b9cf5f3e"
        set(value) = preference.edit { it.putString("providerId", value) }

    var userRole: UserRole
        get() = UserRole[preference.getString("userRole","")]
        set(value) = preference.edit { it.putString("userRole", value.value) }
}
