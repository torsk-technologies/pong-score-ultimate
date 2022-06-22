package com.carlosreiakvam.android.handsdowntabletennis.score_logic

import com.carlosreiakvam.android.handsdowntabletennis.play_screen.Defaults.BESTOFDEFAULT
import timber.log.Timber

class Game {

    var player1 = Player("player one", 1)
    var player2 = Player("player two", 2)
    var currentPlayerServer = player1
    var firstServerPlayer = player1
    var gameNumber = 1
    var isGameStart = false
    var isMatchStart = false
    var bestOf = BESTOFDEFAULT.int
    private val matchPool = mapOf(
        3 to 2, 5 to 3, 7 to 4, 9 to 5, 11 to 6, 13 to 7, 15 to 8, 17 to 9, 19 to 10, 21 to 11
    )


    fun registerPoint(player: Player, otherPlayer: Player) {
        isGameStart = false
        isMatchStart = false
        player.increaseGameScore()

        if (isGameWon(player, otherPlayer)) {
            player.increaseMatchScore()
            if (isMatchWon(player)) {
                initNewMatch(player)
                return
            } else {
                initNewGame()
                if (serveSwitchOnGameWon()) serveSwitch()
                return
            }
        } else if (serveSwitchOnPoint(player, otherPlayer)) serveSwitch()
    }

    private fun serveSwitch() {
        currentPlayerServer = if (currentPlayerServer == player1) player2
        else player1
    }

    private fun initNewGame() {
        Timber.d("new game")
        isGameStart = true
        gameNumber += 1
        player1.resetGameScore()
        player2.resetGameScore()
        if (firstServerPlayer == player1 && gameNumber % 2 != 0) {
            currentPlayerServer = player1
        } else if (firstServerPlayer == player2 && gameNumber % 2 == 0) {
            currentPlayerServer = player2
        }
    }

    fun initNewMatch(firstServerPlayer: Player) {
        Timber.d("new match")
        this.firstServerPlayer = firstServerPlayer
        gameNumber = 1
        player1.resetGameScore(); player1.resetMatchScore()
        player2.resetGameScore(); player2.resetMatchScore()
    }

    private fun isGameWon(player: Player, otherPlayer: Player): Boolean {
        return player.gameScore == 11 && otherPlayer.gameScore <= 9 ||
                player.gameScore >= 11 && player.gameScore >= otherPlayer.gameScore + 2
    }

    private fun isMatchWon(player: Player): Boolean {
        if (matchPool[bestOf] == player.matchScore) {
            initNewMatch(player1)
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