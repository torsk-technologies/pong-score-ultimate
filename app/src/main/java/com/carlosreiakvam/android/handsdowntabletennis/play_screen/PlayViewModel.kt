package com.carlosreiakvam.android.handsdowntabletennis.play_screen

import android.util.Log
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlayViewModel : ViewModel() {

    private val _player1Turn = MutableLiveData(true)
    val player1Turn: LiveData<Boolean>
        get() = _player1Turn

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

    fun checkForPlayerSwitch(): Boolean {
        if (_p1GameScore.value!! > 0 || _p2GameScore.value!! > 0)
            if ((_p1GameScore.value!! + _p2GameScore.value!!) % 2 == 0) {
                Log.d("TAG", "checkForPlayerSwitch switcher turn")
                switchPlayerTurn()
                return true
            }
        return false
    }

    fun p1IncreaseGameScore() {
        _p1GameScore.value = _p1GameScore.value?.plus(1)
        if (_p1GameScore.value == 11 && _p2GameScore.value!! <= 9) {
            p1IncreaseMatchScore()
            resetGameScore()
        } else {
            if (_p1GameScore.value!! >= 11 && _p1GameScore.value!! >= _p2GameScore.value!! + 2) {
                p1IncreaseMatchScore()
                resetGameScore()
            }
        }
    }

    fun p2IncreaseGameScore() {
        _p2GameScore.value = _p2GameScore.value?.plus(1)
        if (_p2GameScore.value == 11 && _p1GameScore.value!! <= 9) {
            p2IncreaseMatchScore()
            resetGameScore()
        } else {
            if (_p2GameScore.value!! >= 11 && _p2GameScore.value!! >= _p1GameScore.value!! + 2) {
                p2IncreaseMatchScore()
                resetGameScore()
            }
        }
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
        _player1Turn.value = _player1Turn.value != true
        Log.d("TAG", "player1 turn value")
        Log.d("TAG", _player1Turn.value.toString())
    }


}