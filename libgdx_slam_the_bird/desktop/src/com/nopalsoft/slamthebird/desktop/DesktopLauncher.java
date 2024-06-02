package com.nopalsoft.slamthebird.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.nopalsoft.slamthebird.MainSlamBird;
import com.nopalsoft.slamthebird.handlers.RequestHandler;
import com.badlogic.gdx.Gdx;

public class DesktopLauncher {
    static RequestHandler handler = () -> Gdx.net.openURI("https://www.facebook.com");


    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(480, 800);
        config.setTitle("Slam the bird");
        new Lwjgl3Application(new MainSlamBird(handler), config);
    }
}
