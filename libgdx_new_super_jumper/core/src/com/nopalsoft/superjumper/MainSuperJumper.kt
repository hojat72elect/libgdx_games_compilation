package com.nopalsoft.superjumper

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.nopalsoft.superjumper.screens.BasicScreen
import com.nopalsoft.superjumper.screens.MainMenuScreen

class MainSuperJumper : Game() {
    lateinit var stage: Stage
    lateinit var batcher: SpriteBatch

    override fun create() {
        stage = Stage(StretchViewport(BasicScreen.SCREEN_WIDTH, BasicScreen.SCREEN_HEIGHT))
        batcher = SpriteBatch()
        Settings.load()
        Assets.load()
        setScreen(MainMenuScreen(this))
    }
}