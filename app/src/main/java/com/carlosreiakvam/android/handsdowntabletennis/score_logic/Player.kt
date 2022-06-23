package com.carlosreiakvam.android.handsdowntabletennis.score_logic

class Player(var name: String, var playerNumber: Int) {
    var gameScore: Int = 0
    var matchScore: Int = 0
    var gameWon: Boolean = false
    var matchWon: Boolean = false
    var isFirstServer: Boolean = false // to be implemented


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