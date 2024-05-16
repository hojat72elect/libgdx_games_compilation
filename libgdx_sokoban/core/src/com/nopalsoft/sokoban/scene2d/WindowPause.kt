package com.nopalsoft.sokoban.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.sokoban.Assets;
import com.nopalsoft.sokoban.Settings;
import com.nopalsoft.sokoban.game.GameScreen;
import com.nopalsoft.sokoban.screens.MainMenuScreen;
import com.nopalsoft.sokoban.screens.Screens;

public class WindowPause extends Window {

    Button buttonHome, buttonRefresh;
    Table tableAnimations;

    public WindowPause(Screens currentScreen) {
        super(currentScreen, 350, 300, 100);

        setCloseButton();
        setTitle("Paused", 1);

        Table menuTable = new Table();
        menuTable.setFillParent(true);

        buttonHome = new Button(Assets.buttonHome, Assets.buttonHomePressed);
        buttonHome.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.changeScreenWithFadeOut(MainMenuScreen.class, screen.game);
            }
        });

        buttonRefresh = new Button(Assets.buttonRefresh, Assets.buttonRefreshPressed);
        buttonRefresh.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.changeScreenWithFadeOut(GameScreen.class, ((GameScreen) screen).level, screen.game);
            }
        });

        final Button btAnimations = new Button(Assets.buttonOff, Assets.buttonOn, Assets.buttonOn);
        btAnimations.setChecked(Settings.getAnimationWalkIsON());

        tableAnimations = new Table();
        tableAnimations.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                Settings.setAnimationWalkIsON(!Settings.getAnimationWalkIsON());

                btAnimations.setChecked(Settings.getAnimationWalkIsON());
                Settings.save();
            }
        });

        menuTable.defaults().expandX();

        menuTable.pad(30).padTop(55);
        menuTable.add(buttonHome);
        menuTable.add(buttonRefresh);
        menuTable.row();

        Label lbAnimatons = new Label("Animations", new LabelStyle(Assets.fontRed, Color.WHITE));
        tableAnimations.add(lbAnimatons);
        tableAnimations.add(btAnimations).padLeft(15);

        menuTable.add(tableAnimations).colspan(2).padTop(10);

        addActor(menuTable);

    }

    @Override
    public void hideCompleted() {
        ((GameScreen) screen).setRunning();

    }

}
