// AddBudget.kt
package com.example.fintrack

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.util.Calendar

class AddBudget : AppCompatActivity() {

    private lateinit var budgetName: EditText
    private lateinit var budgetAmount: EditText
    private lateinit var startDate: EditText
    private lateinit var endDate: EditText
    private lateinit var budgetNote: EditText
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button
    private lateinit var backButton: ImageView
    private lateinit var viewBudgetHistoryBtn: Button
    private lateinit var categorySpinner: Spinner
    private lateinit var notificationHelper: NotificationHelper

    private val BUDGET_ID_COUNTER = "BudgetIdCounter"
    private lateinit var sharedPreferences: SharedPreferences
    private val PREF_NAME = "BudgetPrefs"
    private val TRANSACTION_HISTORY = "TransactionHistory"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_budget)

        // View initialization
        budgetName = findViewById(R.id.budgetName)
        budgetAmount = findViewById(R.id.budgetAmount)
        startDate = findViewById(R.id.startDate)
        endDate = findViewById(R.id.endDate)
        budgetNote = findViewById(R.id.budgetNote)
        saveButton = findViewById(R.id.saveBudgetBtn)
        deleteButton = findViewById(R.id.cancelBudgetBtn)
        backButton = findViewById(R.id.btnBack1)
        notificationHelper = NotificationHelper(this)
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        // In BudgetHistory.kt
        val deleteButton = findViewById<Button>(R.id.cancelBudgetBtn)
        deleteButton.setOnClickListener {
            startActivity(Intent(this, Dashboard::class.java))
        }

        // Date pickers
        startDate.setOnClickListener { showDatePicker(startDate) }
        endDate.setOnClickListener { showDatePicker(endDate) }
        categorySpinner = findViewById(R.id.budgetCategorySpinner)

        val categories = resources.getStringArray(R.array.budget_categories)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        backButton.setOnClickListener {

            val intent = Intent(this,Dashboard::class.java)
            startActivity(intent)
        }

        saveButton.setOnClickListener {
            if (validateInputs()) {
                saveBudget()
                startActivity(Intent(this, BudgetHistory::class.java))
                finish()
            }
        }
        val viewBudgetHistoryBtn = findViewById<Button>(R.id.viewBudgetHistoryBtn)
        viewBudgetHistoryBtn.setOnClickListener {
            val intent = Intent(this, BudgetHistory::class.java)
            startActivity(intent)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
        }

    }

    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, y, m, d ->
                editText.setText("%04d-%02d-%02d".format(y, m + 1, d))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun validateInputs(): Boolean {
        return when {
            budgetName.text.isEmpty() -> {
                budgetName.error = "Required"
                false
            }

            budgetAmount.text.isEmpty() -> {
                budgetAmount.error = "Required"
                false
            }

            startDate.text.isEmpty() -> {
                startDate.error = "Required"
                false
            }

            endDate.text.isEmpty() -> {
                endDate.error = "Required"
                false
            }

            else -> true
        }
    }

    private fun saveBudget() {
        // Initialize ID counter if missing
        if (!sharedPreferences.contains(BUDGET_ID_COUNTER)) {
            sharedPreferences.edit().putInt(BUDGET_ID_COUNTER, 0).apply()
        }

        val newId = sharedPreferences.getInt(BUDGET_ID_COUNTER, 0) + 1
        val selectedCategory = categorySpinner.selectedItem.toString()
        // Create JSON object with ID FIRST
        val budgetJson = JSONObject().apply {
            put("id", newId)
            put("name", budgetName.text.toString())
            put("amount", budgetAmount.text.toString())
            put("startDate", startDate.text.toString())
            put("endDate", endDate.text.toString())
            put("note", budgetNote.text.toString())
            put("category", selectedCategory)
        }

        // Update preferences atomically
        sharedPreferences.edit().apply {
            putInt(BUDGET_ID_COUNTER, newId)
            val currentData = JSONArray(sharedPreferences.getString(TRANSACTION_HISTORY, "[]"))
            currentData.put(budgetJson)
            putString(TRANSACTION_HISTORY, currentData.toString())
            apply()
        }
        // ✅ Send notification
        Log.d("BudgetSave", "Sending notification...")
        notificationHelper.sendNotification(
            "Budget Added",
            "Your new budget was added successfully!"
        )
        Toast.makeText(this, "Budget saved!", Toast.LENGTH_SHORT).show()

        }


    }
