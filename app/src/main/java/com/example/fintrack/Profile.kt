package com.example.fintrack
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Profile : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var nameTextView: TextView
    private lateinit var ageTextView: TextView
    private lateinit var currencyTextView: TextView
    private lateinit var countryTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var profilePic: ImageView
    private lateinit var btnSettings: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
// Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("user_profile_data", MODE_PRIVATE)

        // Initialize the TextViews
        nameTextView = findViewById(R.id.textName)
        ageTextView = findViewById(R.id.textAge)
        countryTextView = findViewById(R.id.textCountry)
        currencyTextView = findViewById(R.id.textCurrency)
        emailTextView = findViewById(R.id.textEmail)
        profilePic = findViewById(R.id.profilePic)
        btnSettings = findViewById(R.id.btnSettings)
// Handle clicks
        btnSettings.setOnClickListener {
            val intent = Intent(this, Setting::class.java)
            startActivity(intent)
        }

        profilePic.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
        }
        // Retrieve the stored user data from SharedPreferences
        val name = sharedPreferences.getString("username", "N/A")
        val age = sharedPreferences.getString("age", "N/A")
        val country = sharedPreferences.getString("country", "N/A")
        val currency = sharedPreferences.getString("currency", "N/A")
        val email = sharedPreferences.getString("email", "N/A")



        // Display the data in the TextViews
        nameTextView.text = name
        ageTextView.text = age
        countryTextView.text = country
        currencyTextView.text = currency
        emailTextView.text = email



    }
}