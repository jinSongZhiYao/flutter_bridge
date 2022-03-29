package com.example.flutter_bridge


interface MethodHandler {
}

interface MethodHandlerHaveReturnAsync : MethodHandler {
    fun onMethodCall(params: Map<String, Any?>,result: MethodResult)
}

interface MethodHandlerHaveReturn<R> : MethodHandler {
    fun onMethodCall(params: Map<String, Any?>): R
}

interface MethodHandlerNoReturn : MethodHandler {
    fun onMethodCall(params: Map<String, Any?>)
}