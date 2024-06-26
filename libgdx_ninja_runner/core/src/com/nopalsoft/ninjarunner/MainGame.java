package com.nopalsoft.ninjarunner;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.ninjarunner.game.GameScreen;
import com.nopalsoft.ninjarunner.leaderboard.Person;
import com.nopalsoft.ninjarunner.screens.Screens;

/**
 * It has already been made in KT. Don't do it again.
 */
public class MainGame extends Game {


    public Array<Person> arrayOfPersons = new Array<>();
    public Stage stage;
    public SpriteBatch batcher;
    public I18NBundle languages;

    public MainGame() {

    }

    @Override
    public void create() {
        languages = I18NBundle.createBundle(Gdx.files.internal("strings/strings"));
        batcher = new SpriteBatch();
        stage = new Stage(new StretchViewport(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT));

        Settings.load();
        Assets.load();
        setScreen(new GameScreen(this, true));

    }

}
