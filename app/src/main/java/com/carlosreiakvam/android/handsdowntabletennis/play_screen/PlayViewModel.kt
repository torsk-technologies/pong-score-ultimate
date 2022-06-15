package com.carlosreiakvam.android.handsdowntabletennis.play_screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.Constants.*
import java.util.*

class PlayViewModel : ViewModel() {

    private val undoList: LinkedList<Map<Int, Int>> = LinkedList<Map<Int, Int>>()

    private val _gameState = MutableLiveData<Map<Int, Int>>(
        mapOf(
            P1GAMESCORE.int to 0,
            P2GAMESCORE.int to 0,
            P1MATCHSCORE.int to 0,
            P2MATCHSCORE.int to 0,
            PTURN.int to 0
        )
    )
    val gameState: LiveData<Map<Int, Int>>
        get() = _gameState


    init {
        Log.d("TAG", "viewmodel")
    }


    fun setGameState(
        p1GameScore: Int = gameState.value?.get(P1GAMESCORE.int)!!,
        p2GameScore: Int = gameState.value?.get(P2GAMESCORE.int)!!,
        p1MatchScore: Int = gameState.value?.get(P1MATCHSCORE.int)!!,
        p2MatchScore: Int = gameState.value?.get(P2MATCHSCORE.int)!!,
        pTurn: Int = gameState.value?.get(PTURN.int)!!
    ) {
        _gameState.value = mapOf(
            P1GAMESCORE.int to p1GameScore,
            P2GAMESCORE.int to p2GameScore,
            P1MATCHSCORE.int to p1MatchScore,
            P2MATCHSCORE.int to p2MatchScore,
            PTURN.int to pTurn
        )

    }

    private fun isMatchWon(playerGameScore: Int, otherPlayerGameScore: Int): Boolean {
        val clickedPlayerHasNormalWin =
            _gameState.value!![playerGameScore] == 11 && _gameState.value!![otherPlayerGameScore]!! <= 9

        val clickedPlayerHasOvertimeWin = _gameState.value!![playerGameScore]!! >= 11
                && _gameState.value!![playerGameScore]!! >= _gameState.value!![otherPlayerGameScore]!!.plus(
            2
        )

        return clickedPlayerHasNormalWin || clickedPlayerHasOvertimeWin
    }


    fun registerPoint(playerGameScore: Int, otherPlayerGameScore: Int, playerMatchScore: Int) {
        increaseGameScore(playerGameScore)

        if (isMatchWon(playerGameScore, otherPlayerGameScore)) {
            increaseMatchScore(playerMatchScore)
            resetGameScore()
        }

        Log.d("TAG", "gamestate.value: ${_gameState.value}")
        undoList.add(_gameState.value!!)
        Log.d("TAG", "undolist: ${undoList.peekLast()}")
//        checkForServeSwitch()
    }


    private fun resetGameScore() {
        setGameState(p1GameScore = 0, p2GameScore = 0)
    }

    private fun increaseGameScore(playerGameScore: Int) {
        if (playerGameScore == P1GAMESCORE.int) {
            setGameState(p1GameScore = _gameState.value?.get(playerGameScore)?.plus(1) ?: 0)
        } else {
            setGameState(p2GameScore = _gameState.value?.get(playerGameScore)?.plus(1) ?: 0)
        }
    }


    private fun increaseMatchScore(playerMatchScore: Int) {
        if (playerMatchScore == P1MATCHSCORE.int) {
            setGameState(p1MatchScore = _gameState.value?.get(playerMatchScore)?.plus(1) ?: 0)
        } else {
            setGameState(p2MatchScore = _gameState.value?.get(playerMatchScore)?.plus(1) ?: 0)

        }

    }

//    private fun checkForServeSwitch() {
//        if (_p1GameScore.value!! >= 10 && _p2GameScore.value!! >= 10) {
//            switchPlayerTurn()
//        } else if ((_p1GameScore.value!! + _p2GameScore.value!!) % 2 == 0) {
//            switchPlayerTurn()
//        }
//    }


//    private fun switchPlayerTurn() {
//        if (_p1Turn.value == "X") {
//            _p1Turn.value = ""
//            _p2Turn.value = "X"
//        } else {
//            _p1Turn.value = "X"
//            _p2Turn.value = ""
//        }
//    }
//

    fun resetMatch() {
        setGameState(p1GameScore = 0, p2GameScore = 0, p1MatchScore = 0, p2MatchScore = 0)
    }

    fun undo() {
        Log.d("TAG", "undo")
        undoList.pollLast()
        val peekScores = undoList.peekLast()
        setGameState(
            p1GameScore = peekScores?.get(P1GAMESCORE.int) ?: 0,
            p2GameScore = peekScores?.get(P2GAMESCORE.int) ?: 0,
            p1MatchScore = peekScores?.get(P1MATCHSCORE.int) ?: 0,
            p2MatchScore = peekScores?.get(P2MATCHSCORE.int) ?: 0,
            pTurn = peekScores?.get(PTURN.int) ?: 0
        )
        Log.d("TAG", "undolist after undo: ${undoList.peekLast()}")
    }
}
