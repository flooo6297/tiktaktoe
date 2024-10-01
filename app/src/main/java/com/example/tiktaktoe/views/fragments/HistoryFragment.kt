package com.example.tiktaktoe.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tiktaktoe.R
import com.example.tiktaktoe.databinding.FragmentHistoryBinding
import com.example.tiktaktoe.views.GameEntityAdapter
import com.example.tiktaktoe.views.GameViewModel

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private val viewModel: GameViewModel by activityViewModels()
    private val gameAdapter by lazy { GameEntityAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeGameHistory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        setupRecyclerView()
        setupReturnButton()

        return binding.root
    }

    // Observe the game history LiveData
    private fun observeGameHistory() {
        viewModel.gameHistory.observe(this) { games ->
            gameAdapter.submitList(games)
        }
    }

    // Set up the RecyclerView
    private fun setupRecyclerView() {
        binding.recyclerview.apply {
            adapter = gameAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    // Set up the return button click listener
    private fun setupReturnButton() {
        binding.returnButton.setOnClickListener {
            findNavController().navigate(R.id.action_historyScreen_to_mainScreen)
        }
    }
}
