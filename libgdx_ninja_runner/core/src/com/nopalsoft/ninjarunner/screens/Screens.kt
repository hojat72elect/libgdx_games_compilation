package com.nopalsoft.ninjarunner.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.nopalsoft.ninjarunner.Assets;
import com.nopalsoft.ninjarunner.MainGame;
import com.nopalsoft.ninjarunner.Settings;
import com.nopalsoft.ninjarunner.shop.ShopScreen;
import com.nopalsoft.ninjarunner.game.GameScreen;

public abstract class Screens extends InputAdapter implements Screen, GestureListener {
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 480;
    public static final float WORLD_WIDTH = 8;
    public static final float WORLD_HEIGHT = 4.8f;

    public MainGame game;

    public OrthographicCamera myCamera;
    public SpriteBatch batcher;
    public Stage stage;

    Image blackFadeOut;

    public Screens(final Game _game) {
        this.game = (MainGame) _game;
        this.stage = game.stage;
        this.stage.clear();
        this.batcher = game.batcher;


        myCamera = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
        myCamera.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0);

        GestureDetector detector = new GestureDetector(20, .5f, 2, .15f, this);

        InputMultiplexer input = new InputMultiplexer(this, detector, stage);
        Gdx.input.setInputProcessor(input);

    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(.191f, .703f, .996f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        myCamera.update();
        batcher.setProjectionMatrix(myCamera.combined);
        draw(delta);

        stage.act(delta);
        stage.draw();

    }

    public void changeScreenWithFadeOut(final Class<?> newScreen, final MainGame game) {
        blackFadeOut = new Image(Assets.pixelNegro);
        blackFadeOut.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        blackFadeOut.getColor().a = 0;
        blackFadeOut.addAction(Actions.sequence(Actions.fadeIn(.5f), Actions.run(() -> {
            if (newScreen == GameScreen.class) {
                game.setScreen(new GameScreen(game, true));
            } else if (newScreen == SettingsScreen.class)
                game.setScreen(new SettingsScreen(game));
            else if (newScreen == ShopScreen.class)
                game.setScreen(new ShopScreen(game));
        })));
        stage.addActor(blackFadeOut);
    }

    public abstract void draw(float delta);

    public abstract void update(float delta);

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
        Settings.save();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        batcher.dispose();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        if (Math.abs(velocityX) > Math.abs(velocityY)) {
            if (velocityX > 0) {
                right();
            } else {
                left();
            }
        } else {
            if (velocityY > 0) {
                down();
            } else {
                up();
            }
        }
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void pinchStop() {

    }

    public void up() {
        Gdx.app.log("UP", "");
    }

    public void down() {
        Gdx.app.log("DOWN", "");
    }

    public void left() {
        Gdx.app.log("LEFT", "");
    }

    public void right() {
        Gdx.app.log("RIGHT", "");
    }

}