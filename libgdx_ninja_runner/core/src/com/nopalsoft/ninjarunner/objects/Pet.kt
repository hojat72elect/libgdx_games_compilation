package com.nopalsoft.ninjarunner.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Pet {
    public final static int STATE_NORMAL = 0;

    public final static float SPEED = 5f;
    public static final float RADIUS = .25f;
    public final PetType petType;
    public final float drawWidth;
    public final float drawHeight;
    public final float dashDrawWidth;
    public final float dashDrawHeight;
    public final Vector2 position;
    final public Vector2 targetPosition;
    public int state;
    public Vector2 velocity;
    public float stateTime;

    public Pet(float x, float y, PetType petType) {
        this.petType = petType;
        position = new Vector2(x, y);
        targetPosition = new Vector2(x, y);
        velocity = new Vector2();
        state = STATE_NORMAL;
        stateTime = 0;

        switch (petType) {
            case PINK_BIRD:
                drawWidth = .73f;
                drawHeight = .66f;
                dashDrawWidth = 2.36f;
                dashDrawHeight = 1.25f;
                break;
            default:
            case BOMB:
                drawWidth = dashDrawWidth = .52f;
                drawHeight = dashDrawHeight = .64f;
                break;

        }

    }

    public void update(Body body, float delta, float targetX, float targetY) {
        position.x = body.getPosition().x;
        position.y = body.getPosition().y;

        targetPosition.set(targetX, targetY);

        velocity = body.getLinearVelocity();
        velocity.set(targetPosition).sub(position).scl(SPEED);
        body.setLinearVelocity(velocity);
        stateTime += delta;
    }


    public void updateStateTime(float delta) {
        stateTime += delta;

    }

    public enum PetType {
        PINK_BIRD, BOMB

    }

}
