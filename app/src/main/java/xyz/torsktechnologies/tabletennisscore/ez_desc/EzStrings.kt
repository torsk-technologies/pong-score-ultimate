package xyz.torsktechnologies.tabletennisscore.ez_desc

class EzStrings {
    val ezServe: String = """
    Serve
    A game of table tenis starts with a serve.
    A serve is where a player first sets the ball into play.
    The serving player has to hit the ball so that it hits the table on his/her side of the net first and then the opponents side of the table. 
""".trimIndent()

    val ezReturn: String = """
    Return
    A return is a return of a ball that is in play.
    The returning player has to hit the ball over the net,  and bounce on the opponents side of the table before the opponent can strike back. 
    """.trimIndent()
    val ezPoint: String = """
    Point
    You score a point when your opponent is unable to make a valid return.
     """.trimIndent()

    val ezTurns: String = """
    Turns
    Players alternates between serving. Each player gets two serves before alternating, unless both players have at least 10 points, in which players get one serve before alternating.
    """.trimIndent()

    val ezGame: String = """
    Game
    A game is won when a player first gets 11 points unless both players have at least 10 points, in which the winner has to win by two points.
    """.trimIndent()

    val ezMatch: String = """
        Match
        A match is won by winning an odd number of games. If for instance the match is set to best of 5, the winner is the player that first gets 3 points.
    """.trimIndent()


}