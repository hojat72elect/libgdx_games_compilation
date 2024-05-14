package com.nopalsoft.superjumper.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.nopalsoft.superjumper.Assets;
import com.nopalsoft.superjumper.objects.Character;
import com.nopalsoft.superjumper.objects.Platform;
import com.nopalsoft.superjumper.screens.Screens;
import com.nopalsoft.superjumper.objects.PlatformPiece;
import com.nopalsoft.superjumper.objects.Coin;
import com.nopalsoft.superjumper.objects.Item;

public class WorldGameRender {
    final float WIDTH = Screens.WORLD_WIDTH;
    final float HEIGHT = Screens.WORLD_HEIGHT;

    WorldGame oWorld;
    SpriteBatch batcher;
    OrthographicCamera oCam;
    Box2DDebugRenderer boxRender;

    public WorldGameRender(SpriteBatch batcher, WorldGame oWorld) {
        this.oWorld = oWorld;
        this.batcher = batcher;

        oCam = new OrthographicCamera(WIDTH, HEIGHT);
        oCam.position.set(WIDTH / 2f, HEIGHT / 2f, 0);

        boxRender = new Box2DDebugRenderer();
    }

    public void unprojectToWorldCoords(Vector3 touchPoint) {
        oCam.unproject(touchPoint);
    }

    public void render() {
        if (oWorld.state == WorldGame.STATE_RUNNING)
            oCam.position.y = oWorld.oPer.position.y;

        if (oCam.position.y < Screens.WORLD_HEIGHT / 2f) {
            oCam.position.y = Screens.WORLD_HEIGHT / 2f;
        }

        oCam.update();
        batcher.setProjectionMatrix(oCam.combined);

        batcher.begin();

        renderCharacters();
        renderPlatforms();
        renderPlatformPieces();
        renderCoins();
        renderItems();
        renderEnemy();
        renderCloud();
        renderRay();
        renderBullet();

        batcher.end();

        // boxRender.render(oWorld.oWorldBox, oCam.combined);

    }

    private void renderCharacters() {
        AtlasRegion keyframe;

        com.nopalsoft.superjumper.objects.Character obj = oWorld.oPer;

        if (obj.speed.y > 0)
            keyframe = Assets.characterJump;
        else
            keyframe = Assets.characterStand;

        if (obj.speed.x > 0)
            batcher.draw(keyframe, obj.position.x + Character.DRAW_WIDTH / 2f, obj.position.y - Character.DRAW_HEIGTH / 2f,
                    -Character.DRAW_WIDTH / 2f, Character.DRAW_HEIGTH / 2f, -Character.DRAW_WIDTH, com.nopalsoft.superjumper.objects.Character.DRAW_HEIGTH, 1, 1, obj.angleDeg);

        else
            batcher.draw(
                    keyframe,
                    obj.position.x - Character.DRAW_WIDTH / 2f,
                    obj.position.y - Character.DRAW_HEIGTH / 2f,
                    Character.DRAW_WIDTH / 2f,
                    Character.DRAW_HEIGTH / 2f,
                    Character.DRAW_WIDTH,
                    Character.DRAW_HEIGTH,
                    1,
                    1,
                    obj.angleDeg
            );

        if (obj.isJetPack) {
            batcher.draw(Assets.jetpack, obj.position.x - .45f / 2f, obj.position.y - .7f / 2f, .45f, .7f);

            TextureRegion fireFrame = Assets.jetpackFire.getKeyFrame(obj.durationJetPack, true);
            batcher.draw(fireFrame, obj.position.x - .35f / 2f, obj.position.y - .95f, .35f, .6f);

        }
        if (obj.isBubble) {
            batcher.draw(Assets.bubble, obj.position.x - .5f, obj.position.y - .5f, 1, 1);
        }

    }

    private void renderPlatforms() {
        for (Platform obj : oWorld.arrPlataformas) {
            com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion keyframe = null;

            if (obj.type == Platform.TYPE_BREAKABLE) {
                switch (obj.color) {
                    case Platform.COLOR_BEIGE:
                        keyframe = Assets.platformBeigeBroken;
                        break;
                    case Platform.COLOR_BLUE:
                        keyframe = Assets.platformBlueBroken;
                        break;
                    case Platform.COLOR_GRAY:
                        keyframe = Assets.platformGrayBroken;
                        break;
                    case Platform.COLOR_GREEN:
                        keyframe = Assets.platformGreenBroken;
                        break;
                    case Platform.COLOR_MULTICOLOR:
                        keyframe = Assets.platformMulticolorBroken;
                        break;
                    case Platform.COLOR_PINK:
                        keyframe = Assets.platformPinkBroken;
                        break;

                }
            } else {
                switch (obj.color) {
                    case Platform.COLOR_BEIGE:
                        keyframe = Assets.platformBeige;
                        break;
                    case Platform.COLOR_BLUE:
                        keyframe = Assets.platformBlue;
                        break;
                    case Platform.COLOR_GRAY:
                        keyframe = Assets.platformGray;
                        break;
                    case Platform.COLOR_GREEN:
                        keyframe = Assets.platformGreen;
                        break;
                    case Platform.COLOR_MULTICOLOR:
                        keyframe = Assets.platformMulticolor;
                        break;
                    case Platform.COLOR_PINK:
                        keyframe = Assets.platformPink;
                        break;
                    case Platform.COLOR_BEIGE_LIGHT:
                        keyframe = Assets.platformBeigeLight;
                        break;
                    case Platform.COLOR_BLUE_LIGHT:
                        keyframe = Assets.platformBlueLight;
                        break;
                    case Platform.COLOR_GRAY_LIGHT:
                        keyframe = Assets.platformGrayLight;
                        break;
                    case Platform.COLOR_GREEN_LIGHT:
                        keyframe = Assets.platformGreenLight;
                        break;
                    case Platform.COLOR_MULTICOLOR_LIGHT:
                        keyframe = Assets.platformMulticolorLight;
                        break;
                    case Platform.COLOR_PINK_LIGHT:
                        keyframe = Assets.platformPinkLight;
                        break;
                }

            }
            batcher.draw(keyframe, obj.position.x - Platform.DRAW_WIDTH_NORMAL / 2f, obj.position.y - Platform.DRAW_HEIGHT_NORMAL / 2f,
                    Platform.DRAW_WIDTH_NORMAL, Platform.DRAW_HEIGHT_NORMAL);
        }
    }

    private void renderPlatformPieces() {
        for (com.nopalsoft.superjumper.objects.PlatformPiece obj : oWorld.arrPiezasPlataformas) {
            com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion keyframe = null;

            if (obj.type == com.nopalsoft.superjumper.objects.PlatformPiece.TYPE_LEFT) {
                switch (obj.color) {
                    case Platform.COLOR_BEIGE:
                        keyframe = Assets.platformBeigeLeft;
                        break;
                    case Platform.COLOR_BLUE:
                        keyframe = Assets.platformBlueLeft;
                        break;
                    case Platform.COLOR_GRAY:
                        keyframe = Assets.platformGrayLeft;
                        break;
                    case Platform.COLOR_GREEN:
                        keyframe = Assets.platformGreenLeft;
                        break;
                    case Platform.COLOR_MULTICOLOR:
                        keyframe = Assets.platformMulticolorLeft;
                        break;
                    case Platform.COLOR_PINK:
                        keyframe = Assets.platformPinkLeft;
                        break;

                }
            } else {
                switch (obj.color) {
                    case Platform.COLOR_BEIGE:
                        keyframe = Assets.platformBeigeRight;
                        break;
                    case Platform.COLOR_BLUE:
                        keyframe = Assets.platformBlueRight;
                        break;
                    case Platform.COLOR_GRAY:
                        keyframe = Assets.platformGrayRight;
                        break;
                    case Platform.COLOR_GREEN:
                        keyframe = Assets.platformGreenRight;
                        break;
                    case Platform.COLOR_MULTICOLOR:
                        keyframe = Assets.platformMulticolorRight;
                        break;
                    case Platform.COLOR_PINK:
                        keyframe = Assets.platformPinkRight;
                        break;

                }
            }

            batcher.draw(keyframe, obj.position.x - PlatformPiece.DRAW_WIDTH_NORMAL / 2f, obj.position.y - PlatformPiece.DRAW_HEIGHT_NORMAL
                            / 2f, PlatformPiece.DRAW_WIDTH_NORMAL / 2f, PlatformPiece.DRAW_HEIGHT_NORMAL / 2f, PlatformPiece.DRAW_WIDTH_NORMAL,
                    com.nopalsoft.superjumper.objects.PlatformPiece.DRAW_HEIGHT_NORMAL, 1, 1, obj.angleDegree);

        }
    }

    private void renderCoins() {
        for (Coin obj : oWorld.arrMonedas) {
            batcher.draw(Assets.coin, obj.position.x - Coin.DRAW_WIDTH / 2f, obj.position.y - Coin.DRAW_HEIGHT / 2f, Coin.DRAW_WIDTH,
                    Coin.DRAW_HEIGHT);
        }

    }

    private void renderItems() {
        for (Item obj : oWorld.arrItem) {
            com.badlogic.gdx.graphics.g2d.TextureRegion keyframe = null;

            switch (obj.type) {
                case com.nopalsoft.superjumper.objects.Item.TYPE_BUBBLE:
                    keyframe = com.nopalsoft.superjumper.Assets.bubbleSmall;
                    break;
                case com.nopalsoft.superjumper.objects.Item.TYPE_JETPACK:
                    keyframe = com.nopalsoft.superjumper.Assets.jetpackSmall;
                    break;
                case com.nopalsoft.superjumper.objects.Item.TYPE_GUN:
                    keyframe = com.nopalsoft.superjumper.Assets.gun;
                    break;

            }

            batcher.draw(keyframe, obj.position.x - com.nopalsoft.superjumper.objects.Item.DRAW_WIDTH / 2f, obj.position.y - com.nopalsoft.superjumper.objects.Item.DRAW_HEIGHT / 2f, com.nopalsoft.superjumper.objects.Item.DRAW_WIDTH, com.nopalsoft.superjumper.objects.Item.DRAW_HEIGHT);

        }

    }

    private void renderEnemy() {
        for (com.nopalsoft.superjumper.objects.Enemy obj : oWorld.arrEnemigo) {
            com.badlogic.gdx.graphics.g2d.TextureRegion keyframe = com.nopalsoft.superjumper.Assets.enemy.getKeyFrame(obj.stateTime, true);

            batcher.draw(keyframe, obj.position.x - com.nopalsoft.superjumper.objects.Enemy.DRAW_WIDTH / 2f, obj.position.y - com.nopalsoft.superjumper.objects.Enemy.DRAW_HEIGHT / 2f, com.nopalsoft.superjumper.objects.Enemy.DRAW_WIDTH,
                    com.nopalsoft.superjumper.objects.Enemy.DRAW_HEIGHT);
        }

    }

    private void renderCloud() {
        for (com.nopalsoft.superjumper.objects.Cloud obj : oWorld.arrNubes) {
            com.badlogic.gdx.graphics.g2d.TextureRegion keyframe = null;

            switch (obj.guy) {
                case com.nopalsoft.superjumper.objects.Cloud.TIPO_ANGRY:
                    keyframe = com.nopalsoft.superjumper.Assets.cloudAngry;
                    break;
                case com.nopalsoft.superjumper.objects.Cloud.TIPO_HAPPY:
                    keyframe = com.nopalsoft.superjumper.Assets.cloudHappy;
                    break;

            }

            batcher.draw(keyframe, obj.position.x - com.nopalsoft.superjumper.objects.Cloud.DRAW_WIDTH / 2f, obj.position.y - com.nopalsoft.superjumper.objects.Cloud.DRAW_HEIGHT / 2f, com.nopalsoft.superjumper.objects.Cloud.DRAW_WIDTH, com.nopalsoft.superjumper.objects.Cloud.DRAW_HEIGHT);

            if (obj.isBlowing) {
                batcher.draw(com.nopalsoft.superjumper.Assets.cloudWind, obj.position.x - .35f, obj.position.y - .85f, .6f, .8f);
            }
        }

    }

    private void renderRay() {
        for (com.nopalsoft.superjumper.objects.Ray obj : oWorld.arrRayos) {
            com.badlogic.gdx.graphics.g2d.TextureRegion keyframe = com.nopalsoft.superjumper.Assets.ray.getKeyFrame(obj.stateTime, true);

            batcher.draw(keyframe, obj.position.x - com.nopalsoft.superjumper.objects.Ray.DRAW_WIDTH / 2f, obj.position.y - com.nopalsoft.superjumper.objects.Ray.DRAW_HEIGHT / 2f, com.nopalsoft.superjumper.objects.Ray.DRAW_WIDTH, com.nopalsoft.superjumper.objects.Ray.DRAW_HEIGHT);
        }
    }

    private void renderBullet() {
        for (com.nopalsoft.superjumper.objects.Bullet obj : oWorld.arrBullets) {
            batcher.draw(com.nopalsoft.superjumper.Assets.bullet, obj.position.x - com.nopalsoft.superjumper.objects.Bullet.SIZE / 2f, obj.position.y - com.nopalsoft.superjumper.objects.Bullet.SIZE / 2f, com.nopalsoft.superjumper.objects.Bullet.SIZE, com.nopalsoft.superjumper.objects.Bullet.SIZE);
        }
    }

}
