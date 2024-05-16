package com.nopalsoft.sokoban.scene2d

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.sokoban.Assets
import com.nopalsoft.sokoban.game.GameScreen


class ControlsNoPad(oScreen: GameScreen) : Table() {

    private val gameScreen = oScreen
    private lateinit var buttonUp: Button
    private lateinit var buttonDown: Button
    private lateinit var buttonLeft: Button
    private lateinit var buttonRight: Button

    init {
        color.a = .4f
        initialize()
        val buttonSize = 75
        defaults().size(buttonSize.toFloat())
        add(buttonUp).colspan(2).center()
        row()
        add(buttonLeft).left()
        add(buttonRight).right().padLeft(buttonSize / 1.15f)
        row()
        add(buttonDown).colspan(2).center()
        pack()
    }

    private fun initialize() {

        buttonUp = Button(Assets.buttonUp, Assets.buttonUpPressed)
        buttonUp.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                gameScreen.up()
            }
        })

        buttonDown = Button(Assets.buttonDown, Assets.buttonDownPressed)
        buttonDown.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                gameScreen.down()
            }
        })

        buttonLeft = Button(Assets.buttonLeft, Assets.buttonLeftPressed)
        buttonLeft.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                gameScreen.left()
            }
        })

        buttonRight = Button(Assets.buttonRight, Assets.buttonRightPressed)
        buttonRight.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                gameScreen.right()
            }
        })

    }
}