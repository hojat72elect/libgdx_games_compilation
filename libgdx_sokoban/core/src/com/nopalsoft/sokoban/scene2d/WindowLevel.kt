package com.nopalsoft.sokoban.scene2d

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.sokoban.Assets
import com.nopalsoft.sokoban.game.GameScreen
import com.nopalsoft.sokoban.screens.Screens

class WindowLevel(currentScreen: Screens) : Window(currentScreen, 350f, 300f, 100f) {

    private val buttonPlay = Button(Assets.buttonPlay, Assets.buttonPlayPressed)
    private val labelBestMoves = Label("0", LabelStyle(Assets.fontRed, Color.WHITE))
    private val labelBestTime = Label("0", LabelStyle(Assets.fontRed, Color.WHITE))


    init {
        setCloseButton()
        setTitle("Puntuaciones", .75f)
        val tableMenu = Table()
        tableMenu.setFillParent(true)
        val imgClock = Image(Assets.clock)
        val imgMoves = Image(Assets.characterStand)

        tableMenu.defaults().expandX()
        tableMenu.padLeft(30f).padRight(30f).padBottom(20f).padTop(50f)
        tableMenu.add(imgMoves).size(45f)
        tableMenu.add(labelBestMoves)

        tableMenu.row().padTop(10f)
        tableMenu.add(imgClock).size(45f)
        tableMenu.add(labelBestTime)

        tableMenu.row().padTop(10f)
        tableMenu.add(buttonPlay).colspan(2).size(60f)

        addActor(tableMenu)
    }


    fun show(stage: Stage, level: Int, bestMoves: Int, bestTime: Int) {
        labelBestMoves.setText(bestMoves.toString())
        labelBestTime.setText(bestTime.toString())

        buttonPlay.clear()
        buttonPlay.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                screen.changeScreenWithFadeOut(GameScreen::class.java, level, screen.game)
            }
        })

        super.show(stage)
    }
}