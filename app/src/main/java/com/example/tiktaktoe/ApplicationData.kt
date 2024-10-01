package com.example.tiktaktoe

import android.content.Context
import androidx.room.Room
import com.example.tiktaktoe.persistent_data.game_data.GameDataAccessObject
import com.example.tiktaktoe.persistent_data.game_data.GameDatabase
import com.example.tiktaktoe.persistent_data.repository.GameRepository
import com.example.tiktaktoe.gamelogic.GameManager
import com.example.tiktaktoe.gamelogic.PlayerManager

class ApplicationData(context: Context) {
    private val dbName = "game_saveData"

    val gameManager: GameManager = GameManager()

    private val gameDatabase: GameDatabase = Room.databaseBuilder(
        context,
        GameDatabase::class.java,
        dbName
    ).build()

    private val gameDao: GameDataAccessObject = gameDatabase.gameDataAccessObject()

    val gameRepository: GameRepository = GameRepository(gameDao)

    val playerManager: PlayerManager = PlayerManager()
}