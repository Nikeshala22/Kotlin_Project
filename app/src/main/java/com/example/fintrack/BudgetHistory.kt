// BudgetHistory.kt
package com.example.fintrack

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fintrack.adapter.BudgetAdapter
import com.example.fintrack.model.Budget
import org.json.JSONArray

class BudgetHistory : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BudgetAdapter
    private val PREF_NAME = "BudgetPrefs"
    private val TRANSACTION_HISTORY = "TransactionHistory"
    private lateinit var backButton: ImageView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_history)
        backButton = findViewById(R.id.btnBack1)
        recyclerView = findViewById(R.id.budgetHistoryRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        loadBudgetHistory()

        backButton.setOnClickListener {

            val intent = Intent(this,Dashboard::class.java)
            startActivity(intent)
        }
    }

    // BudgetHistory.kt
    override fun onResume() {
        super.onResume()
        loadBudgetHistory()  // Refresh data when returning from UpdateBudget
    }

    // BudgetHistory.kt
    private fun loadBudgetHistory() {

        val cleanData = sharedPreferences.getString(TRANSACTION_HISTORY, "[]")
            ?.replace("\\\"", "\"")  // Fix escaped quotes
            ?.replace("\\", "")      // Remove other escapes

        sharedPreferences.edit()
            .putString(TRANSACTION_HISTORY, cleanData)
            .apply()

        val budgetList = mutableListOf<Budget>()
        val data = sharedPreferences.getString(TRANSACTION_HISTORY, "[]")

        try {
            val jsonArray = JSONArray(data)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val id = obj.optInt("id", -1)

                if (id == -1) continue  // Skip invalid entries

                budgetList.add(
                    Budget(
                        id,
                        obj.optString("name", ""),
                        obj.optString("amount", ""),
                        obj.optString("startDate", ""),
                        obj.optString("endDate", ""),
                        obj.optString("note", ""),
                        obj.optString("category", "")
                    )
                )
            }

            // Update RecyclerView efficiently
            adapter = BudgetAdapter(budgetList)
            recyclerView.adapter = adapter

        } catch (e: Exception) {
            Log.e("BudgetHistory", "Error: ${e.stackTraceToString()}")
        }
    }
}