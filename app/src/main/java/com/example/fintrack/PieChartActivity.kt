package com.example.fintrack

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import org.json.JSONArray

class PieChartActivity : AppCompatActivity() {

    private lateinit var transactionChart: PieChart
    private lateinit var budgetChart: PieChart
    private lateinit var backButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pie_chart)

        transactionChart = findViewById(R.id.transactionPieChart)
        budgetChart = findViewById(R.id.budgetPieChart)

        setupChart(transactionChart, "")
        setupChart(budgetChart, "")
        backButton = findViewById(R.id.backButton)
        loadTransactionData()
        loadBudgetData()

        backButton.setOnClickListener {

            val intent = Intent(this,Dashboard::class.java)
            startActivity(intent)
        }
    }

    private fun setupChart(chart: PieChart, centerText: String) {
        chart.setUsePercentValues(true)
        chart.description.isEnabled = false
        chart.setExtraOffsets(5f, 10f, 5f, 5f)
        chart.dragDecelerationFrictionCoef = 0.95f
        chart.isDrawHoleEnabled = true
        chart.setHoleColor(Color.WHITE)
        chart.setTransparentCircleColor(Color.WHITE)
        chart.setTransparentCircleAlpha(110)
        chart.holeRadius = 58f
        chart.transparentCircleRadius = 61f
        chart.setDrawCenterText(true)
        chart.centerText = centerText
        chart.rotationAngle = 0f
        chart.isRotationEnabled = true
        chart.isHighlightPerTapEnabled = true
        chart.animateY(1400, Easing.EaseInOutQuad)
        chart.setEntryLabelColor(Color.BLACK)
        chart.setEntryLabelTextSize(12f)

        val legend = chart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)
    }

    private fun loadTransactionData() {
        val transactions = SharedPrefManager.getAllTransactions(this)
        val categoryMap = mutableMapOf<String, Float>()

        transactions.filter { it.isExpense }.forEach {
            categoryMap[it.category] = (categoryMap[it.category] ?: 0f) + it.amount.toFloat()
        }

        val entries = ArrayList<PieEntry>()
        categoryMap.forEach { (cat, amt) -> entries.add(PieEntry(amt, cat)) }

        val dataSet = PieDataSet(entries, "Expenses").apply {
            colors = ColorTemplate.MATERIAL_COLORS.toList()
            sliceSpace = 3f
            selectionShift = 5f
        }

        transactionChart.data = PieData(dataSet).apply {
            setValueFormatter(PercentFormatter(transactionChart))
            setValueTextSize(11f)
            setValueTextColor(Color.BLACK)
        }

        transactionChart.invalidate()
    }
    private fun loadBudgetData() {
        val sharedPreferences = getSharedPreferences("BudgetPrefs", MODE_PRIVATE)
        val jsonString = sharedPreferences.getString("TransactionHistory", "[]") ?: "[]"
        val jsonArray = JSONArray(jsonString)

        val categoryMap = mutableMapOf<String, Float>()

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.optJSONObject(i) ?: continue
            val category = obj.optString("category")
            val amount = obj.optString("amount").toFloatOrNull() ?: continue
            categoryMap[category] = (categoryMap[category] ?: 0f) + amount
        }

        if (categoryMap.isEmpty()) {
            budgetChart.centerText = "No budget data"
            return
        }

        val entries = ArrayList<PieEntry>()
        categoryMap.forEach { (category, amount) ->
            entries.add(PieEntry(amount, category))
        }

        val dataSet = PieDataSet(entries, "").apply {
            sliceSpace = 3f
            selectionShift = 5f
            colors = ColorTemplate.MATERIAL_COLORS.toList()
        }

        val data = PieData(dataSet).apply {
            setValueFormatter(PercentFormatter(budgetChart))
            setValueTextSize(11f)
            setValueTextColor(Color.BLACK)
        }

        budgetChart.data = data
        budgetChart.invalidate()
    }

}
