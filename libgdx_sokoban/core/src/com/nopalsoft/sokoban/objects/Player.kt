package com.nopalsoft.sokoban.objects

import com.badlogic.gdx.graphics.g2d.Batch
import com.nopalsoft.sokoban.Assets
import com.nopalsoft.sokoban.Settings

class Player(position: Int) : Tiles(position) {

    private var state = STATE_STAND
    private var stateTime = 0f

    override fun act(delta: Float) {
        super.act(delta)
        stateTime += delta
    }

    fun moveToPosition(pos: Int, up: Boolean, down: Boolean, right: Boolean, left: Boolean) {
        super.moveToPosition(pos, false)

        if (up) {
            state = STATE_UP
        } else if (down) {
            state = STATE_DOWN
        } else if (right) {
            state = STATE_RIGHT
        } else if (left) {
            state = STATE_LEFT
        }
        stateTime = 0f
    }


    override fun draw(batch: Batch, parentAlpha: Float) {
        val keyFrame = if (Settings.animationWalkIsON) {
            when (state) {
                STATE_DOWN -> {
                    Assets.characterGoingDown.getKeyFrame(stateTime, true)
                }

                STATE_UP -> {
                    Assets.characterGoingUp.getKeyFrame(stateTime, true)
                }

                STATE_LEFT -> {
                    Assets.characterGoingLeft.getKeyFrame(stateTime, true)
                }

                STATE_RIGHT -> {
                    Assets.characterGoingRight.getKeyFrame(stateTime, true)
                }

                else -> {
                    Assets.characterStand
                }
            }
        } else {
            Assets.characterStand
        }

        batch.draw(keyFrame, x, y, SIZE, SIZE)
    }

    override fun endMovingToPosition() {
        state = STATE_STAND
        stateTime = 0f
    }

    fun canMove() = state == STATE_STAND


    companion object {
        const val STATE_LEFT = 0
        const val STATE_UP = 1
        const val STATE_DOWN = 2
        const val STATE_RIGHT = 3
        const val STATE_STAND = 4
    }
}