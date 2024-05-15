package com.nopalsoft.invaders

import com.badlogic.gdx.Gdx

object Settings {

    @JvmStatic
    val highScores = intArrayOf(0, 0, 0, 0, 0)// only 5 scores are saved
    private const val prefName = "com.tiarsoft.droid.Settings"
    private val pref = Gdx.app.getPreferences(prefName)

    const val drawDebugLines = false

    @JvmStatic
    var soundEnabled = false

    @JvmStatic
    var musicEnabled = false

    @JvmStatic
    var tiltControlEnabled = true

    @JvmStatic
    var accelerometerSensitivity = 10

    @JvmStatic
    var numberOfTimesGameHasBeenPlayed = 0

    @JvmStatic
    fun load() {
        tiltControlEnabled = pref.getBoolean("isTiltControl", true)
        soundEnabled = pref.getBoolean("sonidoActivado", false)
        musicEnabled = pref.getBoolean("musicaActivado", false)
        for (i in 0..4) { // only 5 scores are loaded
            highScores[i] = pref.getString(
                "puntuacion$i", "0"
            ).toInt()
        }
        accelerometerSensitivity =
            pref.getInteger("acelerometerSensitive", 10)
        numberOfTimesGameHasBeenPlayed =
            pref.getInteger("numeroDeVecesQueSeHaJugado", 0)
    }

    @JvmStatic
    fun save() {
        pref.putBoolean("isTiltControl", tiltControlEnabled)

        pref.putBoolean("sonidoActivado", soundEnabled)
        pref.putBoolean("musicaActivado", musicEnabled)

        for (i in 0..4) { // only 5 scores are uploaded
            pref.putString("puntuacion$i", highScores[i].toString())
        }
        pref.putInteger("acelerometerSensitive", accelerometerSensitivity)
        pref.putInteger(
            "numeroDeVecesQueSeHaJugado",
            numberOfTimesGameHasBeenPlayed
        )
        pref.flush()
    }

    @JvmStatic
    fun addScore(puntuacion: Int) {
        for (i in 0..4) {
            if (highScores[i] < puntuacion) {
                for (j in 4 downTo i + 1) highScores[j] = highScores[j - 1]
                highScores[i] = puntuacion
                break
            }
        }
    }
}