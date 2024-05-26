package com.nopalsoft.ninjarunner.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.nopalsoft.ninjarunner.AnimationSprite;
import com.nopalsoft.ninjarunner.Assets;
import com.nopalsoft.ninjarunner.objects.ItemCandyBean;
import com.nopalsoft.ninjarunner.objects.ItemCandyCorn;
import com.nopalsoft.ninjarunner.objects.ItemCandyJelly;
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
import com.nopalsoft.ninjarunner.screens.Screens;
import com.nopalsoft.ninjarunner.objects.Item;
import com.nopalsoft.ninjarunner.objects.ItemCurrency;


public class WorldGameRenderer {
    final float WIDTH = Screens.WORLD_WIDTH;
    final float HEIGHT = Screens.WORLD_HEIGHT;

    SpriteBatch batcher;
    WorldGame myWorld;
    OrthographicCamera myCamera;

    Box2DDebugRenderer renderBox;

    public WorldGameRenderer(SpriteBatch batcher, WorldGame myWorld) {

        this.myCamera = new OrthographicCamera(WIDTH, HEIGHT);
        this.myCamera.position.set(WIDTH / 2f, HEIGHT / 2f, 0);
        this.batcher = batcher;
        this.myWorld = myWorld;
        this.renderBox = new Box2DDebugRenderer();

    }

    public void render(float delta) {

        myCamera.position.set(myWorld.myPlayer.position.x + 1.5f, myWorld.myPlayer.position.y, 0);

        if (myCamera.position.y < HEIGHT / 2f)
            myCamera.position.y = HEIGHT / 2f;
        else if (myCamera.position.y > HEIGHT / 2f)
            myCamera.position.y = HEIGHT / 2f;

        if (myCamera.position.x < WIDTH / 2f)
            myCamera.position.x = WIDTH / 2f;

        myCamera.update();
        batcher.setProjectionMatrix(myCamera.combined);
        batcher.begin();
        batcher.enableBlending();

        renderPlatforms();
        renderWall();

        renderItems();

        renderPlayer();
        renderPet();

        renderObstacles(delta);
        renderMissile();

        batcher.end();

    }

    private void renderItems() {

        for (Item obj : myWorld.arrayItems) {
            Sprite spriteFrame = null;

            if (obj.state == ItemCurrency.STATE_NORMAL) {
                if (obj instanceof ItemCurrency) {
                    spriteFrame = Assets.coinAnimation.getKeyFrame(obj.stateTime, true);
                } else if (obj instanceof ItemMagnet) {
                    spriteFrame = Assets.magnet;
                } else if (obj instanceof ItemEnergy) {
                    spriteFrame = Assets.energy;
                } else if (obj instanceof ItemHearth) {
                    spriteFrame = Assets.hearth;
                } else if (obj instanceof ItemCandyJelly) {
                    spriteFrame = Assets.jellyRed;
                } else if (obj instanceof ItemCandyBean) {
                    spriteFrame = Assets.beanRed;
                } else if (obj instanceof ItemCandyCorn) {
                    spriteFrame = Assets.candyCorn;
                }

            } else {
                if (obj instanceof ItemCandyJelly) {
                    spriteFrame = Assets.candyExplosionRed.getKeyFrame(obj.stateTime, false);
                } else if (obj instanceof ItemCandyBean) {
                    spriteFrame = Assets.candyExplosionRed.getKeyFrame(obj.stateTime, false);
                } else {
                    spriteFrame = Assets.pickAnimation.getKeyFrame(obj.stateTime, false);
                }
            }

            if (spriteFrame != null) {
                spriteFrame.setPosition(obj.position.x - obj.width / 2f, obj.position.y - obj.height / 2f);
                spriteFrame.setSize(obj.width, obj.height);
                spriteFrame.draw(batcher);
            }

        }

    }

    private void renderPlatforms() {

        for (Platform obj : myWorld.arrayPlatforms) {
            Sprite spriteFrame;

            spriteFrame = Assets.platform;

            spriteFrame.setPosition(
                    obj.position.x - Platform.WIDTH / 2f,
                    obj.position.y - Platform.HEIGHT / 2f
            );
            spriteFrame.setSize(Platform.WIDTH, Platform.HEIGHT);
            spriteFrame.draw(batcher);
        }

    }

    private void renderPet() {
        Pet myPet = myWorld.myPet;

        Sprite spriteFrame;

        float width = myPet.drawWidth;
        float height = myPet.drawHeight;

        if (myPet.petType == Pet.PetType.BOMB) {
            spriteFrame = Assets.PetBombFlyAnimation.getKeyFrame(myPet.stateTime, true);
        } else {
            if (myWorld.myPlayer.isDash) {
                spriteFrame = Assets.Pet1DashAnimation.getKeyFrame(myPet.stateTime, true);
                width = myPet.dashDrawWidth;
                height = myPet.dashDrawHeight;
            } else
                spriteFrame = Assets.Pet1FlyAnimation.getKeyFrame(myPet.stateTime, true);
        }

        spriteFrame.setPosition(myPet.position.x - width + Pet.RADIUS, myWorld.myPet.position.y - height / 2f);
        spriteFrame.setSize(width, height);
        spriteFrame.draw(batcher);
    }

    private void renderWall() {

        for (Wall obj : myWorld.arrayWall) {
            Sprite spriteFrame = Assets.wall;
            spriteFrame.setPosition(obj.position.x - Wall.WIDTH / 2f, obj.position.y - Wall.HEIGHT / 2f);
            spriteFrame.setSize(Wall.WIDTH, Wall.HEIGHT);
            spriteFrame.draw(batcher);
        }

    }

    private void renderObstacles(float delta) {
        for (Obstacle obj : myWorld.arrayObstacles) {
            if (obj.state == Obstacle.STATE_NORMAL) {

                float width, height;
                Sprite spriteFrame;

                if (obj instanceof ObstacleBoxes4) {
                    width = ObstacleBoxes4.DRAW_WIDTH;
                    height = ObstacleBoxes4.DRAW_HEIGHT;
                    spriteFrame = Assets.boxes4;

                } else {
                    width = ObstacleBoxes7.DRAW_WIDTH;
                    height = ObstacleBoxes7.DRAW_HEIGHT;
                    spriteFrame = Assets.boxes7;
                }
                spriteFrame.setPosition(obj.position.x - width / 2f, obj.position.y - height / 2f);
                spriteFrame.setSize(width, height);
                spriteFrame.draw(batcher);

            } else {
                if (obj.effect != null)
                    obj.effect.draw(batcher, delta);

            }
        }

    }

    private void renderMissile() {
        for (Missile obj : myWorld.arrayMissiles) {
            Sprite spriteFrame;
            float width, height;

            if (obj.state == Missile.STATE_NORMAL) {
                width = Missile.WIDTH;
                height = Missile.HEIGHT;
                spriteFrame = Assets.missileAnimation.getKeyFrame(obj.stateTime, true);
            } else if (obj.state == Missile.STATE_EXPLODE) {
                width = 1f;
                height = .84f;
                spriteFrame = Assets.explosion.getKeyFrame(obj.stateTime, false);
            } else
                continue;

            spriteFrame.setPosition(obj.position.x - width / 2f, obj.position.y - height / 2f);
            spriteFrame.setSize(width, height);
            spriteFrame.draw(batcher);
        }

    }

    private void renderPlayer() {
        Player oPer = myWorld.myPlayer;

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
            case Player.TYPE_GIRL:
            case Player.TYPE_BOY:
                animIdle = Assets.playerIdleAnimation;
                animJump = Assets.playerJumpAnimation;
                animRun = Assets.playerRunAnimation;
                animSlide = Assets.playerSlideAnimation;
                animDash = Assets.playerDashAnimation;
                animHurt = Assets.playerHurtAnimation;
                animDizzy = Assets.playerDizzyAnimation;
                animDead = Assets.playerDeadAnimation;
                break;
            case Player.TYPE_NINJA:
            default:
                animIdle = Assets.ninjaIdleAnimation;
                animJump = Assets.ninjaJumpAnimation;
                animRun = Assets.ninjaRunAnimation;
                animSlide = Assets.ninjaSlideAnimation;
                animDash = Assets.ninjaDashAnimation;
                animHurt = Assets.ninjaHurtAnimation;
                animDizzy = Assets.ninjaDizzyAnimation;
                animDead = Assets.ninjaDeadAnimation;
                break;
        }

        if (oPer.state == Player.STATE_NORMAL) {

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
        } else if (oPer.state == Player.STATE_HURT) {
            spriteFrame = animHurt.getKeyFrame(oPer.stateTime, false);
            offsetY = .1f;

        } else if (oPer.state == Player.STATE_DIZZY) {
            spriteFrame = animDizzy.getKeyFrame(oPer.stateTime, true);
            offsetY = .1f;

        } else {
            spriteFrame = animDead.getKeyFrame(oPer.stateTime, false);
            offsetY = .1f;
        }

        spriteFrame.setPosition(myWorld.myPlayer.position.x - .75f, myWorld.myPlayer.position.y - .52f - offsetY);
        spriteFrame.setSize(Player.DRAW_WIDTH, Player.DRAW_HEIGHT);
        spriteFrame.draw(batcher);

    }

}
