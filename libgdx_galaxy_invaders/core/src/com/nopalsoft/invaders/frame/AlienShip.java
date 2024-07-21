package com.nopalsoft.invaders.frame;

public class AlienShip extends DynamicGameObject {

	public static final float RADIUS = 1.5f;

	public static final float DRAW_WIDTH = 3.5f;
	public static final float DRAW_HEIGHT = 3.5f;

	public static final int MOVE_SIDES = 0;
	public static final int MOVE_DOWN = 2;
	public static final int EXPLODING = 3;
	public static final float SPEED = 4f;
	public static final float SPEED_DOWN = -3.5f;

	public static final float MOVE_RANGE_SIDES = 6.7f;
	public static final float MOVE_RANGE_DOWN = 1.2f;
	public static final float EXPLODE_TIME = 0.05f * 19;

	public final int SIMPLE_SCORE = 10;

	public int livesLeft;
	public int punctuation;
	public float stateTime;
	public int state;
	float movedDistance;
	float increaseSpeed;

	public AlienShip(int life, float increaseSpeed, float x, float y) {
		super(x, y, RADIUS);
		stateTime = 0;
		state = MOVE_SIDES;
		velocity.set(SPEED, SPEED_DOWN);
		movedDistance = 0;
		punctuation = SIMPLE_SCORE;
		livesLeft = life;
		this.increaseSpeed = 1 + increaseSpeed;
	}

	public void update(float deltaTime) {
		if (state != EXPLODING) {
			switch (state) {
				case MOVE_SIDES:
					position.x += velocity.x * deltaTime * increaseSpeed;
					movedDistance += Math.abs(velocity.x * deltaTime) * increaseSpeed;
					if (movedDistance > MOVE_RANGE_SIDES) {
						state = MOVE_DOWN;
						velocity.x *= -1;
						movedDistance = 0;
					}
					break;
				case MOVE_DOWN:
					position.y += velocity.y * deltaTime * increaseSpeed;
					movedDistance += Math.abs(velocity.x * deltaTime) * increaseSpeed;
					if (movedDistance > MOVE_RANGE_DOWN) {
						state = MOVE_SIDES;
						movedDistance = 0;
					}
					break;
			}
		}

		boundsCircle.x = position.x;
		boundsCircle.y = position.y;
		stateTime += deltaTime;
	}

	public void beingHit(int powerBullet) {
		livesLeft--;
		if (livesLeft <= 0) {
			state = EXPLODING;
			velocity.add(0, 0);
			stateTime = 0;
		}
	}

	/**
	 * Call this method is bullet power 1.
	 */
	public void beingHit() {
		beingHit(1);
	}
}
