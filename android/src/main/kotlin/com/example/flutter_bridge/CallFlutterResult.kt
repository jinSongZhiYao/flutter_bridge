package com.example.flutter_bridge

import io.flutter.plugin.common.MethodChannel

interface CallFlutterResult : MethodChannel.Result {

    override fun error(errorCode: String?, errorMessage: String?, errorDetails: Any?) {
    }

    override fun notImplemented() {
    }
}