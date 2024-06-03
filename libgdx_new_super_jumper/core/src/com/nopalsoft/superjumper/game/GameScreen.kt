package com.nopalsoft.superjumper.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.superjumper.Assets
import com.nopalsoft.superjumper.MainSuperJumper
import com.nopalsoft.superjumper.Settings
import com.nopalsoft.superjumper.scene2d.BaseScreenGameOver
import com.nopalsoft.superjumper.scene2d.PauseScreen
import com.nopalsoft.superjumper.screens.BasicScreen

class GameScreen(game: MainSuperJumper) : BasicScreen(game) {

    var myWorldGame = WorldGame()
    private val renderer = WorldGameRender(batcher, myWorldGame)
    private val touchPositionWorldCoords = Vector3()
    private var didFire: Boolean = false

    private val labelDistance = Label("", Assets.labelStyleBig)
    private val labelCoins = Label("", Assets.labelStyleBig)
    private val labelBullets = Label("", Assets.labelStyleBig)

    private val buttonPause = Button(Assets.buttonPause)

    private val pauseScreen = PauseScreen(this)
    private val menuMarker = Table()


    init {
        state = STATE_RUNNING
        Settings.numberOfTimesPlayed++

        menuMarker.setSize(SCREEN_WIDTH, 40f)
        menuMarker.y = SCREEN_HEIGHT - menuMarker.height

        menuMarker.add(Image(TextureRegionDrawable(Assets.coin))).left().padLeft(5f)

        menuMarker.add(labelCoins).left()

        menuMarker.add(labelDistance).center().expandX()

        menuMarker.add(Image(TextureRegionDrawable(Assets.gun))).height(45f).width(30f).left()
        menuMarker.add(labelBullets).left().padRight(5f)

        buttonPause.setSize(35f, 35f)
        buttonPause.setPosition(SCREEN_WIDTH - 40, SCREEN_HEIGHT - 80)
        addPressEffect(buttonPause)
        buttonPause.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                setPaused()
            }
        })

        stage.addActor(menuMarker)
        stage.addActor(buttonPause)
    }


    override fun update(delta: Float) {
        when (state) {
            STATE_RUNNING -> updateRunning(delta)
            STATE_GAME_OVER -> updateGameOver(delta)
        }
    }


    private fun updateRunning(delta: Float) {
        var accelarationX = -(Gdx.input.accelerometerX / 3f)

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            accelarationX = -1f
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            accelarationX = 1f
        }

        myWorldGame.update(delta, accelarationX, didFire, touchPositionWorldCoords)

        labelCoins.setText("x" + myWorldGame.coins)
        labelDistance.setText("Score " + myWorldGame.maxDistance)
        labelBullets.setText("x" + Settings.numBullets)

        if (myWorldGame.state == WorldGame.STATE_GAMEOVER) {
            setGameover()
        }

        didFire = false
    }

    private fun updateGameOver(delta: Float) {
        myWorldGame.update(delta, 0f, false, touchPositionWorldCoords)
    }

    override fun draw(delta: Float) {
        batcher.begin()
        batcher.draw(Assets.background, 0f, 0f, SCREEN_WIDTH, SCREEN_HEIGHT)
        batcher.end()

        if (state != STATE_PAUSED) {
            renderer.render()
        }
    }

    private fun setPaused() {
        if (state == STATE_RUNNING) {
            state = STATE_PAUSED
            pauseScreen.show(stage)
        }
    }

    fun setRunning() {
        state = STATE_RUNNING
    }

    private fun setGameover() {
        state = STATE_GAME_OVER
        Settings.changeBestScore(myWorldGame.maxDistance)
        BaseScreenGameOver(this).show(stage)
    }


    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        touchPositionWorldCoords.set(
            screenX.toFloat(),
            0f,
            0f
        ) // Always as if I had touched the highest part of the screen.
        renderer.unProjectToWorldCoords(touchPositionWorldCoords)

        didFire = true
        return false
    }


    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            if (pauseScreen.isVisible) {
                pauseScreen.hide()
            } else {
                setPaused()
            }
            return true
        }
        return super.keyDown(keycode)
    }


    companion object {
        private const val STATE_RUNNING = 2
        private const val STATE_PAUSED = 3
        private const val STATE_GAME_OVER = 4
        private var state = 0
    }
}