package com.example.vampire

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.charts.*
import android.graphics.Color
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SummaryFragment : Fragment() {

    //late init declarations
    lateinit var pieChart: PieChart
    lateinit var barChart: BarChart
    lateinit var pieData: PieData
    lateinit var pieDataSet: PieDataSet
    lateinit var barData: BarData
    lateinit var barDataSet: BarDataSet
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

        val group2 = ArrayList<BarEntry>()
        val group1 = ArrayList<BarEntry>()
        categorizeSpending()
        getBudgets()

        val set1 = BarDataSet(group1, "Buys")
        set1.color = Color.RED
        val set2 = BarDataSet(group2, "Budget")
        set2.color = Color.BLUE

        val barWidth = 0.3f   // each bar is 30% wide
        val barSpace = 0f     // no gap between bars in the same group
        val groupSpace = 0.4f // 40% gap between groups
        //0.3 + 0.3 + 0.4 = 1
        barData = BarData(set1, set2)
        barData.barWidth = barWidth
        barChart.groupBars(0f, groupSpace, barSpace)

        barChart.invalidate()





        return view
    }

    fun categorizeSpending() {
        val sums = mutableListOf(0f,0f,0f,0f,0f,0f,0f)
        lifecycleScope.launch(Dispatchers.IO) {
            val buys : List<Buy> = buyDao.getAll()
            withContext(Dispatchers.Main) {
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
                group1 = ArrayList<BarEntry>()
                for (i in 0..6) {
                    group1.add(BarEntry(i.toFloat(), sums[i].toFloat()))
                } } } }

    fun getBudgets() {
        group2 = ArrayList<BarEntry>()
        lifecycleScope.launch(Dispatchers.IO) {
            val budgets : List<Budget> = budgetDao.getAll()
            withContext(Dispatchers.Main) {
                budgets.forEach { budget ->
                    group2.add(BarEntry((budget.id)-1.toFloat(),budget.amount))
                } } } }

}