package com.nopalsoft.invaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.nopalsoft.invaders.parallax.ParallaxBackground;
import com.nopalsoft.invaders.parallax.ParallaxLayer;

public class Assets {

    private static final GlyphLayout glyphLayout = new GlyphLayout();
    public static I18NBundle languages;
    public static AtlasRegion background;
    public static ParallaxBackground parallaxBackground;

    public static AtlasRegion shipLeft;
    public static AtlasRegion shipRight;
    public static AtlasRegion ship;

    // Fonts
    public static BitmapFont font60;// Mainly for the title of the app
    public static BitmapFont font45;
    public static BitmapFont font15;
    public static BitmapFont font10;

    // Menu
    public static AtlasRegion ellipseMenuLeft;
    public static NinePatchDrawable buttonSignInUp;
    public static NinePatchDrawable buttonSignInDown;
    public static NinePatchDrawable titleMenuBox;

    // Game
    public static NinePatchDrawable recuadroInGameStatus;
    public static TextureRegionDrawable btLeft;
    public static TextureRegionDrawable btRight;
    public static TextureRegionDrawable btFire;
    public static TextureRegionDrawable btFireDown;
    public static TextureRegionDrawable btMissil;
    public static TextureRegionDrawable btMissilDown;

    // Aid
    public static AtlasRegion help1;
    public static AtlasRegion clickHelp;

    // Buttons
    public static TextureRegionDrawable buttonSoundOn;
    public static TextureRegionDrawable buttonSoundOff;
    public static TextureRegionDrawable buttonMusicOn;
    public static TextureRegionDrawable buttonMusicOff;

    // Ammunition
    public static AtlasRegion balaNormal;
    public static Animation<TextureRegion> missile;
    public static Animation<TextureRegion> superLightning;
    public static AtlasRegion bulletLevel1;
    public static AtlasRegion bulletLevel2;
    public static AtlasRegion bulletLevel3;
    public static AtlasRegion bulletLevel4;

    public static AtlasRegion boost1;
    public static AtlasRegion boost2;
    public static AtlasRegion boost3;
    public static AtlasRegion upgLife;

    public static Animation<TextureRegion> explosionFuego;
    public static Animation<TextureRegion> shield;

    public static AtlasRegion bulletNormalEnemy;
    public static AtlasRegion alien1;
    public static AtlasRegion alien2;
    public static AtlasRegion alien3;
    public static AtlasRegion alien4;

    // Sounds
    public static Music music;
    public static Sound coinSound;
    public static Sound clickSound;
    public static Sound explosionSound;
    public static Sound missileFire;

    // Styles
    public static TextButtonStyle styleTextButtonMenu;
    public static TextButtonStyle styleTextButtonFacebook;
    public static TextButtonStyle styleTextButtonBack;
    public static TextButtonStyle styleTextButton;

    public static WindowStyle styleDialogPause;

    public static LabelStyle styleLabel;

    public static LabelStyle styleLabelDialog;
    public static SliderStyle styleSlider;

    public static ImageButtonStyle styleImageButtonPause;
    public static ImageButtonStyle styleImageButtonStyleCheckBox;


    static private void loadFont(TextureAtlas atlas) {
        font60 = new BitmapFont(Gdx.files.internal("data/font35.fnt"), atlas.findRegion("font35"), false);
        font45 = new BitmapFont(Gdx.files.internal("data/font35.fnt"), atlas.findRegion("font35"), false);
        font15 = new BitmapFont(Gdx.files.internal("data/font15.fnt"), atlas.findRegion("font15"), false);
        font10 = new BitmapFont(Gdx.files.internal("data/font15.fnt"), atlas.findRegion("font15"), false);

        font60.setColor(Color.GREEN);
        font45.setColor(Color.GREEN);
        font15.setColor(Color.GREEN);
        font10.setColor(Color.GREEN);
    }

    static private void loadSceneStyles(TextureAtlas atlas) {

        /* Dialog */

        NinePatchDrawable recuadroLogIn = new NinePatchDrawable(atlas.createPatch("recuadroLogIn"));
        AtlasRegion dialogDim = atlas.findRegion("fondoNegro");
        styleDialogPause = new WindowStyle(font45, Color.GREEN, recuadroLogIn);
        styleDialogPause.stageBackground = new NinePatchDrawable(new NinePatch(dialogDim));
        styleLabelDialog = new LabelStyle(font15, Color.GREEN);

        /* */

        NinePatchDrawable default_round_down = new NinePatchDrawable(atlas.createPatch("botonDown"));
        NinePatchDrawable default_round = new NinePatchDrawable(atlas.createPatch("boton"));
        styleTextButton = new TextButtonStyle(default_round, default_round_down, null, font15);
        styleTextButton.fontColor = Color.GREEN;

        /* Menu */
        NinePatchDrawable botonMenu = new NinePatchDrawable(atlas.createPatch("botonMenu"));
        NinePatchDrawable botonMenuDown = new NinePatchDrawable(atlas.createPatch("botonMenuPresionado"));
        styleTextButtonMenu = new TextButtonStyle(botonMenu, botonMenuDown, null, font45);
        styleTextButtonMenu.fontColor = Color.GREEN;

        styleLabel = new LabelStyle(font15, Color.GREEN);

        /* Slider */
        NinePatchDrawable default_slider = new NinePatchDrawable(atlas.createPatch("default-slider"));
        TextureRegionDrawable default_slider_knob = new TextureRegionDrawable(atlas.findRegion("default-slider-knob"));

        styleSlider = new SliderStyle(default_slider, default_slider_knob);

        TextureRegionDrawable btBackUp = new TextureRegionDrawable(atlas.findRegion("btBack"));
        TextureRegionDrawable btBackDown = new TextureRegionDrawable(atlas.findRegion("btBackDown"));

        styleTextButtonBack = new TextButtonStyle(btBackUp, btBackDown, null, font15);
        styleTextButtonBack.fontColor = Color.GREEN;

        TextureRegionDrawable buttonPauseUp = new TextureRegionDrawable(atlas.findRegion("btPause"));
        TextureRegionDrawable buttonPauseDown = new TextureRegionDrawable(atlas.findRegion("btPauseDown"));
        styleImageButtonPause = new ImageButtonStyle(buttonPauseUp, buttonPauseDown, null, null, null, null);

        NinePatchDrawable botonFacebook = new NinePatchDrawable(atlas.createPatch("btShareFacebookUp"));
        NinePatchDrawable botonFacebookDown = new NinePatchDrawable(atlas.createPatch("btShareFacebookDown"));
        styleTextButtonFacebook = new TextButtonStyle(botonFacebook, botonFacebookDown, null, font10);

        TextureRegionDrawable checked = new TextureRegionDrawable(atlas.findRegion("checkBoxDown"));
        TextureRegionDrawable uncheked = new TextureRegionDrawable(atlas.findRegion("checkBox"));

        styleImageButtonStyleCheckBox = new ImageButtonStyle(uncheked, checked, checked, null, null, null);
    }

    public static void load() {
        languages = I18NBundle.createBundle(Gdx.files.internal("strings/strings"));


        TextureAtlas atlas1 = new TextureAtlas(Gdx.files.internal("data/atlasMap.txt"));

        loadFont(atlas1);
        loadSceneStyles(atlas1);

        // Menu
        ellipseMenuLeft = atlas1.findRegion("elipseMenuIzq");
        titleMenuBox = new NinePatchDrawable(atlas1.createPatch("tituloMenuRecuadro"));

        // Game
        recuadroInGameStatus = new NinePatchDrawable(atlas1.createPatch("recuadroInGameStatus"));
        btLeft = new TextureRegionDrawable(atlas1.findRegion("btLeft"));
        btRight = new TextureRegionDrawable(atlas1.findRegion("btRight"));
        btFire = new TextureRegionDrawable(atlas1.findRegion("btFire"));
        btFireDown = new TextureRegionDrawable(atlas1.findRegion("btFire"));
        btMissil = new TextureRegionDrawable(atlas1.findRegion("btMissil"));
        btMissilDown = new TextureRegionDrawable(atlas1.findRegion("btMissil"));

        background = atlas1.findRegion("fondo");

        buttonSignInUp = new NinePatchDrawable(new NinePatch(atlas1.createPatch("btSignInUp")));
        buttonSignInDown = new NinePatchDrawable(new NinePatch(atlas1.createPatch("btSignInDown")));

        /* Ayuda */
        help1 = atlas1.findRegion("help1");
        clickHelp = atlas1.findRegion("ayudaClick");

        /* Botones */
        buttonMusicOn = new TextureRegionDrawable(atlas1.findRegion("btMusica"));
        buttonMusicOff = new TextureRegionDrawable(atlas1.findRegion("btSinMusica"));
        buttonSoundOn = new TextureRegionDrawable(atlas1.findRegion("btSonido"));
        buttonSoundOff = new TextureRegionDrawable(atlas1.findRegion("btSinSonido"));

        /* Ship */
        shipRight = atlas1.findRegion("naveRight");
        shipLeft = atlas1.findRegion("naveLeft");
        ship = atlas1.findRegion("nave");

        AtlasRegion shield0 = atlas1.findRegion("shield0");
        AtlasRegion shield1 = atlas1.findRegion("shield1");
        AtlasRegion shield2 = atlas1.findRegion("shield2");
        AtlasRegion shield3 = atlas1.findRegion("shield3");
        AtlasRegion shield4 = atlas1.findRegion("shield4");
        AtlasRegion shield5 = atlas1.findRegion("shield5");
        AtlasRegion shield6 = atlas1.findRegion("shield6");
        AtlasRegion shield7 = atlas1.findRegion("shield7");
        AtlasRegion shield8 = atlas1.findRegion("shield9");
        AtlasRegion shield9 = atlas1.findRegion("shield9");
        AtlasRegion shield10 = atlas1.findRegion("shield10");
        AtlasRegion shield11 = atlas1.findRegion("shield11");
        shield = new Animation<>(.1f, shield0, shield1, shield2, shield3, shield4, shield5, shield6, shield7, shield8, shield9, shield10, shield11);

        // UFOs
        alien1 = atlas1.findRegion("alien1");
        alien2 = atlas1.findRegion("alien2");
        alien3 = atlas1.findRegion("alien3");
        alien4 = atlas1.findRegion("alien4");

        boost1 = atlas1.findRegion("upgLaser");
        boost2 = atlas1.findRegion("upgBomb");
        boost3 = atlas1.findRegion("upgShield");
        upgLife = atlas1.findRegion("upgLife");

        // Ammunition
        balaNormal = atlas1.findRegion("balaNormal");
        bulletNormalEnemy = atlas1.findRegion("balaNormalEnemigo");

        AtlasRegion misil1 = atlas1.findRegion("misil1");
        AtlasRegion misil2 = atlas1.findRegion("misil2");
        missile = new Animation<>(0.2f, misil1, misil2);

        AtlasRegion superRayo1 = atlas1.findRegion("superRayo1");
        AtlasRegion superRayo2 = atlas1.findRegion("superRayo2");
        superLightning = new Animation<>(0.2f, superRayo1, superRayo2);

        bulletLevel1 = atlas1.findRegion("disparoA1");
        bulletLevel2 = atlas1.findRegion("disparoA2");
        bulletLevel3 = atlas1.findRegion("disparoA3");
        bulletLevel4 = atlas1.findRegion("disparoA4");

        // explosion fire
        AtlasRegion newExplosion1 = atlas1.findRegion("newExplosion1");
        AtlasRegion newExplosion2 = atlas1.findRegion("newExplosion2");
        AtlasRegion newExplosion3 = atlas1.findRegion("newExplosion3");
        AtlasRegion newExplosion4 = atlas1.findRegion("newExplosion4");
        AtlasRegion newExplosion5 = atlas1.findRegion("newExplosion5");
        AtlasRegion newExplosion6 = atlas1.findRegion("newExplosion6");
        AtlasRegion newExplosion7 = atlas1.findRegion("newExplosion7");
        AtlasRegion newExplosion8 = atlas1.findRegion("newExplosion8");
        AtlasRegion newExplosion9 = atlas1.findRegion("newExplosion9");
        AtlasRegion newExplosion10 = atlas1.findRegion("newExplosion10");
        AtlasRegion newExplosion11 = atlas1.findRegion("newExplosion11");
        AtlasRegion newExplosion12 = atlas1.findRegion("newExplosion12");
        AtlasRegion newExplosion13 = atlas1.findRegion("newExplosion13");
        AtlasRegion newExplosion14 = atlas1.findRegion("newExplosion14");
        AtlasRegion newExplosion15 = atlas1.findRegion("newExplosion15");
        AtlasRegion newExplosion16 = atlas1.findRegion("newExplosion16");
        AtlasRegion newExplosion17 = atlas1.findRegion("newExplosion17");
        AtlasRegion newExplosion18 = atlas1.findRegion("newExplosion18");
        AtlasRegion newExplosion19 = atlas1.findRegion("newExplosion19");
        explosionFuego = new Animation<>(0.05f, newExplosion1, newExplosion2, newExplosion3, newExplosion4, newExplosion5, newExplosion6, newExplosion7, newExplosion8, newExplosion9, newExplosion10, newExplosion11, newExplosion12, newExplosion13, newExplosion14, newExplosion15, newExplosion16, newExplosion17, newExplosion18, newExplosion19);

        ParallaxLayer para1 = new ParallaxLayer(background, new Vector2(0, 50), new Vector2(0, 0));
        ParallaxLayer[] arr = new ParallaxLayer[]{para1};
        parallaxBackground = new ParallaxBackground(arr, 320, 480, new Vector2(0, 1));

        music = Gdx.audio.newMusic(Gdx.files.internal("data/sonidos/musica.mp3"));
        music.setLooping(true);
        music.setVolume(0.1f);
        coinSound = Gdx.audio.newSound(Gdx.files.internal("data/sonidos/coin.ogg"));
        clickSound = Gdx.audio.newSound(Gdx.files.internal("data/sonidos/click.ogg"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("data/sonidos/sound_explode.ogg"));
        missileFire = Gdx.audio.newSound(Gdx.files.internal("data/sonidos/missilFire3.ogg"));

        Settings.load();
        if (Settings.musicEnabled)
            music.play();

    }

    public static void playSound(Sound sound, float volumen) {
        if (Settings.soundEnabled)
            sound.play(volumen);
    }

    public static void playSound(Sound sound) {
        if (Settings.soundEnabled)
            sound.play(1);
    }

    public static float getTextWidth(BitmapFont font, String text) {
        glyphLayout.setText(font, text);
        return glyphLayout.width;
    }
}
