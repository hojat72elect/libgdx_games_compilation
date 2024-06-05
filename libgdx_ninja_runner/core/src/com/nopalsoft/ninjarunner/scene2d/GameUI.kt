package com.nopalsoft.ninjarunner.scene2d

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.ninjarunner.Assets
import com.nopalsoft.ninjarunner.game.GameScreen
import com.nopalsoft.ninjarunner.game.WorldGame
import com.nopalsoft.ninjarunner.screens.Screens

class GameUI(val gameScreen: GameScreen, val myWorld: WorldGame) : Group() {

    @JvmField
    var didJump = false

    @JvmField
    var didSlide = false

    @JvmField
    var didDash = false

    var tableHeader = Table()
    var labelScore = Label("0", Assets.labelStyleSmall);
    var buttonJump = Button(ButtonStyle(null, null, null))
    var buttonSlide = Button(ButtonStyle(null, null, null));


    init {
        setBounds(0f, 0f, Screens.SCREEN_WIDTH.toFloat(), Screens.SCREEN_HEIGHT.toFloat())

        buttonJump.setSize(width / 2f, height)
        buttonJump.setPosition(0f, 0f)
        buttonJump.addListener(object : ClickListener() {
            override fun touchDown(
                event: InputEvent,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {
                didJump = true
                return false
            }
        })

        buttonSlide.setSize(width / 2f, height)
        buttonSlide.setPosition(width / 2f + 1, 0f)
        buttonSlide.addListener(object : ClickListener() {
            override fun touchDown(
                event: InputEvent,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {
                didSlide = true
                return true
            }

            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                didSlide = false
            }
        })

        tableHeader.setSize(Screens.SCREEN_WIDTH.toFloat(), 50f)
        tableHeader.setPosition(0f, Screens.SCREEN_HEIGHT - tableHeader.height)

        tableHeader.add(labelScore).fill()

        addActor(tableHeader)

        addActor(buttonJump)
        addActor(buttonSlide)
    }


    override fun act(delta: Float) {
        super.act(delta)
    }

    private fun addInActions() {
    }

    fun show(stage: Stage) {
        addInActions()
        stage.addActor(this)
    }

    companion object {
        const val ANIMATION_TIME = .35f
    }
}