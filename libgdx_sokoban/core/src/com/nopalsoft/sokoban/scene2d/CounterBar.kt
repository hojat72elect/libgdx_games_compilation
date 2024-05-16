package com.nopalsoft.sokoban.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.sokoban.Assets;

public class CounterBar extends Table {
    float WIDTH = 125;
    float HEIGHT = 42;

    Label labelDisplay;

    public CounterBar(TextureRegionDrawable fondo, float x, float y) {

        this.setBounds(x, y, WIDTH, HEIGHT);
        setBackground(fondo);

        labelDisplay = new Label("", new LabelStyle(Assets.fontRed, Color.WHITE));
        labelDisplay.setFontScale(.8f);
        add(labelDisplay);

        center();
        padLeft(25);
        padBottom(5);
    }

    public void updateActualNum(int actualNum) {
        labelDisplay.setText(actualNum + "");
    }

}
