package com.example.vampire

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        //declare variables
        val db = DatabaseProvider.getDatabase(requireContext())
        val buyDao = db.buyDao()
        val deleteButton = view.findViewById<Button>(R.id.deleteButton)
        val categorySpinner2 = view.findViewById<Spinner>(R.id.categorySpinner2)

        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter.createFromResource(requireContext(), R.array.category_array, R.layout.spinner_item)
            .also { adapter ->
                // Specify the layout to use when the list of choices appears.
                adapter.setDropDownViewResource(R.layout.spinner_item)
                // Apply the adapter to the spinner.
                categorySpinner2.adapter = adapter }


        //delete button listener
        deleteButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                buyDao.clearTable() } }

        return view
    }
}