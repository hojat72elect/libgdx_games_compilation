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
import com.nopalsoft.superjumper.objects.Ray;
import com.nopalsoft.superjumper.screens.Screens;
import com.nopalsoft.superjumper.objects.Player;

public class WorldGame {


    final public static int STATE_RUNNING = 0;
    final public static int STATE_GAMEOVER = 1;
    private final Array<Body> arrayBodies;
    public World myWorldBox;
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
    Array<Ray> arrayRays;
    Array<Bullet> arrayBullets;
    float worldCreatedUpToY;

    public WorldGame() {
        myWorldBox = new World(new Vector2(0, -9.8f), true);
        myWorldBox.setContactListener(new Colisiones());

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

        crearPiso();
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
                Coin.createCoins(myWorldBox, arrayCoins, worldCreatedUpToY);

            if (MathUtils.random(20) < 5)
                Coin.createACoin(myWorldBox, arrayCoins, worldCreatedUpToY + .5f);

            if (MathUtils.random(20) < 5)
                crearEnemigo(worldCreatedUpToY + .5f);

            if (timeToCreateCloud >= TIME_TO_CREATE_CLOUD) {
                crearNubes(worldCreatedUpToY + .7f);
                timeToCreateCloud = 0;
            }

            if (MathUtils.random(50) < 5)
                createItem(worldCreatedUpToY + .5f);
        }

    }

    /**
     * El piso solo aparece 1 vez, al principio del juego
     */
    private void crearPiso() {
        BodyDef bd = new BodyDef();
        bd.type = BodyType.StaticBody;

        Body body = myWorldBox.createBody(bd);

        EdgeShape shape = new EdgeShape();
        shape.set(0, 0, Screens.WORLD_WIDTH, 0);

        FixtureDef fixutre = new FixtureDef();
        fixutre.shape = shape;

        body.createFixture(fixutre);
        body.setUserData("piso");

        shape.dispose();

    }

    private void crearPersonaje() {
        player = new Player(2.4f, .5f);

        BodyDef bd = new BodyDef();
        bd.position.set(player.position.x, player.position.y);
        bd.type = BodyType.DynamicBody;

        Body body = myWorldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Player.WIDTH / 2f, Player.HEIGHT / 2f);

        FixtureDef fixutre = new FixtureDef();
        fixutre.shape = shape;
        fixutre.density = 10;
        fixutre.friction = 0;
        fixutre.restitution = 0;

        body.createFixture(fixutre);
        body.setUserData(player);
        body.setFixedRotation(true);

        shape.dispose();
    }

    private void createPlatform(float y) {

        Platform oPlat = Pools.obtain(Platform.class);
        oPlat.init(MathUtils.random(Screens.WORLD_WIDTH), y, MathUtils.random(1));

        BodyDef bd = new BodyDef();
        bd.position.set(oPlat.position.x, oPlat.position.y);
        bd.type = BodyType.KinematicBody;

        Body body = myWorldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Platform.WIDTH_NORMAL / 2f, Platform.HEIGHT_NORMAL / 2f);

        FixtureDef fixutre = new FixtureDef();
        fixutre.shape = shape;

        body.createFixture(fixutre);
        body.setUserData(oPlat);
        arrayPlatforms.add(oPlat);

        shape.dispose();

    }

    /**
     * The breakable platform is 2 squares.
     */
    private void createPiecesOfPlatforms(Platform oPlat) {
        createPiecesOfPlatforms(oPlat, PlatformPiece.TYPE_LEFT);
        createPiecesOfPlatforms(oPlat, PlatformPiece.TYPE_RIGHT);

    }

    private void createPiecesOfPlatforms(Platform oPla, int type) {
        PlatformPiece piece;
        float x;
        float angularVelocity = 100;

        if (type == PlatformPiece.TYPE_LEFT) {
            x = oPla.position.x - PlatformPiece.WIDTH_NORMAL / 2f;
            angularVelocity *= -1;
        } else {
            x = oPla.position.x + PlatformPiece.WIDTH_NORMAL / 2f;
        }

        piece = Pools.obtain(PlatformPiece.class);
        piece.initializePlatformPiece(x, oPla.position.y, type, oPla.color);

        BodyDef bd = new BodyDef();
        bd.position.set(piece.position.x, piece.position.y);
        bd.type = BodyType.DynamicBody;

        Body body = myWorldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(PlatformPiece.WIDTH_NORMAL / 2f, PlatformPiece.HEIGHT_NORMAL / 2f);

        FixtureDef fixutre = new FixtureDef();
        fixutre.shape = shape;
        fixutre.isSensor = true;

        body.createFixture(fixutre);
        body.setUserData(piece);
        body.setAngularVelocity(MathUtils.degRad * angularVelocity);
        arrayPlatformPieces.add(piece);

        shape.dispose();
    }

    private void crearEnemigo(float y) {
        Enemy oEn = Pools.obtain(Enemy.class);
        oEn.initializeEnemy(MathUtils.random(Screens.WORLD_WIDTH), y);

        BodyDef bd = new BodyDef();
        bd.position.set(oEn.position.x, oEn.position.y);
        bd.type = BodyType.DynamicBody;

        Body body = myWorldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Enemy.WIDTH / 2f, Enemy.HEIGHT / 2f);

        FixtureDef fixutre = new FixtureDef();
        fixutre.shape = shape;
        fixutre.isSensor = true;

        body.createFixture(fixutre);
        body.setUserData(oEn);
        body.setGravityScale(0);

        float velocidad = MathUtils.random(1f, Enemy.SPEED_X);

        if (MathUtils.randomBoolean())
            body.setLinearVelocity(velocidad, 0);
        else
            body.setLinearVelocity(-velocidad, 0);
        arrayEnemies.add(oEn);

        shape.dispose();
    }

    private void createItem(float y) {
        Item oItem = Pools.obtain(Item.class);
        oItem.initializeItem(MathUtils.random(Screens.WORLD_WIDTH), y);

        BodyDef bd = new BodyDef();
        bd.position.set(oItem.position.x, oItem.position.y);
        bd.type = BodyType.StaticBody;
        Body oBody = myWorldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Item.WIDTH / 2f, Item.HEIGHT / 2f);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.isSensor = true;
        oBody.createFixture(fixture);
        oBody.setUserData(oItem);
        shape.dispose();
        arrayItem.add(oItem);
    }

    private void crearNubes(float y) {
        com.nopalsoft.superjumper.objects.Cloud cloud = Pools.obtain(com.nopalsoft.superjumper.objects.Cloud.class);
        cloud.init(MathUtils.random(Screens.WORLD_WIDTH), y);

        BodyDef bd = new BodyDef();
        bd.position.set(cloud.position.x, cloud.position.y);
        bd.type = BodyType.DynamicBody;

        Body body = myWorldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(com.nopalsoft.superjumper.objects.Cloud.WIDTH / 2f, com.nopalsoft.superjumper.objects.Cloud.HEIGHT / 2f);

        FixtureDef fixutre = new FixtureDef();
        fixutre.shape = shape;
        fixutre.isSensor = true;

        body.createFixture(fixutre);
        body.setUserData(cloud);
        body.setGravityScale(0);

        float velocidad = MathUtils.random(1f, com.nopalsoft.superjumper.objects.Cloud.VELOCIDAD_X);

        if (MathUtils.randomBoolean())
            body.setLinearVelocity(velocidad, 0);
        else
            body.setLinearVelocity(-velocidad, 0);
        arrayClouds.add(cloud);

        shape.dispose();
    }

    private void crearRayo(float x, float y) {
        com.nopalsoft.superjumper.objects.Ray ray = Pools.obtain(com.nopalsoft.superjumper.objects.Ray.class);
        ray.initializeRay(x, y);

        BodyDef bd = new BodyDef();
        bd.position.set(ray.position.x, ray.position.y);
        bd.type = BodyType.KinematicBody;

        Body body = myWorldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(com.nopalsoft.superjumper.objects.Ray.WIDTH / 2f, com.nopalsoft.superjumper.objects.Ray.HEIGHT / 2f);

        FixtureDef fixutre = new FixtureDef();
        fixutre.shape = shape;
        fixutre.isSensor = true;

        body.createFixture(fixutre);
        body.setUserData(ray);

        body.setLinearVelocity(0, com.nopalsoft.superjumper.objects.Ray.Y_SPEED);
        arrayRays.add(ray);

        shape.dispose();
    }

    private void crearBullet(float origenX, float origenY, float destinoX, float destinoY) {
        Bullet oBullet = Pools.obtain(Bullet.class);
        oBullet.initializeBullet(origenX, origenY);

        BodyDef bd = new BodyDef();
        bd.position.set(oBullet.position.x, oBullet.position.y);
        bd.type = BodyType.KinematicBody;

        Body body = myWorldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Bullet.SIZE / 2f, Bullet.SIZE / 2f);

        FixtureDef fixutre = new FixtureDef();
        fixutre.shape = shape;
        fixutre.isSensor = true;

        body.createFixture(fixutre);
        body.setUserData(oBullet);
        body.setBullet(true);

        Vector2 destino = new Vector2(destinoX, destinoY);
        destino.sub(oBullet.position).nor().scl(Bullet.XY_SPEED);

        body.setLinearVelocity(destino.x, destino.y);

        arrayBullets.add(oBullet);

        shape.dispose();
    }

    public void update(float delta, float acelX, boolean fire, Vector3 touchPositionWorldCoords) {
        myWorldBox.step(delta, 8, 4);

        eliminarObjetos();

        if (player.position.y + 10 > worldCreatedUpToY) {
            createNextPart();
        }

        timeToCreateCloud += delta;// Actualizo el tiempo para crear una nube

        myWorldBox.getBodies(arrayBodies);

        for (Body body : arrayBodies) {
            if (body.getUserData() instanceof Player) {
                updatePersonaje(body, delta, acelX, fire, touchPositionWorldCoords);
            } else if (body.getUserData() instanceof Platform) {
                updatePlataforma(body, delta);
            } else if (body.getUserData() instanceof PlatformPiece) {
                updatePiezaPlataforma(body, delta);
            } else if (body.getUserData() instanceof Coin) {
                updateMoneda(body, delta);
            } else if (body.getUserData() instanceof Enemy) {
                updateEnemigo(body, delta);
            } else if (body.getUserData() instanceof Item) {
                updateItem(body, delta);
            } else if (body.getUserData() instanceof Cloud) {
                updateNube(body, delta);
            } else if (body.getUserData() instanceof Ray) {
                updateRayo(body, delta);
            } else if (body.getUserData() instanceof Bullet) {
                updateBullet(body, delta);
            }

        }

        if (maxDistance < (player.position.y * 10)) {
            maxDistance = (int) (player.position.y * 10);
        }

        // Si el personaje esta 5.5f mas abajo de la altura maxima se muere (Se multiplica por 10 porque la distancia se multiplica por 10 )
        if (player.state == Player.STATE_NORMAL && maxDistance - (5.5f * 10) > (player.position.y * 10)) {
            player.die();
        }
        if (player.state == Player.STATE_DEAD && maxDistance - (25 * 10) > (player.position.y * 10)) {
            state = STATE_GAMEOVER;
        }

    }

    private void eliminarObjetos() {
        myWorldBox.getBodies(arrayBodies);

        for (Body body : arrayBodies) {
            if (!myWorldBox.isLocked()) {

                if (body.getUserData() instanceof Platform) {
                    Platform oPlat = (Platform) body.getUserData();
                    if (oPlat.state == Platform.STATE_DESTROY) {
                        arrayPlatforms.removeValue(oPlat, true);
                        myWorldBox.destroyBody(body);
                        if (oPlat.type == Platform.TYPE_BREAKABLE)
                            createPiecesOfPlatforms(oPlat);
                        com.badlogic.gdx.utils.Pools.free(oPlat);
                    }
                } else if (body.getUserData() instanceof Coin) {
                    Coin oMon = (Coin) body.getUserData();
                    if (oMon.state == Coin.STATE_TAKEN) {
                        arrayCoins.removeValue(oMon, true);
                        myWorldBox.destroyBody(body);
                        com.badlogic.gdx.utils.Pools.free(oMon);
                    }
                } else if (body.getUserData() instanceof PlatformPiece) {
                    PlatformPiece oPiez = (PlatformPiece) body.getUserData();
                    if (oPiez.state == PlatformPiece.STATE_DESTROY) {
                        arrayPlatformPieces.removeValue(oPiez, true);
                        myWorldBox.destroyBody(body);
                        com.badlogic.gdx.utils.Pools.free(oPiez);
                    }
                } else if (body.getUserData() instanceof Enemy) {
                    Enemy oEnemy = (Enemy) body.getUserData();
                    if (oEnemy.state == Enemy.STATE_DEAD) {
                        arrayEnemies.removeValue(oEnemy, true);
                        myWorldBox.destroyBody(body);
                        Pools.free(oEnemy);
                    }
                } else if (body.getUserData() instanceof Item) {
                    Item oItem = (Item) body.getUserData();
                    if (oItem.state == Item.STATE_TAKEN) {
                        arrayItem.removeValue(oItem, true);
                        myWorldBox.destroyBody(body);
                        Pools.free(oItem);
                    }
                } else if (body.getUserData() instanceof Cloud) {
                    Cloud cloud = (Cloud) body.getUserData();
                    if (cloud.state == Cloud.STATE_DEAD) {
                        arrayClouds.removeValue(cloud, true);
                        myWorldBox.destroyBody(body);
                        Pools.free(cloud);
                    }
                } else if (body.getUserData() instanceof Ray) {
                    Ray ray = (Ray) body.getUserData();
                    if (ray.state == Ray.STATE_DESTROY) {
                        arrayRays.removeValue(ray, true);
                        myWorldBox.destroyBody(body);
                        Pools.free(ray);
                    }
                } else if (body.getUserData() instanceof Bullet) {
                    Bullet oBullet = (Bullet) body.getUserData();
                    if (oBullet.getState() == Bullet.STATE_DESTROY) {
                        arrayBullets.removeValue(oBullet, true);
                        myWorldBox.destroyBody(body);
                        Pools.free(oBullet);
                    }
                } else if (body.getUserData().equals("piso")) {
                    if (player.position.y - 5.5f > body.getPosition().y || player.state == Player.STATE_DEAD) {
                        myWorldBox.destroyBody(body);
                    }
                }
            }
        }
    }

    private void updatePersonaje(Body body, float delta, float acelX, boolean fire, Vector3 touchPositionWorldCoords) {
        player.update(body, delta, acelX);

        if (Settings.numBullets > 0 && fire) {
            crearBullet(player.position.x, player.position.y, touchPositionWorldCoords.x, touchPositionWorldCoords.y);
            Settings.numBullets--;

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

    private void updateEnemigo(Body body, float delta) {
        Enemy obj = (Enemy) body.getUserData();
        obj.update(body, delta);
        if (player.position.y - 5.5f > obj.position.y) {
            obj.hit();
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

        if (obj.isLighthing) {
            crearRayo(obj.position.x, obj.position.y - .65f);
            obj.fireLighting();
        }

        if (player.position.y - 5.5f > obj.position.y) {
            obj.destroy();
        }
    }

    private void updateRayo(Body body, float delta) {
        Ray obj = (Ray) body.getUserData();
        obj.update(body, delta);

        if (player.position.y - 5.5f > obj.position.y) {
            obj.destroy();
        }
    }

    private void updateBullet(Body body, float delta) {
        Bullet obj = (Bullet) body.getUserData();
        obj.update(body, delta);

        if (player.position.y - 5.5f > obj.position.y) {
            obj.destroy();
        }
    }

    class Colisiones implements ContactListener {

        @Override
        public void beginContact(Contact contact) {
            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();

            if (a.getBody().getUserData() instanceof Player)
                beginContactPersonaje(b);
            else if (b.getBody().getUserData() instanceof Player)
                beginContactPersonaje(a);

            if (a.getBody().getUserData() instanceof Bullet)
                beginContactBullet(a, b);
            else if (b.getBody().getUserData() instanceof Bullet)
                beginContactBullet(b, a);

        }

        private void beginContactPersonaje(Fixture fixOtraCosa) {
            Object otraCosa = fixOtraCosa.getBody().getUserData();

            if (otraCosa.equals("piso")) {
                player.jump();

                if (player.state == Player.STATE_DEAD) {
                    state = STATE_GAMEOVER;
                }
            } else if (otraCosa instanceof Platform) {
                Platform obj = (Platform) otraCosa;

                if (player.speed.y <= 0) {
                    player.jump();
                    if (obj.type == Platform.TYPE_BREAKABLE) {
                        obj.setDestroy();
                    }
                }

            } else if (otraCosa instanceof Coin) {
                Coin obj = (Coin) otraCosa;
                obj.take();
                coins++;
                player.jump();
            } else if (otraCosa instanceof Enemy) {
                player.hit();
            } else if (otraCosa instanceof Ray) {
                player.hit();
            } else if (otraCosa instanceof Item) {
                Item obj = (Item) otraCosa;
                obj.take();

                switch (obj.type) {
                    case Item.TYPE_BUBBLE:
                        player.setBubble();
                        break;
                    case Item.TYPE_JETPACK:
                        player.setJetPack();
                        break;
                    case Item.TYPE_GUN:
                        Settings.numBullets += 10;
                        break;

                }

            }

        }

        private void beginContactBullet(Fixture fixBullet, Fixture fixOtraCosa) {
            Object otraCosa = fixOtraCosa.getBody().getUserData();
            Bullet oBullet = (Bullet) fixBullet.getBody().getUserData();

            if (otraCosa instanceof Enemy) {
                Enemy obj = (Enemy) otraCosa;
                obj.hit();
                oBullet.destroy();

            } else if (otraCosa instanceof Cloud) {
                Cloud obj = (Cloud) otraCosa;
                obj.hit();
                oBullet.destroy();

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
            // TODO Auto-generated method stub

        }

    }

}
