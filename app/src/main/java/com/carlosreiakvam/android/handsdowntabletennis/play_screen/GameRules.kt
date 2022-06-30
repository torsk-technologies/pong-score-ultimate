package com.carlosreiakvam.android.handsdowntabletennis.play_screen

data class GameRules(
    var GameToBestOf: Int = 3,
    var firstServer: Int = 1,
)

data class WinStates(
    var gameWonByBestOf: Int = 3,
    var gameWinner: Int = 1,
    var isGameWon: Boolean = false,
    var isMatchWon: Boolean = false,
    var isMatchReset: Boolean = false,
    var isGamePoint: Boolean = false
)



