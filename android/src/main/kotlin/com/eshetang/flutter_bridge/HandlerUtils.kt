package com.eshetang.flutter_bridge

import android.os.Handler
import android.os.Looper

object HandlerUtils {
    /**
     * 获取主线程handler
     */
    fun getMainHandler(): Handler {
        return Handler(Looper.getMainLooper())
    }

    /**
     * 是否是主线程
     */
    fun isMainThread(): Boolean {
        return Looper.getMainLooper().thread === Thread.currentThread()
    }
}