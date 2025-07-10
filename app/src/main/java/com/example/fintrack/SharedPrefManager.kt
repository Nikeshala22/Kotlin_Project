package com.example.fintrack

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

object SharedPrefManager {

    private const val PREF_NAME = "FinTrackPrefs"
    private const val TRANSACTION_LIST_KEY = "transaction_list"

    // Save a single transaction with proper ID assignment
    // Inside SharedPrefManager
    fun saveTransaction(context: Context, transaction: Transaction) {
        val prefs = context.getSharedPreferences("TransactionPrefs", Context.MODE_PRIVATE)
        val transactions = getAllTransactions(context).toMutableList()
        transactions.add(transaction)
        saveAllTransactions(context, transactions)
    }

    // Get all transactions, gracefully handling missing ID fields
    fun getAllTransactions(context: Context): List<Transaction> {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        // Get the stored JSON string from SharedPreferences
        val jsonString = sharedPreferences.getString(TRANSACTION_LIST_KEY, "[]")

        // Check if the JSON string is valid and not empty
        if (jsonString.isNullOrEmpty()) {
            Log.d("SharedPref", "No transactions found or empty data")
            return emptyList()
        }

        try {
            // Parse the JSON string into a JSONArray
            val jsonArray = JSONArray(jsonString)

            val transactionList = mutableListOf<Transaction>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)

                // Check if the 'id' key is present, if not skip this item
                val id = if (obj.has("id")) obj.getInt("id") else -1 // Defaulting to -1 if missing

                val transaction = Transaction(
                    id = id,
                    title = obj.getString("title"),
                    category = obj.getString("category"),
                    amount = obj.getDouble("amount"),
                    date = obj.getString("date"),
                    isExpense = obj.getBoolean("isExpense"),
                    note = obj.optString("note", ""),
                    account = obj.optString("account", "Cash")
                )
                transactionList.add(transaction)
            }

            Log.d("SharedPref", "All Transactions loaded: ${transactionList.size}")
            return transactionList
        } catch (e: Exception) {
            Log.e("SharedPref", "Error parsing transactions: ${e.message}")
            return emptyList()
        }
    }

    // Generate a unique transaction ID by finding the maximum existing ID
    fun generateTransactionId(context: Context): Int {
        val transactions = getAllTransactions(context)
        return if (transactions.isEmpty()) 1 else transactions.maxOf { it.id } + 1
    }

    // Save the updated list of transactions after modification

    fun saveAllTransactions(context: Context, transactions: List<Transaction>) {
        val jsonArray = JSONArray()
        for (transaction in transactions) {
            val jsonObject = JSONObject().apply {
                put("id", transaction.id)
                put("title", transaction.title)
                put("category", transaction.category)
                put("amount", transaction.amount)
                put("date", transaction.date)
                put("isExpense", transaction.isExpense)
                put("note", transaction.note)
                put("account", transaction.account)
            }
            jsonArray.put(jsonObject)
        }
        val prefs = context.getSharedPreferences("FinTrackPrefs", Context.MODE_PRIVATE)
        prefs.edit().putString("transaction_list", jsonArray.toString()).apply()
    }
    fun updateTransaction(context: Context, updatedTransaction: Transaction) {
        val transactions = getAllTransactions(context).toMutableList()
        val index = transactions.indexOfFirst { it.id == updatedTransaction.id }
        if (index != -1) {
            transactions[index] = updatedTransaction
            saveAllTransactions(context, transactions)
        }

    }
    fun deleteTransaction(context: Context, id: Int) {
        val transactions = getAllTransactions(context).toMutableList()
        val updatedList = transactions.filter { it.id != id }
        saveAllTransactions(context, updatedList)
    }


}
