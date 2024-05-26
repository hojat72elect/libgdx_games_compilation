package com.nopalsoft.ninjarunner.screens

import com.badlogic.gdx.Game
import com.badlogic.gdx.Input
import com.nopalsoft.ninjarunner.game.GameScreen

class SettingsScreen(game: Game) : Screens(game) {

    override fun draw(delta: Float) {
        // Nothing is going on in here
    }

    override fun update(delta: Float) {
        // Nothing is going on in here
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            changeScreenWithFadeOut(GameScreen::class.java, game)
            return true
        }
        return super.keyUp(keycode)
    }
}