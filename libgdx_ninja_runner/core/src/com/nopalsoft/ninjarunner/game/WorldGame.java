package com.nopalsoft.ninjarunner.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.nopalsoft.ninjarunner.Assets;
import com.nopalsoft.ninjarunner.Settings;
import com.nopalsoft.ninjarunner.objects.Item;
import com.nopalsoft.ninjarunner.objects.ItemCandyBean;
import com.nopalsoft.ninjarunner.objects.ItemCandyCorn;
import com.nopalsoft.ninjarunner.objects.ItemCandyJelly;
import com.nopalsoft.ninjarunner.objects.ItemCurrency;
import com.nopalsoft.ninjarunner.objects.ItemEnergy;
import com.nopalsoft.ninjarunner.objects.ItemHearth;
import com.nopalsoft.ninjarunner.objects.ItemMagnet;
import com.nopalsoft.ninjarunner.objects.Missile;
import com.nopalsoft.ninjarunner.objects.Obstacle;
import com.nopalsoft.ninjarunner.objects.ObstacleBoxes4;
import com.nopalsoft.ninjarunner.objects.ObstacleBoxes7;
import com.nopalsoft.ninjarunner.objects.Pet;
import com.nopalsoft.ninjarunner.objects.Platform;
import com.nopalsoft.ninjarunner.objects.Player;
import com.nopalsoft.ninjarunner.objects.Wall;

public class WorldGame {
    static final int STATE_GAMEOVER = 1;
    public int state;
    public World myWorldBox;
    public Player myPlayer;
    public Pet myPet;
    float timeToSpawnMissile;
    ObjectManagerBox2d myManager;
    Array<Body> arrayBodies;
    Array<Platform> arrayPlatforms;
    Array<Wall> arrayWall;
    Array<Item> arrayItems;
    Array<Obstacle> arrayObstacles;
    Array<Missile> arrayMissiles;

    /**
     * Variable that indicates how far the world has been created.
     */
    float worldCreatedUpToX;

    int takenCoins;
    long score;
    boolean isBodySliding; // Indicates whether the body you have right now is sliding.
    boolean recreateFixture = false;

    public WorldGame() {
        myWorldBox = new World(new Vector2(0, -9.8f), true);
        myWorldBox.setContactListener(new com.nopalsoft.ninjarunner.game.WorldGame.Collisions());

        myManager = new ObjectManagerBox2d(this);

        arrayBodies = new Array<>();
        arrayPlatforms = new Array<>();
        arrayItems = new Array<>();
        arrayWall = new Array<>();
        arrayObstacles = new Array<>();
        arrayMissiles = new Array<>();

        timeToSpawnMissile = 0;

        myManager.createHeroStand(2f, 1f, Settings.getSelectedSkin());
        myManager.createPet(myPlayer.position.x - 1, myPlayer.position.y + .75f);

        worldCreatedUpToX = myManager.createPlatform(0, 0, 3);

        createNextPart();

    }

    private void createNextPart() {
        float x = worldCreatedUpToX;

        while (worldCreatedUpToX < (x + 1)) {

            // First I create the platform
            float separation = MathUtils.random(1f, 3f);
            float y = MathUtils.random(0, 1.5f);

            // y = 0;
            int connectedPlatforms = 25;

            worldCreatedUpToX = myManager.createPlatform(worldCreatedUpToX + separation, y, connectedPlatforms);

            // Then I add things above the platform.

            float xAux = x + separation;

            while (xAux < worldCreatedUpToX - 2) {
                if (xAux < x + separation + 2)
                    xAux = addRandomItems(xAux, y);

                if (MathUtils.randomBoolean(.1f)) {
                    xAux = myManager.createBox4(xAux, y + .8f);
                    xAux = addRandomItems(xAux, y);

                } else if (MathUtils.randomBoolean(.1f)) {
                    xAux = myManager.createBox7(xAux, y + 1f);
                    xAux = addRandomItems(xAux, y);
                } else if (MathUtils.randomBoolean(.1f)) {
                    xAux = myManager.createWall(xAux, y + 3.17f);
                    xAux = addRandomItems(xAux, y);
                } else {
                    xAux = addRandomItems(xAux, y);
                }
            }
        }
    }

    private float addRandomItems(float xAux, float y) {

        if (MathUtils.randomBoolean(.3f)) {
            for (int i = 0; i < 5; i++) {
                myManager.createItem(ItemCurrency.class, xAux, y + 1.5f);
                xAux = myManager.createItem(ItemCurrency.class, xAux, y + 1f);
            }
        } else if (MathUtils.randomBoolean(.5f)) {

            for (int i = 0; i < 5; i++) {
                myManager.createItem(ItemCandyBean.class, xAux, y + .8f);
                myManager.createItem(ItemCandyBean.class, xAux, y + 1.1f);
                xAux = myManager.createItem(ItemCandyJelly.class, xAux, y + 1.5f);
            }
        } else if (MathUtils.randomBoolean(.5f)) {

            for (int i = 0; i < 5; i++) {
                myManager.createItem(ItemCandyCorn.class, xAux, y + .8f);
                myManager.createItem(ItemCandyCorn.class, xAux, y + 1.1f);
                xAux = myManager.createItem(ItemCandyCorn.class, xAux, y + 1.5f);
            }
        }

        if (MathUtils.randomBoolean(.025f)) {

            xAux = myManager.createItem(ItemHearth.class, xAux, y + 1.5f);
            xAux = myManager.createItem(ItemEnergy.class, xAux, y + 1.5f);
        } else if (MathUtils.randomBoolean(.025f)) {

            xAux = myManager.createItem(ItemMagnet.class, xAux, y + 1.5f);

        }

        return xAux;
    }

    public void update(float delta, boolean didJump, boolean dash, boolean didSlide) {
        myWorldBox.step(delta, 8, 4);

        myWorldBox.getBodies(arrayBodies);
        eliminarObjetos();
        myWorldBox.getBodies(arrayBodies);

        for (com.badlogic.gdx.physics.box2d.Body body : arrayBodies) {
            if (body.getUserData() instanceof Player) {
                updatePersonaje(delta, body, didJump, dash, didSlide);
            } else if (body.getUserData() instanceof Pet) {
                updateMascota(delta, body);
            } else if (body.getUserData() instanceof Platform) {
                updatePlataforma(body);
            } else if (body.getUserData() instanceof Wall) {
                updatePared(body);
            } else if (body.getUserData() instanceof Item) {
                updateItem(delta, body);
            } else if (body.getUserData() instanceof Obstacle) {
                updateObstaculos(delta, body);
            } else if (body.getUserData() instanceof Missile) {
                updateMissil(delta, body);
            }
        }

        if (myPlayer.position.x > worldCreatedUpToX - 5)
            createNextPart();

        if (myPlayer.state == Player.STATE_DEAD && myPlayer.stateTime >= Player.DURATION_DEAD)
            state = STATE_GAMEOVER;

        timeToSpawnMissile += delta;
        float TIME_TO_SPAWN_MISSIL = 15;
        if (timeToSpawnMissile >= TIME_TO_SPAWN_MISSIL) {
            timeToSpawnMissile -= TIME_TO_SPAWN_MISSIL;

            myManager.crearMissil(myPlayer.position.x + 10, myPlayer.position.y);

        }

    }

    private void eliminarObjetos() {
        for (Body body : arrayBodies) {
            if (body.getUserData() instanceof Platform) {
                Platform obj = (Platform) body.getUserData();
                if (obj.state == Platform.STATE_DESTROY) {
                    arrayPlatforms.removeValue(obj, true);
                    Pools.free(obj);
                    myWorldBox.destroyBody(body);
                }
            } else if (body.getUserData() instanceof Wall) {
                Wall obj = (Wall) body.getUserData();
                if (obj.state == Wall.STATE_DESTROY) {
                    arrayWall.removeValue(obj, true);
                    Pools.free(obj);
                    myWorldBox.destroyBody(body);
                }
            } else if (body.getUserData() instanceof Item) {
                Item obj = (Item) body.getUserData();
                if (obj.state == Item.STATE_DESTROY && obj.stateTime >= Item.DURATION_PICK) {
                    arrayItems.removeValue(obj, true);
                    Pools.free(obj);
                    myWorldBox.destroyBody(body);
                }
            } else if (body.getUserData() instanceof ObstacleBoxes4) {
                ObstacleBoxes4 obj = (ObstacleBoxes4) body.getUserData();

                if (obj.state == ObstacleBoxes4.STATE_DESTROY && obj.effect.isComplete()) {
                    obj.effect.free();
                    arrayObstacles.removeValue(obj, true);
                    Pools.free(obj);
                    myWorldBox.destroyBody(body);
                }
            } else if (body.getUserData() instanceof ObstacleBoxes7) {
                ObstacleBoxes7 obj = (ObstacleBoxes7) body.getUserData();

                if (obj.state == ObstacleBoxes7.STATE_DESTROY && obj.effect.isComplete()) {
                    obj.effect.free();
                    arrayObstacles.removeValue(obj, true);
                    Pools.free(obj);
                    myWorldBox.destroyBody(body);
                }
            } else if (body.getUserData() instanceof Missile) {
                Missile obj = (Missile) body.getUserData();
                if (obj.state == Missile.STATE_DESTROY) {
                    arrayMissiles.removeValue(obj, true);
                    Pools.free(obj);
                    myWorldBox.destroyBody(body);
                }
            }
        }
    }

    private void updatePersonaje(float delta, Body body, boolean didJump, boolean dash, boolean didSlide) {
        myPlayer.update(delta, body, didJump, false, dash, didSlide);

        if (myPlayer.position.y < -1) {
            myPlayer.die();
        } else if (myPlayer.isSlide && !isBodySliding) {
            recreateFixture = true;
            isBodySliding = true;
            myManager.recreateFixturePlayerSlide(body);
        } else if (!myPlayer.isSlide && isBodySliding) {
            recreateFixture = true;
            isBodySliding = false;
            myManager.recreateFixturePlayerStand(body);
        }

    }

    private void updateMascota(float delta, Body body) {

        float targetPositionX = myPlayer.position.x - .75f;
        float targetPositionY = myPlayer.position.y + .25f;

        if (myPet.petType == Pet.PetType.BOMB) {
            Missile oMissile = getClosestUpcomingMissile();
            if (oMissile != null && oMissile.distanceFromCharacter < 5f && oMissile.state == Missile.STATE_NORMAL) {
                targetPositionX = oMissile.position.x;
                targetPositionY = oMissile.position.y;
            }
        } else {
            if (myPlayer.isDash) {
                targetPositionX = myPlayer.position.x + 4.25f;
                targetPositionY = myPlayer.position.y;
            }
        }

        myPet.update(body, delta, targetPositionX, targetPositionY);
    }

    private void updatePlataforma(Body body) {
        Platform obj = (Platform) body.getUserData();

        if (obj.position.x < myPlayer.position.x - 3)
            obj.setDestroy();

    }

    private void updatePared(Body body) {
        Wall obj = (Wall) body.getUserData();

        if (obj.position.x < myPlayer.position.x - 3)
            obj.setDestroy();

    }

    private void updateItem(float delta, Body body) {
        Item obj = (Item) body.getUserData();
        obj.update(delta, body, myPet, myPlayer);

        if (obj.position.x < myPlayer.position.x - 3)
            obj.setPicked();

    }

    private void updateObstaculos(float delta, Body body) {
        Obstacle obj = (Obstacle) body.getUserData();
        obj.update(delta);

        if (obj.position.x < myPlayer.position.x - 3)
            obj.setDestroy();

    }

    private void updateMissil(float delta, Body body) {
        Missile obj = (Missile) body.getUserData();
        obj.update(delta, body, myPlayer);

        if (obj.position.x < myPlayer.position.x - 3)
            obj.setDestroy();

        arrayMissiles.sort();

    }

    /**
     * Return the missile closest to the character that is in normal state.
     */
    private Missile getClosestUpcomingMissile() {
        for (int i = 0; i < arrayMissiles.size; i++) {
            if (arrayMissiles.get(i).state == Missile.STATE_NORMAL)
                return arrayMissiles.get(i);
        }
        return null;
    }


    class Collisions implements ContactListener {

        @Override
        public void beginContact(Contact contact) {
            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();

            if (a.getBody().getUserData() instanceof Player)
                beginContactHeroOtraCosa(a, b);
            else if (b.getBody().getUserData() instanceof Player)
                beginContactHeroOtraCosa(b, a);

            if (a.getBody().getUserData() instanceof Pet)
                beginContactMascotaOtraCosa(b);
            else if (b.getBody().getUserData() instanceof Pet)
                beginContactMascotaOtraCosa(a);

        }

        private void beginContactHeroOtraCosa(Fixture fixHero, Fixture otraCosa) {
            Object other = otraCosa.getBody().getUserData();

            if (other instanceof Platform) {
                if (fixHero.getUserData().equals("pies")) {
                    if (recreateFixture)
                        recreateFixture = false;
                    else
                        myPlayer.touchFloor();

                }
            } else if (other instanceof Item) {
                Item obj = (Item) other;
                if (obj.state == Item.STATE_NORMAL) {
                    if (obj instanceof ItemCurrency) {
                        takenCoins++;
                        score++;
                        Assets.playSound(Assets.coin, 1);
                    } else if (obj instanceof ItemMagnet) {
                        myPlayer.setPickUpMagnet();
                    } else if (obj instanceof ItemEnergy) {
                        //todo oPlayer.shield++;
                    } else if (obj instanceof ItemHearth) {
                        myPlayer.lives++;
                    } else if (obj instanceof ItemCandyJelly) {
                        Assets.playSound(Assets.popCandy, 1);
                        score += 2;
                    } else if (obj instanceof ItemCandyBean) {
                        Assets.playSound(Assets.popCandy, 1);
                        score += 5;
                    } else if (obj instanceof ItemCandyCorn) {
                        Assets.playSound(Assets.popCandy, 1);
                        score += 15;
                    }

                    obj.setPicked();
                }
            } else if (other instanceof Wall) {
                Wall obj = (Wall) other;
                if (obj.state == Wall.STATE_NORMAL) {
                    myPlayer.getDizzy();
                }
            } else if (other instanceof Obstacle) {
                Obstacle obj = (Obstacle) other;
                if (obj.state == Obstacle.STATE_NORMAL) {
                    obj.setDestroy();
                    myPlayer.getHurt();
                }
            } else if (other instanceof Missile) {
                Missile obj = (Missile) other;
                if (obj.state == Obstacle.STATE_NORMAL) {
                    obj.setHitTarget();
                    myPlayer.getDizzy();
                }
            }

        }

        public void beginContactMascotaOtraCosa(Fixture otherFixture) {
            Object otherObject = otherFixture.getBody().getUserData();

            if (otherObject instanceof Wall && myPlayer.isDash) {
                Wall obj = (Wall) otherObject;
                obj.setDestroy();
            } else if (otherObject instanceof Obstacle && myPlayer.isDash) {
                Obstacle obj = (Obstacle) otherObject;
                obj.setDestroy();
            } else if (otherObject instanceof ItemCurrency) {
                ItemCurrency obj = (ItemCurrency) otherObject;
                if (obj.state == ItemCurrency.STATE_NORMAL) {
                    obj.setPicked();
                    takenCoins++;
                    Assets.playSound(Assets.coin, 1);
                }
            } else if (otherObject instanceof Missile) {
                Missile obj = (Missile) otherObject;
                if (obj.state == Obstacle.STATE_NORMAL) {
                    obj.setHitTarget();
                }
            }

        }

        @Override
        public void endContact(Contact contact) {
            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();

            if (a == null || b == null)
                return;

            if (a.getBody().getUserData() instanceof Player)
                endContactHeroOtherFixture(a, b);
            else if (b.getBody().getUserData() instanceof Player)
                endContactHeroOtherFixture(b, a);

        }

        private void endContactHeroOtherFixture(Fixture fixHero, Fixture otherfixture) {
            Object otherObject = otherfixture.getBody().getUserData();

            if (otherObject instanceof Platform) {
                if (fixHero.getUserData().equals("pies"))
                    myPlayer.endTouchFloor();

            }
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

        private void preSolveHero(Fixture fixHero, Fixture otherFixture, Contact contact) {
            Object otherObject = otherFixture.getBody().getUserData();

            if (otherObject instanceof Platform) {
                Platform obj = (Platform) otherObject;

                float ponyY = fixHero.getBody().getPosition().y - .30f;
                float pisY = obj.position.y + Platform.HEIGHT / 2f;

                if (ponyY < pisY)
                    contact.setEnabled(false);

            }

        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {
            // TODO Auto-generated method stub

        }
    }

}
