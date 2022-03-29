package com.example.flutter_bridge

import android.widget.Toast
import com.example.flutter_bridge.FlutterBridge


interface HandleCallBack<R> {
    fun callSuccess(result: R?)
    fun callError(errorMessage: String = "", errorCode: String = "", errorDetails: Any = "") {
        L.log("errorCode=$errorCode errorMessage=$errorMessage errorDetails=${errorDetails.toString()}")
    }

    fun notImplemented() {
        Toast.makeText(FlutterBridge.instance.context, "flutter没有找到对应方法", Toast.LENGTH_LONG).show()
    }
}

interface HandleCallBackNoReturn : HandleCallBack<String> {
    override fun callSuccess(result: String?) {
        callSuccess()
    }

    fun callSuccess()
}


class DefaultHandleCallBack<R> : HandleCallBack<R> {
    override fun callSuccess(result: R?) {
    }


}