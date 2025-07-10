package com.example.fintrack

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        // Delay for 3 seconds and then navigate to the next activity
        Handler(Looper.getMainLooper()).postDelayed({
            // Intent to navigate to the next activity
            val intent = Intent(this, OnBScreen0::class.java)
            startActivity(intent)
            // Finish the current activity so the user can't return to it
            finish()
        }, 3000) // 3000 milliseconds = 3 seconds

    }
}