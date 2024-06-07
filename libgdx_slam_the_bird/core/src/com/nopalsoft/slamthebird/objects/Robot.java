package com.nopalsoft.slamthebird.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.nopalsoft.slamthebird.Assets;
import com.nopalsoft.slamthebird.Settings;

public class Robot {
    public static final float DURATION_DEAD_ANIMATION = 2;
    public static float RADIUS = .28f;
    public static int STATE_FALLING = 0;
    public static int STATE_JUMPING = 1;
    public static int STATE_DEAD = 2;
    public float SPEED_JUMP = 6.25f;
    public float SPEED_MOVE = 5f;
    public float DURATION_SUPER_JUMP = 5;
    public float durationSuperJump;
    public float DURATION_INVINCIBLE = 5;
    public float durationInvincible;
    public Vector2 position;

    public int state;
    public float stateTime;

    public boolean jump, slam;
    public boolean isSuperJump;
    public boolean isInvincible;

    public float angleGrad;
    public Vector2 speed;

    public Robot(float x, float y) {
        position = new Vector2(x, y);
        state = STATE_JUMPING;
        speed = new Vector2();
        jump = true; // To make the first jump

        DURATION_SUPER_JUMP += Settings.LEVEL_BOOST_SUPER_JUMP;
        DURATION_INVINCIBLE += Settings.LEVEL_BOOST_INVINCIBLE;

    }

    public void update(float delta, Body body, float accelerationX, boolean slam) {
        this.slam = slam;// To draw the fast fall =)
        position.x = body.getPosition().x;
        position.y = body.getPosition().y;
        angleGrad = 0;
        if (state == STATE_FALLING || state == STATE_JUMPING) {

            if (slam)
                body.setGravityScale(2.5f);
            else
                body.setGravityScale(1);

            if (jump) {
                jump = false;
                state = STATE_JUMPING;
                stateTime = 0;
                if (isSuperJump) {
                    body.setLinearVelocity(body.getLinearVelocity().x,
                            SPEED_JUMP + 3);
                } else {
                    body.setLinearVelocity(body.getLinearVelocity().x,
                            SPEED_JUMP);
                }
            }

            Vector2 speed = body.getLinearVelocity();

            if (speed.y < 0 && state != STATE_FALLING) {
                state = STATE_FALLING;
                stateTime = 0;
            }
            body.setLinearVelocity(accelerationX * SPEED_MOVE, speed.y);

            if (isSuperJump) {
                durationSuperJump += delta;
                if (durationSuperJump >= DURATION_SUPER_JUMP) {
                    isSuperJump = false;
                    durationSuperJump = 0;
                }
            }

            if (isInvincible) {
                durationInvincible += delta;
                if (durationInvincible >= DURATION_INVINCIBLE) {
                    isInvincible = false;
                    durationInvincible = 0;
                }
            }
        } else if (state == STATE_DEAD) {
            body.setLinearVelocity(0, -3);
            body.setFixedRotation(false);
            angleGrad = (float) Math.toDegrees(body.getAngle());
            body.setAngularVelocity((float) Math.toRadians(20));
        }
        speed = body.getLinearVelocity();
        stateTime += delta;
    }

    public void updateReady(Body body, float acelX) {
        position.x = body.getPosition().x;
        position.y = body.getPosition().y;

        body.setLinearVelocity(acelX * SPEED_MOVE, 0);
        speed = body.getLinearVelocity();
    }

    public void jump() {
        if (state == STATE_FALLING) {
            jump = true;
            stateTime = 0;
            Assets.playSound(Assets.soundJump);
        }
    }

    /**
     * The robot is hit and dies.
     */
    public void hit() {
        state = STATE_DEAD;
        stateTime = 0;
    }

}
