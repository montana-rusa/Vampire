package com.example.vampire

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.charts.*
import android.graphics.Color
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters


class SummaryFragment : Fragment() {

    //late init declarations
    lateinit var pieChart: PieChart
    lateinit var pieData: PieData
    lateinit var pieDataSet: PieDataSet
    lateinit var barChart: BarChart
    lateinit var barData: BarData
    lateinit var db: AppDatabase
    lateinit var buyDao: BuyDao
    lateinit var budgetDao: BudgetDao
    lateinit var group1 : ArrayList<BarEntry>
    lateinit var group2 : ArrayList<BarEntry>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_summary, container, false)

        //builds bar chart
        barChart = view.findViewById(R.id.barChart)
        db = DatabaseProvider.getDatabase(requireContext())
        budgetDao = db.budgetDao()
        buyDao = db.buyDao()

        group2 = ArrayList()
        group1 = ArrayList()

        val weekTextView: TextView = view.findViewById(R.id.weekTextView)
        weekTextView.text = getCurrentWeekDates()

        loadChartData()
        return view
    }

    private fun loadChartData() {

        //enter coroutine
        lifecycleScope.launch(Dispatchers.IO) {

            //initiate variables
            val buys = buyDao.getAll()
            val budgets = budgetDao.getAll()
            val sums = mutableListOf(0f,0f,0f,0f,0f,0f,0f)

            // generate 'sums'
            buys.forEach { buy ->
                when (buy.type.toString()) {
                    "Groceries" -> {
                        sums[0] += buy.amount }
                    "Alcohol" -> {
                        sums[1] += buy.amount }
                    "Eating Out" -> {
                        sums[2] += buy.amount }
                    "Activities" -> {
                        sums[3] += buy.amount }
                    "Transport" -> {
                        sums[4] += buy.amount }
                    "Utilities" -> {
                        sums[5] += buy.amount }
                    "Luxuries" -> {
                        sums[6] += buy.amount }
                } }

            //generating groups 1 and 2
            group1 = ArrayList()
            group2 = ArrayList()
            budgets.forEach { budget ->
                group2.add(BarEntry((budget.id)-1.toFloat(),budget.amount)) }
            for (i in 0..6) {
            group1.add(BarEntry(i.toFloat(), sums[i].toFloat())) }

            //leave coroutine
            withContext(Dispatchers.Main) {

                //create sets 1 and 2
                val set1 = BarDataSet(group1, "Buys")
                set1.color = Color.RED
                val set2 = BarDataSet(group2, "Budget")
                set2.color = Color.BLUE

                val barWidth = 0.3f   // each bar is 30% wide
                val barSpace = 0f     // no gap between bars in the same group
                val groupSpace = 0.4f // 40% gap between groups
                //0.3 + 0.3 + 0.4 = 1 so this works

                //create and generate barChart
                barData = BarData(set1, set2)
                barData.barWidth = barWidth
                barChart.data = barData
                barChart.groupBars(0f, groupSpace, barSpace)
                barChart.invalidate()
            }
        }
    }

    private fun getCurrentWeekDates(): String {
        // Today
        val today = LocalDate.now()

        // Find Monday of the current week
        val monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        // Find Sunday of the current week
        val sunday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

        // Formatter for display
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

        // Return formatted string
        return "Week: ${monday.format(formatter)} - ${sunday.format(formatter)}"
    }


}
