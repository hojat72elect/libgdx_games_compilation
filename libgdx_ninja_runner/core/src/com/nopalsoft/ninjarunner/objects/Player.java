package com.nopalsoft.ninjarunner.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.nopalsoft.ninjarunner.Assets;
import com.nopalsoft.ninjarunner.Settings;

public class Player {
    public final static int STATE_NORMAL = 0;// NORMAL APLICA PARA RUN,DASH,SLIDE,JUMP
    public final static int STATE_HURT = 1;
    public final static int STATE_DIZZY = 2;
    public final static int STATE_DEAD = 3;
    public final static int STATE_REVIVE = 4;
    public final static int TYPE_GIRL = 0;
    public final static int TYPE_BOY = 1;
    public final static int TYPE_NINJA = 2;
    public final static float DRAW_WIDTH = 1.27f;
    public final static float DRAW_HEIGHT = 1.05f;
    public final static float WIDTH = .55f;
    public final static float HEIGHT = 1f;
    public final static float HEIGHT_SLIDE = .45f;
    public static final float VELOCITY_RUN = 3;
    public static final float VELOCITY_DASH = 7;
    public final static float DURATION_DEAD = Assets.playerDeadAnimation.animationDuration + .5f;
    public final static float DURATION_HURT = Assets.playerHurtAnimation.animationDuration + .1f;
    public final static float DURATION_DIZZY = 1.25f;
    public static float VELOCIDAD_JUMP = 5;
    public final int tipo;
    public final float VELOCIDAD_SECOND_JUMP = 4;
    public final int MAX_VIDAS = Settings.getLEVEL_LIFE() + 5;
    final float DURATION_MAGNET;
    final float DURATION_DASH = 5;
    final Vector2 initialPosition;
    public int state;
    public Vector2 position;
    public float stateTime;
    public boolean isJumping;// To know if I can draw the jumping animation.
    public int numberOfFloorsInContact;// Floors you are currently touching if ==0 you cannot jump.
    public boolean didGetHurtAtLeastOnce;
    /**
     * True if you touch the stairs.
     */

    public int lives;
    public boolean isDash;
    public boolean isSlide;
    public boolean isIdle;
    /**
     * Power
     **/
    public boolean isMagnetEnabled = false;
    float durationMagnet;
    float durationDash;
    private boolean canJump;
    private boolean canDoubleJump;

    public Player(float x, float y, int tipo) {
        position = new Vector2(x, y);
        initialPosition = new Vector2(x, y);
        state = STATE_NORMAL;
        stateTime = 0;
        this.tipo = tipo;
        canJump = true;
        canDoubleJump = true;
        didGetHurtAtLeastOnce = false;
        isIdle = true;

        lives = MAX_VIDAS;
        DURATION_MAGNET = 10;

    }

    public void update(float delta, Body body, boolean didJump, boolean isJumpPressed, boolean dash, boolean didSlide) {
        position.x = body.getPosition().x;
        position.y = body.getPosition().y;

        isIdle = false;

        // It doesn't matter if he's alive/dizzy/or whatever, it takes away this time.
        if (isMagnetEnabled) {
            durationMagnet += delta;
            if (durationMagnet >= DURATION_MAGNET) {
                durationMagnet = 0;
                isMagnetEnabled = false;
            }
        }

        if (state == STATE_REVIVE) {
            state = STATE_NORMAL;
            canJump = true;
            isJumping = false;
            canDoubleJump = true;
            stateTime = 0;
            lives = MAX_VIDAS;
            initialPosition.y = 3;
            position.x = initialPosition.x;
            position.y = initialPosition.y;
            body.setTransform(initialPosition, 0);
            body.setLinearVelocity(0, 0);

        } else if (state == STATE_HURT) {
            stateTime += delta;
            if (stateTime >= DURATION_HURT) {
                state = STATE_NORMAL;
                stateTime = 0;
            }
        } else if (state == STATE_DIZZY) {
            stateTime += delta;
            body.setLinearVelocity(0, body.getLinearVelocity().y);
            if (stateTime >= DURATION_DIZZY) {
                state = STATE_NORMAL;
                stateTime = 0;
            }
            return;
        } else if (state == STATE_DEAD) {
            stateTime += delta;
            body.setLinearVelocity(0, body.getLinearVelocity().y);
            return;
        }

        Vector2 velocity = body.getLinearVelocity();

        if (didJump && (canJump || canDoubleJump)) {
            velocity.y = VELOCIDAD_JUMP;

            if (!canJump) {
                canDoubleJump = false;
                velocity.y = VELOCIDAD_SECOND_JUMP;
            }

            canJump = false;
            isJumping = true;
            stateTime = 0;

            isSlide = false;

            body.setGravityScale(.9f);
            Assets.playSound(Assets.jump, 1);// FIXME Fix the sound

        }
        if (!isJumpPressed)
            body.setGravityScale(1);

        if (!isJumping) {
            isSlide = didSlide;
        }

        // DASH
        if (dash) {
            isDash = true;
            durationDash = 0;
        }

        if (isDash) {
            durationDash += delta;
            velocity.x = VELOCITY_DASH;
            if (durationDash >= DURATION_DASH) {
                isDash = false;
                stateTime = 0;
                velocity.x = VELOCITY_RUN;
            }
        } else {
            velocity.x = VELOCITY_RUN;
        }
        stateTime += delta;

        body.setLinearVelocity(velocity);

    }

    public void getHurt() {
        if (state != STATE_NORMAL)
            return;

        lives--;
        if (lives > 0) {
            state = STATE_HURT;
        } else {
            state = STATE_DEAD;
        }
        stateTime = 0;
        didGetHurtAtLeastOnce = true;
    }

    public void getDizzy() {
        if (state != STATE_NORMAL)
            return;

        lives--;
        if (lives > 0) {
            state = STATE_DIZZY;
        } else {
            state = STATE_DEAD;
        }
        stateTime = 0;
        didGetHurtAtLeastOnce = true;
    }

    public void die() {
        if (state != STATE_DEAD) {
            lives = 0;

            state = STATE_DEAD;
            stateTime = 0;
        }
    }

    public void touchFloor() {
        numberOfFloorsInContact++;

        canJump = true;
        isJumping = false;
        canDoubleJump = true;
        if (state == STATE_NORMAL)
            stateTime = 0;
    }

    public void endTouchFloor() {
        numberOfFloorsInContact--;
        if (numberOfFloorsInContact == 0) {
            canJump = false;

            // If I stop touching the floor because I jump, I can still jump again.
            if (!isJumping)
                canDoubleJump = false;
        }

    }

    public void updateStateTime(float delta) {
        stateTime += delta;
    }

    public void setPickUpMagnet() {
        durationMagnet = 0;
        isMagnetEnabled = true;

    }
}
