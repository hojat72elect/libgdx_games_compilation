package com.nopalsoft.invaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {

    public final static int[] highScores = new int[]{0, 0, 0, 0, 0};// only 5 scores are saved
    private final static String prefName = "com.tiarsoft.droid.Settings";
    private final static Preferences pref = Gdx.app.getPreferences(prefName);
    public static boolean drawDebugLines = false;
    public static boolean soundEnabled = false;
    public static boolean musicEnabled = false;
    public static boolean isTiltControl = true;
    public static int accelerometerSensitivity = 10;
    public static int numberOfTimesGameHasBeenPlayed = 0;

    public static void load() {

        isTiltControl = pref.getBoolean("isTiltControl", true);

        soundEnabled = pref.getBoolean("sonidoActivado", false);
        musicEnabled = pref.getBoolean("musicaActivado", false);
        for (int i = 0; i < 5; i++) {// solo 5 puntuaciones se cargan
            highScores[i] = Integer.parseInt(pref.getString("puntuacion" + i, "0"));
        }
        accelerometerSensitivity = pref.getInteger("acelerometerSensitive", 10);
        numberOfTimesGameHasBeenPlayed = pref.getInteger("numeroDeVecesQueSeHaJugado", 0);
    }

    public static void save() {
        pref.putBoolean("isTiltControl", isTiltControl);

        pref.putBoolean("sonidoActivado", soundEnabled);
        pref.putBoolean("musicaActivado", musicEnabled);

        for (int i = 0; i < 5; i++) {// solo 5 puntuaciones se cargan
            pref.putString("puntuacion" + i, String.valueOf(highScores[i]));
        }
        pref.putInteger("acelerometerSensitive", accelerometerSensitivity);
        pref.putInteger("numeroDeVecesQueSeHaJugado", numberOfTimesGameHasBeenPlayed);
        pref.flush();
    }

    public static void addScore(int puntuacion) {
        for (int i = 0; i < 5; i++) {
            if (highScores[i] < puntuacion) {
                for (int j = 4; j > i; j--)
                    highScores[j] = highScores[j - 1];
                highScores[i] = puntuacion;
                break;
            }
        }
    }

}
