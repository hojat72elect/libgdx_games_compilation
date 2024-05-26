package com.nopalsoft.ninjarunner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.nopalsoft.ninjarunner.objects.Pet.PetType;
import com.nopalsoft.ninjarunner.objects.Player;

public class Settings {

    private final static Preferences pref = Gdx.app.getPreferences("com.nopalsoft.ninjarunner.settings");
    public static boolean isSoundEnabled = false;
    public static boolean isMusicEnabled = false;
    public static int selectedSkin = Player.TYPE_NINJA;
    public static int totalCoins = 1500000;
    public static PetType selectedPet = PetType.PINK_BIRD;

    public static int LEVEL_PET_BIRD;
    public static int LEVEL_PET_BOMB;

    public static int LEVEL_MAGNET;
    public static int LEVEL_LIFE;
    public static int LEVEL_ENERGY;
    public static int LEVEL_COINS;
    public static int LEVEL_TREASURE_CHEST;
    public static long bestScore;

    public static void load() {
        LEVEL_MAGNET = pref.getInteger("LEVEL_MAGNET", 0);
        LEVEL_LIFE = pref.getInteger("LEVEL_LIFE", 0);
        LEVEL_ENERGY = pref.getInteger("LEVEL_ENERGY", 0);
        LEVEL_COINS = pref.getInteger("LEVEL_COINS", 0);
        LEVEL_TREASURE_CHEST = pref.getInteger("LEVEL_TREASURE_CHEST", 0);

    }

    public static void save() {

    }

    public static void setNewScore(long score) {
        if (score > bestScore) {
            bestScore = score;
        }
    }

}
