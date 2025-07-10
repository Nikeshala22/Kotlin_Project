package com.example.fintrack

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class DeleteTranscation : AppCompatActivity() {

    private lateinit var confirmDeleteBtn: Button
    private lateinit var deleteDateView: TextView
    private lateinit var deleteAmountView: TextView
    private lateinit var deleteCategoryView: TextView
    private lateinit var deleteNoteView: TextView

    private var transactionId: Int = -1
    private var isExpenseOriginal: Boolean = true


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_delete_transcation)

        confirmDeleteBtn= findViewById(R.id.confirmDeleteBtn)
        deleteDateView = findViewById(R.id.deleteDateView)
        deleteAmountView = findViewById(R.id.deleteAmountView)
        deleteCategoryView = findViewById(R.id.deleteCategoryView)
        deleteNoteView = findViewById(R.id.deleteNoteView)

        // Receive the ID from the previous activity

        transactionId = intent.getIntExtra("transaction_id", -1)
        if (transactionId == -1) {
            Toast.makeText(this, "Invalid transaction ID.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        // Load the transaction from SharedPreferences
        val allTransactions = SharedPrefManager.getAllTransactions(this)
        val transaction = allTransactions.find { it.id == transactionId }

        if (transaction != null) {
            // Show transaction details
            deleteDateView.text = "Date: ${transaction.date}"
            deleteAmountView.text = "Amount: ${transaction.amount}"
            deleteCategoryView.text = "Category: ${transaction.category}"
            deleteNoteView.text = "Note: ${transaction.note}"
        } else {
            Toast.makeText(this, "Transaction not found.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Handle deletion
        confirmDeleteBtn.setOnClickListener {
            SharedPrefManager.deleteTransaction(this, transactionId)
            Toast.makeText(this, "Transaction deleted!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}