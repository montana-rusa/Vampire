package com.example.vampire

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import androidx.core.content.edit
import java.time.temporal.TemporalAdjusters

class MainActivity : AppCompatActivity() {

    //variable declarations
    private lateinit var bottomNav : BottomNavigationView
    lateinit var db : AppDatabase
    lateinit var budgetDao: BudgetDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets }

        //automatically loads in the addFragment
        loadFragment(AddFragment())

        //handles switching between Fragments
        bottomNav = findViewById(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.add -> {
                    loadFragment(AddFragment())
                    true }
                R.id.summary -> {
                    loadFragment(SummaryFragment())
                    true }
                R.id.settings -> {
                    loadFragment(SettingFragment())
                    true }
                else -> false } }

        //fills Budget database if empty
        db = DatabaseProvider.getDatabase(this)
        budgetDao = db.budgetDao()
        fillIfEmpty()
        weeklyDeleteData(this, db)
        popupShow()
    }

    //this function loads the Fragment given as its parameter onto the screen
    private fun loadFragment(fragment : Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit() }

    private fun fillIfEmpty() {
        lifecycleScope.launch(Dispatchers.IO) {
            budgetDao.clearTable()
            if (budgetDao.getRowCount() == 0) {
                budgetDao.insertAll(
                    Budget(id=1,type = "Groceries", amount = 30F),
                    Budget(id=2,type = "Alcohol", amount = 15F),
                    Budget(id=3,type = "Eating Out", amount = 20F),
                    Budget(id=4,type = "Activities", amount = 20F),
                    Budget(id=5,type = "Transport", amount = 15F),
                    Budget(id=6,type = "Utilities", amount = 10F),
                    Budget(id=7,type = "Luxuries", amount = 10F)
                ) } }
    }

    private fun weeklyDeleteData(context : Context, db : AppDatabase) {

        //declare variables
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val today = LocalDate.now()
        val lastClearedDateString = prefs.getString("last_cleared_date", null)
        val thisMonday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

        val shouldClear = if (lastClearedDateString == null) { true }
        else {
            val lastClearedDate = LocalDate.parse(lastClearedDateString)
            lastClearedDate.isBefore(thisMonday) }

        if (shouldClear) {
            lifecycleScope.launch(Dispatchers.IO) {
                db.buyDao().clearTable() }

            prefs.edit { putString("last_cleared_date", today.toString()) }
        }
    }

    private fun popupShow() {
        val prefs = this.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val isFirstRun = prefs.getBoolean("isFirstRun", true)

        if (isFirstRun) {
            AlertDialog.Builder(this)
                .setTitle("Welcome to Vampire!")
                .setMessage("You can use this app to track your spending on a weekly basis, set weekly budgets for each type, and view graphs relating to the data you've provided. I hope you find it useful!")
                .setPositiveButton("Got it") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
            // Save flag so it won’t show next time
            prefs.edit { putBoolean("isFirstRun", false) }
        }

    }

}

