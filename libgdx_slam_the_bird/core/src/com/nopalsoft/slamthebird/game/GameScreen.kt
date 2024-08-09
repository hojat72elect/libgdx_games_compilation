package com.nopalsoft.slamthebird.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.slamthebird.Achievements;
import com.nopalsoft.slamthebird.Assets;
import com.nopalsoft.slamthebird.MainSlamBird;
import com.nopalsoft.slamthebird.Settings;
import com.nopalsoft.slamthebird.scene2d.LabelCoins;
import com.nopalsoft.slamthebird.scene2d.LabelCombo;
import com.nopalsoft.slamthebird.scene2d.LabelScore;
import com.nopalsoft.slamthebird.scene2d.WindowPause;
import com.nopalsoft.slamthebird.scene2d.WindowRate;
import com.nopalsoft.slamthebird.screens.Screens;
import com.nopalsoft.slamthebird.shop.ShopScreen;

public class GameScreen extends Screens {
    static final int STATE_READY = 1;
    static final int STATE_RUNNING = 2;
    static final int STATE_PAUSED = 3;
    static final int STATE_GAME_OVER = 4;
    static final int STATE_TRY_AGAIN = 5;

    static int state;

    WorldGame worldGame;
    WorldGameRender renderer;
    Image imageGameOver;
    Group groupTryAgain;
    Group groupButtons;
    Image imageAppTitle;
    boolean sideComboText;// Create new scratch file from selection.

    WindowRate windowRate;
    WindowPause windowPause;
    int combo;

    public GameScreen(MainSlamBird game) {
        super(game);
        worldGame = new WorldGame();
        renderer = new WorldGameRender(batcher, worldGame);

        groupTryAgain = new Group();
        windowRate = new WindowRate(this);
        windowPause = new WindowPause(this);

        setUpButtons();
        setUpGameover();
        setReady();

    }

    private void setUpButtons() {
        groupButtons = new Group();
        groupButtons.setSize(stage.getWidth(), stage.getHeight());
        groupButtons.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                if (windowRate.isVisible())
                    return false;

                setRunning();
                Settings.numberOfTimesPlayed++;
                return true;
            }
        });

        Button buttonAchievements, buttonLeaderboard, buttonMore, buttonRate, buttonShop;
        Button buttonShareFacebook, buttonShareTwitter;

        Image tapToPlay, bestScore;

        bestScore = new Image(Assets.bestScore);
        bestScore.setSize(170, 25);
        bestScore.setPosition(SCREEN_WIDTH / 2f - bestScore.getWidth() / 2f, 770);
        bestScore.addAction(Actions.repeat(
                Integer.MAX_VALUE,
                Actions.sequence(Actions.alpha(.6f, .75f),
                        Actions.alpha(1, .75f))));

        buttonShop = new Button(Assets.buttonShop);
        buttonShop.setSize(90, 70);
        buttonShop.setPosition(0, 730);
        addEfectoPress(buttonShop);
        buttonShop.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeScreenWithFadeOut(ShopScreen.class, game);
            }
        });

        buttonMore = new Button(Assets.buttonMore);
        buttonMore.setSize(90, 70);
        buttonMore.setPosition(390, 730);
        addEfectoPress(buttonMore);
        buttonMore.addListener(new ClickListener() {
        });

        buttonLeaderboard = new Button(Assets.buttonLeaderBoard);
        buttonLeaderboard.setSize(110, 75);
        buttonLeaderboard.setPosition(230 - 110, 310);
        addEfectoPress(buttonLeaderboard);
        buttonLeaderboard.addListener(new ClickListener() {
        });

        buttonAchievements = new Button(Assets.buttonAchievements);
        buttonAchievements.setSize(110, 75);
        buttonAchievements.setPosition(250, 310);
        addEfectoPress(buttonAchievements);
        buttonAchievements.addListener(new ClickListener() {
        });

        buttonRate = new Button(Assets.buttonRate);
        buttonRate.setSize(110, 75);

        buttonRate.setPosition(SCREEN_WIDTH / 2f - buttonRate.getWidth() / 2f - 25, 220);// Con el boton face y twitter cambia la pos
        addEfectoPress(buttonRate);
        buttonRate.addListener(new ClickListener() {
        });

        buttonShareFacebook = new Button(new TextureRegionDrawable(
                Assets.buttonFacebook));
        buttonShareFacebook.setSize(40, 40);
        buttonShareFacebook.setPosition(280, 257);
        addEfectoPress(buttonShareFacebook);
        buttonShareFacebook.addListener(new ClickListener() {
        });

        buttonShareTwitter = new Button(new TextureRegionDrawable(Assets.buttonTwitter));
        buttonShareTwitter.setSize(40, 40);
        buttonShareTwitter.setPosition(280, 212);
        addEfectoPress(buttonShareTwitter);
        buttonShareTwitter.addListener(new ClickListener() {
        });

        final Button btMusica, btSonido;

        btMusica = new Button(Assets.buttonStyleMusic);
        btMusica.setPosition(5, 100);
        btMusica.setChecked(!Settings.isMusicOn);
        btMusica.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                event.stop();
                return true;
            }
        });
        btMusica.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                event.stop();
                Settings.isMusicOn = !Settings.isMusicOn;
                btMusica.setChecked(!Settings.isMusicOn);
                if (Settings.isMusicOn)
                    Assets.playMusic();
                else
                    Assets.pauseMusic();
                Gdx.app.log("Muscia", Settings.isMusicOn + "");
            }
        });

        btSonido = new Button(Assets.buttonStyleSound);
        btSonido.setPosition(5, 180);
        btSonido.setChecked(!Settings.isSoundOn);
        btSonido.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                event.stop();
                return true;
            }
        });
        btSonido.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                Settings.isSoundOn = !Settings.isSoundOn;
                btSonido.setChecked(!Settings.isSoundOn);

            }
        });

        tapToPlay = new Image(Assets.tapToPlay);
        tapToPlay.setSize(333, 40);
        tapToPlay
                .setPosition(SCREEN_WIDTH / 2f - tapToPlay.getWidth() / 2f, 140);
        tapToPlay.setOrigin(tapToPlay.getWidth() / 2f,
                tapToPlay.getHeight() / 2f);
        float scaleTime = .75f;
        tapToPlay.addAction(Actions.repeat(Integer.MAX_VALUE, Actions.sequence(
                Actions.scaleTo(.95f, .95f, scaleTime),
                Actions.scaleTo(1f, 1f, scaleTime))));


        groupButtons.addActor(tapToPlay);
        groupButtons.addActor(bestScore);
        groupButtons.addActor(buttonShop);
        groupButtons.addActor(buttonMore);
        groupButtons.addActor(buttonLeaderboard);
        groupButtons.addActor(buttonAchievements);
        groupButtons.addActor(buttonRate);
        groupButtons.addActor(btMusica);
        groupButtons.addActor(btSonido);
        groupButtons.addActor(buttonShareFacebook);
        groupButtons.addActor(buttonShareTwitter);
    }

    private void setUpGameover() {
        imageGameOver = new Image(Assets.backgroundGameOver);
        imageGameOver.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        imageGameOver.setOrigin(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f);
        imageGameOver.setScale(2);
        imageGameOver.addAction(Actions.sequence(
                Actions.scaleTo(1.1f, 1.1f, .25f), Actions.delay(1f),
                Actions.run(() -> {
                    imageGameOver.remove();
                    imageGameOver.setScale(2);
                    setTryAgain();
                })));

    }

    @Override
    public void update(float delta) {

        switch (state) {
            case STATE_RUNNING:
                updateRunning(delta);
                break;
            case STATE_READY:
            case STATE_TRY_AGAIN:
                updateReady(delta);
                break;
            default:
                break;
        }

    }

    private void updateReady(float delta) {
        float acelX = Gdx.input.getAccelerometerX() * -1 / 5f;

        if (Gdx.input.isKeyPressed(Keys.A))
            acelX = -1;
        else if (Gdx.input.isKeyPressed(Keys.D))
            acelX = 1;

        worldGame.updateReady(delta, acelX);

    }

    private void updateRunning(float delta) {
        boolean slam = false;
        float acelX = Gdx.input.getAccelerometerX() * -1 / 5f;

        if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT))
            acelX = -1;
        else if (Gdx.input.isKeyPressed(Keys.D)
                || Gdx.input.isKeyPressed(Keys.RIGHT))
            acelX = 1;
        Gdx.app.log("Slam is", " " + false);

        if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE)
                || Gdx.input.isKeyPressed(Keys.DOWN)) {
            slam = true;
            Gdx.app.log("Slam is", " " + true);
        }

        worldGame.update(delta, acelX, slam);

        if (worldGame.state == WorldGame.STATE_GAME_OVER) {
            setGameover();
        }

        if (worldGame.combo == 0)
            combo = 0;

        if (worldGame.combo > combo) {
            stage.getBatch().setColor(1, 1, 1, 1);// Un BUG que no pone el alpha en 1 otra vez

            combo = worldGame.combo;
            LabelCombo lblCombo = new LabelCombo(worldGame.robot.position.x * 100,
                    worldGame.robot.position.y * 100 - 50, combo);

            float sideToMove;
            if (sideComboText) {
                sideToMove = 0;
                sideComboText = false;
            } else {
                sideComboText = true;
                sideToMove = 380;
            }

            lblCombo.addAction(Actions.sequence(Actions.moveTo(sideToMove, 400,
                    2.5f, Interpolation.exp10Out), Actions.removeActor()));
            stage.addActor(lblCombo);

        }

    }

    @Override
    public void draw(float delta) {
        renderer.render();

        oCam.update();
        batcher.setProjectionMatrix(oCam.combined);

        batcher.begin();

        switch (state) {
            case STATE_RUNNING:
                drawRunning();
                break;
            case STATE_READY:
            case STATE_TRY_AGAIN:
                drawReady();
                break;
            default:
                break;
        }
        batcher.end();

    }

    private void drawRunning() {
        drawNumGrandeCentradoX(SCREEN_WIDTH / 2f, 700, worldGame.scoreSlammed);

        batcher.draw(Assets.coin, 449, 764, 30, 34);
        drawPuntuacionChicoOrigenDerecha(445, 764, worldGame.takenCoins);

    }

    private void drawReady() {

        drawNumChicoCentradoX(SCREEN_WIDTH / 2f, 730, Settings.bestScore);

    }

    private void setPaused() {
        if (state == STATE_RUNNING) {
            state = STATE_PAUSED;
            windowPause.show(stage);
        }

    }

    public void setRunningFromPaused() {
        if (state == STATE_PAUSED) {
            state = STATE_RUNNING;
        }

    }

    private void setReady() {
        imageAppTitle = new Image(Assets.title);
        imageAppTitle.setSize(400, 290);
        imageAppTitle.setPosition(SCREEN_WIDTH / 2f - imageAppTitle.getWidth() / 2f,
                415);
        state = STATE_READY;
        stage.addActor(groupButtons);
        stage.addActor(imageAppTitle);

    }

    private void setRunning() {

        groupTryAgain.addAction(Actions.sequence(Actions.fadeOut(.5f),
                Actions.removeActor()));
        imageAppTitle.addAction(Actions.sequence(Actions.fadeOut(.5f),
                Actions.removeActor()));
        groupButtons.addAction(Actions.sequence(Actions.fadeOut(.5f),
                Actions.run(() -> {
                    groupButtons.remove();
                    groupTryAgain.remove();// POr el bug
                    state = STATE_RUNNING;
                })));

    }

    private void setGameover() {
        Settings.setBestScores(worldGame.scoreSlammed);
        state = STATE_GAME_OVER;
        stage.addActor(imageGameOver);
    }

    private void setTryAgain() {
        state = STATE_TRY_AGAIN;
        setUpGameover();

        groupTryAgain = new Group();
        groupTryAgain.setSize(420, 300);
        groupTryAgain.setPosition(SCREEN_WIDTH / 2f - groupTryAgain.getWidth()
                / 2, 800);
        groupTryAgain.addAction(Actions.sequence(Actions.moveTo(
                groupTryAgain.getX(), 410, 1, Interpolation.bounceOut), Actions
                .run(() -> {
                    groupButtons.addAction(Actions.fadeIn(.5f));
                    stage.addActor(groupButtons);

                    if (Settings.numberOfTimesPlayed % 7 == 0
                            && !Settings.isQualified) {
                        windowRate.show(stage);
                    }
                })));

        Image background = new Image(Assets.buttonScores);
        background.setSize(groupTryAgain.getWidth(), groupTryAgain.getHeight());
        groupTryAgain.addActor(background);

        /*
         * Aqui voy a agregar lo de mas del try agai
         */
        Image score = new Image(Assets.score);
        score.setSize(225, 70);
        score.setPosition(420 / 2f - score.getWidth() / 2f, 200);

        Image coinsEarned = new Image(Assets.coinsEarned);
        coinsEarned.setSize(243, 25);
        coinsEarned.setPosition(25, 47);

        LabelScore labelScore = new LabelScore(420 / 2f, 120, worldGame.scoreSlammed);
        LabelCoins labelCoins = new LabelCoins(385, 45, worldGame.takenCoins);

        Achievements.unlockCoins();

        groupTryAgain.addActor(score);
        groupTryAgain.addActor(labelScore);
        groupTryAgain.addActor(labelCoins);
        groupTryAgain.addActor(coinsEarned);

        worldGame = new WorldGame();
        renderer = new WorldGameRender(batcher, worldGame);

        stage.addActor(groupTryAgain);

    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
            if (state == STATE_READY)
                Gdx.app.exit();
            else if (state == STATE_TRY_AGAIN)
                changeScreenWithFadeOut(GameScreen.class, game);
            setPaused();
            return true;
        }
        return false;

    }
}
