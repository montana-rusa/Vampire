package com.example.vampire

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.DayOfWeek

class AddFragment : Fragment() {

    //lateinits
    private lateinit var buyDao: BuyDao
    private lateinit var historyView : TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        //fetches ids
        val editText = view.findViewById<EditText>(R.id.editText)
        val submitButton = view.findViewById<Button>(R.id.submitButton)
        val categorySpinner = view.findViewById<Spinner>(R.id.categorySpinner)
        val db = DatabaseProvider.getDatabase(requireContext())
        buyDao = db.buyDao()
        historyView = view.findViewById<TextView>(R.id.historyView)

        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter.createFromResource(requireContext(), R.array.category_array, R.layout.spinner_item)
            .also { adapter ->
                // Specify the layout to use when the list of choices appears.
                adapter.setDropDownViewResource(R.layout.spinner_item)
                // Apply the adapter to the spinner.
                categorySpinner.adapter = adapter }

        //activate 'Submit' button listener
        submitButton.setOnClickListener {
            val enteredData = editText.text.toString().toFloatOrNull()
            val selectedItem = categorySpinner.getSelectedItem().toString()
            val dayOfWeek = (LocalDate.now().dayOfWeek).toString()
            if ((enteredData != null) && (selectedItem != "Select category")) {
                lifecycleScope.launch(Dispatchers.IO) {
                    buyDao.insertAll(Buy(day = dayOfWeek, type = selectedItem, amount = enteredData))
                    withContext(Dispatchers.Main) {
                        updateHistory()
                    } }
            } else { Toast.makeText(requireContext(), "Invalid entry!", Toast.LENGTH_SHORT).show() }
        }

        updateHistory()

        return view
    }

    //compiles a list of all rows in updateHistory
    private fun updateHistory() {
        lifecycleScope.launch(Dispatchers.IO) {
            val buys : List<Buy> = buyDao.getAll()
            withContext(Dispatchers.Main) {
                val sb = StringBuilder()
                buys.forEach { buy ->
                    sb.append("${buy.day}: ${buy.type}: ${buy.amount}\n") }
                historyView.text = sb.toString() }}
    }







}