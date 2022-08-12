package com.degpeg.degpeg_sample

import com.degpeg.Controller

class AppController : Controller() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: AppController
    }
}

