package com.example.fintrack

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.fintrack.R

class Register1 : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val PREF_NAME = "user_profile_data"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register1)

        // Initialize Views
        val ageInput = findViewById<EditText>(R.id.btnAge)
        val currencyInput = findViewById<EditText>(R.id.btnHeight)
        val countryInput = findViewById<EditText>(R.id.btnCountry)
        val nextButton = findViewById<Button>(R.id.btnNext1)
        val backBtn = findViewById<ImageView>(R.id.btnBack2)

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE)

        nextButton.setOnClickListener {
            val age = ageInput.text.toString().trim()
            val currency = currencyInput.text.toString().trim()
            val country = countryInput.text.toString().trim()

            // Validate form
            if (age.isEmpty() || currency.isEmpty() || country.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Save data to SharedPreferences
                val editor = sharedPreferences.edit()
                editor.putString("age", age)
                editor.putString("currency", currency)
                editor.putString("country", country)
                editor.apply()

                Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show()

                // Go to next screen (if needed)
                val intent = Intent(this, Register2::class.java)
                startActivity(intent)
            }
        }

        // Optional: Go back to previous screen
        backBtn.setOnClickListener {
            finish()
        }
    }
}
