package com.nopalsoft.superjumper.objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Pools;
import com.nopalsoft.superjumper.screens.Screens;

public class Coin implements Poolable {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_TAKEN = 1;
	public int state;

	public final static float DRAW_WIDTH = .27f;
	public final static float DRAW_HEIGHT = .34f;
	public final static float WIDTH = .25f;
	public final static float HEIGHT = .32f;

	public final Vector2 position;

	public float stateTime;

	public Coin() {
		position = new Vector2();
	}

	public void initializeCoin(float x, float y) {
		position.set(x, y);
		state = STATE_NORMAL;
		stateTime = 0;
	}

	public void update(float delta) {
		stateTime += delta;
	}

	public void take() {
		state = STATE_TAKEN;
		stateTime = 0;
	}

	final static float COINS_SEPARATION = .025f;// Variable so that the coins are not stuck

	public static void createCoins(World worldBox, Array<Coin> coinsArray, float y) {
		createACoupleOfCoins(worldBox, coinsArray, y);
	}

	public static void createACoin(World worldBox, Array<Coin> arrayCoins, float y) {
		createCoins(worldBox, arrayCoins, generaPosX(1), y);
	}

	private static void createACoupleOfCoins(World worldBox, Array<Coin> arrayCoins, float y) {
		int maxRow = MathUtils.random(25) + 1;
		int maxColumn = MathUtils.random(6) + 1;

		float x = generaPosX(maxColumn);
		for (int column = 0; column < maxColumn; column++) {
			for (int row = 0; row < maxRow; row++) {
				createCoins(worldBox, arrayCoins, x + (column * (WIDTH + COINS_SEPARATION)), y + (row * (HEIGHT + COINS_SEPARATION)));
			}
		}

	}

	/**
	 * It generates a position in X depending on the number of lines of the line so that they do not get out of the screen on the right or on the left.
     */
	private static float generaPosX(int numberOfLineCoins) {
		float x = MathUtils.random(Screens.WORLD_WIDTH) + WIDTH / 2f;
		if (x + (numberOfLineCoins * (WIDTH + COINS_SEPARATION)) > Screens.WORLD_WIDTH) {
			x -= (x + (numberOfLineCoins * (WIDTH + COINS_SEPARATION))) - Screens.WORLD_WIDTH;// Make the difference from the width and what is happening.
			x += WIDTH / 2f;// Adds half to be stuck.
		}
		return x;
	}

	private static void createCoins(World worldBox, Array<Coin> arrayCoins, float x, float y) {
		Coin coin = Pools.obtain(Coin.class);
		coin.initializeCoin(x, y);

		BodyDef bd = new BodyDef();
		bd.position.x = x;
		bd.position.y = y;
		bd.type = BodyType.StaticBody;
		Body oBody = worldBox.createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(WIDTH / 2f, HEIGHT / 2f);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.isSensor = true;
		oBody.createFixture(fixture);
		oBody.setUserData(coin);
		shape.dispose();
		arrayCoins.add(coin);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

}
