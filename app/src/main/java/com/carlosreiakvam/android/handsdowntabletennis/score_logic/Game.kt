package com.carlosreiakvam.android.handsdowntabletennis.score_logic

class Game() {

    var player1 = Player("player one", 1)
    var player2 = Player("player two", 1)
    var currentPlayerServer = 1
    var gameNumber = 1
    var isGameStart = false
    var isMatchStart = false

    fun newGame() {
        player1.resetGameScore()
        player2.resetGameScore()
    }

    /**
     * @param firstServerPlayer must be 1 or 2
     */
    fun newMatch(firstServerPlayer: Int) {
        when (firstServerPlayer) {
            1 -> player1.isFirstServer = true
            2 -> player2.isFirstServer = true
        }
        player1.resetGameScore()
        player1.resetMatchScore()
        player2.resetGameScore()
        player2.resetMatchScore()
    }

    /**
     * @return player number of player that won, or -1 if no winner.
     */
    fun isGameWon(player: Int): Boolean {
        return when (player) {
            1 -> player1.gameScore == 11 && player2.gameScore <= 9 ||
                    player1.gameScore >= 11 && player1.gameScore >= player2.gameScore + 2
            2 -> player2.gameScore == 11 && player1.gameScore <= 9 ||
                    player2.gameScore >= 11 && player2.gameScore >= player1.gameScore + 2
            else -> false
        }
    }

    fun switchServeIfNecessary() {
        if (player1.gameScore >= 10 && player2.gameScore >= 10 ||
            (player1.gameScore.plus(player2.gameScore) % 2 == 0)
        ) {
            switchPlayerServer()
        }
    }

    private fun switchPlayerServer() {
        if (currentPlayerServer == 1) currentPlayerServer = 2
        else if (currentPlayerServer == 2) currentPlayerServer = 1
    }

}