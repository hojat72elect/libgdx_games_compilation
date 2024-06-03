package com.nopalsoft.superjumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Assets {

	public static BitmapFont fontSmall;
	public static BitmapFont fontBig;

	public static AtlasRegion background;
	public static TextureRegionDrawable settings;

	// Player
	public static AtlasRegion characterJump;
	public static AtlasRegion characterStand;
	public static Animation<AtlasRegion> characterWalk;

	public static AtlasRegion coin;
	public static AtlasRegion gun;
	public static AtlasRegion bullet;
	public static AtlasRegion spring;
	public static AtlasRegion bubbleSmall;
	public static AtlasRegion jetpackSmall;
	public static AtlasRegion bubble;
	public static AtlasRegion jetpack;
	public static Animation<AtlasRegion> jetpackFire;

	public static Animation<AtlasRegion> enemy;

	public static AtlasRegion cloudHappy;
	public static AtlasRegion cloudAngry;

	public static Animation<AtlasRegion> ray;
	public static AtlasRegion cloudWind;

	// Platforms
	public static AtlasRegion platformBeige;
	public static AtlasRegion platformBeigeLight;
	public static AtlasRegion platformBeigeBroken;
	public static AtlasRegion platformBeigeLeft;
	public static AtlasRegion platformBeigeRight;

	public static AtlasRegion platformBlue;
	public static AtlasRegion platformBlueLight;
	public static AtlasRegion platformBlueBroken;
	public static AtlasRegion platformBlueLeft;
	public static AtlasRegion platformBlueRight;

	public static AtlasRegion platformGray;
	public static AtlasRegion platformGrayLight;
	public static AtlasRegion platformGrayBroken;
	public static AtlasRegion platformGrayLeft;
	public static AtlasRegion platformGrayRight;
	public static AtlasRegion platformGreen;
	public static AtlasRegion platformGreenLight;
	public static AtlasRegion platformGreenBroken;
	public static AtlasRegion platformGreenLeft;
	public static AtlasRegion platformGreenRight;

	public static AtlasRegion platformMulticolor;
	public static AtlasRegion platformMulticolorLight;
	public static AtlasRegion platformMulticolorBroken;
	public static AtlasRegion platformMulticolorLeft;
	public static AtlasRegion platformMulticolorRight;

	public static AtlasRegion platformPink;
	public static AtlasRegion platformPinkLight;
	public static AtlasRegion platformPinkBroken;
	public static AtlasRegion platformPinkLeft;
	public static AtlasRegion platformPinkRight;

	public static TextureRegionDrawable buttonPause;

	public static LabelStyle labelStyleSmall;
	public static LabelStyle labelStyleBig;
	public static TextButtonStyle textButtonStyleBig;

	public static NinePatchDrawable pixelBlack;

	public static void loadStyles(TextureAtlas atlas) {
		// Label Style
		labelStyleSmall = new LabelStyle(fontSmall, Color.WHITE);
		labelStyleBig = new LabelStyle(fontBig, Color.WHITE);

		TextureRegionDrawable button = new TextureRegionDrawable(atlas.findRegion("button"));
		textButtonStyleBig = new TextButtonStyle(button, button, null, fontBig);

		pixelBlack = new NinePatchDrawable(new NinePatch(atlas.findRegion("pixelNegro"), 1, 1, 0, 0));
	}

	public static void load() {
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/atlasMap.txt"));

		fontSmall = new BitmapFont(Gdx.files.internal("data/fontGrande.fnt"), atlas.findRegion("fontGrande"));
		fontBig = new BitmapFont(Gdx.files.internal("data/fontGrande.fnt"), atlas.findRegion("fontGrande"));

		loadStyles(atlas);

		buttonPause = new TextureRegionDrawable(atlas.findRegion("btPause"));

		background = atlas.findRegion("Background");
		settings = new TextureRegionDrawable(atlas.findRegion("titulo"));


		characterJump = atlas.findRegion("personajeJump");
		characterStand = atlas.findRegion("personajeStand");

		AtlasRegion walk1 = atlas.findRegion("personajeWalk1");
		AtlasRegion walk2 = atlas.findRegion("personajeWalk2");
		characterWalk = new Animation<>(.5f, walk1, walk2);

		coin = atlas.findRegion("Coin");
		gun = atlas.findRegion("Pistol");
		bullet = atlas.findRegion("Bullet");
		spring = atlas.findRegion("Spring");
		bubbleSmall = atlas.findRegion("Bubble_Small");
		jetpackSmall = atlas.findRegion("Jetpack_Small");
		bubble = atlas.findRegion("Bubble_Big");
		jetpack = atlas.findRegion("Jetpack_Big");

		AtlasRegion jetpackFire1 = atlas.findRegion("JetFire1");
		AtlasRegion jetpackFire2 = atlas.findRegion("JetFire2");
		jetpackFire = new Animation<>(.085f, jetpackFire1, jetpackFire2);

		AtlasRegion enemigo1 = atlas.findRegion("HearthEnemy1");
		AtlasRegion enemigo2 = atlas.findRegion("HearthEnemy2");
		enemy = new Animation<>(.2f, enemigo1, enemigo2);

		cloudHappy = atlas.findRegion("HappyCloud");
		cloudAngry = atlas.findRegion("AngryCloud");
		cloudWind = atlas.findRegion("CloudWind");

		AtlasRegion lightning1 = atlas.findRegion("Lightning1");
		AtlasRegion lightning2 = atlas.findRegion("Lightning2");
		ray = new Animation<>(.08f, lightning1, lightning2);


		platformBeige = atlas.findRegion("LandPiece_DarkBeige");
		platformBeigeLight = atlas.findRegion("LandPiece_LightBeige");
		platformBeigeBroken = atlas.findRegion("BrokenLandPiece_Beige");
		platformBeigeLeft = atlas.findRegion("HalfLandPiece_Left_Beige");
		platformBeigeRight = atlas.findRegion("HalfLandPiece_Right_Beige");

		platformBlue = atlas.findRegion("LandPiece_DarkBlue");
		platformBlueLight = atlas.findRegion("LandPiece_LightBlue");
		platformBlueBroken = atlas.findRegion("BrokenLandPiece_Blue");
		platformBlueLeft = atlas.findRegion("HalfLandPiece_Left_Blue");
		platformBlueRight = atlas.findRegion("HalfLandPiece_Right_Blue");

		platformGray = atlas.findRegion("LandPiece_DarkGray");
		platformGrayLight = atlas.findRegion("LandPiece_LightGray");
		platformGrayBroken = atlas.findRegion("BrokenLandPiece_Gray");
		platformGrayLeft = atlas.findRegion("HalfLandPiece_Left_Gray");
		platformGrayRight = atlas.findRegion("HalfLandPiece_Right_Gray");

		platformGreen = atlas.findRegion("LandPiece_DarkGreen");
		platformGreenLight = atlas.findRegion("LandPiece_LightGreen");
		platformGreenBroken = atlas.findRegion("BrokenLandPiece_Green");
		platformGreenLeft = atlas.findRegion("HalfLandPiece_Left_Green");
		platformGreenRight = atlas.findRegion("HalfLandPiece_Right_Green");

		platformMulticolor = atlas.findRegion("LandPiece_DarkMulticolored");
		platformMulticolorLight = atlas.findRegion("LandPiece_LightMulticolored");
		platformMulticolorBroken = atlas.findRegion("BrokenLandPiece_Multicolored");
		platformMulticolorLeft = atlas.findRegion("HalfLandPiece_Left_Multicolored");
		platformMulticolorRight = atlas.findRegion("HalfLandPiece_Right_Multicolored");

		platformPink = atlas.findRegion("LandPiece_DarkPink");
		platformPinkLight = atlas.findRegion("LandPiece_LightPink");
		platformPinkBroken = atlas.findRegion("BrokenLandPiece_Pink");
		platformPinkLeft = atlas.findRegion("HalfLandPiece_Left_Pink");
		platformPinkRight = atlas.findRegion("HalfLandPiece_Right_Pink");

	}
}
