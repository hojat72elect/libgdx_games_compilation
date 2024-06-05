package com.nopalsoft.superjumper.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.nopalsoft.superjumper.Assets
import com.nopalsoft.superjumper.MainSuperJumper
import com.nopalsoft.superjumper.Settings
import com.nopalsoft.superjumper.game.GameScreen

abstract class BasicScreen(val game: MainSuperJumper) : InputAdapter(), Screen {


    private val oCam = OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT)
    val batcher: SpriteBatch = game.batcher
    val stage: Stage = game.stage
    private var music: Music? = null
    private var blackFadeOut: Image? = null


    init {
        stage.clear()
        oCam.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0f)
        Gdx.input.inputProcessor = InputMultiplexer(this, stage)
    }


    override fun render(delta: Float) {

        val optimizedDelta = if (delta > .1f) {
            .1f
        } else {
            delta
        }


        update(optimizedDelta)
        stage.act(optimizedDelta)

        oCam.update()
        batcher.projectionMatrix = oCam.combined

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        draw(optimizedDelta)
        stage.draw()
    }

    fun addPressEffect(actor: Actor) {
        actor.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {
                actor.setPosition(actor.x, actor.y - 5)
                event.stop()
                return true
            }

            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                actor.setPosition(actor.x, actor.y + 5)
            }
        })
    }


    fun changeScreenWithFadeOut(newScreen: Class<*>, game: MainSuperJumper) {
        blackFadeOut = Image(Assets.pixelBlack)
        blackFadeOut?.setSize(SCREEN_WIDTH, SCREEN_HEIGHT)
        blackFadeOut?.color?.a = 0f
        blackFadeOut?.addAction(Actions.sequence(Actions.fadeIn(.5f), Actions.run {
            if (newScreen == GameScreen::class.java) {
                game.screen = GameScreen(game)
            } else if (newScreen == MainMenuScreen::class.java) {
                game.screen = MainMenuScreen(game)
            }
        }))

        val lbl = Label("Loading..", Assets.labelStyleBig)
        lbl.setPosition(
            SCREEN_WIDTH / 2f - lbl.width / 2f,
            SCREEN_HEIGHT / 2f - lbl.height / 2f
        )
        lbl.color.a = 0f
        lbl.addAction(Actions.fadeIn(.6f))

        stage.addActor(blackFadeOut)
        stage.addActor(lbl)
    }


    abstract fun update(delta: Float)

    abstract fun draw(delta: Float)

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun show() {
        // Nothing is happening here
    }

    override fun hide() {
        if (music != null) {
            music!!.stop()
            music!!.dispose()
            music = null
        }

        Settings.save()
    }

    override fun pause() {
        // Nothing happens here.
    }

    override fun resume() {
        // Nothing happens here.
    }

    override fun dispose() {
        batcher.dispose()
    }

    companion object {

        const val SCREEN_WIDTH = 480f
        const val SCREEN_HEIGHT = 800f

        const val WORLD_WIDTH = 4.8f
        const val WORLD_HEIGHT = 8f

    }
}