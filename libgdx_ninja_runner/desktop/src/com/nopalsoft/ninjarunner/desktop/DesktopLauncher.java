package com.nopalsoft.ninjarunner.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.nopalsoft.ninjarunner.MainGame;
import com.nopalsoft.ninjarunner.handlers.RequestHandler;

public class DesktopLauncher {

    public static MainGame game;
    static RequestHandler handler = new RequestHandler() {

        @Override
        public void showRater() {
            // TODO Auto-generated method stub

        }

        @Override
        public void showMoreGames() {
            // TODO Auto-generated method stub

        }

        @Override
        public void shareApp() {
            // TODO Auto-generated method stub

        }

        @Override
        public void loadInterstitial() {
            // TODO Auto-generated method stub

        }
    };


    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(800, 480);
        config.setTitle("Ninja Runner");
        game = new MainGame(handler);
        new Lwjgl3Application(game, config);
    }
}
