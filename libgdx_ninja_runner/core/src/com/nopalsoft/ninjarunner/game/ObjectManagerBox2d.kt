package com.nopalsoft.ninjarunner.game

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.utils.Pools
import com.nopalsoft.ninjarunner.Settings.selectedPet
import com.nopalsoft.ninjarunner.objects.*

class ObjectManagerBox2d(var myWorld: WorldGame) {
    var myWorldBox: World = myWorld.myWorldBox

    fun createHeroStand(x: Float, y: Float, type: Int) {
        myWorld.myPlayer = Player(x, y, type)

        val myBodyDefinition = BodyDef()
        myBodyDefinition.position.x = x
        myBodyDefinition.position.y = y
        myBodyDefinition.type = BodyType.DynamicBody

        val myBody = myWorldBox.createBody(myBodyDefinition)

        recreateFixturePlayerStand(myBody)

        myBody.isFixedRotation = true
        myBody.userData = myWorld.myPlayer
        myBody.isBullet = true
        myBody.setLinearVelocity(Player.VELOCITY_RUN, 0f)
    }

    private fun destroyAllFixturesFromBody(myBody: Body) {
        for (fix in myBody.fixtureList) {
            myBody.destroyFixture(fix)
        }
        myBody.fixtureList.clear()
    }

    fun recreateFixturePlayerStand(myBody: Body) {
        destroyAllFixturesFromBody(myBody) // First I take away all the ones I have.

        val shape = PolygonShape()
        shape.setAsBox(Player.WIDTH / 2f, Player.HEIGHT / 2f)

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.density = 10f
        fixture.friction = 0f
        val myBodyFixture = myBody.createFixture(fixture)
        myBodyFixture.userData = "cuerpo"

        val sensorPiesShape = PolygonShape()
        sensorPiesShape.setAsBox(Player.WIDTH / 2.2f, .025f, Vector2(0f, -.51f), 0f)
        fixture.shape = sensorPiesShape
        fixture.density = 0f
        fixture.restitution = 0f
        fixture.friction = 0f
        fixture.isSensor = true
        val sensorPies = myBody.createFixture(fixture)
        sensorPies.userData = "pies"

        shape.dispose()
        sensorPiesShape.dispose()
    }

    fun recreateFixturePlayerSlide(myBody: Body) {
        destroyAllFixturesFromBody(myBody) // First I take away all the ones I have.

        val shape = PolygonShape()
        // Calculate that by the time the cube is created, how small is it so that it
        // remains in the correct position.
        shape.setAsBox(Player.WIDTH / 2f, Player.HEIGHT_SLIDE / 2f, Vector2(0f, -.25f), 0f)

        val fixture = FixtureDef()
        fixture.shape = shape
        fixture.density = 10f
        fixture.friction = 0f
        val myBodyFixture = myBody.createFixture(fixture)
        myBodyFixture.userData = "cuerpo"

        val sensorPiesShape = PolygonShape()
        sensorPiesShape.setAsBox(Player.WIDTH / 2.2f, .025f, Vector2(0f, -.51f), 0f)
        fixture.shape = sensorPiesShape
        fixture.density = 0f
        fixture.restitution = 0f
        fixture.friction = 0f
        fixture.isSensor = true
        val sensorPies = myBody.createFixture(fixture)
        sensorPies.userData = "pies"

        shape.dispose()
        sensorPiesShape.dispose()
    }

    fun createPet(x: Float, y: Float) {
        myWorld.myPet = Pet(x, y, selectedPet)

        val bd = BodyDef()
        bd.position[x] = y
        bd.type = BodyType.DynamicBody

        val body = myWorldBox.createBody(bd)

        val shape = CircleShape()
        shape.radius = Pet.RADIUS

        val fixutre = FixtureDef()
        fixutre.shape = shape
        fixutre.isSensor = true

        body.createFixture(fixutre)
        body.userData = myWorld.myPet

        shape.dispose()
    }

    fun createItem(itemClass: Class<out Item?>?, x: Float, y: Float): Float {
        var x = x
        val obj = Pools.obtain(itemClass)!!
        x += obj.width / 2f

        obj.initializeItem(x, y)

        val myBodyDefinition = BodyDef()
        myBodyDefinition.position[obj.position.x] = obj.position.y
        myBodyDefinition.type = BodyType.KinematicBody

        val body = myWorldBox.createBody(myBodyDefinition)

        val shape = CircleShape()
        shape.radius = obj.width / 2f

        val myFixtureDefinition = FixtureDef()
        myFixtureDefinition.shape = shape
        myFixtureDefinition.isSensor = true

        body.createFixture(myFixtureDefinition)
        body.userData = obj
        myWorld.arrayItems.add(obj)

        shape.dispose()

        return x + obj.width / 2f
    }

    /**
     * Returns the position of the right edge of the box in.
     */
    fun createBox4(x: Float, y: Float): Float {
        var x = x
        val obj = Pools.obtain(ObstacleBoxes4::class.java)

        x += ObstacleBoxes4.DRAW_WIDTH / 2f

        obj.initializeObstacle(x, y)

        val bd = BodyDef()
        bd.position[x] = y
        bd.type = BodyType.StaticBody

        val body = myWorldBox.createBody(bd)

        val shape = PolygonShape()
        shape.setAsBox(.35f, .19f, Vector2(0f, -.19f), 0f)

        var fixutre = FixtureDef()
        fixutre.shape = shape
        fixutre.isSensor = true
        body.createFixture(fixutre)

        shape.setAsBox(.18f, .19f, Vector2(0f, .19f), 0f)
        fixutre = FixtureDef()
        fixutre.shape = shape
        fixutre.isSensor = true
        body.createFixture(fixutre)

        body.userData = obj
        myWorld.arrayObstacles.add(obj)

        shape.dispose()

        return x + ObstacleBoxes4.DRAW_WIDTH / 2f
    }

    fun createBox7(x: Float, y: Float): Float {
        var x = x
        val obj = Pools.obtain(ObstacleBoxes7::class.java)

        x += ObstacleBoxes7.DRAW_WIDTH / 2f

        obj.initializeObstacle(x, y)

        val bd = BodyDef()
        bd.position[x] = y
        bd.type = BodyType.StaticBody

        val body = myWorldBox.createBody(bd)

        val shape = PolygonShape()
        shape.setAsBox(.35f, .38f, Vector2(0f, -.19f), 0f)

        var fixutre = FixtureDef()
        fixutre.shape = shape
        fixutre.isSensor = true
        body.createFixture(fixutre)

        shape.setAsBox(.18f, .19f, Vector2(0f, .38f), 0f)
        fixutre = FixtureDef()
        fixutre.shape = shape
        fixutre.isSensor = true
        body.createFixture(fixutre)

        body.userData = obj
        myWorld.arrayObstacles.add(obj)

        shape.dispose()

        return x + ObstacleBoxes7.DRAW_WIDTH / 2f
    }

    /**
     * @param x        lower left position.
     * @param y       lower left position.
     * @param numberOfPlatforms number of platforms attached.
     */
    fun createPlatform(x: Float, y: Float, numberOfPlatforms: Int): Float {
        var x = x
        val yCenter = Platform.HEIGHT / 2f + y

        var xHome = x
        var myPlatform: Platform? = null
        for (i in 0 until numberOfPlatforms) {
            myPlatform = Pools.obtain(Platform::class.java)
            x += Platform.WIDTH / 2f
            myPlatform.initializePlatform(x, yCenter)
            myWorld.arrayPlatforms.add(myPlatform)
            // I subtract (-.01) so that there is one pixel on the left and the line does not
            // appear when two platforms are close together.
            x += Platform.WIDTH / 2f - .01f
        }

        xHome += Platform.WIDTH / 2f * numberOfPlatforms - (.005f * numberOfPlatforms)

        // HERE I HAVE TO ADJUST THE X POSITION OF THE PLATFORM so that it does not overlap
        // with the previous ones.
        val myBodyDefinition = BodyDef()
        myBodyDefinition.position[xHome] = yCenter
        myBodyDefinition.type = BodyType.StaticBody

        val body = myWorldBox.createBody(myBodyDefinition)

        val shape = PolygonShape()
        shape.setAsBox(Platform.WIDTH / 2f * numberOfPlatforms - (.005f * numberOfPlatforms), Platform.HEIGHT / 2f)

        val fixutre = FixtureDef()
        fixutre.shape = shape
        fixutre.friction = 0f

        body.createFixture(fixutre)
        body.userData = myPlatform

        shape.dispose()

        return xHome + Platform.WIDTH * numberOfPlatforms / 2f
    }

    fun createWall(x: Float, y: Float): Float {
        var x = x
        val myWall = Pools.obtain(Wall::class.java)

        x += Wall.WIDTH / 2f
        myWall.initializeWall(x, y)

        val bd = BodyDef()
        bd.position[myWall.position.x] = myWall.position.y
        bd.type = BodyType.StaticBody

        val body = myWorldBox.createBody(bd)

        val shape = PolygonShape()
        shape.setAsBox(Wall.WIDTH / 2f, Wall.HEIGHT / 2f)

        val fixutre = FixtureDef()
        fixutre.shape = shape
        fixutre.isSensor = true

        body.createFixture(fixutre)
        body.userData = myWall
        myWorld.arrayWall.add(myWall)

        shape.dispose()

        return x + Wall.WIDTH / 2f
    }

    fun crearMissil(x: Float, y: Float) {
        val obj = Pools.obtain(Missile::class.java)
        obj.initializeMissile(x, y)

        val bd = BodyDef()
        bd.position[obj.position.x] = obj.position.y
        bd.type = BodyType.KinematicBody

        val body = myWorldBox.createBody(bd)

        val shape = PolygonShape()
        shape.setAsBox(Missile.WIDTH / 2f, Missile.HEIGHT / 2f)

        val fixutre = FixtureDef()
        fixutre.shape = shape
        fixutre.isSensor = true

        body.createFixture(fixutre)
        body.userData = obj
        body.setLinearVelocity(Missile.SPEED_X, 0f)
        myWorld.arrayMissiles.add(obj)

        shape.dispose()
    }
}
