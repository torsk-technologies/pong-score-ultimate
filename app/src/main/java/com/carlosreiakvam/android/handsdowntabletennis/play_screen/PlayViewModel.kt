package com.carlosreiakvam.android.handsdowntabletennis.play_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosreiakvam.android.handsdowntabletennis.local_db.GameStateDAO
import com.carlosreiakvam.android.handsdowntabletennis.local_db.GameStateEntity
import com.carlosreiakvam.android.handsdowntabletennis.score_logic.Game
import com.carlosreiakvam.android.handsdowntabletennis.score_logic.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class PlayViewModel(
    private val gameStateDAO: GameStateDAO,
    private val gameRulesFromArgs: GameRules,
    private val isNewGame: Boolean, //
) : ViewModel() {
    private var player1: Player = Player("player one")
    private var player2: Player = Player("player two")
    private var game: Game

    private val _winStatesLive: MutableLiveData<WinStates> = MutableLiveData()
    val winStates: LiveData<WinStates>
        get() = _winStatesLive

    private val _currentServerLive: MutableLiveData<Int> = MutableLiveData()
    val currentServerLive: LiveData<Int>
        get() = _currentServerLive

    private val _player1Live: MutableLiveData<Player> = MutableLiveData()
    val player1Live: LiveData<Player>
        get() = _player1Live

    private val _player2Live: MutableLiveData<Player> = MutableLiveData()
    val player2Live: LiveData<Player>
        get() = _player2Live

    private val _gameRulesLive: MutableLiveData<GameRules> = MutableLiveData()
    val gameRulesLive: LiveData<GameRules>
        get() = _gameRulesLive

    private fun getLast(): Flow<GameStateEntity> = gameStateDAO.getLast()
    private suspend fun deleteLast() = gameStateDAO.deleteLast()

    init {
        if (isNewGame) {
            Timber.d("is new game")
            game = Game(player1, player2, GameRules(
                bestOf = gameRulesFromArgs.bestOf,
                firstServer = gameRulesFromArgs.firstServer))
            viewModelScope.launch {
                try {
                    gameStateDAO.deleteAll()
                    gameStateDAO.insertGameState(GameStateEntity( // Insert new 0 init gamestate
                        gameToBestOf = gameRulesFromArgs.bestOf,
                        firstServer = gameRulesFromArgs.firstServer))
                } catch (e: Exception) {
                }
            }
        } else {
            Timber.d("is not new game")
            game = Game(player1, player2, GameRules(9, 1))
            setGameStateFromDB()
        }
        updateLiveDataFromGame()
    }

    fun onUndo() {
        viewModelScope.launch {
            deleteLast()
            setGameStateFromDB()
        }
        updateLiveDataFromGame()
    }

    private fun updateLiveDataFromGame() {
        _currentServerLive.value = game.currentServer
        _player1Live.value = game.player1
        _player2Live.value = game.player2
        _winStatesLive.value = game.winStates
        _gameRulesLive.value = game.gameRules
    }

    fun registerPoint(playerNumber: Int) {
        game.registerPoint(playerNumber) // update Game with new values
        updateLiveDataFromGame()
        insertGameStateToDB() // insert values from Game to DB
    }


    private fun insertGameStateToDB() {
        viewModelScope.launch {
            gameStateDAO.insertGameState(GameStateEntity(
                p1GameScore = _player1Live.value?.gameScore ?: 0,
                p1MatchScore = _player1Live.value?.matchScore ?: 0,
                p2GameScore = _player2Live.value?.gameScore ?: 0,
                p2MatchScore = _player2Live.value?.matchScore ?: 0,
                currentServer = _currentServerLive.value ?: 1,
                firstServer = _gameRulesLive.value?.firstServer ?: InitialValues.FIRSTSERVER.i,
                isGameWon = _winStatesLive.value?.isGameWon ?: false,
                isMatchWon = _winStatesLive.value?.isMatchWon ?: false,
                isMatchReset = _winStatesLive.value?.isMatchReset ?: false,
                gameToBestOf = _gameRulesLive.value?.bestOf ?: InitialValues.BESTOF.i,
                gameWinner = _winStatesLive.value?.gameWinner ?: 1,
                nGamesPlayed = game.nGamesPlayed
            ))
        }
    }

    private fun setGameStateFromDB() {
        viewModelScope.launch {
            try {
                getLast().collect { gameStateEntity ->
                    // All values have init values
                    game.player1.gameScore = gameStateEntity.p1GameScore
                    game.player1.matchScore = gameStateEntity.p1MatchScore
                    game.player2.gameScore = gameStateEntity.p2GameScore
                    game.player2.matchScore = gameStateEntity.p2MatchScore
                    game.gameRules.bestOf = gameStateEntity.gameToBestOf
                    game.gameRules.firstServer = gameStateEntity.firstServer
                    game.currentServer = gameStateEntity.currentServer
                    game.nGamesPlayed = gameStateEntity.nGamesPlayed
                    game.winStates.isGameWon = gameStateEntity.isGameWon
                    game.winStates.isMatchWon = gameStateEntity.isMatchWon
                    game.winStates.isMatchReset = gameStateEntity.isMatchReset
                    game.winStates.gameWinner = gameStateEntity.gameWinner
                    _player1Live.value = game.player1
                    _player2Live.value = game.player2
                    _currentServerLive.value = game.currentServer
                }
            } catch (nullPointer: NullPointerException) {
            }
        }
    }


    fun newGameOnMatchWon() {
        viewModelScope.launch {
            gameStateDAO.deleteAll()
        }.invokeOnCompletion {
            player1 = Player("player one")
            player2 = Player("player two")
            game = Game(player1, player2, gameRulesFromArgs)
            viewModelScope.launch {
                gameStateDAO.insertGameState(GameStateEntity(gameToBestOf = gameRulesFromArgs.bestOf,
                    firstServer = gameRulesFromArgs.firstServer))
            }
            updateLiveDataFromGame()
        }
    }

    fun onFirstRun() {
        viewModelScope.launch { gameStateDAO.insertGameState(GameStateEntity()) }
    }

    override fun onCleared() {
        super.onCleared()
    }


}

