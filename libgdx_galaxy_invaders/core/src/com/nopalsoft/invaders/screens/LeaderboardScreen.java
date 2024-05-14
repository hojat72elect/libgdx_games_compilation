package com.nopalsoft.invaders.screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.invaders.Assets;
import com.nopalsoft.invaders.MainInvaders;

public class LeaderboardScreen extends Screens {

    TextButton btLeaderBoard, btAchievements, btBack, btSignOut;
    Image ellipseLeft;

    public LeaderboardScreen(final MainInvaders game) {
        super(game);

        btBack = new TextButton(Assets.languages.get("back"), Assets.styleTextButtonBack);
        btBack.pad(0, 15, 35, 0);
        btBack.setSize(63, 63);
        btBack.setPosition(SCREEN_WIDTH - 63, SCREEN_HEIGHT - 63);
        btBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
                game.setScreen(new MainMenuScreen(game));
            }
        });

        btLeaderBoard = new TextButton(Assets.languages.get("leaderboard"), Assets.styleTextButtonMenu);
        btLeaderBoard.setHeight(50);// Height 50
        btLeaderBoard.setSize(50, 0);// We add 50 to the current width.
        btLeaderBoard.setPosition(0, 245);
        btLeaderBoard.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
            }
        });

        btAchievements = new TextButton(Assets.languages.get("achievements"), Assets.styleTextButtonMenu);
        btAchievements.setHeight(50);// Height 50
        btAchievements.setSize(50, 0);//  We add 50 to the current width.
        btAchievements.setPosition(0, 150);
        btAchievements.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
            }
        });

        btSignOut = new TextButton(Assets.languages.get("sign_out"), new TextButtonStyle(Assets.buttonSignInUp, Assets.buttonSignInDown, null, Assets.font15));
        btSignOut.getLabel().setWrap(true);
        btSignOut.setWidth(140);
        btSignOut.setPosition(2, 2);
        btSignOut.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
                game.setScreen(new MainMenuScreen(game));
            }
        });

        ellipseLeft = new Image(Assets.ellipseMenuLeft);
        ellipseLeft.setSize(18.5f, 250.5f);
        ellipseLeft.setPosition(0, 105);

        stage.addActor(btSignOut);
        stage.addActor(btAchievements);
        stage.addActor(btLeaderBoard);
        stage.addActor(btBack);
        stage.addActor(ellipseLeft);

    }

    @Override
    public void draw(float delta) {
        myCamera.update();
        batcher.setProjectionMatrix(myCamera.combined);

        batcher.disableBlending();
        Assets.parallaxBackground.render(delta);
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public boolean keyDown(int keyPressed) {
        if (keyPressed == Keys.BACK || keyPressed == Keys.ESCAPE) {
            Assets.playSound(Assets.clickSound);
            game.setScreen(new MainMenuScreen(game));
            return true;
        }
        return false;
    }
}
