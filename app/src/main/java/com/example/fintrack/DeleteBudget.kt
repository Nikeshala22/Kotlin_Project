package com.example.fintrack

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray

class DeleteBudget: AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private var budgetId: Int = -1

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_delete_budget)

        budgetId = intent.getIntExtra("budget_id", -1)
        sharedPreferences = getSharedPreferences("BudgetPrefs", MODE_PRIVATE)

        if (budgetId != -1) {
            loadBudgetData(budgetId)
        }

        findViewById<Button>(R.id.btnDeleteBudget).setOnClickListener {
            deleteBudget(budgetId)
        }

    }
    private fun loadBudgetData(id: Int) {
        try {
            val data = sharedPreferences.getString("TransactionHistory", "[]") ?: "[]"
            val jsonArray = JSONArray(data)

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.optJSONObject(i) ?: continue
                val currentId = obj.optInt("id", -1)

                if (currentId == id) {
                    // Populate TextViews (not EditTexts)
                    findViewById<TextView>(R.id.viewBudgetName).text = obj.optString("name", "")
                    findViewById<TextView>(R.id.viewBudgetAmount).text = obj.optString("amount", "")
                    findViewById<TextView>(R.id.viewStartDate).text = obj.optString("startDate", "")
                    findViewById<TextView>(R.id.viewEndDate).text = obj.optString("endDate", "")
                    findViewById<TextView>(R.id.viewBudgetNote).text = obj.optString("note", "")
                    return
                }
            }
            Toast.makeText(this, "Budget not found!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error loading budget.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteBudget(id: Int) {
        try {
            val data = sharedPreferences.getString("TransactionHistory", "[]") ?: "[]"
            val jsonArray = JSONArray(data)
            val newArray = JSONArray()

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.optJSONObject(i) ?: continue
                if (obj.optInt("id", -1) != id) {
                    newArray.put(obj)
                }
            }

            // Save updated list
            sharedPreferences.edit()
                .putString("TransactionHistory", newArray.toString())
                .apply()

            Toast.makeText(this, "Budget deleted successfully!", Toast.LENGTH_SHORT).show()
            finish() // Go back to previous screen
        } catch (e: Exception) {
            Toast.makeText(this, "Error deleting budget.", Toast.LENGTH_SHORT).show()
        }
    }


}