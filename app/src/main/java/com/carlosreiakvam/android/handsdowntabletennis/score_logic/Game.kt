package com.carlosreiakvam.android.handsdowntabletennis.score_logic

import com.carlosreiakvam.android.handsdowntabletennis.play_screen.GameRules
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.Players.PLAYER1
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.Players.PLAYER2
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.WinConditions
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.WinStates
import timber.log.Timber

class Game(
    var player1: Player,
    var player2: Player,
    var gameRules: GameRules,
) {
    var winStates = WinStates()
    var nGamesPlayed = 0
    var currentServer = gameRules.firstServer
    private var winConditions = WinConditions()
    private val matchPool = mapOf(
        3 to 2, 5 to 3, 7 to 4, 9 to 5, 11 to 6, 13 to 7, 15 to 8, 17 to 9, 19 to 10, 21 to 11
    )

    init {
        Timber.d("game init")
        Timber.d("first server: ${gameRules.firstServer}")
        Timber.d("best of: ${gameRules.bestOf}")
    }


    fun registerPoint(playerNumber: Int) {
        resetWinAndResetStates()
        val players = numberToPlayer(playerNumber)
        val player = players[0]
        val otherPlayer = players[1]

        player.increaseGameScore()

        if (winConditions.isGameWon(player, otherPlayer)) {
            player.increaseMatchScore()

            if (winConditions.matchWonByBestOf(matchPool, gameRules, player)) {
                Timber.d("match won")
                // return if maximum limit is reached
                winStates.isMatchWon = true
                player1.resetGameScore()
                player2.resetGameScore()
                currentServer = gameRules.firstServer
                return
            } else {
                // on game won
                Timber.d("game won")
                nGamesPlayed += 1
                winStates.isGameWon = true
                player1.resetGameScore()
                player2.resetGameScore()
                serveSwitchOnGameWon()
                winStates.isGameWon = true
                return
            }
        } else {
            winStates.isGameWon = winConditions.isGameWon(player, otherPlayer)

            if (serveSwitchOnPoint(player, otherPlayer)) serveSwitch()
        }

        Timber.d("player1 gamescore: ${player1.gameScore}")
        Timber.d("player1 matchScore: ${player1.matchScore}")
    }

    private fun numberToPlayer(playerNumber: Int): List<Player> {
        return if (playerNumber == 1) listOf(player1, player2)
        else listOf(player2, player1)
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

    private fun serveSwitchOnGameWon() {
        Timber.d("game points played: $nGamesPlayed")
        Timber.d("game points played mod 2: ${nGamesPlayed % 2}")

        if (gameRules.firstServer == PLAYER1.i) {
            currentServer = if (nGamesPlayed % 2 == 0) PLAYER1.i else PLAYER2.i
        } else if (gameRules.firstServer == PLAYER2.i)
            currentServer = if (nGamesPlayed % 2 == 0) PLAYER2.i else PLAYER1.i
        Timber.d("current server: $currentServer")
    }

    private fun serveSwitchOnPoint(player: Player, otherPlayer: Player): Boolean {
        return (player.gameScore >= 10 && otherPlayer.gameScore >= 10 ||
                (player.gameScore.plus(otherPlayer.gameScore) % 2 == 0))
    }

}