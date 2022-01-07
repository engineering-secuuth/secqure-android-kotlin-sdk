package com.secqure.secqureauth

import android.util.Log
import com.google.gson.Gson
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

private const val TAG = "APIClient"
class APIClient() {
    private var mbody: RequestBody? = null
    private var request: Request? = null
    private val gson = Gson()
    @Throws(IOException::class)
    fun executePost(resourcePath: String, payload: JSONObject?, apiKey: String, secretKey: String): Response {
        val httpClient: OkHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
        val mediaType: MediaType? = MediaType.parse("application/json")
        val url: String = SECQURE_API_URL.toString() + resourcePath
        if (payload != null) {
            mbody = RequestBody.create(mediaType, payload.toString())
        } else {
            mbody = RequestBody.create(mediaType, "{}")
        }
        //        Log.d("Secuuth-SDK", mbody.toString());
        request = Request.Builder()
            .addHeader("keyid", apiKey)
            .url(url)
            .post(mbody)
            .build()
        val call: Call = httpClient.newCall(request)
        val response: Response = call.execute()
//        System.out.println("Response body: " + response.body()?.toString())
//        System.out.println("Response body: " + response.body()?.string())
//        val jsonDataString = response.body()?.toString()
//        Log.d("Secuuth-SDK-response", jsonDataString.toString())
        return if (response.isSuccessful()) { // Success
            Log.d("Secuuth-SDK-response", "Successful response")
            response
        } else { // Failure
            Log.d(TAG, "Failed response while renewing tokens")
            response
        }
    }

    @Throws(IOException::class)
    fun executeGet(resourcePath: String, queryString: String): Response {
        Log.d("Secuuth-SDK", "executeGet invoked")
        val httpClient: OkHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
//        val url: String = SECQURE_API_URL.toString() + resourcePath + "?" + queryString.toString()
        val url = resourcePath
        request = Request.Builder()
            .url(url)
            .build()
        val call: Call = httpClient.newCall(request)
        val response: Response = call.execute()
        println("initAuth Response: " + response.body()?.string())
        Log.d("Secuuth-SDK-response", response.toString())
        return if (response.isSuccessful()) { // Success
            response
        } else { // Failure
            Log.d("Secuuth-SDK-response", "Failed response")
            response
        }
    }
}
