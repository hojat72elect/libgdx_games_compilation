package com.nopalsoft.superjumper.objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Pool.Poolable

/**
 * The lightning that gets casted from cloud, downwards. If player gets hit by that, it will be killed.
 */
class Lightning : Poolable {

    @JvmField
    val position = Vector2()

    @JvmField
    var stateTime = 0f

    @JvmField
    var state = 0

    fun initializeLightning(x: Float, y: Float) {
        position.set(x, y)
        stateTime = 0f
        state = STATE_NORMAL
    }

    fun update(body: Body, delta: Float) {
        position.x = body.position.x
        position.y = body.position.y
        stateTime += delta
    }

    fun destroy() {
        if (state == STATE_NORMAL) {
            state = STATE_DESTROY
            stateTime = 0f
        }
    }


    override fun reset() {
        // this was defined as a poolable, but this function
        // has not been implemented.
    }


    companion object {
        const val STATE_NORMAL = 0
        const val STATE_DESTROY = 1
        const val DRAW_WIDTH = .25f
        const val DRAW_HEIGHT = .98f
        const val WIDTH = .21f
        const val HEIGHT = .93f
        const val Y_SPEED = -3f
    }
}