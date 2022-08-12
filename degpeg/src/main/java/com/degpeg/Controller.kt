package com.degpeg

import android.app.Application

open class Controller : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: Controller
    }
}

