package xyz.torsktechnologies.pongcounter.score_logic


class Player(var name: String) {
    var gameScore: Int = 0
    var matchScore: Int = 0

    fun increaseGameScore() {
        gameScore += 1
    }

    fun resetGameScore() {
        gameScore = 0
    }

//    fun resetMatchScore() {
//        matchScore = 0
//    }

    fun increaseMatchScore() {
        matchScore += 1
    }
}