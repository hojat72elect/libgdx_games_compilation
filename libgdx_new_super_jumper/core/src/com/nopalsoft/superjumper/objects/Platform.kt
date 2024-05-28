package com.nopalsoft.superjumper.objects

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool.Poolable

class Platform : Poolable {

    @JvmField
    var state = 0

    @JvmField
    var type = 0

    @JvmField
    var color = 0

    @JvmField
    val position = Vector2()
    var stateTime = 0f


    fun initializePlatform(x: Float, y: Float, type: Int) {
        position.set(x, y)
        this.type = type

        color = if (type == TYPE_NORMAL) {
            MathUtils.random(11)
        } else {
            MathUtils.random(5)
        }
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
        // Nothing is happening in here.
    }

    companion object {

        const val STATE_NORMAL = 0
        const val STATE_DESTROY = 1

        const val TYPE_NORMAL = 0
        const val TYPE_BREAKABLE = 1

        const val DRAW_WIDTH_NORMAL = 1.25f
        const val DRAW_HEIGHT_NORMAL = .45f
        const val WIDTH_NORMAL = 1.25f
        const val HEIGHT_NORMAL = .45f

        const val COLOR_BEIGE = 0
        const val COLOR_BLUE = 1
        const val COLOR_GRAY = 2
        const val COLOR_GREEN = 3
        const val COLOR_MULTICOLOR = 4
        const val COLOR_PINK = 5
        const val COLOR_BEIGE_LIGHT = 6
        const val COLOR_BLUE_LIGHT = 7
        const val COLOR_GRAY_LIGHT = 8
        const val COLOR_GREEN_LIGHT = 9
        const val COLOR_MULTICOLOR_LIGHT = 10
        const val COLOR_PINK_LIGHT = 11
    }
}