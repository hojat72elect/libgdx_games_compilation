package com.nopalsoft.superjumper.game

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
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
import com.badlogic.gdx.utils.Pools
import com.nopalsoft.superjumper.Settings.numBullets
import com.nopalsoft.superjumper.objects.Bullet
import com.nopalsoft.superjumper.objects.Cloud
import com.nopalsoft.superjumper.objects.Coin
import com.nopalsoft.superjumper.objects.Coin.Companion.createACoin
import com.nopalsoft.superjumper.objects.Coin.Companion.createCoins
import com.nopalsoft.superjumper.objects.Enemy
import com.nopalsoft.superjumper.objects.Item
import com.nopalsoft.superjumper.objects.Lightning
import com.nopalsoft.superjumper.objects.Platform
import com.nopalsoft.superjumper.objects.PlatformPiece
import com.nopalsoft.superjumper.objects.Player
import com.nopalsoft.superjumper.screens.BasicScreen

class WorldGame {
    private val arrayBodies: Array<Body>
    var worldBox: World = World(Vector2(0f, -9.8f), true)
    var coins: Int = 0
    var maxDistance: Int = 0
    var state: Int
    var TIME_TO_CREATE_CLOUD: Float = 15f
    var timeToCreateCloud: Float
    var player: Player? = null
    var arrayPlatforms: Array<Platform>
    var arrayPlatformPieces: Array<PlatformPiece>
    var arrayCoins: Array<Coin>
    var arrayEnemies: Array<Enemy>
    var arrayItem: Array<Item>
    var arrayClouds: Array<Cloud>
    var arrayLightnings: Array<Lightning>
    var arrayBullets: Array<Bullet>
    var worldCreatedUpToY: Float

    init {
        worldBox.setContactListener(Collision())

        arrayBodies = Array()
        arrayPlatforms = Array()
        arrayPlatformPieces = Array()
        arrayCoins = Array()
        arrayEnemies = Array()
        arrayItem = Array()
        arrayClouds = Array()
        arrayLightnings = Array()
        arrayBullets = Array()

        timeToCreateCloud = 0f

        state = STATE_RUNNING

        createFloor()
        createPlayer()

        worldCreatedUpToY = player!!.position.y
        createNextPart()
    }

    private fun createNextPart() {
        val y = worldCreatedUpToY + 2

        var i = 0
        while (worldCreatedUpToY < (y + 10)) {
            worldCreatedUpToY = y + (i * 2)

            createPlatform(worldCreatedUpToY)
            createPlatform(worldCreatedUpToY)

            if (MathUtils.random(100) < 5) createCoins(worldBox, arrayCoins, worldCreatedUpToY)

            if (MathUtils.random(20) < 5) createACoin(worldBox, arrayCoins, worldCreatedUpToY + .5f)

            if (MathUtils.random(20) < 5) createEnemy(worldCreatedUpToY + .5f)

            if (timeToCreateCloud >= TIME_TO_CREATE_CLOUD) {
                createClouds(worldCreatedUpToY + .7f)
                timeToCreateCloud = 0f
            }

            if (MathUtils.random(50) < 5) createItem(worldCreatedUpToY + .5f)
            i++
        }
    }

    /**
     * The floor only appears 1 time, at the beginning of the game.
     */
    private fun createFloor() {
        val bodyDefinition = BodyDef()
        bodyDefinition.type = BodyType.StaticBody

        val body = worldBox.createBody(bodyDefinition)

        val shape = EdgeShape()
        shape[0f, 0f, BasicScreen.WORLD_WIDTH] = 0f

        val fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape

        body.createFixture(fixtureDefinition)
        body.userData = "piso"

        shape.dispose()
    }

    private fun createPlayer() {
        player = Player(2.4f, .5f)

        val bodyDefinition = BodyDef()
        bodyDefinition.position[player!!.position.x] = player!!.position.y
        bodyDefinition.type = BodyType.DynamicBody

        val body = worldBox.createBody(bodyDefinition)

        val shape = PolygonShape()
        shape.setAsBox(Player.WIDTH / 2f, Player.HEIGHT / 2f)

        val fixtureDefinition = FixtureDef()
        fixtureDefinition.shape = shape
        fixtureDefinition.density = 10f
        fixtureDefinition.friction = 0f
        fixtureDefinition.restitution = 0f

        body.createFixture(fixtureDefinition)
        body.userData = player
        body.isFixedRotation = true

        shape.dispose()
    }

    private fun createPlatform(y: Float) {
        val newPlatform = Pools.obtain(
            Platform::class.java
        )
        newPlatform.initializePlatform(
            MathUtils.random(BasicScreen.WORLD_WIDTH),
            y,
            MathUtils.random(1)
        )

        val bodyDefinition = BodyDef()
        bodyDefinition.position[newPlatform.position.x] = newPlatform.position.y
        bodyDefinition.type = BodyType.KinematicBody

        val body = worldBox.createBody(bodyDefinition)

        val shape = PolygonShape()
        shape.setAsBox(Platform.WIDTH_NORMAL / 2f, Platform.HEIGHT_NORMAL / 2f)

        val fixture = FixtureDef()
        fixture.shape = shape

        body.createFixture(fixture)
        body.userData = newPlatform
        arrayPlatforms.add(newPlatform)

        shape.dispose()
    }

    /**
     * The breakable platform is 2 squares.
     */
    private fun createPiecesOfPlatforms(oPlat: Platform) {
        createPiecesOfPlatforms(oPlat, PlatformPiece.TYPE_LEFT)
        createPiecesOfPlatforms(oPlat, PlatformPiece.TYPE_RIGHT)
    }

    private fun createPiecesOfPlatforms(platform: Platform, type: Int) {
        val x: Float
        var angularVelocity = 100f

        if (type == PlatformPiece.TYPE_LEFT) {
            x = platform.position.x - PlatformPiece.WIDTH_NORMAL / 2f
            angularVelocity *= -1f
        } else {
            x = platform.position.x + PlatformPiece.WIDTH_NORMAL / 2f
        }

        val piece = Pools.obtain(PlatformPiece::class.java)
        piece.initializePlatformPiece(x, platform.position.y, type, platform.color)

        val bodyDefinition = BodyDef()
        bodyDefinition.position[piece.position.x] = piece.position.y
        bodyDefinition.type = BodyType.DynamicBody

        val body = worldBox.createBody(bodyDefinition)

        val shape = PolygonShape()
        shape.setAsBox(PlatformPiece.WIDTH_NORMAL / 2f, PlatformPiece.HEIGHT_NORMAL / 2f)

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.isSensor = true

        body.createFixture(fixture)
        body.userData = piece
        body.angularVelocity = MathUtils.degRad * angularVelocity
        arrayPlatformPieces.add(piece)

        shape.dispose()
    }

    private fun createEnemy(y: Float) {
        val enemy = Pools.obtain(Enemy::class.java)
        enemy.initializeEnemy(MathUtils.random(BasicScreen.WORLD_WIDTH), y)

        val bodyDefinition = BodyDef()
        bodyDefinition.position[enemy.position.x] = enemy.position.y
        bodyDefinition.type = BodyType.DynamicBody

        val body = worldBox.createBody(bodyDefinition)

        val shape = PolygonShape()
        shape.setAsBox(Enemy.WIDTH / 2f, Enemy.HEIGHT / 2f)

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.isSensor = true

        body.createFixture(fixture)
        body.userData = enemy
        body.gravityScale = 0f

        val speed = MathUtils.random(1f, Enemy.SPEED_X)

        if (MathUtils.randomBoolean()) body.setLinearVelocity(speed, 0f)
        else body.setLinearVelocity(-speed, 0f)
        arrayEnemies.add(enemy)

        shape.dispose()
    }

    private fun createItem(y: Float) {
        val newItem = Pools.obtain(
            Item::class.java
        )
        newItem.initializeItem(MathUtils.random(BasicScreen.WORLD_WIDTH), y)

        val bodyDefinition = BodyDef()
        bodyDefinition.position[newItem.position.x] = newItem.position.y
        bodyDefinition.type = BodyType.StaticBody
        val body = worldBox.createBody(bodyDefinition)

        val shape = PolygonShape()
        shape.setAsBox(Item.WIDTH / 2f, Item.HEIGHT / 2f)

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.isSensor = true
        body.createFixture(fixture)
        body.userData = newItem
        shape.dispose()
        arrayItem.add(newItem)
    }

    private fun createClouds(y: Float) {
        val cloud = Pools.obtain(Cloud::class.java)
        cloud.initializeCloud(MathUtils.random(BasicScreen.WORLD_WIDTH), y)

        val bodyDefinition = BodyDef()
        bodyDefinition.position[cloud.position.x] = cloud.position.y
        bodyDefinition.type = BodyType.DynamicBody

        val body = worldBox.createBody(bodyDefinition)

        val shape = PolygonShape()
        shape.setAsBox(Cloud.WIDTH / 2f, Cloud.HEIGHT / 2f)

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.isSensor = true

        body.createFixture(fixture)
        body.userData = cloud
        body.gravityScale = 0f

        val speed = MathUtils.random(1f, Cloud.SPEED_X)

        if (MathUtils.randomBoolean()) body.setLinearVelocity(speed, 0f)
        else body.setLinearVelocity(-speed, 0f)
        arrayClouds.add(cloud)

        shape.dispose()
    }

    private fun createLightning(x: Float, y: Float) {
        val lightning = Pools.obtain(Lightning::class.java)
        lightning.initializeLightning(x, y)

        val bd = BodyDef()
        bd.position[lightning.position.x] = lightning.position.y
        bd.type = BodyType.KinematicBody

        val body = worldBox.createBody(bd)

        val shape = PolygonShape()
        shape.setAsBox(Lightning.WIDTH / 2f, Lightning.HEIGHT / 2f)

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.isSensor = true

        body.createFixture(fixture)
        body.userData = lightning

        body.setLinearVelocity(0f, Lightning.Y_SPEED)
        arrayLightnings.add(lightning)

        shape.dispose()
    }

    private fun createBullet(
        originX: Float,
        originY: Float,
        destinationX: Float,
        destinationY: Float
    ) {
        val bullet = Pools.obtain(Bullet::class.java)
        bullet.initializeBullet(originX, originY)

        val bd = BodyDef()
        bd.position[bullet.position.x] = bullet.position.y
        bd.type = BodyType.KinematicBody

        val body = worldBox.createBody(bd)

        val shape = PolygonShape()
        shape.setAsBox(Bullet.SIZE / 2f, Bullet.SIZE / 2f)

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.isSensor = true

        body.createFixture(fixture)
        body.userData = bullet
        body.isBullet = true

        val destination = Vector2(destinationX, destinationY)
        destination.sub(bullet.position).nor().scl(Bullet.XY_SPEED)

        body.setLinearVelocity(destination.x, destination.y)

        arrayBullets.add(bullet)

        shape.dispose()
    }

    fun update(
        delta: Float,
        accelerationX: Float,
        fire: Boolean,
        touchPositionWorldCoords: Vector3
    ) {
        worldBox.step(delta, 8, 4)

        removeObjects()

        if (player!!.position.y + 10 > worldCreatedUpToY) {
            createNextPart()
        }

        timeToCreateCloud += delta // I update the time to create a cloud.

        worldBox.getBodies(arrayBodies)

        for (body in arrayBodies) {
            if (body.userData is Player) {
                updatePlayer(body, delta, accelerationX, fire, touchPositionWorldCoords)
            } else if (body.userData is Platform) {
                updatePlatform(body, delta)
            } else if (body.userData is PlatformPiece) {
                updatePlatformPieces(body, delta)
            } else if (body.userData is Coin) {
                updateCoins(body, delta)
            } else if (body.userData is Enemy) {
                updateEnemy(body, delta)
            } else if (body.userData is Item) {
                updateItem(body, delta)
            } else if (body.userData is Cloud) {
                updateCloud(body, delta)
            } else if (body.userData is Lightning) {
                updateLightning(body, delta)
            } else if (body.userData is Bullet) {
                updateBullet(body, delta)
            }
        }

        if (maxDistance < (player!!.position.y * 10)) {
            maxDistance = (player!!.position.y * 10).toInt()
        }

        // If the character is 5.5f below the maximum height he dies (It is multiplied by 10
        // because the distance is multiplied by 10)
        if (player!!.state == Player.STATE_NORMAL && maxDistance - (5.5f * 10) > (player!!.position.y * 10)) {
            player!!.die()
        }
        if (player!!.state == Player.STATE_DEAD && maxDistance - (25 * 10) > (player!!.position.y * 10)) {
            state = STATE_GAMEOVER
        }
    }

    private fun removeObjects() {
        worldBox.getBodies(arrayBodies)

        for (body in arrayBodies) {
            if (!worldBox.isLocked) {
                if (body.userData is Platform) {
                    val platform = body.userData as Platform
                    if (platform.state == Platform.STATE_DESTROY) {
                        arrayPlatforms.removeValue(platform, true)
                        worldBox.destroyBody(body)
                        if (platform.type == Platform.TYPE_BREAKABLE) createPiecesOfPlatforms(
                            platform
                        )
                        Pools.free(platform)
                    }
                } else if (body.userData is Coin) {
                    val coin = body.userData as Coin
                    if (coin.state == Coin.STATE_TAKEN) {
                        arrayCoins.removeValue(coin, true)
                        worldBox.destroyBody(body)
                        Pools.free(coin)
                    }
                } else if (body.userData is PlatformPiece) {
                    val platformPiece = body.userData as PlatformPiece
                    if (platformPiece.state == PlatformPiece.STATE_DESTROY) {
                        arrayPlatformPieces.removeValue(platformPiece, true)
                        worldBox.destroyBody(body)
                        Pools.free(platformPiece)
                    }
                } else if (body.userData is Enemy) {
                    val oEnemy = body.userData as Enemy
                    if (oEnemy.state == Enemy.STATE_DEAD) {
                        arrayEnemies.removeValue(oEnemy, true)
                        worldBox.destroyBody(body)
                        Pools.free(oEnemy)
                    }
                } else if (body.userData is Item) {
                    val oItem = body.userData as Item
                    if (oItem.state == Item.STATE_TAKEN) {
                        arrayItem.removeValue(oItem, true)
                        worldBox.destroyBody(body)
                        Pools.free(oItem)
                    }
                } else if (body.userData is Cloud) {
                    val cloud = body.userData as Cloud
                    if (cloud.state == Cloud.STATE_DEAD) {
                        arrayClouds.removeValue(cloud, true)
                        worldBox.destroyBody(body)
                        Pools.free(cloud)
                    }
                } else if (body.userData is Lightning) {
                    val lightning = body.userData as Lightning
                    if (lightning.state == Lightning.STATE_DESTROY) {
                        arrayLightnings.removeValue(lightning, true)
                        worldBox.destroyBody(body)
                        Pools.free(lightning)
                    }
                } else if (body.userData is Bullet) {
                    val oBullet = body.userData as Bullet
                    if (oBullet.state == Bullet.STATE_DESTROY) {
                        arrayBullets.removeValue(oBullet, true)
                        worldBox.destroyBody(body)
                        Pools.free(oBullet)
                    }
                } else if (body.userData == "piso") {
                    if (player!!.position.y - 5.5f > body.position.y || player!!.state == Player.STATE_DEAD) {
                        worldBox.destroyBody(body)
                    }
                }
            }
        }
    }

    private fun updatePlayer(
        body: Body,
        delta: Float,
        accelerationX: Float,
        fire: Boolean,
        touchPositionWorldCoords: Vector3
    ) {
        player!!.update(body, delta, accelerationX)

        if (numBullets > 0 && fire) {
            createBullet(
                player!!.position.x,
                player!!.position.y,
                touchPositionWorldCoords.x,
                touchPositionWorldCoords.y
            )
            numBullets = numBullets - 1
        }
    }

    private fun updatePlatform(body: Body, delta: Float) {
        val obj = body.userData as Platform
        obj.update(delta)
        if (player!!.position.y - 5.5f > obj.position.y) {
            obj.setDestroy()
        }
    }

    private fun updatePlatformPieces(body: Body, delta: Float) {
        val obj = body.userData as PlatformPiece
        obj.update(delta, body)
        if (player!!.position.y - 5.5f > obj.position.y) {
            obj.setDestroy()
        }
    }

    private fun updateCoins(body: Body, delta: Float) {
        val obj = body.userData as Coin
        obj.update(delta)
        if (player!!.position.y - 5.5f > obj.position.y) {
            obj.take()
        }
    }

    private fun updateEnemy(body: Body, delta: Float) {
        val enemy = body.userData as Enemy
        enemy.update(body, delta)
        if (player!!.position.y - 5.5f > enemy.position.y) {
            enemy.hit()
        }
    }

    private fun updateItem(body: Body, delta: Float) {
        val obj = body.userData as Item
        obj.update(delta)
        if (player!!.position.y - 5.5f > obj.position.y) {
            obj.take()
        }
    }

    private fun updateCloud(body: Body, delta: Float) {
        val obj = body.userData as Cloud
        obj.update(body, delta)

        if (obj.isLightning) {
            createLightning(obj.position.x, obj.position.y - .65f)
            obj.fireLighting()
        }

        if (player!!.position.y - 5.5f > obj.position.y) {
            obj.destroy()
        }
    }

    private fun updateLightning(body: Body, delta: Float) {
        val lightning = body.userData as Lightning
        lightning.update(body, delta)

        if (player!!.position.y - 5.5f > lightning.position.y) {
            lightning.destroy()
        }
    }

    private fun updateBullet(body: Body, delta: Float) {
        val obj = body.userData as Bullet
        obj.update(body, delta)

        if (player!!.position.y - 5.5f > obj.position.y) {
            obj.destroy()
        }
    }

    internal inner class Collision : ContactListener {
        override fun beginContact(contact: Contact) {
            val a = contact.fixtureA
            val b = contact.fixtureB

            if (a.body.userData is Player) beginContactPlayer(b)
            else if (b.body.userData is Player) beginContactPlayer(a)

            if (a.body.userData is Bullet) beginContactBullet(a, b)
            else if (b.body.userData is Bullet) beginContactBullet(b, a)
        }

        private fun beginContactPlayer(otherFixture: Fixture) {
            val otherObject = otherFixture.body.userData

            if (otherObject == "piso") {
                player!!.jump()

                if (player!!.state == Player.STATE_DEAD) {
                    state = STATE_GAMEOVER
                }
            } else if (otherObject is Platform) {
                val obj = otherObject

                if (player!!.speed.y <= 0) {
                    player!!.jump()
                    if (obj.type == Platform.TYPE_BREAKABLE) {
                        obj.setDestroy()
                    }
                }
            } else if (otherObject is Coin) {
                otherObject.take()
                coins++
                player!!.jump()
            } else if (otherObject is Enemy) {
                player!!.hit()
            } else if (otherObject is Lightning) {
                player!!.hit()
            } else if (otherObject is Item) {
                val obj = otherObject
                obj.take()

                when (obj.type) {
                    Item.TYPE_BUBBLE -> player!!.setBubble()
                    Item.TYPE_JETPACK -> player!!.setJetPack()
                    Item.TYPE_GUN -> numBullets = numBullets + 10
                }
            }
        }

        private fun beginContactBullet(bulletFixture: Fixture, otherFixture: Fixture) {
            val otherObject = otherFixture.body.userData
            val bullet = bulletFixture.body.userData as Bullet

            if (otherObject is Enemy) {
                otherObject.hit()
                bullet.destroy()
            } else if (otherObject is Cloud) {
                otherObject.hit()
                bullet.destroy()
            }
        }

        override fun endContact(contact: Contact) {
        }

        override fun preSolve(contact: Contact, oldManifold: Manifold) {
            val a = contact.fixtureA
            val b = contact.fixtureB

            if (a.body.userData is Player) preSolveHero(a, b, contact)
            else if (b.body.userData is Player) preSolveHero(b, a, contact)
        }

        private fun preSolveHero(playerFixture: Fixture, otherFixture: Fixture, contact: Contact) {
            val otherObject = otherFixture.body.userData

            if (otherObject is Platform) {
                // If you go up, cross the platform.

                val otherPlatform = otherObject

                val playerY = playerFixture.body.position.y - .30f
                val platformY = otherPlatform.position.y + Platform.HEIGHT_NORMAL / 2f

                if (playerY < platformY) contact.isEnabled = false

                if (otherPlatform.type == Platform.TYPE_NORMAL && player!!.state == Player.STATE_DEAD) {
                    contact.isEnabled = false
                }
            }
        }

        override fun postSolve(contact: Contact, impulse: ContactImpulse) {
            // Nothing happens in here.
        }
    }

    companion object {
        const val STATE_RUNNING: Int = 0
        const val STATE_GAMEOVER: Int = 1
    }
}
