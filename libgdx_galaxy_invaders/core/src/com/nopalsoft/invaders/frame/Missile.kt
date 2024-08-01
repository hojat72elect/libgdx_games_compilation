package com.nopalsoft.invaders.frame

import com.badlogic.gdx.math.Circle


/**
 * X and Y are the position of the tip of the ship.
 *
 * @param x The same as Bob.x
 * @param y The same as Bob.y
 */
class Missile(x: Float, y: Float) : DynamicGameObject(x, y, WIDTH, HEIGHT) {

    private val speed: Float = 30f

    var stateTime: Float
    var state: Int

    init {
        // I also initialize the radius because the explosion will be nicer.
        boundsCircle = Circle(position.x, position.y, RADIO_EXPLOSION)
        state = STATE_TRIGGERED
        stateTime = 0f
        velocity[0f] = speed
    }

    fun update(deltaTime: Float) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime)
        boundsRectangle!!.x = position.x - WIDTH / 2
        boundsRectangle.y = position.y - HEIGHT / 2
        boundsCircle!!.x = position.x
        boundsCircle!!.y = position.y
        stateTime += deltaTime
    }

    fun hitTarget() {
        velocity[0f] = 0f
        stateTime = 0f
        state = STATE_EXPLODING
    }

    companion object {
        const val WIDTH: Float = 0.4f
        const val HEIGHT: Float = 1.4f

        const val RADIO_EXPLOSION = 7.5f
        const val EXPLODE_TIME = 0.05f * 19
        const val STATE_TRIGGERED = 0
        const val STATE_EXPLODING = 1
    }
}
