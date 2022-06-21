package com.carlosreiakvam.android.handsdowntabletennis.score_logic

import android.util.Log
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.Defaults.BESTOFDEFAULT

class Game {

    var player1 = Player("player one", 1)
    var player2 = Player("player two", 2)
    var currentPlayerServer = player1
    var gameNumber = 1
    var isGameStart = false
    var isMatchStart = false
    var bestOf = BESTOFDEFAULT.int


    fun registerPoint(player: Player) {
        isGameStart = false
        player.increaseGameScore()
        if (isMatchWon(player)) {
            newMatch(player)
            gameNumber += 1
        } else if (isGameWon(player)) {
            onGameWon(player)
            gameNumber += 1
        }
        switchServeIfNecessary()
        Log.d("slabras", "gamenumber: $gameNumber")
    }

    private fun onMatchWon() {
        TODO("Not yet implemented")
    }

    private fun newGame() {
        gameNumber = 1
        player1.resetGameScore()
        player2.resetGameScore()
        isGameStart = true
        if (player1.isFirstServer && gameNumber % 2 != 0) {
            currentPlayerServer = player1
        } else if (player2.isFirstServer && gameNumber % 2 == 0) {
            currentPlayerServer = player2
        }
        Log.d("slabras", "current server: $currentPlayerServer")
    }

    fun newMatch(firstServerPlayer: Player) {
        when (firstServerPlayer) {
            player1 -> {
                currentPlayerServer = player1
                player1.isFirstServer = true
                player2.isFirstServer = false
            }
            player2 -> {
                currentPlayerServer = player2
                player2.isFirstServer = true
                player1.isFirstServer = false
            }
        }
        gameNumber = 1
        player1.resetGameScore()
        player1.resetMatchScore()
        player2.resetGameScore()
        player2.resetMatchScore()
    }

    fun isGameWon(player: Player): Boolean {
        return when (player) {
            player1 -> player1.gameScore == 11 && player2.gameScore <= 9 ||
                    player1.gameScore >= 11 && player1.gameScore >= player2.gameScore + 2
            player2 -> player2.gameScore == 11 && player1.gameScore <= 9 ||
                    player2.gameScore >= 11 && player2.gameScore >= player1.gameScore + 2
            else -> false
        }
    }

    fun isMatchWon(player: Player): Boolean {
        return when (bestOf) {
            3 -> player.matchScore == 2
            5 -> player.matchScore == 3
            7 -> player.matchScore == 4
            9 -> player.matchScore == 5
            11 -> player.matchScore == 6
            13 -> player.matchScore == 7
            15 -> player.matchScore == 8
            17 -> player.matchScore == 9
            19 -> player.matchScore == 10
            21 -> player.matchScore == 11
            else -> false
        }
    }

    private fun onGameWon(player: Player) {
        player.increaseMatchScore()
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
        if (currentPlayerServer == player1) currentPlayerServer = player2
        else if (currentPlayerServer == player2) currentPlayerServer = player1
    }
}