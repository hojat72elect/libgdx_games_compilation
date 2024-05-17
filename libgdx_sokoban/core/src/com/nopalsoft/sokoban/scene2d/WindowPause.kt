package com.nopalsoft.sokoban.scene2d

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.sokoban.Assets
import com.nopalsoft.sokoban.Settings
import com.nopalsoft.sokoban.game.GameScreen
import com.nopalsoft.sokoban.screens.MainMenuScreen
import com.nopalsoft.sokoban.screens.Screens

class WindowPause(currentScreen: Screens) : Window(currentScreen, 350F, 300F, 100F) {

    private val buttonHome = Button(Assets.buttonHome, Assets.buttonHomePressed)
    private val buttonRefresh = Button(Assets.buttonRefresh, Assets.buttonRefreshPressed)
    private val tableAnimations = Table()


    init {
        setCloseButton()
        setTitle("Paused", 1f)

        val menuTable = Table()
        menuTable.setFillParent(true)

        buttonHome.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                screen.changeScreenWithFadeOut(MainMenuScreen::class.java, screen.game)
            }
        })
        buttonRefresh.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                screen.changeScreenWithFadeOut(
                    GameScreen::class.java,
                    (screen as GameScreen).level,
                    screen.game
                )
            }
        })

        val btAnimations = Button(Assets.buttonOff, Assets.buttonOn, Assets.buttonOn)
        btAnimations.isChecked = Settings.animationWalkIsON

        tableAnimations.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                Settings.animationWalkIsON = !Settings.animationWalkIsON

                btAnimations.isChecked = Settings.animationWalkIsON
                Settings.save()
            }
        })

        menuTable.defaults().expandX()
        menuTable.pad(30f).padTop(55f)
        menuTable.add(buttonHome)
        menuTable.add(buttonRefresh)
        menuTable.row()

        val labelAnimations = Label("Animations", LabelStyle(Assets.fontRed, Color.WHITE))
        tableAnimations.add(labelAnimations)
        tableAnimations.add(btAnimations).padLeft(15f)

        menuTable.add(tableAnimations).colspan(2).padTop(10f)

        addActor(menuTable)
    }

    override fun hideCompleted() {
        (screen as GameScreen).setRunning()
    }
}