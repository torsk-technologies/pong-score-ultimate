package com.carlosreiakvam.android.handsdowntabletennis.play_screen

import com.carlosreiakvam.android.handsdowntabletennis.play_screen.Players.*


data class GameState(
    val gameNumber: Int = 1,
    val gameToBestOf: Boolean = false,
    val player1State: PlayerState = PlayerState(
        gameScore = 0,
        matchScore = 0,
        isFirstServer = true,
        isCurrentServer = true,
        isGameWinner = false,
        isMatchWinner = false
    ),
    val player2State: PlayerState = PlayerState(
        gameScore = 0,
        matchScore = 0,
        isFirstServer = false,
        isCurrentServer = false,
        isGameWinner = false,
        isMatchWinner = false
    ),
    val winStates: WinStates = WinStates(
        isGameWon = false,
        isMatchWon = false,
        isMatchReset = false,
    )
)

data class PlayerState(
    val gameScore: Int = 0,
    val matchScore: Int = 0,
    val isFirstServer: Boolean = false,
    val isCurrentServer: Boolean = false,
    val isGameWinner: Boolean = false,
    val isMatchWinner: Boolean = false,
)

data class WinStates(
    val isGameWon: Boolean = false,
    val isMatchWon: Boolean = false,
    val isMatchReset: Boolean = false,
    val matchWonByPlayer: Int = PLAYER1.playerNumber,
    val gameWonByBestOf: Int = 3
)

