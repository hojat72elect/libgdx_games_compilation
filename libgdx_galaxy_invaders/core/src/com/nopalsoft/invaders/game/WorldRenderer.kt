package com.nopalsoft.invaders.game

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.nopalsoft.invaders.Assets
import com.nopalsoft.invaders.Settings
import com.nopalsoft.invaders.frame.AlienShip
import com.nopalsoft.invaders.frame.Boost
import com.nopalsoft.invaders.frame.Missile
import com.nopalsoft.invaders.frame.Ship
import com.nopalsoft.invaders.screens.Screens

class WorldRenderer(batch: SpriteBatch, private var oWorld: World) {
    private var cam: OrthographicCamera = OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT)
    private var batch: SpriteBatch

    init {
        cam.position[FRUSTUM_WIDTH / 2f, FRUSTUM_HEIGHT / 2f] =
            0f
        this.batch = batch
    }

    fun render(deltaTime: Float) {
        cam.update()
        batch.projectionMatrix = cam.combined
        renderBackground(deltaTime)
        renderObjects()
    }

    private fun renderBackground(deltaTime: Float) {
        batch.disableBlending()
        batch.begin()
        // batch.draw(Assets.fondo1, cam.position.x - FRUSTUM_WIDTH / 2, cam.position.y - FRUSTUM_HEIGHT / 2, FRUSTUM_WIDTH,FRUSTUM_HEIGHT);
        batch.end()
        if (oWorld.state == World.STATE_RUNNING) {
            Assets.parallaxBackground.render(deltaTime)
        } else { // GAMEOVER, PAUSA, READY, ETC
            Assets.parallaxBackground.render(0f)
        }
    }

    private fun renderObjects() {
        batch.enableBlending()
        batch.begin()
        renderNave()
        renderAliens()
        renderShipBullet()
        renderAlienBullet()
        renderMissil()
        renderBoost()
        batch.end()
        if (Settings.drawDebugLines) {
            renderDebugBounds()
        }
    }

    private fun renderNave() {
        val keyFrame = if (oWorld.oNave.state == Ship.NAVE_STATE_NORMAL) {
            if (oWorld.oNave.velocity.x < -3) Assets.shipLeft
            else if (oWorld.oNave.velocity.x > 3) Assets.shipRight
            else Assets.ship
        } else {
            Assets.explosionFuego.getKeyFrame(oWorld.oNave.stateTime, false)
        }
        batch.draw(
            keyFrame,
            oWorld.oNave.position.x - Ship.DRAW_WIDTH / 2f,
            oWorld.oNave.position.y - Ship.DRAW_HEIGHT
                    / 2f,
            Ship.DRAW_WIDTH,
            Ship.DRAW_HEIGHT
        )

        /* Dibuja el escudo de la nave */
        if (oWorld.oNave.livesShield > 0) {
            batch.draw(
                Assets.shield.getKeyFrame(oWorld.oNave.stateTime, true),
                oWorld.oNave.position.x - 5.5f,
                oWorld.oNave.position.y - 5.5f,
                11f,
                11f
            )
        }
    }

    private fun renderAliens() {
        val len = oWorld.alienShips.size
        for (i in 0 until len) {
            val oAlienShip = oWorld.alienShips[i]
            val keyFrame = if (oAlienShip.state == AlienShip.EXPLOTING) {
                Assets.explosionFuego.getKeyFrame(oAlienShip.stateTime, false)
            } else {
                if (oAlienShip.vidasLeft >= 10) Assets.alien4
                else if (oAlienShip.vidasLeft >= 5) Assets.alien3
                else if (oAlienShip.vidasLeft >= 2) Assets.alien2
                else Assets.alien1
            }

            batch.draw(
                keyFrame, oAlienShip.position.x - AlienShip.DRAW_WIDTH / 2f, oAlienShip.position.y
                        - AlienShip.DRAW_HEIGHT / 2f, AlienShip.DRAW_WIDTH, AlienShip.DRAW_HEIGHT
            )
        }
    }

    private fun renderShipBullet() {
        for (i in 0 until oWorld.shipBullets.size) {
            val bullet = oWorld.shipBullets[i]

            if (bullet.level <= 1) {
                batch.draw(
                    Assets.balaNormal,
                    bullet.position.x - 0.15f,
                    bullet.position.y - 0.45f,
                    0.3f,
                    0.9f
                )
            } else if (bullet.level == 2) {
                batch.draw(
                    Assets.bulletLevel1,
                    bullet.position.x - 1.05f,
                    bullet.position.y - 0.75f,
                    2.1f,
                    1.5f
                )
            } else if (bullet.level == 3) {
                batch.draw(
                    Assets.bulletLevel2,
                    bullet.position.x - 1.05f,
                    bullet.position.y - 0.75f,
                    2.1f,
                    1.5f
                )
            } else if (bullet.level == 4) {
                batch.draw(
                    Assets.bulletLevel3,
                    bullet.position.x - 1.05f,
                    bullet.position.y - 0.75f,
                    2.1f,
                    1.5f
                )
            } else {
                batch.draw(
                    Assets.bulletLevel4,
                    bullet.position.x - 1.05f,
                    bullet.position.y - 0.75f,
                    2.1f,
                    1.5f
                )
            }
        }
    }


    private fun renderAlienBullet() {
        val len = oWorld.alienBullets.size
        for (i in 0 until len) {
            val oAlienBullet = oWorld.alienBullets[i]
            batch.draw(
                Assets.bulletNormalEnemy,
                oAlienBullet.position.x - 0.15f,
                oAlienBullet.position.y - 0.45f,
                0.3f,
                0.9f
            )
        }
    }

    private fun renderMissil() {
        val len = oWorld.missiles.size
        for (i in 0 until len) {
            val oMissile = oWorld.missiles[i]
            var widht: Float
            var heigth: Float
            var keyFrame: TextureRegion?
            when (oMissile.state) {
                Missile.STATE_TRIGGERED -> {
                    keyFrame = Assets.missile.getKeyFrame(oMissile.stateTime, true)
                    widht = .8f
                    heigth = 2.5f
                }

                Missile.STATE_EXPLODING -> {
                    keyFrame = Assets.explosionFuego.getKeyFrame(oMissile.stateTime, false)
                    run {
                        heigth = 15.0f
                        widht = heigth
                    }
                }

                else -> {
                    keyFrame = Assets.explosionFuego.getKeyFrame(oMissile.stateTime, false)
                    run {
                        heigth = 15.0f
                        widht = heigth
                    }
                }
            }
            batch.draw(
                keyFrame,
                oMissile.position.x - widht / 2f,
                oMissile.position.y - heigth / 2f,
                widht,
                heigth
            )
        }
    }

    private fun renderBoost() {
        val len = oWorld.boosts.size
        for (i in 0 until len) {
            val oBoost = oWorld.boosts[i]

            val keyFrame: TextureRegion? = when (oBoost.type) {
                Boost.VIDA_EXTRA -> Assets.upgLife

                Boost.UPGRADE_LEVEL_WEAPONS -> Assets.boost1
                Boost.MISSILE_EXTRA -> Assets.boost2
                else -> Assets.boost3

            }
            batch.draw(
                keyFrame,
                oBoost.position.x - Boost.DRAW_SIZE / 2f,
                oBoost.position.y - Boost.DRAW_SIZE / 2f,
                Boost.DRAW_SIZE,
                Boost.DRAW_SIZE
            )
        }
    }

    private fun renderDebugBounds() {
        val render = ShapeRenderer()
        render.projectionMatrix = cam.combined
        render.begin(ShapeType.Line)

        val naveBounds = oWorld.oNave.boundsRectangle
        render.rect(naveBounds!!.x, naveBounds.y, naveBounds.width, naveBounds.height)

        for (obj in oWorld.alienShips) {
            val objBounds = obj.boundsCircle
            render.circle(objBounds!!.x, objBounds.y, objBounds.radius)
        }

        for (obj in oWorld.boosts) {
            val objBounds = obj.boundsCircle
            render.circle(objBounds!!.x, objBounds.y, objBounds.radius)
        }

        render.end()
    }

    companion object {
        const val FRUSTUM_WIDTH: Float = Screens.WORLD_SCREEN_WIDTH.toFloat()
        const val FRUSTUM_HEIGHT: Float = Screens.WORLD_SCREEN_HEIGHT.toFloat()
    }
}
