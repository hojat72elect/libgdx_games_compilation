package com.nopalsoft.ninjarunner

import com.badlogic.gdx.Gdx
import com.nopalsoft.ninjarunner.objects.Pet
import com.nopalsoft.ninjarunner.objects.Player

object Settings {
    private val pref = Gdx.app.getPreferences("com.nopalsoft.ninjarunner.settings")
    var isSoundEnabled = false
    var isMusicEnabled = false
    var selectedSkin = Player.TYPE_NINJA
    var totalCoins = 1_500_000
    var selectedPet = Pet.PetType.PINK_BIRD
    var LEVEL_PET_BIRD = 0
    var LEVEL_PET_BOMB = 0
    var LEVEL_MAGNET = 0
    var LEVEL_LIFE = 0
    var LEVEL_ENERGY = 0
    var LEVEL_COINS = 0
    var LEVEL_TREASURE_CHEST = 0
    var bestScore = 0L

    fun load() {
        LEVEL_MAGNET = pref.getInteger("LEVEL_MAGNET", 0)
        LEVEL_LIFE = pref.getInteger("LEVEL_LIFE", 0)
        LEVEL_ENERGY = pref.getInteger("LEVEL_ENERGY", 0)
        LEVEL_COINS = pref.getInteger("LEVEL_COINS", 0)
        LEVEL_TREASURE_CHEST = pref.getInteger("LEVEL_TREASURE_CHEST", 0)
    }

    fun save() {
        // FIXME: looks like we're not doing anything for saving the settings of the game.
    }

    fun setNewScore(score: Long) {
        if (score > bestScore) {
            bestScore = score
        }
    }
}