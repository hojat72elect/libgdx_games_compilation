package com.nopalsoft.slamthebird.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.nopalsoft.slamthebird.Assets
import com.nopalsoft.slamthebird.MainSlamBird
import com.nopalsoft.slamthebird.Settings.save
import com.nopalsoft.slamthebird.game.GameScreen
import com.nopalsoft.slamthebird.shop.ShopScreen

abstract class Screens(game: MainSlamBird) : InputAdapter(), Screen {
    @JvmField
    var game: MainSlamBird

    @JvmField
    var oCam: OrthographicCamera
    @JvmField
    var batcher: SpriteBatch?
    @JvmField
    var stage: Stage? = game.stage

    var blackFadeOut: Image? = null

    init {
        stage!!.clear()
        this.batcher = game.batcher
        this.game = game

        oCam = OrthographicCamera(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        oCam.position[SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f] = 0f

        val input = InputMultiplexer(this, stage)
        Gdx.input.inputProcessor = input
    }

    override fun render(delta: Float) {
        var delta = delta
        if (delta > .1f) delta = .1f

        update(delta)

        // Gdx.gl.glClearColor(.3f, 0, 1f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        draw(delta)

        stage!!.act(delta)
        stage!!.draw()


        //        stage.setDebugAll(true);
    }

    fun drawNumGrandeCentradoX(x: Float, y: Float, puntuacion: Int) {
        val score = puntuacion.toString()

        val len = score.length
        val charWidth = 42f
        val textWidth = len * charWidth
        for (i in 0 until len) {
            var keyFrame: AtlasRegion?

            val character = score[i]

            keyFrame = if (character == '0') {
                Assets.num0Big
            } else if (character == '1') {
                Assets.num1Big
            } else if (character == '2') {
                Assets.num2Big
            } else if (character == '3') {
                Assets.num3Big
            } else if (character == '4') {
                Assets.num4Big
            } else if (character == '5') {
                Assets.num5Big
            } else if (character == '6') {
                Assets.num6Big
            } else if (character == '7') {
                Assets.num7Big
            } else if (character == '8') {
                Assets.num8Big
            } else { // 9
                Assets.num9Big
            }

            batcher!!.draw(
                keyFrame, x + ((charWidth - 1f) * i) - textWidth / 2f,
                y, charWidth, 64f
            )
        }
    }

    fun drawPuntuacionChicoOrigenDerecha(
        x: Float, y: Float,
        puntuacion: Int
    ) {
        val score = puntuacion.toString()

        val len = score.length
        var charWidth: Float
        var textWidth = 0f
        for (i in len - 1 downTo 0) {
            var keyFrame: AtlasRegion?

            charWidth = 22f
            val character = score[i]

            if (character == '0') {
                keyFrame = Assets.num0Small
            } else if (character == '1') {
                keyFrame = Assets.num1Small
                charWidth = 11f
            } else if (character == '2') {
                keyFrame = Assets.num2Small
            } else if (character == '3') {
                keyFrame = Assets.num3Small
            } else if (character == '4') {
                keyFrame = Assets.num4Small
            } else if (character == '5') {
                keyFrame = Assets.num5Small
            } else if (character == '6') {
                keyFrame = Assets.num6Small
            } else if (character == '7') {
                keyFrame = Assets.num7Small
            } else if (character == '8') {
                keyFrame = Assets.num8Small
            } else { // 9
                keyFrame = Assets.num9Small
            }
            textWidth += charWidth
            batcher!!.draw(keyFrame, x - textWidth, y, charWidth, 32f)
        }
    }

    fun drawNumChicoCentradoX(x: Float, y: Float, puntuacion: Int) {
        val score = puntuacion.toString()

        val len = score.length
        val charWidth = 22f
        val textWidth = len * charWidth
        for (i in 0 until len) {
            var keyFrame: AtlasRegion?

            val character = score[i]

            keyFrame = if (character == '0') {
                Assets.num0Small
            } else if (character == '1') {
                Assets.num1Small
            } else if (character == '2') {
                Assets.num2Small
            } else if (character == '3') {
                Assets.num3Small
            } else if (character == '4') {
                Assets.num4Small
            } else if (character == '5') {
                Assets.num5Small
            } else if (character == '6') {
                Assets.num6Small
            } else if (character == '7') {
                Assets.num7Small
            } else if (character == '8') {
                Assets.num8Small
            } else { // 9
                Assets.num9Small
            }

            batcher!!.draw(
                keyFrame, x + ((charWidth - 1f) * i) - textWidth / 2f,
                y, charWidth, 32f
            )
        }
    }

    fun changeScreenWithFadeOut(
        newScreen: Class<*>,
        game: MainSlamBird
    ) {
        blackFadeOut = Image(Assets.blackPixel)
        blackFadeOut!!.setSize(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        blackFadeOut!!.color.a = 0f
        blackFadeOut!!.addAction(
            Actions.sequence(
                Actions.fadeIn(.5f),
                Actions.run {
                    if (newScreen == GameScreen::class.java) game.screen = GameScreen(game)
                    else if (newScreen == ShopScreen::class.java) game.screen = ShopScreen(game)
                })
        )
        stage!!.addActor(blackFadeOut)
    }

    fun addEfectoPress(actor: Actor) {
        actor.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent, x: Float, y: Float,
                pointer: Int, button: Int
            ): Boolean {
                actor.setPosition(actor.x, actor.y - 5)
                event.stop()
                return true
            }

            override fun touchUp(
                event: InputEvent, x: Float, y: Float,
                pointer: Int, button: Int
            ) {
                actor.setPosition(actor.x, actor.y + 5)
            }
        })
    }

    abstract fun draw(delta: Float)

    abstract fun update(delta: Float)

    override fun resize(width: Int, height: Int) {
        stage!!.viewport.update(width, height, true)
    }

    override fun show() {
    }

    override fun hide() {
        save()
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun dispose() {
        stage!!.dispose()
        batcher!!.dispose()
        save()
    }

    companion object {
        const val SCREEN_WIDTH: Int = 480
        const val SCREEN_HEIGHT: Int = 800

        const val WORLD_SCREEN_WIDTH: Float = 4.8f
        const val WORLD_SCREEN_HEIGHT: Float = 8f
    }
}
