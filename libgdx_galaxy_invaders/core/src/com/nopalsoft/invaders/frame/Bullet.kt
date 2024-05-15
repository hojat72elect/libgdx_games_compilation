package com.nopalsoft.invaders.frame

class Bullet : DynamicGameObject {
    private val speed: Float = 30f
    var level: Int = 1
    private var stateTime: Float

    @JvmField
    var state: Int

    /**
     * Space ship bullet
     */
    constructor(x: Float, y: Float, boostLevel: Int) : super(x, y, WIDTH, HEIGHT) {
        state = STATE_DISPARADO
        stateTime = 0f
        velocity[0f] = speed
        this.level += boostLevel
    }

    /**
     * Alien Bullet
     */
    constructor(x: Float, y: Float) : super(x, y, WIDTH, HEIGHT) {
        state = STATE_DISPARADO
        stateTime = 0f
        velocity[0f] = -speed
    }

    fun update(deltaTime: Float) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime)
        boundsRectangle!!.x = position.x - WIDTH / 2
        boundsRectangle.y = position.y - HEIGHT / 2
        stateTime += deltaTime
    }

    fun hitTarget(vidaTarget: Int) {
        level -= vidaTarget
        if (level <= 0) {
            velocity[0f] = 0f
            stateTime = 0f
            state = STATE_EXPLOTANDO
        }
    }

    /**
     * In case the bullet leaves the World.Height screen, I call this method to remove it from the array.
     */
    fun destroyBullet() {
        velocity[0f] = 0f
        stateTime = 0f
        state = STATE_EXPLOTANDO
    }

    companion object {
        const val WIDTH: Float = 2.1f
        const val HEIGHT: Float = 1.5f
        const val STATE_DISPARADO: Int = 0
        const val STATE_EXPLOTANDO: Int = 1
    }
}
