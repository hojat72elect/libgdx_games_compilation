package com.nopalsoft.ninjarunner.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pools;
import com.nopalsoft.ninjarunner.Settings;
import com.nopalsoft.ninjarunner.objects.Item;
import com.nopalsoft.ninjarunner.objects.Missile;
import com.nopalsoft.ninjarunner.objects.ObstacleBoxes4;
import com.nopalsoft.ninjarunner.objects.ObstacleBoxes7;
import com.nopalsoft.ninjarunner.objects.Pet;
import com.nopalsoft.ninjarunner.objects.Platform;
import com.nopalsoft.ninjarunner.objects.Player;
import com.nopalsoft.ninjarunner.objects.Wall;

public class ObjectManagerBox2d {

    WorldGame myWorld;
    World myWorldBox;

    public ObjectManagerBox2d(WorldGame myWorld) {
        this.myWorld = myWorld;
        myWorldBox = myWorld.myWorldBox;
    }

    public void createHeroStand(float x, float y, int type) {
        myWorld.myPlayer = new Player(x, y, type);

        BodyDef myBodyDefinition = new BodyDef();
        myBodyDefinition.position.x = x;
        myBodyDefinition.position.y = y;
        myBodyDefinition.type = BodyType.DynamicBody;

        Body myBody = myWorldBox.createBody(myBodyDefinition);

        recreateFixturePlayerStand(myBody);

        myBody.setFixedRotation(true);
        myBody.setUserData(myWorld.myPlayer);
        myBody.setBullet(true);
        myBody.setLinearVelocity(Player.VELOCITY_RUN, 0);

    }

    private void destroyAllFixturesFromBody(Body myBody) {
        for (Fixture fix : myBody.getFixtureList()) {
            myBody.destroyFixture(fix);
        }
        myBody.getFixtureList().clear();
    }

    public void recreateFixturePlayerStand(Body myBody) {
        destroyAllFixturesFromBody(myBody);// First I take away all the ones I have.

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Player.WIDTH / 2f, Player.HEIGHT / 2f);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.density = 10;
        fixture.friction = 0;
        Fixture myBodyFixture = myBody.createFixture(fixture);
        myBodyFixture.setUserData("cuerpo");

        PolygonShape sensorPiesShape = new PolygonShape();
        sensorPiesShape.setAsBox(Player.WIDTH / 2.2f, .025f, new Vector2(0, -.51f), 0);
        fixture.shape = sensorPiesShape;
        fixture.density = 0;
        fixture.restitution = 0f;
        fixture.friction = 0;
        fixture.isSensor = true;
        Fixture sensorPies = myBody.createFixture(fixture);
        sensorPies.setUserData("pies");

        shape.dispose();
        sensorPiesShape.dispose();

    }

    public void recreateFixturePlayerSlide(Body myBody) {
        destroyAllFixturesFromBody(myBody);// First I take away all the ones I have.

        PolygonShape shape = new PolygonShape();
        // Calculate that by the time the cube is created, how small is it so that it
        // remains in the correct position.
        shape.setAsBox(Player.WIDTH / 2f, Player.HEIGHT_SLIDE / 2f, new Vector2(0, -.25f), 0);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.density = 10;
        fixture.friction = 0;
        Fixture myBodyFixture = myBody.createFixture(fixture);
        myBodyFixture.setUserData("cuerpo");

        PolygonShape sensorPiesShape = new PolygonShape();
        sensorPiesShape.setAsBox(Player.WIDTH / 2.2f, .025f, new Vector2(0, -.51f), 0);
        fixture.shape = sensorPiesShape;
        fixture.density = 0;
        fixture.restitution = 0f;
        fixture.friction = 0;
        fixture.isSensor = true;
        Fixture sensorPies = myBody.createFixture(fixture);
        sensorPies.setUserData("pies");

        shape.dispose();
        sensorPiesShape.dispose();

    }

    public void createPet(float x, float y) {
        myWorld.myPet = new Pet(x, y, Settings.getSelectedPet());

        BodyDef bd = new BodyDef();
        bd.position.set(x, y);
        bd.type = BodyType.DynamicBody;

        Body body = myWorldBox.createBody(bd);

        CircleShape shape = new CircleShape();
        shape.setRadius(Pet.RADIUS);

        FixtureDef fixutre = new FixtureDef();
        fixutre.shape = shape;
        fixutre.isSensor = true;

        body.createFixture(fixutre);
        body.setUserData(myWorld.myPet);

        shape.dispose();

    }

    public float createItem(Class<? extends Item> itemClass, float x, float y) {
        Item obj = Pools.obtain(itemClass);
        x += obj.width / 2f;

        obj.initializeItem(x, y);

        BodyDef myBodyDefinition = new BodyDef();
        myBodyDefinition.position.set(obj.position.x, obj.position.y);
        myBodyDefinition.type = BodyType.KinematicBody;

        Body body = myWorldBox.createBody(myBodyDefinition);

        CircleShape shape = new CircleShape();
        shape.setRadius(obj.width / 2f);

        FixtureDef myFixtureDefinition = new FixtureDef();
        myFixtureDefinition.shape = shape;
        myFixtureDefinition.isSensor = true;

        body.createFixture(myFixtureDefinition);
        body.setUserData(obj);
        myWorld.arrayItems.add(obj);

        shape.dispose();

        return x + obj.width / 2f;

    }

    /**
     * Returns the position of the right edge of the box in.
     */
    public float createBox4(float x, float y) {
        ObstacleBoxes4 obj = Pools.obtain(ObstacleBoxes4.class);

        x += ObstacleBoxes4.DRAW_WIDTH / 2f;

        obj.initializeObstacle(x, y);

        BodyDef bd = new BodyDef();
        bd.position.set(x, y);
        bd.type = BodyType.StaticBody;

        Body body = myWorldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(.35f, .19f, new Vector2(0, -.19f), 0);

        FixtureDef fixutre = new FixtureDef();
        fixutre.shape = shape;
        fixutre.isSensor = true;
        body.createFixture(fixutre);

        shape.setAsBox(.18f, .19f, new Vector2(0, .19f), 0);
        fixutre = new FixtureDef();
        fixutre.shape = shape;
        fixutre.isSensor = true;
        body.createFixture(fixutre);

        body.setUserData(obj);
        myWorld.arrayObstacles.add(obj);

        shape.dispose();

        return x + ObstacleBoxes4.DRAW_WIDTH / 2f;

    }

    public float createBox7(float x, float y) {
        ObstacleBoxes7 obj = Pools.obtain(ObstacleBoxes7.class);

        x += ObstacleBoxes7.DRAW_WIDTH / 2f;

        obj.initializeObstacle(x, y);

        BodyDef bd = new BodyDef();
        bd.position.set(x, y);
        bd.type = BodyType.StaticBody;

        Body body = myWorldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(.35f, .38f, new Vector2(0, -.19f), 0);

        FixtureDef fixutre = new FixtureDef();
        fixutre.shape = shape;
        fixutre.isSensor = true;
        body.createFixture(fixutre);

        shape.setAsBox(.18f, .19f, new Vector2(0, .38f), 0);
        fixutre = new FixtureDef();
        fixutre.shape = shape;
        fixutre.isSensor = true;
        body.createFixture(fixutre);

        body.setUserData(obj);
        myWorld.arrayObstacles.add(obj);

        shape.dispose();

        return x + ObstacleBoxes7.DRAW_WIDTH / 2f;

    }

    /**
     * @param x        lower left position.
     * @param y       lower left position.
     * @param numberOfPlatforms number of platforms attached.
     */
    public float createPlatform(float x, float y, int numberOfPlatforms) {

        float yCenter = Platform.HEIGHT / 2f + y;

        float xHome = x;
        Platform myPlatform = null;
        for (int i = 0; i < numberOfPlatforms; i++) {
            myPlatform = Pools.obtain(Platform.class);
            x += Platform.WIDTH / 2f;
            myPlatform.initializePlatform(x, yCenter);
            myWorld.arrayPlatforms.add(myPlatform);
            // I subtract (-.01) so that there is one pixel on the left and the line does not
            // appear when two platforms are close together.
            x += Platform.WIDTH / 2f - .01f;
        }

        xHome += Platform.WIDTH / 2f * numberOfPlatforms - (.005f * numberOfPlatforms);

        // HERE I HAVE TO ADJUST THE X POSITION OF THE PLATFORM so that it does not overlap
        // with the previous ones.

        BodyDef myBodyDefinition = new BodyDef();
        myBodyDefinition.position.set(xHome, yCenter);
        myBodyDefinition.type = BodyType.StaticBody;

        Body body = myWorldBox.createBody(myBodyDefinition);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Platform.WIDTH / 2f * numberOfPlatforms - (.005f * numberOfPlatforms), Platform.HEIGHT / 2f);

        FixtureDef fixutre = new FixtureDef();
        fixutre.shape = shape;
        fixutre.friction = 0;

        body.createFixture(fixutre);
        body.setUserData(myPlatform);

        shape.dispose();

        return xHome + Platform.WIDTH * numberOfPlatforms / 2f;

    }

    public float createWall(float x, float y) {
        Wall myWall = Pools.obtain(Wall.class);

        x += Wall.WIDTH / 2f;
        myWall.initializeWall(x, y);

        BodyDef bd = new BodyDef();
        bd.position.set(myWall.position.x, myWall.position.y);
        bd.type = BodyType.StaticBody;

        Body body = myWorldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Wall.WIDTH / 2f, Wall.HEIGHT / 2f);

        FixtureDef fixutre = new FixtureDef();
        fixutre.shape = shape;
        fixutre.isSensor = true;

        body.createFixture(fixutre);
        body.setUserData(myWall);
        myWorld.arrayWall.add(myWall);

        shape.dispose();

        return x + Wall.WIDTH / 2f;

    }

    public void crearMissil(float x, float y) {
        Missile obj = Pools.obtain(Missile.class);
        obj.initializeMissile(x, y);

        BodyDef bd = new BodyDef();
        bd.position.set(obj.position.x, obj.position.y);
        bd.type = BodyType.KinematicBody;

        Body body = myWorldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Missile.WIDTH / 2f, Missile.HEIGHT / 2f);

        FixtureDef fixutre = new FixtureDef();
        fixutre.shape = shape;
        fixutre.isSensor = true;

        body.createFixture(fixutre);
        body.setUserData(obj);
        body.setLinearVelocity(Missile.SPEED_X, 0);
        myWorld.arrayMissiles.add(obj);

        shape.dispose();

    }

}
