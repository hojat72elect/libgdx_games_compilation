package com.nopalsoft.ninjarunner.objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Pool.Poolable
import com.nopalsoft.ninjarunner.Assets

class Missile : Poolable, Comparable<Missile> {

    val position = Vector2()
    var state = 0
    var stateTime = 0f
    var distanceFromCharacter = 0f


    fun initializeMissile(x: Float, y: Float) {
        position.set(x, y)
        state = STATE_NORMAL
        stateTime = 0f
    }

    fun update(delta: Float, body: Body, myPlayer: Player) {
        if (state == STATE_NORMAL) {
            position.x = body.position.x
            position.y = body.position.y
        }
        if (state == STATE_EXPLODE) {
            if (stateTime >= EXPLOSION_DURATION) {
                state = STATE_DESTROY
                stateTime = 0f
            }
        }

        distanceFromCharacter = myPlayer.position.dst(position)
        stateTime += delta
    }

    fun setHitTarget() {
        if (state == STATE_NORMAL) {
            state = STATE_EXPLODE
            stateTime = 0f
        }
    }

    fun setDestroy() {
        if (state != STATE_DESTROY) {
            state = STATE_DESTROY
            stateTime = 0f
        }
    }

    override fun reset() {
        // nothing is going on in here
    }

    override fun compareTo(other: Missile) =
        distanceFromCharacter.compareTo(other.distanceFromCharacter)


    companion object {

        const val STATE_NORMAL = 0
        const val STATE_EXPLODE = 1
        const val STATE_DESTROY = 2
        const val WIDTH = 1.27f
        const val HEIGHT = .44f
        const val SPEED_X = -2.5f
        private val EXPLOSION_DURATION = Assets.explosion!!.animationDuration + .1f

    }
}