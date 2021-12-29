package com.secqure.secqureauth

import android.content.Context
import android.content.Intent
import android.os.StrictMode
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.secqure.androidkotlinsdk.LoginActivity
import okhttp3.Response
import org.json.JSONObject

class Secqure (private val mContext: Context, private val apiKey: String, private val apiKeySecret: String) {
    fun login(idType: String, callBackClass: String) {
        Log.d("Secqure", "Secqure invoked...")
//        val secuuthWebSDKURL = "https://websdk.secuuth.io/?apiKey=$apiKey&apiKeySecret=$apiKeySecret&idType=$idType"
//        val secuuthWebSDKURL = "http://192.168.1.122:5000/getLoginWebview?keyId=$apiKey&profileName=Default"
        val secuuthWebSDKURL = "http://192.168.43.143:8082/webview.html"
//        val secuuthWebSDKURL = "http://192.168.43.143:5000/auth/getLoginWebview?keyId="+ apiKey + "&profileName=Default"
//        val secuuthWebSDKURL = "https://dev.secuuth.io/HTML/index-new.html"
//        val secuuthWebSDKURL = "https://websdk.sawolabs.com/?apiKey=$apiKey&apiKeySecret=$apiKeySecret&identifierType=$identifierType&webSDKVariant=android"
        val intent = Intent(mContext, LoginActivity::class.java).apply {
            putExtra(SECQURE_WEBSDK_URL, secuuthWebSDKURL)
            putExtra(CALLBACK_ACTIVITY_NAME, callBackClass)
        }
        mContext.startActivity(intent)
    }

    fun renewTokens(callBackClass: String) {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val shPref_refreshToken = SharedPrefUtils().getFromSharedPrefs(mContext, "refreshToken")
        Log.d("Secqure", shPref_refreshToken)
        lateinit var response: Response
        if(shPref_refreshToken == "null") {
            freshLogin(callBackClass)
        } else {
            val shPref_userIdentity = SharedPrefUtils().getFromSharedPrefs(mContext, "userIdentity")
            val pubKey = (JsonParser().parse(shPref_userIdentity).asJsonObject).get("pubKey").asString
            val bodyParams = JSONObject()
            bodyParams.put("publicKey", pubKey)
            bodyParams.put("refreshToken", shPref_refreshToken)
            response = AuthUtils().renewTokens(bodyParams)
            val jsonDataString = response.body()?.string()
            Log.d("Secqure", jsonDataString.toString())
//            Log.d("Secqure", "Response code : " + response.code().toString())
            if(response.isSuccessful()) {
                SharedPrefUtils().putInRefreshTokenInSharedPrefs(mContext, jsonDataString.toString())
                val intent = Intent(mContext, Class.forName(callBackClass)).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    putExtra(LOGIN_SUCCESS_MESSAGE, response.toString())
                }
                mContext.startActivity(intent)
//            finish()
            } else {
                Log.d("Secqure", "Failure")
                freshLogin(callBackClass)
            }
        }
    }

    private fun freshLogin(callBackClass: String) {
        Log.d("Secqure", "Secqure invoked...")
        val secuuthWebSDKURL = "http://192.168.43.143:8082/webview.html"
        val intent = Intent(mContext, LoginActivity::class.java).apply {
            putExtra(SECQURE_WEBSDK_URL, secuuthWebSDKURL)
            putExtra(CALLBACK_ACTIVITY_NAME, callBackClass)
        }
        mContext.startActivity(intent)
    }
}