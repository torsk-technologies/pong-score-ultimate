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
import kotlinx.coroutines.launch
import timber.log.Timber

class PlayViewModel(private val gameStateDAO: GameStateDAO) : ViewModel() {
    private val player1 = Player("player one", 1)
    private val player2 = Player("player two", 2)
    private val game = Game(player1, player2, GameRules())

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
        Timber.d("viewModel initiated")
    }

    fun getLast(): Flow<GameStateEntity> = gameStateDAO.getLast()
    fun deleteLast() = gameStateDAO.deleteLast()
    fun deleteAll() = gameStateDAO.deleteAll()

    fun registerPoint(playerNumber: Int) {
        game.registerPoint(playerNumber) // update Game with new values
        _currentServerLive.value = game.currentServer
        _player1Live.value = game.player1
        _player2Live.value = game.player2
        _winStatesLive.value = game.winStates
        insertGameScoresToDB() // insert values from Game to DB
    }


    private fun insertGameScoresToDB() {
        viewModelScope.launch {
            gameStateDAO.insertGameState(GameStateEntity(
                p1GameScore = _player1Live.value?.gameScore ?: 0,
                p1MatchScore = _player1Live.value?.matchScore ?: 0,
                p2GameScore = _player2Live.value?.gameScore ?: 0,
                p2MatchScore = _player2Live.value?.matchScore ?: 0,
                currentServer = _currentServerLive.value ?: 1,
                firstServer = _gameRulesLive.value?.firstServer ?: 1,
                isGameWon = _winStatesLive.value?.isGameWon ?: false,
                isMatchWon = _winStatesLive.value?.isMatchWon ?: false,
                isMatchReset = _winStatesLive.value?.isMatchReset ?: false,
                gameToBestOf = _gameRulesLive.value?.GameToBestOf ?: 3,
                gameWonByBestOf = _winStatesLive.value?.gameWonByBestOf ?: 3,
                gameWinner = _winStatesLive.value?.gameWinner ?: 1
            ))
        }
    }


    fun onMatchReset() {
        game.onMatchReset()
        deleteAll()
        insertGameScoresToDB() // uses init values
    }

    override fun onCleared() {
        super.onCleared()
        Timber.tag("lifecycle").d("ViewModel cleared")
    }


}
