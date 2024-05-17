package com.nopalsoft.sokoban.objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.nopalsoft.sokoban.Settings.animationWalkIsON
import com.nopalsoft.sokoban.game.Board

/**
 * All objects are useful (the floor, the character, the boxes).
 */
open class Tiles(@JvmField var position: Int) : Actor() {
    // ALL MAPS ARE 25x15 Tiles of 32px which gives a resolution of 800x480
    @JvmField
    val SIZE: Float = 32 * Board.UNIT_SCALE // Token size

    init {
        setSize(SIZE, SIZE)
        setPosition(
            mapPositions[position]!!.x, mapPositions[position]!!.y
        )
    }

    /**
     * If it is UNDO it moves without animation (quickFix).
     */
    fun moveToPosition(pos: Int, undo: Boolean) {
        var time = .05f
        if (animationWalkIsON && !undo) time = .45f
        this.position = pos
        addAction(
            Actions.sequence(
                Actions.moveTo(
                    mapPositions[position]!!.x, mapPositions[position]!!.y, time
                ), Actions.run { this.endMovingToPosition() })
        )
    }

    /**
     * It is called automatically when it has already been moved to the position.
     */
    protected open fun endMovingToPosition() {
    }

    companion object {
        val mapPositions: LinkedHashMap<Int, Vector2> = LinkedHashMap()

        init {
            // Positions start from left to right, bottom to top.
            var posicionTile = 0
            for (y in 0..14) {
                for (x in 0..24) {
                    mapPositions[posicionTile] = Vector2(
                        x * 32 * Board.UNIT_SCALE,
                        y * 32 * Board.UNIT_SCALE
                    )
                    posicionTile++
                }
            }
        }
    }
}
