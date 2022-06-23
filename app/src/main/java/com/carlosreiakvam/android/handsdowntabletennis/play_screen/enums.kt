package com.carlosreiakvam.android.handsdowntabletennis.play_screen

enum class Scores(val index: Int, val str: String) {
    P1GAMESCORE(0, "P1GAMESCORE"),
    P2GAMESCORE(1, "P2GAMESCORE"),
    P1MATCHSCORE(2, "P1MATCHSCORE"),
    P2MATCHSCORE(3, "P2MATCHSCORE"),
    GAMENUMBER(4, "GAMENUMBER"),
    P1CURRENTSERVER(5, "P1CURRENTSERVER"),
    P2CURRENTSERVER(6, "P2CURRENTSERVER"),
}

enum class States(val index: Int) {
    ISGAMEWON(7),
    WONBYBESTOF(8),
    ISMATCHWON(9),
    ISMATCHRESET(10),
    FIRSTPLAYERSERVER(11),
    BESTOF(12)
}

enum class Defaults(val int: Int) {
    BESTOFDEFAULT(3);
}

enum class Players(val playerNumber: Int) {
    PLAYER1(1),
    PLAYER2(2)
}

