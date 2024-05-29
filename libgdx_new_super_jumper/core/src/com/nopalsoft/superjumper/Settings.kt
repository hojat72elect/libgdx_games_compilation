package com.nopalsoft.superjumper

import com.badlogic.gdx.Gdx

object Settings {


    private var isMusicOn = true
    private var isSoundOn = true

    private var didBuyNoAds = false
    private var didLikeFacebook = false
    private var didRate = false

    @JvmStatic
    var numberOfTimesPlayed = 0

    private var coinsTotal = 0

    @JvmStatic
    var numBullets = 0

    @JvmStatic
    var bestScore = 0

    private var LEVEL_LIFE = 0
    private var LEVEL_SHIELD = 0
    private var LEVEL_SECOND_JUMP = 0
    private var LEVEL_WEAPON = 0

    private val pref = Gdx.app.getPreferences("com.nopalsoft.superjumper")

    @JvmStatic
    fun save() {
        pref.putBoolean("isMusicOn", isMusicOn)
        pref.putBoolean("isSoundOn", isSoundOn)

        pref.putBoolean("didBuyNoAds", didBuyNoAds)
        pref.putBoolean("didLikeFacebook", didLikeFacebook)
        pref.putBoolean("didRate", didRate)

        pref.putInteger("numeroVecesJugadas", numberOfTimesPlayed)
        pref.putInteger("coinsTotal", coinsTotal)
        pref.putInteger("numBullets", numBullets)
        pref.putInteger("bestScore", bestScore)

        pref.putInteger("LEVEL_WEAPON", LEVEL_WEAPON)
        pref.putInteger("LEVEL_SECOND_JUMP", LEVEL_SECOND_JUMP)
        pref.putInteger("LEVEL_LIFE", LEVEL_LIFE)
        pref.putInteger("LEVEL_SHIELD", LEVEL_SHIELD)

        pref.flush()
    }

    @JvmStatic
    fun load() {
        isMusicOn = pref.getBoolean("isMusicOn", true)
        isSoundOn = pref.getBoolean("isSoundOn", true)

        didBuyNoAds = pref.getBoolean("didBuyNoAds", false)
        didLikeFacebook = pref.getBoolean("didLikeFacebook", false)
        didRate = pref.getBoolean("didRate", false)

        numberOfTimesPlayed = pref.getInteger("numeroVecesJugadas", 0)

        coinsTotal = pref.getInteger("coinsTotal", 0)
        numBullets = pref.getInteger("numBullets", 30)
        bestScore = pref.getInteger("bestScore", 0)

        LEVEL_WEAPON = pref.getInteger("LEVEL_WEAPON", 0)
        LEVEL_SECOND_JUMP = pref.getInteger("LEVEL_SECOND_JUMP", 0)
        LEVEL_LIFE = pref.getInteger("LEVEL_LIFE", 0)
        LEVEL_SHIELD = pref.getInteger("LEVEL_SHIELD", 0)
    }

    @JvmStatic
    fun changeBestScore(distance: Int) {
        if (bestScore < distance) {
            bestScore = distance
            save()
        }
    }
}