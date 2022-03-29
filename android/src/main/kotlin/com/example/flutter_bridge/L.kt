package com.example.flutter_bridge

import android.util.Log

object L {

    var toggle = false

    fun log(msg: String) {
        if (toggle)
            Log.d("flutterBridge", msg)
    }

}