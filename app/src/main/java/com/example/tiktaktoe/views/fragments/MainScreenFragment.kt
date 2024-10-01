package com.example.tiktaktoe.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tiktaktoe.R
import com.example.tiktaktoe.TikTakToeApp
import com.example.tiktaktoe.databinding.FragmentMainScreenBinding
import com.example.tiktaktoe.views.GameViewModel
import kotlinx.coroutines.launch

class MainScreenFragment : Fragment() {

    private val viewModel: GameViewModel by activityViewModels()
    private lateinit var binding: FragmentMainScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_screen, container, false)
        binding.apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        setupButtons()
        setupNavigationButtons()

        viewModel.startNewGame()

        return binding.root
    }

    private fun observeViewModel() {
        viewModel.apply {
            cellUpdate.observe(this@MainScreenFragment) { cell ->
                getListOfButtons(binding)[cell.cellId].setImageResource(cell.resourceId)
            }

            currentPlayerNames.observe(this@MainScreenFragment) { (name1, name2) ->
                binding.playerName1.text = name1
                binding.playerName2.text = name2
            }

            currentScores.observe(this@MainScreenFragment) { (score1, score2) ->
                binding.playerScore1.text = score1.toString()
                binding.playerScore2.text = score2.toString()
            }

            outcome.observe(this@MainScreenFragment) {
                findNavController().navigate(R.id.action_mainScreen_to_gameOutcomeDialog)
            }
        }
    }

    private fun setupButtons() {
        getListOfButtons(binding).forEachIndexed { index, button ->
            button.setOnClickListener {
                TikTakToeApp.coroutineScope.launch {
                    viewModel.handleCellInput(index)
                }
            }
        }
    }

    private fun setupNavigationButtons() {
        binding.apply {
            restartButton.setOnClickListener {
                viewModel.startNewGame()
            }
            settingsButton.setOnClickListener {
                findNavController().navigate(R.id.action_mainScreen_to_nameSelectionScreen)
            }
            historyButton.setOnClickListener {
                findNavController().navigate(R.id.action_mainScreen_to_historyScreen)
            }
        }
    }

    private fun getListOfButtons(binding: FragmentMainScreenBinding): List<ImageButton> {
        return listOf(
            binding.imageButton1,
            binding.imageButton2,
            binding.imageButton3,
            binding.imageButton4,
            binding.imageButton5,
            binding.imageButton6,
            binding.imageButton7,
            binding.imageButton8,
            binding.imageButton9
        )
    }
}
