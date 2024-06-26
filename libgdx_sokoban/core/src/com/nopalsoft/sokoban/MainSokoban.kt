package com.nopalsoft.sokoban

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.nopalsoft.sokoban.screens.MainMenuScreen
import com.nopalsoft.sokoban.screens.Screens

class MainSokoban : Game() {
   lateinit var stage: Stage
    var batcher: SpriteBatch? = null

    override fun create() {
        stage = Stage(
            StretchViewport(
                Screens.SCREEN_WIDTH,
                Screens.SCREEN_HEIGHT
            )
        )
        batcher = SpriteBatch()

        Assets.load()
        Settings.load()
        setScreen(MainMenuScreen(this))
    }
}