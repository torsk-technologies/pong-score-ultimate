package com.carlosreiakvam.android.handsdowntabletennis.play_screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlayViewModel : ViewModel() {

    private val _p1Turn = MutableLiveData("X")
    val p1Turn: LiveData<String>
        get() = _p1Turn

    private val _p2Turn = MutableLiveData("")
    val p2Turn: LiveData<String>
        get() = _p2Turn

    private val _p1GameScore = MutableLiveData(0)
    val p1GameScore: LiveData<Int>
        get() = _p1GameScore

    private val _p1MatchScore = MutableLiveData(0)
    val p1MatchScore: LiveData<Int>
        get() = _p1MatchScore

    private var _p2GameScore = MutableLiveData(0)
    val p2GameScore: LiveData<Int>
        get() = _p2GameScore

    private val _p2MatchScore = MutableLiveData(0)
    val p2MatchScore: LiveData<Int>
        get() = _p2MatchScore

    init {
        Log.d("tag", "init i viewmodel")
    }

    fun checkForPlayerSwitch() {
        if (_p1GameScore.value!! >= 10 && _p2GameScore.value!! >= 10) {
            switchPlayerTurn()
        } else if ((_p1GameScore.value!! + _p2GameScore.value!!) % 2 == 0) {
            switchPlayerTurn()
        }
    }


    fun increaseGameScore(player: Int) {
        when (player) {
            1 -> {
                _p1GameScore.value = _p1GameScore.value?.plus(1)
                if (_p1GameScore.value == 11 && _p2GameScore.value!! <= 9) {
                    p1IncreaseMatchScore()
                    resetGameScore()
                } else if (_p1GameScore.value!! >= 11 && _p1GameScore.value!! >= _p2GameScore.value!! + 2) {
                    p1IncreaseMatchScore()
                    resetGameScore()
                }
            }
            2 -> {
                _p2GameScore.value = _p2GameScore.value?.plus(1)
                if (_p2GameScore.value == 11 && _p1GameScore.value!! <= 9) {
                    p2IncreaseMatchScore()
                    resetGameScore()
                } else if (_p2GameScore.value!! >= 11 && _p2GameScore.value!! >= _p1GameScore.value!! + 2) {
                    p2IncreaseMatchScore()
                    resetGameScore()
                }
            }
        }
        checkForPlayerSwitch()
    }

    private fun resetGameScore() {
        _p1GameScore.value = 0
        _p2GameScore.value = 0
    }

    private fun p1IncreaseMatchScore() {
        _p1MatchScore.value = _p1MatchScore.value?.plus(1)
    }

    private fun p2IncreaseMatchScore() {
        _p2MatchScore.value = _p2MatchScore.value?.plus(1)
    }

    private fun switchPlayerTurn() {
        if (_p1Turn.value == "X") {
            _p1Turn.value = ""
            _p2Turn.value = "X"
        } else {
            _p1Turn.value = "X"
            _p2Turn.value = ""
        }
    }

//    init {
//        val p1 = Player("Player 1")
//        val p2 = Player("Player 2")
//    }


}

//class Player(val name: String) {
//
//    val _gameScore = MutableLiveData(0)
//    val gamescore: LiveData<Int>
//        get() = _gameScore
//    val _matchScore = MutableLiveData(0)
//    val matchScore: LiveData<Int>
//        get() = _matchScore
//
//    fun increaseScore(other_score: MutableLiveData<Int>) {
//        _gameScore.value = _gameScore.value?.plus(1)
//        if (_gameScore.value == 11 && other_score.value!! <= 9) {
//            increaseMatchScore()
//            resetGameScore()
//        } else if (_gameScore.value!! >= 11 && _gameScore.value!! >= other_score.value!! + 2) {
//            increaseMatchScore()
//            resetGameScore()
//        }
//    }
//
//    fun increaseMatchScore() {
//        _matchScore.value = _matchScore.value?.plus(1)
//    }
//
//    private fun resetGameScore() {
//        _gameScore.value = 0
//    }
//}
