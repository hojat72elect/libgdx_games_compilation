package com.nopalsoft.invaders.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.nopalsoft.invaders.Assets
import com.nopalsoft.invaders.Assets.boost1
import com.nopalsoft.invaders.Assets.boost2
import com.nopalsoft.invaders.Assets.boost3
import com.nopalsoft.invaders.Assets.btFireDown
import com.nopalsoft.invaders.Assets.btMissil
import com.nopalsoft.invaders.Assets.btMissilDown
import com.nopalsoft.invaders.Assets.clickHelp
import com.nopalsoft.invaders.Assets.clickSound
import com.nopalsoft.invaders.Assets.font10
import com.nopalsoft.invaders.Assets.font15
import com.nopalsoft.invaders.Assets.font45
import com.nopalsoft.invaders.Assets.getTextWidth
import com.nopalsoft.invaders.Assets.help1
import com.nopalsoft.invaders.Assets.languages
import com.nopalsoft.invaders.Assets.missile
import com.nopalsoft.invaders.Assets.parallaxBackground
import com.nopalsoft.invaders.Assets.playSound
import com.nopalsoft.invaders.Assets.recuadroInGameStatus
import com.nopalsoft.invaders.Assets.ship
import com.nopalsoft.invaders.Assets.styleDialogPause
import com.nopalsoft.invaders.Assets.styleImageButtonPause
import com.nopalsoft.invaders.Assets.styleLabel
import com.nopalsoft.invaders.Assets.styleLabelDialog
import com.nopalsoft.invaders.Assets.styleTextButton
import com.nopalsoft.invaders.Assets.styleTextButtonFacebook
import com.nopalsoft.invaders.Assets.upgLife
import com.nopalsoft.invaders.MainInvaders
import com.nopalsoft.invaders.Settings.addScore
import com.nopalsoft.invaders.Settings.numberOfTimesGameHasBeenPlayed
import com.nopalsoft.invaders.Settings.tiltControlEnabled
import com.nopalsoft.invaders.screens.MainMenuScreen
import com.nopalsoft.invaders.screens.Screens

class GameScreen(game: MainInvaders) : Screens(game) {
    val GAME_TUTORIAL: Int = 4

    var screenTutorial: Int = 0 // If it is on screen 1 or 2 of the tutorial
    var oWorld: World
    var renderer: WorldRenderer
    var isItShotItself: Boolean = false
    var didItFireMissile: Boolean = false
    var touchPoint: Vector3
    var leftButton: Rectangle
    var rightButton: Rectangle
    var dialogPause: Dialog
    var dialogGameOver: Dialog
    var scoresBar: Table
    var lbLevel: Label
    var lbScore: Label
    var lbNumVidas: Label
    var btPause: ImageButton
    var btLeft: ImageButton
    var btRight: ImageButton
    var btFire: ImageButton
    var buttonMissile: TextButton
    var groupTutorial: Group? = null
    var labelTiltYourDevice: Label? = null
    var accel: Float

    var level: Int
    var rotation: Float = 0f
    var addRotation: Float = .3f

    init {
        numberOfTimesGameHasBeenPlayed = numberOfTimesGameHasBeenPlayed + 1
        state = GAME_READY
        if (numberOfTimesGameHasBeenPlayed < 3) { // It will be shown 2 times, time zero and time 1.
            state = GAME_TUTORIAL
            screenTutorial = 0
            setUpTutorial()
        }
        touchPoint = Vector3()

        oWorld = World()
        renderer = WorldRenderer(batcher!!, oWorld)
        leftButton = Rectangle(0f, 0f, 160f, 480f)
        rightButton = Rectangle(161f, 0f, 160f, 480f)

        // OnScreen Controls
        accel = 0f
        level = oWorld.currentLevel
        btLeft = ImageButton(Assets.btLeft)
        btLeft.setSize(65f, 50f)
        btLeft.setPosition(10f, 5f)
        btLeft.addListener(object : ClickListener() {
            override fun enter(event: InputEvent, x: Float, y: Float, pointer: Int, fromActor: Actor) {
                accel = 5f
            }

            override fun exit(event: InputEvent, x: Float, y: Float, pointer: Int, toActor: Actor) {
                accel = 0f
                super.exit(event, x, y, pointer, toActor)
            }
        })
        btRight = ImageButton(Assets.btRight)
        btRight.setSize(65f, 50f)
        btRight.setPosition(85f, 5f)
        btRight.addListener(object : ClickListener() {
            override fun enter(event: InputEvent, x: Float, y: Float, pointer: Int, fromActor: Actor) {
                accel = -5f
            }

            override fun exit(event: InputEvent, x: Float, y: Float, pointer: Int, toActor: Actor) {
                accel = 0f
                super.exit(event, x, y, pointer, toActor)
            }
        })

        buttonMissile =
            TextButton(oWorld.missileCount.toString() + "", TextButtonStyle(btMissil, btMissilDown, null, font10))
        buttonMissile.label.color = Color.GREEN
        buttonMissile.setSize(60f, 60f)
        buttonMissile.setPosition((SCREEN_WIDTH - 5 - 60 - 20 - 60).toFloat(), 5f)
        buttonMissile.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                didItFireMissile = true
            }
        })
        btFire = ImageButton(Assets.btFire, btFireDown)
        btFire.setSize(60f, 60f)
        btFire.setPosition((SCREEN_WIDTH - 60 - 5).toFloat(), 5f)
        btFire.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                isItShotItself = true
            }
        })

        // End OnScreen Controls
        // Start dialog Pause
        dialogPause = Dialog(languages!!["game_paused"], styleDialogPause)

        val btContinue = TextButton(languages!!["continue"], styleTextButton)
        val btMenu = TextButton(languages!!["main_menu"], styleTextButton)

        btContinue.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                playSound(clickSound!!)
                state = GAME_RUNNING
                oWorld.state = World.STATE_RUNNING
                dialogPause.hide()
            }
        })

        btMenu.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                playSound(clickSound!!)
                game.screen = MainMenuScreen(game)
                dialogPause.hide()
            }
        })

        dialogPause.buttonTable.pad(15f)
        dialogPause.buttonTable.add(btContinue).minWidth(160f).minHeight(40f).expand().padBottom(20f)
        dialogPause.buttonTable.row()
        dialogPause.buttonTable.add(btMenu).minWidth(160f).minHeight(40f).expand()

        // Inicio dialogGameOver
        dialogGameOver = Dialog("Game Over", styleDialogPause)

        val btTryAgain = TextButton(languages!!["try_again"], styleTextButton)
        val btMenu2 = TextButton(languages!!["main_menu"], styleTextButton)
        val btShare = TextButton(languages!!["share"], styleTextButtonFacebook)

        btTryAgain.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                playSound(clickSound!!)
                game.screen = GameScreen(game)
                dialogGameOver.hide()
            }
        })

        btMenu2.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                playSound(clickSound!!)
                game.screen = MainMenuScreen(game)
                dialogGameOver.hide()
            }
        })
        btShare.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                val text =
                    languages!!.format("i_just_score_n_points_playing_droid_invaders_can_you_beat_me", oWorld.score)
                Gdx.app.log("Share text", text)
                playSound(clickSound!!)
            }
        })

        dialogGameOver.buttonTable.pad(15f)
        dialogGameOver.buttonTable.add(btTryAgain).minWidth(160f).minHeight(40f).expand().padBottom(20f)
        dialogGameOver.buttonTable.row()
        dialogGameOver.buttonTable.add(btMenu2).minWidth(160f).minHeight(40f).expand()
        dialogGameOver.buttonTable.row()


        val lbShare = Label(languages!!["share_your_score_on_facebook"], styleLabelDialog)
        lbShare.setAlignment(Align.center)
        lbShare.wrap = true
        dialogGameOver.buttonTable.add(lbShare).width(200f).expand()
        dialogGameOver.buttonTable.row()
        dialogGameOver.buttonTable.add(btShare).expand()


        if (numberOfTimesGameHasBeenPlayed % 5 == 0) {
            game.dialogs!!.showDialogRate()
        }

        btPause = ImageButton(styleImageButtonPause)
        btPause.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                setPaused()
            }
        })

        lbLevel = Label(languages!!["level"] + " " + oWorld.currentLevel, styleLabel)
        lbScore = Label(languages!!["score"] + " " + oWorld.score, styleLabel)
        lbNumVidas = Label("x" + oWorld.myShip.lives, styleLabel)
        val imVida = Image(ship)

        scoresBar = Table()
        scoresBar.background = recuadroInGameStatus
        scoresBar.width = SCREEN_WIDTH.toFloat()
        scoresBar.height = 30f
        scoresBar.setPosition(0f, (SCREEN_HEIGHT - 30).toFloat())

        scoresBar.add(lbLevel).left()

        scoresBar.add(lbScore).center().expandX()

        scoresBar.add(imVida).size(20f).right()
        scoresBar.add(lbNumVidas).right()
        scoresBar.add(btPause).size(26f).right().padLeft(8f)

        // scoresBar.debug();
        stage!!.addActor(scoresBar)
    }

    private fun setUpTutorial() {
        labelTiltYourDevice =
            Label(languages!!["tilt_your_device_to_move_horizontally"], LabelStyle(font45, Color.GREEN))
        labelTiltYourDevice!!.wrap = true
        labelTiltYourDevice!!.setAlignment(Align.center)
        labelTiltYourDevice!!.setPosition(0f, 120f)
        labelTiltYourDevice!!.width = SCREEN_WIDTH.toFloat()
        stage!!.addActor(labelTiltYourDevice)

        groupTutorial = Group()

        // gpTutorial.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        val boostTable = Table()
        groupTutorial!!.addActor(boostTable)

        val vida = Image(upgLife)
        val boostBomba = Image(boost2)
        val boostEscudo = Image(boost3)
        val boostUpgradeWeapon = Image(boost1)

        val lblVida = Label(languages!!["get_one_extra_life"], styleLabel)
        val lblBomba = Label(languages!!["get_one_extra_missil"], styleLabel)
        val lblShield = Label(languages!!["get_a_shield"], styleLabel)
        val lblUpgradeWeapn = Label(languages!!["upgrade_your_weapon"], styleLabel)

        boostTable.setPosition(0f, 340f)
        boostTable.width = SCREEN_WIDTH.toFloat()

        val iconSize = 40
        boostTable.add(vida).size(iconSize.toFloat())
        boostTable.add(lblVida).padLeft(15f).left()
        boostTable.row().padTop(10f)
        boostTable.add(boostBomba).size(iconSize.toFloat())
        boostTable.add(lblBomba).padLeft(15f).left()
        boostTable.row().padTop(10f)
        boostTable.add(boostEscudo).size(iconSize.toFloat())
        boostTable.add(lblShield).padLeft(15f).left()
        boostTable.row().padTop(10f)
        boostTable.add(boostUpgradeWeapon).size(iconSize.toFloat())
        boostTable.add(lblUpgradeWeapn).padLeft(15f).left()
        val touchLeft =
            Label(languages!!["touch_left_side_to_fire_missils"], styleLabel)
        touchLeft.wrap = true
        touchLeft.width = 160f
        touchLeft.setAlignment(Align.center)
        touchLeft.setPosition(0f, 50f)

        val touchRight = Label(languages!!["touch_right_side_to_fire"], styleLabel)
        touchRight.wrap = true
        touchRight.width = 160f
        touchRight.setAlignment(Align.center)
        touchRight.setPosition(165f, 50f)

        groupTutorial!!.addActor(touchRight)
        groupTutorial!!.addActor(touchLeft)
    }

    override fun update(deltaTime: Float) {
        // if (deltaTime > 0.1f) deltaTime = 0.1f;
        when (state) {
            GAME_TUTORIAL -> updateTutorial()
            GAME_READY -> updateReady()
            GAME_RUNNING -> updateRunning(deltaTime)
        }
    }

    private fun updateTutorial() {
        if (Gdx.input.justTouched()) {
            if (screenTutorial == 0) {
                screenTutorial++
                labelTiltYourDevice!!.remove()
                stage!!.addActor(groupTutorial)
            } else {
                state = GAME_READY
                groupTutorial!!.remove()
            }
        }
    }

    private fun updateReady() {
        if (Gdx.input.justTouched() && !game.dialogs!!.isDialogShown) {
            state = GAME_RUNNING

            if (!tiltControlEnabled) {
                stage!!.addActor(btLeft)
                stage.addActor(btRight)
                stage.addActor(buttonMissile)
                stage.addActor(btFire)
            }
        }
    }

    private fun updateRunning(deltaTime: Float) {
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT) || Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(
                Input.Keys.DPAD_RIGHT
            ) || Gdx.input.isKeyPressed(Input.Keys.D)
        ) {
            accel = 0f
            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) accel = 5f
            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) accel = -5f

            oWorld.update(deltaTime, accel, isItShotItself, didItFireMissile)
        } else if (tiltControlEnabled) {
            if (Gdx.input.justTouched()) {
                myCamera.unproject(touchPoint.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))

                if (leftButton.contains(touchPoint.x, touchPoint.y)) {
                    didItFireMissile = true
                }
                if (rightButton.contains(touchPoint.x, touchPoint.y)) {
                    isItShotItself = true
                }
            }
            oWorld.update(deltaTime, Gdx.input.accelerometerX, isItShotItself, didItFireMissile)
        } else {
            oWorld.update(deltaTime, accel, isItShotItself, didItFireMissile)
        }

        if (level != oWorld.currentLevel) {
            level = oWorld.currentLevel
            lbLevel.setText(languages!!["level"] + " " + level)
        }

        lbScore.setText(languages!!["score"] + " " + oWorld.score)
        lbNumVidas.setText("x" + oWorld.myShip.lives)

        if (oWorld.state == World.STATE_GAME_OVER) {
            state = GAME_OVER
            dialogGameOver.show(stage)
        }

        buttonMissile.setText(oWorld.missileCount.toString() + "")

        isItShotItself = false
        didItFireMissile = false
    }

    private fun setPaused() {
        playSound(clickSound!!)
        state = GAME_PAUSE
        oWorld.state = World.STATE_PAUSED
        dialogPause.show(stage)
    }

    override fun draw(delta: Float) {
        if (state != GAME_TUTORIAL) renderer.render(delta)
        else parallaxBackground!!.render(delta)
        myCamera.update()
        batcher!!.projectionMatrix = myCamera.combined
        batcher.enableBlending()
        batcher.begin()

        when (state) {
            GAME_TUTORIAL -> presentTurorial()
            GAME_READY -> presentReady()
            GAME_RUNNING -> presentRunning()
        }
        batcher.end()
    }

    private fun presentTurorial() {
        if (screenTutorial == 0 && tiltControlEnabled) {
            if (rotation < -20 || rotation > 20) addRotation *= -1f
            rotation += addRotation
            batcher!!.draw(help1, SCREEN_WIDTH / 2f - 51, 190f, 51f, 0f, 102f, 200f, 1f, 1f, rotation)
        } else {
            batcher!!.draw(clickHelp, 155f, 0f, 10f, 125f)
        }
    }

    private fun presentReady() {
        val touchToStart = languages!!["touch_to_start"]
        val textWidth = getTextWidth(font45, touchToStart)
        font45!!.draw(batcher, touchToStart, (SCREEN_WIDTH / 2f) - (textWidth / 2f), 220f)
    }

    private fun presentRunning() {
        if (oWorld.missileCount > 0 && tiltControlEnabled) {
            batcher!!.draw(missile!!.getKeyFrame(0f), 1f, 1f, 8f, 28f)
            font15!!.draw(batcher, "X" + oWorld.missileCount, 10f, 25f)
        }
    }

    override fun hide() {
        addScore(oWorld.score)
        super.hide()
    }

    override fun pause() {
        setPaused()
        super.pause()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
            playSound(clickSound!!)
            if (state == GAME_RUNNING) {
                setPaused()
                return true
            } else if (state == GAME_PAUSE) {
                game.screen = MainMenuScreen(game)
                return true
            }
        } else if (keycode == Input.Keys.MENU) {
            setPaused()
            return true
        } else if (keycode == Input.Keys.SPACE) {
            isItShotItself = true

            return true
        } else if (keycode == Input.Keys.ALT_LEFT) {
            didItFireMissile = true
            return true
        }
        return false
    }

    companion object {
        const val GAME_RUNNING: Int = 1
        const val GAME_PAUSE: Int = 3
        const val GAME_READY: Int = 0
        const val GAME_OVER: Int = 2
        var state =0
    }
}
