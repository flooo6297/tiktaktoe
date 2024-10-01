package com.example.tiktaktoe.persistent_data.game_data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tiktaktoe.gamelogic.ResultOfGame

@Entity(tableName = "game_table")
class GameEntity (
    @ColumnInfo(name = "player_1_name")    val player1Name: String,
    @ColumnInfo(name = "player_2_name")    val player2Name: String,
    @ColumnInfo(name = "result")    val result: ResultOfGame,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)