package com.secqure.secqureauth

import android.util.Log
import android.webkit.JavascriptInterface
import android.widget.Toast

class SecqureWebviewJSInterface (
    private val returnPayloadToCallbackActivity: (String) -> Unit,
    private val deviceId: (String)
) {
    @JavascriptInterface
    fun handlePostSubmit(payload: String) {
        returnPayloadToCallbackActivity(payload)
    }

    @JavascriptInterface
    fun getDeviceId(): String {
        return deviceId
    }
}