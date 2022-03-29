package com.example.flutter_bridge_example

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.example.flutter_bridge.*
import io.flutter.embedding.android.FlutterActivity
import io.flutter.plugin.common.MethodChannel


class MainActivity : FlutterActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        FlutterBridge.instance.registerHandler("startEnterActivity",
            object : MethodHandlerHaveReturn<Map<String, String>> {
                override fun onMethodCall(params: Map<String, Any?>): Map<String, String> {
                    val pageName = (params.get("pageName") as String?) ?: ""
                    var intent = Intent(applicationContext, Class.forName(pageName))
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    applicationContext.startActivity(intent)
                    return hashMapOf("aa" to "bb", "cc" to "dd")
                }
            })

        FlutterBridge.instance.registerHandler("callNativeReturnMap",
            object : MethodHandlerHaveReturn<Map<String, Any>> {
                override fun onMethodCall(params: Map<String, Any?>): Map<String, Any> {

                    Log.e("flutterBridge","callNativeReturnMap params =${params.toString()}")

                    return hashMapOf(
                        "aa" to "bb",
                        "cc" to 1,
                        "dd" to arrayListOf(1, 2, 3),
                        "ee" to true
                    )
                }
            })

        FlutterBridge.instance.registerHandler(
            "getSDKVersion",
            object : MethodHandlerHaveReturn<String> {
                override fun onMethodCall(params: Map<String, Any?>): String {
                    return Build.VERSION.SDK_INT.toString()
                }
            })


        FlutterBridge.instance.registerHandler("callNativeNoReturn",
            object : MethodHandlerNoReturn {
                override fun onMethodCall(params: Map<String, Any?>) {
                    Log.e("MainActivity", "method callNativeNoReturn 被调用了")
                }
            })


        FlutterBridge.instance.registerHandler("requestHttp", object :
                MethodHandlerHaveReturnAsync {
            override fun onMethodCall(params: Map<String, Any?>, result: MethodResult) {

                Log.e("MainActivity", "requestHttp start")

                Thread(Runnable {

                    HttpConnectionUtil().postRequsetAsync(
                        "https://bff.eshetang.com/dashboard/appVersion",
                        HashMap()
                    ) { str ->
                        Log.e("MainActivity", "currentThrad=${Thread.currentThread().name}")
                        result.success(str)
                        Log.e("MainActivity", "requestHttp end result=$str")
                    }
                }).start()

            }
        })

    }


    override fun onDestroy() {
        super.onDestroy()
        FlutterBridge.instance.unRegisterHandler("startEnterActivity")
    }
}
