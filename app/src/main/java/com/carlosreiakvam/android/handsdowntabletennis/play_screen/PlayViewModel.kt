package com.carlosreiakvam.android.handsdowntabletennis.play_screen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.Constants.*
import kotlinx.coroutines.withContext
import java.util.*

class PlayViewModel(application: Application) : AndroidViewModel(application) {

    private val undoList: LinkedList<Map<Int, Int>> = LinkedList<Map<Int, Int>>()


    private val _gameStart: MutableLiveData<Boolean> = MutableLiveData(false)
    val gameStart: LiveData<Boolean>
        get() = _gameStart


    private val _gameState = MutableLiveData<Map<Int, Int>>(
        mapOf(
            P1GAMESCORE.int to 0,
            P2GAMESCORE.int to 0,
            P1MATCHSCORE.int to 0,
            P2MATCHSCORE.int to 0,
            PTURN.int to 1
        )
    )
    val gameState: LiveData<Map<Int, Int>>
        get() = _gameState


    init {
        Log.d("TAG", "viewmodel")
    }


//    fun setupPrefs() {
//        val preferences = PreferenceManager.getDefaultSharedPreferences(getApplication())
//        preferences.
//    }


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

        undoList.add(_gameState.value!!)
        checkForServeSwitch()
    }


    private fun resetGameScore() {
        setGameState(p1GameScore = 0, p2GameScore = 0)
    }

    fun setupNewGame() {
        _gameStart.value = false
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

    private fun checkForServeSwitch() {
        if (_gameState.value?.get(P1GAMESCORE.int)!! >= 10 && _gameState.value?.get(P2GAMESCORE.int)!! >= 10) {
            switchPlayerTurn()
        } else if ((_gameState.value!!.get(P1GAMESCORE.int)!!
                .plus(_gameState.value!!.get(P2GAMESCORE.int)!!)) % 2 == 0
        ) {
            switchPlayerTurn()
        }
    }

    fun chooseServer(player: Int) {
        if (player == 1) {
            setGameState(pTurn = 1)
        } else {
            setGameState(pTurn = 2)
        }

    }


    private fun switchPlayerTurn() {
        if (_gameState.value?.get(PTURN.int) == 1) {
            setGameState(pTurn = 2)
        } else {
            setGameState(pTurn = 1)
        }
    }


    fun resetMatch() {
        setGameState(p1GameScore = 0, p2GameScore = 0, p1MatchScore = 0, p2MatchScore = 0)
        _gameStart.value = true
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
