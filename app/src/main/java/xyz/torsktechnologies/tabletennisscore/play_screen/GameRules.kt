package xyz.torsktechnologies.tabletennisscore.play_screen

data class GameRules(
    var bestOf: Int = InitialValues.BESTOF.i,
    var firstServer: Int = 1,
)

data class WinStates(
    var gameWinner: Int = 1,
    var isGameWon: Boolean = false,
    var isMatchWon: Boolean = false,
    var isMatchReset: Boolean = false,
    var isGamePoint: Boolean = false,
    var isMatchPoint: Boolean = false,
)



