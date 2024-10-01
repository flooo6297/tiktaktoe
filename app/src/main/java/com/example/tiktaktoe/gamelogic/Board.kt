package com.example.tiktaktoe.gamelogic

import io.reactivex.rxjava3.subjects.PublishSubject

enum class CellState {
    EMPTY, PLAYER_1, PLAYER_2
}

data class CellChange(
    val row: Int,
    val col: Int,
    val state: CellState
)

class Board {

    private val cells: List<MutableList<CellState>> = List(3) { MutableList(3) { CellState.EMPTY } }
    val cellChangePublisher: PublishSubject<CellChange> = PublishSubject.create()

    fun clearBoard() {
        cells.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, _ ->
                setCellToState(rowIndex, colIndex, CellState.EMPTY)
            }
        }
    }

    fun setCellToState(row: Int, col: Int, state: CellState) {
        if (isCellEmpty(row, col) || state == CellState.EMPTY) {
            cells[row][col] = state
            notifyCellChange(row, col, state)
        }
    }

    private fun isCellEmpty(row: Int, col: Int): Boolean {
        return cells[row][col] == CellState.EMPTY
    }

    private fun notifyCellChange(row: Int, col: Int, state: CellState) {
        cellChangePublisher.onNext(CellChange(row, col, state))
    }
}
