package com.nopalsoft.ninjarunner.objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool.Poolable

class Platform: Poolable {

    @JvmField
    val position = Vector2()
    @JvmField
    var state = 0
    var stateTime = 0f


    fun initializePlatform(x: Float, y: Float) {
        position.set(x, y)
        state = STATE_NORMAL
        stateTime = 0f
    }

    fun update(delta: Float) {
        stateTime += delta
    }

    fun setDestroy() {
        if (state == STATE_NORMAL) {
            state = STATE_DESTROY
            stateTime = 0f
        }
    }


    override fun reset() {
       // nothing is going on here
    }

    companion object{
        const val STATE_NORMAL = 0
        const val STATE_DESTROY = 1
        const val HEIGHT = .50f
        const val WIDTH = 1.64f
    }
}