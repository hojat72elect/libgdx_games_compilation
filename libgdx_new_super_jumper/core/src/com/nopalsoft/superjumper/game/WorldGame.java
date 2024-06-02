package com.nopalsoft.superjumper.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.nopalsoft.superjumper.Settings;
import com.nopalsoft.superjumper.objects.Bullet;
import com.nopalsoft.superjumper.objects.Cloud;
import com.nopalsoft.superjumper.objects.Coin;
import com.nopalsoft.superjumper.objects.Enemy;
import com.nopalsoft.superjumper.objects.Item;
import com.nopalsoft.superjumper.objects.Platform;
import com.nopalsoft.superjumper.objects.PlatformPiece;
import com.nopalsoft.superjumper.objects.Player;
import com.nopalsoft.superjumper.objects.Lightning;
import com.nopalsoft.superjumper.screens.BasicScreen;

public class WorldGame {


    final public static int STATE_RUNNING = 0;
    final public static int STATE_GAMEOVER = 1;
    private final Array<Body> arrayBodies;
    public World worldBox;
    public int coins;
    public int maxDistance;
    int state;
    float TIME_TO_CREATE_CLOUD = 15;
    float timeToCreateCloud;
    Player player;
    Array<Platform> arrayPlatforms;
    Array<PlatformPiece> arrayPlatformPieces;
    Array<Coin> arrayCoins;
    Array<Enemy> arrayEnemies;
    Array<Item> arrayItem;
    Array<Cloud> arrayClouds;
    Array<Lightning> arrayRays;
    Array<Bullet> arrayBullets;
    float worldCreatedUpToY;

    public WorldGame() {
        worldBox = new World(new Vector2(0, -9.8f), true);
        worldBox.setContactListener(new com.nopalsoft.superjumper.game.WorldGame.Collision());

        arrayBodies = new Array<>();
        arrayPlatforms = new Array<>();
        arrayPlatformPieces = new Array<>();
        arrayCoins = new Array<>();
        arrayEnemies = new Array<>();
        arrayItem = new Array<>();
        arrayClouds = new Array<>();
        arrayRays = new Array<>();
        arrayBullets = new Array<>();

        timeToCreateCloud = 0;

        state = STATE_RUNNING;

        createFloor();
        crearPersonaje();

        worldCreatedUpToY = player.position.y;
        createNextPart();

    }

    private void createNextPart() {
        float y = worldCreatedUpToY + 2;

        for (int i = 0; worldCreatedUpToY < (y + 10); i++) {
            worldCreatedUpToY = y + (i * 2);

            createPlatform(worldCreatedUpToY);
            createPlatform(worldCreatedUpToY);

            if (MathUtils.random(100) < 5)
                Coin.createCoins(worldBox, arrayCoins, worldCreatedUpToY);

            if (MathUtils.random(20) < 5)
                Coin.createACoin(worldBox, arrayCoins, worldCreatedUpToY + .5f);

            if (MathUtils.random(20) < 5)
                createEnemy(worldCreatedUpToY + .5f);

            if (timeToCreateCloud >= TIME_TO_CREATE_CLOUD) {
                createClouds(worldCreatedUpToY + .7f);
                timeToCreateCloud = 0;
            }

            if (MathUtils.random(50) < 5)
                createItem(worldCreatedUpToY + .5f);
        }

    }

    /**
     * The floor only appears 1 time, at the beginning of the game.
     */
    private void createFloor() {
        BodyDef bodyDefinition = new BodyDef();
        bodyDefinition.type = BodyType.StaticBody;

        Body body = worldBox.createBody(bodyDefinition);

        EdgeShape shape = new EdgeShape();
        shape.set(0, 0, BasicScreen.WORLD_WIDTH, 0);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;

        body.createFixture(fixtureDefinition);
        body.setUserData("piso");

        shape.dispose();

    }

    private void crearPersonaje() {
        player = new Player(2.4f, .5f);

        BodyDef bd = new BodyDef();
        bd.position.set(player.position.x, player.position.y);
        bd.type = BodyType.DynamicBody;

        Body body = worldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Player.WIDTH / 2f, Player.HEIGHT / 2f);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.density = 10;
        fixtureDefinition.friction = 0;
        fixtureDefinition.restitution = 0;

        body.createFixture(fixtureDefinition);
        body.setUserData(player);
        body.setFixedRotation(true);

        shape.dispose();
    }

    private void createPlatform(float y) {

        Platform newPlatform = Pools.obtain(Platform.class);
        newPlatform.initializePlatform(MathUtils.random(BasicScreen.WORLD_WIDTH), y, MathUtils.random(1));

        BodyDef bodyDefinition = new BodyDef();
        bodyDefinition.position.set(newPlatform.position.x, newPlatform.position.y);
        bodyDefinition.type = BodyType.KinematicBody;

        Body body = worldBox.createBody(bodyDefinition);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Platform.WIDTH_NORMAL / 2f, Platform.HEIGHT_NORMAL / 2f);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;

        body.createFixture(fixture);
        body.setUserData(newPlatform);
        arrayPlatforms.add(newPlatform);

        shape.dispose();

    }

    /**
     * The breakable platform is 2 squares.
     */
    private void createPiecesOfPlatforms(Platform oPlat) {
        createPiecesOfPlatforms(oPlat, PlatformPiece.TYPE_LEFT);
        createPiecesOfPlatforms(oPlat, PlatformPiece.TYPE_RIGHT);

    }

    private void createPiecesOfPlatforms(Platform platform, int type) {
        PlatformPiece piece;
        float x;
        float angularVelocity = 100;

        if (type == PlatformPiece.TYPE_LEFT) {
            x = platform.position.x - PlatformPiece.WIDTH_NORMAL / 2f;
            angularVelocity *= -1;
        } else {
            x = platform.position.x + PlatformPiece.WIDTH_NORMAL / 2f;
        }

        piece = Pools.obtain(PlatformPiece.class);
        piece.initializePlatformPiece(x, platform.position.y, type, platform.color);

        BodyDef bodyDefinition = new BodyDef();
        bodyDefinition.position.set(piece.position.x, piece.position.y);
        bodyDefinition.type = BodyType.DynamicBody;

        Body body = worldBox.createBody(bodyDefinition);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(PlatformPiece.WIDTH_NORMAL / 2f, PlatformPiece.HEIGHT_NORMAL / 2f);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.isSensor = true;

        body.createFixture(fixture);
        body.setUserData(piece);
        body.setAngularVelocity(MathUtils.degRad * angularVelocity);
        arrayPlatformPieces.add(piece);

        shape.dispose();
    }

    private void createEnemy(float y) {
        Enemy enemy = Pools.obtain(Enemy.class);
        enemy.initializeEnemy(MathUtils.random(BasicScreen.WORLD_WIDTH), y);

        BodyDef bodyDefinition = new BodyDef();
        bodyDefinition.position.set(enemy.position.x, enemy.position.y);
        bodyDefinition.type = BodyType.DynamicBody;

        Body body = worldBox.createBody(bodyDefinition);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Enemy.WIDTH / 2f, Enemy.HEIGHT / 2f);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.isSensor = true;

        body.createFixture(fixture);
        body.setUserData(enemy);
        body.setGravityScale(0);

        float speed = MathUtils.random(1f, Enemy.SPEED_X);

        if (MathUtils.randomBoolean())
            body.setLinearVelocity(speed, 0);
        else
            body.setLinearVelocity(-speed, 0);
        arrayEnemies.add(enemy);

        shape.dispose();
    }

    private void createItem(float y) {
        Item newItem = Pools.obtain(Item.class);
        newItem.initializeItem(MathUtils.random(BasicScreen.WORLD_WIDTH), y);

        BodyDef bodyDefinition = new BodyDef();
        bodyDefinition.position.set(newItem.position.x, newItem.position.y);
        bodyDefinition.type = BodyType.StaticBody;
        Body body = worldBox.createBody(bodyDefinition);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Item.WIDTH / 2f, Item.HEIGHT / 2f);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.isSensor = true;
        body.createFixture(fixture);
        body.setUserData(newItem);
        shape.dispose();
        arrayItem.add(newItem);
    }

    private void createClouds(float y) {
        Cloud cloud = Pools.obtain(Cloud.class);
        cloud.initializeCloud(MathUtils.random(BasicScreen.WORLD_WIDTH), y);

        BodyDef bodyDefinition = new BodyDef();
        bodyDefinition.position.set(cloud.position.x, cloud.position.y);
        bodyDefinition.type = BodyType.DynamicBody;

        Body body = worldBox.createBody(bodyDefinition);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Cloud.WIDTH / 2f, Cloud.HEIGHT / 2f);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.isSensor = true;

        body.createFixture(fixture);
        body.setUserData(cloud);
        body.setGravityScale(0);

        float speed = MathUtils.random(1f, Cloud.SPEED_X);

        if (MathUtils.randomBoolean())
            body.setLinearVelocity(speed, 0);
        else
            body.setLinearVelocity(-speed, 0);
        arrayClouds.add(cloud);

        shape.dispose();
    }

    private void createLightning(float x, float y) {
        Lightning lightning = Pools.obtain(Lightning.class);
        lightning.initializeRay(x, y);

        BodyDef bd = new BodyDef();
        bd.position.set(lightning.position.x, lightning.position.y);
        bd.type = BodyType.KinematicBody;

        Body body = worldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Lightning.WIDTH / 2f, Lightning.HEIGHT / 2f);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.isSensor = true;

        body.createFixture(fixture);
        body.setUserData(lightning);

        body.setLinearVelocity(0, Lightning.Y_SPEED);
        arrayRays.add(lightning);

        shape.dispose();
    }

    private void createBullet(float originX, float originY, float destinationX, float destinationY) {
        Bullet bullet = Pools.obtain(Bullet.class);
        bullet.initializeBullet(originX, originY);

        BodyDef bd = new BodyDef();
        bd.position.set(bullet.position.x, bullet.position.y);
        bd.type = BodyType.KinematicBody;

        Body body = worldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Bullet.SIZE / 2f, Bullet.SIZE / 2f);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.isSensor = true;

        body.createFixture(fixture);
        body.setUserData(bullet);
        body.setBullet(true);

        Vector2 destination = new Vector2(destinationX, destinationY);
        destination.sub(bullet.position).nor().scl(Bullet.XY_SPEED);

        body.setLinearVelocity(destination.x, destination.y);

        arrayBullets.add(bullet);

        shape.dispose();
    }

    public void update(float delta, float acelX, boolean fire, Vector3 touchPositionWorldCoords) {
        worldBox.step(delta, 8, 4);

        removeObjects();

        if (player.position.y + 10 > worldCreatedUpToY) {
            createNextPart();
        }

        timeToCreateCloud += delta;// Actualizo el tiempo para crear una nube

        worldBox.getBodies(arrayBodies);

        for (Body body : arrayBodies) {
            if (body.getUserData() instanceof Player) {
                updatePlayer(body, delta, acelX, fire, touchPositionWorldCoords);
            } else if (body.getUserData() instanceof Platform) {
                updatePlataforma(body, delta);
            } else if (body.getUserData() instanceof PlatformPiece) {
                updatePiezaPlataforma(body, delta);
            } else if (body.getUserData() instanceof Coin) {
                updateMoneda(body, delta);
            } else if (body.getUserData() instanceof Enemy) {
                updateEnemy(body, delta);
            } else if (body.getUserData() instanceof Item) {
                updateItem(body, delta);
            } else if (body.getUserData() instanceof Cloud) {
                updateNube(body, delta);
            } else if (body.getUserData() instanceof Lightning) {
                updateLightning(body, delta);
            } else if (body.getUserData() instanceof Bullet) {
                updateBullet(body, delta);
            }

        }

        if (maxDistance < (player.position.y * 10)) {
            maxDistance = (int) (player.position.y * 10);
        }

        // If the character is 5.5f below the maximum height he dies (It is multiplied by 10
        // because the distance is multiplied by 10)
        if (player.state == Player.STATE_NORMAL && maxDistance - (5.5f * 10) > (player.position.y * 10)) {
            player.die();
        }
        if (player.state == Player.STATE_DEAD && maxDistance - (25 * 10) > (player.position.y * 10)) {
            state = STATE_GAMEOVER;
        }

    }

    private void removeObjects() {
        worldBox.getBodies(arrayBodies);

        for (Body body : arrayBodies) {
            if (!worldBox.isLocked()) {

                if (body.getUserData() instanceof Platform) {
                    Platform platform = (Platform) body.getUserData();
                    if (platform.state == Platform.STATE_DESTROY) {
                        arrayPlatforms.removeValue(platform, true);
                        worldBox.destroyBody(body);
                        if (platform.type == Platform.TYPE_BREAKABLE)
                            createPiecesOfPlatforms(platform);
                        Pools.free(platform);
                    }
                } else if (body.getUserData() instanceof Coin) {
                    Coin coin = (Coin) body.getUserData();
                    if (coin.state == Coin.STATE_TAKEN) {
                        arrayCoins.removeValue(coin, true);
                        worldBox.destroyBody(body);
                        Pools.free(coin);
                    }
                } else if (body.getUserData() instanceof PlatformPiece) {
                    PlatformPiece platformPiece = (PlatformPiece) body.getUserData();
                    if (platformPiece.state == PlatformPiece.STATE_DESTROY) {
                        arrayPlatformPieces.removeValue(platformPiece, true);
                        worldBox.destroyBody(body);
                        Pools.free(platformPiece);
                    }
                } else if (body.getUserData() instanceof Enemy) {
                    Enemy oEnemy = (Enemy) body.getUserData();
                    if (oEnemy.state == Enemy.STATE_DEAD) {
                        arrayEnemies.removeValue(oEnemy, true);
                        worldBox.destroyBody(body);
                        Pools.free(oEnemy);
                    }
                } else if (body.getUserData() instanceof Item) {
                    Item oItem = (Item) body.getUserData();
                    if (oItem.state == Item.STATE_TAKEN) {
                        arrayItem.removeValue(oItem, true);
                        worldBox.destroyBody(body);
                        Pools.free(oItem);
                    }
                } else if (body.getUserData() instanceof Cloud) {
                    Cloud cloud = (Cloud) body.getUserData();
                    if (cloud.state == Cloud.STATE_DEAD) {
                        arrayClouds.removeValue(cloud, true);
                        worldBox.destroyBody(body);
                        Pools.free(cloud);
                    }
                } else if (body.getUserData() instanceof Lightning) {
                    Lightning lightning = (Lightning) body.getUserData();
                    if (lightning.state == Lightning.STATE_DESTROY) {
                        arrayRays.removeValue(lightning, true);
                        worldBox.destroyBody(body);
                        Pools.free(lightning);
                    }
                } else if (body.getUserData() instanceof Bullet) {
                    Bullet oBullet = (Bullet) body.getUserData();
                    if (oBullet.getState() == Bullet.STATE_DESTROY) {
                        arrayBullets.removeValue(oBullet, true);
                        worldBox.destroyBody(body);
                        Pools.free(oBullet);
                    }
                } else if (body.getUserData().equals("piso")) {
                    if (player.position.y - 5.5f > body.getPosition().y || player.state == Player.STATE_DEAD) {
                        worldBox.destroyBody(body);
                    }
                }
            }
        }
    }

    private void updatePlayer(Body body, float delta, float accelerationX, boolean fire, Vector3 touchPositionWorldCoords) {
        player.update(body, delta, accelerationX);

        if (Settings.getNumBullets() > 0 && fire) {
            createBullet(player.position.x, player.position.y, touchPositionWorldCoords.x, touchPositionWorldCoords.y);
            Settings.setNumBullets(Settings.getNumBullets() - 1);

        }

    }

    private void updatePlataforma(Body body, float delta) {
        Platform obj = (Platform) body.getUserData();
        obj.update(delta);
        if (player.position.y - 5.5f > obj.position.y) {
            obj.setDestroy();
        }
    }

    private void updatePiezaPlataforma(Body body, float delta) {
        PlatformPiece obj = (PlatformPiece) body.getUserData();
        obj.update(delta, body);
        if (player.position.y - 5.5f > obj.position.y) {
            obj.setDestroy();
        }

    }

    private void updateMoneda(Body body, float delta) {
        Coin obj = (Coin) body.getUserData();
        obj.update(delta);
        if (player.position.y - 5.5f > obj.position.y) {
            obj.take();
        }

    }

    private void updateEnemy(Body body, float delta) {
        Enemy enemy = (Enemy) body.getUserData();
        enemy.update(body, delta);
        if (player.position.y - 5.5f > enemy.position.y) {
            enemy.hit();
        }

    }

    private void updateItem(Body body, float delta) {
        Item obj = (Item) body.getUserData();
        obj.update(delta);
        if (player.position.y - 5.5f > obj.position.y) {
            obj.take();
        }
    }

    private void updateNube(Body body, float delta) {
        Cloud obj = (Cloud) body.getUserData();
        obj.update(body, delta);

        if (obj.isLightning) {
            createLightning(obj.position.x, obj.position.y - .65f);
            obj.fireLighting();
        }

        if (player.position.y - 5.5f > obj.position.y) {
            obj.destroy();
        }
    }

    private void updateLightning(Body body, float delta) {
        Lightning lightning = (Lightning) body.getUserData();
        lightning.update(body, delta);

        if (player.position.y - 5.5f > lightning.position.y) {
            lightning.destroy();
        }
    }

    private void updateBullet(Body body, float delta) {
        Bullet obj = (Bullet) body.getUserData();
        obj.update(body, delta);

        if (player.position.y - 5.5f > obj.position.y) {
            obj.destroy();
        }
    }

    class Collision implements ContactListener {

        @Override
        public void beginContact(Contact contact) {
            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();

            if (a.getBody().getUserData() instanceof Player)
                beginContactPlayer(b);
            else if (b.getBody().getUserData() instanceof Player)
                beginContactPlayer(a);

            if (a.getBody().getUserData() instanceof Bullet)
                beginContactBullet(a, b);
            else if (b.getBody().getUserData() instanceof Bullet)
                beginContactBullet(b, a);

        }

        private void beginContactPlayer(Fixture otherFixture) {
            Object otherObject = otherFixture.getBody().getUserData();

            if (otherObject.equals("piso")) {
                player.jump();

                if (player.state == Player.STATE_DEAD) {
                    state = STATE_GAMEOVER;
                }
            } else if (otherObject instanceof Platform) {
                Platform obj = (Platform) otherObject;

                if (player.speed.y <= 0) {
                    player.jump();
                    if (obj.type == Platform.TYPE_BREAKABLE) {
                        obj.setDestroy();
                    }
                }

            } else if (otherObject instanceof Coin) {
                Coin obj = (Coin) otherObject;
                obj.take();
                coins++;
                player.jump();
            } else if (otherObject instanceof Enemy) {
                player.hit();
            } else if (otherObject instanceof Lightning) {
                player.hit();
            } else if (otherObject instanceof Item) {
                Item obj = (Item) otherObject;
                obj.take();

                switch (obj.type) {
                    case Item.TYPE_BUBBLE:
                        player.setBubble();
                        break;
                    case Item.TYPE_JETPACK:
                        player.setJetPack();
                        break;
                    case Item.TYPE_GUN:
                        Settings.setNumBullets(Settings.getNumBullets() + 10);
                        break;

                }

            }

        }

        private void beginContactBullet(Fixture fixBullet, Fixture fixOtraCosa) {
            Object otherObject = fixOtraCosa.getBody().getUserData();
            Bullet bullet = (Bullet) fixBullet.getBody().getUserData();

            if (otherObject instanceof Enemy) {
                Enemy enemy = (Enemy) otherObject;
                enemy.hit();
                bullet.destroy();

            } else if (otherObject instanceof Cloud) {
                Cloud obj = (Cloud) otherObject;
                obj.hit();
                bullet.destroy();

            }
        }

        @Override
        public void endContact(Contact contact) {

        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {
            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();

            if (a.getBody().getUserData() instanceof Player)
                preSolveHero(a, b, contact);
            else if (b.getBody().getUserData() instanceof Player)
                preSolveHero(b, a, contact);

        }

        private void preSolveHero(Fixture fixPersonaje, Fixture otraCosa, Contact contact) {
            Object oOtraCosa = otraCosa.getBody().getUserData();

            if (oOtraCosa instanceof Platform) {
                // If you go up, cross the platform.

                Platform obj = (Platform) oOtraCosa;

                float ponyY = fixPersonaje.getBody().getPosition().y - .30f;
                float pisY = obj.position.y + Platform.HEIGHT_NORMAL / 2f;

                if (ponyY < pisY)
                    contact.setEnabled(false);

                if (obj.type == Platform.TYPE_NORMAL && player.state == Player.STATE_DEAD) {
                    contact.setEnabled(false);
                }

            }

        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {
            // Nothing happens in here.
        }

    }

}
