package com.nopalsoft.ninjarunner.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.nopalsoft.ninjarunner.Assets;


public class Missile implements Poolable, Comparable<Missile> {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_EXPLODE = 1;
    public final static int STATE_DESTROY = 2;
    public static final float WIDTH = 1.27f;
    public static final float HEIGHT = .44f;
    public static final float SPEED_X = -2.5f;
    private final static float EXPLOSION_DURATION = Assets.explosion.animationDuration + .1f;
    public final Vector2 position;
    public int state;
    public float stateTime;
    public float distanceFromCharacter;

    public Missile() {
        position = new Vector2();
    }

    public void init(float x, float y) {
        position.set(x, y);
        state = STATE_NORMAL;
        stateTime = 0;

    }

    public void update(float delta, Body body, Player myPlayer) {
        if (state == STATE_NORMAL) {
            position.x = body.getPosition().x;
            position.y = body.getPosition().y;


        }
        if (state == STATE_EXPLODE) {

            if (stateTime >= EXPLOSION_DURATION) {
                state = STATE_DESTROY;
                stateTime = 0;
            }
        }

        distanceFromCharacter = myPlayer.position.dst(position);
        stateTime += delta;
    }

    public void setHitTarget() {
        if (state == STATE_NORMAL) {
            state = STATE_EXPLODE;
            stateTime = 0;
        }
    }

    public void setDestroy() {
        if (state != STATE_DESTROY) {
            state = STATE_DESTROY;
            stateTime = 0;
        }
    }

    @Override
    public void reset() {
    }

    @Override
    public int compareTo(Missile o2) {
        return Float.compare(distanceFromCharacter, o2.distanceFromCharacter);
    }

}
