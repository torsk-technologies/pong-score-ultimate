package com.carlosreiakvam.android.handsdowntabletennis.score_logic

import timber.log.Timber

class Player(var name: String) {
    var gameScore: Int = 0
    var matchScore: Int = 0

    init {
        Timber.d("Player init. gamescore: $name: $gameScore")
    }


    fun increaseGameScore() {
        Timber.d("inca")
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