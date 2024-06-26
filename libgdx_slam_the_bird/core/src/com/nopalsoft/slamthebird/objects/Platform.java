package com.nopalsoft.slamthebird.objects;

import com.badlogic.gdx.math.Vector2;

public class Platform {
    final public static int STATE_NORMAL = 0;
    final public static int STATE_CHANGING = 1;
    final public static int STATE_FIRE = 2;
    final public static int STATE_BREAKABLE = 3;
    final public static int STATE_BROKEN = 4;
    public static float WIDTH = .75f;
    public static float HEIGHT = .2f;
    public final float DURATION_ACTIVE = 10; // This time should be less than TIME_TO_CHANGE_STATE_PLATFORM in the WorldGame class.
    final float TIME_TO_BE_ACTIVE = 1.25f;
    public int state;
    public Vector2 position;
    public float stateTime;
    public float changingScale;// For when it changes you see an animation. Start at .5 so that everything doesn't get smaller
    private boolean isFire, isBreakable;

    public Platform(float x, float y) {
        position = new Vector2(x, y);
        state = STATE_NORMAL;
        changingScale = .5f;
    }

    public void update(float delta) {
        stateTime += delta;

        if (state == STATE_CHANGING) {
            changingScale = stateTime / TIME_TO_BE_ACTIVE;// 1.2 maximum scale.

            if (stateTime >= TIME_TO_BE_ACTIVE) {
                if (isFire)
                    state = STATE_FIRE;
                else if (isBreakable)
                    state = STATE_BREAKABLE;
                stateTime = 0;
            }
        }

        if ((state == STATE_FIRE || state == STATE_BREAKABLE || state == STATE_BROKEN) && stateTime >= DURATION_ACTIVE) {
            isBreakable = isFire = false;
            state = STATE_NORMAL;
            stateTime = 0;
            changingScale = .5f;
        }
    }

    public void setFire() {
        state = STATE_CHANGING;
        isFire = true;
        stateTime = 0;
    }

    public void setBreakable() {
        state = STATE_CHANGING;
        isBreakable = true;
        stateTime = 0;
    }

    public void setBroken() {
        state = STATE_BROKEN;
        stateTime = 0;
    }

}
