package com.example.fintrack

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject

class UpdateBudget : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private var budgetId: Int = -1
    private lateinit var categorySpinner: Spinner

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_update_budget)
        categorySpinner = findViewById(R.id.budgetCategorySpinner)

        // Initialize category spinner with predefined categories
        val categories = resources.getStringArray(R.array.budget_categories)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        // Get the budget ID from the Intent
        budgetId = intent.getIntExtra("budget_id", -1)
        sharedPreferences = getSharedPreferences("BudgetPrefs", MODE_PRIVATE)

        // If the budget ID is valid, load its data
        if (budgetId != -1) {
            loadBudgetData(budgetId)
        }

        // Save button click listener
        findViewById<Button>(R.id.saveBudgetBtn).setOnClickListener {
            saveUpdatedBudget(budgetId)
        }
    }

    // Function to load existing budget data
    private fun loadBudgetData(id: Int) {
        try {
            val data = sharedPreferences.getString("TransactionHistory", "[]") ?: "[]"
            val jsonArray = JSONArray(data)

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.optJSONObject(i) ?: continue
                val currentId = obj.optInt("id", -1)

                if (currentId == id) {
                    // Populate the form fields with the current budget data
                    findViewById<EditText>(R.id.budgetName).setText(obj.optString("name", ""))
                    findViewById<EditText>(R.id.budgetAmount).setText(obj.optString("amount", ""))
                    findViewById<EditText>(R.id.startDate).setText(obj.optString("startDate", ""))
                    findViewById<EditText>(R.id.endDate).setText(obj.optString("endDate", ""))
                    findViewById<EditText>(R.id.budgetNote).setText(obj.optString("note", ""))

                    // Set the selected category based on the stored value
                    val category = obj.optString("category", "")
                    val categories = resources.getStringArray(R.array.budget_categories)
                    val categoryIndex = categories.indexOf(category)
                    if (categoryIndex != -1) {
                        categorySpinner.setSelection(categoryIndex)
                    }

                    return
                }
            }
            Toast.makeText(this, "Budget not found!", Toast.LENGTH_SHORT).show()
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, "Error loading budget: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    // Function to save the updated budget data
    private fun saveUpdatedBudget(id: Int) {
        try {
            val data = sharedPreferences.getString("TransactionHistory", "[]") ?: "[]"
            val jsonArray = JSONArray(data)

            // Create a new JSON array to update the data
            val newArray = JSONArray()
            var found = false

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                if (obj.optInt("id", -1) == id) {
                    // Update the matching entry with the new data
                    newArray.put(JSONObject().apply {
                        put("id", id)
                        put("name", findViewById<EditText>(R.id.budgetName).text.toString())
                        put("amount", findViewById<EditText>(R.id.budgetAmount).text.toString())
                        put("startDate", findViewById<EditText>(R.id.startDate).text.toString())
                        put("endDate", findViewById<EditText>(R.id.endDate).text.toString())
                        put("note", findViewById<EditText>(R.id.budgetNote).text.toString())
                        put("category", categorySpinner.selectedItem.toString()) // Save selected category
                    })
                    found = true
                } else {
                    newArray.put(obj)
                }
            }

            // If budget was found and updated, save the new array back to SharedPreferences
            if (found) {
                sharedPreferences.edit()
                    .putString("TransactionHistory", newArray.toString())
                    .apply()
                Toast.makeText(this, "Budget Updated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Budget not found!", Toast.LENGTH_SHORT).show()
            }
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
