package com.carlosreiakvam.android.handsdowntabletennis.play_screen

import com.carlosreiakvam.android.handsdowntabletennis.score_logic.Player

class WinConditions {

    fun isGameWon(player: Player, otherPlayer: Player): Boolean {
        return player.gameScore == 11 && otherPlayer.gameScore <= 9 ||
                player.gameScore >= 11 && player.gameScore >= otherPlayer.gameScore + 2
    }

    fun matchWonByBestOf(matchPool: Map<Int, Int>, gameRules: GameRules, player: Player): Boolean {
        if (matchPool[gameRules.bestOf] == player.matchScore) {
            return true
        }
        return false
    }
}