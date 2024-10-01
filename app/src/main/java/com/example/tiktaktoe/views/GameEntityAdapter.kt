package com.example.tiktaktoe.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tiktaktoe.R
import com.example.tiktaktoe.persistent_data.game_data.GameEntity
import com.example.tiktaktoe.gamelogic.ResultOfGame

class GameEntityAdapter : ListAdapter<GameEntity, GameEntityAdapter.GameViewHolder>(GameEntityDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_item, parent, false)
        return GameViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val player1Name: TextView = itemView.findViewById(R.id.player_1_name)
        private val player2Name: TextView = itemView.findViewById(R.id.player_2_name)

        fun bind(game: GameEntity?) {
            game?.let {
                player1Name.text = it.player1Name
                player2Name.text = it.player2Name
                updateTextViews(it)
            }
        }

        private fun updateTextViews(game: GameEntity) {
            when (game.result) {
                ResultOfGame.PLAYER_1 -> setWinnerLoser(player1Name, player2Name)
                ResultOfGame.PLAYER_2 -> setWinnerLoser(player2Name, player1Name)
                else -> setDraw(player1Name, player2Name)
            }
        }

        private fun setWinnerLoser(winnerView: TextView, loserView: TextView) {
            winnerView.apply {
                setTextAppearance(R.style.victor)
                setBackgroundResource(android.R.color.holo_orange_light)
            }
            loserView.setTextAppearance(R.style.loser)
        }

        private fun setDraw(vararg views: TextView) {
            views.forEach {
                it.setTextAppearance(R.style.participant)
            }
        }
    }

    class GameEntityDiffCallback : DiffUtil.ItemCallback<GameEntity>() {
        override fun areItemsTheSame(oldItem: GameEntity, newItem: GameEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GameEntity, newItem: GameEntity): Boolean {
            return oldItem == newItem
        }
    }
}
