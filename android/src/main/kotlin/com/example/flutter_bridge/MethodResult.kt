package com.example.flutter_bridge

import io.flutter.plugin.common.MethodChannel

class MethodResult(private val resultOrigin: MethodChannel.Result) {

    fun success(result: Any?) {
        if (HandlerUtils.isMainThread()) {
            resultOrigin.success(result)
        } else {
            HandlerUtils.getMainHandler().post {
                resultOrigin.success(result)
            }
        }
    }

    fun error(errorMessage: String = "", errorCode: String = "", errorDetails: Any = "") {
        if (HandlerUtils.isMainThread()) {
            resultOrigin.error(errorCode, errorMessage, errorDetails)
        } else {
            HandlerUtils.getMainHandler().post {
                resultOrigin.error(errorCode, errorMessage, errorDetails)
            }
        }
    }

}