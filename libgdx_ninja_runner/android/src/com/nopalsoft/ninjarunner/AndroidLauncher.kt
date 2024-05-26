package com.nopalsoft.ninjarunner

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration

class AndroidLauncher : AndroidApplication() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // the configuration
        val config = AndroidApplicationConfiguration()
        // Start the game
        initialize(MainGame(), config)
    }
}