package com.nopalsoft.slamthebird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Assets {

    public static AtlasRegion title;
    public static AtlasRegion tapToPlay;
    public static AtlasRegion bestScore;
    public static AtlasRegion score;
    public static AtlasRegion combo;
    public static AtlasRegion coinsEarned;
    public static AtlasRegion shop;
    public static NinePatchDrawable horizontalSeparator;
    public static NinePatchDrawable verticalSeparator;
    public static AtlasRegion background;
    public static AtlasRegion backgroundGameOver;
    public static AtlasRegion player;
    public static AtlasRegion playerShopDefault;
    public static AtlasRegion playerShopRed;
    public static AtlasRegion playerShopBlue;
    public static Animation<TextureRegion> animationPlayerJump;
    public static Animation<TextureRegion> animationPlayerSlam;
    public static Animation<TextureRegion> animationPlayerHit;
    public static Animation<TextureRegion> slam;
    public static NinePatchDrawable blackPixel;
    public static AtlasRegion platform;
    public static Animation<TextureRegion> animationPlatformFire;
    public static Animation<TextureRegion> breakablePlatform;
    public static TextureRegionDrawable buttonAchievements;
    public static TextureRegionDrawable buttonLeaderBoard;
    public static TextureRegionDrawable buttonMore;
    public static TextureRegionDrawable buttonRate;
    public static TextureRegionDrawable buttonShop;
    public static TextureRegionDrawable buttonFacebook;
    public static TextureRegionDrawable buttonTwitter;
    public static TextureRegionDrawable buttonBack;
    public static TextureRegionDrawable buttonNoAds;
    public static ButtonStyle buttonStyleMusic;
    public static ButtonStyle buttonStyleSound;
    public static TextureRegionDrawable buttonUpgradeOn;
    public static TextureRegionDrawable buttonUpgradeOff;
    public static TextureRegionDrawable buttonScores;
    public static AtlasRegion birdSpawn;
    public static AtlasRegion birdBlue;
    public static Animation<TextureRegion> animationBlueBirdFlap;
    public static Animation<TextureRegion> animationRedBirdFlap;
    public static Animation<TextureRegion> animationEvolving;
    public static Animation<TextureRegion> animationCoin;
    public static AtlasRegion coin;
    public static AtlasRegion transparentPixel;
    public static AtlasRegion invincibleBoost;
    public static AtlasRegion coinRainBoost;
    public static AtlasRegion IceBoost;
    public static AtlasRegion superJumpBoost;
    public static AtlasRegion boosts;
    public static Animation<TextureRegion> animationInvincibleBoostEnd;
    public static Animation<TextureRegion> animationSuperJumpBoostEnd;
    public static AtlasRegion num0Big;
    public static AtlasRegion num1Big;
    public static AtlasRegion num2Big;
    public static AtlasRegion num3Big;
    public static AtlasRegion num4Big;
    public static AtlasRegion num5Big;
    public static AtlasRegion num6Big;
    public static AtlasRegion num7Big;
    public static AtlasRegion num8Big;
    public static AtlasRegion num9Big;
    public static AtlasRegion num0Small;
    public static AtlasRegion num1Small;
    public static AtlasRegion num2Small;
    public static AtlasRegion num3Small;
    public static AtlasRegion num4Small;
    public static AtlasRegion num5Small;
    public static AtlasRegion num6Small;
    public static AtlasRegion num7Small;
    public static AtlasRegion num8Small;
    public static AtlasRegion num9Small;
    public static BitmapFont font;
    public static TextButtonStyle textButtonStyleBuy;
    public static TextButtonStyle textButtonStylePurchased;
    public static TextButtonStyle textButtonStyleSelected;
    public static ScrollPaneStyle scrollPaneStyle;
    public static LabelStyle labelStyleSmall;
    public static Sound soundJump;
    public static Sound soundCoin;
    public static Sound soundBoost;
    static TextureAtlas textureAtlas;
    static Music music;

    public static void loadStyles() {
        font = new BitmapFont();
        font.getData().setScale(1.15f);

        horizontalSeparator = new NinePatchDrawable(new NinePatch(
                textureAtlas.findRegion("Shop/separadorHorizontal"), 0, 1, 0, 0));
        verticalSeparator = new NinePatchDrawable(new NinePatch(
                textureAtlas.findRegion("Shop/separadorVertical"), 0, 1, 0, 0));

        labelStyleSmall = new LabelStyle(font, Color.WHITE);

        // Button Buy
        TextureRegionDrawable buttonBuy = new TextureRegionDrawable(
                textureAtlas.findRegion("Shop/btBuy"));
        textButtonStyleBuy = new TextButtonStyle(buttonBuy, null, null, font);


        // Button Purchased
        TextureRegionDrawable buttonPurchased = new TextureRegionDrawable(
                textureAtlas.findRegion("Shop/btPurchased"));
        textButtonStylePurchased = new TextButtonStyle(buttonPurchased, null, null,
                font);

        // Button Selected
        TextureRegionDrawable buttonSelected = new TextureRegionDrawable(
                textureAtlas.findRegion("Shop/btSelected"));
        textButtonStyleSelected = new TextButtonStyle(buttonSelected, null, null,
                font);

        // Button Music
        TextureRegionDrawable buttonMusicOn = new TextureRegionDrawable(
                textureAtlas.findRegion("MenuPrincipal/btMusica"));
        TextureRegionDrawable buttonMusicOff = new TextureRegionDrawable(
                textureAtlas.findRegion("MenuPrincipal/btSinMusica"));
        buttonStyleMusic = new ButtonStyle(buttonMusicOn, null, buttonMusicOff);

        // Button Sound
        TextureRegionDrawable buttonSoundOn = new TextureRegionDrawable(
                textureAtlas.findRegion("MenuPrincipal/btSonido"));
        TextureRegionDrawable buttonSoundOff = new TextureRegionDrawable(
                textureAtlas.findRegion("MenuPrincipal/btSinSonido"));
        buttonStyleSound = new ButtonStyle(buttonSoundOn, null, buttonSoundOff);

        scrollPaneStyle = new ScrollPaneStyle(null, null, null, null,
                verticalSeparator);
    }

    public static void loadPlayer() {

        String selectedPlayer = "AndroidBot";

        if (Settings.selectedSkin == com.nopalsoft.slamthebird.shop.PlayersSubMenu.SKIN_ANDROID_RED) {
            selectedPlayer = "AndroidBotRojo";
        } else if (Settings.selectedSkin == com.nopalsoft.slamthebird.shop.PlayersSubMenu.SKIN_ANDROID_BLUE) {
            selectedPlayer = "AndroidBotAzul";
        }

        player = textureAtlas.findRegion("Personajes/" + selectedPlayer
                + "/personajeFall");

        AtlasRegion per1 = textureAtlas.findRegion("Personajes/" + selectedPlayer
                + "/personajeSlam1");
        AtlasRegion per2 = textureAtlas.findRegion("Personajes/" + selectedPlayer
                + "/personajeSlam2");
        AtlasRegion per3 = textureAtlas.findRegion("Personajes/" + selectedPlayer
                + "/personajeSlam3");
        AtlasRegion per4 = textureAtlas.findRegion("Personajes/" + selectedPlayer
                + "/personajeSlam4");
        animationPlayerSlam = new Animation<>(.05f, per1, per2, per3, per4);

        per1 = textureAtlas.findRegion("Personajes/" + selectedPlayer
                + "/personajeJump1");
        per2 = textureAtlas.findRegion("Personajes/" + selectedPlayer
                + "/personajeJump1");
        per3 = textureAtlas.findRegion("Personajes/" + selectedPlayer
                + "/personajeJump1");
        animationPlayerJump = new Animation<>(.1f, per1, per2, per3);

        per1 = textureAtlas.findRegion("Personajes/" + selectedPlayer
                + "/personajeHit");
        per2 = textureAtlas.findRegion("Personajes/" + selectedPlayer
                + "/personajeHit");
        per3 = textureAtlas.findRegion("Personajes/" + selectedPlayer
                + "/personajeHit");
        animationPlayerHit = new Animation<>(.1f, per1, per2, per3);

        // Estos son los que aparecen en la tienda;
        playerShopDefault = textureAtlas
                .findRegion("Personajes/AndroidBot/personajeFall");
        playerShopRed = textureAtlas
                .findRegion("Personajes/AndroidBotRojo/personajeFall");
        playerShopBlue = textureAtlas
                .findRegion("Personajes/AndroidBotAzul/personajeFall");
    }

    public static void load() {

        textureAtlas = new TextureAtlas(Gdx.files.internal("data/atlasMap.txt"));

        loadStyles();

        title = textureAtlas.findRegion("MenuPrincipal/titulo");
        tapToPlay = textureAtlas.findRegion("MenuPrincipal/tapToPlay");
        bestScore = textureAtlas.findRegion("MenuPrincipal/bestScore");
        score = textureAtlas.findRegion("MenuPrincipal/score");
        combo = textureAtlas.findRegion("MenuPrincipal/combo");
        coinsEarned = textureAtlas.findRegion("MenuPrincipal/coinsEarned");

        background = textureAtlas.findRegion("fondo");
        buttonScores = new TextureRegionDrawable(
                textureAtlas.findRegion("MenuPrincipal/fondoPuntuaciones"));
        backgroundGameOver = textureAtlas.findRegion("fondoGameover");

        transparentPixel = textureAtlas.findRegion("pixelTransparente");

        shop = textureAtlas.findRegion("Shop/Shop");

        buttonBack = new TextureRegionDrawable(textureAtlas.findRegion("Shop/btAtras"));
        buttonNoAds = new TextureRegionDrawable(textureAtlas.findRegion("Shop/btNoAds"));

        buttonUpgradeOff = new TextureRegionDrawable(
                textureAtlas.findRegion("Shop/upgradeOff"));
        buttonUpgradeOn = new TextureRegionDrawable(
                textureAtlas.findRegion("Shop/upgradeOn"));

        blackPixel = new NinePatchDrawable(new NinePatch(
                textureAtlas.findRegion("MenuPrincipal/pixelNegro"), 1, 1, 0, 0));

        AtlasRegion per1 = textureAtlas.findRegion("moneda1");
        AtlasRegion per2 = textureAtlas.findRegion("moneda2");
        AtlasRegion per3 = textureAtlas.findRegion("moneda3");
        coin = per1;
        animationCoin = new Animation<>(.15f, per1, per2, per3, per2);

        birdBlue = textureAtlas.findRegion("InGame/flapAzul");
        birdSpawn = textureAtlas.findRegion("InGame/flapSpawn");

        AtlasRegion flap1 = textureAtlas.findRegion("InGame/flapAzulAlas1");
        AtlasRegion flap2 = textureAtlas.findRegion("InGame/flapAzulAlas2");
        AtlasRegion flap3 = textureAtlas.findRegion("InGame/flapAzulAlas3");
        animationBlueBirdFlap = new Animation<>(.15f, flap1, flap2, flap3, flap2);

        flap1 = textureAtlas.findRegion("InGame/flapRojoAlas1");
        flap2 = textureAtlas.findRegion("InGame/flapRojoAlas2");
        flap3 = textureAtlas.findRegion("InGame/flapRojoAlas3");
        animationRedBirdFlap = new Animation<>(.15f, flap1, flap2, flap3, flap2);
        animationEvolving = new Animation<>(.075f, birdBlue, flap1, birdBlue, flap2,
                birdBlue, flap3);

        flap1 = textureAtlas.findRegion("InGame/plataformFire1");
        flap2 = textureAtlas.findRegion("InGame/plataformFire2");
        flap3 = textureAtlas.findRegion("InGame/plataformFire3");
        animationPlatformFire = new Animation<>(.15f, flap1, flap2, flap3, flap2);

        flap1 = textureAtlas.findRegion("InGame/plataforma2");
        flap2 = textureAtlas.findRegion("InGame/plataforma3");
        flap3 = textureAtlas.findRegion("InGame/plataforma4");
        breakablePlatform = new Animation<>(.1f, flap1, flap2, flap3);
        platform = textureAtlas.findRegion("InGame/plataforma1");

        flap1 = textureAtlas.findRegion("InGame/slam1");
        flap2 = textureAtlas.findRegion("InGame/slam2");
        flap3 = textureAtlas.findRegion("InGame/slam3");
        slam = new Animation<>(.1f, flap1, flap2, flap3);

        invincibleBoost = textureAtlas.findRegion("InGame/boostInvencible");
        coinRainBoost = textureAtlas.findRegion("InGame/boostCoinRain");
        IceBoost = textureAtlas.findRegion("InGame/boostIce");
        superJumpBoost = textureAtlas.findRegion("InGame/boostSuperSalto");
        boosts = textureAtlas.findRegion("InGame/boosts");

        animationInvincibleBoostEnd = new Animation<>(.15f, invincibleBoost,
                transparentPixel);
        animationSuperJumpBoostEnd = new Animation<>(.15f, superJumpBoost,
                transparentPixel);

        buttonAchievements = new TextureRegionDrawable(
                textureAtlas.findRegion("MenuPrincipal/btAchievements"));
        buttonLeaderBoard = new TextureRegionDrawable(
                textureAtlas.findRegion("MenuPrincipal/btLeaderboard"));
        buttonMore = new TextureRegionDrawable(
                textureAtlas.findRegion("MenuPrincipal/btMore"));
        buttonRate = new TextureRegionDrawable(
                textureAtlas.findRegion("MenuPrincipal/btRate"));
        buttonShop = new TextureRegionDrawable(
                textureAtlas.findRegion("MenuPrincipal/btShop"));
        buttonFacebook = new TextureRegionDrawable(
                textureAtlas.findRegion("MenuPrincipal/btFacebook"));
        buttonTwitter = new TextureRegionDrawable(
                textureAtlas.findRegion("MenuPrincipal/btTwitter"));

        num0Big = textureAtlas.findRegion("Numeros/num0");
        num1Big = textureAtlas.findRegion("Numeros/num1");
        num2Big = textureAtlas.findRegion("Numeros/num2");
        num3Big = textureAtlas.findRegion("Numeros/num3");
        num4Big = textureAtlas.findRegion("Numeros/num4");
        num5Big = textureAtlas.findRegion("Numeros/num5");
        num6Big = textureAtlas.findRegion("Numeros/num6");
        num7Big = textureAtlas.findRegion("Numeros/num7");
        num8Big = textureAtlas.findRegion("Numeros/num8");
        num9Big = textureAtlas.findRegion("Numeros/num9");

        num0Small = textureAtlas.findRegion("Numeros/0");
        num1Small = textureAtlas.findRegion("Numeros/1");
        num2Small = textureAtlas.findRegion("Numeros/2");
        num3Small = textureAtlas.findRegion("Numeros/3");
        num4Small = textureAtlas.findRegion("Numeros/4");
        num5Small = textureAtlas.findRegion("Numeros/5");
        num6Small = textureAtlas.findRegion("Numeros/6");
        num7Small = textureAtlas.findRegion("Numeros/7");
        num8Small = textureAtlas.findRegion("Numeros/8");
        num9Small = textureAtlas.findRegion("Numeros/9");

        Settings.load();

        // It should be called after loading settings
        loadPlayer();

        soundCoin = Gdx.audio.newSound(Gdx.files
                .internal("data/Sounds/pickCoin.mp3"));
        soundJump = Gdx.audio.newSound(Gdx.files
                .internal("data/Sounds/salto.mp3"));
        soundBoost = Gdx.audio.newSound(Gdx.files
                .internal("data/Sounds/pickBoost.mp3"));

        music = Gdx.audio.newMusic(Gdx.files
                .internal("data/Sounds/musica.mp3"));

        music.setLooping(true);

        if (Settings.isMusicOn)
            playMusic();

    }

    public static void playSound(Sound sound) {
        if (Settings.isSoundOn)
            sound.play(1);
    }

    public static void playMusic() {
        music.play();
    }

    public static void pauseMusic() {
        music.pause();
    }
}
