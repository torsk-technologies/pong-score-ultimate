package com.carlosreiakvam.android.handsdowntabletennis.score_logic

import timber.log.Timber

class Game {

    var player1 = Player("player one", 1)
    var player2 = Player("player two", 2)

    //    var currentPlayerServer = player1
//    var firstServerPlayer = player1
    var totalMatchPoints = 0
    var isGameWon = false
    var gameWonByBestOf = 3
    var isMatchWon = false
    var isMatchReset = false
    var gameToBestOf = false

    //    var bestOf = BESTOFDEFAULT.int
    private val matchPool = mapOf(
        3 to 2, 5 to 3, 7 to 4, 9 to 5, 11 to 6, 13 to 7, 15 to 8, 17 to 9, 19 to 10, 21 to 11
    )

    init {
        player1.isFirstServer = true
        player1.isCurrentServer = true
    }


    fun registerPoint(player: Player, otherPlayer: Player) {
        resetGameStates()
        player.increaseGameScore()

        if (isGameWon(player, otherPlayer)) {
            player.increaseMatchScore()

            if (matchWonByBestOf(player)) {
                Timber.d("match won")
                // return if maximum limit is reached
                if (gameWonByBestOf > 21) {
                    onMatchReset()
                    return
                }
                isMatchWon = true
                player1.resetGameScore()
                player2.resetGameScore()
                player1.isCurrentServer = true
                player1.isFirstServer = true

                return
            } else {
                // on game won
                Timber.d("game won")
                isGameWon = true
                totalMatchPoints += 1
                player1.resetGameScore()
                player2.resetGameScore()
                serveSwitchOnGameWon()
                return
            }
        } else if (serveSwitchOnPoint(player, otherPlayer)) serveSwitch()
    }

    private fun resetGameStates() {
        isGameWon = false
        isMatchWon = false
        isMatchReset = false
    }

    private fun serveSwitch() {
        if (player1.isCurrentServer) {
            player2.isCurrentServer = true
            player1.isCurrentServer = false
        } else if (player2.isCurrentServer) {
            player1.isCurrentServer = true
            player2.isCurrentServer = false
        }
    }

    fun onMatchReset() {
        player1.isFirstServer = true
        player1.isCurrentServer = true
        isMatchReset = true
        gameWonByBestOf = 3
        totalMatchPoints = 0
        player1.resetGameScore(); player1.resetMatchScore()
        player2.resetGameScore(); player2.resetMatchScore()
    }


    private fun isGameWon(player: Player, otherPlayer: Player): Boolean {
        return player.gameScore == 11 && otherPlayer.gameScore <= 9 ||
                player.gameScore >= 11 && player.gameScore >= otherPlayer.gameScore + 2
    }

    private fun matchWonByBestOf(player: Player): Boolean {
        if (matchPool[gameWonByBestOf] == player.matchScore) {
            gameWonByBestOf += 2
            return true
        }
        return false
    }

    private fun serveSwitchOnGameWon() {
        if (player1.isFirstServer && totalMatchPoints % 2 != 0) {
            player2.isCurrentServer = true
            player1.isCurrentServer = false
        } else {
            player2.isCurrentServer = false
            player1.isCurrentServer = true
        }
    }

    private fun serveSwitchOnPoint(player: Player, otherPlayer: Player): Boolean {
        return (player.gameScore >= 10 && otherPlayer.gameScore >= 10 ||
                (player.gameScore.plus(otherPlayer.gameScore) % 2 == 0))
    }
}