package com.nopalsoft.invaders.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Intersector
import com.nopalsoft.invaders.Assets
import com.nopalsoft.invaders.Settings.accelerometerSensitivity
import com.nopalsoft.invaders.frame.AlienShip
import com.nopalsoft.invaders.frame.Boost
import com.nopalsoft.invaders.frame.Bullet
import com.nopalsoft.invaders.frame.Missile
import com.nopalsoft.invaders.frame.Ship
import com.nopalsoft.invaders.screens.Screens
import java.util.Random

class World {
    @JvmField
    var state: Int

    @JvmField
    var myShip = Ship(WIDTH / 2f, 9.5f)
    var boosts = arrayListOf<Boost>()
    var missiles = arrayListOf<Missile>()
    var shipBullets = arrayListOf<Bullet>()
    var alienBullets = arrayListOf<Bullet>()
    var alienShips = arrayListOf<AlienShip>()

    private var oRan: Random

    @JvmField
    var score: Int

    @JvmField
    var currentLevel: Int

    @JvmField
    var missileCount: Int = 5

    private var extraChanceDrop: Int

    private var maxMissilesRonda: Int
    private var maxBalasRonda: Int
    private var nivelBala: Int =
        0 // It is the level at which the current bullet is, each time a boost is grabbed it increases.
    private var probs: Float // This variable will increase each level to make the game more difficult.
    private var aumentoVel: Float

    init {

        myShip.lives = 5
        extraChanceDrop = 5
        maxMissilesRonda = 5
        maxBalasRonda = 5

        score = 0
        currentLevel = 0
        aumentoVel = 0f
        probs = aumentoVel
        oRan = Random(Gdx.app.graphics.deltaTime.toLong() * 10_000)
        state = STATE_RUNNING
        agregarAliens()
    }

    private fun agregarAliens() {
        currentLevel++

        // Cada 2 niveles aumento los missiles que se pueden disparar
        if (currentLevel % 2f == 0f) {
            maxMissilesRonda++
            maxBalasRonda++
        }
        var x: Float
        var y = 21f
        var vida = 1

        var vidaAlterable = false
        if (currentLevel > 2) {
            vidaAlterable = true
            probs += 0.2f
            aumentoVel += .02f
        }

        // I will add 25 aliens 5x5 (5 columns and 5 rows)
        for (col in 0..5) {
            y += 3.8f
            x = 1.5f
            for (ren in 0..5) {
                if (vidaAlterable) vida = oRan.nextInt(3) + 1 + probs.toInt() //

                alienShips.add(AlienShip(vida, aumentoVel, x, y))
                x += 4.5f
            }
        }
    }

    fun update(deltaTime: Float, accelX: Float, seDisparo: Boolean, seDisparoMissil: Boolean) {
        updateNave(deltaTime, accelX)
        updateAlienShip(deltaTime) // The alien bullets are added right here. They are updated in another method

        updateBalaNormalYConNivel(deltaTime, seDisparo)
        updateMissil(deltaTime, seDisparoMissil)
        updateBalaAlien(deltaTime)
        // Boosts are added every time an alienShip is hit. Here they are only updated.
        updateBoost(deltaTime)

        if (myShip.state != Ship.NAVE_STATE_EXPLODE) {
            checkCollision()
        }
        checkGameOver()
        checkLevelEnd() // When I have killed all the aliens
    }

    private fun updateNave(deltaTime: Float, accelX: Float) {
        if (myShip.state != Ship.NAVE_STATE_EXPLODE) {
            myShip.velocity.x = -accelX / accelerometerSensitivity * Ship.NAVE_MOVE_SPEED
        }
        myShip.update(deltaTime)
    }

    private fun updateAlienShip(deltaTime: Float) {
        val it: MutableIterator<AlienShip> = alienShips.iterator()
        while (it.hasNext()) {
            val oAlienShip = it.next()
            oAlienShip.update(deltaTime)

            /* Add Bullets to Aliens */
            if (oRan.nextInt(5000) < (1 + probs) && oAlienShip.state != AlienShip.EXPLOTING) {
                val x = oAlienShip.position.x
                val y = oAlienShip.position.y
                alienBullets.add(Bullet(x, y))
            }

            /* I delete if they have already exploded */
            if (oAlienShip.state == AlienShip.EXPLOTING && oAlienShip.stateTime > AlienShip.TIEMPO_EXPLODE) {
                it.remove()
            }

            /* If the aliens reach the bottom you automatically lose */
            if (oAlienShip.position.y < 9.5f) {
                state = STATE_GAME_OVER
            }
        }
    }

    private fun updateBalaAlien(deltaTime: Float) {
        /* Now I update and recalculate len in case a new bullet is fired */

        val it: MutableIterator<Bullet> = alienBullets.iterator()
        while (it.hasNext()) {
            val oAlienBullet = it.next()
            if (oAlienBullet.position.y < -2) oAlienBullet.destroyBullet()
            oAlienBullet.update(deltaTime)
            if (oAlienBullet.state == Bullet.STATE_EXPLOTANDO) {
                it.remove()
            }
        }
    }

    private fun updateBalaNormalYConNivel(deltaTime: Float, seDisparo: Boolean) {
        val x = myShip.position.x
        val y = myShip.position.y + 1

        if (seDisparo && shipBullets.size < maxBalasRonda) {
            shipBullets.add(Bullet(x, y, nivelBala))
        }

        val it1: MutableIterator<Bullet> = shipBullets.iterator()
        while (it1.hasNext()) {
            val oBullet = it1.next()
            if (oBullet.position.y > HEIGHT + 2) oBullet.destroyBullet() // So that the missile does not go so far

            oBullet.update(deltaTime)
            if (oBullet.state == Bullet.STATE_EXPLOTANDO) {
                it1.remove()
            }
        }
    }

    private fun updateMissil(deltaTime: Float, seDisparoMissil: Boolean) {
        // Limit of maxMissilesRound Missiles in a round
        val len = missiles.size
        if (seDisparoMissil && missileCount > 0 && len < maxMissilesRonda) {
            val x = myShip.position.x
            val y = myShip.position.y + 1
            missiles.add(Missile(x, y))
            missileCount--
            Assets.playSound(Assets.missileFire, 0.15f)
        }

        // Now I update. Len recalculate in case a new missile was fired.
        val it: MutableIterator<Missile> = missiles.iterator()
        while (it.hasNext()) {
            val oMissile = it.next()
            if (oMissile.position.y > HEIGHT + 2 && oMissile.state != Missile.STATE_EXPLODING) oMissile.hitTarget()
            oMissile.update(deltaTime)
            if (oMissile.state == Missile.STATE_EXPLODING && oMissile.stateTime > Missile.EXPLODE_TIME) {
                it.remove()
            }
        }
    }

    private fun updateBoost(deltaTime: Float) {
        val it: MutableIterator<Boost> = boosts.iterator()
        while (it.hasNext()) {
            val oBoost = it.next()
            oBoost.update(deltaTime)
            if (oBoost.position.y < -2) {
                it.remove()
            }
        }
    }

    /**
     * All types of collisions are checked
     */
    private fun checkCollision() {
        checkColisionNaveBalaAliens() // First I check if they hit my ship =(
        checkColisionAliensBala() // I check if my bullets hit those guys (I checkNormal Bullet, Level1 Bullet, Level2 Bullet, Level3 Bullet... etc.
        checkColisionAlienMissil()
        checkColisionBoostNave()
    }

    private fun checkColisionNaveBalaAliens() {
        for (oAlienBullet in alienBullets) {
            if (Intersector.overlaps(
                    myShip.boundsRectangle,
                    oAlienBullet.boundsRectangle
                ) && myShip.state != Ship.NAVE_STATE_EXPLODE && myShip.state != Ship.NAVE_STATE_BEING_HIT
            ) {
                myShip.beingHit()
                oAlienBullet.hitTarget(1)
            }
        }
    }

    private fun checkColisionAliensBala() {
        for (oBala in shipBullets) {
            for (oAlien in alienShips) {
                if (Intersector.overlaps(
                        oAlien.boundsCircle,
                        oBala.boundsRectangle
                    ) && (oAlien.state != AlienShip.EXPLOTING)
                ) {
                    oBala.hitTarget(oAlien.vidasLeft)
                    oAlien.beingHit()
                    if (oAlien.state == AlienShip.EXPLOTING) { // I only increase the score and add boost if it is already exploding, not if I decrease its life
                        score += oAlien.punctuation // I update the score
                        addBoost(
                            oAlien.position.x,
                            oAlien.position.y
                        ) // Here I'm going to see if it gives me any boost or not.
                        Assets.playSound(Assets.explosionSound, 0.6f)
                    }
                }
            }
        }
    }

    private fun checkColisionAlienMissil() {
        for (oMissile in missiles) {
            for (oAlien in alienShips) {
                if (oMissile.state == Missile.STATE_TRIGGERED && Intersector.overlaps(
                        oAlien.boundsCircle,
                        oMissile.boundsRectangle
                    ) && oAlien.state != AlienShip.EXPLOTING
                ) {
                    oMissile.hitTarget()
                    oAlien.beingHit()
                    if (oAlien.state == AlienShip.EXPLOTING) { // I only increase the score and add boost if it is already exploding, not if I decrease its life
                        score += oAlien.punctuation // I update the score
                        addBoost(
                            oAlien.position.x,
                            oAlien.position.y
                        ) // Here I'm going to see if it gives me any boost or not.
                        Assets.playSound(Assets.explosionSound, 0.6f)
                    }
                }
                // Check with the radius of the explosion
                if (oMissile.state == Missile.STATE_EXPLODING && Intersector.overlaps(
                        oAlien.boundsCircle,
                        oMissile.boundsCircle
                    ) && oAlien.state != AlienShip.EXPLOTING
                ) {
                    oAlien.beingHit()
                    if (oAlien.state == AlienShip.EXPLOTING) { // I only increase the score and add boost if it is already exploding, not if I decrease its life
                        score += oAlien.punctuation // I update the score
                        addBoost(
                            oAlien.position.x,
                            oAlien.position.y
                        ) // Here I'm going to see if it gives me any boost or not.
                        Assets.playSound(Assets.explosionSound, 0.6f)
                    }
                }
            }
        }
    }

    private fun checkColisionBoostNave() {
        val it: MutableIterator<Boost> = boosts.iterator()
        while (it.hasNext()) {
            val oBoost = it.next()
            if (Intersector.overlaps(
                    oBoost.boundsCircle,
                    myShip.boundsRectangle
                ) && myShip.state != Ship.NAVE_STATE_EXPLODE
            ) {
                when (oBoost.type) {
                    Boost.VIDA_EXTRA -> myShip.hitExtraLife()
                    Boost.UPGRADE_LEVEL_WEAPONS -> nivelBala++
                    Boost.MISSILE_EXTRA -> missileCount++
                    Boost.SHIELD -> myShip.hitShield()
                    else -> myShip.hitShield()
                }
                it.remove()
                Assets.playSound(Assets.coinSound)
            }
        }
    }

    /**
     * Receives the x,y coordinates of the ship that has just been destroyed. The Boost can be a
     * life, weapons, shield, etc.
     *
     * @param x position where the boost will appear.
     * @param y position where the boost will appear.
     */
    private fun addBoost(x: Float, y: Float) {
        if (oRan.nextInt(100) < 5 + extraChanceDrop) { // Chances of a boost appearing
            when (oRan.nextInt(4)) {
                Boost.VIDA_EXTRA -> boosts.add(Boost(Boost.VIDA_EXTRA, x, y))
                1 -> boosts.add(Boost(Boost.UPGRADE_LEVEL_WEAPONS, x, y))
                Boost.MISSILE_EXTRA -> boosts.add(Boost(Boost.MISSILE_EXTRA, x, y))
                else -> boosts.add(Boost(Boost.SHIELD, x, y))
            }
        }
    }

    private fun checkGameOver() {
        if (myShip.state == Ship.NAVE_STATE_EXPLODE && myShip.stateTime > Ship.EXPLODE_TIME) {
            myShip.position.x = 200f
            state = STATE_GAME_OVER
        }
    }

    private fun checkLevelEnd() {
        if (alienShips.size == 0) {
            shipBullets.clear()
            alienBullets.clear()
            agregarAliens()
        }
    }

    companion object {
        const val HEIGHT = Screens.WORLD_SCREEN_HEIGHT
        const val STATE_RUNNING = 0
        const val STATE_GAME_OVER = 1
        const val STATE_PAUSED = 2
        var WIDTH = Screens.WORLD_SCREEN_WIDTH.toFloat()
    }
}
