package com.nopalsoft.sokoban;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.AtlasTmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.sokoban.parallax.ParallaxBackground;
import com.nopalsoft.sokoban.parallax.ParallaxLayer;

public class Assets {

    public static BitmapFont font;
    public static BitmapFont fontRed;

    public static ParallaxBackground background;
    public static NinePatchDrawable blackPixel;

    public static TiledMap map;

    public static TextureRegionDrawable backgroundMoves;
    public static TextureRegionDrawable backgroundTime;

    public static TextureRegionDrawable buttonRight;
    public static TextureRegionDrawable buttonRightPressed;
    public static TextureRegionDrawable buttonLeft;
    public static TextureRegionDrawable buttonLeftPressed;
    public static TextureRegionDrawable buttonUp;
    public static TextureRegionDrawable buttonUpPressed;
    public static TextureRegionDrawable buttonDown;
    public static TextureRegionDrawable buttonDownPressed;
    public static TextureRegionDrawable buttonRefresh;
    public static TextureRegionDrawable buttonRefreshPressed;
    public static TextureRegionDrawable buttonPause;
    public static TextureRegionDrawable buttonPausePressed;
    public static TextureRegionDrawable buttonLeaderboard;
    public static TextureRegionDrawable buttonLeaderboardPressed;
    public static TextureRegionDrawable buttonAchievement;
    public static TextureRegionDrawable buttonAchievementPressed;
    public static TextureRegionDrawable buttonFacebook;
    public static TextureRegionDrawable buttonFacebookPressed;
    public static TextureRegionDrawable buttonSettings;
    public static TextureRegionDrawable buttonSettingsPressed;
    public static TextureRegionDrawable buttonMore;
    public static TextureRegionDrawable buttonMorePressed;
    public static TextureRegionDrawable buttonClose;
    public static TextureRegionDrawable buttonClosePressed;
    public static TextureRegionDrawable buttonHome;
    public static TextureRegionDrawable buttonHomePressed;
    public static TextureRegionDrawable buttonOff;
    public static TextureRegionDrawable buttonOn;
    public static TextureRegionDrawable buttonPlay;
    public static TextureRegionDrawable buttonPlayPressed;

    public static TextureRegionDrawable levelOff;
    public static TextureRegionDrawable levelStar;

    public static TextureRegionDrawable clock;

    public static AtlasRegion beigeBox;
    public static AtlasRegion darkBeigeBox;
    public static AtlasRegion blackBox;
    public static AtlasRegion darkBlackBox;
    public static AtlasRegion blueBox;
    public static AtlasRegion darkBlueBox;
    public static AtlasRegion brownBox;
    public static AtlasRegion darkBrownBox;
    public static AtlasRegion grayBox;
    public static AtlasRegion darkGrayBox;
    public static AtlasRegion purpleBox;
    public static AtlasRegion darkPurpleBox;
    public static AtlasRegion redBox;
    public static AtlasRegion darkRedBox;
    public static AtlasRegion yellowBox;
    public static AtlasRegion darkYellowBox;

    public static AtlasRegion endPointBeige;
    public static AtlasRegion endPointBlack;
    public static AtlasRegion endPointBlue;
    public static AtlasRegion endPointBrown;
    public static AtlasRegion endPointGray;
    public static AtlasRegion endPointPurple;
    public static AtlasRegion endPointRed;
    public static AtlasRegion endPointYellow;

    public static Animation<TextureRegion> characterGoingUp;
    public static Animation<TextureRegion> characterGoingDown;
    public static Animation<TextureRegion> characterGoingLeft;
    public static Animation<TextureRegion> characterGoingRight;
    public static AtlasRegion characterStand;
    public static TextureRegionDrawable backgroundWindow;
    public static TextButtonStyle styleTextButtonLevel;
    public static TextButtonStyle styleTextButtonLevelLocked;
    static TextureAtlas atlas;

    public static void load() {
        atlas = new TextureAtlas(Gdx.files.internal("data/atlasMap.txt"));

        font = new BitmapFont(Gdx.files.internal("data/font32.fnt"), atlas.findRegion("UI/font32"));
        fontRed = new BitmapFont(Gdx.files.internal("data/font32Red.fnt"), atlas.findRegion("UI/font32Red"));

        loadUI();

        blackPixel = new NinePatchDrawable(new NinePatch(atlas.findRegion("pixelNegro"), 1, 1, 0, 0));

        backgroundWindow = new TextureRegionDrawable(atlas.findRegion("UI/backgroundVentana"));

        beigeBox = atlas.findRegion("cajaBeige");
        darkBeigeBox = atlas.findRegion("cajaDarkBeige");
        blackBox = atlas.findRegion("cajaBlack");
        darkBlackBox = atlas.findRegion("cajaDarkBlack");
        blueBox = atlas.findRegion("cajaBlue");
        darkBlueBox = atlas.findRegion("cajaDarkBlue");
        brownBox = atlas.findRegion("cajaBrown");
        darkBrownBox = atlas.findRegion("cajaDarkBrown");
        grayBox = atlas.findRegion("cajaGray");
        darkGrayBox = atlas.findRegion("cajaDarkGray");
        purpleBox = atlas.findRegion("cajaPurple");
        darkPurpleBox = atlas.findRegion("cajaDarkPurple");
        redBox = atlas.findRegion("cajaRed");
        darkRedBox = atlas.findRegion("cajaDarkRed");
        yellowBox = atlas.findRegion("cajaYellow");
        darkYellowBox = atlas.findRegion("cajaDarkYellow");

        endPointBeige = atlas.findRegion("endPointBeige");
        endPointBlack = atlas.findRegion("endPointBlack");
        endPointBlue = atlas.findRegion("endPointBlue");
        endPointBrown = atlas.findRegion("endPointBrown");
        endPointGray = atlas.findRegion("endPointGray");
        endPointPurple = atlas.findRegion("endPointPurple");
        endPointRed = atlas.findRegion("endPointRed");
        endPointYellow = atlas.findRegion("endPointYellow");

        characterStand = atlas.findRegion("Character4");

        AtlasRegion up1 = atlas.findRegion("Character7");
        AtlasRegion up2 = atlas.findRegion("Character8");
        AtlasRegion up3 = atlas.findRegion("Character9");
        characterGoingUp = new Animation<>(.09f, up2, up3, up1);

        AtlasRegion down1 = atlas.findRegion("Character4");
        AtlasRegion down2 = atlas.findRegion("Character5");
        AtlasRegion down3 = atlas.findRegion("Character6");
        characterGoingDown = new Animation<>(.09f, down2, down3, down1);

        AtlasRegion right1 = atlas.findRegion("Character2");
        AtlasRegion right2 = atlas.findRegion("Character3");
        characterGoingRight = new Animation<>(.09f, right1, right2, right1);

        AtlasRegion left1 = atlas.findRegion("Character1");
        AtlasRegion left2 = atlas.findRegion("Character10");
        characterGoingLeft = new Animation<>(.09f, left1, left2, left1);

        AtlasRegion royalBackgroundFlip = atlas.findRegion("backgroundFlip");
        royalBackgroundFlip.flip(true, false);
        ParallaxLayer background = new ParallaxLayer(atlas.findRegion("background"), new Vector2(1, 0), new Vector2(0, 0), new Vector2(798, 480), 800, 480);
        ParallaxLayer backgroundFlip = new ParallaxLayer(royalBackgroundFlip, new Vector2(1, 0), new Vector2(799, 0), new Vector2(798, 480), 800, 480);
        Assets.background = new ParallaxBackground(new ParallaxLayer[]{background, backgroundFlip}, 800, 480, new Vector2(20, 0));

    }

    private static void loadUI() {
        buttonRight = new TextureRegionDrawable(atlas.findRegion("UI/btDer"));
        buttonRightPressed = new TextureRegionDrawable(atlas.findRegion("UI/btDerPress"));
        buttonLeft = new TextureRegionDrawable(atlas.findRegion("UI/btIzq"));
        buttonLeftPressed = new TextureRegionDrawable(atlas.findRegion("UI/btIzqPress"));
        buttonUp = new TextureRegionDrawable(atlas.findRegion("UI/btUp"));
        buttonUpPressed = new TextureRegionDrawable(atlas.findRegion("UI/btUpPress"));
        buttonDown = new TextureRegionDrawable(atlas.findRegion("UI/btDown"));
        buttonDownPressed = new TextureRegionDrawable(atlas.findRegion("UI/btDownPress"));
        buttonRefresh = new TextureRegionDrawable(atlas.findRegion("UI/btRefresh"));
        buttonRefreshPressed = new TextureRegionDrawable(atlas.findRegion("UI/btRefreshPress"));
        buttonPause = new TextureRegionDrawable(atlas.findRegion("UI/btPausa"));
        buttonPausePressed = new TextureRegionDrawable(atlas.findRegion("UI/btPausaPress"));
        buttonLeaderboard = new TextureRegionDrawable(atlas.findRegion("UI/btLeaderboard"));
        buttonLeaderboardPressed = new TextureRegionDrawable(atlas.findRegion("UI/btLeaderboardPress"));
        buttonAchievement = new TextureRegionDrawable(atlas.findRegion("UI/btAchievement"));
        buttonAchievementPressed = new TextureRegionDrawable(atlas.findRegion("UI/btAchievementPress"));
        buttonFacebook = new TextureRegionDrawable(atlas.findRegion("UI/btFacebook"));
        buttonFacebookPressed = new TextureRegionDrawable(atlas.findRegion("UI/btFacebookPress"));
        buttonSettings = new TextureRegionDrawable(atlas.findRegion("UI/btSettings"));
        buttonSettingsPressed = new TextureRegionDrawable(atlas.findRegion("UI/btSettingsPress"));
        buttonMore = new TextureRegionDrawable(atlas.findRegion("UI/btMas"));
        buttonMorePressed = new TextureRegionDrawable(atlas.findRegion("UI/btMasPress"));
        buttonClose = new TextureRegionDrawable(atlas.findRegion("UI/btClose"));
        buttonClosePressed = new TextureRegionDrawable(atlas.findRegion("UI/btClosePress"));
        buttonHome = new TextureRegionDrawable(atlas.findRegion("UI/btHome"));
        buttonHomePressed = new TextureRegionDrawable(atlas.findRegion("UI/btHomePress"));
        buttonOff = new TextureRegionDrawable(atlas.findRegion("UI/btOff"));
        buttonOn = new TextureRegionDrawable(atlas.findRegion("UI/btOn"));

        buttonPlay = new TextureRegionDrawable(atlas.findRegion("UI/btPlay"));
        buttonPlayPressed = new TextureRegionDrawable(atlas.findRegion("UI/btPlayPress"));
        clock = new TextureRegionDrawable(atlas.findRegion("UI/clock"));

        levelOff = new TextureRegionDrawable(atlas.findRegion("UI/levelOff"));
        levelStar = new TextureRegionDrawable(atlas.findRegion("UI/levelStar"));

        /* Button level */
        TextureRegionDrawable btLevel = new TextureRegionDrawable(atlas.findRegion("UI/btLevel"));
        styleTextButtonLevel = new TextButtonStyle(btLevel, null, null, font);

        /* Button level */
        TextureRegionDrawable btLevelLocked = new TextureRegionDrawable(atlas.findRegion("UI/btLevelLocked"));
        styleTextButtonLevelLocked = new TextButtonStyle(btLevelLocked, null, null, font);

        backgroundMoves = new TextureRegionDrawable(atlas.findRegion("UI/backgroundMoves"));
        backgroundTime = new TextureRegionDrawable(atlas.findRegion("UI/backgroundTime"));

    }

    public static void loadTiledMap(int numMap) {
        if (map != null) {
            map.dispose();
            map = null;
        }
        if (Settings.isTest()) {
            map = new TmxMapLoader().load("data/mapsTest/map" + numMap + ".tmx");
        } else {
            map = new AtlasTmxMapLoader().load("data/maps/map" + numMap + ".tmx");
        }
    }

}
