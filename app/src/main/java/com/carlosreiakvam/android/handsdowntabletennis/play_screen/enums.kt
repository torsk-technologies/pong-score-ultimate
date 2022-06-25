package com.carlosreiakvam.android.handsdowntabletennis.play_screen

enum class Scores() {
    P1GAMESCORE,
    P2GAMESCORE,
    P1MATCHSCORE,
    P2MATCHSCORE,
    GAMENUMBER,
    P1CURRENTSERVER,
    P2CURRENTSERVER,
}


enum class Defaults(val int: Int) {
    BESTOFDEFAULT(3);
}

enum class Players(val playerNumber: Int) {
    PLAYER1(1),
    PLAYER2(2)
}

