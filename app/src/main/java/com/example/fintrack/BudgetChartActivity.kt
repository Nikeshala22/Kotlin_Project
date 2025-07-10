package com.example.fintrack

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
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

class BudgetChartActivity : AppCompatActivity() {

    private lateinit var pieChart: PieChart

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_budget_chart)

        pieChart = findViewById(R.id.pieChart)
        setupPieChart()
        loadBudgetData()
    }

    private fun setupPieChart() {
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        pieChart.dragDecelerationFrictionCoef = 0.95f
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)
        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)
        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f
        pieChart.setDrawCenterText(true)
        pieChart.rotationAngle = 0f
        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true
        pieChart.animateY(1400, Easing.EaseInOutQuad)

        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.setEntryLabelTextSize(12f)
        pieChart.centerText = "Budgets by Category"

        val legend = pieChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)
        legend.xEntrySpace = 7f
        legend.yEntrySpace = 0f
        legend.yOffset = 0f
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
            pieChart.centerText = "No budget data"
            return
        }

        val entries = ArrayList<PieEntry>()
        categoryMap.forEach { (category, amount) ->
            entries.add(PieEntry(amount, category))
        }

        val dataSet = PieDataSet(entries, "Budgets by Category").apply {
            sliceSpace = 3f
            selectionShift = 5f
            colors = ColorTemplate.MATERIAL_COLORS.toList()
        }

        val data = PieData(dataSet).apply {
            setValueFormatter(PercentFormatter(pieChart))
            setValueTextSize(11f)
            setValueTextColor(Color.BLACK)
        }

        pieChart.data = data
        pieChart.invalidate()
    }
}
