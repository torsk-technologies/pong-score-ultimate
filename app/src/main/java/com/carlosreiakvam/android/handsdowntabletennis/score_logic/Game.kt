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
    var wonByBestOf = 3
    var isMatchWon = false
    var isMatchReset = false

    //    var bestOf = BESTOFDEFAULT.int
    private val matchPool = mapOf(
        3 to 2, 5 to 3, 7 to 4, 9 to 5, 11 to 6, 13 to 7, 15 to 8, 17 to 9, 19 to 10, 21 to 11
    )


    fun registerPoint(player: Player, otherPlayer: Player) {
        resetGameStates()
        player.increaseGameScore()

        if (isGameWon(player, otherPlayer)) {
            player.increaseMatchScore()

            if (matchWonByBestOf(player)) {
                Timber.d("match won")
                // return if maximum limit is reached
                if (wonByBestOf > 21) {
                    onMatchReset(player)
                    return
                }
                isMatchWon = true
                player1.resetGameScore()
                player2.resetGameScore()
                this.firstServerPlayer = player1
                this.currentPlayerServer = player1
                return
            } else {
                Timber.d("game won")
                isGameWon = true
                gameNumber += 1
                player1.resetGameScore()
                player2.resetGameScore()
                if (serveSwitchOnGameWon()) serveSwitch()
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
        currentPlayerServer = if (currentPlayerServer == player1) player2
        else player1
    }

    fun onMatchReset(firstServerPlayer: Player) {
        this.firstServerPlayer = firstServerPlayer
        this.currentPlayerServer = firstServerPlayer
        isMatchReset = true
        wonByBestOf = 3
        gameNumber = 1
        player1.resetGameScore(); player1.resetMatchScore()
        player2.resetGameScore(); player2.resetMatchScore()
    }


    private fun isGameWon(player: Player, otherPlayer: Player): Boolean {
        return player.gameScore == 11 && otherPlayer.gameScore <= 9 ||
                player.gameScore >= 11 && player.gameScore >= otherPlayer.gameScore + 2
    }

    private fun matchWonByBestOf(player: Player): Boolean {
        if (matchPool[wonByBestOf] == player.matchScore) {
            wonByBestOf += 2
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