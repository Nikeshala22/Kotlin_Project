package com.example.fintrack

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class Register2 : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var pwdEditText: EditText
    private lateinit var repwdEditText: EditText
    private lateinit var checkBox: CheckBox
    private lateinit var finishBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register2)
// Initialize SharedPreferences for storing registration data
        sharedPreferences = getSharedPreferences("user_profile_data", MODE_PRIVATE)
// Initialize UI components
        usernameEditText = findViewById(R.id.btnUsername)
        emailEditText = findViewById(R.id.emailBtn)
        pwdEditText = findViewById(R.id.pwdBtn)
        repwdEditText = findViewById(R.id.repwdBtn)
        checkBox = findViewById(R.id.checkBox)
        finishBtn = findViewById(R.id.btnFinish)
        // Handle the Finish Button click
        finishBtn.setOnClickListener {
            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = pwdEditText.text.toString()
            val confirmPassword = repwdEditText.text.toString()

            // Validation for Username, Email, Password, and Terms
            if (username.isEmpty()) {
                Toast.makeText(this, "Please enter your username", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!isPasswordStrong(password)) {
                Toast.makeText(this, "Password must be at least 8 characters long, " +
                        "contain one uppercase letter, one lowercase letter, one digit, and one special character.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please confirm your password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!checkBox.isChecked) {
                Toast.makeText(this, "Please accept the terms and conditions", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
// Save user data in SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putString("username", username)
            editor.putString("email", email)
            editor.putString("password", password)
            editor.apply()

            Toast.makeText(this, "Registration complete!", Toast.LENGTH_LONG).show()

            // Proceed to next screen (MainActivity or another screen)
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }

    // Function to check if the password is strong
    private fun isPasswordStrong(password: String): Boolean {
        val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\$%^&*(),.?\":{}|<>]).{8,}$"
        return password.matches(passwordPattern.toRegex())
    }
}
