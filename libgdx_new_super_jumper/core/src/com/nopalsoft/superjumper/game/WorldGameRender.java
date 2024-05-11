package com.nopalsoft.superjumper.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.nopalsoft.superjumper.Assets;
import com.nopalsoft.superjumper.objetos.Personaje;
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

		Personaje obj = oWorld.oPer;

		if (obj.velocidad.y > 0)
			keyframe = Assets.personajeJump;
		else
			keyframe = Assets.personajeStand;

		if (obj.velocidad.x > 0)
			batcher.draw(keyframe, obj.position.x + Personaje.DRAW_WIDTH / 2f, obj.position.y - Personaje.DRAW_HEIGTH / 2f,
					-Personaje.DRAW_WIDTH / 2f, Personaje.DRAW_HEIGTH / 2f, -Personaje.DRAW_WIDTH, Personaje.DRAW_HEIGTH, 1, 1, obj.angleDeg);

		else
			batcher.draw(keyframe, obj.position.x - Personaje.DRAW_WIDTH / 2f, obj.position.y - Personaje.DRAW_HEIGTH / 2f,
					Personaje.DRAW_WIDTH / 2f, Personaje.DRAW_HEIGTH / 2f, Personaje.DRAW_WIDTH, Personaje.DRAW_HEIGTH, 1, 1, obj.angleDeg);

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
        for (com.nopalsoft.superjumper.objetos.Plataformas obj : oWorld.arrPlataformas) {
            com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion keyframe = null;

            if (obj.tipo == com.nopalsoft.superjumper.objetos.Plataformas.TIPO_ROMPIBLE) {
                switch (obj.color) {
                    case com.nopalsoft.superjumper.objetos.Plataformas.COLOR_BEIGE:
                        keyframe = com.nopalsoft.superjumper.Assets.plataformaBeigeBroken;
                        break;
                    case com.nopalsoft.superjumper.objetos.Plataformas.COLOR_BLUE:
                        keyframe = com.nopalsoft.superjumper.Assets.plataformaBlueBroken;
                        break;
                    case com.nopalsoft.superjumper.objetos.Plataformas.COLOR_GRAY:
                        keyframe = com.nopalsoft.superjumper.Assets.plataformaGrayBroken;
                        break;
                    case com.nopalsoft.superjumper.objetos.Plataformas.COLOR_GREEN:
                        keyframe = com.nopalsoft.superjumper.Assets.plataformaGreenBroken;
                        break;
                    case com.nopalsoft.superjumper.objetos.Plataformas.COLOR_MULTICOLOR:
                        keyframe = com.nopalsoft.superjumper.Assets.plataformaMulticolorBroken;
                        break;
                    case com.nopalsoft.superjumper.objetos.Plataformas.COLOR_PINK:
                        keyframe = com.nopalsoft.superjumper.Assets.plataformaPinkBroken;
                        break;

                }
            } else {
                switch (obj.color) {
                    case com.nopalsoft.superjumper.objetos.Plataformas.COLOR_BEIGE:
                        keyframe = com.nopalsoft.superjumper.Assets.plataformaBeige;
                        break;
                    case com.nopalsoft.superjumper.objetos.Plataformas.COLOR_BLUE:
                        keyframe = com.nopalsoft.superjumper.Assets.plataformaBlue;
                        break;
                    case com.nopalsoft.superjumper.objetos.Plataformas.COLOR_GRAY:
                        keyframe = com.nopalsoft.superjumper.Assets.plataformaGray;
                        break;
                    case com.nopalsoft.superjumper.objetos.Plataformas.COLOR_GREEN:
                        keyframe = com.nopalsoft.superjumper.Assets.plataformaGreen;
                        break;
                    case com.nopalsoft.superjumper.objetos.Plataformas.COLOR_MULTICOLOR:
                        keyframe = com.nopalsoft.superjumper.Assets.plataformaMulticolor;
                        break;
                    case com.nopalsoft.superjumper.objetos.Plataformas.COLOR_PINK:
                        keyframe = com.nopalsoft.superjumper.Assets.plataformaPink;
                        break;
                    case com.nopalsoft.superjumper.objetos.Plataformas.COLOR_BEIGE_LIGHT:
                        keyframe = com.nopalsoft.superjumper.Assets.plataformaBeigeLight;
                        break;
                    case com.nopalsoft.superjumper.objetos.Plataformas.COLOR_BLUE_LIGHT:
                        keyframe = com.nopalsoft.superjumper.Assets.plataformaBlueLight;
                        break;
                    case com.nopalsoft.superjumper.objetos.Plataformas.COLOR_GRAY_LIGHT:
                        keyframe = com.nopalsoft.superjumper.Assets.plataformaGrayLight;
                        break;
                    case com.nopalsoft.superjumper.objetos.Plataformas.COLOR_GREEN_LIGHT:
                        keyframe = com.nopalsoft.superjumper.Assets.plataformaGreenLight;
                        break;
                    case com.nopalsoft.superjumper.objetos.Plataformas.COLOR_MULTICOLOR_LIGHT:
                        keyframe = com.nopalsoft.superjumper.Assets.plataformaMulticolorLight;
                        break;
                    case com.nopalsoft.superjumper.objetos.Plataformas.COLOR_PINK_LIGHT:
                        keyframe = com.nopalsoft.superjumper.Assets.plataformaPinkLight;
                        break;
                }

            }
            batcher.draw(keyframe, obj.position.x - com.nopalsoft.superjumper.objetos.Plataformas.DRAW_WIDTH_NORMAL / 2f, obj.position.y - com.nopalsoft.superjumper.objetos.Plataformas.DRAW_HEIGTH_NORMAL / 2f,
                    com.nopalsoft.superjumper.objetos.Plataformas.DRAW_WIDTH_NORMAL, com.nopalsoft.superjumper.objetos.Plataformas.DRAW_HEIGTH_NORMAL);
        }
	}

	private void renderPiezasPlataformas() {
        for (com.nopalsoft.superjumper.objetos.PiezaPlataformas obj : oWorld.arrPiezasPlataformas) {
            com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion keyframe = null;

            if (obj.tipo == com.nopalsoft.superjumper.objetos.PiezaPlataformas.TIPO_LEFT) {
                switch (obj.color) {
                    case com.nopalsoft.superjumper.objetos.Plataformas.COLOR_BEIGE:
                        keyframe = com.nopalsoft.superjumper.Assets.plataformaBeigeLeft;
                        break;
                    case com.nopalsoft.superjumper.objetos.Plataformas.COLOR_BLUE:
                        keyframe = com.nopalsoft.superjumper.Assets.plataformaBlueLeft;
                        break;
                    case com.nopalsoft.superjumper.objetos.Plataformas.COLOR_GRAY:
                        keyframe = com.nopalsoft.superjumper.Assets.plataformaGrayLeft;
                        break;
                    case com.nopalsoft.superjumper.objetos.Plataformas.COLOR_GREEN:
                        keyframe = com.nopalsoft.superjumper.Assets.plataformaGreenLeft;
                        break;
                    case com.nopalsoft.superjumper.objetos.Plataformas.COLOR_MULTICOLOR:
                        keyframe = com.nopalsoft.superjumper.Assets.plataformaMulticolorLeft;
                        break;
                    case com.nopalsoft.superjumper.objetos.Plataformas.COLOR_PINK:
                        keyframe = com.nopalsoft.superjumper.Assets.plataformaPinkLeft;
                        break;

                }
            } else {
                switch (obj.color) {
                    case com.nopalsoft.superjumper.objetos.Plataformas.COLOR_BEIGE:
                        keyframe = com.nopalsoft.superjumper.Assets.plataformaBeigeRight;
                        break;
                    case com.nopalsoft.superjumper.objetos.Plataformas.COLOR_BLUE:
                        keyframe = com.nopalsoft.superjumper.Assets.plataformaBlueRight;
                        break;
                    case com.nopalsoft.superjumper.objetos.Plataformas.COLOR_GRAY:
                        keyframe = com.nopalsoft.superjumper.Assets.plataformaGrayRight;
                        break;
                    case com.nopalsoft.superjumper.objetos.Plataformas.COLOR_GREEN:
                        keyframe = com.nopalsoft.superjumper.Assets.plataformaGreenRight;
                        break;
                    case com.nopalsoft.superjumper.objetos.Plataformas.COLOR_MULTICOLOR:
                        keyframe = com.nopalsoft.superjumper.Assets.plataformaMulticolorRight;
                        break;
                    case com.nopalsoft.superjumper.objetos.Plataformas.COLOR_PINK:
                        keyframe = com.nopalsoft.superjumper.Assets.plataformaPinkRight;
                        break;

                }
            }

            batcher.draw(keyframe, obj.position.x - com.nopalsoft.superjumper.objetos.PiezaPlataformas.DRAW_WIDTH_NORMAL / 2f, obj.position.y - com.nopalsoft.superjumper.objetos.PiezaPlataformas.DRAW_HEIGTH_NORMAL
                            / 2f, com.nopalsoft.superjumper.objetos.PiezaPlataformas.DRAW_WIDTH_NORMAL / 2f, com.nopalsoft.superjumper.objetos.PiezaPlataformas.DRAW_HEIGTH_NORMAL / 2f, com.nopalsoft.superjumper.objetos.PiezaPlataformas.DRAW_WIDTH_NORMAL,
                    com.nopalsoft.superjumper.objetos.PiezaPlataformas.DRAW_HEIGTH_NORMAL, 1, 1, obj.angleDeg);

        }
	}

	private void renderCoins() {
        for (com.nopalsoft.superjumper.objetos.Moneda obj : oWorld.arrMonedas) {
            batcher.draw(com.nopalsoft.superjumper.Assets.coin, obj.position.x - com.nopalsoft.superjumper.objetos.Moneda.DRAW_WIDTH / 2f, obj.position.y - com.nopalsoft.superjumper.objetos.Moneda.DRAW_HEIGHT / 2f, com.nopalsoft.superjumper.objetos.Moneda.DRAW_WIDTH,
                    com.nopalsoft.superjumper.objetos.Moneda.DRAW_HEIGHT);
        }

	}

	private void renderItems() {
        for (com.nopalsoft.superjumper.objetos.Item obj : oWorld.arrItem) {
            com.badlogic.gdx.graphics.g2d.TextureRegion keyframe = null;

            switch (obj.tipo) {
                case com.nopalsoft.superjumper.objetos.Item.TIPO_BUBBLE:
                    keyframe = com.nopalsoft.superjumper.Assets.bubbleSmall;
                    break;
                case com.nopalsoft.superjumper.objetos.Item.TIPO_JETPACK:
                    keyframe = com.nopalsoft.superjumper.Assets.jetpackSmall;
                    break;
                case com.nopalsoft.superjumper.objetos.Item.TIPO_GUN:
                    keyframe = com.nopalsoft.superjumper.Assets.gun;
                    break;

            }

            batcher.draw(keyframe, obj.position.x - com.nopalsoft.superjumper.objetos.Item.DRAW_WIDTH / 2f, obj.position.y - com.nopalsoft.superjumper.objetos.Item.DRAW_HEIGHT / 2f, com.nopalsoft.superjumper.objetos.Item.DRAW_WIDTH, com.nopalsoft.superjumper.objetos.Item.DRAW_HEIGHT);

        }

	}

	private void renderEnemigo() {
        for (com.nopalsoft.superjumper.objetos.Enemigo obj : oWorld.arrEnemigo) {
            com.badlogic.gdx.graphics.g2d.TextureRegion keyframe = com.nopalsoft.superjumper.Assets.enemigo.getKeyFrame(obj.stateTime, true);

            batcher.draw(keyframe, obj.position.x - com.nopalsoft.superjumper.objetos.Enemigo.DRAW_WIDTH / 2f, obj.position.y - com.nopalsoft.superjumper.objetos.Enemigo.DRAW_HEIGHT / 2f, com.nopalsoft.superjumper.objetos.Enemigo.DRAW_WIDTH,
                    com.nopalsoft.superjumper.objetos.Enemigo.DRAW_HEIGHT);
        }

	}

	private void renderNube() {
        for (com.nopalsoft.superjumper.objetos.Nube obj : oWorld.arrNubes) {
            com.badlogic.gdx.graphics.g2d.TextureRegion keyframe = null;

            switch (obj.tipo) {
                case com.nopalsoft.superjumper.objetos.Nube.TIPO_ANGRY:
                    keyframe = com.nopalsoft.superjumper.Assets.nubeAngry;
                    break;
                case com.nopalsoft.superjumper.objetos.Nube.TIPO_HAPPY:
                    keyframe = com.nopalsoft.superjumper.Assets.nubeHappy;
                    break;

            }

            batcher.draw(keyframe, obj.position.x - com.nopalsoft.superjumper.objetos.Nube.DRAW_WIDTH / 2f, obj.position.y - com.nopalsoft.superjumper.objetos.Nube.DRAW_HEIGHT / 2f, com.nopalsoft.superjumper.objetos.Nube.DRAW_WIDTH, com.nopalsoft.superjumper.objetos.Nube.DRAW_HEIGHT);

            if (obj.isBlowing) {
                batcher.draw(com.nopalsoft.superjumper.Assets.nubeViento, obj.position.x - .35f, obj.position.y - .85f, .6f, .8f);
            }
        }

	}

	private void renderRayo() {
        for (com.nopalsoft.superjumper.objetos.Rayo obj : oWorld.arrRayos) {
            com.badlogic.gdx.graphics.g2d.TextureRegion keyframe = com.nopalsoft.superjumper.Assets.rayo.getKeyFrame(obj.stateTime, true);

            batcher.draw(keyframe, obj.position.x - com.nopalsoft.superjumper.objetos.Rayo.DRAW_WIDTH / 2f, obj.position.y - com.nopalsoft.superjumper.objetos.Rayo.DRAW_HEIGHT / 2f, com.nopalsoft.superjumper.objetos.Rayo.DRAW_WIDTH, com.nopalsoft.superjumper.objetos.Rayo.DRAW_HEIGHT);
        }
	}

	private void renderBullet() {
        for (com.nopalsoft.superjumper.objetos.Bullet obj : oWorld.arrBullets) {
            batcher.draw(com.nopalsoft.superjumper.Assets.bullet, obj.position.x - com.nopalsoft.superjumper.objetos.Bullet.SIZE / 2f, obj.position.y - com.nopalsoft.superjumper.objetos.Bullet.SIZE / 2f, com.nopalsoft.superjumper.objetos.Bullet.SIZE, com.nopalsoft.superjumper.objetos.Bullet.SIZE);
        }
	}

}
