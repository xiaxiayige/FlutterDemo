package com.example.flutter_app

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import com.example.flutter_app.activity.AliRtcPlayerActivity
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel


class MainActivity : FlutterActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor, "test-Method").setMethodCallHandler { call, result ->
            if (call.method == "showVideo") {
                startActivity()
                result.success("跳转成功")
            }
        }
    }

    fun startActivity() {
        startActivity(Intent(this, AliRtcPlayerActivity::class.java))
    }
}
