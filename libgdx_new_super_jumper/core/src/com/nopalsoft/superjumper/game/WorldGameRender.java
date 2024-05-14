package com.nopalsoft.superjumper.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.nopalsoft.superjumper.Assets;
import com.nopalsoft.superjumper.screens.Screens;

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

		renderPersonaje();
		renderPlataformas();
		renderPiezasPlataformas();
		renderCoins();
		renderItems();
		renderEnemigo();
		renderNube();
		renderRayo();
		renderBullet();

		batcher.end();

		// boxRender.render(oWorld.oWorldBox, oCam.combined);

	}

	private void renderPersonaje() {
		AtlasRegion keyframe;

		com.nopalsoft.superjumper.objects.Character obj = oWorld.oPer;

		if (obj.speed.y > 0)
			keyframe = Assets.characterJump;
		else
			keyframe = Assets.characterStand;

		if (obj.speed.x > 0)
			batcher.draw(keyframe, obj.position.x + com.nopalsoft.superjumper.objects.Character.DRAW_WIDTH / 2f, obj.position.y - com.nopalsoft.superjumper.objects.Character.DRAW_HEIGTH / 2f,
					-com.nopalsoft.superjumper.objects.Character.DRAW_WIDTH / 2f, com.nopalsoft.superjumper.objects.Character.DRAW_HEIGTH / 2f, -com.nopalsoft.superjumper.objects.Character.DRAW_WIDTH, com.nopalsoft.superjumper.objects.Character.DRAW_HEIGTH, 1, 1, obj.angleDeg);

		else
			batcher.draw(keyframe, obj.position.x - com.nopalsoft.superjumper.objects.Character.DRAW_WIDTH / 2f, obj.position.y - com.nopalsoft.superjumper.objects.Character.DRAW_HEIGTH / 2f,
					com.nopalsoft.superjumper.objects.Character.DRAW_WIDTH / 2f, com.nopalsoft.superjumper.objects.Character.DRAW_HEIGTH / 2f, com.nopalsoft.superjumper.objects.Character.DRAW_WIDTH, com.nopalsoft.superjumper.objects.Character.DRAW_HEIGTH, 1, 1, obj.angleDeg);

		if (obj.isJetPack) {
			batcher.draw(Assets.jetpack, obj.position.x - .45f / 2f, obj.position.y - .7f / 2f, .45f, .7f);

			TextureRegion fireFrame = Assets.jetpackFire.getKeyFrame(obj.durationJetPack, true);
			batcher.draw(fireFrame, obj.position.x - .35f / 2f, obj.position.y - .95f, .35f, .6f);

		}
		if (obj.isBubble) {
			batcher.draw(Assets.bubble, obj.position.x - .5f, obj.position.y - .5f, 1, 1);
		}

	}

	private void renderPlataformas() {
        for (com.nopalsoft.superjumper.objects.Platform obj : oWorld.arrPlataformas) {
            com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion keyframe = null;

            if (obj.type == com.nopalsoft.superjumper.objects.Platform.TYPE_BREAKABLE) {
                switch (obj.color) {
                    case com.nopalsoft.superjumper.objects.Platform.COLOR_BEIGE:
                        keyframe = com.nopalsoft.superjumper.Assets.platformBeigeBroken;
                        break;
                    case com.nopalsoft.superjumper.objects.Platform.COLOR_BLUE:
                        keyframe = com.nopalsoft.superjumper.Assets.platformBlueBroken;
                        break;
                    case com.nopalsoft.superjumper.objects.Platform.COLOR_GRAY:
                        keyframe = com.nopalsoft.superjumper.Assets.platformGrayBroken;
                        break;
                    case com.nopalsoft.superjumper.objects.Platform.COLOR_GREEN:
                        keyframe = com.nopalsoft.superjumper.Assets.platformGreenBroken;
                        break;
                    case com.nopalsoft.superjumper.objects.Platform.COLOR_MULTICOLOR:
                        keyframe = com.nopalsoft.superjumper.Assets.platformMulticolorBroken;
                        break;
                    case com.nopalsoft.superjumper.objects.Platform.COLOR_PINK:
                        keyframe = com.nopalsoft.superjumper.Assets.platformPinkBroken;
                        break;

                }
            } else {
                switch (obj.color) {
                    case com.nopalsoft.superjumper.objects.Platform.COLOR_BEIGE:
                        keyframe = com.nopalsoft.superjumper.Assets.platformBeige;
                        break;
                    case com.nopalsoft.superjumper.objects.Platform.COLOR_BLUE:
                        keyframe = com.nopalsoft.superjumper.Assets.platformBlue;
                        break;
                    case com.nopalsoft.superjumper.objects.Platform.COLOR_GRAY:
                        keyframe = com.nopalsoft.superjumper.Assets.platformGray;
                        break;
                    case com.nopalsoft.superjumper.objects.Platform.COLOR_GREEN:
                        keyframe = com.nopalsoft.superjumper.Assets.platformGreen;
                        break;
                    case com.nopalsoft.superjumper.objects.Platform.COLOR_MULTICOLOR:
                        keyframe = com.nopalsoft.superjumper.Assets.platformMulticolor;
                        break;
                    case com.nopalsoft.superjumper.objects.Platform.COLOR_PINK:
                        keyframe = com.nopalsoft.superjumper.Assets.platformPink;
                        break;
                    case com.nopalsoft.superjumper.objects.Platform.COLOR_BEIGE_LIGHT:
                        keyframe = com.nopalsoft.superjumper.Assets.platformBeigeLight;
                        break;
                    case com.nopalsoft.superjumper.objects.Platform.COLOR_BLUE_LIGHT:
                        keyframe = com.nopalsoft.superjumper.Assets.platformBlueLight;
                        break;
                    case com.nopalsoft.superjumper.objects.Platform.COLOR_GRAY_LIGHT:
                        keyframe = com.nopalsoft.superjumper.Assets.platformGrayLight;
                        break;
                    case com.nopalsoft.superjumper.objects.Platform.COLOR_GREEN_LIGHT:
                        keyframe = com.nopalsoft.superjumper.Assets.platformGreenLight;
                        break;
                    case com.nopalsoft.superjumper.objects.Platform.COLOR_MULTICOLOR_LIGHT:
                        keyframe = com.nopalsoft.superjumper.Assets.platformMulticolorLight;
                        break;
                    case com.nopalsoft.superjumper.objects.Platform.COLOR_PINK_LIGHT:
                        keyframe = com.nopalsoft.superjumper.Assets.platformPinkLight;
                        break;
                }

            }
            batcher.draw(keyframe, obj.position.x - com.nopalsoft.superjumper.objects.Platform.DRAW_WIDTH_NORMAL / 2f, obj.position.y - com.nopalsoft.superjumper.objects.Platform.DRAW_HEIGHT_NORMAL / 2f,
                    com.nopalsoft.superjumper.objects.Platform.DRAW_WIDTH_NORMAL, com.nopalsoft.superjumper.objects.Platform.DRAW_HEIGHT_NORMAL);
        }
	}

	private void renderPiezasPlataformas() {
        for (com.nopalsoft.superjumper.objects.PlatformPiece obj : oWorld.arrPiezasPlataformas) {
            com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion keyframe = null;

            if (obj.type == com.nopalsoft.superjumper.objects.PlatformPiece.TYPE_LEFT) {
                switch (obj.color) {
                    case com.nopalsoft.superjumper.objects.Platform.COLOR_BEIGE:
                        keyframe = com.nopalsoft.superjumper.Assets.platformBeigeLeft;
                        break;
                    case com.nopalsoft.superjumper.objects.Platform.COLOR_BLUE:
                        keyframe = com.nopalsoft.superjumper.Assets.platformBlueLeft;
                        break;
                    case com.nopalsoft.superjumper.objects.Platform.COLOR_GRAY:
                        keyframe = com.nopalsoft.superjumper.Assets.platformGrayLeft;
                        break;
                    case com.nopalsoft.superjumper.objects.Platform.COLOR_GREEN:
                        keyframe = com.nopalsoft.superjumper.Assets.platformGreenLeft;
                        break;
                    case com.nopalsoft.superjumper.objects.Platform.COLOR_MULTICOLOR:
                        keyframe = com.nopalsoft.superjumper.Assets.platformMulticolorLeft;
                        break;
                    case com.nopalsoft.superjumper.objects.Platform.COLOR_PINK:
                        keyframe = com.nopalsoft.superjumper.Assets.platformPinkLeft;
                        break;

                }
            } else {
                switch (obj.color) {
                    case com.nopalsoft.superjumper.objects.Platform.COLOR_BEIGE:
                        keyframe = com.nopalsoft.superjumper.Assets.platformBeigeRight;
                        break;
                    case com.nopalsoft.superjumper.objects.Platform.COLOR_BLUE:
                        keyframe = com.nopalsoft.superjumper.Assets.platformBlueRight;
                        break;
                    case com.nopalsoft.superjumper.objects.Platform.COLOR_GRAY:
                        keyframe = com.nopalsoft.superjumper.Assets.platformGrayRight;
                        break;
                    case com.nopalsoft.superjumper.objects.Platform.COLOR_GREEN:
                        keyframe = com.nopalsoft.superjumper.Assets.platformGreenRight;
                        break;
                    case com.nopalsoft.superjumper.objects.Platform.COLOR_MULTICOLOR:
                        keyframe = com.nopalsoft.superjumper.Assets.platformMulticolorRight;
                        break;
                    case com.nopalsoft.superjumper.objects.Platform.COLOR_PINK:
                        keyframe = com.nopalsoft.superjumper.Assets.platformPinkRight;
                        break;

                }
            }

            batcher.draw(keyframe, obj.position.x - com.nopalsoft.superjumper.objects.PlatformPiece.DRAW_WIDTH_NORMAL / 2f, obj.position.y - com.nopalsoft.superjumper.objects.PlatformPiece.DRAW_HEIGHT_NORMAL
                            / 2f, com.nopalsoft.superjumper.objects.PlatformPiece.DRAW_WIDTH_NORMAL / 2f, com.nopalsoft.superjumper.objects.PlatformPiece.DRAW_HEIGHT_NORMAL / 2f, com.nopalsoft.superjumper.objects.PlatformPiece.DRAW_WIDTH_NORMAL,
                    com.nopalsoft.superjumper.objects.PlatformPiece.DRAW_HEIGHT_NORMAL, 1, 1, obj.angleDeg);

        }
	}

	private void renderCoins() {
        for (com.nopalsoft.superjumper.objects.Coin obj : oWorld.arrMonedas) {
            batcher.draw(com.nopalsoft.superjumper.Assets.coin, obj.position.x - com.nopalsoft.superjumper.objects.Coin.DRAW_WIDTH / 2f, obj.position.y - com.nopalsoft.superjumper.objects.Coin.DRAW_HEIGHT / 2f, com.nopalsoft.superjumper.objects.Coin.DRAW_WIDTH,
                    com.nopalsoft.superjumper.objects.Coin.DRAW_HEIGHT);
        }

	}

	private void renderItems() {
        for (com.nopalsoft.superjumper.objects.Item obj : oWorld.arrItem) {
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

	private void renderEnemigo() {
        for (com.nopalsoft.superjumper.objects.Enemy obj : oWorld.arrEnemigo) {
            com.badlogic.gdx.graphics.g2d.TextureRegion keyframe = com.nopalsoft.superjumper.Assets.enemy.getKeyFrame(obj.stateTime, true);

            batcher.draw(keyframe, obj.position.x - com.nopalsoft.superjumper.objects.Enemy.DRAW_WIDTH / 2f, obj.position.y - com.nopalsoft.superjumper.objects.Enemy.DRAW_HEIGHT / 2f, com.nopalsoft.superjumper.objects.Enemy.DRAW_WIDTH,
                    com.nopalsoft.superjumper.objects.Enemy.DRAW_HEIGHT);
        }

	}

	private void renderNube() {
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

	private void renderRayo() {
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
