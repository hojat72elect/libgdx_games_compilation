package com.nopalsoft.superjumper.objects

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool.Poolable

class Item : Poolable {

    val position = Vector2()
    var state = 0
    var type = 0
    var stateTime = 0f


    fun initializeItem(x: Float, y: Float) {
        position.set(x, y)
        state = STATE_NORMAL
        stateTime = 0f

        type = MathUtils.random(2)
    }


    fun update(delta: Float) {
        stateTime += delta
    }

    fun take() {
        state = STATE_TAKEN
        stateTime = 0f
    }


    override fun reset() {
        // Nothing is happening in here.
    }

    companion object {
        const val STATE_NORMAL = 0
        const val STATE_TAKEN = 1
        const val DRAW_WIDTH = .27f
        const val DRAW_HEIGHT = .34f
        const val WIDTH = .25f
        const val HEIGHT = .32f

        const val TYPE_JETPACK = 0
        const val TYPE_BUBBLE = 1
        const val TYPE_GUN = 2
    }
}