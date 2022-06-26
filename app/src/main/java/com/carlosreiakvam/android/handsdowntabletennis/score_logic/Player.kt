package com.carlosreiakvam.android.handsdowntabletennis.score_logic

import timber.log.Timber

class Player(var name: String, var playerNumber: Int) {
    var gameScore: Int = 0
    var matchScore: Int = 0
    var isFirstServer: Boolean = false
    var isCurrentServer: Boolean = false
    var isGameWinner: Boolean = false
    var isMatchWinner: Boolean = false

    init {
        Timber.d("gamescore $name: $gameScore")
    }


    fun increaseGameScore() {
        gameScore += 1
    }

    fun resetGameScore() {
        gameScore = 0
    }

    fun resetMatchScore() {
        matchScore = 0
    }

    fun increaseMatchScore() {
        matchScore += 1
    }
}