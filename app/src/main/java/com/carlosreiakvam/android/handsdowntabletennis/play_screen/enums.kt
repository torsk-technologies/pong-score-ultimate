package com.carlosreiakvam.android.handsdowntabletennis.play_screen

enum class Scores(val index: Int, val str: String) {
    P1GAMESCORE(0, "P1GAMESCORE"),
    P2GAMESCORE(1, "P2GAMESCORE"),
    P1MATCHSCORE(2, "P1MATCHSCORE"),
    P2MATCHSCORE(3, "P2MATCHSCORE"),
    GAMENUMBER(4, "GAMENUMBER"),
}

enum class States(val index: Int) {
    ISGAMESTART(5),
    ISMATCHSTART(6),
    CURRENTPLAYERSERVER(7),
}

