package com.nopalsoft.sokoban.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Group
import com.nopalsoft.sokoban.Assets
import com.nopalsoft.sokoban.objects.Box
import com.nopalsoft.sokoban.objects.EndPoint
import com.nopalsoft.sokoban.objects.Player
import com.nopalsoft.sokoban.objects.Tiles
import com.nopalsoft.sokoban.objects.Wall
import com.badlogic.gdx.utils.Array as GdxArray

class Board : Group() {

    @JvmField
    var state = STATE_RUNNING

    @JvmField
    var moveUp = false

    @JvmField
    var moveDown = false

    @JvmField
    var moveLeft = false

    @JvmField
    var moveRight = false

    @JvmField
    var undo = false

    // X previous position, Y current position.
    private val arrayOfCharacterMoves: GdxArray<Vector2> = GdxArray()

    // X previous position, Y current position.
    private val arrayMovesBox: GdxArray<Vector2> = GdxArray()
    private val arrayTiles: GdxArray<Tiles> = GdxArray(25 * 15)

    @JvmField
    var moves = 0

    @JvmField
    var time = 0f
    private lateinit var player: Player

    init {
        setSize(800f, 480f)
        initializeMap("StaticMap")
        initializeMap("Objetos")


        // AFTER initializing the objects I add them to the Board in order so that some
        // are drawn before others.
        addToBoard(Wall::class.java)
        addToBoard(EndPoint::class.java)
        addToBoard(Box::class.java)
        addToBoard(Player::class.java)
    }


    override fun act(delta: Float) {
        super.act(delta)

        if (state == STATE_RUNNING) {

            if (moves <= 0)
                undo = false

            if (undo) {
                undo()
            } else {

                var auxMoves = 0
                if (moveUp) {
                    auxMoves = 25
                } else if (moveDown) {
                    auxMoves = -25
                } else if (moveLeft) {
                    auxMoves = -1
                } else if (moveRight) {
                    auxMoves = 1
                }

                if (player.canMove() && (moveDown || moveLeft || moveRight || moveUp)) {
                    val nextPos = player.position + auxMoves

                    if (checkIfIsEmptySpace(nextPos) || (!checkIsBoxInPosition(nextPos) && checkIsEndpointInPosition(
                            nextPos
                        ))
                    ) {
                        arrayOfCharacterMoves.add(
                            Vector2(
                                player.position.toFloat(),
                                nextPos.toFloat()
                            )
                        )
                        arrayMovesBox.add(null)
                        player.moveToPosition(nextPos, moveUp, moveDown, moveRight, moveLeft)
                        moves++
                    } else {
                        if (checkIsBoxInPosition(nextPos)) {
                            val boxNextPos = nextPos + auxMoves
                            if (checkIfIsEmptySpace(boxNextPos) || (!checkIsBoxInPosition(boxNextPos) && checkIsEndpointInPosition(
                                    boxNextPos
                                ))
                            ) {
                                val oBox = getBoxInPosition(nextPos)

                                arrayOfCharacterMoves.add(
                                    Vector2(
                                        player.position.toFloat(),
                                        nextPos.toFloat()
                                    )
                                )
                                arrayMovesBox.add(
                                    Vector2(
                                        oBox?.position?.toFloat() ?: 0f,
                                        boxNextPos.toFloat()
                                    )
                                )
                                moves++

                                oBox?.moveToPosition(boxNextPos, false)
                                player.moveToPosition(
                                    nextPos,
                                    moveUp,
                                    moveDown,
                                    moveRight,
                                    moveLeft
                                )
                                val myEndPoint = getEndPointInPosition(boxNextPos)
                                if (myEndPoint != null)
                                    oBox?.setIsInEndPoint(myEndPoint)

                            }
                        }
                    }
                }

                moveDown = false
                moveUp = false
                moveRight = false
                moveLeft = false

                if (checkBoxesMissingTheRightEndPoint() == 0) state =
                    STATE_GAMEOVER

            }

            if (state == STATE_RUNNING)
                time += Gdx.graphics.rawDeltaTime
        }
    }

    private fun addToBoard(type: Class<*>) {
        for (obj in arrayTiles) {
            if (obj.javaClass == type) {
                addActor(obj)
            }
        }
    }


    private fun initializeMap(layerName: String) {
        val layer = Assets.map.layers[layerName] as TiledMapTileLayer
        var tilePosition = 0

        for (y in 0 until 15) {
            for (x in 0 until 25) {
                val cell = layer.getCell(x, y)
                if (cell != null) {
                    val tile = cell.tile
                    if (tile.properties != null) {
                        if (tile.properties.containsKey("tipo")) {
                            val tyleType = tile.properties["tipo"].toString()

                            when (tyleType) {
                                "startPoint" -> createPerson(tilePosition)
                                "pared" -> createWall(tilePosition)
                                "caja" -> createBox(
                                    tilePosition,
                                    tile.properties["color"].toString()
                                )

                                "endPoint" -> createEndPoint(
                                    tilePosition,
                                    tile.properties["color"].toString()
                                )
                            }

                        }

                    }
                }
                tilePosition++
            }
        }
    }

    private fun createPerson(tilePosition: Int) {
        val obj = Player(tilePosition)
        arrayTiles.add(obj)
        player = obj
    }

    private fun createWall(tilePosition: Int) {
        val obj = Wall(tilePosition)
        arrayTiles.add(obj)
    }

    private fun createBox(tilePosition: Int, color: String) {
        val obj = Box(tilePosition, color)
        arrayTiles.add(obj)
    }

    private fun createEndPoint(tilePosition: Int, color: String) {
        val obj = EndPoint(tilePosition, color)
        arrayTiles.add(obj)
    }

    private fun undo() {
        if (arrayOfCharacterMoves.size >= moves) {
            val posAntPersonaje = arrayOfCharacterMoves.removeIndex(moves - 1)
            player.moveToPosition(posAntPersonaje.x.toInt(), true)
        }
        if (arrayMovesBox.size >= moves) {
            val posAntBox = arrayMovesBox.removeIndex(moves - 1)
            if (posAntBox != null) {
                val oBox = getBoxInPosition(posAntBox.y.toInt())
                oBox?.moveToPosition(posAntBox.x.toInt(), true)
                val endPointInPosition = getEndPointInPosition(oBox!!.position)
                if (endPointInPosition != null)
                    oBox.setIsInEndPoint(endPointInPosition)
            }
        }
        moves--
        undo = false
    }

    private fun checkIfIsEmptySpace(pos: Int): Boolean {
        val tilesIterator = GdxArray.ArrayIterator(arrayTiles)
        while (tilesIterator.hasNext()) {
            if (tilesIterator.next().position == pos)
                return false
        }
        return true
    }

    /**
     * Indicates if the object in the position is a box
     */
    private fun checkIsBoxInPosition(pos: Int): Boolean {
        var isBoxInPosition = false
        val tilesIterator = GdxArray.ArrayIterator(arrayTiles)
        while (tilesIterator.hasNext()) {
            val obj = tilesIterator.next()
            if (obj.position == pos && obj is Box)
                isBoxInPosition = true
        }
        return isBoxInPosition
    }

    private fun checkIsEndpointInPosition(pos: Int): Boolean {
        var isEndPointInPosition = false
        val tilesIterator = GdxArray.ArrayIterator(arrayTiles)
        while (tilesIterator.hasNext()) {
            val obj = tilesIterator.next()
            if (obj.position == pos && obj is EndPoint)
                isEndPointInPosition = true
        }
        return isEndPointInPosition
    }

    private fun getBoxInPosition(pos: Int): Box? {
        val tilesIterator = GdxArray.ArrayIterator(arrayTiles)
        while (tilesIterator.hasNext()) {
            val obj = tilesIterator.next()
            if (obj.position == pos && obj is Box)
                return obj
        }
        return null
    }

    private fun getEndPointInPosition(pos: Int): EndPoint? {
        val tilesIterator = GdxArray.ArrayIterator(arrayTiles)
        while (tilesIterator.hasNext()) {
            val obj = tilesIterator.next()
            if (obj.position == pos && obj is EndPoint)
                return obj
        }
        return null
    }

    private fun checkBoxesMissingTheRightEndPoint(): Int {
        var numBox = 0
        val tilesIterator = GdxArray.ArrayIterator(arrayTiles)
        while (tilesIterator.hasNext()) {
            val obj = tilesIterator.next()
            if (obj is Box) {
                if (!obj.isInRightEndPoint) numBox++
            }
        }
        return numBox
    }

    companion object {
        const val UNIT_SCALE = 1f
        const val STATE_RUNNING = 1
        const val STATE_GAMEOVER = 2
    }
}