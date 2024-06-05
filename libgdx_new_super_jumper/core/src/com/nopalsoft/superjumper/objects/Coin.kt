package com.nopalsoft.superjumper.objects

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Pool.Poolable
import com.badlogic.gdx.utils.Pools
import com.nopalsoft.superjumper.screens.BasicScreen

class Coin : Poolable {

    var state = 0
    val position = Vector2()
    var stateTime = 0f

    private fun initializeCoin(x: Float, y: Float) {
        position.set(x, y)
        state = STATE_NORMAL
        stateTime = 0f
    }

    fun update(delta: Float) {
        stateTime += delta
    }

    fun take() {
        state = STATE_TAKEN
        stateTime = 0f
    }

    override fun reset() {
        // Nothing is happening in here.
    }

    companion object {
        const val STATE_NORMAL = 0
        const val STATE_TAKEN = 1

        const val DRAW_WIDTH = .27f
        const val DRAW_HEIGHT = .34f

        private const val WIDTH = .25f
        private const val HEIGHT = .32f

        private const val COINS_SEPARATION = .025f // Variable so that the coins are not stuck


        fun createCoins(worldBox: World, coinsArray: Array<Coin>, y: Float) {
            createACoupleOfCoins(worldBox, coinsArray, y)
        }


        fun createACoin(worldBox: World, arrayCoins: Array<Coin>, y: Float) {
            createCoins(worldBox, arrayCoins, generaPosX(1), y)
        }


        private fun createACoupleOfCoins(worldBox: World, arrayCoins: Array<Coin>, y: Float) {
            val maxRow = MathUtils.random(25) + 1
            val maxColumn = MathUtils.random(6) + 1

            val x = generaPosX(maxColumn)
            for (column in 0 until maxColumn) {
                for (row in 0 until maxRow) {
                    createCoins(
                        worldBox,
                        arrayCoins,
                        x + (column * (WIDTH + COINS_SEPARATION)),
                        y + (row * (HEIGHT + COINS_SEPARATION))
                    )
                }
            }
        }

        /**
         * It generates a position in X depending on the number of lines of the line so
         * that they do not get out of the screen on the right or on the left.
         */
        private fun generaPosX(numberOfLineCoins: Int): Float {
            var x = MathUtils.random(BasicScreen.WORLD_WIDTH) + (WIDTH / 2f)
            if (x + (numberOfLineCoins * (WIDTH + COINS_SEPARATION)) > BasicScreen.WORLD_WIDTH) {
                x -= (x + (numberOfLineCoins * (WIDTH + COINS_SEPARATION))) - BasicScreen.WORLD_WIDTH // Make the difference from the width and what is happening.
                x += WIDTH / 2f // Adds half to be stuck.
            }
            return x
        }


        private fun createCoins(worldBox: World, arrayCoins: Array<Coin>, x: Float, y: Float) {
            val coin = Pools.obtain(Coin::class.java)
            coin.initializeCoin(x, y)

            val bd = BodyDef()
            bd.position.x = x
            bd.position.y = y
            bd.type = BodyType.StaticBody
            val oBody = worldBox.createBody(bd)

            val shape = PolygonShape()
            shape.setAsBox(WIDTH / 2f, HEIGHT / 2f)

            val fixture = FixtureDef()
            fixture.shape = shape
            fixture.isSensor = true
            oBody.createFixture(fixture)
            oBody.userData = coin
            shape.dispose()
            arrayCoins.add(coin)
        }

    }
}