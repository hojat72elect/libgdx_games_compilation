package com.nopalsoft.sokoban.scene2d

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.sokoban.Assets

class CounterBar(background: TextureRegionDrawable, x: Float, y: Float) : Table() {

    private val labelDisplay = Label("", LabelStyle(Assets.fontRed, Color.WHITE))

    init {
        this.setBounds(x, y, WIDTH, HEIGHT)
        setBackground(background)
        labelDisplay.setFontScale(.8f)
        add(labelDisplay)
        center()
        padLeft(25f)
        padBottom(5f)
    }

    fun updateActualNum(actualNum: Int) {
        labelDisplay.setText(actualNum.toString())
    }


    companion object {
        private const val WIDTH = 125f
        private const val HEIGHT = 42f
    }
}