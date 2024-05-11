package com.nopalsoft.superjumper;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.nopalsoft.superjumper.handlers.GameServicesHandler;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

public class IOSLauncher extends IOSApplication.Delegate implements GameServicesHandler {
    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }

    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        return new IOSApplication(new MainSuperJumper(this), config);
    }

    @Override
    public void submitScore(long score) {

    }


    @Override
    public void getLeaderboard() {

    }


    @Override
    public boolean isSignedIn() {
        return false;
    }

    @Override
    public void signIn() {

    }

}