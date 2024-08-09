package com.nopalsoft.slamthebird.scene2d

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.slamthebird.Assets
import com.nopalsoft.slamthebird.screens.Screens

class WindowRate(currentScreen: Screens?) : Window(currentScreen!!) {
    init {
        setSize(390f, 260f)
        y = 300f
        setBackGround()

        val labelTitle = Label("Support this game", Assets.labelStyleSmall)
        labelTitle.setPosition(width / 2f - labelTitle.width / 2f, 210f)

        val lbContenido = Label(
            "Hello, thank you for playing Slam the Bird.\nHelp us to support this game. Just rate us at the app store.",
            Assets.labelStyleSmall
        )
        lbContenido.setSize(width - 20, 170f)
        lbContenido.setPosition(
            width / 2f - lbContenido.width / 2f,
            50f
        )
        lbContenido.wrap = true

        val btRate = TextButton(
            "Rate",
            Assets.textButtonStylePurchased
        )
        screen.addEfectoPress(btRate)
        btRate.label.wrap = true
        btRate.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                hide()
            }
        })

        val btNotNow = TextButton(
            "Not now",
            Assets.textButtonStyleSelected
        )
        screen.addEfectoPress(btNotNow)
        btNotNow.label.wrap = true
        btNotNow.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                hide()
            }
        })

        val tbBotones = Table()
        tbBotones.setSize(width - 20, 60f)
        tbBotones.setPosition(width / 2f - tbBotones.width / 2f, 10f)

        tbBotones.defaults().uniform().expand().center().fill().pad(10f)
        tbBotones.add(btRate)
        tbBotones.add(btNotNow)

        addActor(lbContenido)
        addActor(tbBotones)
        addActor(labelTitle)
    }
}
