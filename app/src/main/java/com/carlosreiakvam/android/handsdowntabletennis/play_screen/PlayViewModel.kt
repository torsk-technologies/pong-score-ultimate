package com.carlosreiakvam.android.handsdowntabletennis.play_screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlayViewModel : ViewModel() {

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


    fun p1IncreaseGameScore() {
        _p1GameScore.value = _p1GameScore.value?.plus(1)
    }

    fun p2IncreaseGameScore() {
        _p2GameScore.value = _p2GameScore.value?.plus(1)
    }

}