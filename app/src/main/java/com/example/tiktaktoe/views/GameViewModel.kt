package com.example.tiktaktoe.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiktaktoe.R
import com.example.tiktaktoe.TikTakToeApp
import com.example.tiktaktoe.persistent_data.game_data.GameEntity
import com.example.tiktaktoe.persistent_data.game_data.DataMapper
import com.example.tiktaktoe.persistent_data.repository.GameRepository
import com.example.tiktaktoe.gamelogic.CellState
import com.example.tiktaktoe.gamelogic.GameManager
import com.example.tiktaktoe.gamelogic.ResultOfGame
import com.example.tiktaktoe.gamelogic.PlayerManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    private val _cellUpdate = MutableLiveData<CellData>()
    val cellUpdate: LiveData<CellData> get() = _cellUpdate

    private val _currentPlayerNames = MutableLiveData<Pair<String, String>>()
    val currentPlayerNames: LiveData<Pair<String, String>> get() = _currentPlayerNames

    private val _currentScores = MutableLiveData<Pair<Int, Int>>()
    val currentScores: LiveData<Pair<Int, Int>> get() = _currentScores

    private val _outcome = MutableLiveData<ResultOfGame>()
    val outcome: LiveData<ResultOfGame> get() = _outcome

    private val _gameHistory = MutableLiveData<List<GameEntity>>()
    val gameHistory: LiveData<List<GameEntity>> get() = _gameHistory

    private val repository: GameRepository = TikTakToeApp.applicationData.gameRepository

    private val gameHandler: GameManager = TikTakToeApp.applicationData.gameManager

    private val playerHandler: PlayerManager = TikTakToeApp.applicationData.playerManager

    private val disposables = CompositeDisposable()

    init {
        initializeGameHistory()
        fetchPlayerNames()
        fetchPlayerScores()
        observeCellChanges()
        observeGameOutcome()

        playerHandler.player1.name  = "X"
        playerHandler.player2.name = "O"
    }

    private fun initializeGameHistory() {
        viewModelScope.launch {
            repository.allGames.collect { games ->
                _gameHistory.value = games
            }
        }
    }

    private fun fetchPlayerNames() {
        playerHandler.getPlayerNames()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { names ->
                updatePlayerNames(names)
            }
            .also { disposables.add(it) }
    }

    private fun fetchPlayerScores() {
        playerHandler.getPlayerScores()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { scores ->
                _currentScores.value = scores
            }
            .also { disposables.add(it) }
    }

    private fun observeCellChanges() {
        Observable.combineLatest(
            playerHandler.getIds(),
            gameHandler.getCellChanges()
        ) { drawableIds, cellUpdate ->
            val cellId = cellUpdate.row * 3 + cellUpdate.col
            val drawableId = when (cellUpdate.state) {
                CellState.EMPTY -> R.drawable.ic_tiktaktoe_empty
                CellState.PLAYER_1 -> drawableIds.first
                CellState.PLAYER_2 -> drawableIds.second
            }
            CellData(cellId, drawableId)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { cellData ->
                _cellUpdate.value = cellData
            }
            .also { disposables.add(it) }
    }

    private fun observeGameOutcome() {
        gameHandler.getResultOfGame()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { gameOutcome ->
                _outcome.value = gameOutcome
                updateScoresAndSaveGame(gameOutcome)
            }
            .also { disposables.add(it) }
    }

    private fun updateScoresAndSaveGame(outcome: ResultOfGame) {
        playerHandler.updateScoreOnGameOutcome(outcome)
        viewModelScope.launch {
            repository.insert(
                DataMapper.fromDomain(
                    playerHandler.player1,
                    playerHandler.player2,
                    outcome
                )
            )
        }
    }

    private fun updatePlayerNames(names: Pair<String, String>) {
        _currentPlayerNames.value = names
        calculateScores(names)
    }

    private fun calculateScores(names: Pair<String, String>) {
        val winnersNames = _gameHistory.value?.filter { game ->
            (game.player1Name == names.first && game.player2Name == names.second) ||
                    (game.player1Name == names.second && game.player2Name == names.first)
        }?.mapNotNull { game ->
            when (game.result) {
                ResultOfGame.PLAYER_1 -> game.player1Name
                ResultOfGame.PLAYER_2 -> game.player2Name
                else -> null
            }
        }

        playerHandler.player1.score = winnersNames?.count { it == names.first } ?: 0
        playerHandler.player2.score = winnersNames?.count { it == names.second } ?: 0
    }

    fun handleCellInput(id: Int) {
        gameHandler.handleInput(id / 3, id % 3)
    }

    fun startNewGame() {
        gameHandler.startNewGame()
    }

    fun assignPlayerNames(name1: String?, name2: String?) {
        name1?.let { playerHandler.player1.name = it }
        name2?.let { playerHandler.player2.name = it }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
