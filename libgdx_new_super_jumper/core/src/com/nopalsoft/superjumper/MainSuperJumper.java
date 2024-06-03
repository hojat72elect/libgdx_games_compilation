package com.nopalsoft.superjumper;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.superjumper.screens.BasicScreen;
import com.nopalsoft.superjumper.screens.MainMenuScreen;

public class MainSuperJumper extends Game {

    public Stage stage;
    public SpriteBatch batcher;
    public MainSuperJumper() {

    }

    @Override
    public void create() {

        stage = new Stage(new StretchViewport(BasicScreen.SCREEN_WIDTH, BasicScreen.SCREEN_HEIGHT));

        batcher = new SpriteBatch();
        Settings.load();
        Assets.load();

        setScreen(new MainMenuScreen(this));
    }
}
