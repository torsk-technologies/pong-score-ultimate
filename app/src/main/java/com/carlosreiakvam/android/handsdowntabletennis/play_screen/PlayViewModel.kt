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
    private val gameRulesFromArgs: GameRules, // either -1 or val from bestOfScreen
) : ViewModel() {
    private var player1: Player = Player("player one", 1)
    private var player2: Player = Player("player two", 2)
    private var game: Game
//    private var game = Game(player1, player2, GameRules(
//        bestOf = gameRulesFromArgs.bestOf, // either -1 or val from bestOfScreen
//        firstServer = gameRulesFromArgs.firstServer))


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


    init {
        Timber.d("init viewmodel")
        // Check if init comes from opening screen or best of screen
        if (gameRulesFromArgs.bestOf == -1) { // either -1 or val from bestOfScreen
            Timber.d("game not setup from bestOf screen")
            game = Game(player1, player2, GameRules(9, 1))
            setGameStateFromDB()
            updateLiveDataFromGame()
        } else {
            // game setup from best of screen
            game = Game(player1, player2, GameRules(
                bestOf = gameRulesFromArgs.bestOf,
                firstServer = gameRulesFromArgs.firstServer))
            viewModelScope.launch {
                gameStateDAO.deleteAll()
                gameStateDAO.insertGameState(GameStateEntity( // Insert new gamestate
                    gameToBestOf = gameRulesFromArgs.bestOf,
                    firstServer = gameRulesFromArgs.firstServer))
            }
        }
        updateLiveDataFromGame()
    }

    private fun getLast(): Flow<GameStateEntity> = gameStateDAO.getLast()
    suspend fun deleteLast() = gameStateDAO.deleteLast()

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
        Timber.d("registerPoint")
        game.registerPoint(playerNumber) // update Game with new values
        updateLiveDataFromGame()
        insertGameStateToDB() // insert values from Game to DB
    }


    private fun insertGameStateToDB() {
        Timber.d("insertGameStateToDB")
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
                pointsPlayed = game.pointsPlayed
            ))
        }
    }

    private fun setGameStateFromDB() {
        viewModelScope.launch {
            try {
                Timber.d("DB does indeed exist")
                getLast().collect { gameStateEntity ->
                    // All values have init values
                    game.player1.gameScore = gameStateEntity.p1GameScore
                    game.player1.matchScore = gameStateEntity.p1MatchScore
                    game.player2.gameScore = gameStateEntity.p2GameScore
                    game.player2.matchScore = gameStateEntity.p2MatchScore
                    game.gameRules.bestOf = gameStateEntity.gameToBestOf
                    game.gameRules.firstServer = gameStateEntity.firstServer
                    game.currentServer = gameStateEntity.currentServer
                    game.pointsPlayed = gameStateEntity.pointsPlayed
                    game.winStates.isGameWon = gameStateEntity.isGameWon
                    game.winStates.isMatchWon = gameStateEntity.isMatchWon
                    game.winStates.isMatchReset = gameStateEntity.isMatchReset
                    game.winStates.gameWinner = gameStateEntity.gameWinner
                    _player1Live.value = game.player1
                    _player2Live.value = game.player2
                    _currentServerLive.value = game.currentServer
                }
            } catch (nullp: NullPointerException) {
                Timber.d("DB does not exist")
                // if no db exists
                insertGameStateToDB()
            }
        }
    }


    fun newGameOnMatchWon() {
        viewModelScope.launch {
            gameStateDAO.deleteAll()
        }.invokeOnCompletion {
            player1 = Player("player one", 1)
            player2 = Player("player two", 2)
            game = Game(player1, player2, gameRulesFromArgs)
            viewModelScope.launch {
                gameStateDAO.insertGameState(GameStateEntity(gameToBestOf = gameRulesFromArgs.bestOf,
                    firstServer = gameRulesFromArgs.firstServer))
            }
            updateLiveDataFromGame()
        }
    }

    override fun onCleared() {
        super.onCleared()
        Timber.tag("lifecycle").d("ViewModel cleared")
    }


}

