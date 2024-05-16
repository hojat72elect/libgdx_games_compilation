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
import com.nopalsoft.sokoban.objetos.Box;
import com.nopalsoft.sokoban.objetos.EndPoint;
import com.nopalsoft.sokoban.objetos.Tiles;

public class Board extends Group {
    public static final float UNIT_SCALE = 1f;

    static public final int STATE_RUNNING = 1;
    static public final int STATE_GAMEOVER = 2;
    public int state;
    public boolean moveUp, moveDown, moveLeft, moveRight;
    public boolean undo;
    // X previous position, Y current position.
    Array<Vector2> arrayOfCharacterMoves;
    /**
     * X previous position, Y current position.
     */
    Array<Vector2> arrMovesCaja;
    Array<Tiles> arrTiles;
    int moves;
    float time;
    private com.nopalsoft.sokoban.objetos.Character character;

    public Board() {
        setSize(800, 480);

        arrTiles = new Array<>(25 * 15);
        arrayOfCharacterMoves = new Array<>();
        arrMovesCaja = new Array<>();

        initializeMap("StaticMap");
        initializeMap("Objetos");

        // DESPUES de inicializar los objetos los agrego al Board en orden para que se dibujen unos primero que otros
        agregarAlTablero(com.nopalsoft.sokoban.objetos.Wall.class);
        agregarAlTablero(EndPoint.class);
        agregarAlTablero(Box.class);
        agregarAlTablero(com.nopalsoft.sokoban.objetos.Character.class);

        state = STATE_RUNNING;

        time = moves = 0;
    }

    private void agregarAlTablero(Class<?> tipo) {
        for (com.nopalsoft.sokoban.objetos.Tiles obj : arrTiles) {
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

                                switch (tipo) {
                                    case "startPoint":
                                        crearPersonaje(posTile);
                                        break;
                                    case "pared":
                                        crearPared(posTile);
                                        break;
                                    case "caja":
                                        crearCaja(posTile, tile.getProperties().get("color").toString());
                                        break;
                                    case "endPoint":
                                        crearEndPoint(posTile, tile.getProperties().get("color").toString());
                                        break;
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
        com.nopalsoft.sokoban.objetos.Character obj = new com.nopalsoft.sokoban.objetos.Character(posTile);
        arrTiles.add(obj);
        character = obj;

    }

    private void crearPared(int posTile) {
        com.nopalsoft.sokoban.objetos.Wall obj = new com.nopalsoft.sokoban.objetos.Wall(posTile);
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

                if (character.canMove() && (moveDown || moveLeft || moveRight || moveUp)) {
                    int nextPos = character.posicion + auxMoves;

                    if (checarEspacioVacio(nextPos) || (!checarIsBoxInPosition(nextPos) && checarIsEndInPosition(nextPos))) {
                        arrayOfCharacterMoves.add(new Vector2(character.posicion, nextPos));
                        arrMovesCaja.add(null);
                        character.moveToPosition(nextPos, moveUp, moveDown, moveRight, moveLeft);
                        moves++;
                    } else {
                        if (checarIsBoxInPosition(nextPos)) {
                            int boxNextPos = nextPos + auxMoves;
                            if (checarEspacioVacio(boxNextPos) || (!checarIsBoxInPosition(boxNextPos) && checarIsEndInPosition(boxNextPos))) {
                                Box oBox = getBoxInPosition(nextPos);

                                arrayOfCharacterMoves.add(new Vector2(character.posicion, nextPos));
                                arrMovesCaja.add(new Vector2(oBox.posicion, boxNextPos));
                                moves++;

                                oBox.moveToPosition(boxNextPos, false);
                                character.moveToPosition(nextPos, moveUp, moveDown, moveRight, moveLeft);
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
        if (arrayOfCharacterMoves.size >= moves) {
            Vector2 posAntPersonaje = arrayOfCharacterMoves.removeIndex(moves - 1);
            character.moveToPosition((int) posAntPersonaje.x, true);
        }
        if (arrMovesCaja.size >= moves) {
            Vector2 posAntBox = arrMovesCaja.removeIndex(moves - 1);
            if (posAntBox != null) {
                Box oBox = getBoxInPosition((int) posAntBox.y);
                oBox.moveToPosition((int) posAntBox.x, true);
                oBox.setIsInEndPoint(getEndPointInPosition(oBox.posicion));
            }
        }
        moves--;
        undo = false;
    }

    private boolean checarEspacioVacio(int pos) {
        ArrayIterator<Tiles> ite = new ArrayIterator<Tiles>(arrTiles);
        while (ite.hasNext()) {
            if (ite.next().posicion == pos)
                return false;
        }
        return true;
    }

    /**
     * Indica si el objeto en la posicion es una caja
     */
    private boolean checarIsBoxInPosition(int pos) {
        boolean isBoxInPosition = false;
        ArrayIterator<Tiles> ite = new ArrayIterator<Tiles>(arrTiles);
        while (ite.hasNext()) {
            Tiles obj = ite.next();
            if (obj.posicion == pos && obj instanceof Box)
                isBoxInPosition = true;
        }
        return isBoxInPosition;

    }

    /**
     * Indica si el objeto en la posicion es endPoint
     */
    private boolean checarIsEndInPosition(int pos) {
        boolean isEndPointInPosition = false;
        ArrayIterator<Tiles> ite = new ArrayIterator<Tiles>(arrTiles);
        while (ite.hasNext()) {
            Tiles obj = ite.next();
            if (obj.posicion == pos && obj instanceof EndPoint)
                isEndPointInPosition = true;
        }
        return isEndPointInPosition;

    }

    private Box getBoxInPosition(int pos) {
        ArrayIterator<Tiles> ite = new ArrayIterator<Tiles>(arrTiles);
        while (ite.hasNext()) {
            Tiles obj = ite.next();
            if (obj.posicion == pos && obj instanceof Box)
                return (Box) obj;
        }
        return null;
    }

    private EndPoint getEndPointInPosition(int pos) {
        ArrayIterator<Tiles> ite = new ArrayIterator<Tiles>(arrTiles);
        while (ite.hasNext()) {
            Tiles obj = ite.next();
            if (obj.posicion == pos && obj instanceof EndPoint)
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
