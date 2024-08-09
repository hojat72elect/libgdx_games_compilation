package com.nopalsoft.slamthebird

import com.badlogic.gdx.Gdx

object Achievements {
    var didInit: Boolean = false


    var SuperJump: String? = null
    var SlamCombo: String? = null
    var SuperSlam: String? = null
    var InvencibleSlam: String? = null
    var YouGotAnySpareChange: String? = null
    var coinMaster: String? = null

    fun init() {
        SuperJump = "20274"
        SlamCombo = "20276"
        SuperSlam = "20278"
        InvencibleSlam = "20280"
        YouGotAnySpareChange = "20282"
        coinMaster = "20284"

        didInit = true
    }

    private fun didInit() {
        if (didInit) return
        Gdx.app.log("Achievements", "You must call first Achievements.init()")
    }

    @JvmStatic
    fun unlockSuperJump() {
        didInit()
    }

    @JvmStatic
    fun unlockCombos() {
        didInit()
    }

    @JvmStatic
    fun unlockCoins() {
        didInit()
    }
}
