package com.nopalsoft.invaders.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.invaders.Assets;
import com.nopalsoft.invaders.MainInvaders;
import com.nopalsoft.invaders.Settings;
import com.nopalsoft.invaders.screens.MainMenuScreen;
import com.nopalsoft.invaders.screens.Screens;

public class GameScreen extends Screens {
    public static final int GAME_RUNNING = 1;
    public static final int GAME_PAUSE = 3;
    static final int GAME_READY = 0;
    static final int GAME_OVER = 2;
    public static int state;
    public final int GAME_TUTORIAL = 4;

    int screenTutorial; // If it is on screen 1 or 2 of the tutorial
    World oWorld;
    WorldRenderer renderer;
    boolean isItShotItself = false;
    boolean didItFireMissile = false;
    Vector3 touchPoint;
    Rectangle leftButton;
    Rectangle rightButton;
    Dialog dialogPause, dialogGameOver;
    Table scoresBar;
    Label lbLevel, lbScore, lbNumVidas;
    ImageButton btPause;
    ImageButton btLeft, btRight, btFire;
    TextButton buttonMissile;
    Group groupTutorial;
    Label labelTiltYourDevice;
    float accel;

    int level;
    float rotation = 0;
    float addRotation = .3f;

    public GameScreen(final MainInvaders game) {
        super(game);
        Settings.setNumberOfTimesGameHasBeenPlayed(Settings.getNumberOfTimesGameHasBeenPlayed() + 1);
        state = GAME_READY;
        if (Settings.getNumberOfTimesGameHasBeenPlayed() < 3) {// It will be shown 2 times, time zero and time 1.
            state = GAME_TUTORIAL;
            screenTutorial = 0;
            setUpTutorial();
        }
        touchPoint = new Vector3();

        oWorld = new World();
        renderer = new WorldRenderer(batcher, oWorld);
        leftButton = new Rectangle(0, 0, 160, 480);
        rightButton = new Rectangle(161, 0, 160, 480);

        // OnScreen Controls
        accel = 0;
        level = oWorld.currentLevel;
        btLeft = new ImageButton(Assets.getBtLeft());
        btLeft.setSize(65, 50);
        btLeft.setPosition(10, 5);
        btLeft.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                accel = 5;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                accel = 0;
                super.exit(event, x, y, pointer, toActor);
            }

        });
        btRight = new ImageButton(Assets.getBtRight());
        btRight.setSize(65, 50);
        btRight.setPosition(85, 5);
        btRight.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                accel = -5;

            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                accel = 0;
                super.exit(event, x, y, pointer, toActor);
            }

        });

        buttonMissile = new TextButton(oWorld.missileCount + "", new TextButtonStyle(Assets.getBtMissil(), Assets.getBtMissilDown(), null, Assets.getFont10()));
        buttonMissile.getLabel().setColor(Color.GREEN);
        buttonMissile.setSize(60, 60);
        buttonMissile.setPosition(SCREEN_WIDTH - 5 - 60 - 20 - 60, 5);
        buttonMissile.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                didItFireMissile = true;
            }
        });
        btFire = new ImageButton(Assets.getBtFire(), Assets.getBtFireDown());
        btFire.setSize(60, 60);
        btFire.setPosition(SCREEN_WIDTH - 60 - 5, 5);
        btFire.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isItShotItself = true;
            }
        });

        // End OnScreen Controls
        // Start dialog Pause
        dialogPause = new Dialog(Assets.getLanguages().get("game_paused"), Assets.getStyleDialogPause());

        TextButton btContinue = new TextButton(Assets.getLanguages().get("continue"), Assets.getStyleTextButton());
        TextButton btMenu = new TextButton(Assets.getLanguages().get("main_menu"), Assets.getStyleTextButton());

        btContinue.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.getClickSound());
                state = GAME_RUNNING;
                oWorld.state = World.STATE_RUNNING;
                dialogPause.hide();

            }
        });

        btMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.getClickSound());
                game.setScreen(new MainMenuScreen(game));
                dialogPause.hide();

            }
        });

        dialogPause.getButtonTable().pad(15);
        dialogPause.getButtonTable().add(btContinue).minWidth(160).minHeight(40).expand().padBottom(20);
        dialogPause.getButtonTable().row();
        dialogPause.getButtonTable().add(btMenu).minWidth(160).minHeight(40).expand();

        // Inicio dialogGameOver

        dialogGameOver = new Dialog("Game Over", Assets.getStyleDialogPause());

        TextButton btTryAgain = new TextButton(Assets.getLanguages().get("try_again"), Assets.getStyleTextButton());
        TextButton btMenu2 = new TextButton(Assets.getLanguages().get("main_menu"), Assets.getStyleTextButton());
        TextButton btShare = new TextButton(Assets.getLanguages().get("share"), Assets.getStyleTextButtonFacebook());

        btTryAgain.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.getClickSound());
                game.setScreen(new GameScreen(game));
                dialogGameOver.hide();

            }
        });

        btMenu2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.getClickSound());
                game.setScreen(new MainMenuScreen(game));
                dialogGameOver.hide();

            }
        });
        btShare.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String text = Assets.getLanguages().format("i_just_score_n_points_playing_droid_invaders_can_you_beat_me", oWorld.score);
                Gdx.app.log("Share text", text);
                Assets.playSound(Assets.getClickSound());
            }
        });

        dialogGameOver.getButtonTable().pad(15);
        dialogGameOver.getButtonTable().add(btTryAgain).minWidth(160).minHeight(40).expand().padBottom(20);
        dialogGameOver.getButtonTable().row();
        dialogGameOver.getButtonTable().add(btMenu2).minWidth(160).minHeight(40).expand();
        dialogGameOver.getButtonTable().row();


        Label lbShare = new Label(Assets.getLanguages().get("share_your_score_on_facebook"), Assets.getStyleLabelDialog());
        lbShare.setAlignment(Align.center);
        lbShare.setWrap(true);
        dialogGameOver.getButtonTable().add(lbShare).width(200).expand();
        dialogGameOver.getButtonTable().row();
        dialogGameOver.getButtonTable().add(btShare).expand();


        if (Settings.getNumberOfTimesGameHasBeenPlayed() % 5 == 0) {
            game.dialogs.showDialogRate();
        }

        btPause = new ImageButton(Assets.getStyleImageButtonPause());
        btPause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPaused();
            }
        });

        lbLevel = new Label(Assets.getLanguages().get("level") + " " + oWorld.currentLevel, Assets.getStyleLabel());
        lbScore = new Label(Assets.getLanguages().get("score") + " " + oWorld.score, Assets.getStyleLabel());
        lbNumVidas = new Label("x" + oWorld.myShip.lives, Assets.getStyleLabel());
        Image imVida = new Image(Assets.getShip());

        scoresBar = new Table();
        scoresBar.setBackground(Assets.getRecuadroInGameStatus());
        scoresBar.setWidth(SCREEN_WIDTH);
        scoresBar.setHeight(30);
        scoresBar.setPosition(0, SCREEN_HEIGHT - 30);

        scoresBar.add(lbLevel).left();

        scoresBar.add(lbScore).center().expandX();

        scoresBar.add(imVida).size(20).right();
        scoresBar.add(lbNumVidas).right();
        scoresBar.add(btPause).size(26).right().padLeft(8);
        // scoresBar.debug();

        stage.addActor(scoresBar);

    }

    private void setUpTutorial() {

        labelTiltYourDevice = new Label(Assets.getLanguages().get("tilt_your_device_to_move_horizontally"), new LabelStyle(Assets.getFont45(), Color.GREEN));
        labelTiltYourDevice.setWrap(true);
        labelTiltYourDevice.setAlignment(Align.center);
        labelTiltYourDevice.setPosition(0, 120);
        labelTiltYourDevice.setWidth(SCREEN_WIDTH);
        stage.addActor(labelTiltYourDevice);

        groupTutorial = new Group();
        // gpTutorial.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);

        Table boostTable = new Table();
        groupTutorial.addActor(boostTable);

        Image vida = new Image(Assets.getUpgLife());
        Image boostBomba = new Image(Assets.getBoost2());
        Image boostEscudo = new Image(Assets.getBoost3());
        Image boostUpgradeWeapon = new Image(Assets.getBoost1());

        Label lblVida = new Label(Assets.getLanguages().get("get_one_extra_life"), Assets.getStyleLabel());
        Label lblBomba = new Label(Assets.getLanguages().get("get_one_extra_missil"), Assets.getStyleLabel());
        Label lblShield = new Label(Assets.getLanguages().get("get_a_shield"), Assets.getStyleLabel());
        Label lblUpgradeWeapn = new Label(Assets.getLanguages().get("upgrade_your_weapon"), Assets.getStyleLabel());

        boostTable.setPosition(0, 340);
        boostTable.setWidth(SCREEN_WIDTH);

        int iconSize = 40;
        boostTable.add(vida).size(iconSize);
        boostTable.add(lblVida).padLeft(15).left();
        boostTable.row().padTop(10);
        boostTable.add(boostBomba).size(iconSize);
        boostTable.add(lblBomba).padLeft(15).left();
        boostTable.row().padTop(10);
        boostTable.add(boostEscudo).size(iconSize);
        boostTable.add(lblShield).padLeft(15).left();
        boostTable.row().padTop(10);
        boostTable.add(boostUpgradeWeapon).size(iconSize);
        boostTable.add(lblUpgradeWeapn).padLeft(15).left();

        Label touchLeft, touchRight;
        touchLeft = new Label(Assets.getLanguages().get("touch_left_side_to_fire_missils"), Assets.getStyleLabel());
        touchLeft.setWrap(true);
        touchLeft.setWidth(160);
        touchLeft.setAlignment(Align.center);
        touchLeft.setPosition(0, 50);

        touchRight = new Label(Assets.getLanguages().get("touch_right_side_to_fire"), Assets.getStyleLabel());
        touchRight.setWrap(true);
        touchRight.setWidth(160);
        touchRight.setAlignment(Align.center);
        touchRight.setPosition(165, 50);

        groupTutorial.addActor(touchRight);
        groupTutorial.addActor(touchLeft);

    }

    @Override
    public void update(float deltaTime) {
        // if (deltaTime > 0.1f) deltaTime = 0.1f;
        switch (state) {
            case GAME_TUTORIAL:
                updateTutorial();
                break;
            case GAME_READY:
                updateReady();
                break;
            case GAME_RUNNING:
                updateRunning(deltaTime);
                break;

        }

    }

    private void updateTutorial() {
        if (Gdx.input.justTouched()) {
            if (screenTutorial == 0) {
                screenTutorial++;
                labelTiltYourDevice.remove();
                stage.addActor(groupTutorial);
            } else {
                state = GAME_READY;
                groupTutorial.remove();
            }

        }
    }

    private void updateReady() {
        if (Gdx.input.justTouched() && !game.dialogs.isDialogShown()) {
            state = GAME_RUNNING;

            if (!Settings.getTiltControlEnabled()) {
                stage.addActor(btLeft);
                stage.addActor(btRight);
                stage.addActor(buttonMissile);
                stage.addActor(btFire);
            }

        }
    }

    private void updateRunning(float deltaTime) {

        if (Gdx.input.isKeyPressed(Keys.DPAD_LEFT) || Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.DPAD_RIGHT) || Gdx.input.isKeyPressed(Keys.D)) {
            accel = 0;
            if (Gdx.input.isKeyPressed(Keys.DPAD_LEFT) || Gdx.input.isKeyPressed(Keys.A))
                accel = 5f;
            if (Gdx.input.isKeyPressed(Keys.DPAD_RIGHT) || Gdx.input.isKeyPressed(Keys.D))
                accel = -5f;

            oWorld.update(deltaTime, accel, isItShotItself, didItFireMissile);
        } else if (Settings.getTiltControlEnabled()) {
            if (Gdx.input.justTouched()) {
                myCamera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

                if (leftButton.contains(touchPoint.x, touchPoint.y)) {
                    didItFireMissile = true;
                }
                if (rightButton.contains(touchPoint.x, touchPoint.y)) {
                    isItShotItself = true;
                }
            }
            oWorld.update(deltaTime, Gdx.input.getAccelerometerX(), isItShotItself, didItFireMissile);
        } else {
            oWorld.update(deltaTime, accel, isItShotItself, didItFireMissile);
        }

        if (level != oWorld.currentLevel) {
            level = oWorld.currentLevel;
            lbLevel.setText(Assets.getLanguages().get("level") + " " + level);
        }

        lbScore.setText(Assets.getLanguages().get("score") + " " + oWorld.score);
        lbNumVidas.setText("x" + oWorld.myShip.lives);

        if (oWorld.state == World.STATE_GAME_OVER) {
            state = GAME_OVER;
            dialogGameOver.show(stage);
        }

        buttonMissile.setText(oWorld.missileCount + "");

        isItShotItself = false;
        didItFireMissile = false;
    }

    private void setPaused() {
        Assets.playSound(Assets.getClickSound());
        state = GAME_PAUSE;
        oWorld.state = World.STATE_PAUSED;
        dialogPause.show(stage);
    }

    @Override
    public void draw(float delta) {

        if (state != GAME_TUTORIAL)
            renderer.render(delta);
        else
            Assets.getParallaxBackground().render(delta);
        myCamera.update();
        batcher.setProjectionMatrix(myCamera.combined);
        batcher.enableBlending();
        batcher.begin();

        switch (state) {
            case GAME_TUTORIAL:
                presentTurorial();
                break;
            case GAME_READY:
                presentReady();
                break;
            case GAME_RUNNING:
                presentRunning();
                break;
        }
        batcher.end();


    }

    private void presentTurorial() {
        if (screenTutorial == 0 && Settings.getTiltControlEnabled()) {
            if (rotation < -20 || rotation > 20)
                addRotation *= -1;
            rotation += addRotation;
            batcher.draw(Assets.getHelp1(), SCREEN_WIDTH / 2f - 51, 190, 51, 0, 102, 200, 1, 1, rotation);
        } else {
            batcher.draw(Assets.getClickHelp(), 155, 0, 10, 125);

        }

    }

    private void presentReady() {
        String touchToStart = Assets.getLanguages().get("touch_to_start");
        float textWidth = Assets.getTextWidth(Assets.getFont45(), touchToStart);
        Assets.getFont45().draw(batcher, touchToStart, (SCREEN_WIDTH / 2f) - (textWidth / 2f), 220);
    }

    private void presentRunning() {
        if (oWorld.missileCount > 0 && Settings.getTiltControlEnabled()) {
            batcher.draw(Assets.getMissile().getKeyFrame(0), 1, 1, 8, 28);
            Assets.getFont15().draw(batcher, "X" + oWorld.missileCount, 10, 25);
        }
    }

    @Override
    public void hide() {
        com.nopalsoft.invaders.Settings.addScore(oWorld.score);
        super.hide();
    }

    @Override
    public void pause() {
        setPaused();
        super.pause();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
            Assets.playSound(Assets.getClickSound());
            if (state == GAME_RUNNING) {
                setPaused();
                return true;
            } else if (state == GAME_PAUSE) {
                game.setScreen(new MainMenuScreen(game));
                return true;
            }
        } else if (keycode == Keys.MENU) {
            setPaused();
            return true;
        } else if (keycode == Keys.SPACE) {
            isItShotItself = true;

            return true;
        } else if (keycode == Keys.ALT_LEFT) {
            didItFireMissile = true;
            return true;
        }
        return false;
    }

}
