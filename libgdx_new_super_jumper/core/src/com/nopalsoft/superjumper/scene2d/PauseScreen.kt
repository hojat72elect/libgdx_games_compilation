package com.nopalsoft.superjumper.scene2d

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.nopalsoft.superjumper.Assets
import com.nopalsoft.superjumper.game.GameScreen
import com.nopalsoft.superjumper.screens.MainMenuScreen

class PauseScreen(currentScreen: GameScreen) : BaseScreen(currentScreen, 350f, 280f, 300f) {

    private var btMenu: TextButton? = null
    private var btResume: TextButton? = null


    init {
        val lbShop = Label("Pause", Assets.labelStyleBig)
        lbShop.setFontScale(1.5f)
        lbShop.setAlignment(Align.center)
        lbShop.setPosition(width / 2f - lbShop.width / 2f, 230f)
        addActor(lbShop)

        initButtons()

        val content = Table()

        content.defaults().expandX().uniform().fill()

        content.add(btResume)
        content.row().padTop(20f)
        content.add(btMenu)

        content.pack()
        content.setPosition(width / 2f - content.width / 2f, 50f)

        addActor(content)

    }

    private fun initButtons() {
        btMenu = TextButton("Menu", Assets.textButtonStyleBig)
        btMenu?.pad(15f)

        screen.addEfectoPress(btMenu)
        btMenu?.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                hide()
                screen.changeScreenWithFadeOut(MainMenuScreen::class.java, game)
            }
        })

        btResume = TextButton("Resume", Assets.textButtonStyleBig)
        btResume?.pad(15f)

        screen.addEfectoPress(btResume)
        btResume?.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                hide()
            }
        })
    }

    override fun hide() {
        (screen as GameScreen).setRunning()
        super.hide()
    }
}