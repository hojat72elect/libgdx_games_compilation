package com.nopalsoft.slamthebird

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences

object Settings {
    private val pref: Preferences = Gdx.app.getPreferences("com.nopalsoft.slamthebird")

    @JvmField
    var currentCoins: Int = 0

    @JvmField
    var bestScore: Int = 0

    @JvmField
    var selectedSkin: Int = 0

    @JvmField
    var numberOfTimesPlayed: Int = 0

    @JvmField
    var LEVEL_BOOST_TIME: Int = 0

    @JvmField
    var LEVEL_BOOST_ICE: Int = 0

    @JvmField
    var LEVEL_BOOST_COINS: Int = 0

    @JvmField
    var LEVEL_BOOST_INVINCIBLE: Int = 0

    @JvmField
    var LEVEL_BOOST_SUPER_JUMP: Int = 0

    @JvmField
    var isMusicOn: Boolean = false

    @JvmField
    var isSoundOn: Boolean = false

    @JvmField
    var didBuyNoAds: Boolean = false

    @JvmField
    var didLikeFacebook: Boolean = false

    @JvmField
    var isQualified: Boolean = false

    @JvmStatic
    fun save() {
        pref.putInteger("monedasActuales", currentCoins)
        pref.putInteger("bestScore", bestScore)
        pref.putInteger("skinSeleccionada", selectedSkin)
        pref.putInteger("numeroVecesJugadas", numberOfTimesPlayed)
        pref.putInteger("NIVEL_BOOST_BOOST_TIME", LEVEL_BOOST_TIME)
        pref.putInteger("NIVEL_BOOST_ICE", LEVEL_BOOST_ICE)
        pref.putInteger("NIVEL_BOOST_MONEDAS", LEVEL_BOOST_COINS)
        pref.putInteger("NIVEL_BOOST_INVENCIBLE", LEVEL_BOOST_INVINCIBLE)
        pref.putInteger("NIVEL_BOOST_SUPER_JUMP", LEVEL_BOOST_SUPER_JUMP)

        pref.putBoolean("isMusicOn", isMusicOn)
        pref.putBoolean("isSoundOn", isSoundOn)

        pref.putBoolean("didBuyNoAds", didBuyNoAds)
        pref.putBoolean("didLikeFacebook", didLikeFacebook)
        pref.putBoolean("seCalifico", isQualified)
        pref.flush()
    }

    @JvmStatic
    fun load() {
        currentCoins = pref.getInteger("monedasActuales", 0)
        bestScore = pref.getInteger("bestScore", 0)
        selectedSkin = pref.getInteger("skinSeleccionada", 0)
        numberOfTimesPlayed = pref.getInteger("numeroVecesJugadas", 0)
        LEVEL_BOOST_TIME = pref.getInteger("NIVEL_BOOST_BOOST_TIME", 0)
        LEVEL_BOOST_ICE = pref.getInteger("NIVEL_BOOST_ICE", 0)
        LEVEL_BOOST_COINS = pref.getInteger("NIVEL_BOOST_MONEDAS", 0)
        LEVEL_BOOST_INVINCIBLE = pref.getInteger("NIVEL_BOOST_INVENCIBLE", 0)
        LEVEL_BOOST_SUPER_JUMP = pref.getInteger("NIVEL_BOOST_SUPER_JUMP", 0)

        isMusicOn = pref.getBoolean("isMusicOn", true)
        isSoundOn = pref.getBoolean("isSoundOn", true)

        didBuyNoAds = pref.getBoolean("didBuyNoAds", false)
        didLikeFacebook = pref.getBoolean("didLikeFacebook", false)
        isQualified = pref.getBoolean("seCalifico", false)
    }

    @JvmStatic
    fun setBestScores(score: Int) {
        if (bestScore < score) bestScore = score
        save()
    }
}
