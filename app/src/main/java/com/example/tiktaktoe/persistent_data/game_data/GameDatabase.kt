package com.example.tiktaktoe.persistent_data.game_data

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [GameEntity::class], version = 1, exportSchema = false)
abstract class GameDatabase: RoomDatabase() {
    abstract fun gameDataAccessObject(): GameDataAccessObject
}