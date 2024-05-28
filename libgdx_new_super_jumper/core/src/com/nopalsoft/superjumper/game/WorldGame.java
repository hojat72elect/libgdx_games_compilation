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
import com.nopalsoft.superjumper.objects.Character;
import com.nopalsoft.superjumper.objects.Cloud;
import com.nopalsoft.superjumper.objects.Coin;
import com.nopalsoft.superjumper.objects.Enemy;
import com.nopalsoft.superjumper.objects.Item;
import com.nopalsoft.superjumper.objects.Platform;
import com.nopalsoft.superjumper.objects.PlatformPiece;
import com.nopalsoft.superjumper.objects.Ray;
import com.nopalsoft.superjumper.screens.Screens;

public class WorldGame {


    final public static int STATE_RUNNING = 0;
    final public static int STATE_GAMEOVER = 1;
    private final Array<Body> arrBodies;
    public World oWorldBox;
    public int coins;
    public int distanciaMax;
    int state;
    float TIME_TO_CREATE_NUBE = 15;
    float timeToCreateNube;
    Character oPer;
    Array<Platform> arrPlataformas;
    Array<PlatformPiece> arrPiezasPlataformas;
    Array<Coin> arrMonedas;
    Array<Enemy> arrEnemigo;
    Array<Item> arrItem;
    Array<Cloud> arrNubes;
    Array<Ray> arrRayos;
    Array<Bullet> arrBullets;
    float mundoCreadoHastaY;

    public WorldGame() {
        oWorldBox = new World(new Vector2(0, -9.8f), true);
        oWorldBox.setContactListener(new Colisiones());

        arrBodies = new Array<>();
        arrPlataformas = new Array<>();
        arrPiezasPlataformas = new Array<>();
        arrMonedas = new Array<>();
        arrEnemigo = new Array<>();
        arrItem = new Array<>();
        arrNubes = new Array<>();
        arrRayos = new Array<>();
        arrBullets = new Array<>();

        timeToCreateNube = 0;

        state = STATE_RUNNING;

        crearPiso();
        crearPersonaje();

        mundoCreadoHastaY = oPer.position.y;
        createNextPart();

    }

    private void createNextPart() {
        float y = mundoCreadoHastaY + 2;

        for (int i = 0; mundoCreadoHastaY < (y + 10); i++) {
            mundoCreadoHastaY = y + (i * 2);

            createPlatform(mundoCreadoHastaY);
            createPlatform(mundoCreadoHastaY);

            if (MathUtils.random(100) < 5)
                com.nopalsoft.superjumper.objects.Coin.createCoins(oWorldBox, arrMonedas, mundoCreadoHastaY);

            if (MathUtils.random(20) < 5)
                com.nopalsoft.superjumper.objects.Coin.createUnaMoneda(oWorldBox, arrMonedas, mundoCreadoHastaY + .5f);

            if (MathUtils.random(20) < 5)
                crearEnemigo(mundoCreadoHastaY + .5f);

            if (timeToCreateNube >= TIME_TO_CREATE_NUBE) {
                crearNubes(mundoCreadoHastaY + .7f);
                timeToCreateNube = 0;
            }

            if (MathUtils.random(50) < 5)
                createItem(mundoCreadoHastaY + .5f);
        }

    }

    /**
     * El piso solo aparece 1 vez, al principio del juego
     */
    private void crearPiso() {
        BodyDef bd = new BodyDef();
        bd.type = BodyType.StaticBody;

        Body body = oWorldBox.createBody(bd);

        EdgeShape shape = new EdgeShape();
        shape.set(0, 0, Screens.WORLD_WIDTH, 0);

        FixtureDef fixutre = new FixtureDef();
        fixutre.shape = shape;

        body.createFixture(fixutre);
        body.setUserData("piso");

        shape.dispose();

    }

    private void crearPersonaje() {
        oPer = new com.nopalsoft.superjumper.objects.Character(2.4f, .5f);

        BodyDef bd = new BodyDef();
        bd.position.set(oPer.position.x, oPer.position.y);
        bd.type = BodyType.DynamicBody;

        Body body = oWorldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(com.nopalsoft.superjumper.objects.Character.WIDTH / 2f, com.nopalsoft.superjumper.objects.Character.HEIGTH / 2f);

        FixtureDef fixutre = new FixtureDef();
        fixutre.shape = shape;
        fixutre.density = 10;
        fixutre.friction = 0;
        fixutre.restitution = 0;

        body.createFixture(fixutre);
        body.setUserData(oPer);
        body.setFixedRotation(true);

        shape.dispose();
    }

    private void createPlatform(float y) {

        com.nopalsoft.superjumper.objects.Platform oPlat = Pools.obtain(com.nopalsoft.superjumper.objects.Platform.class);
        oPlat.init(MathUtils.random(Screens.WORLD_WIDTH), y, MathUtils.random(1));

        BodyDef bd = new BodyDef();
        bd.position.set(oPlat.position.x, oPlat.position.y);
        bd.type = BodyType.KinematicBody;

        Body body = oWorldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(com.nopalsoft.superjumper.objects.Platform.WIDTH_NORMAL / 2f, com.nopalsoft.superjumper.objects.Platform.HEIGHT_NORMAL / 2f);

        FixtureDef fixutre = new FixtureDef();
        fixutre.shape = shape;

        body.createFixture(fixutre);
        body.setUserData(oPlat);
        arrPlataformas.add(oPlat);

        shape.dispose();

    }

    /**
     * The breakable platform is 2 squares.
     */
    private void createPiecesOfPlatforms(com.nopalsoft.superjumper.objects.Platform oPlat) {
        createPiecesOfPlatforms(oPlat, com.nopalsoft.superjumper.objects.PlatformPiece.TYPE_LEFT);
        createPiecesOfPlatforms(oPlat, PlatformPiece.TYPE_RIGHT);

    }

    private void createPiecesOfPlatforms(com.nopalsoft.superjumper.objects.Platform oPla, int type) {
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
        piece.init(x, oPla.position.y, type, oPla.color);

        BodyDef bd = new BodyDef();
        bd.position.set(piece.position.x, piece.position.y);
        bd.type = BodyType.DynamicBody;

        Body body = oWorldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(PlatformPiece.WIDTH_NORMAL / 2f, PlatformPiece.HEIGHT_NORMAL / 2f);

        FixtureDef fixutre = new FixtureDef();
        fixutre.shape = shape;
        fixutre.isSensor = true;

        body.createFixture(fixutre);
        body.setUserData(piece);
        body.setAngularVelocity(MathUtils.degRad * angularVelocity);
        arrPiezasPlataformas.add(piece);

        shape.dispose();
    }

    private void crearEnemigo(float y) {
        com.nopalsoft.superjumper.objects.Enemy oEn = Pools.obtain(com.nopalsoft.superjumper.objects.Enemy.class);
        oEn.init(MathUtils.random(Screens.WORLD_WIDTH), y);

        BodyDef bd = new BodyDef();
        bd.position.set(oEn.position.x, oEn.position.y);
        bd.type = BodyType.DynamicBody;

        Body body = oWorldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(com.nopalsoft.superjumper.objects.Enemy.WIDTH / 2f, com.nopalsoft.superjumper.objects.Enemy.HEIGHT / 2f);

        FixtureDef fixutre = new FixtureDef();
        fixutre.shape = shape;
        fixutre.isSensor = true;

        body.createFixture(fixutre);
        body.setUserData(oEn);
        body.setGravityScale(0);

        float velocidad = MathUtils.random(1f, com.nopalsoft.superjumper.objects.Enemy.VELOCIDAD_X);

        if (MathUtils.randomBoolean())
            body.setLinearVelocity(velocidad, 0);
        else
            body.setLinearVelocity(-velocidad, 0);
        arrEnemigo.add(oEn);

        shape.dispose();
    }

    private void createItem(float y) {
        Item oItem = Pools.obtain(Item.class);
        oItem.initializeItem(MathUtils.random(Screens.WORLD_WIDTH), y);

        BodyDef bd = new BodyDef();
        bd.position.set(oItem.position.x, oItem.position.y);
        bd.type = BodyType.StaticBody;
        Body oBody = oWorldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Item.WIDTH / 2f, Item.HEIGHT / 2f);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.isSensor = true;
        oBody.createFixture(fixture);
        oBody.setUserData(oItem);
        shape.dispose();
        arrItem.add(oItem);
    }

    private void crearNubes(float y) {
        com.nopalsoft.superjumper.objects.Cloud cloud = Pools.obtain(com.nopalsoft.superjumper.objects.Cloud.class);
        cloud.init(MathUtils.random(Screens.WORLD_WIDTH), y);

        BodyDef bd = new BodyDef();
        bd.position.set(cloud.position.x, cloud.position.y);
        bd.type = BodyType.DynamicBody;

        Body body = oWorldBox.createBody(bd);

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
        arrNubes.add(cloud);

        shape.dispose();
    }

    private void crearRayo(float x, float y) {
        com.nopalsoft.superjumper.objects.Ray ray = Pools.obtain(com.nopalsoft.superjumper.objects.Ray.class);
        ray.initializeRay(x, y);

        BodyDef bd = new BodyDef();
        bd.position.set(ray.position.x, ray.position.y);
        bd.type = BodyType.KinematicBody;

        Body body = oWorldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(com.nopalsoft.superjumper.objects.Ray.WIDTH / 2f, com.nopalsoft.superjumper.objects.Ray.HEIGHT / 2f);

        FixtureDef fixutre = new FixtureDef();
        fixutre.shape = shape;
        fixutre.isSensor = true;

        body.createFixture(fixutre);
        body.setUserData(ray);

        body.setLinearVelocity(0, com.nopalsoft.superjumper.objects.Ray.Y_SPEED);
        arrRayos.add(ray);

        shape.dispose();
    }

    private void crearBullet(float origenX, float origenY, float destinoX, float destinoY) {
        Bullet oBullet = Pools.obtain(Bullet.class);
        oBullet.initializeBullet(origenX, origenY);

        BodyDef bd = new BodyDef();
        bd.position.set(oBullet.position.x, oBullet.position.y);
        bd.type = BodyType.KinematicBody;

        Body body = oWorldBox.createBody(bd);

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

        arrBullets.add(oBullet);

        shape.dispose();
    }

    public void update(float delta, float acelX, boolean fire, Vector3 touchPositionWorldCoords) {
        oWorldBox.step(delta, 8, 4);

        eliminarObjetos();

        if (oPer.position.y + 10 > mundoCreadoHastaY) {
            createNextPart();
        }

        timeToCreateNube += delta;// Actualizo el tiempo para crear una nube

        oWorldBox.getBodies(arrBodies);

        for (com.badlogic.gdx.physics.box2d.Body body : arrBodies) {
            if (body.getUserData() instanceof com.nopalsoft.superjumper.objects.Character) {
                updatePersonaje(body, delta, acelX, fire, touchPositionWorldCoords);
            } else if (body.getUserData() instanceof com.nopalsoft.superjumper.objects.Platform) {
                updatePlataforma(body, delta);
            } else if (body.getUserData() instanceof PlatformPiece) {
                updatePiezaPlataforma(body, delta);
            } else if (body.getUserData() instanceof com.nopalsoft.superjumper.objects.Coin) {
                updateMoneda(body, delta);
            } else if (body.getUserData() instanceof com.nopalsoft.superjumper.objects.Enemy) {
                updateEnemigo(body, delta);
            } else if (body.getUserData() instanceof com.nopalsoft.superjumper.objects.Item) {
                updateItem(body, delta);
            } else if (body.getUserData() instanceof com.nopalsoft.superjumper.objects.Cloud) {
                updateNube(body, delta);
            } else if (body.getUserData() instanceof com.nopalsoft.superjumper.objects.Ray) {
                updateRayo(body, delta);
            } else if (body.getUserData() instanceof com.nopalsoft.superjumper.objects.Bullet) {
                updateBullet(body, delta);
            }

        }

        if (distanciaMax < (oPer.position.y * 10)) {
            distanciaMax = (int) (oPer.position.y * 10);
        }

        // Si el personaje esta 5.5f mas abajo de la altura maxima se muere (Se multiplica por 10 porque la distancia se multiplica por 10 )
        if (oPer.state == com.nopalsoft.superjumper.objects.Character.STATE_NORMAL && distanciaMax - (5.5f * 10) > (oPer.position.y * 10)) {
            oPer.die();
        }
        if (oPer.state == com.nopalsoft.superjumper.objects.Character.STATE_DEAD && distanciaMax - (25 * 10) > (oPer.position.y * 10)) {
            state = STATE_GAMEOVER;
        }

    }

    private void eliminarObjetos() {
        oWorldBox.getBodies(arrBodies);

        for (com.badlogic.gdx.physics.box2d.Body body : arrBodies) {
            if (!oWorldBox.isLocked()) {

                if (body.getUserData() instanceof com.nopalsoft.superjumper.objects.Platform) {
                    com.nopalsoft.superjumper.objects.Platform oPlat = (com.nopalsoft.superjumper.objects.Platform) body.getUserData();
                    if (oPlat.state == com.nopalsoft.superjumper.objects.Platform.STATE_DESTROY) {
                        arrPlataformas.removeValue(oPlat, true);
                        oWorldBox.destroyBody(body);
                        if (oPlat.type == com.nopalsoft.superjumper.objects.Platform.TYPE_BREAKABLE)
                            createPiecesOfPlatforms(oPlat);
                        com.badlogic.gdx.utils.Pools.free(oPlat);
                    }
                } else if (body.getUserData() instanceof com.nopalsoft.superjumper.objects.Coin) {
                    com.nopalsoft.superjumper.objects.Coin oMon = (com.nopalsoft.superjumper.objects.Coin) body.getUserData();
                    if (oMon.state == com.nopalsoft.superjumper.objects.Coin.STATE_TAKEN) {
                        arrMonedas.removeValue(oMon, true);
                        oWorldBox.destroyBody(body);
                        com.badlogic.gdx.utils.Pools.free(oMon);
                    }
                } else if (body.getUserData() instanceof com.nopalsoft.superjumper.objects.PlatformPiece) {
                    com.nopalsoft.superjumper.objects.PlatformPiece oPiez = (com.nopalsoft.superjumper.objects.PlatformPiece) body.getUserData();
                    if (oPiez.state == com.nopalsoft.superjumper.objects.PlatformPiece.STATE_DESTROY) {
                        arrPiezasPlataformas.removeValue(oPiez, true);
                        oWorldBox.destroyBody(body);
                        com.badlogic.gdx.utils.Pools.free(oPiez);
                    }
                } else if (body.getUserData() instanceof com.nopalsoft.superjumper.objects.Enemy) {
                    com.nopalsoft.superjumper.objects.Enemy oEnemy = (com.nopalsoft.superjumper.objects.Enemy) body.getUserData();
                    if (oEnemy.state == com.nopalsoft.superjumper.objects.Enemy.STATE_DEAD) {
                        arrEnemigo.removeValue(oEnemy, true);
                        oWorldBox.destroyBody(body);
                        Pools.free(oEnemy);
                    }
                } else if (body.getUserData() instanceof Item) {
                    Item oItem = (Item) body.getUserData();
                    if (oItem.state == Item.STATE_TAKEN) {
                        arrItem.removeValue(oItem, true);
                        oWorldBox.destroyBody(body);
                        Pools.free(oItem);
                    }
                } else if (body.getUserData() instanceof Cloud) {
                    Cloud cloud = (Cloud) body.getUserData();
                    if (cloud.state == Cloud.STATE_DEAD) {
                        arrNubes.removeValue(cloud, true);
                        oWorldBox.destroyBody(body);
                        Pools.free(cloud);
                    }
                } else if (body.getUserData() instanceof Ray) {
                    Ray ray = (Ray) body.getUserData();
                    if (ray.state == Ray.STATE_DESTROY) {
                        arrRayos.removeValue(ray, true);
                        oWorldBox.destroyBody(body);
                        Pools.free(ray);
                    }
                } else if (body.getUserData() instanceof Bullet) {
                    Bullet oBullet = (Bullet) body.getUserData();
                    if (oBullet.getState() == Bullet.STATE_DESTROY) {
                        arrBullets.removeValue(oBullet, true);
                        oWorldBox.destroyBody(body);
                        Pools.free(oBullet);
                    }
                } else if (body.getUserData().equals("piso")) {
                    if (oPer.position.y - 5.5f > body.getPosition().y || oPer.state == Character.STATE_DEAD) {
                        oWorldBox.destroyBody(body);
                    }
                }
            }
        }
    }

    private void updatePersonaje(Body body, float delta, float acelX, boolean fire, Vector3 touchPositionWorldCoords) {
        oPer.update(body, delta, acelX);

        if (Settings.numBullets > 0 && fire) {
            crearBullet(oPer.position.x, oPer.position.y, touchPositionWorldCoords.x, touchPositionWorldCoords.y);
            Settings.numBullets--;

        }

    }

    private void updatePlataforma(Body body, float delta) {
        com.nopalsoft.superjumper.objects.Platform obj = (com.nopalsoft.superjumper.objects.Platform) body.getUserData();
        obj.update(delta);
        if (oPer.position.y - 5.5f > obj.position.y) {
            obj.setDestroy();
        }
    }

    private void updatePiezaPlataforma(Body body, float delta) {
        PlatformPiece obj = (PlatformPiece) body.getUserData();
        obj.update(delta, body);
        if (oPer.position.y - 5.5f > obj.position.y) {
            obj.setDestroy();
        }

    }

    private void updateMoneda(Body body, float delta) {
        Coin obj = (Coin) body.getUserData();
        obj.update(delta);
        if (oPer.position.y - 5.5f > obj.position.y) {
            obj.take();
        }

    }

    private void updateEnemigo(Body body, float delta) {
        Enemy obj = (Enemy) body.getUserData();
        obj.update(body, delta);
        if (oPer.position.y - 5.5f > obj.position.y) {
            obj.hit();
        }

    }

    private void updateItem(Body body, float delta) {
        Item obj = (Item) body.getUserData();
        obj.update(delta);
        if (oPer.position.y - 5.5f > obj.position.y) {
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

        if (oPer.position.y - 5.5f > obj.position.y) {
            obj.destroy();
        }
    }

    private void updateRayo(Body body, float delta) {
        Ray obj = (Ray) body.getUserData();
        obj.update(body, delta);

        if (oPer.position.y - 5.5f > obj.position.y) {
            obj.destroy();
        }
    }

    private void updateBullet(Body body, float delta) {
        Bullet obj = (Bullet) body.getUserData();
        obj.update(body, delta);

        if (oPer.position.y - 5.5f > obj.position.y) {
            obj.destroy();
        }
    }

    class Colisiones implements ContactListener {

        @Override
        public void beginContact(Contact contact) {
            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();

            if (a.getBody().getUserData() instanceof Character)
                beginContactPersonaje(b);
            else if (b.getBody().getUserData() instanceof Character)
                beginContactPersonaje(a);

            if (a.getBody().getUserData() instanceof Bullet)
                beginContactBullet(a, b);
            else if (b.getBody().getUserData() instanceof Bullet)
                beginContactBullet(b, a);

        }

        private void beginContactPersonaje(Fixture fixOtraCosa) {
            Object otraCosa = fixOtraCosa.getBody().getUserData();

            if (otraCosa.equals("piso")) {
                oPer.jump();

                if (oPer.state == Character.STATE_DEAD) {
                    state = STATE_GAMEOVER;
                }
            } else if (otraCosa instanceof Platform) {
                Platform obj = (Platform) otraCosa;

                if (oPer.speed.y <= 0) {
                    oPer.jump();
                    if (obj.type == Platform.TYPE_BREAKABLE) {
                        obj.setDestroy();
                    }
                }

            } else if (otraCosa instanceof Coin) {
                Coin obj = (Coin) otraCosa;
                obj.take();
                coins++;
                oPer.jump();
            } else if (otraCosa instanceof Enemy) {
                oPer.hit();
            } else if (otraCosa instanceof Ray) {
                oPer.hit();
            } else if (otraCosa instanceof Item) {
                Item obj = (Item) otraCosa;
                obj.take();

                switch (obj.type) {
                    case Item.TYPE_BUBBLE:
                        oPer.setBubble();
                        break;
                    case Item.TYPE_JETPACK:
                        oPer.setJetPack();
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

            if (a.getBody().getUserData() instanceof Character)
                preSolveHero(a, b, contact);
            else if (b.getBody().getUserData() instanceof Character)
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

                if (obj.type == Platform.TYPE_NORMAL && oPer.state == Character.STATE_DEAD) {
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
