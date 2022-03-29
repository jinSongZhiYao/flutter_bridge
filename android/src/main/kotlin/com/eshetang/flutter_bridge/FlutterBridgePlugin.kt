package com.eshetang.flutter_bridge

import android.content.Context
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodChannel

/** FlutterBridgePlugin */
class FlutterBridgePlugin : FlutterPlugin {

    private lateinit var applicationContext: Context
    private lateinit var flutterBridge: FlutterBridge


    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        applicationContext = flutterPluginBinding.applicationContext
        flutterBridge = FlutterBridge.instance
        flutterBridge.context = applicationContext
        flutterBridge.initChannel(flutterPluginBinding.binaryMessenger)

        flutterBridge.registerHandler("toggleLog", object : MethodHandlerNoReturn {
            override fun onMethodCall(params: Map<String, Any?>) {
                L.toggle = params["toggle"] as Boolean
            }

        })
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {

    }

    fun getChannel(): MethodChannel {
        return flutterBridge.channel
    }


}
