package com.nopalsoft.invaders.frame

class Boost(val type: Int, x: Float, y: Float) : DynamicGameObject(x, y, RADIUS) {

    private var stateTime = 0

    init {
        velocity.add(0f, SPEED)
    }

    fun update(deltaTime: Float) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime)
        boundsCircle.x = position.x
        boundsCircle.y = position.y
        stateTime += deltaTime.toInt()
    }

    companion object {
        const val DRAW_SIZE = 5f
        const val RADIUS = 1f
        const val SPEED = -10f

        const val VIDA_EXTRA = 0
        const val UPGRADE_LEVEL_WEAPONS = 1
        const val MISSILE_EXTRA = 2
        const val SHIELD = 3
    }
}