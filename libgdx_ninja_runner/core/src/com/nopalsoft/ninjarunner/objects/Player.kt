package com.nopalsoft.ninjarunner.objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.nopalsoft.ninjarunner.Assets
import com.nopalsoft.ninjarunner.Settings

class Player(x: Float, y: Float, @JvmField val type: Int) {

    private val speedSecondJump = 4f
    private val maxLives = Settings.LEVEL_LIFE + 5


    private val initialPosition = Vector2(x, y)

    @JvmField
    var state = STATE_NORMAL

    @JvmField
    val position = Vector2(x, y)

    @JvmField
    var stateTime = 0f

    @JvmField
    var isJumping = false // To know if I can draw the jumping animation.
    private var numberOfFloorsInContact =
        0 // Floors you are currently touching if ==0 you cannot jump.
    private var didGetHurtAtLeastOnce = false


    @JvmField
    var lives = maxLives

    @JvmField
    var isDash = false

    @JvmField
    var isSlide = false

    @JvmField
    var isIdle = true

    var isMagnetEnabled = false
    private var durationMagnet = 0f
    private var durationDash = 0f
    private var canJump = true
    private var canDoubleJump = true


    fun update(
        delta: Float,
        body: Body,
        didJump: Boolean,
        isJumpPressed: Boolean,
        dash: Boolean,
        didSlide: Boolean
    ) {
        position.x = body.position.x
        position.y = body.position.y

        isIdle = false

        // It doesn't matter if he's alive/dizzy/or whatever, it takes away this time.
        if (isMagnetEnabled) {
            durationMagnet += delta
            if (durationMagnet >= DURATION_MAGNET) {
                durationMagnet = 0f
                isMagnetEnabled = false
            }
        }

        if (state == STATE_REVIVE) {
            state = STATE_NORMAL
            canJump = true
            isJumping = false
            canDoubleJump = true
            stateTime = 0f
            lives = maxLives
            initialPosition.y = 3f
            position.x = initialPosition.x
            position.y = initialPosition.y
            body.setTransform(initialPosition, 0f)
            body.setLinearVelocity(0f, 0f)
        } else if (state == STATE_HURT) {
            stateTime += delta
            if (stateTime >= DURATION_HURT) {
                state = STATE_NORMAL
                stateTime = 0f
            }
        } else if (state == STATE_DIZZY) {
            stateTime += delta
            body.setLinearVelocity(0f, body.linearVelocity.y)
            if (stateTime >= DURATION_DIZZY) {
                state = STATE_NORMAL
                stateTime = 0f
            }
            return
        } else if (state == STATE_DEAD) {
            stateTime += delta
            body.setLinearVelocity(0f, body.linearVelocity.y)
            return
        }

        val velocity = body.linearVelocity

        if (didJump && (canJump || canDoubleJump)) {
            velocity.y = JUMP_SPEED

            if (!canJump) {
                canDoubleJump = false
                velocity.y = speedSecondJump
            }

            canJump = false
            isJumping = true
            stateTime = 0f

            isSlide = false

            body.gravityScale = .9f
            Assets.playSound(Assets.jump, 1) // FIXME Fix the sound
        }
        if (!isJumpPressed) body.gravityScale = 1f

        if (!isJumping) {
            isSlide = didSlide
        }

        // DASH
        if (dash) {
            isDash = true
            durationDash = 0f
        }

        if (isDash) {
            durationDash += delta
            velocity.x = VELOCITY_DASH
            if (durationDash >= DURATION_DASH) {
                isDash = false
                stateTime = 0f
                velocity.x = VELOCITY_RUN
            }
        } else {
            velocity.x = VELOCITY_RUN
        }
        stateTime += delta

        body.linearVelocity = velocity
    }


    fun getHurt() {
        if (state != STATE_NORMAL) return

        lives--
        state = if (lives > 0) {
            STATE_HURT
        } else {
            STATE_DEAD
        }
        stateTime = 0f
        didGetHurtAtLeastOnce = true
    }


    fun getDizzy() {
        if (state != STATE_NORMAL) return

        lives--
        state = if (lives > 0) {
            STATE_DIZZY
        } else {
            STATE_DEAD
        }
        stateTime = 0f
        didGetHurtAtLeastOnce = true
    }


    fun die() {
        if (state != STATE_DEAD) {
            lives = 0

            state = STATE_DEAD
            stateTime = 0f
        }
    }

    fun touchFloor() {
        numberOfFloorsInContact++

        canJump = true
        isJumping = false
        canDoubleJump = true
        if (state == STATE_NORMAL) stateTime = 0f
    }


    fun endTouchFloor() {
        numberOfFloorsInContact--
        if (numberOfFloorsInContact == 0) {
            canJump = false

            // If I stop touching the floor because I jump, I can still jump again (double jump).
            if (!isJumping)
                canDoubleJump = false
        }
    }


    fun updateStateTime(delta: Float) {
        stateTime += delta
    }

    fun setPickUpMagnet() {
        durationMagnet = 0f
        isMagnetEnabled = true
    }


    companion object {

        const val STATE_NORMAL = 0 // STATE_NORMAL applies to run, dash, slide, jump.
        const val STATE_HURT = 1
        const val STATE_DIZZY = 2
        const val STATE_DEAD = 3
        const val STATE_REVIVE = 4
        const val TYPE_GIRL = 0
        const val TYPE_BOY = 1
        const val TYPE_NINJA = 2
        const val DRAW_WIDTH = 1.27f
        const val DRAW_HEIGHT = 1.05f
        const val WIDTH = .55f
        const val HEIGHT = 1f
        const val HEIGHT_SLIDE = .45f
        const val VELOCITY_RUN = 3f
        const val VELOCITY_DASH = 7f
        private const val DURATION_MAGNET = 10f
        private const val DURATION_DASH = 5

        @JvmStatic
        val DURATION_DEAD = Assets.playerDeadAnimation.animationDuration + .5f
        val DURATION_HURT = Assets.playerHurtAnimation.animationDuration + .1f
        const val DURATION_DIZZY = 1.25f
        const val JUMP_SPEED = 5f
    }
}