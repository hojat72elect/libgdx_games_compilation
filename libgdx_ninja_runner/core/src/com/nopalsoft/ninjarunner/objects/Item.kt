package com.nopalsoft.ninjarunner.objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Pool.Poolable
import com.nopalsoft.ninjarunner.Assets

open class Item(@JvmField val width: Float, @JvmField val height: Float) : Poolable {

    @JvmField
    var state = 0

    @JvmField
    var stateTime = 0f

    @JvmField
    val position = Vector2()
    var velocity = Vector2()

    fun initializeItem(x: Float, y: Float) {
        position.set(x, y)
        velocity.set(0f, 0f)
        state = STATE_NORMAL
        stateTime = 0f
    }


    fun update(delta: Float, body: Body, oPet: Pet, oPlayer: Player) {
        if (state == STATE_NORMAL) {
            position.x = body.position.x
            position.y = body.position.y

            // First I check if they are attracted to the character.
            if (oPlayer.isMagnetEnabled && position.dst(oPlayer.position) <= 5f) {
                moveCoinsMagnet(body, oPlayer.position)
            } else if (position.dst(oPet.position) <= 2f) {
                // TODO
            } else {
                body.setLinearVelocity(0f, 0f)
            }
        }

        stateTime += delta
    }

    private fun moveCoinsMagnet(body: Body, targetPosition: Vector2) {
        velocity = body.linearVelocity
        velocity.set(targetPosition).sub(position).scl(Player.VELOCITY_DASH + 3)
        body.linearVelocity = velocity
    }

    fun setPicked() {
        if (state == STATE_NORMAL) {
            state = STATE_DESTROY
            stateTime = 0f
        }
    }


    override fun reset() {
        // Nothing is going on in here
    }

    companion object {
        const val STATE_NORMAL = 0
        const val STATE_DESTROY = 1

        @JvmStatic
        val DURATION_PICK = Assets.pickAnimation.animationDuration + .1
    }
}