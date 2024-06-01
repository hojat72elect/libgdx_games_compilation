package com.nopalsoft.superjumper.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.superjumper.Assets;
import com.nopalsoft.superjumper.MainSuperJumper;
import com.nopalsoft.superjumper.Settings;
import com.nopalsoft.superjumper.screens.BasicScreen;
import com.nopalsoft.superjumper.scene2d.PauseScreen;
import com.nopalsoft.superjumper.scene2d.BaseScreenGameOver;

public class GameScreen extends BasicScreen {

    static final int STATE_RUNNING = 2;
    static final int STATE_PAUSED = 3;
    static final int STATE_GAME_OVER = 4;
    static int state;

    public WorldGame myWorldGame;
    WorldGameRender renderer;

    Vector3 touchPositionWorldCoords;
    boolean didFire;

    Label labelDistance, labelCoins, labelBullets;

    Button buttonPause;

    PauseScreen pauseScreen;

    public GameScreen(MainSuperJumper game) {
        super(game);

        pauseScreen = new PauseScreen(this);

        myWorldGame = new WorldGame();
        renderer = new WorldGameRender(batcher, myWorldGame);
        touchPositionWorldCoords = new Vector3();

        state = STATE_RUNNING;
        Settings.setNumberOfTimesPlayed(Settings.getNumberOfTimesPlayed()+1);

        Table menuMarker = new Table();
        menuMarker.setSize(SCREEN_WIDTH, 40);
        menuMarker.setY(SCREEN_HEIGHT - menuMarker.getHeight());

        labelCoins = new Label("", Assets.labelStyleBig);
        labelDistance = new Label("", Assets.labelStyleBig);
        labelBullets = new Label("", Assets.labelStyleBig);

        menuMarker.add(new Image(new TextureRegionDrawable(Assets.coin))).left().padLeft(5);
        menuMarker.add(labelCoins).left();

        menuMarker.add(labelDistance).center().expandX();

        menuMarker.add(new Image(new TextureRegionDrawable(Assets.gun))).height(45).width(30).left();
        menuMarker.add(labelBullets).left().padRight(5);

        buttonPause = new Button(Assets.btPause);
        buttonPause.setSize(35, 35);
        buttonPause.setPosition(SCREEN_WIDTH - 40, SCREEN_HEIGHT - 80);
        addPressEffect(buttonPause);
        buttonPause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPaused();
            }
        });

        stage.addActor(menuMarker);
        stage.addActor(buttonPause);

    }

    @Override
    public void update(float delta) {
        switch (state) {
            case STATE_RUNNING:
                updateRunning(delta);
                break;
            case STATE_GAME_OVER:
                updateGameOver(delta);
                break;
        }

    }

    private void updateRunning(float delta) {

        float acelX;

        acelX = -(Gdx.input.getAccelerometerX() / 3f);

        if (Gdx.input.isKeyPressed(Keys.A))
            acelX = -1;
        else if (Gdx.input.isKeyPressed(Keys.D))
            acelX = 1;

        myWorldGame.update(delta, acelX, didFire, touchPositionWorldCoords);

        labelCoins.setText("x" + myWorldGame.coins);
        labelDistance.setText("Score " + myWorldGame.maxDistance);
        labelBullets.setText("x" + Settings.getNumBullets());

        if (myWorldGame.state == WorldGame.STATE_GAMEOVER) {
            setGameover();
        }

        didFire = false;

    }

    private void updateGameOver(float delta) {
        myWorldGame.update(delta, 0, false, touchPositionWorldCoords);

    }

    @Override
    public void draw(float delta) {

        batcher.begin();
        batcher.draw(Assets.background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        batcher.end();

        if (state != STATE_PAUSED) {
            renderer.render();
        }

    }

    private void setPaused() {
        if (state == STATE_RUNNING) {
            state = STATE_PAUSED;
            pauseScreen.show(stage);
        }
    }

    public void setRunning() {
        state = STATE_RUNNING;

    }

    private void setGameover() {
        state = STATE_GAME_OVER;
        Settings.changeBestScore(myWorldGame.maxDistance);
        new BaseScreenGameOver(this).show(stage);

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchPositionWorldCoords.set(screenX, 0, 0);// Siempre como si hubiera tocado la parte mas alta de la pantalla
        renderer.unprojectToWorldCoords(touchPositionWorldCoords);


        didFire = true;
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
            if (pauseScreen.isVisible())
                pauseScreen.hide();
            else
                setPaused();
            return true;
        }
        return super.keyDown(keycode);
    }

}
