package com.secqure.secqureauth

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import okhttp3.Response
import org.json.JSONObject

class AuthUtils() {

    fun renewTokens(payload: JSONObject, apiKey: String, secretKey: String): Response {
        payload.put(SECQURE_RENEW_REFRESH_TOKEN, "true")
        val resp: Response = APIClient().executePost("/auth/renewTokens", payload, apiKey, secretKey)
        return resp
    }
}