package com.example.tiktaktoe.persistent_data.game_data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDataAccessObject {
    @Query("SELECT * FROM game_table")
    fun getAllPrevGames(): Flow<List<GameEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(gameEntity: GameEntity)
}