package com.secqure.secqureauth

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONException

private const val TAG = "SharedPrefUtils"
class SharedPrefUtils() {

    public fun putInSharedPrefs(context : Context, payload: String) {
        val respPayload: JsonObject = JsonParser().parse(payload).asJsonObject
        val user: JsonObject = JsonParser().parse(respPayload.get(SECQURE_USER).toString()).asJsonObject
        val tokens: JsonObject = JsonParser().parse(respPayload.get(SECQURE_TOKENS).toString()).asJsonObject

        val sharedPreferences: SharedPreferences
        val sharedPrefEditor: SharedPreferences.Editor
//        sharedPreferences = getSharedPreferences("secqurePref_payload", MODE_PRIVATE)
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_FILENAME, Context.MODE_PRIVATE)
//        sharedPreferences = getSharedPreferences("com.secqure.androidsdk.SECQURE_SHARED_PREF",
//            AppCompatActivity.MODE_PRIVATE
//        )
        sharedPrefEditor = sharedPreferences.edit()
        try {
//            sharedPrefEditor.putString("token", payload)
            sharedPrefEditor.putString(SECQURE_USER_IDENTITY, user.toString())
            sharedPrefEditor.putString(SECQURE_REFRESH_TOKEN, tokens.get(SECQURE_REFRESH_TOKEN).asString)
            sharedPrefEditor.commit()
        } catch (e: JSONException) {
            Log.d(TAG, "Exception while storing in SharedPreferences")
            e.printStackTrace()
        }
    }

    public fun putInRefreshTokenInSharedPrefs(context : Context, payload: String) {
        val respPayload: JsonObject = JsonParser().parse(payload).asJsonObject
//        val tokens: JsonObject = JsonParser().parse(respPayload.get("tokens").toString()).asJsonObject

        val sharedPreferences: SharedPreferences
        val sharedPrefEditor: SharedPreferences.Editor
//        sharedPreferences = getSharedPreferences("secqurePref_payload", MODE_PRIVATE)
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_FILENAME, Context.MODE_PRIVATE)
//        sharedPreferences = getSharedPreferences("com.secqure.androidsdk.SECQURE_SHARED_PREF",
//            AppCompatActivity.MODE_PRIVATE
//        )
        sharedPrefEditor = sharedPreferences.edit()
        try {
//            sharedPrefEditor.putString("token", payload)
            sharedPrefEditor.putString(SECQURE_REFRESH_TOKEN, respPayload.get(SECQURE_REFRESH_TOKEN).asString)
            sharedPrefEditor.commit()
        } catch (e: JSONException) {
            Log.d(TAG, "Exception while storing in SharedPreferences")
            e.printStackTrace()
        }
    }

    public fun getFromSharedPrefs(context: Context, key: String): String {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREF_FILENAME, Context.MODE_PRIVATE)
        val value = sharedPreferences.getString(key, null).toString()
        return value
    }
}