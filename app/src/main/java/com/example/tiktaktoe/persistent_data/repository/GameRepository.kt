package com.example.tiktaktoe.persistent_data.repository

import androidx.annotation.WorkerThread
import com.example.tiktaktoe.persistent_data.game_data.GameDataAccessObject
import com.example.tiktaktoe.persistent_data.game_data.GameEntity
import kotlinx.coroutines.flow.Flow

class GameRepository(private val gameDataAccessObject: GameDataAccessObject) {

    val allGames: Flow<List<GameEntity>> = gameDataAccessObject.getAllPrevGames()

    @WorkerThread
    suspend fun insert(gameEntity: GameEntity) {
        gameDataAccessObject.insert(gameEntity)
    }
}