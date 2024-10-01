package com.example.tiktaktoe.gamelogic

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlin.properties.Delegates

class PlayerManager {
    val player1 = Player()
    val player2 = Player()
    private val playersObservable = Observable.combineLatest(
        player1.publisher,
        player2.publisher
    ) { p1, p2 -> Pair(p1, p2) }

    fun updateScoreOnGameOutcome(gameOutcome: ResultOfGame) {
        if (gameOutcome == ResultOfGame.PLAYER_1) {
            player1.score++
        } else if (gameOutcome == ResultOfGame.PLAYER_2) {
            player2.score++
        }
    }

    fun getIds(): Observable<Pair<Int, Int>> {
        return playersObservable.map { pair -> Pair(pair.first.drawableId, pair.second.drawableId) }.distinctUntilChanged()
    }

    fun getPlayerNames(): Observable<Pair<String, String>> {
        return playersObservable.map { pair -> Pair(pair.first.name, pair.second.name) }.distinctUntilChanged()
    }

    fun getPlayerScores(): Observable<Pair<Int, Int>> {
        return playersObservable.map { pair -> Pair(pair.first.score, pair.second.score) }.distinctUntilChanged()
    }
}

class Player {
    var name: String by Delegates.observable("") { _, _, _ -> publisher.onNext(this) }
    var drawableId: Int by Delegates.observable(0) { _, _, _ -> publisher.onNext(this) }
    var score: Int by Delegates.observable(0) { _, _, _ -> publisher.onNext(this) }
    val publisher = BehaviorSubject.create<Player>()

    init {
        publisher.onNext(this)
    }
}