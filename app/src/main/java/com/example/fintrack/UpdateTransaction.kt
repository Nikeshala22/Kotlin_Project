package com.example.fintrack

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UpdateTransaction : AppCompatActivity() {

    private lateinit var editCategory: EditText
    private lateinit var editTitle: EditText
    private lateinit var editAmount: EditText
    private lateinit var editDate: EditText  // Changed from startDate
    private lateinit var editNote: EditText  // Changed from budgetNot
    private lateinit var saveBtn: Button
    private lateinit var cancelBtn: Button

    private var transactionId: Int = -1
    private var isExpenseOriginal: Boolean = true


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_transaction)

        // Initialize views
        editTitle = findViewById(R.id.editTitle)
        editCategory = findViewById(R.id.editCategory)
        editAmount = findViewById(R.id.editAmount)
        editDate = findViewById(R.id.editDate)  // XML has editDate, not startDate
        editNote = findViewById(R.id.editNote)
        saveBtn = findViewById(R.id.saveBtn)    // XML uses saveBtn, not saveBudgetBtn
        cancelBtn = findViewById(R.id.cancelBtn)
        // Receive the ID from the previous activity

        transactionId = intent.getIntExtra("transaction_id", -1)
        if (transactionId == -1) {
            Toast.makeText(this, "Invalid transaction ID.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        /// Load transaction
        val allTransactions = SharedPrefManager.getAllTransactions(this)
        val transaction = allTransactions.find { it.id == transactionId }

        if (transaction != null) {
            isExpenseOriginal = transaction.isExpense
            editTitle.setText(transaction.title)
            editCategory.setText(transaction.category)
            editAmount.setText(transaction.amount.toString())
            editDate.setText(transaction.date)
            editNote.setText(transaction.note)
        } else {
            Toast.makeText(this, "Transaction not found!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Save button
        saveBtn.setOnClickListener {
            val updatedTransaction = Transaction(
                id = transactionId,
                title = editTitle.text.toString(),
                category = editCategory.text.toString(),
                amount = editAmount.text.toString().toDoubleOrNull() ?: 0.0,
                date = editDate.text.toString(),
                isExpense = isExpenseOriginal, // Preserve original type
                note = editNote.text.toString(),
                account = transaction.account // Keep original account
            )

            SharedPrefManager.updateTransaction(this, updatedTransaction)
            Toast.makeText(this, "Transaction updated!", Toast.LENGTH_SHORT).show()
            finish()
        }
        // Cancel button
        cancelBtn.setOnClickListener {
            finish()
        }


        }


    }

