package com.nopalsoft.superjumper.objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Pool.Poolable

class Bullet : Poolable {

    var state = 0
    var stateTime = 0f
    val position = Vector2()


    override fun reset() {
        // Nothing is happening in here
    }

    fun initializeBullet(x: Float, y: Float) {
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

    companion object {
        const val STATE_NORMAL = 0
        const val STATE_DESTROY = 1
        const val SIZE = .15f
        const val XY_SPEED = 8f
    }
}