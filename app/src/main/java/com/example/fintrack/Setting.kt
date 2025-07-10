package com.example.fintrack

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton


class Setting : AppCompatActivity() {

    private lateinit var btnBack5: ImageView
    private lateinit var myAcc: AppCompatButton
    private lateinit var edtProfile: AppCompatButton
    private lateinit var myGoals: AppCompatButton
    private lateinit var appearance: AppCompatButton
    private lateinit var dltAcc: AppCompatButton
    private lateinit var btnLogOut: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        // Initialize views
        btnBack5 = findViewById(R.id.btnBack5)
        myAcc = findViewById(R.id.myAcc)
        edtProfile = findViewById(R.id.edtProfile)
        myGoals = findViewById(R.id.myGoals)
        appearance = findViewById(R.id.appearance)
        dltAcc = findViewById(R.id.dltAcc)
        btnLogOut = findViewById(R.id.btnLogOut)

        // Back button functionality
        btnBack5.setOnClickListener {
            finish()
        }

        // My Account button
        myAcc.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
        }

        // Edit Profile button
        edtProfile.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
        }

        // Budget button
        myGoals.setOnClickListener {
            startActivity(Intent(this, ViewBudget::class.java))
        }

        // Expenses button
        appearance.setOnClickListener {
            startActivity(Intent(this, AddTransactions::class.java))
        }

        // Delete Account button
        dltAcc.setOnClickListener {
            // Implement delete account logic here
        }

        // Log Out button
        btnLogOut.setOnClickListener {
            // Clear user data and redirect to login screen
            val prefs = getSharedPreferences("FinTrackPrefs", MODE_PRIVATE)
            prefs.edit().clear().apply()

            val intent = Intent(this, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
