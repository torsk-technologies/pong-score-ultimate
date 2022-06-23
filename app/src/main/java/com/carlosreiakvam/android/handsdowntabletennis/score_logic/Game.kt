package com.carlosreiakvam.android.handsdowntabletennis.score_logic

import com.carlosreiakvam.android.handsdowntabletennis.play_screen.Defaults.BESTOFDEFAULT
import timber.log.Timber

class Game {

    var player1 = Player("player one", 1)
    var player2 = Player("player two", 2)
    var currentPlayerServer = player1
    var firstServerPlayer = player1
    var gameNumber = 1
    var isGameWon = false
    var isMatchWon = false
    var isMatchReset = false
    var bestOf = BESTOFDEFAULT.int
    private val matchPool = mapOf(
        3 to 2, 5 to 3, 7 to 4, 9 to 5, 11 to 6, 13 to 7, 15 to 8, 17 to 9, 19 to 10, 21 to 11
    )


    fun registerPoint(player: Player, otherPlayer: Player) {
        resetGameStates()
        player.increaseGameScore()

        if (isGameWon(player, otherPlayer)) {
            player.increaseMatchScore()
            if (isMatchWon(player)) {
                onMatchWon(player)
                return
            } else {
                onGameWon()
                return
            }
        } else if (serveSwitchOnPoint(player, otherPlayer)) serveSwitch()
    }

    fun resetGameStates() {
        player1.gameWon = false
        player2.gameWon = false
        isGameWon = false
        isMatchWon = false
        isMatchReset = false
    }

    private fun serveSwitch() {
        currentPlayerServer = if (currentPlayerServer == player1) player2
        else player1
    }

    private fun onGameWon() {
        Timber.d("onGameWon")
        isGameWon = true
        gameNumber += 1
        player1.resetGameScore()
        player2.resetGameScore()
        if (serveSwitchOnGameWon()) serveSwitch()
    }

    fun onMatchReset(firstServerPlayer: Player) {
        this.firstServerPlayer = firstServerPlayer
        this.currentPlayerServer = firstServerPlayer
        gameNumber = 1
        isMatchReset = true
        player1.resetGameScore(); player1.resetMatchScore()
        player2.resetGameScore(); player2.resetMatchScore()
    }


    private fun onMatchWon(player: Player) {
        player.gameWon = true
        this.firstServerPlayer = player1
        this.currentPlayerServer = player1
        player1.resetGameScore(); player1.resetMatchScore()
        player2.resetGameScore(); player2.resetMatchScore()
        gameNumber = 1
        isMatchWon = true
    }

    private fun isGameWon(player: Player, otherPlayer: Player): Boolean {
        return player.gameScore == 11 && otherPlayer.gameScore <= 9 ||
                player.gameScore >= 11 && player.gameScore >= otherPlayer.gameScore + 2
    }

    private fun isMatchWon(player: Player): Boolean {
        Timber.d("matchpool bestof val: ${matchPool[bestOf]}")
        Timber.d("player matchscore: ${player.matchScore}")
        if (matchPool[bestOf] == player.matchScore) {
            return true
        }
        return false
    }

    private fun serveSwitchOnGameWon(): Boolean {
        return firstServerPlayer == player1 && gameNumber % 2 == 0
    }

    private fun serveSwitchOnPoint(player: Player, otherPlayer: Player): Boolean {
        return (player.gameScore >= 10 && otherPlayer.gameScore >= 10 ||
                (player.gameScore.plus(otherPlayer.gameScore) % 2 == 0))
    }
}