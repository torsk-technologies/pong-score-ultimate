package com.carlosreiakvam.android.handsdowntabletennis.play_screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.Scores.*
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.States.*
import com.carlosreiakvam.android.handsdowntabletennis.score_logic.Game
import com.carlosreiakvam.android.handsdowntabletennis.score_logic.Player
import timber.log.Timber
import java.lang.Error
import java.util.*

class PlayViewModel(application: Application) : AndroidViewModel(application) {
    val game = Game()

    private val undoList: LinkedList<GameState> = LinkedList<GameState>()

    private val _gameState: MutableLiveData<GameState> = MutableLiveData()
    val gameState: LiveData<GameState>
        get() = _gameState


    init {
        Timber.d("viewModel initiated")
    }

    fun registerPoint(player: Player, otherPlayer: Player) {
        game.registerPoint(player, otherPlayer)
        updateGameState()
        undoList.add(_gameState.value ?: GameState())
    }

    fun updateGameState() {
        _gameState.value = GameState(
            gameNumber = game.totalMatchPoints,
            gameToBestOf = game.gameToBestOf,
            player1State = PlayerState(
                gameScore = game.player1.gameScore,
                matchScore = game.player1.matchScore,
                isCurrentServer = game.player1.isCurrentServer,
                isFirstServer = game.player1.isFirstServer,
                isGameWinner = game.player1.isGameWinner,
                isMatchWinner = game.player1.isMatchWinner
            ),
            player2State = PlayerState(
                gameScore = game.player2.gameScore,
                matchScore = game.player2.matchScore,
                isCurrentServer = game.player2.isCurrentServer,
                isFirstServer = game.player2.isFirstServer,
                isGameWinner = game.player2.isGameWinner,
                isMatchWinner = game.player2.isMatchWinner
            ),
            winStates = WinStates(
                isGameWon = game.isGameWon,
                isMatchWon = game.isMatchWon,
                isMatchReset = game.isMatchReset,
                gameWonByBestOf = game.gameWonByBestOf
            )
        )
    }


    fun performUndo() {
        undoList.pollLast()
        val peekScores = undoList.peekLast()
        game.player1.gameScore = (peekScores?.player1State?.gameScore ?: 0)
        game.player2.gameScore = (peekScores?.player2State?.gameScore ?: 0)
        game.player1.matchScore = (peekScores?.player1State?.matchScore ?: 0)
        game.player2.matchScore = (peekScores?.player2State?.matchScore ?: 0)
        game.player1.isCurrentServer = (peekScores?.player1State?.isCurrentServer ?: true)
        game.player2.isCurrentServer = (peekScores?.player2State?.isCurrentServer ?: false)
        game.isGameWon = (peekScores?.winStates?.isGameWon ?: false)
        game.isMatchWon = (peekScores?.winStates?.isMatchWon ?: false)
        game.isMatchReset = (peekScores?.winStates?.isMatchReset ?: false)
        updateGameState()
    }

    fun onMatchReset() {
        game.onMatchReset()
        updateGameState()
    }

    override fun onCleared() {
        super.onCleared()
        Timber.tag("lifecycle").d("Viewmodel cleared")
    }
}
