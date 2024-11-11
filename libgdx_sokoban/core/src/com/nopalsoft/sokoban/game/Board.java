package com.nopalsoft.sokoban.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterator;
import com.nopalsoft.sokoban.Assets;
import com.nopalsoft.sokoban.game_objects.Box;
import com.nopalsoft.sokoban.game_objects.EndPoint;
import com.nopalsoft.sokoban.game_objects.Pared;
import com.nopalsoft.sokoban.game_objects.Tiles;

public class Board extends Group {
	public static final float UNIT_SCALE = 1f;

	static public final int STATE_RUNNING = 1;
	static public final int STATE_GAMEOVER = 2;
	public int state;

	/**
	 * X previous position, Y current position.
	 */
	Array<Vector2> arrMovesPersonaje;

	/**
	 * X previous position, Y current position
	 */
	Array<Vector2> arrMovesCaja;

	Array<Tiles> arrTiles;
	private com.nopalsoft.sokoban.game_objects.Player player;

	public boolean moveUp, moveDown, moveLeft, moveRight;
	public boolean undo;

	int moves;
	float time;

	public Board() {
		setSize(800, 480);

		arrTiles = new Array<>(25 * 15);
		arrMovesPersonaje = new Array<>();
		arrMovesCaja = new Array<>();

		initializeMap("StaticMap");
		initializeMap("Objetos");

		// AFTER initializing the objects I add them to the Board in order so that some are drawn before others.
		agregarAlTablero(Pared.class);
		agregarAlTablero(EndPoint.class);
		agregarAlTablero(Box.class);
		agregarAlTablero(com.nopalsoft.sokoban.game_objects.Player.class);

		state = STATE_RUNNING;

		time = moves = 0;
	}

	private void agregarAlTablero(Class<?> tipo) {
        for (Tiles obj : arrTiles) {
            if (obj.getClass() == tipo) {
                addActor(obj);
            }

        }
	}

	private void initializeMap(String layerName) {
		TiledMapTileLayer layer = (TiledMapTileLayer) Assets.map.getLayers().get(layerName);
		if (layer != null) {

			int posTile = 0;
			for (int y = 0; y < 15; y++) {
				for (int x = 0; x < 25; x++) {
					Cell cell = layer.getCell(x, y);
					if (cell != null) {
						TiledMapTile tile = cell.getTile();
						if (tile.getProperties() != null) {
							if (tile.getProperties().containsKey("tipo")) {
								String tipo = tile.getProperties().get("tipo").toString();

								if (tipo.equals("startPoint")) {
									crearPersonaje(posTile);
								}
								else if (tipo.equals("pared")) {
									crearPared(posTile);
								}
								else if (tipo.equals("caja")) {
									crearCaja(posTile, tile.getProperties().get("color").toString());
								}
								else if (tipo.equals("endPoint")) {
									crearEndPoint(posTile, tile.getProperties().get("color").toString());
								}

							}
						}
					}
					posTile++;
				}
			}
		}
	}

	private void crearPersonaje(int posTile) {
		com.nopalsoft.sokoban.game_objects.Player obj = new com.nopalsoft.sokoban.game_objects.Player(posTile);
		arrTiles.add(obj);
		player = obj;

	}

	private void crearPared(int posTile) {
		Pared obj = new Pared(posTile);
		arrTiles.add(obj);

	}

	private void crearCaja(int posTile, String color) {
		Box obj = new Box(posTile, color);
		arrTiles.add(obj);
	}

	private void crearEndPoint(int posTile, String color) {
		EndPoint obj = new EndPoint(posTile, color);
		arrTiles.add(obj);
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		if (state == STATE_RUNNING) {

			if (moves <= 0)
				undo = false;

			if (undo) {
				undo();
			}
			else {
				int auxMoves = 0;
				if (moveUp) {
					auxMoves = 25;
				}
				else if (moveDown) {
					auxMoves = -25;
				}
				else if (moveLeft) {
					auxMoves = -1;
				}
				else if (moveRight) {
					auxMoves = 1;
				}

				if (player.canMove() && (moveDown || moveLeft || moveRight || moveUp)) {
					int nextPos = player.position + auxMoves;

					if (checarEspacioVacio(nextPos) || (!checarIsBoxInPosition(nextPos) && checarIsEndInPosition(nextPos))) {
						arrMovesPersonaje.add(new Vector2(player.position, nextPos));
						arrMovesCaja.add(null);
						player.moveToPosition(nextPos, moveUp, moveDown, moveRight, moveLeft);
						moves++;
					}
					else {
						if (checarIsBoxInPosition(nextPos)) {
							int boxNextPos = nextPos + auxMoves;
							if (checarEspacioVacio(boxNextPos) || (!checarIsBoxInPosition(boxNextPos) && checarIsEndInPosition(boxNextPos))) {
								Box oBox = getBoxInPosition(nextPos);

								arrMovesPersonaje.add(new Vector2(player.position, nextPos));
								arrMovesCaja.add(new Vector2(oBox.position, boxNextPos));
								moves++;

								oBox.moveToPosition(boxNextPos, false);
								player.moveToPosition(nextPos, moveUp, moveDown, moveRight, moveLeft);
								oBox.setIsInEndPoint(getEndPointInPosition(boxNextPos));

							}
						}
					}
				}

				moveDown = moveLeft = moveRight = moveUp = false;

				if (checkBoxesMissingTheRightEndPoint() == 0)
					state = STATE_GAMEOVER;

			}

			if (state == STATE_RUNNING)
				time += Gdx.graphics.getRawDeltaTime();
		}
	}

	private void undo() {
		if (arrMovesPersonaje.size >= moves) {
			Vector2 posAntPersonaje = arrMovesPersonaje.removeIndex(moves - 1);
			player.moveToPosition((int) posAntPersonaje.x, true);
		}
		if (arrMovesCaja.size >= moves) {
			Vector2 posAntBox = arrMovesCaja.removeIndex(moves - 1);
			if (posAntBox != null) {
				Box oBox = getBoxInPosition((int) posAntBox.y);
				oBox.moveToPosition((int) posAntBox.x, true);
				oBox.setIsInEndPoint(getEndPointInPosition(oBox.position));
			}
		}
		moves--;
		undo = false;
	}

	private boolean checarEspacioVacio(int pos) {
		ArrayIterator<Tiles> ite = new ArrayIterator<Tiles>(arrTiles);
		while (ite.hasNext()) {
			if (ite.next().position == pos)
				return false;
		}
		return true;
	}

	/**
	 * Indica si el objeto en la posicion es una caja
	 *
	 * @param pos
	 */
	private boolean checarIsBoxInPosition(int pos) {
		boolean isBoxInPosition = false;
		ArrayIterator<Tiles> ite = new ArrayIterator<Tiles>(arrTiles);
		while (ite.hasNext()) {
			Tiles obj = ite.next();
			if (obj.position == pos && obj instanceof Box)
				isBoxInPosition = true;
		}
		return isBoxInPosition;

	}

	/**
	 * Indica si el objeto en la posicion es endPoint
	 *
	 * @param pos
	 */
	private boolean checarIsEndInPosition(int pos) {
		boolean isEndPointInPosition = false;
		ArrayIterator<Tiles> ite = new ArrayIterator<Tiles>(arrTiles);
		while (ite.hasNext()) {
			Tiles obj = ite.next();
			if (obj.position == pos && obj instanceof EndPoint)
				isEndPointInPosition = true;
		}
		return isEndPointInPosition;

	}

	private Box getBoxInPosition(int pos) {
		ArrayIterator<Tiles> ite = new ArrayIterator<Tiles>(arrTiles);
		while (ite.hasNext()) {
			Tiles obj = ite.next();
			if (obj.position == pos && obj instanceof Box)
				return (Box) obj;
		}
		return null;
	}

	private EndPoint getEndPointInPosition(int pos) {
		ArrayIterator<Tiles> ite = new ArrayIterator<Tiles>(arrTiles);
		while (ite.hasNext()) {
			Tiles obj = ite.next();
			if (obj.position == pos && obj instanceof EndPoint)
				return (EndPoint) obj;
		}
		return null;
	}

	private int checkBoxesMissingTheRightEndPoint() {
		int numBox = 0;
		ArrayIterator<Tiles> ite = new ArrayIterator<Tiles>(arrTiles);
		while (ite.hasNext()) {
			Tiles obj = ite.next();
			if (obj instanceof Box) {
				Box oBox = (Box) obj;
				if (!oBox.isInRightEndPoint)
					numBox++;
			}

		}
		return numBox;
	}

}
