package com.carlosreiakvam.android.handsdowntabletennis.score_logic

import com.carlosreiakvam.android.handsdowntabletennis.play_screen.GameRules
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.Players.PLAYER1
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.Players.PLAYER2
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.WinStates
import timber.log.Timber

class Game(
    var player1: Player,
    var player2: Player,
    var gameRules: GameRules,
) {
    var winStates = WinStates()
    var pointsPlayed = 0
    var currentServer = gameRules.firstServer

    private val matchPool = mapOf(
        3 to 2, 5 to 3, 7 to 4, 11 to 6, 15 to 8, 21 to 11
    )

    init {
        Timber.d("game init")
    }


    private fun numberToPlayer(playerNumber: Int): List<Player> {
        return if (playerNumber == 1) listOf(player1, player2)
        else listOf(player2, player1)
    }

    fun registerPoint(playerNumber: Int) {
        resetWinAndResetStates()
        val players = numberToPlayer(playerNumber)
        val player = players[0]
        val otherPlayer = players[1]

        player.increaseGameScore()
        pointsPlayed.plus(1)

        if (isGameWon(player, otherPlayer)) {
            player.increaseMatchScore()

            if (matchWonByBestOf(player)) {
                Timber.d("match won")
                // return if maximum limit is reached
                if (winStates.gameWonByBestOf > 21) {
                    onMatchReset()
                    return
                }
                winStates.isMatchWon = true
                player1.resetGameScore()
                player2.resetGameScore()
                currentServer = gameRules.firstServer
                return
            } else {
                // on game won
                Timber.d("game won")
                winStates.isGameWon = true
                player1.resetGameScore()
                player2.resetGameScore()
                serveSwitchOnGameWon()
                return
            }
        } else if (serveSwitchOnPoint(player, otherPlayer)) serveSwitch()

        Timber.d("player1 gamescore: ${player1.gameScore}")
        Timber.d("player1 matchScore: ${player1.matchScore}")
    }

    private fun resetWinAndResetStates() {
        if (winStates.isGameWon || winStates.isMatchWon || winStates.isMatchReset) {
            winStates.isGameWon = false
            winStates.isMatchWon = false
            winStates.isMatchReset = false
        }
    }

    private fun serveSwitch() {
        currentServer = if (currentServer == PLAYER1.i) PLAYER2.i else PLAYER1.i
    }

    fun onMatchReset() {
        currentServer = gameRules.firstServer
        winStates.isMatchReset = true
        winStates.gameWonByBestOf = 3
        pointsPlayed = 0

        player1.resetGameScore(); player1.resetMatchScore()
        player2.resetGameScore(); player2.resetMatchScore()
    }


    private fun isGameWon(player: Player, otherPlayer: Player): Boolean {
        return player.gameScore == 11 && otherPlayer.gameScore <= 9 ||
                player.gameScore >= 11 && player.gameScore >= otherPlayer.gameScore + 2
    }

    private fun matchWonByBestOf(player: Player): Boolean {
        if (matchPool[winStates.gameWonByBestOf] == player.matchScore) {
            winStates.gameWonByBestOf += 2
            return true
        }
        return false
    }

    private fun serveSwitchOnGameWon() {
        currentServer = if (gameRules.firstServer == PLAYER1.i && pointsPlayed % 2 != 0) PLAYER1.i
        else PLAYER2.i
    }

    private fun serveSwitchOnPoint(player: Player, otherPlayer: Player): Boolean {
        return (player.gameScore >= 10 && otherPlayer.gameScore >= 10 ||
                (player.gameScore.plus(otherPlayer.gameScore) % 2 == 0))
    }

}