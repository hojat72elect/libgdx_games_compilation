package com.nopalsoft.superjumper.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.nopalsoft.superjumper.MainSuperJumper;
import com.nopalsoft.superjumper.handlers.GameServicesHandler;


public class DesktopLauncher {

    static GameServicesHandler gameHandler = new GameServicesHandler() {

        @Override
        public void submitScore(long score) {
            // TODO Auto-generated method stub
        }


        @Override
        public void signIn() {
            // TODO Auto-generated method stub
        }

        @Override
        public boolean isSignedIn() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void getLeaderboard() {
            // TODO Auto-generated method stub
        }

    };

    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(480, 800);
        new Lwjgl3Application(new MainSuperJumper( gameHandler), config);
    }
}
