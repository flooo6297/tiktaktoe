package com.example.tiktaktoe.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.tiktaktoe.databinding.FragmentAboutScreenBinding
import com.example.tiktaktoe.R

class AboutViewFragment : Fragment() {

    private var binding: FragmentAboutScreenBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutScreenBinding.inflate(inflater, container, false)
        val rootView = binding?.root
        binding?.apply {
            textVersionDisplay.text = "0.0.1"
            val developersList = resources.getStringArray(R.array.developers_array)
            textDevelopersDisplay.text = developersList.joinToString("\n")
            backButton.setOnClickListener {
                findNavController().navigate(R.id.action_aboutScreen_to_nameSelectionScreen)
            }
        }
        return rootView ?: throw IllegalStateException("Binding is null")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
