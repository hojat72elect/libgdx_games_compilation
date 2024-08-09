package com.nopalsoft.slamthebird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {

    private final static Preferences pref = Gdx.app.getPreferences("com.nopalsoft.slamthebird");
    public static int currentCoins = 0;
    public static int bestScore;
    public static int selectedSkin = 0;
    public static int numberOfTimesPlayed = 0;
    public static int LEVEL_BOOST_TIME = 0;
    public static int LEVEL_BOOST_ICE = 0;
    public static int LEVEL_BOOST_COINS = 0;
    public static int LEVEL_BOOST_INVINCIBLE = 0;
    public static int LEVEL_BOOST_SUPER_JUMP = 0;
    public static boolean isMusicOn;
    public static boolean isSoundOn;
    public static boolean didBuyNoAds;
    public static boolean didLikeFacebook;
    public static boolean isQualified;

    public static void save() {
        pref.putInteger("monedasActuales", currentCoins);
        pref.putInteger("bestScore", bestScore);
        pref.putInteger("skinSeleccionada", selectedSkin);
        pref.putInteger("numeroVecesJugadas", numberOfTimesPlayed);
        pref.putInteger("NIVEL_BOOST_BOOST_TIME", LEVEL_BOOST_TIME);
        pref.putInteger("NIVEL_BOOST_ICE", LEVEL_BOOST_ICE);
        pref.putInteger("NIVEL_BOOST_MONEDAS", LEVEL_BOOST_COINS);
        pref.putInteger("NIVEL_BOOST_INVENCIBLE", LEVEL_BOOST_INVINCIBLE);
        pref.putInteger("NIVEL_BOOST_SUPER_JUMP", LEVEL_BOOST_SUPER_JUMP);

        pref.putBoolean("isMusicOn", isMusicOn);
        pref.putBoolean("isSoundOn", isSoundOn);

        pref.putBoolean("didBuyNoAds", didBuyNoAds);
        pref.putBoolean("didLikeFacebook", didLikeFacebook);
        pref.putBoolean("seCalifico", isQualified);
        pref.flush();

    }

    public static void load() {
        currentCoins = pref.getInteger("monedasActuales", 0);
        bestScore = pref.getInteger("bestScore", 0);
        selectedSkin = pref.getInteger("skinSeleccionada", 0);
        numberOfTimesPlayed = pref.getInteger("numeroVecesJugadas", 0);
        LEVEL_BOOST_TIME = pref.getInteger("NIVEL_BOOST_BOOST_TIME", 0);
        LEVEL_BOOST_ICE = pref.getInteger("NIVEL_BOOST_ICE", 0);
        LEVEL_BOOST_COINS = pref.getInteger("NIVEL_BOOST_MONEDAS", 0);
        LEVEL_BOOST_INVINCIBLE = pref.getInteger("NIVEL_BOOST_INVENCIBLE", 0);
        LEVEL_BOOST_SUPER_JUMP = pref.getInteger("NIVEL_BOOST_SUPER_JUMP", 0);

        isMusicOn = pref.getBoolean("isMusicOn", true);
        isSoundOn = pref.getBoolean("isSoundOn", true);

        didBuyNoAds = pref.getBoolean("didBuyNoAds", false);
        didLikeFacebook = pref.getBoolean("didLikeFacebook", false);
        isQualified = pref.getBoolean("seCalifico", false);

    }

    public static void setBestScores(int score) {
        if (bestScore < score)
            bestScore = score;
        save();

    }

}
