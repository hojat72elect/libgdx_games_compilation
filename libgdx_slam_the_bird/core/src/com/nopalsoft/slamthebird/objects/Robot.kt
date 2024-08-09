package com.nopalsoft.slamthebird.objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.slamthebird.Assets
import com.nopalsoft.slamthebird.Settings

class Robot(x: Float, y: Float) {
    var SPEED_JUMP: Float = 6.25f
    var SPEED_MOVE: Float = 5f
    @JvmField
    var DURATION_SUPER_JUMP: Float = 5f
    @JvmField
    var durationSuperJump: Float = 0f
    @JvmField
    var DURATION_INVINCIBLE: Float = 5f
    @JvmField
    var durationInvincible: Float = 0f
    @JvmField
    var position: Vector2 = Vector2(x, y)

    @JvmField
    var state: Int
    @JvmField
    var stateTime: Float = 0f

    var jump: Boolean
    @JvmField
    var slam: Boolean = false
    @JvmField
    var isSuperJump: Boolean = false
    @JvmField
    var isInvincible: Boolean = false

    @JvmField
    var angleGrad: Float = 0f
    @JvmField
    var speed: Vector2

    init {
        state = STATE_JUMPING
        speed = Vector2()
        jump = true // To make the first jump

        DURATION_SUPER_JUMP += Settings.LEVEL_BOOST_SUPER_JUMP.toFloat()
        DURATION_INVINCIBLE += Settings.LEVEL_BOOST_INVINCIBLE.toFloat()
    }

    fun update(delta: Float, body: Body, accelerationX: Float, slam: Boolean) {
        this.slam = slam // To draw the fast fall =)
        position.x = body.position.x
        position.y = body.position.y
        angleGrad = 0f
        if (state == STATE_FALLING || state == STATE_JUMPING) {
            if (slam) body.gravityScale = 2.5f
            else body.gravityScale = 1f

            if (jump) {
                jump = false
                state = STATE_JUMPING
                stateTime = 0f
                if (isSuperJump) {
                    body.setLinearVelocity(
                        body.linearVelocity.x,
                        SPEED_JUMP + 3
                    )
                } else {
                    body.setLinearVelocity(
                        body.linearVelocity.x,
                        SPEED_JUMP
                    )
                }
            }

            val speed = body.linearVelocity

            if (speed.y < 0 && state != STATE_FALLING) {
                state = STATE_FALLING
                stateTime = 0f
            }
            body.setLinearVelocity(accelerationX * SPEED_MOVE, speed.y)

            if (isSuperJump) {
                durationSuperJump += delta
                if (durationSuperJump >= DURATION_SUPER_JUMP) {
                    isSuperJump = false
                    durationSuperJump = 0f
                }
            }

            if (isInvincible) {
                durationInvincible += delta
                if (durationInvincible >= DURATION_INVINCIBLE) {
                    isInvincible = false
                    durationInvincible = 0f
                }
            }
        } else if (state == STATE_DEAD) {
            body.setLinearVelocity(0f, -3f)
            body.isFixedRotation = false
            angleGrad = Math.toDegrees(body.angle.toDouble()).toFloat()
            body.angularVelocity = Math.toRadians(20.0).toFloat()
        }
        speed = body.linearVelocity
        stateTime += delta
    }

    fun updateReady(body: Body, acelX: Float) {
        position.x = body.position.x
        position.y = body.position.y

        body.setLinearVelocity(acelX * SPEED_MOVE, 0f)
        speed = body.linearVelocity
    }

    fun jump() {
        if (state == STATE_FALLING) {
            jump = true
            stateTime = 0f
            Assets.playSound(Assets.soundJump)
        }
    }

    /**
     * The robot is hit and dies.
     */
    fun hit() {
        state = STATE_DEAD
        stateTime = 0f
    }

    companion object {
        const val DURATION_DEAD_ANIMATION: Float = 2f
        @JvmField
        var RADIUS: Float = .28f
        @JvmField
        var STATE_FALLING: Int = 0
        @JvmField
        var STATE_JUMPING: Int = 1
        @JvmField
        var STATE_DEAD: Int = 2
    }
}
