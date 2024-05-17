package com.nopalsoft.ninjarunner.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.nopalsoft.ninjarunner.MainGame;


public class DesktopLauncher {

    public static MainGame game;


    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(800, 480);
        config.setTitle("Ninja Runner");
        game = new MainGame();
        new Lwjgl3Application(game, config);
    }
}
