package com.example.tiktaktoe.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tiktaktoe.R
import com.example.tiktaktoe.databinding.FragmentNameSelectionScreenBinding
import com.example.tiktaktoe.views.GameViewModel

class NameSelectionFragment : Fragment() {

    private val viewModel: GameViewModel by activityViewModels()
    private lateinit var binding: FragmentNameSelectionScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observePlayerNames()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_name_selection_screen, container, false)
        binding.apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        setupButtonListeners()

        return binding.root
    }

    // Observe current player names from the ViewModel
    private fun observePlayerNames() {
        viewModel.currentPlayerNames.observe(this) { (name1, name2) ->
            binding.p1NameSelection.setText(name1)
            binding.p2NameSelection.setText(name2)
        }
    }

    // Set up button listeners for start and about buttons
    private fun setupButtonListeners() {
        binding.apply {
            startButton.setOnClickListener {
                startGameWithSelectedNames()
            }

            aboutButton.setOnClickListener {
                findNavController().navigate(R.id.action_nameSelectionScreen_to_aboutScreen)
            }
        }
    }

    // Start the game with the selected player names
    private fun startGameWithSelectedNames() {
        val name1 = binding.p1NameSelection.text.toString()
        val name2 = binding.p2NameSelection.text.toString()

        viewModel.assignPlayerNames(name1, name2)
        viewModel.startNewGame()

        findNavController().navigate(R.id.action_nameSelectionScreen_to_mainScreen)
    }
}
