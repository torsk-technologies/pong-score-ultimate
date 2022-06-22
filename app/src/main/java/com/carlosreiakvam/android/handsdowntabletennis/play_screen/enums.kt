package com.carlosreiakvam.android.handsdowntabletennis.play_screen

enum class Scores(val index: Int, val str: String) {
    P1GAMESCORE(0, "P1GAMESCORE"),
    P2GAMESCORE(1, "P2GAMESCORE"),
    P1MATCHSCORE(2, "P1MATCHSCORE"),
    P2MATCHSCORE(3, "P2MATCHSCORE"),
    GAMENUMBER(4, "GAMENUMBER"),
    CURRENTPLAYERSERVER(5, "CURRENTPLAYERSERVER"),
}

enum class States(val index: Int) {
    ISGAMESTART(6),
    ISMATCHSTART(7),
    FIRSTPLAYERSERVER(8),
    BESTOF(9)
}

enum class Defaults(val int: Int) {
    BESTOFDEFAULT(3);
}

