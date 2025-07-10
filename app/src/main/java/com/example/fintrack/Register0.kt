package com.example.fintrack

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Register0 : AppCompatActivity() {

    lateinit var btnContinue: Button
    lateinit var btnbtnBack1: ImageView
    lateinit var btnGoogle2: Button
    lateinit var btnFacebook2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register0)

        btnContinue = findViewById<Button>(R.id.btnContinue)
        btnbtnBack1 = findViewById<ImageView>(R.id.btnBack1)
        btnGoogle2 = findViewById(R.id.btnGoogle2)
        btnFacebook2 = findViewById(R.id.btnFacebook2)

        btnContinue.setOnClickListener {
            val intent = Intent(this,Register1::class.java)
            startActivity(intent)
        }

        btnbtnBack1.setOnClickListener {
            val intent = Intent(this,First::class.java)
            startActivity(intent)
        }

        btnGoogle2.setOnClickListener {
            val toast = Toast.makeText(this, "Register with Google", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }

        btnFacebook2.setOnClickListener {
            val toast = Toast.makeText(this, "Register with Facebook", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }
}