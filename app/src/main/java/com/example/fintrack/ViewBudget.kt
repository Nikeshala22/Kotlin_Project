package com.example.fintrack

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial

class ViewBudget : AppCompatActivity() {

    private lateinit var currencyDropdown: AutoCompleteTextView
    private lateinit var btnSaveCurrency: Button
    private lateinit var btnBackup: Button
    private lateinit var btnRestore: Button
    private lateinit var switchDarkMode: SwitchMaterial

    private val currencyList = listOf("USD", "EUR", "GBP", "INR", "JPY", "LKR")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_budget)

        currencyDropdown = findViewById(R.id.spinnerCurrency)
        btnSaveCurrency = findViewById(R.id.btnSaveCurrency)

        switchDarkMode = findViewById(R.id.switchDarkMode)

        setupCurrencyDropdown()
        setupListeners()
        loadDarkModeSetting()

    }
    private fun setupCurrencyDropdown() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, currencyList)
        currencyDropdown.setAdapter(adapter)

        // Load previously saved currency
        val savedCurrency = getSharedPreferences("Settings", Context.MODE_PRIVATE)
            .getString("currency", "")
        currencyDropdown.setText(savedCurrency, false)
    }

    private fun setupListeners() {
        btnSaveCurrency.setOnClickListener {
            val selectedCurrency = currencyDropdown.text.toString()
            getSharedPreferences("Settings", Context.MODE_PRIVATE)
                .edit()
                .putString("currency", selectedCurrency)
                .apply()

            Toast.makeText(this, "Currency saved: $selectedCurrency", Toast.LENGTH_SHORT).show()
            // Restart the Dashboard to reflect currency change
            finish()
            startActivity(Intent(this, Dashboard::class.java))
        }



        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            val mode = if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(mode)
            getSharedPreferences("Settings", Context.MODE_PRIVATE)
                .edit()
                .putBoolean("dark_mode", isChecked)
                .apply()
        }
    }

    private fun loadDarkModeSetting() {
        val isDarkMode = getSharedPreferences("Settings", Context.MODE_PRIVATE)
            .getBoolean("dark_mode", false)
        switchDarkMode.isChecked = isDarkMode
    }

}