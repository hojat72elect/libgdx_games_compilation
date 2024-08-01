package com.nopalsoft.invaders.frame

import com.badlogic.gdx.Gdx
import com.nopalsoft.invaders.game.World

class Ship(x: Float, y: Float) : DynamicGameObject(x, y, WIDTH, HEIGHT) {

    var livesShield: Int = 1
    var lives: Int = 3
    var state: Int
    var stateTime: Float = 0f

    init {
        // You start with 1 shield in case the bastards hit you.
        state = NAVE_STATE_NORMAL
        Gdx.app.log("Estado", "Se creo la nave")
    }

    fun update(deltaTime: Float) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime)
        boundsRectangle!!.x = position.x - boundsRectangle.width / 2
        boundsRectangle.y = position.y - boundsRectangle.height / 2

        if (state == NAVE_STATE_BEING_HIT && stateTime > HIT_TIME) {
            state = NAVE_STATE_NORMAL
            stateTime = 0f
            Gdx.app.log("Estado", "Se cambio a normal")
        }

        if (position.x < WIDTH / 2) position.x = WIDTH / 2
        if (position.x > World.WIDTH - WIDTH / 2) position.x = World.WIDTH - WIDTH / 2
        stateTime += deltaTime
    }

    fun beingHit() {
        if (livesShield > 0) {
            livesShield--
        } else {
            lives--
            if (lives <= 0) {
                state = NAVE_STATE_EXPLODE
                stateTime = 0f
                velocity[0f] = 0f
            } else {
                state = NAVE_STATE_BEING_HIT
                stateTime = 0f
            }
        }
    }

    fun hitExtraLife() {
        if (lives < 99) {
            lives++
        }
    }

    fun hitShield() {
        stateTime = 0f
        livesShield = 3
    }

    companion object {
        const val DRAW_WIDTH: Float = 4.5f
        const val DRAW_HEIGHT: Float = 3.6f

        const val WIDTH: Float = 4f
        const val HEIGHT: Float = 2.5f

        const val NAVE_MOVE_SPEED: Float = 50f

        const val NAVE_STATE_NORMAL: Int = 0
        const val NAVE_STATE_EXPLODE: Int = 1
        const val NAVE_STATE_BEING_HIT: Int = 2

        const val EXPLODE_TIME: Float = 0.05f * 19
        const val HIT_TIME: Float =
            0.05f * 21 // one more so that I have a little time to think haha.
    }
}
