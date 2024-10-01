package com.example.tiktaktoe.gamelogic

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.core.Single


class GameManager {
    private val gameOutcomePublisher = PublishSubject.create<Single<ResultOfGame>>()
    private var game = Game()

    fun handleInput(row: Int, col: Int) {
        game.handleSelection(row, col)
    }

    fun getResultOfGame(): Observable<ResultOfGame> {
        return gameOutcomePublisher.switchMap { it.toObservable() }
    }

    fun startNewGame() {
        game.resetGame()
        gameOutcomePublisher.onNext( game.getGameOutcome() )
    }

    fun getCellChanges(): Observable<CellChange> {
        return game.getCellChanges()
    }
}