package com.example.fintrack

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fintrack.adapter.TransactionAdapter
import org.json.JSONArray

class Dashboard : AppCompatActivity() {

    private lateinit var budgetText: TextView
    private lateinit var expenseText: TextView
    private lateinit var balanceText: TextView
    private lateinit var btnAdd4: ImageView
    private lateinit var btnHome: ImageView
    private lateinit var btnGraph: ImageView
    private lateinit var btnAdd2: ImageView
    private lateinit var profilePicture: ImageView
    private lateinit var notification: ImageView
    private lateinit var recyclerview: RecyclerView
    private lateinit var transactionAdapter: TransactionAdapter
    private var transactionList: MutableList<Transaction> = mutableListOf()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        budgetText = findViewById(R.id.budget)
        expenseText = findViewById(R.id.expense)
        balanceText = findViewById(R.id.balance)
        btnAdd4 = findViewById(R.id.btnAdd4)
        btnHome = findViewById(R.id.btnHome)
        btnGraph = findViewById(R.id.btnGraph)
        btnAdd2 = findViewById(R.id.btnAdd2)
        profilePicture = findViewById(R.id.profilePicture)
        notification = findViewById(R.id.Btn)
        recyclerview = findViewById(R.id.recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(this)

        loadTransactions()

        transactionAdapter = TransactionAdapter(this, transactionList)
        recyclerview.adapter = transactionAdapter

        val totalExpense = calculateExpense(transactionList)
        val totalBudget = getTotalBudgetAmount()
        updateBudgetInfo(budget = totalBudget, expense = totalExpense)

        btnAdd4.setOnClickListener {
            val intent = Intent(this, AddTransactions::class.java)
            startActivityForResult(intent, ADD_TRANSACTION_REQUEST_CODE)
        }

        btnHome.setOnClickListener {
            startActivity(Intent(this, Dashboard::class.java))
        }

        btnAdd2.setOnClickListener {
            startActivity(Intent(this, AddBudget::class.java))
        }

        profilePicture.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
        }

        notification.setOnClickListener {
            Toast.makeText(this, "Notifications 0", Toast.LENGTH_SHORT).apply {
                setGravity(Gravity.CENTER, 0, 0)
                show()
            }
        }

        btnGraph.setOnClickListener {
            startActivity(Intent(this, PieChartActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadTransactions()
        getTotalBudgetAmount()

        transactionAdapter.notifyDataSetChanged()
        val totalExpense = calculateExpense(transactionList)
        val totalBudget = getTotalBudgetAmount()
        updateBudgetInfo(budget = totalBudget, expense = totalExpense)
    }

    private fun calculateExpense(transactions: List<Transaction>): Int {
        return transactions.filter { it.isExpense }.sumOf { it.amount }.toInt()
    }

    private fun updateBudgetInfo(budget: Int, expense: Int) {
        budgetText.text = "₱$budget"
        expenseText.text = "₱$expense"
        balanceText.text = "₱${budget - expense}"
    }

    private fun getTotalBudgetAmount(): Int {
        val prefs = getSharedPreferences("BudgetPrefs", MODE_PRIVATE)
        val jsonData = prefs.getString("TransactionHistory", "[]") ?: "[]"
        var total = -62523.0
        try {
            val array = JSONArray(jsonData)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val amt = obj.optString("amount", "0").toDoubleOrNull() ?: 0.0
                total += amt
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return total.toInt()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_TRANSACTION_REQUEST_CODE && resultCode == RESULT_OK) {
            transactionList.clear()
            transactionList.addAll(SharedPrefManager.getAllTransactions(this))
            transactionAdapter.notifyDataSetChanged()
        }
    }

    private fun loadTransactions() {
        transactionList.clear()
        transactionList.addAll(SharedPrefManager.getAllTransactions(this))
    }

    private fun addNewTransaction(newTransaction: Transaction) {
        val newId = SharedPrefManager.generateTransactionId(this)
        val transactionWithId = newTransaction.copy(id = newId)
        transactionList.add(transactionWithId)
        transactionAdapter.notifyItemInserted(transactionList.size - 1)
        saveTransactions()
    }

    private fun saveTransactions() {
        SharedPrefManager.saveAllTransactions(this, transactionList)
    }

    companion object {
        const val ADD_TRANSACTION_REQUEST_CODE = 1
    }
}
