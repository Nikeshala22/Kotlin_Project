package com.example.fintrack.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fintrack.DeleteBudget
import com.example.fintrack.R
import com.example.fintrack.UpdateBudget
import com.example.fintrack.model.Budget

class BudgetAdapter(private val budgets: MutableList<Budget>) :
    RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder>() {

    fun updateData(newData: List<Budget>) {
        budgets.clear()
        budgets.addAll(newData)
        notifyDataSetChanged()  // Critical for UI updates
    }
    inner class BudgetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val amount: TextView = itemView.findViewById(R.id.amount)
        val date: TextView = itemView.findViewById(R.id.date)
        val btnEdit: ImageView = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageView = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_budget_item, parent, false)
        return BudgetViewHolder(view)
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        val budget = budgets[position]
        holder.title.text = budget.name
        holder.amount.text = "Amount: $${budget.amount}"
        holder.date.text = "From ${budget.startDate} to ${budget.endDate}"

        // Add click listeners for edit/delete if needed
        holder.btnEdit.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, UpdateBudget::class.java)
            intent.putExtra("budget_id", budget.id)
            context.startActivity(intent)
        }

        holder.btnDelete.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DeleteBudget::class.java)
            intent.putExtra("budget_id", budget.id)
            context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int = budgets.size
}
