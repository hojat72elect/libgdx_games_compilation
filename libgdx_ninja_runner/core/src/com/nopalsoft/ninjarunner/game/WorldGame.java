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
import com.nopalsoft.ninjarunner.Assets;
import com.nopalsoft.ninjarunner.Settings;
import com.nopalsoft.ninjarunner.objetos.Item;
import com.nopalsoft.ninjarunner.objetos.ItemCandyBean;
import com.nopalsoft.ninjarunner.objetos.ItemCandyCorn;
import com.nopalsoft.ninjarunner.objetos.ItemCandyJelly;
import com.nopalsoft.ninjarunner.objetos.ItemEnergy;
import com.nopalsoft.ninjarunner.objetos.ItemHearth;
import com.nopalsoft.ninjarunner.objetos.ItemMagnet;
import com.nopalsoft.ninjarunner.objetos.ItemMoneda;
import com.nopalsoft.ninjarunner.objetos.Mascota;
import com.nopalsoft.ninjarunner.objetos.Missil;
import com.nopalsoft.ninjarunner.objetos.Obstaculo;
import com.nopalsoft.ninjarunner.objetos.Pared;
import com.nopalsoft.ninjarunner.objetos.Personaje;
import com.nopalsoft.ninjarunner.objetos.Plataforma;

public class WorldGame {
    static final int STATE_GAMEOVER = 1;
    public int state;
    public World oWorldBox;
    public Personaje oPersonaje;
    public Mascota oMascota;
    float timeToSpwanMissil;
    ObjectManagerBox2d oManager;
    Array<Body> arrBodies;
    Array<Plataforma> arrPlataformas;
    Array<Pared> arrPared;
    Array<Item> arrItems;
    Array<Obstaculo> arrObstaculos;
    Array<Missil> arrMissiles;

    /**
     * Variable que indica hasta donde se ha ido creando el mundo
     */
    float mundoCreadoHastaX;

    int monedasTomadas;
    long puntuacion;
    boolean bodyIsSLide;// INdica el si el cuarpo que tiene ahorita es sliding;
    boolean recreateFixture = false;

    public WorldGame() {
        oWorldBox = new World(new Vector2(0, -9.8f), true);
        oWorldBox.setContactListener(new Colisiones());

        oManager = new ObjectManagerBox2d(this);

        arrBodies = new Array<>();
        arrPlataformas = new Array<>();
        arrItems = new Array<>();
        arrPared = new Array<>();
        arrObstaculos = new Array<>();
        arrMissiles = new Array<>();

        timeToSpwanMissil = 0;

        oManager.crearHeroStand(2f, 1f, Settings.skinSeleccionada);
        oManager.crearMascota(oPersonaje.position.x - 1, oPersonaje.position.y + .75f);

        mundoCreadoHastaX = oManager.crearPlataforma(0, 0, 3);

        crearSiguienteParte();

    }

    private void crearSiguienteParte() {
        float x = mundoCreadoHastaX;

        // SEPARACION MAXIMA 3f
        // ALTURA MAXIMA 1.5f

        while (mundoCreadoHastaX < (x + 1)) {

            // Primero creo la plataforma
            int plataformasPegadas = MathUtils.random(1, 20);
            float separacion = MathUtils.random(1f, 3f);
            float y = MathUtils.random(0, 1.5f);

            // y = 0;
            plataformasPegadas = 25;

            mundoCreadoHastaX = oManager.crearPlataforma(mundoCreadoHastaX + separacion, y, plataformasPegadas);

            // Despues agrego cosas arriba de la plataforma

            float xAux = x + separacion;

            while (xAux < mundoCreadoHastaX - 2) {
                if (xAux < x + separacion + 2)
                    xAux = addRandomItems(xAux, y);

                if (MathUtils.randomBoolean(.1f)) {
                    xAux = oManager.crearCaja4(xAux, y + .8f);
                    xAux = addRandomItems(xAux, y);

                } else if (MathUtils.randomBoolean(.1f)) {
                    xAux = oManager.crearCaja7(xAux, y + 1f);
                    xAux = addRandomItems(xAux, y);
                } else if (MathUtils.randomBoolean(.1f)) {
                    xAux = oManager.crearPared(xAux, y + 3.17f);
                    xAux = addRandomItems(xAux, y);
                } else {
                    xAux = addRandomItems(xAux, y);
                }
            }

            /*
             * PARED y+3.17; CAJA4 y+.8f; CAJA7 y+1f;
             */

        }

    }

    private float addRandomItems(float xAux, float y) {

        if (MathUtils.randomBoolean(.3f)) {
            for (int i = 0; i < 5; i++) {
                oManager.crearItem(ItemMoneda.class, xAux, y + 1.5f);
                xAux = oManager.crearItem(ItemMoneda.class, xAux, y + 1f);
            }
        } else if (MathUtils.randomBoolean(.5f)) {

            for (int i = 0; i < 5; i++) {
                oManager.crearItem(ItemCandyBean.class, xAux, y + .8f);
                oManager.crearItem(ItemCandyBean.class, xAux, y + 1.1f);
                xAux = oManager.crearItem(ItemCandyJelly.class, xAux, y + 1.5f);
            }
        } else if (MathUtils.randomBoolean(.5f)) {

            for (int i = 0; i < 5; i++) {
                oManager.crearItem(ItemCandyCorn.class, xAux, y + .8f);
                oManager.crearItem(ItemCandyCorn.class, xAux, y + 1.1f);
                xAux = oManager.crearItem(ItemCandyCorn.class, xAux, y + 1.5f);
            }
        }

        if (MathUtils.randomBoolean(.025f)) {

            xAux = oManager.crearItem(ItemHearth.class, xAux, y + 1.5f);
            xAux = oManager.crearItem(ItemEnergy.class, xAux, y + 1.5f);
        } else if (MathUtils.randomBoolean(.025f)) {

            xAux = oManager.crearItem(ItemMagnet.class, xAux, y + 1.5f);

        }

        return xAux;
    }

    public void update(float delta, boolean didJump, boolean dash, boolean didSlide) {
        oWorldBox.step(delta, 8, 4);

        oWorldBox.getBodies(arrBodies);
        eliminarObjetos();
        oWorldBox.getBodies(arrBodies);

        for (com.badlogic.gdx.physics.box2d.Body body : arrBodies) {
            if (body.getUserData() instanceof com.nopalsoft.ninjarunner.objetos.Personaje) {
                updatePersonaje(delta, body, didJump, dash, didSlide);
            } else if (body.getUserData() instanceof com.nopalsoft.ninjarunner.objetos.Mascota) {
                updateMascota(delta, body);
            } else if (body.getUserData() instanceof com.nopalsoft.ninjarunner.objetos.Plataforma) {
                updatePlataforma(body);
            } else if (body.getUserData() instanceof com.nopalsoft.ninjarunner.objetos.Pared) {
                updatePared(body);
            } else if (body.getUserData() instanceof com.nopalsoft.ninjarunner.objetos.Item) {
                updateItem(delta, body);
            } else if (body.getUserData() instanceof com.nopalsoft.ninjarunner.objetos.Obstaculo) {
                updateObstaculos(delta, body);
            } else if (body.getUserData() instanceof com.nopalsoft.ninjarunner.objetos.Missil) {
                updateMissil(delta, body);
            }
        }

        if (oPersonaje.position.x > mundoCreadoHastaX - 5)
            crearSiguienteParte();

        if (oPersonaje.state == Personaje.STATE_DEAD && oPersonaje.stateTime >= Personaje.DURATION_DEAD)
            state = STATE_GAMEOVER;

        timeToSpwanMissil += delta;
        float TIME_TO_SPAWN_MISSIL = 15;
        if (timeToSpwanMissil >= TIME_TO_SPAWN_MISSIL) {
            timeToSpwanMissil -= TIME_TO_SPAWN_MISSIL;

            oManager.crearMissil(oPersonaje.position.x + 10, oPersonaje.position.y);

        }

    }

    private void eliminarObjetos() {
        for (com.badlogic.gdx.physics.box2d.Body body : arrBodies) {
            if (body.getUserData() instanceof com.nopalsoft.ninjarunner.objetos.Plataforma) {
                com.nopalsoft.ninjarunner.objetos.Plataforma obj = (com.nopalsoft.ninjarunner.objetos.Plataforma) body.getUserData();
                if (obj.state == com.nopalsoft.ninjarunner.objetos.Plataforma.STATE_DESTROY) {
                    arrPlataformas.removeValue(obj, true);
                    com.badlogic.gdx.utils.Pools.free(obj);
                    oWorldBox.destroyBody(body);
                }
            } else if (body.getUserData() instanceof com.nopalsoft.ninjarunner.objetos.Pared) {
                com.nopalsoft.ninjarunner.objetos.Pared obj = (com.nopalsoft.ninjarunner.objetos.Pared) body.getUserData();
                if (obj.state == com.nopalsoft.ninjarunner.objetos.Pared.STATE_DESTROY) {
                    arrPared.removeValue(obj, true);
                    com.badlogic.gdx.utils.Pools.free(obj);
                    oWorldBox.destroyBody(body);
                }
            } else if (body.getUserData() instanceof com.nopalsoft.ninjarunner.objetos.Item) {
                com.nopalsoft.ninjarunner.objetos.Item obj = (com.nopalsoft.ninjarunner.objetos.Item) body.getUserData();
                if (obj.state == com.nopalsoft.ninjarunner.objetos.Item.STATE_DESTROY && obj.stateTime >= com.nopalsoft.ninjarunner.objetos.Item.DURATION_PICK) {
                    arrItems.removeValue(obj, true);
                    com.badlogic.gdx.utils.Pools.free(obj);
                    oWorldBox.destroyBody(body);
                }
            } else if (body.getUserData() instanceof com.nopalsoft.ninjarunner.objetos.ObstaculoCajas4) {
                com.nopalsoft.ninjarunner.objetos.ObstaculoCajas4 obj = (com.nopalsoft.ninjarunner.objetos.ObstaculoCajas4) body.getUserData();

                if (obj.state == com.nopalsoft.ninjarunner.objetos.ObstaculoCajas4.STATE_DESTROY && obj.effect.isComplete()) {
                    obj.effect.free();
                    arrObstaculos.removeValue(obj, true);
                    com.badlogic.gdx.utils.Pools.free(obj);
                    oWorldBox.destroyBody(body);
                }
            } else if (body.getUserData() instanceof com.nopalsoft.ninjarunner.objetos.ObstaculoCajas7) {
                com.nopalsoft.ninjarunner.objetos.ObstaculoCajas7 obj = (com.nopalsoft.ninjarunner.objetos.ObstaculoCajas7) body.getUserData();

                if (obj.state == com.nopalsoft.ninjarunner.objetos.ObstaculoCajas7.STATE_DESTROY && obj.effect.isComplete()) {
                    obj.effect.free();
                    arrObstaculos.removeValue(obj, true);
                    com.badlogic.gdx.utils.Pools.free(obj);
                    oWorldBox.destroyBody(body);
                }
            } else if (body.getUserData() instanceof com.nopalsoft.ninjarunner.objetos.Missil) {
                com.nopalsoft.ninjarunner.objetos.Missil obj = (com.nopalsoft.ninjarunner.objetos.Missil) body.getUserData();
                if (obj.state == com.nopalsoft.ninjarunner.objetos.Missil.STATE_DESTROY) {
                    arrMissiles.removeValue(obj, true);
                    com.badlogic.gdx.utils.Pools.free(obj);
                    oWorldBox.destroyBody(body);
                }
            }
        }
    }

    private void updatePersonaje(float delta, Body body, boolean didJump, boolean dash, boolean didSlide) {
        oPersonaje.update(delta, body, didJump, false, dash, didSlide);

        if (oPersonaje.position.y < -1) {
            oPersonaje.die();
        } else if (oPersonaje.isSlide && !bodyIsSLide) {
            recreateFixture = true;
            bodyIsSLide = true;
            oManager.recreateFixturePersonajeSlide(body);
        } else if (!oPersonaje.isSlide && bodyIsSLide) {
            recreateFixture = true;
            bodyIsSLide = false;
            oManager.recreateFixturePersonajeStand(body);
        }

    }

    private void updateMascota(float delta, Body body) {

        float targetPositionX = oPersonaje.position.x - .75f;
        float targetPositionY = oPersonaje.position.y + .25f;

        if (oMascota.tipo == Mascota.Tipo.BOMBA) {
            Missil oMissil = getNextClosesMissil();
            if (oMissil != null && oMissil.distanceFromPersonaje < 5f && oMissil.state == Missil.STATE_NORMAL) {
                targetPositionX = oMissil.position.x;
                targetPositionY = oMissil.position.y;
            }
        } else {
            if (oPersonaje.isDash) {
                targetPositionX = oPersonaje.position.x + 4.25f;
                targetPositionY = oPersonaje.position.y;
            }
        }

        oMascota.update(body, delta, targetPositionX, targetPositionY);
    }

    private void updatePlataforma(Body body) {
        Plataforma obj = (Plataforma) body.getUserData();

        if (obj.position.x < oPersonaje.position.x - 3)
            obj.setDestroy();

    }

    private void updatePared(Body body) {
        Pared obj = (Pared) body.getUserData();

        if (obj.position.x < oPersonaje.position.x - 3)
            obj.setDestroy();

    }

    private void updateItem(float delta, Body body) {
        Item obj = (Item) body.getUserData();
        obj.update(delta, body, oMascota, oPersonaje);

        if (obj.position.x < oPersonaje.position.x - 3)
            obj.setPicked();

    }

    private void updateObstaculos(float delta, Body body) {
        Obstaculo obj = (Obstaculo) body.getUserData();
        obj.update(delta);

        if (obj.position.x < oPersonaje.position.x - 3)
            obj.setDestroy();

    }

    private void updateMissil(float delta, Body body) {
        Missil obj = (Missil) body.getUserData();
        obj.update(delta, body, oPersonaje);

        if (obj.position.x < oPersonaje.position.x - 3)
            obj.setDestroy();

        arrMissiles.sort();

    }

    /**
     * Regresa el misil mas cercano al personaje que este en estado normal
     */
    private Missil getNextClosesMissil() {
        for (int i = 0; i < arrMissiles.size; i++) {
            if (arrMissiles.get(i).state == Missil.STATE_NORMAL)
                return arrMissiles.get(i);
        }
        return null;
    }

    /**
     * #### COLISIONES #####
     */

    class Colisiones implements ContactListener {

        @Override
        public void beginContact(Contact contact) {
            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();

            if (a.getBody().getUserData() instanceof Personaje)
                beginContactHeroOtraCosa(a, b);
            else if (b.getBody().getUserData() instanceof Personaje)
                beginContactHeroOtraCosa(b, a);

            if (a.getBody().getUserData() instanceof Mascota)
                beginContactMascotaOtraCosa(b);
            else if (b.getBody().getUserData() instanceof Mascota)
                beginContactMascotaOtraCosa(a);

        }

        private void beginContactHeroOtraCosa(Fixture fixHero, Fixture otraCosa) {
            Object oOtraCosa = otraCosa.getBody().getUserData();

            if (oOtraCosa instanceof Plataforma) {
                if (fixHero.getUserData().equals("pies")) {
                    if (recreateFixture)
                        recreateFixture = false;
                    else
                        oPersonaje.touchFloor();

                }
            } else if (oOtraCosa instanceof Item) {
                Item obj = (Item) oOtraCosa;
                if (obj.state == Item.STATE_NORMAL) {
                    if (obj instanceof ItemMoneda) {
                        monedasTomadas++;
                        puntuacion++;
                        Assets.playSound(Assets.coin, 1);
                    } else if (obj instanceof ItemMagnet) {
                        oPersonaje.setPickUpMagnet();
                    } else if (obj instanceof ItemEnergy) {
                        //todo oPersonaje.shield++;
                    } else if (obj instanceof ItemHearth) {
                        oPersonaje.vidas++;
                    } else if (obj instanceof ItemCandyJelly) {
                        Assets.playSound(Assets.popCandy, 1);
                        puntuacion += 2;
                    } else if (obj instanceof ItemCandyBean) {
                        Assets.playSound(Assets.popCandy, 1);
                        puntuacion += 5;
                    } else if (obj instanceof ItemCandyCorn) {
                        Assets.playSound(Assets.popCandy, 1);
                        puntuacion += 15;
                    }

                    obj.setPicked();
                }
            } else if (oOtraCosa instanceof Pared) {
                Pared obj = (Pared) oOtraCosa;
                if (obj.state == Pared.STATE_NORMAL) {
                    oPersonaje.getDizzy();
                }
            } else if (oOtraCosa instanceof Obstaculo) {
                Obstaculo obj = (Obstaculo) oOtraCosa;
                if (obj.state == Obstaculo.STATE_NORMAL) {
                    obj.setDestroy();
                    oPersonaje.getHurt();
                }
            } else if (oOtraCosa instanceof Missil) {
                Missil obj = (Missil) oOtraCosa;
                if (obj.state == Obstaculo.STATE_NORMAL) {
                    obj.setHitTarget();
                    oPersonaje.getDizzy();
                }
            }

        }

        public void beginContactMascotaOtraCosa(Fixture otraCosa) {
            Object oOtraCosa = otraCosa.getBody().getUserData();

            if (oOtraCosa instanceof Pared && oPersonaje.isDash) {
                Pared obj = (Pared) oOtraCosa;
                obj.setDestroy();
            } else if (oOtraCosa instanceof Obstaculo && oPersonaje.isDash) {
                Obstaculo obj = (Obstaculo) oOtraCosa;
                obj.setDestroy();
            } else if (oOtraCosa instanceof ItemMoneda) {
                ItemMoneda obj = (ItemMoneda) oOtraCosa;
                if (obj.state == ItemMoneda.STATE_NORMAL) {
                    obj.setPicked();
                    monedasTomadas++;
                    Assets.playSound(Assets.coin, 1);
                }
            } else if (oOtraCosa instanceof Missil) {
                Missil obj = (Missil) oOtraCosa;
                if (obj.state == Obstaculo.STATE_NORMAL) {
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

            if (a.getBody().getUserData() instanceof Personaje)
                endContactHeroOtraCosa(a, b);
            else if (b.getBody().getUserData() instanceof Personaje)
                endContactHeroOtraCosa(b, a);

        }

        private void endContactHeroOtraCosa(Fixture fixHero, Fixture otraCosa) {
            Object oOtraCosa = otraCosa.getBody().getUserData();

            if (oOtraCosa instanceof Plataforma) {
                if (fixHero.getUserData().equals("pies"))
                    oPersonaje.endTouchFloor();

            }
        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {
            Fixture a = contact.getFixtureA();
            Fixture b = contact.getFixtureB();

            if (a.getBody().getUserData() instanceof Personaje)
                preSolveHero(a, b, contact);
            else if (b.getBody().getUserData() instanceof Personaje)
                preSolveHero(b, a, contact);

        }

        private void preSolveHero(Fixture fixHero, Fixture otraCosa, Contact contact) {
            Object oOtraCosa = otraCosa.getBody().getUserData();

            if (oOtraCosa instanceof Plataforma) {
                Plataforma obj = (Plataforma) oOtraCosa;

                float ponyY = fixHero.getBody().getPosition().y - .30f;
                float pisY = obj.position.y + Plataforma.HEIGHT / 2f;

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
