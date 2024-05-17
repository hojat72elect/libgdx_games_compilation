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
import com.nopalsoft.sokoban.objects.Box;
import com.nopalsoft.sokoban.objects.EndPoint;
import com.nopalsoft.sokoban.objects.Player;
import com.nopalsoft.sokoban.objects.Tiles;
import com.nopalsoft.sokoban.objects.Wall;

public class Board extends Group {
    public static final float UNIT_SCALE = 1f;

    static public final int STATE_RUNNING = 1;
    static public final int STATE_GAMEOVER = 2;
    public int state;
    public boolean moveUp, moveDown, moveLeft, moveRight;
    public boolean undo;
    // X previous position, Y current position.
    Array<Vector2> arrayOfCharacterMoves;
    // X previous position, Y current position.
    Array<Vector2> arrayMovesBox;
    Array<Tiles> arrayTiles;
    int moves;
    float time;
    private Player player;

    public Board() {
        setSize(800, 480);

        arrayTiles = new Array<>(25 * 15);
        arrayOfCharacterMoves = new Array<>();
        arrayMovesBox = new Array<>();

        initializeMap("StaticMap");
        initializeMap("Objetos");

        // AFTER initializing the objects I add them to the Board in order so that some
        // are drawn before others.
        addToBoard(Wall.class);
        addToBoard(EndPoint.class);
        addToBoard(Box.class);
        addToBoard(Player.class);

        state = STATE_RUNNING;
        time = moves = 0;
    }

    private void addToBoard(Class<?> type) {
        for (Tiles obj : arrayTiles) {
            if (obj.getClass() == type) {
                addActor(obj);
            }

        }
    }

    private void initializeMap(String layerName) {
        TiledMapTileLayer layer = (TiledMapTileLayer) Assets.map.getLayers().get(layerName);
        if (layer != null) {

            int tilePosition = 0;
            for (int y = 0; y < 15; y++) {
                for (int x = 0; x < 25; x++) {
                    Cell cell = layer.getCell(x, y);
                    if (cell != null) {
                        TiledMapTile tile = cell.getTile();
                        if (tile.getProperties() != null) {
                            if (tile.getProperties().containsKey("tipo")) {
                                String tipo = tile.getProperties().get("tipo").toString();

                                switch (tipo) {
                                    case "startPoint":
                                        createPerson(tilePosition);
                                        break;
                                    case "pared":
                                        createWall(tilePosition);
                                        break;
                                    case "caja":
                                        createBox(tilePosition, tile.getProperties().get("color").toString());
                                        break;
                                    case "endPoint":
                                        createEndPoint(tilePosition, tile.getProperties().get("color").toString());
                                        break;
                                }

                            }
                        }
                    }
                    tilePosition++;
                }
            }
        }
    }

    private void createPerson(int tilePosition) {
        Player obj = new Player(tilePosition);
        arrayTiles.add(obj);
        player = obj;

    }

    private void createWall(int tilePosition) {
        Wall obj = new Wall(tilePosition);
        arrayTiles.add(obj);

    }

    private void createBox(int tilePosition, String color) {
        Box obj = new Box(tilePosition, color);
        arrayTiles.add(obj);
    }

    private void createEndPoint(int tilePosition, String color) {
        EndPoint obj = new EndPoint(tilePosition, color);
        arrayTiles.add(obj);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (state == STATE_RUNNING) {

            if (moves <= 0)
                undo = false;

            if (undo) {
                undo();
            } else {
                int auxMoves = 0;
                if (moveUp) {
                    auxMoves = 25;
                } else if (moveDown) {
                    auxMoves = -25;
                } else if (moveLeft) {
                    auxMoves = -1;
                } else if (moveRight) {
                    auxMoves = 1;
                }

                if (player.canMove() && (moveDown || moveLeft || moveRight || moveUp)) {
                    int nextPos = player.position + auxMoves;

                    if (checkIfIsEmptySpace(nextPos) || (!checkIsBoxInPosition(nextPos) && checkIsEndpointInPosition(nextPos))) {
                        arrayOfCharacterMoves.add(new Vector2(player.position, nextPos));
                        arrayMovesBox.add(null);
                        player.moveToPosition(nextPos, moveUp, moveDown, moveRight, moveLeft);
                        moves++;
                    } else {
                        if (checkIsBoxInPosition(nextPos)) {
                            int boxNextPos = nextPos + auxMoves;
                            if (checkIfIsEmptySpace(boxNextPos) || (!checkIsBoxInPosition(boxNextPos) && checkIsEndpointInPosition(boxNextPos))) {
                                Box oBox = getBoxInPosition(nextPos);

                                arrayOfCharacterMoves.add(new Vector2(player.position, nextPos));
                                arrayMovesBox.add(new Vector2(oBox.position, boxNextPos));
                                moves++;

                                oBox.moveToPosition(boxNextPos, false);
                                player.moveToPosition(nextPos, moveUp, moveDown, moveRight, moveLeft);
                                EndPoint myEndPoint = getEndPointInPosition(boxNextPos);
                                if (myEndPoint != null) {
                                    oBox.setIsInEndPoint(myEndPoint);
                                }


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
        if (arrayOfCharacterMoves.size >= moves) {
            Vector2 posAntPersonaje = arrayOfCharacterMoves.removeIndex(moves - 1);
            player.moveToPosition((int) posAntPersonaje.x, true);
        }
        if (arrayMovesBox.size >= moves) {
            Vector2 posAntBox = arrayMovesBox.removeIndex(moves - 1);
            if (posAntBox != null) {
                Box oBox = getBoxInPosition((int) posAntBox.y);
                oBox.moveToPosition((int) posAntBox.x, true);
                EndPoint endPointInPosition = getEndPointInPosition(oBox.position);
                if (endPointInPosition != null)
                    oBox.setIsInEndPoint(endPointInPosition);
            }
        }
        moves--;
        undo = false;
    }

    private boolean checkIfIsEmptySpace(int pos) {
        ArrayIterator<Tiles> tilesIterator = new ArrayIterator<>(arrayTiles);
        while (tilesIterator.hasNext()) {
            if (tilesIterator.next().position == pos)
                return false;
        }
        return true;
    }

    /**
     * Indicates if the object in the position is a box
     */
    private boolean checkIsBoxInPosition(int pos) {
        boolean isBoxInPosition = false;
        ArrayIterator<Tiles> tilesIterator = new ArrayIterator<>(arrayTiles);
        while (tilesIterator.hasNext()) {
            Tiles obj = tilesIterator.next();
            if (obj.position == pos && obj instanceof Box)
                isBoxInPosition = true;
        }
        return isBoxInPosition;

    }

    /**
     * Indicates if the object at the position is an endPoint.
     */
    private boolean checkIsEndpointInPosition(int pos) {
        boolean isEndPointInPosition = false;
        ArrayIterator<Tiles> tilesIterator = new ArrayIterator<>(arrayTiles);
        while (tilesIterator.hasNext()) {
            Tiles obj = tilesIterator.next();
            if (obj.position == pos && obj instanceof EndPoint)
                isEndPointInPosition = true;
        }
        return isEndPointInPosition;

    }

    private Box getBoxInPosition(int pos) {
        ArrayIterator<Tiles> ite = new ArrayIterator<>(arrayTiles);
        while (ite.hasNext()) {
            Tiles obj = ite.next();
            if (obj.position == pos && obj instanceof Box)
                return (Box) obj;
        }
        return null;
    }

    private EndPoint getEndPointInPosition(int pos) {
        ArrayIterator<Tiles> tilesIterator = new ArrayIterator<>(arrayTiles);
        while (tilesIterator.hasNext()) {
            Tiles obj = tilesIterator.next();
            if (obj.position == pos && obj instanceof EndPoint)
                return (EndPoint) obj;
        }
        return null;
    }

    private int checkBoxesMissingTheRightEndPoint() {
        int numBox = 0;
        ArrayIterator<Tiles> tilesIterator = new ArrayIterator<>(arrayTiles);
        while (tilesIterator.hasNext()) {
            Tiles obj = tilesIterator.next();
            if (obj instanceof Box) {
                Box oBox = (Box) obj;
                if (!oBox.isInRightEndPoint)
                    numBox++;
            }

        }
        return numBox;
    }

}
