package com.nopalsoft.sokoban.objects

/**
 * Represents each level of the game that we have played.
 * @param numStars Maximum number of stars that user has gained in this level.
 * @param bestTime The least amount of seconds that user has taken for completing this level.
 * @param bestMoves The least number of moves user has so far taken for solving this level.
 */
data class Level(
    var numStars: Int = 0,
    var bestTime: Int = 0,
    var bestMoves: Int = 0,
)
