package com.nopalsoft.slamthebird.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.nopalsoft.slamthebird.Settings;

import java.util.Random;

public class Enemy {
    public static float WIDTH = .4f;
    public static float HEIGHT = .4f;

    public static int STATE_JUST_APPEAR = 0;
    public static int STATE_FLYING = 1;
    public static int STATE_HIT = 2;
    public static int STATE_EVOLVING = 3;// For it to fly again
    public static int STATE_DEAD = 4;
    public static float MAX_SPEED_BLUE = 1.75f;
    public static float MAX_SPEED_RED = 3.25f;
    public float TIME_JUST_APPEAR = 1.7f;
    public float TIME_TO_CHANGE_VEL = 3;
    public float timeToChangeVel;

    public float TIME_TO_EVOLVE = 3f;
    public float timeToEvolve;

    public float DURARTION_EVOLVING = 1.5f;

    public float DURARTION_FROZEN = 5f;
    public Vector2 position;
    public Vector2 speed;
    public boolean isFrozen;
    public int state;
    public float stateTime;
    public int lives;
    public float appearScale;
    float durationFrozen;

    public Enemy(float x, float y) {
        position = new Vector2(x, y);
        state = STATE_JUST_APPEAR;
        lives = 2;
        stateTime = 0;
        speed = new Vector2();
        isFrozen = false;
        durationFrozen = 0;
        DURARTION_FROZEN += Settings.LEVEL_BOOST_ICE;

    }

    public void update(float delta, Body body, Random random) {
        position.x = body.getPosition().x;
        position.y = body.getPosition().y;

        if (isFrozen) {
            body.setLinearVelocity(0, 0);
            if (durationFrozen >= DURARTION_FROZEN) {
                isFrozen = false;
                durationFrozen = 0;
                setNewVelocity(body, random, false);
            }
            durationFrozen += delta;
            return;// Nothing more is done if it is frozen. It doesn't move, it doesn't change speed, it doesn't evolve, it doesn't swim.
        }

        // Whatever happens, I don't want it to be above 10f.
        if (position.y > 10f) {
            speed = body.getLinearVelocity();
            body.setLinearVelocity(speed.x, speed.y * -1);
        }
        if (state == STATE_JUST_APPEAR) {
            appearScale = stateTime * 1.5f / TIME_JUST_APPEAR;// 1.5f maximum scale

            if (stateTime >= TIME_JUST_APPEAR) {
                state = STATE_FLYING;
                stateTime = 0;
                setNewVelocity(body, random, false);
            }
        }

        if (state != STATE_JUST_APPEAR) {

            timeToChangeVel += delta;
            if (timeToChangeVel >= TIME_TO_CHANGE_VEL) {
                timeToChangeVel -= TIME_TO_CHANGE_VEL;

                Vector2 vel = body.getLinearVelocity();

                // Change in X
                if (random.nextBoolean())
                    vel.x *= -1;

                if (state == STATE_FLYING) {
                    if (random.nextBoolean())
                        vel.y *= -1;
                }
                body.setLinearVelocity(vel);
            }
        }

        if (state == STATE_HIT) {
            body.setGravityScale(1);
            timeToEvolve += delta;
            if (timeToEvolve >= TIME_TO_EVOLVE) {
                state = STATE_EVOLVING;
                stateTime = 0;
                timeToEvolve = 0;
            }
        }

        if (state == STATE_EVOLVING && stateTime >= DURARTION_EVOLVING) {
            state = STATE_FLYING;
            body.setGravityScale(0);
            setNewVelocity(body, random, true);
            lives = 3;
            stateTime = 0;
        }

        speed = body.getLinearVelocity();

        limitSpeed(body);
        speed = body.getLinearVelocity();

        stateTime += delta;
    }

    /*
     * Limit the speed because sometimes the force resulting from the collision drove the enemy crazy.
     */
    private void limitSpeed(Body body) {
        float vel = MAX_SPEED_BLUE;
        if (lives == 3)
            vel = MAX_SPEED_RED;

        if (speed.x > vel) {
            speed.x = vel;
        } else if (speed.x < -vel) {
            speed.x = -vel;
        }

        if (lives > 1) {// So the bird fell quickly if I took off its wings.
            if (speed.y > vel) {
                speed.y = vel;
            } else if (speed.y < -vel) {
                speed.y = -vel;
            }
        }
        body.setLinearVelocity(speed);

    }

    /**
     * If it is touching the floor I make the Y velocity always generated positive.
     */
    private void setNewVelocity(Body body, Random random, boolean isTouchingFLoor) {
        float vel = MAX_SPEED_BLUE;
        if (lives == 3)
            vel = MAX_SPEED_RED;

        float velocityX = random.nextFloat() * vel * 2 - vel;
        float velocityY;
        if (isTouchingFLoor)
            velocityY = random.nextFloat() * vel;
        else
            velocityY = random.nextFloat() * vel * 2 - vel;

        body.setLinearVelocity(velocityX, velocityY);
    }

    public void hit() {
        lives--;
        if (lives == 1)
            state = STATE_HIT;
        else if (lives == 0)
            state = STATE_DEAD;

        stateTime = 0;

    }

    public void die() {
        lives = 0;
        state = STATE_DEAD;
        stateTime = 0;
    }

    public void setFrozen() {
        durationFrozen = 0;
        isFrozen = true;
    }

}
