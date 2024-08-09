package com.nopalsoft.slamthebird.scene2d

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.slamthebird.Assets
import com.nopalsoft.slamthebird.game.GameScreen
import com.nopalsoft.slamthebird.screens.Screens

class WindowPause(currentScreen: Screens) : Window(currentScreen) {
    var gameScreen: GameScreen

    init {
        setSize(350f, 260f)
        y = 300f
        setBackGround()

        gameScreen = currentScreen as GameScreen

        val lbTitle = Label("Paused", Assets.labelStyleSmall)
        lbTitle.setPosition(width / 2f - lbTitle.width / 2f, 210f)

        val btResume = TextButton(
            "Resume",
            Assets.textButtonStylePurchased
        )
        screen.addEfectoPress(btResume)
        btResume.setSize(150f, 50f)
        btResume.setPosition(width / 2f - btResume.width / 2f, 130f)
        btResume.label.wrap = true
        btResume.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                hide()
                gameScreen.setRunningFromPaused()
            }
        })

        val btMainMenu = TextButton(
            "Menu",
            Assets.textButtonStylePurchased
        )
        screen.addEfectoPress(btMainMenu)
        btMainMenu.setSize(150f, 50f)
        btMainMenu.setPosition(width / 2f - btResume.width / 2f, 40f)
        btMainMenu.label.wrap = true
        btMainMenu.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                hide()
                screen.changeScreenWithFadeOut(GameScreen::class.java, game)
            }
        })

        addActor(btResume)
        addActor(btMainMenu)
        addActor(lbTitle)
    }
}
