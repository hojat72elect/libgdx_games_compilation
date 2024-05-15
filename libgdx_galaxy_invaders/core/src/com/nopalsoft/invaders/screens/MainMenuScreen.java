package com.nopalsoft.invaders.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.invaders.Assets;
import com.nopalsoft.invaders.MainInvaders;
import com.nopalsoft.invaders.Settings;
import com.nopalsoft.invaders.game.GameScreen;

public class MainMenuScreen extends Screens {

    TextButton buttonPlay, buttonSettings, buttonLeaderBoard, buttonMore, buttonFacebook;

    Label labelHighestScore;

    ImageButton buttonSound, buttonMusic;
    Image ellipseLeft;

    public MainMenuScreen(final MainInvaders game) {
        super(game);

        Table titleTable = new Table();
        titleTable.setBackground(Assets.titleMenuBox);
        Label qualification = new Label(Assets.languages.get("titulo_app"), new LabelStyle(Assets.font60, Color.GREEN));
        qualification.setAlignment(Align.center);
        titleTable.setSize(265, 100);
        titleTable.setPosition((SCREEN_WIDTH - 265) / 2f, SCREEN_HEIGHT - 110);
        titleTable.add(qualification).expand().center();

        // I put the text in the update
        labelHighestScore = new Label("", new LabelStyle(Assets.font10, Color.GREEN));
        labelHighestScore.setWidth(SCREEN_WIDTH);
        labelHighestScore.setAlignment(Align.center);
        labelHighestScore.setPosition(0, SCREEN_HEIGHT - 120);

        buttonPlay = new TextButton(Assets.languages.get("play"), Assets.styleTextButtonMenu);
        buttonPlay.setSize(250, 50);
        buttonPlay.setPosition(0, 280);
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
                game.setScreen(new GameScreen(game));

            }
        });

        buttonSettings = new TextButton(Assets.languages.get("settings"), Assets.styleTextButtonMenu);
        buttonSettings.setSize(300, 50);
        buttonSettings.setPosition(0, 210);
        buttonSettings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
                game.setScreen(new SettingsScreen(game));
            }
        });

        buttonLeaderBoard = new TextButton(Assets.languages.get("leaderboard"), Assets.styleTextButtonMenu);
        buttonLeaderBoard.setSize(310, 50);
        buttonLeaderBoard.setPosition(0, 140);
        buttonLeaderBoard.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
                game.setScreen(new LeaderboardScreen(game));
            }
        });

        buttonMore = new TextButton(Assets.languages.get("more"), Assets.styleTextButtonMenu);
        buttonMore.setSize(250, 50);
        buttonMore.setPosition(0, 70);
        buttonMore.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
            }
        });

        buttonFacebook = new TextButton(Assets.languages.get("like_us_to_get_lastest_news"), Assets.styleTextButtonFacebook);
        buttonFacebook.getLabel().setWrap(true);
        buttonFacebook.setWidth(170);
        buttonFacebook.setPosition(SCREEN_WIDTH - buttonFacebook.getWidth() - 2, 2);
        buttonFacebook.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
                Gdx.net.openURI("https://www.facebook.com/yayo28");

            }
        });

        buttonSound = new ImageButton(Assets.buttonSoundOn, Assets.buttonSoundOff, Assets.buttonSoundOff);
        buttonSound.setSize(40, 40);
        buttonSound.setPosition(2, 2);
        if (!Settings.getSoundEnabled())
            buttonSound.setChecked(true);
        buttonSound.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                Settings.setSoundEnabled(!Settings.getSoundEnabled());
                Assets.playSound(Assets.clickSound);
                buttonSound.setChecked(!Settings.getSoundEnabled());
            }
        });

        buttonMusic = new ImageButton(Assets.buttonMusicOn, Assets.buttonMusicOff, Assets.buttonMusicOff);
        buttonMusic.setSize(40, 40);
        buttonMusic.setPosition(44, 2);
        if (!Settings.getMusicEnabled())
            buttonMusic.setChecked(true);
        buttonMusic.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                Settings.setMusicEnabled(!Settings.getMusicEnabled());
                Assets.playSound(Assets.clickSound);
                if (!Settings.getMusicEnabled()) {
                    buttonMusic.setChecked(true);
                    Assets.music.pause();
                } else {
                    buttonMusic.setChecked(false);
                    Assets.music.play();
                }
            }
        });

        // Las medidas se sacaron con una formual de 3 si 480 / 960 x 585 donde 585 es el tamano,
        // 960 es el tamano para lo que se hicieron y 480 es el tamano de la camara
        ellipseLeft = new Image(Assets.ellipseMenuLeft);
        ellipseLeft.setSize(18.5f, 292.5f);
        ellipseLeft.setPosition(0, 60);

        stage.addActor(titleTable);
        stage.addActor(labelHighestScore);

        stage.addActor(buttonPlay);
        stage.addActor(buttonSettings);
        stage.addActor(buttonLeaderBoard);
        stage.addActor(buttonMore);
        stage.addActor(ellipseLeft);
        stage.addActor(buttonSound);
        stage.addActor(buttonMusic);
        stage.addActor(buttonFacebook);


        if (Settings.getNumberOfTimesGameHasBeenPlayed() == 0) {
            game.dialogs.showDialogSignIn();

        }
    }

    @Override
    public void update(float delta) {
        labelHighestScore.setText(Assets.languages.format("local_highest_score", String.valueOf(com.nopalsoft.invaders.Settings.getHighScores()[0])));
    }

    @Override
    public void draw(float delta) {
        myCamera.update();
        batcher.setProjectionMatrix(myCamera.combined);

        batcher.disableBlending();
        Assets.parallaxBackground.render(delta);
    }

    @Override
    public boolean keyDown(int tecleada) {
        if (tecleada == Keys.BACK || tecleada == Keys.ESCAPE) {
            Assets.playSound(Assets.clickSound);
            if (game.dialogs.isDialogShown()) {
                game.dialogs.dismissAll();
            } else {

                Gdx.app.exit();
            }
            return true;
        }
        return false;
    }
}
