package com.nopalsoft.slamthebird.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.nopalsoft.slamthebird.Assets;
import com.nopalsoft.slamthebird.objects.Boost;
import com.nopalsoft.slamthebird.objects.Robot;
import com.nopalsoft.slamthebird.objects.Coin;
import com.nopalsoft.slamthebird.screens.Screens;
import com.nopalsoft.slamthebird.objects.Enemy;

public class WorldGameRender {

    final float WIDTH = Screens.WORLD_SCREEN_WIDTH;
    final float HEIGHT = Screens.WORLD_SCREEN_HEIGHT;

    SpriteBatch batcher;
    WorldGame worldGame;

    OrthographicCamera camera;

    Box2DDebugRenderer renderBox;

    public WorldGameRender(SpriteBatch batcher, WorldGame worldGame) {

        this.camera = new OrthographicCamera(WIDTH, HEIGHT);
        this.camera.position.set(WIDTH / 2f, HEIGHT / 2f, 0);

        this.batcher = batcher;
        this.worldGame = worldGame;
        this.renderBox = new Box2DDebugRenderer();
    }

    public void render() {

        camera.position.y = worldGame.robot.position.y;

        if (camera.position.y < HEIGHT / 2f)
            camera.position.y = HEIGHT / 2f;
        else if (camera.position.y > HEIGHT / 2f + 3)
            camera.position.y = HEIGHT / 2f + 3;

        camera.update();
        batcher.setProjectionMatrix(camera.combined);

        batcher.begin();

        batcher.enableBlending();

        renderBackground();
        renderPlatforms();
        renderBoost();
        renderCoins();
        renderEnemies();
        renderPersonaje();

        batcher.end();

    }

    private void renderBackground() {
        batcher.draw(Assets.background, 0, 0, WIDTH, HEIGHT + 3);
    }

    private void renderPlatforms() {

        for (com.nopalsoft.slamthebird.objects.Platform obj : worldGame.arrayPlatforms) {
            TextureRegion keyFrame = Assets.platform;

            if (obj.state == com.nopalsoft.slamthebird.objects.Platform.STATE_BROKEN) {
                if (obj.stateTime < Assets.breakablePlatform.getAnimationDuration())
                    keyFrame = Assets.breakablePlatform.getKeyFrame(
                            obj.stateTime, false);
                else
                    continue;
            }

            if (obj.state == com.nopalsoft.slamthebird.objects.Platform.STATE_BREAKABLE)
                keyFrame = Assets.breakablePlatform.getKeyFrame(0);

            if (obj.state == com.nopalsoft.slamthebird.objects.Platform.STATE_CHANGING)
                batcher.draw(keyFrame, obj.position.x - .5f,
                        obj.position.y - .1f, .5f, .15f, 1f, .3f,
                        obj.changingScale, obj.changingScale, 0);
            else
                batcher.draw(keyFrame, obj.position.x - .5f,
                        obj.position.y - .15f, 1f, .3f);

            if (obj.state == com.nopalsoft.slamthebird.objects.Platform.STATE_FIRE)
                batcher.draw(Assets.animationPlatformFire.getKeyFrame(
                                obj.stateTime, true), obj.position.x - .5f,
                        obj.position.y + .1f, 1f, .3f);
        }

    }

    private void renderBoost() {

        for (Boost obj : worldGame.arrayBoost) {
            TextureRegion keyFrame;
            switch (obj.type) {
                case Boost.TIPO_COIN_RAIN:
                    keyFrame = Assets.coinRainBoost;
                    break;
                case Boost.TIPO_ICE:
                    keyFrame = Assets.IceBoost;
                    break;

                case Boost.TIPO_SUPERJUMP:
                    keyFrame = Assets.superJumpBoost;
                    break;
                case Boost.TIPO_INVENCIBLE:

                default:
                    keyFrame = Assets.invincibleBoost;
                    break;
            }

            batcher.draw(keyFrame, obj.position.x - .175f,
                    obj.position.y - .15f, .35f, .3f);
        }

    }

    private void renderCoins() {

        for (Coin obj : worldGame.arrayCoins) {
            batcher.draw(Assets.animationCoin.getKeyFrame(obj.stateTime, true),
                    obj.position.x - .15f, obj.position.y - .15f, .3f, .34f);
        }

    }

    public void renderEnemies() {
        for (Enemy obj : worldGame.arrayEnemies) {
            if (obj.state == Enemy.STATE_JUST_APPEAR) {
                batcher.draw(Assets.birdSpawn, obj.position.x - .25f,
                        obj.position.y - .25f, .25f, .25f, .5f, .5f,
                        obj.appearScale, obj.appearScale, 0);
                continue;
            }

            TextureRegion keyFrame;
            if (obj.state == Enemy.STATE_FLYING) {
                if (obj.lives >= 3)
                    keyFrame = Assets.animationRedBirdFlap.getKeyFrame(
                            obj.stateTime, true);
                else
                    keyFrame = Assets.animationBlueBirdFlap.getKeyFrame(
                            obj.stateTime, true);
            } else if (obj.state == Enemy.STATE_EVOLVING) {
                keyFrame = Assets.animationEvolving.getKeyFrame(obj.stateTime, true);
            } else {
                keyFrame = Assets.birdBlue;
            }

            if (obj.speed.x > 0)
                batcher.draw(keyFrame, obj.position.x - .285f,
                        obj.position.y - .21f, .57f, .42f);
            else
                batcher.draw(keyFrame, obj.position.x + .285f,
                        obj.position.y - .21f, -.57f, .42f);

        }
    }

    private void renderPersonaje() {

        Robot obj = worldGame.robot;
        TextureRegion keyFrame;

        if (obj.slam && obj.state == Robot.STATE_FALLING) {
            keyFrame = Assets.animationPlayerSlam.getKeyFrame(obj.stateTime);
            batcher.draw(Assets.slam.getKeyFrame(obj.stateTime, true),
                    obj.position.x - .4f, obj.position.y - .55f, .8f, .5f);
        } else if (obj.state == Robot.STATE_FALLING
                || obj.state == Robot.STATE_JUMPING) {
            keyFrame = Assets.animationPlayerJump
                    .getKeyFrame(obj.stateTime, true);
        } else
            keyFrame = Assets.animationPlayerHit.getKeyFrame(obj.stateTime, true);

        // c

        if (obj.speed.x > .1f)
            batcher.draw(keyFrame, obj.position.x - .3f, obj.position.y - .3f,
                    .3f, .3f, .6f, .6f, 1, 1, obj.angleGrad);
        else if (obj.speed.x < -.1f)
            batcher.draw(keyFrame, obj.position.x + .3f, obj.position.y - .3f,
                    -.3f, .3f, -.6f, .6f, 1, 1, obj.angleGrad);
        else
            batcher.draw(Assets.player, obj.position.x - .3f,
                    obj.position.y - .3f, .3f, .3f, .6f, .6f, 1, 1,
                    obj.angleGrad);

        // TODO el personaje cuando se muere no tiene velocidad por lo que no aparece el keyframe sino que agarra assetspersonaje

        // Esto renderear los boost arriba de la cabeza del personaje
        renderBoostActivo(obj);

    }

    private void renderBoostActivo(Robot obj) {
        if (obj.isInvincible || obj.isSuperJump) {
            float timeToAlert = 2.5f;// Tiempo para que empieze a parpaderar el boost
            TextureRegion boostKeyFrame;
            if (obj.isInvincible) {
                if (obj.DURATION_INVINCIBLE - obj.durationInvincible <= timeToAlert) {
                    boostKeyFrame = Assets.animationInvincibleBoostEnd.getKeyFrame(
                            obj.stateTime, true);// anim
                } else
                    boostKeyFrame = Assets.invincibleBoost;
            } else {// jump
                if (obj.DURATION_SUPER_JUMP - obj.durationSuperJump <= timeToAlert) {
                    boostKeyFrame = Assets.animationSuperJumpBoostEnd.getKeyFrame(
                            obj.stateTime, true);// anim
                } else
                    boostKeyFrame = Assets.superJumpBoost;
            }

            batcher.draw(boostKeyFrame, obj.position.x - .175f,
                    obj.position.y + .3f, .35f, .3f);
        }
    }

}
