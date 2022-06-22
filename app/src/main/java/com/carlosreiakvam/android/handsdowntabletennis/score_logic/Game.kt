package com.carlosreiakvam.android.handsdowntabletennis.score_logic

import android.util.Log
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

        if (reactIfMatchWon(player)) {
            Timber.d("react if match won is true")
            return
        } else if (reactIfGameWon(player, otherPlayer)) {
            return
        } else {
            switchServeIfNecessary(player, otherPlayer)
        }
    }


    private fun newGame() {
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

    fun newMatch(firstServerPlayer: Player) {
        this.firstServerPlayer = firstServerPlayer
        gameNumber = 1
        player1.resetGameScore(); player1.resetMatchScore()
        player2.resetGameScore(); player2.resetMatchScore()
    }

    private fun reactIfGameWon(player: Player, otherPlayer: Player): Boolean {
        if (player.gameScore == 11 && otherPlayer.gameScore <= 9 ||
            player.gameScore >= 11 && player.gameScore >= otherPlayer.gameScore + 2
        ) {
            player.increaseMatchScore()
            newGame()
            return true
        }
        return false
    }

    private fun reactIfMatchWon(player: Player): Boolean {
        return matchPool[bestOf] == player.matchScore
    }

    private fun switchServeIfNecessary(player: Player, otherPlayer: Player) {
        if (player.gameScore >= 10 && otherPlayer.gameScore >= 10 ||
            (player.gameScore.plus(otherPlayer.gameScore) % 2 == 0)
        ) {
            currentPlayerServer = otherPlayer
        }
    }
}