package com.nopalsoft.ninjarunner.game

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.nopalsoft.ninjarunner.AnimationSprite
import com.nopalsoft.ninjarunner.Assets
import com.nopalsoft.ninjarunner.objects.*
import com.nopalsoft.ninjarunner.screens.Screens

class WorldGameRenderer(batcher: SpriteBatch, myWorld: WorldGame) {
    val WIDTH: Float = Screens.WORLD_WIDTH
    val HEIGHT: Float = Screens.WORLD_HEIGHT

    var batcher: SpriteBatch
    var myWorld: WorldGame

    var myCamera: OrthographicCamera = OrthographicCamera(WIDTH, HEIGHT)

    var renderBox: Box2DDebugRenderer

    init {
        myCamera.position[WIDTH / 2f, HEIGHT / 2f] = 0f
        this.batcher = batcher
        this.myWorld = myWorld
        this.renderBox = Box2DDebugRenderer()
    }

    fun render(delta: Float) {
        myCamera.position[myWorld.myPlayer!!.position.x + 1.5f, myWorld.myPlayer!!.position.y] = 0f

        if (myCamera.position.y < HEIGHT / 2f) myCamera.position.y = HEIGHT / 2f
        else if (myCamera.position.y > HEIGHT / 2f) myCamera.position.y = HEIGHT / 2f

        if (myCamera.position.x < WIDTH / 2f) myCamera.position.x = WIDTH / 2f

        myCamera.update()
        batcher.projectionMatrix = myCamera.combined
        batcher.begin()
        batcher.enableBlending()

        renderPlatforms()
        renderWall()

        renderItems()

        renderPlayer()
        renderPet()

        renderObstacles(delta)
        renderMissile()

        batcher.end()
    }

    private fun renderItems() {
        for (obj in myWorld.arrayItems) {
            var spriteFrame: Sprite? = null

            if (obj.state == Item.STATE_NORMAL) {
                if (obj is ItemCurrency) {
                    spriteFrame = Assets.coinAnimation!!.getKeyFrame(obj.stateTime, true)
                } else if (obj is ItemMagnet) {
                    spriteFrame = Assets.magnet
                } else if (obj is ItemEnergy) {
                    spriteFrame = Assets.energy
                } else if (obj is ItemHearth) {
                    spriteFrame = Assets.hearth
                } else if (obj is ItemCandyJelly) {
                    spriteFrame = Assets.jellyRed
                } else if (obj is ItemCandyBean) {
                    spriteFrame = Assets.beanRed
                } else if (obj is ItemCandyCorn) {
                    spriteFrame = Assets.candyCorn
                }
            } else {
                spriteFrame = if (obj is ItemCandyJelly) {
                    Assets.candyExplosionRed!!.getKeyFrame(obj.stateTime, false)
                } else if (obj is ItemCandyBean) {
                    Assets.candyExplosionRed!!.getKeyFrame(obj.stateTime, false)
                } else {
                    Assets.pickAnimation!!.getKeyFrame(obj.stateTime, false)
                }
            }

            if (spriteFrame != null) {
                spriteFrame.setPosition(obj.position.x - obj.width / 2f, obj.position.y - obj.height / 2f)
                spriteFrame.setSize(obj.width, obj.height)
                spriteFrame.draw(batcher)
            }
        }
    }

    private fun renderPlatforms() {
        for (obj in myWorld.arrayPlatforms) {
            var spriteFrame = Assets.platform

            spriteFrame!!.setPosition(
                obj.position.x - Platform.WIDTH / 2f,
                obj.position.y - Platform.HEIGHT / 2f
            )
            spriteFrame.setSize(Platform.WIDTH, Platform.HEIGHT)
            spriteFrame.draw(batcher)
        }
    }

    private fun renderPet() {
        val myPet = myWorld.myPet

        val spriteFrame: Sprite

        var width = myPet!!.drawWidth
        var height = myPet.drawHeight

        if (myPet.petType == Pet.PetType.BOMB) {
            spriteFrame = Assets.PetBombFlyAnimation!!.getKeyFrame(myPet.stateTime, true)
        } else {
            if (myWorld.myPlayer!!.isDash) {
                spriteFrame = Assets.Pet1DashAnimation!!.getKeyFrame(myPet.stateTime, true)
                width = myPet.dashDrawWidth
                height = myPet.dashDrawHeight
            } else spriteFrame = Assets.Pet1FlyAnimation!!.getKeyFrame(myPet.stateTime, true)
        }

        spriteFrame.setPosition(myPet.position.x - width + Pet.RADIUS, myWorld.myPet!!.position.y - height / 2f)
        spriteFrame.setSize(width, height)
        spriteFrame.draw(batcher)
    }

    private fun renderWall() {
        for (obj in myWorld.arrayWall) {
            val spriteFrame = Assets.wall
            spriteFrame!!.setPosition(obj.position.x - Wall.WIDTH / 2f, obj.position.y - Wall.HEIGHT / 2f)
            spriteFrame.setSize(Wall.WIDTH, Wall.HEIGHT)
            spriteFrame.draw(batcher)
        }
    }

    private fun renderObstacles(delta: Float) {
        for (obj in myWorld.arrayObstacles) {
            if (obj.state == Obstacle.STATE_NORMAL) {
                var width: Float
                var height: Float
                var spriteFrame: Sprite?

                if (obj is ObstacleBoxes4) {
                    width = ObstacleBoxes4.DRAW_WIDTH
                    height = ObstacleBoxes4.DRAW_HEIGHT
                    spriteFrame = Assets.boxes4
                } else {
                    width = ObstacleBoxes7.DRAW_WIDTH
                    height = ObstacleBoxes7.DRAW_HEIGHT
                    spriteFrame = Assets.boxes7
                }
                spriteFrame!!.setPosition(obj.position.x - width / 2f, obj.position.y - height / 2f)
                spriteFrame.setSize(width, height)
                spriteFrame.draw(batcher)
            } else {
                if (obj.effect != null) obj.effect!!.draw(batcher, delta)
            }
        }
    }

    private fun renderMissile() {
        for (obj in myWorld.arrayMissiles) {
            var spriteFrame: Sprite
            var width: Float
            var height: Float

            if (obj.state == Missile.STATE_NORMAL) {
                width = Missile.WIDTH
                height = Missile.HEIGHT
                spriteFrame = Assets.missileAnimation!!.getKeyFrame(obj.stateTime, true)
            } else if (obj.state == Missile.STATE_EXPLODE) {
                width = 1f
                height = .84f
                spriteFrame = Assets.explosion!!.getKeyFrame(obj.stateTime, false)
            } else continue

            spriteFrame.setPosition(obj.position.x - width / 2f, obj.position.y - height / 2f)
            spriteFrame.setSize(width, height)
            spriteFrame.draw(batcher)
        }
    }

    private fun renderPlayer() {
        val oPer = myWorld.myPlayer

        val spriteFrame: Sprite
        val offsetY: Float

        val animIdle: AnimationSprite?
        val animJump: AnimationSprite?
        val animRun: AnimationSprite?
        val animSlide: AnimationSprite?
        val animDash: AnimationSprite?
        val animHurt: AnimationSprite?
        val animDizzy: AnimationSprite?
        val animDead: AnimationSprite?

        when (oPer!!.type) {
            Player.TYPE_GIRL, Player.TYPE_BOY -> {
                animIdle = Assets.playerIdleAnimation
                animJump = Assets.playerJumpAnimation
                animRun = Assets.playerRunAnimation
                animSlide = Assets.playerSlideAnimation
                animDash = Assets.playerDashAnimation
                animHurt = Assets.playerHurtAnimation
                animDizzy = Assets.playerDizzyAnimation
                animDead = Assets.playerDeadAnimation
            }

            Player.TYPE_NINJA -> {
                animIdle = Assets.ninjaIdleAnimation
                animJump = Assets.ninjaJumpAnimation
                animRun = Assets.ninjaRunAnimation
                animSlide = Assets.ninjaSlideAnimation
                animDash = Assets.ninjaDashAnimation
                animHurt = Assets.ninjaHurtAnimation
                animDizzy = Assets.ninjaDizzyAnimation
                animDead = Assets.ninjaDeadAnimation
            }

            else -> {
                animIdle = Assets.ninjaIdleAnimation
                animJump = Assets.ninjaJumpAnimation
                animRun = Assets.ninjaRunAnimation
                animSlide = Assets.ninjaSlideAnimation
                animDash = Assets.ninjaDashAnimation
                animHurt = Assets.ninjaHurtAnimation
                animDizzy = Assets.ninjaDizzyAnimation
                animDead = Assets.ninjaDeadAnimation
            }
        }
        if (oPer.state == Player.STATE_NORMAL) {
            spriteFrame = if (oPer.isIdle) {
                animIdle!!.getKeyFrame(oPer.stateTime, true)
            } else if (oPer.isJumping) {
                animJump!!.getKeyFrame(oPer.stateTime, false)
            } else if (oPer.isSlide) {
                animSlide!!.getKeyFrame(oPer.stateTime, true)
            } else if (oPer.isDash) {
                animDash!!.getKeyFrame(oPer.stateTime, true)
            } else {
                animRun!!.getKeyFrame(oPer.stateTime, true)
            }
            offsetY = .1f
        } else if (oPer.state == Player.STATE_HURT) {
            spriteFrame = animHurt!!.getKeyFrame(oPer.stateTime, false)
            offsetY = .1f
        } else if (oPer.state == Player.STATE_DIZZY) {
            spriteFrame = animDizzy!!.getKeyFrame(oPer.stateTime, true)
            offsetY = .1f
        } else {
            spriteFrame = animDead!!.getKeyFrame(oPer.stateTime, false)
            offsetY = .1f
        }

        spriteFrame.setPosition(myWorld.myPlayer!!.position.x - .75f, myWorld.myPlayer!!.position.y - .52f - offsetY)
        spriteFrame.setSize(Player.DRAW_WIDTH, Player.DRAW_HEIGHT)
        spriteFrame.draw(batcher)
    }
}
