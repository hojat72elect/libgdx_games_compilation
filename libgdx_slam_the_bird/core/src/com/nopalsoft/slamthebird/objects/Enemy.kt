package com.nopalsoft.slamthebird.objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.slamthebird.Settings
import java.util.Random

class Enemy(x: Float, y: Float) {
    var TIME_JUST_APPEAR: Float = 1.7f
    var TIME_TO_CHANGE_VEL: Float = 3f
    var timeToChangeVel: Float = 0f

    var TIME_TO_EVOLVE: Float = 3f
    var timeToEvolve: Float = 0f

    var DURARTION_EVOLVING: Float = 1.5f

    var DURARTION_FROZEN: Float = 5f
    @JvmField
    var position: Vector2 = Vector2(x, y)
    @JvmField
    var speed: Vector2
    var isFrozen: Boolean
    @JvmField
    var state: Int
    @JvmField
    var stateTime: Float
    @JvmField
    var lives: Int
    @JvmField
    var appearScale: Float = 0f
    var durationFrozen: Float

    init {
        state = STATE_JUST_APPEAR
        lives = 2
        stateTime = 0f
        speed = Vector2()
        isFrozen = false
        durationFrozen = 0f
        DURARTION_FROZEN += Settings.LEVEL_BOOST_ICE.toFloat()
    }

    fun update(delta: Float, body: Body, random: Random) {
        position.x = body.position.x
        position.y = body.position.y

        if (isFrozen) {
            body.setLinearVelocity(0f, 0f)
            if (durationFrozen >= DURARTION_FROZEN) {
                isFrozen = false
                durationFrozen = 0f
                setNewVelocity(body, random, false)
            }
            durationFrozen += delta
            return  // Nothing more is done if it is frozen. It doesn't move, it doesn't change speed, it doesn't evolve, it doesn't swim.
        }

        // Whatever happens, I don't want it to be above 10f.
        if (position.y > 10f) {
            speed = body.linearVelocity
            body.setLinearVelocity(speed.x, speed.y * -1)
        }
        if (state == STATE_JUST_APPEAR) {
            appearScale = stateTime * 1.5f / TIME_JUST_APPEAR // 1.5f maximum scale

            if (stateTime >= TIME_JUST_APPEAR) {
                state = STATE_FLYING
                stateTime = 0f
                setNewVelocity(body, random, false)
            }
        }

        if (state != STATE_JUST_APPEAR) {
            timeToChangeVel += delta
            if (timeToChangeVel >= TIME_TO_CHANGE_VEL) {
                timeToChangeVel -= TIME_TO_CHANGE_VEL

                val vel = body.linearVelocity

                // Change in X
                if (random.nextBoolean()) vel.x *= -1f

                if (state == STATE_FLYING) {
                    if (random.nextBoolean()) vel.y *= -1f
                }
                body.linearVelocity = vel
            }
        }

        if (state == STATE_HIT) {
            body.gravityScale = 1f
            timeToEvolve += delta
            if (timeToEvolve >= TIME_TO_EVOLVE) {
                state = STATE_EVOLVING
                stateTime = 0f
                timeToEvolve = 0f
            }
        }

        if (state == STATE_EVOLVING && stateTime >= DURARTION_EVOLVING) {
            state = STATE_FLYING
            body.gravityScale = 0f
            setNewVelocity(body, random, true)
            lives = 3
            stateTime = 0f
        }

        speed = body.linearVelocity

        limitSpeed(body)
        speed = body.linearVelocity

        stateTime += delta
    }

    /*
     * Limit the speed because sometimes the force resulting from the collision drove the enemy crazy.
     */
    private fun limitSpeed(body: Body) {
        var vel = MAX_SPEED_BLUE
        if (lives == 3) vel = MAX_SPEED_RED

        if (speed.x > vel) {
            speed.x = vel
        } else if (speed.x < -vel) {
            speed.x = -vel
        }

        if (lives > 1) { // So the bird fell quickly if I took off its wings.
            if (speed.y > vel) {
                speed.y = vel
            } else if (speed.y < -vel) {
                speed.y = -vel
            }
        }
        body.linearVelocity = speed
    }

    /**
     * If it is touching the floor I make the Y velocity always generated positive.
     */
    private fun setNewVelocity(body: Body, random: Random, isTouchingFLoor: Boolean) {
        var vel = MAX_SPEED_BLUE
        if (lives == 3) vel = MAX_SPEED_RED

        val velocityX = random.nextFloat() * vel * 2 - vel
        val velocityY = if (isTouchingFLoor) random.nextFloat() * vel
        else random.nextFloat() * vel * 2 - vel

        body.setLinearVelocity(velocityX, velocityY)
    }

    fun hit() {
        lives--
        if (lives == 1) state = STATE_HIT
        else if (lives == 0) state = STATE_DEAD

        stateTime = 0f
    }

    fun die() {
        lives = 0
        state = STATE_DEAD
        stateTime = 0f
    }

    fun setFrozen() {
        durationFrozen = 0f
        isFrozen = true
    }

    companion object {
        @JvmField
        var WIDTH: Float = .4f
        @JvmField
        var HEIGHT: Float = .4f

        @JvmField
        var STATE_JUST_APPEAR: Int = 0
        @JvmField
        var STATE_FLYING: Int = 1
        var STATE_HIT: Int = 2
        @JvmField
        var STATE_EVOLVING: Int = 3 // For it to fly again
        @JvmField
        var STATE_DEAD: Int = 4
        var MAX_SPEED_BLUE: Float = 1.75f
        var MAX_SPEED_RED: Float = 3.25f
    }
}
