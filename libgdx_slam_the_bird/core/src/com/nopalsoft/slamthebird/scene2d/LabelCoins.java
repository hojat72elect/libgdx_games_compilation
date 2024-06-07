package com.nopalsoft.slamthebird.scene2d;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nopalsoft.slamthebird.Assets;

public class LabelCoins extends Actor {
    int numberOfCoins;

    public LabelCoins(float x, float y, int numberOfCoins) {
        this.numberOfCoins = numberOfCoins;
        this.setPosition(x, y);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawPuntuacionChicoOrigenDerecha(batch, this.getX(), this.getY(), numberOfCoins);

    }

    public void drawPuntuacionChicoOrigenDerecha(Batch batcher, float x, float y, int numMonedas) {
        String score = String.valueOf(numMonedas);

        int len = score.length();
        float charWidth;
        float textWidth = 0;
        for (int i = len - 1; i >= 0; i--) {
            AtlasRegion keyFrame;

            charWidth = 22;
            char character = score.charAt(i);

            if (character == '0') {
                keyFrame = Assets.num0Small;
            } else if (character == '1') {
                keyFrame = Assets.num1Small;
                charWidth = 11f;
            } else if (character == '2') {
                keyFrame = Assets.num2Small;
            } else if (character == '3') {
                keyFrame = Assets.num3Small;
            } else if (character == '4') {
                keyFrame = Assets.num4Small;
            } else if (character == '5') {
                keyFrame = Assets.num5Small;
            } else if (character == '6') {
                keyFrame = Assets.num6Small;
            } else if (character == '7') {
                keyFrame = Assets.num7Small;
            } else if (character == '8') {
                keyFrame = Assets.num8Small;
            } else {// 9
                keyFrame = Assets.num9Small;
            }
            textWidth += charWidth;
            batcher.draw(keyFrame, x - textWidth, y, charWidth, 32);
        }
    }
}
