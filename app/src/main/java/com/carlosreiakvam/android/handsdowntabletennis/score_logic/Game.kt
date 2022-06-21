package com.carlosreiakvam.android.handsdowntabletennis.score_logic

import android.util.Log

class Game {

    var player1 = Player("player one", 1)
    var player2 = Player("player two", 1)
    var currentPlayerServer = 1
    var gameNumber = 1
    var isGameStart = false
    var isMatchStart = false


    fun registerPoint(playerNumber: Int) {
        isGameStart = false
        when (playerNumber) {
            1 -> {
                player1.increaseGameScore()
                if (isGameWon(1)) {
                    onGameWon(1)
                }
            }
            2 -> {
                player2.increaseGameScore()
                if (isGameWon(2)) {
                    onGameWon(2)
                }
            }
        }
        gameNumber += 1
        switchServeIfNecessary()
    }

    fun newGame() {
        player1.resetGameScore()
        player2.resetGameScore()
        isGameStart = true
        currentPlayerServer = if (player1.isFirstServer && gameNumber % 2 != 0) 1 else 2
        Log.d("TAG", "current server: $currentPlayerServer")
    }

    /**
     * @param firstServerPlayer must be 1 or 2
     */
    fun newMatch(firstServerPlayer: Int) {
        when (firstServerPlayer) {
            1 -> player1.isFirstServer = true
            2 -> player2.isFirstServer = true
        }
        player1.resetGameScore()
        player1.resetMatchScore()
        player2.resetGameScore()
        player2.resetMatchScore()
        currentPlayerServer = 1
    }

    /**
     * @return player number of player that won, or -1 if no winner.
     */
    private fun isGameWon(player: Int): Boolean {
        return when (player) {
            1 -> player1.gameScore == 11 && player2.gameScore <= 9 ||
                    player1.gameScore >= 11 && player1.gameScore >= player2.gameScore + 2
            2 -> player2.gameScore == 11 && player1.gameScore <= 9 ||
                    player2.gameScore >= 11 && player2.gameScore >= player1.gameScore + 2
            else -> false
        }
    }

    private fun onGameWon(player: Int) {
        when (player) {
            1 -> player1.increaseMatchScore()
            2 -> player2.increaseMatchScore()
        }
        newGame()

    }

    private fun switchServeIfNecessary() {
        if (player1.gameScore >= 10 && player2.gameScore >= 10 ||
            (player1.gameScore.plus(player2.gameScore) % 2 == 0)
        ) {
            switchPlayerServer()
        }
    }

    private fun switchPlayerServer() {
        if (currentPlayerServer == 1) currentPlayerServer = 2
        else if (currentPlayerServer == 2) currentPlayerServer = 1
    }

//    fun checkIfGameStart() {
//        if (player1.gameScore == 0 && player2.gameScore == 0) isGameStart = true
//    }
//
//    fun checkIfMatchStart() {
//        if (player1.gameScore == 0 && player2.gameScore == 0
//            && player1.matchScore == 0 && player2.matchScore == 0
//        ) isMatchStart = true
//    }

}