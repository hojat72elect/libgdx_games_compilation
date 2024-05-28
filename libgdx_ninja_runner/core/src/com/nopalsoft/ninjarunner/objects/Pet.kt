package com.nopalsoft.ninjarunner.objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

/**
 * The pet that the user will have and follows them through the game.
 */
class Pet(x: Float, y: Float, @JvmField val petType: PetType) {

    @JvmField
    val position = Vector2(x, y)
    private val targetPosition = Vector2(x, y)
    private var velocity = Vector2()

    @JvmField
    var stateTime = 0f

    @JvmField
    var drawWidth = 0f

    @JvmField
    var drawHeight = 0f

    @JvmField
    var dashDrawWidth = 0f

    @JvmField
    var dashDrawHeight = 0f

    init {
        when (petType) {
            PetType.PINK_BIRD -> {
                drawWidth = .73f
                drawHeight = .66f
                dashDrawWidth = 2.36f
                dashDrawHeight = 1.25f
            }

            PetType.BOMB -> {
                dashDrawWidth = .52f
                drawWidth = dashDrawWidth
                dashDrawHeight = .64f
                drawHeight = dashDrawHeight
            }

        }
    }

    fun update(body: Body, delta: Float, targetX: Float, targetY: Float) {
        position.x = body.position.x
        position.y = body.position.y

        targetPosition.set(targetX, targetY)

        velocity = body.linearVelocity
        velocity.set(targetPosition).sub(position).scl(SPEED)
        body.linearVelocity = velocity
        stateTime += delta
    }

    fun updateStateTime(delta: Float) {
        stateTime += delta
    }


    companion object {

        const val SPEED = 5f
        const val RADIUS = .25f
    }

    enum class PetType {
        PINK_BIRD, BOMB
    }
}