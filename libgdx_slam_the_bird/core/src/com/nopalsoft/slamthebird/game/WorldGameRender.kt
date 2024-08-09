package com.nopalsoft.slamthebird.game

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.nopalsoft.slamthebird.Assets
import com.nopalsoft.slamthebird.objects.Boost
import com.nopalsoft.slamthebird.objects.Enemy
import com.nopalsoft.slamthebird.objects.Platform
import com.nopalsoft.slamthebird.objects.Robot
import com.nopalsoft.slamthebird.screens.Screens

class WorldGameRender(batcher: SpriteBatch, worldGame: WorldGame) {
    val WIDTH: Float = Screens.WORLD_SCREEN_WIDTH
    val HEIGHT: Float = Screens.WORLD_SCREEN_HEIGHT

    var batcher: SpriteBatch
    var worldGame: WorldGame

    var camera: OrthographicCamera = OrthographicCamera(WIDTH, HEIGHT)

    var renderBox: Box2DDebugRenderer

    init {
        camera.position[WIDTH / 2f, HEIGHT / 2f] = 0f

        this.batcher = batcher
        this.worldGame = worldGame
        this.renderBox = Box2DDebugRenderer()
    }

    fun render() {
        camera.position.y = worldGame.robot!!.position.y

        if (camera.position.y < HEIGHT / 2f) camera.position.y = HEIGHT / 2f
        else if (camera.position.y > HEIGHT / 2f + 3) camera.position.y = HEIGHT / 2f + 3

        camera.update()
        batcher.projectionMatrix = camera.combined

        batcher.begin()

        batcher.enableBlending()

        renderBackground()
        renderPlatforms()
        renderBoost()
        renderCoins()
        renderEnemies()
        renderPersonaje()

        batcher.end()
    }

    private fun renderBackground() {
        batcher.draw(Assets.background, 0f, 0f, WIDTH, HEIGHT + 3)
    }

    private fun renderPlatforms() {
        for (obj in worldGame.arrayPlatforms) {
            var keyFrame: TextureRegion? = Assets.platform

            if (obj.state == Platform.STATE_BROKEN) {
                if (obj.stateTime < Assets.breakablePlatform!!.animationDuration) keyFrame =
                    Assets.breakablePlatform!!.getKeyFrame(
                        obj.stateTime, false
                    )
                else continue
            }

            if (obj.state == Platform.STATE_BREAKABLE) keyFrame = Assets.breakablePlatform!!.getKeyFrame(0f)

            if (obj.state == Platform.STATE_CHANGING) batcher.draw(
                keyFrame, obj.position.x - .5f,
                obj.position.y - .1f, .5f, .15f, 1f, .3f,
                obj.changingScale, obj.changingScale, 0f
            )
            else batcher.draw(
                keyFrame, obj.position.x - .5f,
                obj.position.y - .15f, 1f, .3f
            )

            if (obj.state == Platform.STATE_FIRE) batcher.draw(
                Assets.animationPlatformFire!!.getKeyFrame(
                    obj.stateTime, true
                ), obj.position.x - .5f,
                obj.position.y + .1f, 1f, .3f
            )
        }
    }

    private fun renderBoost() {
        for (obj in worldGame.arrayBoost) {
            var keyFrame: TextureRegion? = when (obj.type) {
                Boost.TIPO_COIN_RAIN -> Assets.coinRainBoost
                Boost.TIPO_ICE -> Assets.IceBoost
                Boost.TIPO_SUPERJUMP -> Assets.superJumpBoost
                Boost.TIPO_INVENCIBLE -> Assets.invincibleBoost
                else -> Assets.invincibleBoost
            }
            batcher.draw(
                keyFrame, obj.position.x - .175f,
                obj.position.y - .15f, .35f, .3f
            )
        }
    }

    private fun renderCoins() {
        for (obj in worldGame.arrayCoins) {
            batcher.draw(
                Assets.animationCoin!!.getKeyFrame(obj.stateTime, true),
                obj.position.x - .15f, obj.position.y - .15f, .3f, .34f
            )
        }
    }

    fun renderEnemies() {
        for (obj in worldGame.arrayEnemies) {
            if (obj.state == Enemy.STATE_JUST_APPEAR) {
                batcher.draw(
                    Assets.birdSpawn, obj.position.x - .25f,
                    obj.position.y - .25f, .25f, .25f, .5f, .5f,
                    obj.appearScale, obj.appearScale, 0f
                )
                continue
            }
            var keyFrame = if (obj.state == Enemy.STATE_FLYING) {
                if (obj.lives >= 3) Assets.animationRedBirdFlap!!.getKeyFrame(
                    obj.stateTime, true
                )
                else Assets.animationBlueBirdFlap!!.getKeyFrame(
                    obj.stateTime, true
                )
            } else if (obj.state == Enemy.STATE_EVOLVING) {
                Assets.animationEvolving!!.getKeyFrame(obj.stateTime, true)
            } else {
                Assets.birdBlue
            }

            if (obj.speed.x > 0) batcher.draw(
                keyFrame, obj.position.x - .285f,
                obj.position.y - .21f, .57f, .42f
            )
            else batcher.draw(
                keyFrame, obj.position.x + .285f,
                obj.position.y - .21f, -.57f, .42f
            )
        }
    }

    private fun renderPersonaje() {
        val obj = worldGame.robot
        val keyFrame: TextureRegion

        if (obj!!.slam && obj.state == Robot.STATE_FALLING) {
            keyFrame = Assets.animationPlayerSlam!!.getKeyFrame(obj.stateTime)
            batcher.draw(
                Assets.slam!!.getKeyFrame(obj.stateTime, true),
                obj.position.x - .4f, obj.position.y - .55f, .8f, .5f
            )
        } else if (obj.state == Robot.STATE_FALLING
            || obj.state == Robot.STATE_JUMPING
        ) {
            keyFrame = Assets.animationPlayerJump!!.getKeyFrame(obj.stateTime, true)
        } else keyFrame = Assets.animationPlayerHit!!.getKeyFrame(obj.stateTime, true)

        // c
        if (obj.speed.x > .1f) batcher.draw(
            keyFrame, obj.position.x - .3f, obj.position.y - .3f,
            .3f, .3f, .6f, .6f, 1f, 1f, obj.angleGrad
        )
        else if (obj.speed.x < -.1f) batcher.draw(
            keyFrame, obj.position.x + .3f, obj.position.y - .3f,
            -.3f, .3f, -.6f, .6f, 1f, 1f, obj.angleGrad
        )
        else batcher.draw(
            Assets.player, obj.position.x - .3f,
            obj.position.y - .3f, .3f, .3f, .6f, .6f, 1f, 1f,
            obj.angleGrad
        )

        // TODO el personaje cuando se muere no tiene velocidad por lo que no aparece el keyframe sino que agarra assetspersonaje

        // Esto renderear los boost arriba de la cabeza del personaje
        renderBoostActivo(obj)
    }

    private fun renderBoostActivo(obj: Robot) {
        if (obj.isInvincible || obj.isSuperJump) {
            val timeToAlert = 2.5f // Tiempo para que empieze a parpaderar el boost
            val boostKeyFrame = if (obj.isInvincible) {
                if (obj.DURATION_INVINCIBLE - obj.durationInvincible <= timeToAlert) {
                    Assets.animationInvincibleBoostEnd!!.getKeyFrame(
                        obj.stateTime, true
                    ) // anim
                } else Assets.invincibleBoost
            } else { // jump
                if (obj.DURATION_SUPER_JUMP - obj.durationSuperJump <= timeToAlert) {
                    Assets.animationSuperJumpBoostEnd!!.getKeyFrame(
                        obj.stateTime, true
                    ) // anim
                } else Assets.superJumpBoost
            }

            batcher.draw(
                boostKeyFrame, obj.position.x - .175f,
                obj.position.y + .3f, .35f, .3f
            )
        }
    }
}
