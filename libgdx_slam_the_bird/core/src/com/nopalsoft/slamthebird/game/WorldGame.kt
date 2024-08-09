package com.nopalsoft.slamthebird.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.ChainShape
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.EdgeShape
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.Manifold
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Pool
import com.nopalsoft.slamthebird.Achievements.unlockCombos
import com.nopalsoft.slamthebird.Achievements.unlockSuperJump
import com.nopalsoft.slamthebird.Assets
import com.nopalsoft.slamthebird.Assets.playSound
import com.nopalsoft.slamthebird.Settings
import com.nopalsoft.slamthebird.objects.Boost
import com.nopalsoft.slamthebird.objects.Coin
import com.nopalsoft.slamthebird.objects.Coin.Companion.createCoinBody
import com.nopalsoft.slamthebird.objects.Enemy
import com.nopalsoft.slamthebird.objects.Platform
import com.nopalsoft.slamthebird.objects.Robot
import com.nopalsoft.slamthebird.screens.Screens
import java.util.Random

class WorldGame {
    val TIME_TO_SPAWN_ENEMY: Float = 7f
    val TIME_TO_CHANGE_STATE_PLATFORM: Float =
        16f // Este tiempo debe ser mayor que DURATION_ACTIVE en la clase plataformas.
    val TIME_TO_SPAWN_COIN: Float = .75f
    val WIDTH: Float = Screens.WORLD_SCREEN_WIDTH
    private val boostPool: Pool<Boost> = object : Pool<Boost>() {
        override fun newObject(): Boost {
            return Boost()
        }
    }
    private val coinPool: Pool<Coin> = object : Pool<Coin>() {
        override fun newObject(): Coin {
            return Coin()
        }
    }
    var timeToSpawnEnemy: Float
    var TIME_TO_SPAWN_BOOST: Float = 15f
    var timeToSpawnBoost: Float = 0f
    var timeToChangeStatePlatform: Float = 0f
    var timeToSpawnCoin: Float = 0f
    var oWorldBox: World = World(Vector2(0f, -9.8f), true)
    @JvmField
    var state: Int
    @JvmField
    var robot: Robot? = null
    var arrayPlatforms: Array<Platform>
    var arrayEnemies: Array<Enemy>
    var arrayBodies: Array<Body>
    var arrayBoost: Array<Boost>
    var arrayCoins: Array<Coin>
    var random: Random
    @JvmField
    var scoreSlammed: Int
    @JvmField
    var takenCoins: Int
    @JvmField
    var combo: Int = 0
    var isCoinRain: Boolean

    init {
        oWorldBox.setContactListener(Colisiones())

        state = STATE_RUNNING
        arrayBodies = Array()
        arrayEnemies = Array()
        arrayPlatforms = Array()
        arrayBoost = Array()
        arrayCoins = Array()

        random = Random()

        timeToSpawnEnemy = 5f
        isCoinRain = false

        scoreSlammed = 0
        takenCoins = scoreSlammed

        val posPiso = .6f
        crearParedes(posPiso) // .05
        crearRobot(WIDTH / 2f, posPiso + .251f)

        crearPlataformas(0 + Platform.WIDTH / 2f, 1.8f + posPiso) // Izq Abajo
        crearPlataformas(WIDTH - Platform.WIDTH / 2f + .1f, 1.8f + posPiso) // Derecha abajo

        crearPlataformas(0 + Platform.WIDTH / 2f, 1.8f * 2f + posPiso) // Izq Arriba
        crearPlataformas(
            WIDTH - Platform.WIDTH / 2f + .1f,
            1.8f * 2f + posPiso
        ) // Derecha Arribadd

        // Boost stuff
        TIME_TO_SPAWN_BOOST -= Settings.LEVEL_BOOST_TIME.toFloat()
    }

    private fun crearParedes(posPisoY: Float) {
        val bd = BodyDef()
        bd.position.x = 0f
        bd.position.y = 0f
        bd.type = BodyType.StaticBody
        val oBody = oWorldBox.createBody(bd)

        val shape = ChainShape()
        val vertices = arrayOfNulls<Vector2>(4)
        vertices[0] = Vector2(0.005f, 50f)
        vertices[1] = Vector2(0.005f, 0f)
        vertices[2] = Vector2(WIDTH, 0f)
        vertices[3] = Vector2(WIDTH, 50f)
        shape.createChain(vertices)

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.restitution = 0f
        fixture.friction = 0f

        oBody.createFixture(fixture)
        oBody.userData = "pared"
        shape.dispose()

        // Piso
        val shapePiso = EdgeShape()
        shapePiso[0f, 0f, WIDTH] = 0f
        bd.position.y = posPisoY
        val oBodyPiso = oWorldBox.createBody(bd)

        fixture.shape = shapePiso
        oBodyPiso.createFixture(fixture)
        oBodyPiso.userData = "piso"

        shapePiso.dispose()
    }

    private fun crearRobot(x: Float, y: Float) {
        robot = Robot(x, y)
        val bd = BodyDef()
        bd.position.x = x
        bd.position.y = y
        bd.type = BodyType.DynamicBody

        val oBody = oWorldBox.createBody(bd)

        val shape = CircleShape()
        shape.radius = Robot.RADIUS

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.density = 5f
        fixture.restitution = 0f
        fixture.friction = 0f
        oBody.createFixture(fixture)

        oBody.isFixedRotation = true
        oBody.userData = robot
        oBody.isBullet = true

        shape.dispose()
    }

    private fun createEnemies() {
        val x = random.nextFloat() * (WIDTH - 1) + .5f // Para que no apareza
        val y = random.nextFloat() * 4f + .6f

        val obj = Enemy(x, y)
        arrayEnemies.add(obj)
        val bd = BodyDef()
        bd.position.x = x
        bd.position.y = y
        bd.type = BodyType.DynamicBody

        val oBody = oWorldBox.createBody(bd)

        val shape = PolygonShape()
        shape.setAsBox(Enemy.WIDTH / 2f, Enemy.HEIGHT / 2f)

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.density = 5f
        fixture.restitution = 0f
        fixture.friction = 0f
        fixture.filter.groupIndex = -1
        oBody.createFixture(fixture)

        oBody.isFixedRotation = true
        oBody.gravityScale = 0f
        oBody.userData = obj

        shape.dispose()
    }

    private fun crearPlataformas(x: Float, y: Float) {
        val bd = BodyDef()
        bd.position.x = x
        bd.position.y = y
        bd.type = BodyType.StaticBody
        val oBody = oWorldBox.createBody(bd)

        val shape = PolygonShape()

        shape.setAsBox(Platform.WIDTH / 2f, Platform.HEIGHT / 2f)

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.restitution = 0f
        fixture.friction = 0f
        oBody.createFixture(fixture)

        val obj = Platform(bd.position.x, bd.position.y)
        oBody.userData = obj
        shape.dispose()

        arrayPlatforms.add(obj)
    }

    private fun crearBoost() {
        val obj = boostPool.obtain()

        val plat = random.nextInt(4) // arriba de que plataforma
        val tipo = random.nextInt(4) // ice, invencible,moneda,etc
        obj.init(
            this, arrayPlatforms[plat].position.x,
            arrayPlatforms[plat].position.y + .3f, tipo
        )

        arrayBoost.add(obj)
    }

    private fun createCoins() {
        for (i in 0..5) {
            var x = 0f
            val y = 8.4f + (i * .5f)
            var speed = Coin.SPEED_MOVE
            if (i % 2f != 0f) {
                speed *= -1f
                x = WIDTH
            }

            val body = createCoinBody(oWorldBox, x, y, speed)
            val obj = coinPool.obtain()
            obj.initializeCoin(body.position.x, body.position.y)
            arrayCoins.add(obj)
            body.userData = obj
        }
    }

    fun updateReady(delta: Float, acelX: Float) {
        oWorldBox.step(delta, 8, 4)
        oWorldBox.getBodies(arrayBodies)
        for (body in arrayBodies) {
            if (body.userData is Robot) {
                robot!!.updateReady(body, acelX)
                break
            }
        }
    }

    fun update(delta: Float, acelX: Float, slam: Boolean) {
        oWorldBox.step(delta, 8, 4)

        eliminarObjetos()

        timeToSpawnEnemy += delta
        timeToSpawnBoost += delta
        timeToChangeStatePlatform += delta
        timeToSpawnCoin += delta

        if (timeToSpawnEnemy >= TIME_TO_SPAWN_ENEMY) {
            timeToSpawnEnemy -= TIME_TO_SPAWN_ENEMY
            timeToSpawnEnemy += (scoreSlammed * .025f) // Hace que aparezcan mas rapido los malos
            if (arrayEnemies.size < 7 + (scoreSlammed * .15f)) {
                if (scoreSlammed <= 15) {
                    createEnemies()
                } else if (scoreSlammed <= 50) {
                    createEnemies()
                    createEnemies()
                } else {
                    createEnemies()
                    createEnemies()
                    createEnemies()
                }
            }
        }

        if (timeToSpawnBoost >= TIME_TO_SPAWN_BOOST) {
            timeToSpawnBoost -= TIME_TO_SPAWN_BOOST
            if (random.nextBoolean()) crearBoost()
        }

        if (timeToSpawnCoin >= TIME_TO_SPAWN_COIN) {
            timeToSpawnCoin -= TIME_TO_SPAWN_COIN
            createCoins()
        }

        if (timeToChangeStatePlatform >= TIME_TO_CHANGE_STATE_PLATFORM) {
            timeToChangeStatePlatform -= TIME_TO_CHANGE_STATE_PLATFORM
            if (random.nextBoolean()) {
                val plat = random.nextInt(4)
                val state = random.nextInt(2)
                val obj = arrayPlatforms[plat]
                if (state == 0) {
                    obj.setBreakable()
                } else {
                    obj.setFire()
                }
            }
        }

        oWorldBox.getBodies(arrayBodies)

        for (body in arrayBodies) {
            if (body.userData is Robot) {
                updateRobot(delta, body, acelX, slam)
            } else if (body.userData is Enemy) {
                updateEnemy(delta, body)
            } else if (body.userData is Boost) {
                updateBoost(delta, body)
            } else if (body.userData is Platform) {
                updatePlataforma(delta, body)
            } else if (body.userData is Coin) {
                updateCoin(delta, body)
            }
        }

        isCoinRain = false
    }

    private fun eliminarObjetos() {
        oWorldBox.getBodies(arrayBodies)

        for (body in arrayBodies) {
            if (!oWorldBox.isLocked) {
                if (body.userData is Robot) {
                    val obj = body.userData as Robot
                    if (obj.state == Robot.STATE_DEAD
                        && obj.stateTime >= Robot.DURATION_DEAD_ANIMATION
                    ) {
                        oWorldBox.destroyBody(body)
                        state = STATE_GAME_OVER
                    }
                } else if (body.userData is Enemy) {
                    val obj = body.userData as Enemy
                    if (obj.state == Enemy.STATE_DEAD) {
                        oWorldBox.destroyBody(body)
                        arrayEnemies.removeValue(obj, true)
                        scoreSlammed++

                        /*
                         * If there are no enemies, I'll at least create one. I'll put this here
                         * so that it doesn't affect the time in which the first bad guy appears.
                         */
                        if (arrayEnemies.size == 0) createEnemies()
                    }
                } else if (body.userData is Boost) {
                    val obj = body.userData as Boost
                    if (obj.state == Boost.STATE_TAKEN) {
                        oWorldBox.destroyBody(body)
                        arrayBoost.removeValue(obj, true)
                        boostPool.free(obj)
                    }
                } else if (body.userData is Coin) {
                    val obj = body.userData as Coin
                    if (obj.state == Coin.STATE_TAKEN) {
                        oWorldBox.destroyBody(body)
                        arrayCoins.removeValue(obj, true)
                        coinPool.free(obj)
                    }
                }
            }
        }
    }

    private fun updateRobot(delta: Float, body: Body, acelX: Float, slam: Boolean) {
        val obj = body.userData as Robot
        obj.update(delta, body, acelX, slam)

        if (obj.position.y > 12) {
            unlockSuperJump()
            Gdx.app.log("ACHIIIII", "Asdsadasd")
        }
    }

    private fun updateEnemy(delta: Float, body: Body) {
        val obj = body.userData as Enemy
        obj.update(delta, body, random)
    }

    private fun updateBoost(delta: Float, body: Body) {
        val obj = body.userData as Boost
        obj.update(delta, body)
    }

    private fun updatePlataforma(delta: Float, body: Body) {
        val obj = body.userData as Platform
        obj.update(delta)
    }

    private fun updateCoin(delta: Float, body: Body) {
        val obj = body.userData as Coin
        obj.update(delta, body)

        if (obj.position.x < -3 || obj.position.x > WIDTH + 3) {
            obj.state = Coin.STATE_TAKEN
        }

        if (isCoinRain) {
            body.gravityScale = 1f
            body.setLinearVelocity(body.linearVelocity.x * .25f, 0f)
        }
    }

    internal inner class Colisiones : ContactListener {
        override fun beginContact(contact: Contact) {
            val a = contact.fixtureA
            val b = contact.fixtureB

            if (a.body.userData is Robot) beginContactRobotOtraCosa(a, b)
            else if (b.body.userData is Robot) beginContactRobotOtraCosa(b, a)

            if (a.body.userData is Enemy) beginContactEnemyWithOthers(a, b)
            else if (b.body.userData is Enemy) beginContactEnemyWithOthers(b, a)
        }

        /**
         * Begin contacto ROBOT con OTRA-COSA
         */
        private fun beginContactRobotOtraCosa(robotFixture: Fixture, otherFixture: Fixture) {
            val robot = robotFixture.body.userData as Robot
            val otherObject = otherFixture.body.userData

            if (otherObject == "piso") {
                robot.jump()

                if (!robot.isInvincible) // Si es invencible no le quito el combo
                    combo = 0
            } else if (otherObject is Platform) {
                val platform = otherObject
                if (platform.state == Platform.STATE_FIRE && !robot.isInvincible) {
                    robot.hit()
                    return
                } else if (platform.state == Platform.STATE_BREAKABLE) {
                    platform.setBroken()
                } else if (platform.state == Platform.STATE_BROKEN) {
                    return
                }
                if (!robot.isInvincible && robot.state == Robot.STATE_FALLING) // Si es invencible no le quito el combo
                    combo = 0
                robot.jump()
            } else if (otherObject is Boost) {
                val boost = otherObject
                boost.hit()
                playSound(Assets.soundBoost!!)

                if (boost.type == Boost.TIPO_SUPERJUMP) {
                    robot.isSuperJump = true
                } else if (boost.type == Boost.TIPO_INVENCIBLE) {
                    robot.isInvincible = true
                } else if (boost.type == Boost.TIPO_COIN_RAIN) {
                    isCoinRain = true
                } else if (boost.type == Boost.TIPO_ICE) {
                    for (arrEnemy in arrayEnemies) {
                        arrEnemy.setFrozen()
                    }
                }
            } else if (otherObject is Coin) {
                val coin = otherObject
                if (coin.state == Coin.STATE_NORMAL) {
                    coin.state = Coin.STATE_TAKEN
                    takenCoins++
                    Settings.currentCoins++
                    playSound(Assets.soundCoin!!)
                }
            } else if (otherObject is Enemy) {
                val enemy = otherObject

                // I can touch from the middle of the enemy up
                val posRobot = robot.position.y - Robot.RADIUS
                val pisY = enemy.position.y

                if (enemy.state != Enemy.STATE_JUST_APPEAR) {
                    if (robot.isInvincible) {
                        enemy.die()
                        combo++
                    } else if (posRobot > pisY) {
                        enemy.hit()
                        robot.jump()
                        combo++
                    } else if (robot.state != Robot.STATE_DEAD) {
                        robot.hit()
                        combo = 0
                    }
                    if (combo >= COMBO_TO_START_GETTING_COINS) {
                        takenCoins += combo
                        Settings.currentCoins += combo
                    }

                    unlockCombos()
                }
            }
        }

        private fun beginContactEnemyWithOthers(enemyFixture: Fixture, otherFixture: Fixture) {
            val enemy = enemyFixture.body.userData as Enemy
            val otherObject = otherFixture.body.userData

            if (otherObject == "pared") {
                enemyFixture.body.setLinearVelocity(
                    enemyFixture.body.linearVelocity.x * -1,
                    enemyFixture.body.linearVelocity.y
                )
            } else if (otherObject == "piso") {
                if (enemy.state == Enemy.STATE_FLYING) {
                    enemyFixture.body.setLinearVelocity(
                        enemyFixture.body.linearVelocity.x,
                        enemyFixture.body.linearVelocity.y * -1
                    )
                }
            }
        }

        override fun endContact(contact: Contact) {
        }

        override fun preSolve(contact: Contact, oldManifold: Manifold) {
            val a = contact.fixtureA
            val b = contact.fixtureB

            if (a.body.userData is Robot) preSolveRobot(a, b, contact)
            else if (b.body.userData is Robot) preSolveRobot(b, a, contact)

            if (a.body.userData is Enemy) preSolveEnemy(a, b, contact)
            else if (b.body.userData is Enemy) preSolveEnemy(b, a, contact)

            if (a.body.userData is Coin) preSolveCoin(b, contact)
            else if (b.body.userData is Coin) preSolveCoin(a, contact)
        }

        private fun preSolveRobot(
            robotFixture: Fixture, otherFixture: Fixture,
            contact: Contact
        ) {
            val otherObject = otherFixture.body.userData
            val robot = robotFixture.body.userData as Robot

            // Platform oneSide
            if (otherObject is Platform) {
                val obj = otherObject
                val posRobot = robot.position.y - Robot.RADIUS + .05f
                val pisY = obj.position.y + (Platform.HEIGHT / 2f)

                if (posRobot < pisY || obj.state == Platform.STATE_BROKEN) contact.isEnabled = false
            } else if (otherObject is Enemy) {
                if (otherObject.state == Enemy.STATE_JUST_APPEAR
                    || robot.isInvincible
                ) contact.isEnabled = false
            } else if (otherObject is Coin) {
                contact.isEnabled = false
            }
        }

        private fun preSolveEnemy(
            enemyFixture: Fixture, otherFixture: Fixture,
            contact: Contact
        ) {
            val otherObject = otherFixture.body.userData
            val enemy = enemyFixture.body.userData as Enemy

            // Enemy cannot touch the platforms if he is flying
            if (otherObject is Platform) {
                if (enemy.state == Enemy.STATE_FLYING) contact.isEnabled = false
            }
        }

        private fun preSolveCoin(
            otherFixture: Fixture,
            contact: Contact
        ) {
            val otherObject = otherFixture.body.userData

            if (otherObject == "pared") {
                contact.isEnabled = false
            }
        }

        override fun postSolve(contact: Contact, impulse: ContactImpulse) {
            // TODO Auto-generated method stub
        }
    }

    companion object {
        const val COMBO_TO_START_GETTING_COINS: Float = 3f
        const val STATE_RUNNING: Int = 0
        const val STATE_GAME_OVER: Int = 1
    }
}
