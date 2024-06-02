package com.nopalsoft.superjumper.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.nopalsoft.superjumper.Assets;
import com.nopalsoft.superjumper.objects.Bullet;
import com.nopalsoft.superjumper.objects.Cloud;
import com.nopalsoft.superjumper.objects.Coin;
import com.nopalsoft.superjumper.objects.Enemy;
import com.nopalsoft.superjumper.objects.Item;
import com.nopalsoft.superjumper.objects.Lightning;
import com.nopalsoft.superjumper.objects.Platform;
import com.nopalsoft.superjumper.objects.PlatformPiece;
import com.nopalsoft.superjumper.objects.Player;
import com.nopalsoft.superjumper.screens.BasicScreen;

public class WorldGameRender {
    final float WIDTH = BasicScreen.WORLD_WIDTH;
    final float HEIGHT = BasicScreen.WORLD_HEIGHT;

    WorldGame worldGame;
    SpriteBatch batcher;
    OrthographicCamera camera;
    Box2DDebugRenderer boxRender;

    public WorldGameRender(SpriteBatch batcher, WorldGame worldGame) {
        this.worldGame = worldGame;
        this.batcher = batcher;

        camera = new OrthographicCamera(WIDTH, HEIGHT);
        camera.position.set(WIDTH / 2f, HEIGHT / 2f, 0);

        boxRender = new Box2DDebugRenderer();
    }

    public void unprojectToWorldCoords(Vector3 touchPoint) {
        camera.unproject(touchPoint);
    }

    public void render() {
        if (worldGame.state == WorldGame.STATE_RUNNING)
            camera.position.y = worldGame.player.position.y;

        if (camera.position.y < BasicScreen.WORLD_HEIGHT / 2f) {
            camera.position.y = BasicScreen.WORLD_HEIGHT / 2f;
        }

        camera.update();
        batcher.setProjectionMatrix(camera.combined);

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

    }

    private void renderCharacters() {
        AtlasRegion keyframe;

        Player obj = worldGame.player;

        if (obj.speed.y > 0)
            keyframe = Assets.characterJump;
        else
            keyframe = Assets.characterStand;

        if (obj.speed.x > 0)
            batcher.draw(
                    keyframe,
                    obj.position.x + Player.DRAW_WIDTH / 2f,
                    obj.position.y - Player.DRAW_HEIGHT / 2f,
                    -Player.DRAW_WIDTH / 2f,
                    Player.DRAW_HEIGHT / 2f,
                    -Player.DRAW_WIDTH,
                    Player.DRAW_HEIGHT,
                    1,
                    1,
                    obj.angleDeg
            );

        else
            batcher.draw(
                    keyframe,
                    obj.position.x - Player.DRAW_WIDTH / 2f,
                    obj.position.y - Player.DRAW_HEIGHT / 2f,
                    Player.DRAW_WIDTH / 2f,
                    Player.DRAW_HEIGHT / 2f,
                    Player.DRAW_WIDTH,
                    Player.DRAW_HEIGHT,
                    1,
                    1,
                    obj.angleDeg
            );

        if (obj.isJetPack) {
            batcher.draw(
                    Assets.jetpack,
                    obj.position.x - .45f / 2f,
                    obj.position.y - .7f / 2f,
                    .45f,
                    .7f
            );

            TextureRegion fireFrame = Assets.jetpackFire.getKeyFrame(
                    obj.durationJetPack,
                    true
            );
            batcher.draw(
                    fireFrame,
                    obj.position.x - .35f / 2f,
                    obj.position.y - .95f,
                    .35f,
                    .6f
            );

        }
        if (obj.isBubble) {
            batcher.draw(
                    Assets.bubble,
                    obj.position.x - .5f,
                    obj.position.y - .5f,
                    1,
                    1
            );
        }

    }

    private void renderPlatforms() {
        for (Platform obj : worldGame.arrayPlatforms) {
            AtlasRegion keyframe = null;

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
        for (PlatformPiece obj : worldGame.arrayPlatformPieces) {
            AtlasRegion keyframe = null;

            if (obj.type == PlatformPiece.TYPE_LEFT) {
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
                    PlatformPiece.DRAW_HEIGHT_NORMAL, 1, 1, obj.angleDegree);

        }
    }

    private void renderCoins() {
        for (Coin obj : worldGame.arrayCoins) {
            batcher.draw(Assets.coin, obj.position.x - Coin.DRAW_WIDTH / 2f, obj.position.y - Coin.DRAW_HEIGHT / 2f, Coin.DRAW_WIDTH,
                    Coin.DRAW_HEIGHT);
        }

    }

    private void renderItems() {
        for (Item obj : worldGame.arrayItem) {
            TextureRegion keyframe = null;

            switch (obj.type) {
                case Item.TYPE_BUBBLE:
                    keyframe = Assets.bubbleSmall;
                    break;
                case Item.TYPE_JETPACK:
                    keyframe = Assets.jetpackSmall;
                    break;
                case Item.TYPE_GUN:
                    keyframe = Assets.gun;
                    break;

            }

            batcher.draw(keyframe, obj.position.x - Item.DRAW_WIDTH / 2f, obj.position.y - Item.DRAW_HEIGHT / 2f, Item.DRAW_WIDTH, Item.DRAW_HEIGHT);

        }

    }

    private void renderEnemy() {
        for (Enemy obj : worldGame.arrayEnemies) {
            TextureRegion keyframe = Assets.enemy.getKeyFrame(obj.stateTime, true);

            batcher.draw(keyframe, obj.position.x - Enemy.DRAW_WIDTH / 2f, obj.position.y - Enemy.DRAW_HEIGHT / 2f, Enemy.DRAW_WIDTH,
                    Enemy.DRAW_HEIGHT);
        }

    }

    private void renderCloud() {
        for (Cloud obj : worldGame.arrayClouds) {
            TextureRegion keyframe = null;

            switch (obj.guy) {
                case Cloud.TYPE_ANGRY:
                    keyframe = Assets.cloudAngry;
                    break;
                case Cloud.TYPE_HAPPY:
                    keyframe = Assets.cloudHappy;
                    break;

            }

            batcher.draw(keyframe, obj.position.x - Cloud.DRAW_WIDTH / 2f, obj.position.y - Cloud.DRAW_HEIGHT / 2f, Cloud.DRAW_WIDTH, Cloud.DRAW_HEIGHT);

            if (obj.isBlowing) {
                batcher.draw(Assets.cloudWind, obj.position.x - .35f, obj.position.y - .85f, .6f, .8f);
            }
        }

    }

    private void renderRay() {
        for (Lightning obj : worldGame.arrayRays) {
            TextureRegion keyframe = Assets.ray.getKeyFrame(obj.stateTime, true);

            batcher.draw(keyframe, obj.position.x - Lightning.DRAW_WIDTH / 2f, obj.position.y - Lightning.DRAW_HEIGHT / 2f, Lightning.DRAW_WIDTH, Lightning.DRAW_HEIGHT);
        }
    }

    private void renderBullet() {
        for (Bullet obj : worldGame.arrayBullets) {
            batcher.draw(Assets.bullet, obj.position.x - Bullet.SIZE / 2f, obj.position.y - Bullet.SIZE / 2f, Bullet.SIZE, Bullet.SIZE);
        }
    }

}
