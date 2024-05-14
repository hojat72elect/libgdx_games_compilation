package com.nopalsoft.invaders.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.nopalsoft.invaders.MainInvaders

fun main() {
    val config = Lwjgl3ApplicationConfiguration()
    config.setWindowedMode(480, 800)
    Lwjgl3Application(MainInvaders(), config)
}

