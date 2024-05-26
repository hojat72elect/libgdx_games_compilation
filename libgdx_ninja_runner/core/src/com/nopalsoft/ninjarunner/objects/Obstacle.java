package com.nopalsoft.ninjarunner.objects;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.nopalsoft.ninjarunner.Assets;

public class Obstacle implements Poolable {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_DESTROY = 1;
    public final Vector2 position;
    public int state;
    public float stateTime;

    public PooledEffect effect;

    public Obstacle() {
        position = new Vector2();
    }

    public void initializeObstacle(float x, float y) {
        position.set(x, y);
        state = STATE_NORMAL;
        stateTime = 0;

    }

    public void update(float delta) {
        stateTime += delta;
    }

    public void setDestroy() {
        if (state == STATE_NORMAL) {
            state = STATE_DESTROY;
            stateTime = 0;
            effect = Assets.boxesEffectPool.obtain();
            effect.setPosition(position.x, position.y);
        }
    }

    @Override
    public void reset() {
    }
}
