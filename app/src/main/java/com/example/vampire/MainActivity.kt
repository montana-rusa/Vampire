package com.example.vampire

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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


    }

    //this function loads the Fragment given as its parameter onto the screen
    private fun loadFragment(fragment : Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit() }

    private fun fillIfEmpty() {
        lifecycleScope.launch(Dispatchers.IO) {
            if (budgetDao.getRowCount() == 0) {
                budgetDao.insertAll(
                    Budget(type="Groceries", amount = 30F),
                    Budget(type="Alcohol", amount = 15F),
                    Budget(type="Eating Out", amount = 20F),
                    Budget(type="Social Activities", amount = 20F),
                    Budget(type="Transport", amount = 15F),
                    Budget(type="Utilities", amount = 10F),
                    Budget(type="Aesthetics", amount = 10F), ) } }
    }
}
