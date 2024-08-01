package com.nopalsoft.invaders.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.nopalsoft.invaders.Assets
import com.nopalsoft.invaders.MainInvaders
import com.nopalsoft.invaders.Settings
import com.nopalsoft.invaders.game.GameScreen

abstract class Screens(val game: MainInvaders) : InputAdapter(), Screen {

    val myCamera = OrthographicCamera(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
    val batcher = game.spriteBatch
    val stage = game.stage

    init {
        stage?.clear()
        myCamera.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0f)
        val input = InputMultiplexer(this, stage)
        Gdx.input.inputProcessor = input

        Assets.font10?.data?.setScale(.65f)
        Assets.font15?.data?.setScale(1f)
        Assets.font45?.data?.setScale(.85f)
        Assets.font60?.data?.setScale(1.2f)

        when (this) {
            is MainMenuScreen -> {
                Assets.font10?.data?.setScale(.65f)
                Assets.font15?.data?.setScale(1f)
                Assets.font45?.data?.setScale(.85f)
                Assets.font60?.data?.setScale(1.2f)
            }

            is GameScreen -> {
                Assets.font15?.data?.setScale(1f)
                Assets.font45?.data?.setScale(.7f)
                Assets.font10?.data?.setScale(.65f)
            }

            is SettingsScreen -> {
                Assets.font10?.data?.setScale(1f)
                Assets.font15?.data?.setScale(1f)
                Assets.font45?.data?.setScale(.65f)
                Assets.font60?.data?.setScale(1f)
            }
        }


    }

    override fun render(delta: Float) {
        update(delta)

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        draw(delta)

        stage?.act(delta)
        stage?.draw()
    }

    abstract fun draw(delta: Float)

    abstract fun update(delta: Float)

    override fun resize(width: Int, height: Int) {
        stage?.viewport?.update(width, height, true)
    }

    override fun show() {
    }

    override fun hide() {
        Settings.save()
    }

    override fun pause() {
        Assets.music?.pause()
    }

    override fun resume() {
        if (Settings.musicEnabled && Assets.music!!.isPlaying.not()) Assets.music!!.play()
    }

    override fun dispose() {
        stage?.dispose()
        batcher?.dispose()
    }


    companion object {
        const val SCREEN_WIDTH = 320
        const val SCREEN_HEIGHT = 480

        const val WORLD_SCREEN_WIDTH = 32
        const val WORLD_SCREEN_HEIGHT = 48
    }
}