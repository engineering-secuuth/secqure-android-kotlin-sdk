package com.secqure.secqureauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

class CallbackActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_callback)
//        Toast.makeText(this, "Logged in successfully", Toast.LENGTH_SHORT).show()
        val intent = intent
        val message = intent.getStringExtra("LOGIN_SUCCESS_MESSAGE")
        Log.d("payload_in_callback:", message!!)
    }
}