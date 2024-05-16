package com.nopalsoft.sokoban

import com.badlogic.gdx.Gdx
import com.nopalsoft.sokoban.objetos.Level

object Settings {

    private val pref = Gdx.app.getPreferences("com.nopalsoft.sokoban")

    @JvmStatic
    var isTest = true

    @JvmStatic
    var animationWalkIsON = false

    @JvmStatic
    var NUM_MAPS = 62

    lateinit var arrayLevel: Array<Level?> // Each position is a level

    @JvmStatic
    fun load() {
        arrayLevel = arrayOfNulls(NUM_MAPS)

        animationWalkIsON = pref.getBoolean("animationWalkIsON", false)

        for (i in 0 until NUM_MAPS) {
            arrayLevel[i] = Level()
            arrayLevel[i]?.numStars = pref.getInteger(
                "numStars$i", 0
            )
            arrayLevel[i]?.bestMoves = pref.getInteger(
                "bestMoves$i", 0
            )
            arrayLevel[i]?.bestTime = pref.getInteger(
                "bestTime$i", 0
            )
        }
    }

    @JvmStatic
    fun save() {
        pref.putBoolean("animationWalkIsON", animationWalkIsON)
        pref.flush()
    }

    @JvmStatic
    fun levelCompeted(level: Int, moves: Int, time: Int) {
        arrayLevel[level]?.numStars = 1
        arrayLevel[level]?.bestMoves = moves
        arrayLevel[level]?.bestTime = time

        pref.putInteger("numStars$level", arrayLevel[level]!!.numStars)
        pref.putInteger("bestMoves$level", arrayLevel[level]!!.bestMoves)
        pref.putInteger("bestTime$level", arrayLevel[level]!!.bestTime)

        pref.flush()
    }
}