package com.secqure.androidkotlinsdk

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.os.HandlerCompat.postDelayed
import android.content.SharedPreferences
import android.os.StrictMode
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.secqure.androidkotlinsdk.R
import com.secqure.secqureauth.*
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject

private const val TAG = "LoginActivity"
class LoginActivity : AppCompatActivity() {

    private lateinit var callbackActivity: String
    private lateinit var secqureWebSDKURL: String
    private lateinit var mWebView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        secqureWebSDKURL = intent.getStringExtra("SECQURE_WEBSDK_URL").toString()
        callbackActivity = intent.getStringExtra("CALLBACK_ACTIVITY_NAME").toString()

        val connectionManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectionManager.activeNetworkInfo

        mWebView = findViewById(R.id.webview)
        if (networkInfo != null && networkInfo.isConnected == true) {
        } else {
            Toast.makeText(this, "Internet connection unavailable", Toast.LENGTH_LONG).show()
            mWebView.destroy()
        }

        mWebView.settings.javaScriptEnabled = true
        mWebView.settings.domStorageEnabled = true
        mWebView.settings.databaseEnabled = true

        mWebView.webViewClient = object: WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view?.loadUrl(request?.url.toString())
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                mWebView.visibility = View.VISIBLE
            }
        }

        Handler().postDelayed(
            {
                val sharedPreferences = getSharedPreferences(SHARED_PREF_FILENAME, Context.MODE_PRIVATE)
                mWebView.addJavascriptInterface(
                    SecqureWebviewJSInterface(
                        ::returnPayloadToCallbackActivity,
                        sharedPreferences.getString(SHARED_PREF_DEVICE_ID, null).toString()
                    ),
                    "webSDKInterface"
                )
                mWebView.loadUrl(secqureWebSDKURL)
            }, 2000
        )
    }

    private fun returnPayloadToCallbackActivity(payload: String) {
        Log.d("AuthActivity", "Payload received: " + payload)
        SharedPrefUtils().putInSharedPrefs(this@LoginActivity, payload)
        val intent = Intent(this, Class.forName(callbackActivity)).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(LOGIN_SUCCESS_MESSAGE, payload)
        }
        startActivity(intent)
        finish()
    }

    private fun storeInSharedPrefs(payload: String) {
        val respPayload: JsonObject = JsonParser().parse(payload).asJsonObject
        val user: JsonObject = JsonParser().parse(respPayload.get("user").toString()).asJsonObject
        val tokens: JsonObject = JsonParser().parse(respPayload.get("tokens").toString()).asJsonObject

        Log.d("JSON_Payload", respPayload.toString())
        Log.d("JSON_Payload_user", respPayload.get("user").toString())
        Log.d("JSON_Payload_user", user.get("pubKey").toString())

        val sharedPreferences: SharedPreferences
        val sharedPrefEditor: SharedPreferences.Editor
//        sharedPreferences = getSharedPreferences("secqurePref_payload", MODE_PRIVATE)
        sharedPreferences = getSharedPreferences("com.secqure.androidsdk.SECQURE_SHARED_PREF", MODE_PRIVATE)
        sharedPrefEditor = sharedPreferences.edit()
        try {
//            sharedPrefEditor.putString("token", payload)
            sharedPrefEditor.putString("userIdentity", user.toString())
            sharedPrefEditor.putString("refreshToken", tokens.get("refreshToken").toString())
            Log.d("Login", payload)
            sharedPrefEditor.commit()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun retrieveFromSharedPrefs(key: String): String {
        val sharedPreferences = getSharedPreferences("com.secqure.androidsdk.SECQURE_SHARED_PREF", MODE_PRIVATE)
        return sharedPreferences.getString(key, null).toString()
    }
}