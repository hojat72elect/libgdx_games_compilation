package com.nopalsoft.invaders.frame;

import com.badlogic.gdx.Gdx;
import com.nopalsoft.invaders.game.World;

public class Ship extends DynamicGameObject {

    public static final float DRAW_WIDTH = 4.5f;
    public static final float DRAW_HEIGHT = 3.6f;

    public static final float WIDTH = 4f;
    public static final float HEIGHT = 2.5f;

    public static final float NAVE_MOVE_SPEED = 50;

    public static final int NAVE_STATE_NORMAL = 0;
    public static final int NAVE_STATE_EXPLODE = 1;
    public static final int NAVE_STATE_BEING_HIT = 2;

    public static final float EXPLODE_TIME = 0.05f * 19;
    public static final float HIT_TIME = 0.05f * 21; // uno mas pa que tenga tantillo tiempo de pensar jaja

    public int livesShield;
    public int lives;
    public int state;
    public float stateTime;

    public Ship(float x, float y) {
        super(x, y, WIDTH, HEIGHT);
        lives = 3;
        livesShield = 1;// empizas con 1 de shield por si los putos te pegan
        state = NAVE_STATE_NORMAL;
        Gdx.app.log("Estado", "Se creo la nave");
    }

    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        boundsRectangle.x = position.x - boundsRectangle.width / 2;
        boundsRectangle.y = position.y - boundsRectangle.height / 2;

        if (state == NAVE_STATE_BEING_HIT && stateTime > HIT_TIME) {
            state = NAVE_STATE_NORMAL;
            stateTime = 0;
            Gdx.app.log("Estado", "Se cambio a normal");
        }

        if (position.x < WIDTH / 2)
            position.x = WIDTH / 2;
        if (position.x > World.WIDTH - WIDTH / 2)
            position.x = World.WIDTH - WIDTH / 2;
        stateTime += deltaTime;
    }

    public void beingHit() {
        if (livesShield > 0) {
            livesShield--;
        } else {
            lives--;
            if (lives <= 0) {
                state = NAVE_STATE_EXPLODE;
                stateTime = 0;
                velocity.set(0, 0);
            } else {
                state = NAVE_STATE_BEING_HIT;
                stateTime = 0;
            }
        }
    }

    public void hitExtraLife() {
        if (lives < 99) {
            lives++;
        }
    }

    public void hitShield() {
        stateTime = 0;
        livesShield = 3;
    }

}
