package com.nopalsoft.slamthebird.objects

import com.badlogic.gdx.math.Vector2

class Platform(x: Float, y: Float) {
    val DURATION_ACTIVE: Float =
        10f // This time should be less than TIME_TO_CHANGE_STATE_PLATFORM in the WorldGame class.
    val TIME_TO_BE_ACTIVE: Float = 1.25f
    @JvmField
    var state: Int
    @JvmField
    var position: Vector2 = Vector2(x, y)
    @JvmField
    var stateTime: Float = 0f
    @JvmField
    var changingScale: Float // For when it changes you see an animation. Start at .5 so that everything doesn't get smaller
    private var isFire = false
    private var isBreakable = false

    init {
        state = STATE_NORMAL
        changingScale = .5f
    }

    fun update(delta: Float) {
        stateTime += delta

        if (state == STATE_CHANGING) {
            changingScale = stateTime / TIME_TO_BE_ACTIVE // 1.2 maximum scale.

            if (stateTime >= TIME_TO_BE_ACTIVE) {
                if (isFire) state = STATE_FIRE
                else if (isBreakable) state = STATE_BREAKABLE
                stateTime = 0f
            }
        }

        if ((state == STATE_FIRE || state == STATE_BREAKABLE || state == STATE_BROKEN) && stateTime >= DURATION_ACTIVE) {
            isFire = false
            isBreakable = isFire
            state = STATE_NORMAL
            stateTime = 0f
            changingScale = .5f
        }
    }

    fun setFire() {
        state = STATE_CHANGING
        isFire = true
        stateTime = 0f
    }

    fun setBreakable() {
        state = STATE_CHANGING
        isBreakable = true
        stateTime = 0f
    }

    fun setBroken() {
        state = STATE_BROKEN
        stateTime = 0f
    }

    companion object {
        const val STATE_NORMAL: Int = 0
        const val STATE_CHANGING: Int = 1
        const val STATE_FIRE: Int = 2
        const val STATE_BREAKABLE: Int = 3
        const val STATE_BROKEN: Int = 4
        @JvmField
        var WIDTH: Float = .75f
        @JvmField
        var HEIGHT: Float = .2f
    }
}
