package com.nopalsoft.ninjarunner

import com.badlogic.gdx.Gdx
import com.nopalsoft.ninjarunner.objects.Pet.PetType
import com.nopalsoft.ninjarunner.objects.Player

object Settings {
    private val pref = Gdx.app.getPreferences("com.nopalsoft.ninjarunner.settings")

    @JvmStatic
    var isSoundEnabled = false

    @JvmStatic
    var isMusicEnabled = false

    @JvmStatic
    var selectedSkin = Player.TYPE_NINJA

    @JvmStatic
    var totalCoins = 1_500_000

    @JvmStatic
    var selectedPet = PetType.PINK_BIRD

    @JvmStatic
    var LEVEL_PET_BIRD = 0

    @JvmStatic
    var LEVEL_PET_BOMB = 0

    @JvmStatic
    var LEVEL_MAGNET = 0

    @JvmStatic
    var LEVEL_LIFE = 0

    @JvmStatic
    var LEVEL_ENERGY = 0

    @JvmStatic
    var LEVEL_COINS = 0

    @JvmStatic
    var LEVEL_TREASURE_CHEST = 0

    @JvmStatic
    var bestScore = 0L

    @JvmStatic
    fun load() {
        LEVEL_MAGNET = pref.getInteger("LEVEL_MAGNET", 0)
        LEVEL_LIFE = pref.getInteger("LEVEL_LIFE", 0)
        LEVEL_ENERGY = pref.getInteger("LEVEL_ENERGY", 0)
        LEVEL_COINS = pref.getInteger("LEVEL_COINS", 0)
        LEVEL_TREASURE_CHEST = pref.getInteger("LEVEL_TREASURE_CHEST", 0)
    }

    @JvmStatic
    fun save() {
        // FIXME: looks like we're not doing anything for saving the settings of the game.
    }

    @JvmStatic
    fun setNewScore(score: Long) {
        if (score > bestScore) {
            bestScore = score
        }
    }
}