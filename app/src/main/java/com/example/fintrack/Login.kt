package com.example.fintrack

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Login : AppCompatActivity() {

    lateinit var btnBack1: ImageView
    lateinit var btnContinue: Button
    lateinit var btnGoogle: Button
    lateinit var btnFacebook: Button

    lateinit var btnEmailLogin: EditText
    lateinit var btnPwdLogin: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnBack1 = findViewById(R.id.btnBack1)
        btnContinue = findViewById(R.id.btnContinue)
        btnGoogle = findViewById(R.id.btnGoogle)
        btnFacebook = findViewById(R.id.btnFacebook)

        btnEmailLogin = findViewById(R.id.btnEmailLogin)
        btnPwdLogin = findViewById(R.id.btnPwdLogin)

        btnBack1.setOnClickListener {
            val intent = Intent(this, First::class.java)
            startActivity(intent)
        }

        btnContinue.setOnClickListener {
            val email = btnEmailLogin.text.toString()
            val password = btnPwdLogin.text.toString()

            // Retrieve saved data from SharedPreferences
            val sharedPreferences = getSharedPreferences("user_profile_data", Context.MODE_PRIVATE)
            val savedEmail = sharedPreferences.getString("email", null)
            val savedPassword = sharedPreferences.getString("password", null)

            // Check if entered email and password match saved data
            if (email == savedEmail && password == savedPassword) {
                // Login successful
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Dashboard::class.java) // Redirect to Dashboard
                startActivity(intent)
                finish() // Close login screen
            } else {
                // Login failed
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }

        btnGoogle.setOnClickListener {
            val toast = Toast.makeText(this, "Log in with Google!", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }

        btnFacebook.setOnClickListener {
            val toast = Toast.makeText(this, "Log in with Facebook!", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }
}
