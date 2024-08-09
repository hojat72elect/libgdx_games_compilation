package com.nopalsoft.slamthebird.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
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
import com.badlogic.gdx.utils.Pool;
import com.nopalsoft.slamthebird.Achievements;
import com.nopalsoft.slamthebird.Assets;
import com.nopalsoft.slamthebird.Settings;
import com.nopalsoft.slamthebird.objects.Boost;
import com.nopalsoft.slamthebird.objects.Coin;
import com.nopalsoft.slamthebird.objects.Robot;
import com.nopalsoft.slamthebird.screens.Screens;
import com.nopalsoft.slamthebird.objects.Platform;
import com.nopalsoft.slamthebird.objects.Enemy;
import java.util.Random;

public class WorldGame {
    public static final float COMBO_TO_START_GETTING_COINS = 3;
    final public static int STATE_RUNNING = 0;
    final public static int STATE_GAME_OVER = 1;
    final public float TIME_TO_SPAWN_ENEMY = 7;
    final public float TIME_TO_CHANGE_STATE_PLATFORM = 16f; // Este tiempo debe ser mayor que DURATION_ACTIVE en la clase plataformas.
    final public float TIME_TO_SPAWN_COIN = .75f;
    final float WIDTH = Screens.WORLD_SCREEN_WIDTH;
    private final Pool<Boost> boostPool = new Pool<Boost>() {
        @Override
        protected Boost newObject() {
            return new Boost();
        }
    };
    private final Pool<Coin> coinPool = new Pool<Coin>() {
        @Override
        protected Coin newObject() {
            return new Coin();
        }
    };
    public float timeToSpawnEnemy;
    public float TIME_TO_SPAWN_BOOST = 15f;
    public float timeToSpawnBoost;
    public float timeToChangeStatePlatform;
    public float timeToSpawnCoin;
    public World oWorldBox;
    int state;
    Robot robot;
    Array<Platform> arrayPlatforms;
    Array<Enemy> arrayEnemies;
    Array<Body> arrayBodies;
    Array<Boost> arrayBoost;
    Array<Coin> arrayCoins;
    Random random;
    int scoreSlammed;
    int takenCoins;
    int combo;
    boolean isCoinRain;

    public WorldGame() {
        oWorldBox = new World(new Vector2(0, -9.8f), true);
        oWorldBox.setContactListener(new Colisiones());

        state = STATE_RUNNING;
        arrayBodies = new Array<>();
        arrayEnemies = new Array<>();
        arrayPlatforms = new Array<>();
        arrayBoost = new Array<>();
        arrayCoins = new Array<>();

        random = new Random();

        timeToSpawnEnemy = 5;
        isCoinRain = false;

        takenCoins = scoreSlammed = 0;

        float posPiso = .6f;
        crearParedes(posPiso);// .05
        crearRobot(WIDTH / 2f, posPiso + .251f);

        crearPlataformas(0 + Platform.WIDTH / 2f, 1.8f + posPiso);// Izq Abajo
        crearPlataformas(WIDTH - Platform.WIDTH / 2f + .1f, 1.8f + posPiso);// Derecha abajo

        crearPlataformas(0 + Platform.WIDTH / 2f, 1.8f * 2f + posPiso);// Izq Arriba
        crearPlataformas(WIDTH - Platform.WIDTH / 2f + .1f,
                1.8f * 2f + posPiso);// Derecha Arribadd

        // Boost stuff
        TIME_TO_SPAWN_BOOST -= Settings.LEVEL_BOOST_TIME;
    }

    private void crearParedes(float posPisoY) {
        BodyDef bd = new BodyDef();
        bd.position.x = 0;
        bd.position.y = 0;
        bd.type = BodyType.StaticBody;
        Body oBody = oWorldBox.createBody(bd);

        ChainShape shape = new ChainShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(0.005f, 50);
        vertices[1] = new Vector2(0.005f, 0);
        vertices[2] = new Vector2(WIDTH, 0);
        vertices[3] = new Vector2(WIDTH, 50);
        shape.createChain(vertices);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.restitution = 0;
        fixture.friction = 0;

        oBody.createFixture(fixture);
        oBody.setUserData("pared");
        shape.dispose();

        // Piso
        EdgeShape shapePiso = new EdgeShape();
        shapePiso.set(0, 0, WIDTH, 0);
        bd.position.y = posPisoY;
        Body oBodyPiso = oWorldBox.createBody(bd);

        fixture.shape = shapePiso;
        oBodyPiso.createFixture(fixture);
        oBodyPiso.setUserData("piso");

        shapePiso.dispose();

    }

    private void crearRobot(float x, float y) {
        robot = new Robot(x, y);
        BodyDef bd = new BodyDef();
        bd.position.x = x;
        bd.position.y = y;
        bd.type = BodyType.DynamicBody;

        Body oBody = oWorldBox.createBody(bd);

        CircleShape shape = new CircleShape();
        shape.setRadius(Robot.RADIUS);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.density = 5;
        fixture.restitution = 0;
        fixture.friction = 0;
        oBody.createFixture(fixture);

        oBody.setFixedRotation(true);
        oBody.setUserData(robot);
        oBody.setBullet(true);

        shape.dispose();
    }

    private void createEnemies() {
        float x = random.nextFloat() * (WIDTH - 1) + .5f;// Para que no apareza
        float y = random.nextFloat() * 4f + .6f;

        Enemy obj = new Enemy(x, y);
        arrayEnemies.add(obj);
        BodyDef bd = new BodyDef();
        bd.position.x = x;
        bd.position.y = y;
        bd.type = BodyType.DynamicBody;

        Body oBody = oWorldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Enemy.WIDTH / 2f, Enemy.HEIGHT / 2f);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.density = 5;
        fixture.restitution = 0;
        fixture.friction = 0;
        fixture.filter.groupIndex = -1;
        oBody.createFixture(fixture);

        oBody.setFixedRotation(true);
        oBody.setGravityScale(0);
        oBody.setUserData(obj);

        shape.dispose();
    }

    private void crearPlataformas(float x, float y) {

        BodyDef bd = new BodyDef();
        bd.position.x = x;
        bd.position.y = y;
        bd.type = BodyType.StaticBody;
        Body oBody = oWorldBox.createBody(bd);

        PolygonShape shape = new PolygonShape();

        shape.setAsBox(Platform.WIDTH / 2f, Platform.HEIGHT / 2f);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.restitution = 0;
        fixture.friction = 0;
        oBody.createFixture(fixture);

        Platform obj = new Platform(bd.position.x, bd.position.y);
        oBody.setUserData(obj);
        shape.dispose();

        arrayPlatforms.add(obj);
    }

    private void crearBoost() {
        Boost obj = boostPool.obtain();

        int plat = random.nextInt(4);// arriba de que plataforma
        int tipo = random.nextInt(4);// ice, invencible,moneda,etc
        obj.init(this, arrayPlatforms.get(plat).position.x,
                arrayPlatforms.get(plat).position.y + .3f, tipo);

        arrayBoost.add(obj);

    }

    private void createCoins() {

        for (int i = 0; i < 6; i++) {
            float x = 0;
            float y = 8.4f + (i * .5f);
            float speed = Coin.SPEED_MOVE;
            if (i % 2f != 0) {
                speed *= -1;
                x = WIDTH;
            }

            Body body = Coin.createCoinBody(oWorldBox, x, y, speed);
            Coin obj = coinPool.obtain();
            obj.initializeCoin(body.getPosition().x, body.getPosition().y);
            arrayCoins.add(obj);
            body.setUserData(obj);
        }

    }

    public void updateReady(float delta, float acelX) {
        oWorldBox.step(delta, 8, 4);
        oWorldBox.getBodies(arrayBodies);
        for (Body body : arrayBodies) {
            if (body.getUserData() instanceof Robot) {
                robot.updateReady(body, acelX);
                break;
            }
        }
    }

    public void update(float delta, float acelX, boolean slam) {
        oWorldBox.step(delta, 8, 4);

        eliminarObjetos();

        timeToSpawnEnemy += delta;
        timeToSpawnBoost += delta;
        timeToChangeStatePlatform += delta;
        timeToSpawnCoin += delta;

        if (timeToSpawnEnemy >= TIME_TO_SPAWN_ENEMY) {
            timeToSpawnEnemy -= TIME_TO_SPAWN_ENEMY;
            timeToSpawnEnemy += (scoreSlammed * .025f); // Hace que aparezcan mas rapido los malos
            if (arrayEnemies.size < 7 + (scoreSlammed * .15f)) {
                if (scoreSlammed <= 15) {
                    createEnemies();
                } else if (scoreSlammed <= 50) {
                    createEnemies();
                    createEnemies();
                } else {
                    createEnemies();
                    createEnemies();
                    createEnemies();
                }
            }

        }

        if (timeToSpawnBoost >= TIME_TO_SPAWN_BOOST) {
            timeToSpawnBoost -= TIME_TO_SPAWN_BOOST;
            if (random.nextBoolean())
                crearBoost();
        }

        if (timeToSpawnCoin >= TIME_TO_SPAWN_COIN) {
            timeToSpawnCoin -= TIME_TO_SPAWN_COIN;
            createCoins();
        }

        if (timeToChangeStatePlatform >= TIME_TO_CHANGE_STATE_PLATFORM) {
            timeToChangeStatePlatform -= TIME_TO_CHANGE_STATE_PLATFORM;
            if (random.nextBoolean()) {
                int plat = random.nextInt(4);
                int state = random.nextInt(2);
                Platform obj = arrayPlatforms.get(plat);
                if (state == 0) {
                    obj.setBreakable();
                } else {
                    obj.setFire();
                }
            }
        }

        oWorldBox.getBodies(arrayBodies);

        for (Body body : arrayBodies) {
            if (body.getUserData() instanceof Robot) {
                updateRobot(delta, body, acelX, slam);
            } else if (body.getUserData() instanceof Enemy) {
                updateEnemy(delta, body);
            } else if (body.getUserData() instanceof Boost) {
                updateBoost(delta, body);
            } else if (body.getUserData() instanceof Platform) {
                updatePlataforma(delta, body);
            } else if (body.getUserData() instanceof Coin) {
                updateCoin(delta, body);
            }
        }

        isCoinRain = false;
    }

    private void eliminarObjetos() {
        oWorldBox.getBodies(arrayBodies);

        for (Body body : arrayBodies) {
            if (!oWorldBox.isLocked()) {
                if (body.getUserData() instanceof Robot) {
                    Robot obj = (Robot) body.getUserData();
                    if (obj.state == Robot.STATE_DEAD
                            && obj.stateTime >= Robot.DURATION_DEAD_ANIMATION) {
                        oWorldBox.destroyBody(body);
                        state = STATE_GAME_OVER;
                    }
                } else if (body.getUserData() instanceof Enemy) {
                    Enemy obj = (Enemy) body.getUserData();
                    if (obj.state == Enemy.STATE_DEAD) {
                        oWorldBox.destroyBody(body);
                        arrayEnemies.removeValue(obj, true);
                        scoreSlammed++;

                        /*
                         * If there are no enemies, I'll at least create one. I'll put this here
                         * so that it doesn't affect the time in which the first bad guy appears.
                         */
                        if (arrayEnemies.size == 0)
                            createEnemies();
                    }
                } else if (body.getUserData() instanceof Boost) {
                    Boost obj = (Boost) body.getUserData();
                    if (obj.state == Boost.STATE_TAKEN) {
                        oWorldBox.destroyBody(body);
                        arrayBoost.removeValue(obj, true);
                        boostPool.free(obj);
                    }
                } else if (body.getUserData() instanceof Coin) {
                    Coin obj = (Coin) body.getUserData();
                    if (obj.state == Coin.STATE_TAKEN) {
                        oWorldBox.destroyBody(body);
                        arrayCoins.removeValue(obj, true);
                        coinPool.free(obj);
                    }
                }
            }

        }
    }

    private void updateRobot(float delta, Body body, float acelX, boolean slam) {
        Robot obj = (Robot) body.getUserData();
        obj.update(delta, body, acelX, slam);

        if (obj.position.y > 12) {
            Achievements.unlockSuperJump();
            Gdx.app.log("ACHIIIII", "Asdsadasd");
        }

    }

    private void updateEnemy(float delta, Body body) {
        Enemy obj = (Enemy) body.getUserData();
        obj.update(delta, body, random);

    }

    private void updateBoost(float delta, Body body) {
        Boost obj = (Boost) body.getUserData();
        obj.update(delta, body);

    }

    private void updatePlataforma(float delta, Body body) {
        Platform obj = (Platform) body.getUserData();
        obj.update(delta);

    }

    private void updateCoin(float delta, Body body) {
        Coin obj = (Coin) body.getUserData();
        obj.update(delta, body);

        if (obj.position.x < -3 || obj.position.x > WIDTH + 3) {
            obj.state = Coin.STATE_TAKEN;
        }

        if (isCoinRain) {
            body.setGravityScale(1);
            body.setLinearVelocity(body.getLinearVelocity().x * .25f, 0);
        }
    }

    class Colisiones implements ContactListener {

        @Override
        public void beginContact(Contact contact) {
            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();

            if (a.getBody().getUserData() instanceof Robot)
                beginContactRobotOtraCosa(a, b);
            else if (b.getBody().getUserData() instanceof Robot)
                beginContactRobotOtraCosa(b, a);

            if (a.getBody().getUserData() instanceof Enemy)
                beginContactEnemyWithOthers(a, b);
            else if (b.getBody().getUserData() instanceof Enemy)
                beginContactEnemyWithOthers(b, a);

        }

        /**
         * Begin contacto ROBOT con OTRA-COSA
         */
        private void beginContactRobotOtraCosa(Fixture robotFixture, Fixture otherFixture) {
            Robot robot = (Robot) robotFixture.getBody().getUserData();
            Object otherObject = otherFixture.getBody().getUserData();

            if (otherObject.equals("piso")) {
                robot.jump();

                if (!robot.isInvincible)// Si es invencible no le quito el combo
                    combo = 0;
            } else if (otherObject instanceof Platform) {
                Platform platform = (Platform) otherObject;
                if (platform.state == Platform.STATE_FIRE && !robot.isInvincible) {
                    robot.hit();
                    return;
                } else if (platform.state == Platform.STATE_BREAKABLE) {
                    platform.setBroken();
                } else if (platform.state == Platform.STATE_BROKEN) {
                    return;
                }
                if (!robot.isInvincible && robot.state == Robot.STATE_FALLING)// Si es invencible no le quito el combo
                    combo = 0;
                robot.jump();
            } else if (otherObject instanceof Boost) {
                Boost boost = (Boost) otherObject;
                boost.hit();
                Assets.playSound(Assets.soundBoost);

                if (boost.type == Boost.TIPO_SUPERJUMP) {
                    robot.isSuperJump = true;
                } else if (boost.type == Boost.TIPO_INVENCIBLE) {
                    robot.isInvincible = true;
                } else if (boost.type == Boost.TIPO_COIN_RAIN) {
                    isCoinRain = true;
                } else if (boost.type == Boost.TIPO_ICE) {
                    for (Enemy arrEnemy : arrayEnemies) {
                        arrEnemy.setFrozen();
                    }
                }
            } else if (otherObject instanceof Coin) {
                Coin coin = (Coin) otherObject;
                if (coin.state == Coin.STATE_NORMAL) {
                    coin.state = Coin.STATE_TAKEN;
                    takenCoins++;
                    Settings.currentCoins++;
                    Assets.playSound(Assets.soundCoin);
                }

            } else if (otherObject instanceof Enemy) {
                Enemy enemy = (Enemy) otherObject;

                // I can touch from the middle of the enemy up
                float posRobot = robot.position.y - Robot.RADIUS;
                float pisY = enemy.position.y;

                if (enemy.state != Enemy.STATE_JUST_APPEAR) {
                    if (robot.isInvincible) {
                        enemy.die();
                        combo++;
                    } else if (posRobot > pisY) {
                        enemy.hit();
                        robot.jump();
                        combo++;
                    } else if (robot.state != Robot.STATE_DEAD) {
                        robot.hit();
                        combo = 0;
                    }
                    if (combo >= COMBO_TO_START_GETTING_COINS) {
                        takenCoins += combo;
                        Settings.currentCoins += combo;
                    }

                    Achievements.unlockCombos();
                }
            }
        }

        private void beginContactEnemyWithOthers(Fixture enemyFixture, Fixture otherFixture) {
            Enemy enemy = (Enemy) enemyFixture.getBody().getUserData();
            Object otherObject = otherFixture.getBody().getUserData();

            if (otherObject.equals("pared")) {

                enemyFixture.getBody().setLinearVelocity(
                        enemyFixture.getBody().getLinearVelocity().x * -1,
                        enemyFixture.getBody().getLinearVelocity().y);
            } else if (otherObject.equals("piso")) {
                if (enemy.state == Enemy.STATE_FLYING) {
                    enemyFixture.getBody().setLinearVelocity(
                            enemyFixture.getBody().getLinearVelocity().x,
                            enemyFixture.getBody().getLinearVelocity().y * -1);
                }
            }
        }

        @Override
        public void endContact(Contact contact) {

        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {

            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();

            if (a.getBody().getUserData() instanceof Robot)
                preSolveRobot(a, b, contact);
            else if (b.getBody().getUserData() instanceof Robot)
                preSolveRobot(b, a, contact);

            if (a.getBody().getUserData() instanceof Enemy)
                preSolveEnemy(a, b, contact);
            else if (b.getBody().getUserData() instanceof Enemy)
                preSolveEnemy(b, a, contact);

            if (a.getBody().getUserData() instanceof Coin)
                preSolveCoin(b, contact);
            else if (b.getBody().getUserData() instanceof Coin)
                preSolveCoin(a, contact);

        }

        private void preSolveRobot(Fixture robotFixture, Fixture otherFixture,
                                   Contact contact) {
            Object otherObject = otherFixture.getBody().getUserData();
            Robot robot = (Robot) robotFixture.getBody().getUserData();

            // Platform oneSide
            if (otherObject instanceof Platform) {
                Platform obj = (Platform) otherObject;
                float posRobot = robot.position.y - Robot.RADIUS + .05f;
                float pisY = obj.position.y + (Platform.HEIGHT / 2f);

                if (posRobot < pisY || obj.state == Platform.STATE_BROKEN)
                    contact.setEnabled(false);

            }
            // Enemy cannot be touched when it appears
            else if (otherObject instanceof Enemy) {
                Enemy enemy = (Enemy) otherObject;
                if (enemy.state == Enemy.STATE_JUST_APPEAR
                        || robot.isInvincible)
                    contact.setEnabled(false);
            } else if (otherObject instanceof Coin) {
                contact.setEnabled(false);
            }
        }

        private void preSolveEnemy(Fixture enemyFixture, Fixture otherFixture,
                                   Contact contact) {
            Object otherObject = otherFixture.getBody().getUserData();
            Enemy enemy = (Enemy) enemyFixture.getBody().getUserData();

            // Enemy cannot touch the platforms if he is flying
            if (otherObject instanceof Platform) {
                if (enemy.state == Enemy.STATE_FLYING)
                    contact.setEnabled(false);

            }
        }

        private void preSolveCoin(Fixture otherFixture,
                                  Contact contact) {
            Object otherObject = otherFixture.getBody().getUserData();

            if (otherObject.equals("pared")) {
                contact.setEnabled(false);
            }
        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {
            // TODO Auto-generated method stub

        }
    }

}
