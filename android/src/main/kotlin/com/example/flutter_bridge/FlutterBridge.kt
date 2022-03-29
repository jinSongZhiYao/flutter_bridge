package com.example.flutter_bridge

import android.content.Context
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class FlutterBridge private constructor() : MethodChannel.MethodCallHandler {

    lateinit var context: Context //applicationContext
    lateinit var channel: MethodChannel

    private val methodMap = hashMapOf<String, MethodHandler>()


    companion object {
        val instance: FlutterBridge by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { FlutterBridge() }
        const val CHANNEL_NAME = "flutterBridge/core"
        const val ERROR_CODE_ILLEGAL_METHODHANDLER = "0001"
        const val ERROR_CODE_ERROR = "0002" // 调用报错
    }

    fun initChannel(messenger: BinaryMessenger) {
        channel = MethodChannel(messenger, CHANNEL_NAME)
        channel.setMethodCallHandler(this)
    }


    /**
     * 注册方法
     */
    fun registerHandler(methodName: String, methodHandler: MethodHandler) {
        methodMap[methodName] = methodHandler
    }

    /**
     * 反 注册方法
     */
    fun unRegisterHandler(methodName: String): MethodHandler? {
        return methodMap.remove(methodName)
    }


    /**
     * 调用flutter方法
     * @methodName: 方法名
     * @param:方法入参
     * @return : 方法返回
     */
    fun <R> callFlutter(
        methodName: String,
        params: Map<String, Any?> = HashMap(),
        callBack: HandleCallBack<R> = DefaultHandleCallBack<R>()
    ) {

        if (HandlerUtils.isMainThread()) {
            callFlutterInner(methodName, params, callBack)
        } else {
            HandlerUtils.getMainHandler().post {
                callFlutterInner(methodName, params, callBack)
            }
        }

    }

    private fun <R> callFlutterInner(
        methodName: String,
        params: Map<String, Any?> = HashMap(),
        callBack: HandleCallBack<R> = DefaultHandleCallBack<R>()
    ) {
        channel.invokeMethod(methodName, params, object : CallFlutterResult {
            override fun success(result: Any?) {
                if (result is String && result == "methodNotImplemented") {
                    callBack.notImplemented()//自己实现 notImplemented
                } else {
                    callBack.callSuccess(result as R?)
                }
            }
        })
    }


    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        try {
            L.log("flutter call native  method=${call.method} arguments=${call.arguments}")
            val methodName = call.method ?: ""
            val params =
                if (call.arguments == null) HashMap()
                else call.arguments as Map<String, Any?>
            val methodHandle = methodMap[methodName]
            if (methodHandle != null) {
                when (methodHandle) {
                    is MethodHandlerHaveReturn<*> -> {
                        result.success(methodHandle.onMethodCall(params))
                    }
                    is MethodHandlerNoReturn -> {
                        methodHandle.onMethodCall(params)
                        result.success("No Return Method Handler")
                    }
                    is MethodHandlerHaveReturnAsync -> {
                        methodHandle.onMethodCall(params, MethodResult(result))
                    }
                    else -> {
                        result.error(
                            ERROR_CODE_ILLEGAL_METHODHANDLER,
                            "illegal MethodHandler",
                            null
                        )
                    }
                }
            } else {
                result.notImplemented()
            }
        } catch (e: Exception) {
            result.error(ERROR_CODE_ERROR, e.message, e)
        }
    }
}