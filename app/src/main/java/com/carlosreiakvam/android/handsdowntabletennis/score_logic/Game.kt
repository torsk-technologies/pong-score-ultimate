package com.carlosreiakvam.android.handsdowntabletennis.score_logic

import android.util.Log
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.Defaults.BESTOFDEFAULT

class Game {

    var player1 = Player("player one", 1)
    var player2 = Player("player two", 2)
    var currentPlayerServer = player1
    var firstServerPlayer = player1
    var gameNumber = 1
    var isGameStart = false
    var isMatchStart = false
    var bestOf = BESTOFDEFAULT.int
    val matchPool = arrayListOf(3, 5, 7, 9, 11, 13, 15, 17, 19, 21)


    fun registerPoint(player: Player, otherPlayer: Player) {
        isGameStart = false
        isMatchStart = false

        if (reactIfMatchWon(player) || reactIfGameWon(player, otherPlayer)) return

        player.increaseGameScore()
        switchServeIfNecessary(player, otherPlayer)
    }


    private fun newGame() {
        Log.d("slabras", "isFirstServer: ${firstServerPlayer.name}")
        isGameStart = true
        gameNumber += 1
        player1.resetGameScore()
        player2.resetGameScore()
        if (firstServerPlayer == player1 && gameNumber % 2 != 0) {
            currentPlayerServer = player1
        } else if (firstServerPlayer == player2 && gameNumber % 2 == 0) {
            currentPlayerServer = player2
        }
        Log.d("slabras", "current server: ${currentPlayerServer.name}")
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
            Log.d("slabras", "gamenumber: $gameNumber")
            return true
        }
        return false
    }

    private fun reactIfMatchWon(player: Player): Boolean {
        var j = 2
        for (i in matchPool) {
            if (bestOf == i && player.matchScore == j) return true
            j++
        }
        return false
    }

    private fun switchServeIfNecessary(player: Player, otherPlayer: Player) {
        if (player.gameScore >= 10 && otherPlayer.gameScore >= 10 ||
            (player.gameScore.plus(otherPlayer.gameScore) % 2 == 0)
        ) {
            Log.d("slabras", "switching servers")
            currentPlayerServer = otherPlayer
        }
    }
}