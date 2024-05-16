package com.nopalsoft.sokoban.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.nopalsoft.sokoban.Settings;

import java.util.LinkedHashMap;

/**
 * All objects are useful (the floor, the character, the boxes).
 */
public class Tiles extends Actor {

    final static LinkedHashMap<Integer, Vector2> mapPositions = new LinkedHashMap<>();

    static {
        // Positions start from left to right, bottom to top.
        int posicionTile = 0;
        for (int y = 0; y < 15; y++) {
            for (int x = 0; x < 25; x++) {
                mapPositions.put(posicionTile, new Vector2(x * 32 * com.nopalsoft.sokoban.game.Board.UNIT_SCALE, y * 32 * com.nopalsoft.sokoban.game.Board.UNIT_SCALE));
                posicionTile++;
            }
        }
    }

    // ALL MAPS ARE 25x15 Tiles of 32px which gives a resolution of 800x480
    final float SIZE = 32 * com.nopalsoft.sokoban.game.Board.UNIT_SCALE;// Token size

    public int position;

    public Tiles(int position) {
        this.position = position;
        setSize(SIZE, SIZE);
        setPosition(mapPositions.get(position).x, mapPositions.get(position).y);

    }

    /**
     * If it is UNDO it moves without animation (quickFix).
     */
    public void moveToPosition(int pos, boolean undo) {
        float time = .05f;
        if (Settings.getAnimationWalkIsON() && !undo)
            time = .45f;
        this.position = pos;
        addAction(Actions.sequence(Actions.moveTo(mapPositions.get(position).x, mapPositions.get(position).y, time), Actions.run(this::endMovingToPosition)));
    }

    /**
     * It is called automatically when it has already been moved to the position.
     */
    protected void endMovingToPosition() {

    }

}
