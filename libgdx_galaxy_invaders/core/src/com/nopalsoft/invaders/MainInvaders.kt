package com.nopalsoft.invaders

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.nopalsoft.invaders.screens.MainMenuScreen
import com.nopalsoft.invaders.screens.Screens

class MainInvaders : Game() {

    var stage: Stage? = null
    var spriteBatch: SpriteBatch? = null
    var dialogs: DialogSingInGGS? = null

    override fun create() {
        stage =
            Stage(StretchViewport(Screens.SCREEN_WIDTH.toFloat(), Screens.SCREEN_HEIGHT.toFloat()))
        spriteBatch = SpriteBatch()
        dialogs = DialogSingInGGS(this, stage!!)

        Assets.load()
        setScreen(MainMenuScreen(this)) // Here I have to put the main thing
    }

    override fun dispose() {
        super.dispose()
        getScreen().dispose()
    }
}
