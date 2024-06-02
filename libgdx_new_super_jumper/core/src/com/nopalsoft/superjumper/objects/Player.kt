package com.nopalsoft.superjumper.objects

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.superjumper.screens.BasicScreen

class Player(x: Float, y: Float) {

    @JvmField
    var state = STATE_NORMAL

    @JvmField
    var durationJetPack = 0f

    @JvmField
    val position = Vector2(x, y)

    @JvmField
    var speed = Vector2()

    @JvmField
    var angleDeg = 0f

    var stateTime = 0f

    private var didJump = false

    @JvmField
    var isBubble = false

    @JvmField
    var isJetPack = false


    fun update(body: Body, delta: Float, accelerationX: Float) {
        position.x = body.position.x
        position.y = body.position.y

        speed = body.linearVelocity

        if (state == STATE_NORMAL) {
            if (didJump) {
                didJump = false
                stateTime = 0f
                speed.y = JUMP_SPEED
            }

            speed.x = accelerationX * X_SPEED

            if (isBubble) {
                durationBubble += delta
                if (durationBubble >= DURATION_BUBBLE) {
                    durationBubble = 0f
                    isBubble = false
                }
            }

            if (isJetPack) {
                durationJetPack += delta
                if (durationJetPack >= DURATION_JETPACK) {
                    durationJetPack = 0f
                    isJetPack = false
                }
                speed.y = JUMP_SPEED
            }
        } else {
            body.angularVelocity = MathUtils.degRad * 360
            speed.x = 0f
        }

        body.linearVelocity = speed

        if (position.x >= BasicScreen.WORLD_WIDTH) {
            position.x = 0f
            body.setTransform(position, 0f)
        } else if (position.x <= 0) {
            position.x = BasicScreen.WORLD_WIDTH
            body.setTransform(position, 0f)
        }

        angleDeg = body.angle * MathUtils.radDeg

        speed = body.linearVelocity
        stateTime += delta
    }


    fun jump() {
        didJump = true
    }

    fun hit() {
        if (state == STATE_NORMAL && !isBubble && !isJetPack) {
            state = STATE_DEAD
            stateTime = 0f
        }
    }


    fun die() {
        if (state == STATE_NORMAL) {
            state = STATE_DEAD
            stateTime = 0f
        }
    }


    fun setBubble() {
        if (state == STATE_NORMAL) {
            isBubble = true
            durationBubble = 0f
        }
    }


    fun setJetPack() {
        if (state == STATE_NORMAL) {
            isJetPack = true
            durationJetPack = 0f
        }
    }


    companion object {
        const val STATE_NORMAL = 0
        const val STATE_DEAD = 1

        const val DRAW_WIDTH = .75f
        const val DRAW_HEIGHT = .8f
        private const val DURATION_BUBBLE = 3f
        const val WIDTH = .4f
        const val HEIGHT = .6f

        private const val JUMP_SPEED = 7.5f
        private const val X_SPEED = 5f


        private var durationBubble = 0f

        private const val DURATION_JETPACK = 3f
    }
}