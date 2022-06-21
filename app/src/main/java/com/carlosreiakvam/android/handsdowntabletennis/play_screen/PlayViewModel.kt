package com.carlosreiakvam.android.handsdowntabletennis.play_screen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.Scores.*
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.States.*
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.Turn.*
import com.carlosreiakvam.android.handsdowntabletennis.score_logic.Game
import java.util.*

class PlayViewModel(application: Application) : AndroidViewModel(application) {

    private val undoList: LinkedList<Map<Int, Any>> = LinkedList<Map<Int, Any>>()

    private val _gameState: MutableLiveData<Map<Int, Any>> = MutableLiveData(
        mapOf(
            P1GAMESCORE.index to 0,
            P2GAMESCORE.index to 0,
            P1MATCHSCORE.index to 0,
            P2MATCHSCORE.index to 0,
            GAMENUMBER.index to 1,
            PTURN.index to 1,
            ISGAMESTART.index to true,
        )
    )
    val gameState: LiveData<Map<Int, Any>>
        get() = _gameState

    val game = Game()

    init {
        Log.d("TAG", "viewmodel")
    }

    fun registerPoint(playerNumber: Int) {
        game.isGameStart = false
        when (playerNumber) {
            1 -> {
                game.player1.increaseGameScore()
                if (game.isGameWon(1)) {
                    game.player1.increaseMatchScore()
                    game.newGame()
                    game.isGameStart = true
                    game.currentPlayerServer = 2
                    game.switchServeIfNecessary()
                }
            }
            2 -> {
                game.player2.increaseGameScore()
                if (game.isGameWon(2)) {
                    game.player2.increaseMatchScore()
                    game.newGame()
                    game.isGameStart = true
                    game.currentPlayerServer = 1
                    game.switchServeIfNecessary()
                }
            }
        }
        game.gameNumber += 1
        setGameState()

    }

    fun setGameState() {
        _gameState.value = mapOf(
            P1GAMESCORE.index to game.player1.gameScore,
            P2GAMESCORE.index to game.player2.gameScore,
            P1MATCHSCORE.index to game.player1.matchScore,
            P2MATCHSCORE.index to game.player2.matchScore,
            PTURN.index to game.currentPlayerServer,
            ISGAMESTART.index to game.isGameStart,
            ISMATCHSTART.index to game.isMatchStart
        )
    }


    fun performUndo() {
        Log.d("TAG", "undo")
        undoList.pollLast()
        val peekScores = undoList.peekLast()
        game.player1.gameScore = (peekScores?.get(P1GAMESCORE.index) ?: 0) as Int
        game.player2.gameScore = (peekScores?.get(P2GAMESCORE.index) ?: 0) as Int
        game.player1.matchScore = (peekScores?.get(P1MATCHSCORE.index) ?: 0) as Int
        game.player2.matchScore = (peekScores?.get(P2MATCHSCORE.index) ?: 0) as Int
        game.currentPlayerServer = (peekScores?.get(PTURN.index) ?: 0) as Int
        game.isGameStart = (peekScores?.get(ISGAMESTART.index) ?: false) as Boolean
        game.isMatchStart = (peekScores?.get(ISMATCHSTART.index) ?: false) as Boolean
        setGameState()
        Log.d("TAG", "undolist after undo: ${undoList.peekLast()}")
    }
}
