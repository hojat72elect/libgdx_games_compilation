package com.nopalsoft.slamthebird

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.nopalsoft.slamthebird.game.GameScreen
import com.nopalsoft.slamthebird.screens.Screens

class MainSlamBird : Game() {
    @JvmField
    var stage: Stage? = null
    @JvmField
    var batcher: SpriteBatch? = null

    override fun create() {
        stage = Stage(StretchViewport(Screens.SCREEN_WIDTH.toFloat(), Screens.SCREEN_HEIGHT.toFloat()))

        batcher = SpriteBatch()
        Assets.load()
        Achievements.init()

        setScreen(GameScreen(this))
    }
}