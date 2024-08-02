package com.nopalsoft.ninjarunner

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.nopalsoft.ninjarunner.Settings.load
import com.nopalsoft.ninjarunner.game.GameScreen
import com.nopalsoft.ninjarunner.leaderboard.Person
import com.nopalsoft.ninjarunner.screens.Screens

/**
 * It has already been made in KT. Don't do it again.
 */
class MainGame : Game() {
    var arrayOfPersons: Array<Person> = Array()
    var stage: Stage? = null
    var batcher: SpriteBatch? = null
    var languages: I18NBundle? = null

    override fun create() {
        languages = I18NBundle.createBundle(Gdx.files.internal("strings/strings"))
        batcher = SpriteBatch()
        stage = Stage(StretchViewport(Screens.SCREEN_WIDTH.toFloat(), Screens.SCREEN_HEIGHT.toFloat()))

        load()
        Assets.load()
        setScreen(GameScreen(this, true))
    }
}
