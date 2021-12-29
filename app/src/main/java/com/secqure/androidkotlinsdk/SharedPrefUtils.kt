package com.secqure.secqureauth

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONException

class SharedPrefUtils() {

    public fun putInSharedPrefs(context : Context, payload: String) {
        val respPayload: JsonObject = JsonParser().parse(payload).asJsonObject
        val user: JsonObject = JsonParser().parse(respPayload.get("user").toString()).asJsonObject
        val tokens: JsonObject = JsonParser().parse(respPayload.get("tokens").toString()).asJsonObject

        Log.d("JSON_Payload", respPayload.toString())
        Log.d("JSON_Payload_user", respPayload.get("user").toString())
        Log.d("JSON_Payload_user", user.get("pubKey").toString())
        Log.d("JSON_Payload_user", tokens.get("refreshToken").toString())

        val sharedPreferences: SharedPreferences
        val sharedPrefEditor: SharedPreferences.Editor
//        sharedPreferences = getSharedPreferences("secqurePref_payload", MODE_PRIVATE)
        sharedPreferences = context.getSharedPreferences("com.secqure.androidsdk.SECQURE_SHARED_PREF", Context.MODE_PRIVATE)
//        sharedPreferences = getSharedPreferences("com.secqure.androidsdk.SECQURE_SHARED_PREF",
//            AppCompatActivity.MODE_PRIVATE
//        )
        sharedPrefEditor = sharedPreferences.edit()
        try {
//            sharedPrefEditor.putString("token", payload)
            sharedPrefEditor.putString("userIdentity", user.toString())
            sharedPrefEditor.putString("refreshToken", tokens.get("refreshToken").asString)
            Log.d("Login", payload)
            sharedPrefEditor.commit()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    public fun putInRefreshTokenInSharedPrefs(context : Context, payload: String) {
        Log.d("JSON_Payload", payload.toString())
        val respPayload: JsonObject = JsonParser().parse(payload).asJsonObject
//        val tokens: JsonObject = JsonParser().parse(respPayload.get("tokens").toString()).asJsonObject

        Log.d("JSON_Payload", respPayload.toString())
        Log.d("JSON_Payload_user", respPayload.get("refreshToken").toString())

        val sharedPreferences: SharedPreferences
        val sharedPrefEditor: SharedPreferences.Editor
//        sharedPreferences = getSharedPreferences("secqurePref_payload", MODE_PRIVATE)
        sharedPreferences = context.getSharedPreferences("com.secqure.androidsdk.SECQURE_SHARED_PREF", Context.MODE_PRIVATE)
//        sharedPreferences = getSharedPreferences("com.secqure.androidsdk.SECQURE_SHARED_PREF",
//            AppCompatActivity.MODE_PRIVATE
//        )
        sharedPrefEditor = sharedPreferences.edit()
        try {
//            sharedPrefEditor.putString("token", payload)
            sharedPrefEditor.putString("refreshToken", respPayload.get("refreshToken").asString)
            Log.d("Login", payload)
            sharedPrefEditor.commit()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    public fun getFromSharedPrefs(context: Context, key: String): String {
//        val sharedPreferences = getSharedPreferences("com.secqure.androidsdk.SECQURE_SHARED_PREF",
//            AppCompatActivity.MODE_PRIVATE
//        )
        val sharedPreferences = context.getSharedPreferences("com.secqure.androidsdk.SECQURE_SHARED_PREF", Context.MODE_PRIVATE)
        Log.d("JSON_Payload", "sharedPreferences : " + sharedPreferences)
        val value = sharedPreferences.getString(key, null).toString()
        Log.d("JSON_Payload", key + " : " + value)
        return value
    }
}