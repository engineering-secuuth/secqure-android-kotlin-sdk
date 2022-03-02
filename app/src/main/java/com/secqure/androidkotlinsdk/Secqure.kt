package com.secqure.secqureauth

import android.content.Context
import android.content.Intent
import android.os.StrictMode
import android.util.Log
import com.google.gson.JsonParser
import com.secqure.androidkotlinsdk.LoginActivity
import okhttp3.Response
import org.json.JSONObject

class Secqure (private val mContext: Context, private val apiKey: String, private val apiKeySecret: String) {
    fun login(idType: String, callBackClass: String) {
        val secuuthWebSDKURL = SECQURE_WEBSDK_URL
        val intent = Intent(mContext, LoginActivity::class.java).apply {
            putExtra(SECQURE_WEBSDK_URL, secuuthWebSDKURL)
            putExtra(CALLBACK_ACTIVITY_NAME, callBackClass)
        }
        mContext.startActivity(intent)
    }

    fun login(callBackClass: String) {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val shPref_refreshToken = SharedPrefUtils().getFromSharedPrefs(mContext, SECQURE_REFRESH_TOKEN)
        lateinit var response: Response
        if(shPref_refreshToken == "null") {
            freshLogin(callBackClass)
        } else {
            val shPref_userIdentity = SharedPrefUtils().getFromSharedPrefs(mContext, SECQURE_USER_IDENTITY)
            val pubKey = (JsonParser().parse(shPref_userIdentity).asJsonObject).get(SECQURE_PUB_KEY).asString
            val subKey = (JsonParser().parse(shPref_userIdentity).asJsonObject).get(SECQURE_SYSTEM_ID).asString

            val bodyParams = JSONObject()
            bodyParams.put(SECQURE_PUBLIC_KEY, pubKey)
            bodyParams.put(SECQURE_USER_SUB_ID, subKey)
            bodyParams.put(SECQURE_REFRESH_TOKEN, shPref_refreshToken)
            response = AuthUtils().renewTokens(bodyParams, apiKey, apiKeySecret)
            val jsonDataString = response.body()?.string()
            if(response.isSuccessful()) {
                SharedPrefUtils().putInRefreshTokenInSharedPrefs(mContext, jsonDataString.toString())
                val intent = Intent(mContext, Class.forName(callBackClass)).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    putExtra(LOGIN_SUCCESS_MESSAGE, jsonDataString.toString())
                }
                mContext.startActivity(intent)
            } else {
                freshLogin(callBackClass)
            }
        }
    }

    private fun freshLogin(callBackClass: String) {
        val secuuthWebSDKURL = SECQURE_WEBSDK_URL + "?keyId=$apiKey&secretKey=$apiKeySecret&profileName=Default&clientType=Android"
        val intent = Intent(mContext, LoginActivity::class.java).apply {
            putExtra("SECQURE_WEBSDK_URL", secuuthWebSDKURL)
            putExtra("CALLBACK_ACTIVITY_NAME", callBackClass)
        }
        mContext.startActivity(intent)
    }
}