package com.nopalsoft.invaders.frame

import kotlin.math.abs

class AlienShip(life: Int, increaseSpeed: Float, x: Float, y: Float) : DynamicGameObject(x, y, RADIUS) {


    var livesLeft: Int
    var punctuation: Int
    var stateTime: Float = 0f
    var state: Int
    private var movedDistance: Float
    private var increaseSpeed: Float

    init {
        state = MOVE_SIDES
        velocity[SPEED] = SPEED_DOWN
        movedDistance = 0f
        punctuation = SIMPLE_SCORE
        livesLeft = life
        this.increaseSpeed = 1 + increaseSpeed
    }

    fun update(deltaTime: Float) {
        if (state != EXPLODING) {
            when (state) {
                MOVE_SIDES -> {
                    position.x += velocity.x * deltaTime * increaseSpeed
                    movedDistance += abs((velocity.x * deltaTime)) * increaseSpeed
                    if (movedDistance > MOVE_RANGE_SIDES) {
                        state = MOVE_DOWN
                        velocity.x *= -1f
                        movedDistance = 0f
                    }
                }

                MOVE_DOWN -> {
                    position.y += velocity.y * deltaTime * increaseSpeed
                    movedDistance += abs((velocity.x * deltaTime)) * increaseSpeed
                    if (movedDistance > MOVE_RANGE_DOWN) {
                        state = MOVE_SIDES
                        movedDistance = 0f
                    }
                }
            }
        }

        boundsCircle!!.x = position.x
        boundsCircle!!.y = position.y
        stateTime += deltaTime
    }

    fun beingHit() {
        livesLeft--
        if (livesLeft <= 0) {
            state = EXPLODING
            velocity.add(0f, 0f)
            stateTime = 0f
        }
    }


    companion object {
        const val RADIUS: Float = 1.5f

        const val DRAW_WIDTH: Float = 3.5f
        const val DRAW_HEIGHT: Float = 3.5f

        const val MOVE_SIDES: Int = 0
        const val MOVE_DOWN: Int = 2
        const val EXPLODING: Int = 3
        const val SPEED: Float = 4f
        const val SPEED_DOWN: Float = -3.5f
        private const val SIMPLE_SCORE: Int = 10
        const val MOVE_RANGE_SIDES: Float = 6.7f
        const val MOVE_RANGE_DOWN: Float = 1.2f
        const val EXPLODE_TIME: Float = 0.05f * 19
    }
}
