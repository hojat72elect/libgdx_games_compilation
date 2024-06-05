package com.nopalsoft.superjumper.game

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.nopalsoft.superjumper.Assets
import com.nopalsoft.superjumper.objects.Bullet
import com.nopalsoft.superjumper.objects.Cloud
import com.nopalsoft.superjumper.objects.Coin
import com.nopalsoft.superjumper.objects.Enemy
import com.nopalsoft.superjumper.objects.Item
import com.nopalsoft.superjumper.objects.Lightning
import com.nopalsoft.superjumper.objects.Platform
import com.nopalsoft.superjumper.objects.PlatformPiece
import com.nopalsoft.superjumper.objects.Player
import com.nopalsoft.superjumper.screens.BasicScreen

class WorldGameRender(
    private val batcher: SpriteBatch,
    private val worldGame: WorldGame
) {


    private val width = BasicScreen.WORLD_WIDTH
    private val height = BasicScreen.WORLD_HEIGHT
    private val camera = OrthographicCamera(width, height)

    init {
        camera.position.set(width / 2, height / 2, 0f)

        // Sets up the debug renderer.
        Box2DDebugRenderer()
    }

    fun unProjectToWorldCoords(touchPoint: Vector3) {
        camera.unproject(touchPoint)
    }

    fun render() {
        if (worldGame.state == WorldGame.STATE_RUNNING) {
            camera.position.y = worldGame.player!!.position.y
        }

        if (camera.position.y < BasicScreen.WORLD_HEIGHT / 2f) {
            camera.position.y = BasicScreen.WORLD_HEIGHT / 2f
        }

        camera.update()
        batcher.setProjectionMatrix(camera.combined)

        batcher.begin()

        renderPlayer()
        renderPlatforms()
        renderPlatformPieces()
        renderCoins()
        renderItems()
        renderEnemy()
        renderCloud()
        renderLightning()
        renderBullet()

        batcher.end()
    }

    private fun renderPlayer() {
        val keyframe: AtlasRegion?

        val player = worldGame.player

        keyframe = if (player!!.speed.y > 0) {
            Assets.characterJump
        } else {
            Assets.characterStand
        }

        if (player.speed.x > 0) {
            batcher.draw(
                keyframe,
                player.position.x + Player.DRAW_WIDTH / 2f,
                player.position.y - Player.DRAW_HEIGHT / 2f,
                -Player.DRAW_WIDTH / 2f,
                Player.DRAW_HEIGHT / 2f,
                -Player.DRAW_WIDTH,
                Player.DRAW_HEIGHT,
                1f,
                1f,
                player.angleDeg
            )
        } else {
            batcher.draw(
                keyframe,
                player.position.x - Player.DRAW_WIDTH / 2f,
                player.position.y - Player.DRAW_HEIGHT / 2f,
                Player.DRAW_WIDTH / 2f,
                Player.DRAW_HEIGHT / 2f,
                Player.DRAW_WIDTH,
                Player.DRAW_HEIGHT,
                1f,
                1f,
                player.angleDeg
            )
        }

        if (player.isJetPack) {
            batcher.draw(
                Assets.jetpack,
                player.position.x - .45f / 2f,
                player.position.y - .7f / 2f,
                .45f,
                .7f
            )

            val fireFrame: TextureRegion = Assets.jetpackFire.getKeyFrame(
                player.durationJetPack,
                true
            )
            batcher.draw(
                fireFrame,
                player.position.x - .35f / 2f,
                player.position.y - .95f,
                .35f,
                .6f
            )
        }
        if (player.isBubble) {
            batcher.draw(
                Assets.bubble,
                player!!.position.x - .5f,
                player.position.y - .5f,
                1f,
                1f
            )
        }
    }

    private fun renderPlatforms() {
        for (platform in worldGame.arrayPlatforms) {
            var keyframe: AtlasRegion? = null

            if (platform.type == Platform.TYPE_BREAKABLE) {
                when (platform.color) {
                    Platform.COLOR_BEIGE -> keyframe = Assets.platformBeigeBroken
                    Platform.COLOR_BLUE -> keyframe = Assets.platformBlueBroken
                    Platform.COLOR_GRAY -> keyframe = Assets.platformGrayBroken
                    Platform.COLOR_GREEN -> keyframe = Assets.platformGreenBroken
                    Platform.COLOR_MULTICOLOR -> keyframe = Assets.platformMulticolorBroken
                    Platform.COLOR_PINK -> keyframe = Assets.platformPinkBroken
                }
            } else {
                when (platform.color) {
                    Platform.COLOR_BEIGE -> keyframe = Assets.platformBeige
                    Platform.COLOR_BLUE -> keyframe = Assets.platformBlue
                    Platform.COLOR_GRAY -> keyframe = Assets.platformGray
                    Platform.COLOR_GREEN -> keyframe = Assets.platformGreen
                    Platform.COLOR_MULTICOLOR -> keyframe = Assets.platformMulticolor
                    Platform.COLOR_PINK -> keyframe = Assets.platformPink
                    Platform.COLOR_BEIGE_LIGHT -> keyframe = Assets.platformBeigeLight
                    Platform.COLOR_BLUE_LIGHT -> keyframe = Assets.platformBlueLight
                    Platform.COLOR_GRAY_LIGHT -> keyframe = Assets.platformGrayLight
                    Platform.COLOR_GREEN_LIGHT -> keyframe = Assets.platformGreenLight
                    Platform.COLOR_MULTICOLOR_LIGHT -> keyframe = Assets.platformMulticolorLight
                    Platform.COLOR_PINK_LIGHT -> keyframe = Assets.platformPinkLight
                }
            }
            batcher.draw(
                keyframe,
                platform.position.x - Platform.DRAW_WIDTH_NORMAL / 2f,
                platform.position.y - Platform.DRAW_HEIGHT_NORMAL / 2f,
                Platform.DRAW_WIDTH_NORMAL,
                Platform.DRAW_HEIGHT_NORMAL
            )
        }
    }

    private fun renderPlatformPieces() {
        for (platformPiece in worldGame.arrayPlatformPieces) {
            var keyframe: AtlasRegion? = null

            if (platformPiece.type == PlatformPiece.TYPE_LEFT) {
                when (platformPiece.color) {
                    Platform.COLOR_BEIGE -> keyframe = Assets.platformBeigeLeft
                    Platform.COLOR_BLUE -> keyframe = Assets.platformBlueLeft
                    Platform.COLOR_GRAY -> keyframe = Assets.platformGrayLeft
                    Platform.COLOR_GREEN -> keyframe = Assets.platformGreenLeft
                    Platform.COLOR_MULTICOLOR -> keyframe = Assets.platformMulticolorLeft
                    Platform.COLOR_PINK -> keyframe = Assets.platformPinkLeft
                }
            } else {
                when (platformPiece.color) {
                    Platform.COLOR_BEIGE -> keyframe = Assets.platformBeigeRight
                    Platform.COLOR_BLUE -> keyframe = Assets.platformBlueRight
                    Platform.COLOR_GRAY -> keyframe = Assets.platformGrayRight
                    Platform.COLOR_GREEN -> keyframe = Assets.platformGreenRight
                    Platform.COLOR_MULTICOLOR -> keyframe = Assets.platformMulticolorRight
                    Platform.COLOR_PINK -> keyframe = Assets.platformPinkRight
                }
            }

            batcher.draw(
                keyframe,
                platformPiece.position.x - PlatformPiece.DRAW_WIDTH_NORMAL / 2f,
                platformPiece.position.y - PlatformPiece.DRAW_HEIGHT_NORMAL
                        / 2f,
                PlatformPiece.DRAW_WIDTH_NORMAL / 2f,
                PlatformPiece.DRAW_HEIGHT_NORMAL / 2f,
                PlatformPiece.DRAW_WIDTH_NORMAL,
                PlatformPiece.DRAW_HEIGHT_NORMAL,
                1f,
                1f,
                platformPiece.angleDegree
            )
        }
    }

    private fun renderCoins() {
        for (coin in worldGame.arrayCoins) {
            batcher.draw(
                Assets.coin,
                coin.position.x - Coin.DRAW_WIDTH / 2f,
                coin.position.y - Coin.DRAW_HEIGHT / 2f,
                Coin.DRAW_WIDTH,
                Coin.DRAW_HEIGHT
            )
        }
    }

    private fun renderItems() {
        for (item in worldGame.arrayItem) {
            var keyframe: TextureRegion? = null

            when (item.type) {
                Item.TYPE_BUBBLE -> keyframe = Assets.bubbleSmall
                Item.TYPE_JETPACK -> keyframe = Assets.jetpackSmall
                Item.TYPE_GUN -> keyframe = Assets.gun
            }
            batcher.draw(
                keyframe,
                item.position.x - Item.DRAW_WIDTH / 2f,
                item.position.y - Item.DRAW_HEIGHT / 2f,
                Item.DRAW_WIDTH,
                Item.DRAW_HEIGHT
            )
        }
    }

    private fun renderEnemy() {
        for (enemy in worldGame.arrayEnemies) {
            val keyframe: TextureRegion = Assets.enemy.getKeyFrame(enemy.stateTime, true)

            batcher.draw(
                keyframe,
                enemy.position.x - Enemy.DRAW_WIDTH / 2f,
                enemy.position.y - Enemy.DRAW_HEIGHT / 2f,
                Enemy.DRAW_WIDTH,
                Enemy.DRAW_HEIGHT
            )
        }
    }

    private fun renderCloud() {
        for (cloud in worldGame.arrayClouds) {
            var keyframe: TextureRegion? = null

            when (cloud.guy) {
                Cloud.TYPE_ANGRY -> keyframe = Assets.cloudAngry
                Cloud.TYPE_HAPPY -> keyframe = Assets.cloudHappy
            }
            batcher.draw(
                keyframe,
                cloud.position.x - Cloud.DRAW_WIDTH / 2f,
                cloud.position.y - Cloud.DRAW_HEIGHT / 2f,
                Cloud.DRAW_WIDTH,
                Cloud.DRAW_HEIGHT
            )

            if (cloud.isBlowing) {
                batcher.draw(
                    Assets.cloudWind,
                    cloud.position.x - .35f,
                    cloud.position.y - .85f,
                    .6f,
                    .8f
                )
            }
        }
    }

    private fun renderLightning() {
        for (lightning in worldGame.arrayLightnings) {
            val keyframe: TextureRegion = Assets.ray.getKeyFrame(lightning.stateTime, true)

            batcher.draw(
                keyframe,
                lightning.position.x - Lightning.DRAW_WIDTH / 2f,
                lightning.position.y - Lightning.DRAW_HEIGHT / 2f,
                Lightning.DRAW_WIDTH,
                Lightning.DRAW_HEIGHT
            )
        }
    }

    private fun renderBullet() {
        for (bullet in worldGame.arrayBullets) {
            batcher.draw(
                Assets.bullet,
                bullet.position.x - Bullet.SIZE / 2f,
                bullet.position.y - Bullet.SIZE / 2f,
                Bullet.SIZE,
                Bullet.SIZE
            )
        }
    }

}