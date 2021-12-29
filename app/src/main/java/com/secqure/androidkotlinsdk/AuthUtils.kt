package com.secqure.secqureauth

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import okhttp3.Response
import org.json.JSONObject

class AuthUtils() {

    fun renewTokens(payload: JSONObject): Response {
//        val sharedPreferences = context.getSharedPreferences("secqurePref_payload", Context.MODE_PRIVATE)
//        var payload = sharedPreferences.getString("token", null).toString()
//        var userIdentity = SharedPrefUtils().getFromSharedPrefs(context, "userIdentity")
//        var refreshToken = SharedPrefUtils().getFromSharedPrefs(context, "refreshToken")
//        Log.d("AuthUtils", "userIdentity from SharedPref: " + userIdentity)
//        Log.d("AuthUtils", "refreshToken from SharedPref: " + refreshToken)
        val resp: Response = APIClient().executePost("/auth/renewTokens", payload)
        return resp
    }
}