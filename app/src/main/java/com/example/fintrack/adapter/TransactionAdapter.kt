package com.example.fintrack.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.fintrack.DeleteTranscation
import com.example.fintrack.R
import com.example.fintrack.Transaction
import com.example.fintrack.UpdateTransaction

class TransactionAdapter(
    private val context: Context,
    private val transactionList: List<Transaction>
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    private val currencySymbolMap = mapOf(
        "USD" to "$",
        "EUR" to "€",
        "GBP" to "£",
        "INR" to "₹",
        "JPY" to "¥",
        "LKR" to "Rs"
    )

    // Read saved currency and map it to symbol
    private var currencySymbol: String

    init {
        val sharedPrefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val currencyCode = sharedPrefs.getString("currency", "USD") ?: "USD"
        currencySymbol = currencySymbolMap[currencyCode] ?:  currencyCode
    }

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val amount: TextView = itemView.findViewById(R.id.amount)
        val date: TextView = itemView.findViewById(R.id.date)
        val btnEdit: ImageView = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageView = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_transaction_item, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionList[position]

        holder.title.text = transaction.title

        val formattedAmount = String.format("%.2f", transaction.amount)
        holder.amount.text = if (transaction.isExpense) {
            "-$currencySymbol$formattedAmount"
        } else {
            "+$currencySymbol$formattedAmount"
        }

        holder.amount.setTextColor(
            if (transaction.isExpense) Color.RED else Color.parseColor("#388E3C")
        )

        holder.date.text = transaction.date

        holder.btnEdit.setOnClickListener {
            Log.d("TransactionAdapter", "Edit clicked for ${transaction.title}, ID: ${transaction.id}")
            Toast.makeText(context, "Edit clicked for ${transaction.title}", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, UpdateTransaction::class.java)
            intent.putExtra("transaction_id", transaction.id)
            context.startActivity(intent)
        }

        holder.btnDelete.setOnClickListener {
            Log.d("TransactionAdapter", "Delete clicked for ${transaction.title}, ID: ${transaction.id}")
            Toast.makeText(context, "Delete clicked for ${transaction.title}", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, DeleteTranscation::class.java)
            intent.putExtra("transaction_id", transaction.id)
            context.startActivity(intent)
        }
        fun updateCurrency() {
            val sharedPrefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
            val currencyCode = sharedPrefs.getString("currency", "USD") ?: "USD"
            val symbol = currencySymbolMap[currencyCode] ?: currencyCode
            (this as TransactionAdapter).currencySymbol = symbol
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = transactionList.size
}
