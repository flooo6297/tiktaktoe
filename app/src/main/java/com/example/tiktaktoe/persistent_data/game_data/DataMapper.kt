package com.example.tiktaktoe.persistent_data.game_data

import com.example.tiktaktoe.gamelogic.ResultOfGame
import com.example.tiktaktoe.gamelogic.Player

object DataMapper {

    fun fromDomain(player1: Player, player2: Player, resultOfGame: ResultOfGame): GameEntity {
        return GameEntity(
            player1Name  = player1.name,
            player2Name  = player2.name,
            result = resultOfGame
        )
    }
}