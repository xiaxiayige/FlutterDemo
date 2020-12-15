package com.example.flutter_app

import android.content.Context
import androidx.multidex.MultiDex
import io.flutter.app.FlutterApplication
import kotlin.properties.Delegates

class App : FlutterApplication() {

    override fun onCreate() {
        super.onCreate()
        mApp = this
    }

    companion object {
        private var mApp: App by Delegates.notNull<App>()

        fun getInstance() = mApp
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }
}