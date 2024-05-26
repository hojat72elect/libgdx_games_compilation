package com.nopalsoft.ninjarunner.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.nopalsoft.ninjarunner.MainGame

fun main() {
    // Required configurations for the game
    val config = Lwjgl3ApplicationConfiguration()
    config.setWindowedMode(800, 480)
    config.setTitle("Ninja Runner")

    // Start the game
    Lwjgl3Application(MainGame(),config)
}