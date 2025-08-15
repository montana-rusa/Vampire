package com.example.vampire

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner

class AddFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)


        // Create an ArrayAdapter using the string array and a default spinner layout.
        val categorySpinner = view.findViewById<Spinner>(R.id.categorySpinner)
        ArrayAdapter.createFromResource(requireContext(), R.array.category_array, R.layout.spinner_item)
            .also { adapter ->
                // Specify the layout to use when the list of choices appears.
                adapter.setDropDownViewResource(R.layout.spinner_item)
                // Apply the adapter to the spinner.
                categorySpinner.adapter = adapter }





        return view
    }







}