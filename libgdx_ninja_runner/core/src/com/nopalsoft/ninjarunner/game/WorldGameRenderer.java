package com.nopalsoft.ninjarunner.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.nopalsoft.ninjarunner.AnimationSprite;
import com.nopalsoft.ninjarunner.Assets;
import com.nopalsoft.ninjarunner.objetos.Mascota;
import com.nopalsoft.ninjarunner.objetos.Personaje;
import com.nopalsoft.ninjarunner.screens.Screens;

public class WorldGameRenderer {
    final float WIDTH = Screens.WORLD_WIDTH;
    final float HEIGHT = Screens.WORLD_HEIGHT;

    SpriteBatch batcher;
    WorldGame oWorld;
    OrthographicCamera oCam;

    Box2DDebugRenderer renderBox;

    public WorldGameRenderer(SpriteBatch batcher, WorldGame oWorld) {

        this.oCam = new OrthographicCamera(WIDTH, HEIGHT);
        this.oCam.position.set(WIDTH / 2f, HEIGHT / 2f, 0);
        this.batcher = batcher;
        this.oWorld = oWorld;
        this.renderBox = new Box2DDebugRenderer();

    }

    public void render(float delta) {
        // oCam.zoom = 10;

        oCam.position.set(oWorld.oPersonaje.position.x + 1.5f, oWorld.oPersonaje.position.y, 0);

        if (oCam.position.y < HEIGHT / 2f)
            oCam.position.y = HEIGHT / 2f;
        else if (oCam.position.y > HEIGHT / 2f)
            oCam.position.y = HEIGHT / 2f;

        if (oCam.position.x < WIDTH / 2f)
            oCam.position.x = WIDTH / 2f;

        oCam.update();
        batcher.setProjectionMatrix(oCam.combined);
        batcher.begin();
        batcher.enableBlending();

        renderPlataformas();
        renderPared();

        renderItems();

        renderPersonaje();
        renderMascota();

        renderObstaculos(delta);
        renderMissil();

        batcher.end();

        // renderBox.render(oWorld.oWorldBox, oCam.combined);
    }

    private void renderItems() {

        for (com.nopalsoft.ninjarunner.objetos.Item obj : oWorld.arrItems) {
            com.badlogic.gdx.graphics.g2d.Sprite spriteFrame = null;

            if (obj.state == com.nopalsoft.ninjarunner.objetos.ItemMoneda.STATE_NORMAL) {
                if (obj instanceof com.nopalsoft.ninjarunner.objetos.ItemMoneda) {
                    spriteFrame = com.nopalsoft.ninjarunner.Assets.moneda.getKeyFrame(obj.stateTime, true);
                } else if (obj instanceof com.nopalsoft.ninjarunner.objetos.ItemMagnet) {
                    spriteFrame = com.nopalsoft.ninjarunner.Assets.magnet;
                } else if (obj instanceof com.nopalsoft.ninjarunner.objetos.ItemEnergy) {
                    spriteFrame = com.nopalsoft.ninjarunner.Assets.energy;
                } else if (obj instanceof com.nopalsoft.ninjarunner.objetos.ItemHearth) {
                    spriteFrame = com.nopalsoft.ninjarunner.Assets.hearth;
                } else if (obj instanceof com.nopalsoft.ninjarunner.objetos.ItemCandyJelly) {
                    spriteFrame = com.nopalsoft.ninjarunner.Assets.jellyRed;
                } else if (obj instanceof com.nopalsoft.ninjarunner.objetos.ItemCandyBean) {
                    spriteFrame = com.nopalsoft.ninjarunner.Assets.beanRed;
                } else if (obj instanceof com.nopalsoft.ninjarunner.objetos.ItemCandyCorn) {
                    spriteFrame = com.nopalsoft.ninjarunner.Assets.candyCorn;
                }

            } else {
                if (obj instanceof com.nopalsoft.ninjarunner.objetos.ItemCandyJelly) {
                    spriteFrame = com.nopalsoft.ninjarunner.Assets.candyExplosionRed.getKeyFrame(obj.stateTime, false);
                } else if (obj instanceof com.nopalsoft.ninjarunner.objetos.ItemCandyBean) {
                    spriteFrame = com.nopalsoft.ninjarunner.Assets.candyExplosionRed.getKeyFrame(obj.stateTime, false);
                } else {
                    spriteFrame = com.nopalsoft.ninjarunner.Assets.pick.getKeyFrame(obj.stateTime, false);
                }
            }

            if (spriteFrame != null) {
                spriteFrame.setPosition(obj.position.x - obj.WIDTH / 2f, obj.position.y - obj.HEIGHT / 2f);
                spriteFrame.setSize(obj.WIDTH, obj.HEIGHT);
                spriteFrame.draw(batcher);
            }

        }

    }

    private void renderPlataformas() {

        for (com.nopalsoft.ninjarunner.objetos.Plataforma obj : oWorld.arrPlataformas) {
            com.badlogic.gdx.graphics.g2d.Sprite spriteFrame;

            spriteFrame = com.nopalsoft.ninjarunner.Assets.plataforma;

            spriteFrame.setPosition(obj.position.x - com.nopalsoft.ninjarunner.objetos.Plataforma.WIDTH / 2f, obj.position.y - com.nopalsoft.ninjarunner.objetos.Plataforma.HEIGHT / 2f);
            spriteFrame.setSize(com.nopalsoft.ninjarunner.objetos.Plataforma.WIDTH, com.nopalsoft.ninjarunner.objetos.Plataforma.HEIGHT);
            spriteFrame.draw(batcher);
        }

    }

    private void renderMascota() {
        Mascota oMas = oWorld.oMascota;

        Sprite spriteFrame;

        float width = oMas.drawWidth;
        float height = oMas.drawHeight;

        if (oMas.tipo == Mascota.Tipo.BOMBA) {
            spriteFrame = Assets.MascotaBombFly.getKeyFrame(oMas.stateTime, true);
        } else {
            if (oWorld.oPersonaje.isDash) {
                spriteFrame = Assets.Mascota1Dash.getKeyFrame(oMas.stateTime, true);
                width = oMas.dashDrawWidth;
                height = oMas.dashDrawHeight;
            } else
                spriteFrame = Assets.Mascota1Fly.getKeyFrame(oMas.stateTime, true);
        }

        spriteFrame.setPosition(oMas.position.x - width + Mascota.RADIUS, oWorld.oMascota.position.y - height / 2f);
        spriteFrame.setSize(width, height);
        spriteFrame.draw(batcher);
    }

    private void renderPared() {

        for (com.nopalsoft.ninjarunner.objetos.Pared obj : oWorld.arrPared) {
            com.badlogic.gdx.graphics.g2d.Sprite spriteFrame = com.nopalsoft.ninjarunner.Assets.pared;
            spriteFrame.setPosition(obj.position.x - com.nopalsoft.ninjarunner.objetos.Pared.WIDTH / 2f, obj.position.y - com.nopalsoft.ninjarunner.objetos.Pared.HEIGHT / 2f);
            spriteFrame.setSize(com.nopalsoft.ninjarunner.objetos.Pared.WIDTH, com.nopalsoft.ninjarunner.objetos.Pared.HEIGHT);
            spriteFrame.draw(batcher);
        }

    }

    private void renderObstaculos(float delta) {
        for (com.nopalsoft.ninjarunner.objetos.Obstaculo obj : oWorld.arrObstaculos) {
            if (obj.state == com.nopalsoft.ninjarunner.objetos.Obstaculo.STATE_NORMAL) {

                float width, height;
                com.badlogic.gdx.graphics.g2d.Sprite spriteFrame;

                if (obj instanceof com.nopalsoft.ninjarunner.objetos.ObstaculoCajas4) {
                    width = com.nopalsoft.ninjarunner.objetos.ObstaculoCajas4.DRAW_WIDTH;
                    height = com.nopalsoft.ninjarunner.objetos.ObstaculoCajas4.DRAW_HEIGHT;
                    spriteFrame = com.nopalsoft.ninjarunner.Assets.cajas4;

                } else {
                    width = com.nopalsoft.ninjarunner.objetos.ObstaculoCajas7.DRAW_WIDTH;
                    height = com.nopalsoft.ninjarunner.objetos.ObstaculoCajas7.DRAW_HEIGHT;
                    spriteFrame = com.nopalsoft.ninjarunner.Assets.cajas7;
                }
                spriteFrame.setPosition(obj.position.x - width / 2f, obj.position.y - height / 2f);
                spriteFrame.setSize(width, height);
                spriteFrame.draw(batcher);

            } else {

                obj.effect.draw(batcher, delta);

            }
        }

    }

    private void renderMissil() {
        for (com.nopalsoft.ninjarunner.objetos.Missil obj : oWorld.arrMissiles) {
            com.badlogic.gdx.graphics.g2d.Sprite spriteFrame;
            float width, height;

            if (obj.state == com.nopalsoft.ninjarunner.objetos.Missil.STATE_NORMAL) {
                width = com.nopalsoft.ninjarunner.objetos.Missil.WIDTH;
                height = com.nopalsoft.ninjarunner.objetos.Missil.HEIGHT;
                spriteFrame = com.nopalsoft.ninjarunner.Assets.missil.getKeyFrame(obj.stateTime, true);
            } else if (obj.state == com.nopalsoft.ninjarunner.objetos.Missil.STATE_EXPLODE) {
                width = 1f;
                height = .84f;
                spriteFrame = com.nopalsoft.ninjarunner.Assets.explosion.getKeyFrame(obj.stateTime, false);
            } else
                continue;

            spriteFrame.setPosition(obj.position.x - width / 2f, obj.position.y - height / 2f);
            spriteFrame.setSize(width, height);
            spriteFrame.draw(batcher);
        }

    }

    private void renderPersonaje() {
        Personaje oPer = oWorld.oPersonaje;

        Sprite spriteFrame;
        float offsetY;

        AnimationSprite animIdle;
        AnimationSprite animJump;
        AnimationSprite animRun;
        AnimationSprite animSlide;
        AnimationSprite animDash;
        AnimationSprite animHurt;
        AnimationSprite animDizzy;
        AnimationSprite animDead;

        switch (oPer.tipo) {
            case Personaje.TIPO_GIRL:
            case Personaje.TIPO_BOY:
                animIdle = Assets.personajeIdle;
                animJump = Assets.personajeJump;
                animRun = Assets.personajeRun;
                animSlide = Assets.personajeSlide;
                animDash = Assets.personajeDash;
                animHurt = Assets.personajeHurt;
                animDizzy = Assets.personajeDizzy;
                animDead = Assets.personajeDead;
                break;
            case Personaje.TIPO_NINJA:
            default:
                animIdle = Assets.ninjaIdle;
                animJump = Assets.ninjaJump;
                animRun = Assets.ninjaRun;
                animSlide = Assets.ninjaSlide;
                animDash = Assets.ninjaDash;
                animHurt = Assets.ninjaHurt;
                animDizzy = Assets.ninjaDizzy;
                animDead = Assets.ninjaDead;
                break;
        }

        if (oPer.state == Personaje.STATE_NORMAL) {

            if (oPer.isIdle) {
                spriteFrame = animIdle.getKeyFrame(oPer.stateTime, true);

            } else if (oPer.isJumping) {
                spriteFrame = animJump.getKeyFrame(oPer.stateTime, false);
            } else if (oPer.isSlide) {
                spriteFrame = animSlide.getKeyFrame(oPer.stateTime, true);

            } else if (oPer.isDash) {
                spriteFrame = animDash.getKeyFrame(oPer.stateTime, true);
            } else {
                spriteFrame = animRun.getKeyFrame(oPer.stateTime, true);
            }
            offsetY = .1f;
        } else if (oPer.state == Personaje.STATE_HURT) {
            spriteFrame = animHurt.getKeyFrame(oPer.stateTime, false);
            offsetY = .1f;

        } else if (oPer.state == Personaje.STATE_DIZZY) {
            spriteFrame = animDizzy.getKeyFrame(oPer.stateTime, true);
            offsetY = .1f;

        } else {
            spriteFrame = animDead.getKeyFrame(oPer.stateTime, false);
            offsetY = .1f;
        }

        spriteFrame.setPosition(oWorld.oPersonaje.position.x - .75f, oWorld.oPersonaje.position.y - .52f - offsetY);
        spriteFrame.setSize(Personaje.DRAW_WIDTH, Personaje.DRAW_HEIGHT);
        spriteFrame.draw(batcher);

    }

}
