package com.example.tiktaktoe.gamelogic

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlin.random.Random

class Game {

    private val board = Board()
    private var nextCellState: CellState = generateRandomStartingPlayer()
    private val compositeDisposable = CompositeDisposable()
    private val playerCellChanges = board.cellChangePublisher.filter { it.state != CellState.EMPTY }

    fun handleSelection(row: Int, col: Int) {
        board.setCellToState(row, col, nextCellState)
    }

    fun getCellChanges(): Observable<CellChange> {
        return board.cellChangePublisher
    }

    fun resetGame() {
        board.clearBoard()
        nextCellState = generateRandomStartingPlayer()
    }

    fun getGameOutcome(): Single<ResultOfGame> {
        val lineObservables = getPossibleSuccessCombinationObservables()

        val p1Observable = getPlayerObservables(CellState.PLAYER_1, ResultOfGame.PLAYER_1, lineObservables)
        val p2Observable = getPlayerObservables(CellState.PLAYER_2, ResultOfGame.PLAYER_2, lineObservables)

        return Observable.merge(p1Observable + p2Observable)
            .first(ResultOfGame.DRAW)
    }

    init {
        playerCellChanges.subscribe {
            nextCellState = if (nextCellState == CellState.PLAYER_1) CellState.PLAYER_2 else CellState.PLAYER_1
        }.let { compositeDisposable.add(it) }
    }

    private fun getPossibleSuccessCombinationObservables(): List<Observable<CellChange>> {
        val lineChecks = listOf(
            listOf(0,1,2).map { row -> { cellChange: CellChange -> cellChange.row == row } },
            listOf(0,1,2).map { col -> { cellChange: CellChange -> cellChange.col == col } },
            listOf(
                { cellChange: CellChange -> cellChange.row == cellChange.col },
                { cellChange: CellChange -> cellChange.row == 2 - cellChange.col }
            )
        ).flatten()

        return lineChecks.map { check -> playerCellChanges.filter(check).take(3) }
    }

    private fun getPlayerObservables(
        playerState: CellState,
        winOutcome: ResultOfGame,
        lineObservables: List<Observable<CellChange>>
    ): List<Observable<ResultOfGame>> {
        return lineObservables.map { lineObservable ->
            lineObservable
                .all { it.state == playerState }
                .flatMapObservable { playerWon ->
                    if (playerWon) Observable.just(winOutcome) else Observable.empty()
                }
        }
    }

    private fun generateRandomStartingPlayer(): CellState {
        return when (Random.nextBoolean()) {
            true -> CellState.PLAYER_1
            else -> CellState.PLAYER_2
        }
    }
}
