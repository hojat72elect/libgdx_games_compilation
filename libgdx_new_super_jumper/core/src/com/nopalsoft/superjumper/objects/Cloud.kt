package com.nopalsoft.superjumper.objects

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Pool.Poolable
import com.nopalsoft.superjumper.screens.Screens

/**
 * The clouds are indestructible. They all start out happy until you shoot them.
 */
class Cloud : Poolable {

    @JvmField
    var state = 0

    @JvmField
    var guy = 0
    private var timeToBlow = 0f
    private var durationBlow = 0f
    private var timeForLightning = 0f

    @JvmField
    val position = Vector2()
    private var speed = Vector2()

    @JvmField
    var isBlowing = false

    @JvmField
    var isLightning = false

    var stateTime = 0f


    fun initializeCloud(x: Float, y: Float) {
        position.set(x, y)
        speed.set(0f, 0f) // I set the speed from the method where I create it.
        stateTime = 0f
        state = STATE_NORMAL
        guy = TYPE_HAPPY

        isLightning = false
        isBlowing = false

        durationBlow = 0f
        timeToBlow = 0f
        timeForLightning = MathUtils.random(TIME_FOR_LIGHTNING)
    }


    fun update(body: Body, delta: Float) {
        position.x = body.position.x
        position.y = body.position.y

        speed = body.linearVelocity

        if (position.x >= Screens.WORLD_WIDTH || position.x <= 0) {
            speed.x *= -1
        }

        body.linearVelocity = speed
        speed = body.linearVelocity

        if (guy == TYPE_ANGRY) {
            timeToBlow += delta
            if (!isBlowing && timeToBlow >= TIME_TO_BLOW) {
                if (MathUtils.randomBoolean())
                    isBlowing = true
                timeToBlow = 0f
            }

            if (isBlowing) {
                durationBlow += delta
                if (durationBlow >= DURATION_BLOW) {
                    durationBlow = 0f
                    isBlowing = false
                }
            }
        } else { // Happy Type

            if (!isLightning) {
                timeForLightning += delta
                if (timeForLightning >= TIME_FOR_LIGHTNING) {
                    isLightning = true
                }
            }
        }

        stateTime += delta
    }


    fun fireLighting() {
        isLightning = false
        timeForLightning = MathUtils.random(TIME_FOR_LIGHTNING)
    }


    fun hit() {
        if (guy == TYPE_HAPPY) {
            guy = TYPE_ANGRY
            durationBlow = 0f
            timeToBlow = 0f
            stateTime = 0f
        }
    }


    fun destroy() {
        if (state == STATE_NORMAL) {
            state = STATE_DEAD
            stateTime = 0f
        }
    }


    override fun reset() {
        // Nothing is happening in here
    }


    companion object {
        const val STATE_NORMAL = 0
        const val STATE_DEAD = 1

        const val DRAW_WIDTH = .95f
        const val DRAW_HEIGHT = .6f

        const val WIDTH = .65f
        const val HEIGHT = .4f

        const val SPEED_X = .5f

        const val TYPE_HAPPY = 0
        const val TYPE_ANGRY = 1

        const val TIME_TO_BLOW = 2f

        const val DURATION_BLOW = 3f

        const val TIME_FOR_LIGHTNING = 5f
    }
}