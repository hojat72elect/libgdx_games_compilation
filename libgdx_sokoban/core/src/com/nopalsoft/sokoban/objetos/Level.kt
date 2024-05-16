package com.nopalsoft.sokoban.objetos

data class Level(
    @JvmField
    var numStars: Int = 0,
    @JvmField
    var bestTime: Int = 0,
    @JvmField
    var bestMoves: Int = 0,
)
