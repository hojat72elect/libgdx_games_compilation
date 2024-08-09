package com.nopalsoft.slamthebird.objects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.utils.Pool.Poolable
import com.nopalsoft.slamthebird.game.WorldGame

class Boost : Poolable {
    @JvmField
    var state: Int = 0

    @JvmField
    var position: Vector2 = Vector2()
    var stateTime: Float = 0f

    @JvmField
    var type: Int = 0

    fun init(worldGame: WorldGame, x: Float, y: Float, type: Int) {
        this.type = type
        position[x] = y
        stateTime = 0f
        state = STATE_NORMAL

        val bodyDefinition = BodyDef()
        bodyDefinition.position.x = x
        bodyDefinition.position.y = y
        bodyDefinition.type = BodyType.KinematicBody

        val body = worldGame.oWorldBox.createBody(bodyDefinition)

        val shape = PolygonShape()
        shape.setAsBox(.15f, .15f)

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.density = 8f
        fixture.restitution = 0f
        fixture.friction = 0f
        fixture.isSensor = true

        body.createFixture(fixture)

        body.userData = this
        shape.dispose()
    }

    fun update(delta: Float, body: Body) {
        position.x = body.position.x
        position.y = body.position.y
        stateTime += delta

        if (stateTime >= DURATION_AVAILABLE) {
            state = STATE_TAKEN
            stateTime = 0f
        }
    }

    fun hit() {
        state = STATE_TAKEN
        stateTime = 0f
    }

    override fun reset() {
        // TODO Auto-generated method stub
    }

    companion object {
        const val TIPO_SUPERJUMP: Int = 0
        const val TIPO_INVENCIBLE: Int = 1
        const val TIPO_COIN_RAIN: Int = 2
        const val TIPO_ICE: Int = 3

        var DURATION_AVAILABLE: Float = 5f

        var STATE_NORMAL: Int = 0

        @JvmField
        var STATE_TAKEN: Int = 1
    }
}
