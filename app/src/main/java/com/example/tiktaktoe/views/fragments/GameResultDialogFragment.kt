package com.example.tiktaktoe.views.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.tiktaktoe.R
import com.example.tiktaktoe.databinding.FragmentGameOutcomeDialogBinding
import com.example.tiktaktoe.gamelogic.ResultOfGame
import com.example.tiktaktoe.views.GameViewModel

class GameResultDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentGameOutcomeDialogBinding
    private val viewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game_outcome_dialog, container, false)
        setupGameOutcomeMessage()
        return binding.root
    }

    private fun setupGameOutcomeMessage() {
        val playerNames = viewModel.currentPlayerNames.value
        binding.gameOutcomeDisplay.text = when (viewModel.outcome.value) {
            ResultOfGame.PLAYER_1 -> "You did it ${playerNames?.first}!"
            ResultOfGame.PLAYER_2 -> "You did it ${playerNames?.second}!"
            else -> "It's a draw!"
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.startNewGame()
    }
}
