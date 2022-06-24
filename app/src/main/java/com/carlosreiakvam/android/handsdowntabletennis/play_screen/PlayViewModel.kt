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
import java.util.*

class PlayViewModel(application: Application) : AndroidViewModel(application) {
    val game = Game()

    private val undoList: LinkedList<Map<Int, Any>> = LinkedList<Map<Int, Any>>()

    private val _gameState: MutableLiveData<GameState> = MutableLiveData()
    val gameState: LiveData<GameState>
        get() = _gameState


    init {
        Timber.d("viewModel initiated")
    }

    fun registerPoint(player: Player, otherPlayer: Player) {
        game.registerPoint(player, otherPlayer)
        updateGameState()
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
        game.player1.gameScore = (peekScores?.get(P1GAMESCORE.index) ?: 0) as Int
        game.player2.gameScore = (peekScores?.get(P2GAMESCORE.index) ?: 0) as Int
        game.player1.matchScore = (peekScores?.get(P1MATCHSCORE.index) ?: 0) as Int
        game.player2.matchScore = (peekScores?.get(P2MATCHSCORE.index) ?: 0) as Int
        game.player1.isCurrentServer = (peekScores?.get(P1CURRENTSERVER.index)) as Boolean
        game.player2.isCurrentServer = (peekScores[P2CURRENTSERVER.index]) as Boolean
        game.isGameWon = (peekScores[ISGAMEWON.index] ?: false) as Boolean
        game.isMatchWon = (peekScores[ISMATCHWON.index] ?: false) as Boolean
        game.isMatchReset = (peekScores[ISMATCHRESET.index] ?: false) as Boolean
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
