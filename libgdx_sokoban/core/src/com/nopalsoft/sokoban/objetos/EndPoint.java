package com.nopalsoft.sokoban.objetos;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.nopalsoft.sokoban.Assets;

public class EndPoint extends Tiles {
    int numColor;

    AtlasRegion keyFrame;

    public EndPoint(int posicion, String color) {
        super(posicion);

        switch (color) {
            case "brown":
                numColor = com.nopalsoft.sokoban.objetos.Box.COLOR_BROWN;
                break;
            case "gray":
                numColor = com.nopalsoft.sokoban.objetos.Box.COLOR_GRAY;
                break;
            case "purple":
                numColor = com.nopalsoft.sokoban.objetos.Box.COLOR_PURPLE;
                break;
            case "blue":
                numColor = com.nopalsoft.sokoban.objetos.Box.COLOR_BLUE;
                break;
            case "black":
                numColor = com.nopalsoft.sokoban.objetos.Box.COLOR_BLACK;
                break;
            case "beige":
                numColor = com.nopalsoft.sokoban.objetos.Box.COLOR_BEIGE;
                break;
            case "yellow":
                numColor = com.nopalsoft.sokoban.objetos.Box.COLOR_YELLOW;
                break;
            case "red":
                numColor = com.nopalsoft.sokoban.objetos.Box.COLOR_RED;
                break;
        }

        setTextureColor(numColor);
    }

    private void setTextureColor(int numColor) {
        switch (numColor) {
            case Box.COLOR_BEIGE:
                keyFrame = Assets.endPointBeige;
                break;

            case Box.COLOR_BLACK:
                keyFrame = Assets.endPointBlack;
                break;

            case Box.COLOR_BLUE:
                keyFrame = Assets.endPointBlue;
                break;

            case Box.COLOR_BROWN:
                keyFrame = Assets.endPointBrown;
                break;

            case Box.COLOR_GRAY:
                keyFrame = Assets.endPointGray;
                break;

            case Box.COLOR_RED:
                keyFrame = Assets.endPointRed;
                break;

            case Box.COLOR_YELLOW:
                keyFrame = Assets.endPointYellow;
                break;

            case Box.COLOR_PURPLE:
                keyFrame = Assets.endPointPurple;
                break;

        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(keyFrame, getX(), getY(), SIZE, SIZE);
    }
}
