package com.carlosreiakvam.android.handsdowntabletennis.play_screen

data class GameRules(
    var bestOf: Int = 21,
    var firstServer: Int = 1,
)

data class WinStates(
    var gameWinner: Int = 1,
    var isGameWon: Boolean = false,
    var isMatchWon: Boolean = false,
    var isMatchReset: Boolean = false,
    var isGamePoint: Boolean = false,
)



