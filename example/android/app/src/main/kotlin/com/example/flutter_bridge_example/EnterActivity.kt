package com.example.flutter_bridge_example

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.flutter_bridge.FlutterBridge
import com.example.flutter_bridge.HandleCallBack
import com.example.flutter_bridge.HandleCallBackNoReturn
import io.flutter.embedding.android.FlutterActivity

class EnterActivity : FlutterActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv = findViewById<TextView>(R.id.tv)

        findViewById<View>(R.id.btn).setOnClickListener {
            FlutterBridge.instance.callFlutter(
                "getFlutterVersion",
                callBack = object : HandleCallBack<String> {
                    override fun callSuccess(result: String?) {
                        tv.text = "调用flutter方法-不带入参-返回String  result=$result"
                    }

                })

        }

        findViewById<View>(R.id.btn_map).setOnClickListener {
            FlutterBridge.instance.callFlutter(
                "getFlutterMap",
                params = hashMapOf(
                    "aa" to "bb",
                    "cc" to 1,
                    "dd" to arrayListOf(1, 2, 3),
                    "ee" to true
                ),
                callBack = object : HandleCallBack<Map<String, Any?>> {
                    override fun callSuccess(result: Map<String, Any?>?) {
                        tv.text = "调用flutter方法-带入参-返回Map result=${result.toString()}"
                    }

                })

        }

        findViewById<View>(R.id.btn_noreturn).setOnClickListener {
            FlutterBridge.instance.callFlutter(
                "callFlutterNoReturn",
                params = hashMapOf(
                    "aa" to "bb",
                    "cc" to 1,
                    "dd" to arrayListOf(1, 2, 3),
                    "ee" to true
                ),
                callBack = object : HandleCallBackNoReturn {
                    override fun callSuccess() {
                        tv.text = "调用flutter方法-带入参-无返回值"
                    }


                })

        }

        findViewById<View>(R.id.btn_notImplemented).setOnClickListener {
            FlutterBridge.instance.callFlutter(
                "methodNotImplemented",
                params = hashMapOf("aa" to "bb"),
                callBack = object : HandleCallBack<Map<String, Any?>> {
                    override fun callSuccess(result: Map<String, Any?>?) {
                        tv.text = "调用flutter方法-没有实现的情况 result=${result.toString()}"
                    }

                })

        }


        findViewById<View>(R.id.btn_not_main_thread).setOnClickListener {
            Thread(Runnable {
                FlutterBridge.instance.callFlutter(
                    "getFlutterVersion",
                    callBack = object : HandleCallBack<String> {
                        override fun callSuccess(result: String?) {
                            tv.text = "调用flutter方法-不带入参-返回String  result=$result"
                        }

                    })
            }).start()

        }

    }

}