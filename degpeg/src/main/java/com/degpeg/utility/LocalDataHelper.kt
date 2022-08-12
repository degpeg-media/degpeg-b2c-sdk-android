package com.degpeg.utility

import android.content.Context
import android.content.SharedPreferences
import com.degpeg.Controller
import com.degpeg.model.User

internal object LocalDataHelper {
    private var preference = Controller.instance.getSharedPreferences(
        "DegPeg_SDK",
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

    var authToken: String?
        get() = preference.getString("authToken", "")
        set(value) = preference.edit { it.putString("authToken", value) }

    var appUser: User? = null
        get() {
            return if (field == null) {
                field = preference.getString("appUser", "").fromJson(User::class.java)
                field
            } else {
                field
            }
        }
        set(value) {
            field = value
            preference.edit { it.putString("appUser", field.toJson()) }
        }

}
