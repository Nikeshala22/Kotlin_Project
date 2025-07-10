package com.example.fintrack

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

private lateinit var notificationHelper: NotificationHelper
class AddTransactions : AppCompatActivity() {

    private lateinit var dateEditText: TextInputEditText
    private lateinit var amountEditText: TextInputEditText
    private lateinit var categoryEditText: TextInputEditText
    private lateinit var titleEditText: TextInputEditText
    private lateinit var accountEditText: TextInputEditText
    private lateinit var noteEditText: TextInputEditText
    private lateinit var saveTransactionBtn: Button
    private lateinit var backButton: ImageView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transactions)

        dateEditText = findViewById(R.id.date)
        amountEditText = findViewById(R.id.amount)
        categoryEditText = findViewById(R.id.category)
        titleEditText = findViewById(R.id.title)
        accountEditText = findViewById(R.id.account)
        noteEditText = findViewById(R.id.note)
        saveTransactionBtn = findViewById(R.id.saveTransactionBtn)
        backButton = findViewById(R.id.btnBack1)
        notificationHelper = NotificationHelper(this)

        dateEditText.setOnClickListener { showDatePicker() }
        categoryEditText.setOnClickListener { showCategoryPicker() }
        accountEditText.setOnClickListener { showAccountPicker() }


        saveTransactionBtn.setOnClickListener { saveTransaction() }

        backButton.setOnClickListener {

            val intent = Intent(this,Dashboard::class.java)
            startActivity(intent)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val formatted = "%02d/%02d/%04d".format(dayOfMonth, month + 1, year)
                dateEditText.setText(formatted)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun showCategoryPicker() {
        val categories = arrayOf("Food", "Transport", "Utilities", "Entertainment", "Other")
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Select Category")
        builder.setItems(categories) { _, which ->
            categoryEditText.setText(categories[which])
        }
        builder.show()
    }

    private fun showAccountPicker() {
        val accounts = arrayOf("Cash", "Bank", "Credit Card")
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Select Account")
        builder.setItems(accounts) { _, which ->
            accountEditText.setText(accounts[which])
        }
        builder.show()
    }

    private fun saveTransaction() {
        val date = dateEditText.text.toString().trim()
        val amountStr = amountEditText.text.toString().trim()
        val category = categoryEditText.text.toString().trim()
        val title = titleEditText.text.toString().trim()
        val account = accountEditText.text.toString().trim()
        val note = noteEditText.text.toString().trim()

        if (date.isEmpty() || amountStr.isEmpty() || category.isEmpty() || title.isEmpty() || account.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountStr.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            return
        }

        val transactionId = SharedPrefManager.generateTransactionId(this)
        Log.d("AddTransactions", "Generated Transaction ID: $transactionId")

        val transaction = Transaction(
            id = transactionId,
            title = title,
            category = category,
            amount = amount,
            date = date,
            isExpense = true,
            note = note,
            account = account
        )

        // Log the transaction before saving
        Log.d("AddTransactions", "Saving transaction: $transaction")

        SharedPrefManager.saveTransaction(this, transaction)
        Log.d("AddTransactions", "Saved: $transaction")

        val resultIntent = Intent()
        resultIntent.putExtra("newTransaction", transaction)
        setResult(Activity.RESULT_OK, resultIntent)

        // Log the updated transaction list after saving
        val transactions = SharedPrefManager.getAllTransactions(this)
        Log.d("AddTransactions", "All transactions: $transactions")

        // ✅ Send notification
        Log.d("Expense tSave", "Sending notification...")
        notificationHelper.sendNotification(
            "Expense Added",
            "Your new expense was added successfully!"
        )

        Toast.makeText(this, "Expense saved successfully", Toast.LENGTH_SHORT).show()
        finish()

    }

}
