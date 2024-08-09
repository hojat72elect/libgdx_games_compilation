package com.nopalsoft.slamthebird.shop

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.slamthebird.Assets
import com.nopalsoft.slamthebird.MainSlamBird
import com.nopalsoft.slamthebird.Settings

class UpgradesSubMenu(var game: MainSlamBird, var container: Table) {
    val MAX_LEVEL: Int = 6

    var level1Price: Int = 500
    var level2Price: Int = 1000
    var level3Price: Int = 2500
    var level4Price: Int = 4000
    var level5Price: Int = 5000
    var level6Price: Int = 6000

    var buttonUpgradeBoostTime: TextButton? = null
    var buttonUpgradeIce: TextButton? = null
    var buttonUpgradeCoins: TextButton? = null
    var buttonUpgradeSuperJump: TextButton? = null
    var buttonUpgradeInvincible: TextButton? = null
    var labelPriceBoostTime: Label? = null
    var labelPriceIce: Label? = null
    var labelPriceCoins: Label? = null
    var labelPriceSuperJump: Label? = null
    var labelPriceInvincible: Label? = null

    var arrBoostTime: Array<Image?>
    var arrBoostIce: Array<Image?>
    var arrBoostCoins: Array<Image?>
    var arrBoostSuperJump: Array<Image?>
    var arrBoostInvincible: Array<Image?>

    init {
        container.clear()

        arrBoostTime = arrayOfNulls(MAX_LEVEL)
        arrBoostIce = arrayOfNulls(MAX_LEVEL)
        arrBoostCoins = arrayOfNulls(MAX_LEVEL)
        arrBoostSuperJump = arrayOfNulls(MAX_LEVEL)
        arrBoostInvincible = arrayOfNulls(MAX_LEVEL)

        if (Settings.LEVEL_BOOST_TIME < MAX_LEVEL) labelPriceBoostTime = Label(
            calculatePrice(Settings.LEVEL_BOOST_TIME).toString() + "",
            Assets.labelStyleSmall
        )

        if (Settings.LEVEL_BOOST_ICE < MAX_LEVEL) labelPriceIce = Label(
            calculatePrice(Settings.LEVEL_BOOST_ICE)
                .toString() + "", Assets.labelStyleSmall
        )

        if (Settings.LEVEL_BOOST_COINS < MAX_LEVEL) labelPriceCoins = Label(
            calculatePrice(Settings.LEVEL_BOOST_COINS).toString() + "",
            Assets.labelStyleSmall
        )

        if (Settings.LEVEL_BOOST_SUPER_JUMP < MAX_LEVEL) labelPriceSuperJump = Label(
            calculatePrice(Settings.LEVEL_BOOST_SUPER_JUMP).toString() + "",
            Assets.labelStyleSmall
        )

        if (Settings.LEVEL_BOOST_INVINCIBLE < MAX_LEVEL) labelPriceInvincible = Label(
            calculatePrice(Settings.LEVEL_BOOST_INVINCIBLE).toString() + "",
            Assets.labelStyleSmall
        )

        initializeButtons()

        container.add(Image(Assets.horizontalSeparator)).expandX().fill()
            .height(5f)
        container.row()

        // Upgrade BoostTime
        container
            .add(
                addCharacterTable(
                    "More power-ups",
                    labelPriceBoostTime, Assets.boosts,
                    "Power-ups will appear more often in the game",
                    arrBoostTime, buttonUpgradeBoostTime
                )
            ).expandX().fill()
        container.row()

        // Upgrade Super Jump
        container
            .add(
                addCharacterTable(
                    "Super jump", labelPriceSuperJump,
                    Assets.superJumpBoost,
                    "Super jump power up will last more time",
                    arrBoostSuperJump, buttonUpgradeSuperJump
                )
            ).expandX()
            .fill()
        container.row()

        // Upgrade Ice
        container
            .add(
                addCharacterTable(
                    "Freeze enemies", labelPriceIce,
                    Assets.IceBoost, "Enemies will last more time frozen",
                    arrBoostIce, buttonUpgradeIce
                )
            ).expandX().fill()
        container.row()

        // Upgrade invincible
        container
            .add(
                addCharacterTable(
                    "Invencible", labelPriceInvincible,
                    Assets.invincibleBoost,
                    "The invencible power-up will last more time",
                    arrBoostInvincible, buttonUpgradeInvincible
                )
            ).expandX()
            .fill()
        container.row()

        // Upgrade Monedas
        container
            .add(
                addCharacterTable(
                    "Coin rain",
                    labelPriceCoins,
                    Assets.coinRainBoost,
                    "More coins will fall down when the coin rain power-up is taken",
                    arrBoostCoins, buttonUpgradeCoins
                )
            ).expandX().fill()
        container.row()

        setArrays()
    }

    private fun addCharacterTable(
        title: String, labelPrice: Label?,
        image: AtlasRegion?, description: String, arrLevel: Array<Image?>,
        button: TextButton?
    ): Table {
        val coinImage = Image(Assets.coin)
        val playerImage = Image(image)

        if (labelPrice == null) coinImage.isVisible = false

        val tableTitleBar = Table()
        tableTitleBar.add(Label(title, Assets.labelStyleSmall)).expandX()
            .left().padLeft(5f)
        tableTitleBar.add(coinImage).right()
        tableTitleBar.add(labelPrice).right().padRight(10f)

        val tableDescription = Table()
        tableDescription.add(playerImage).left().pad(10f).size(55f, 45f)
        val labelDescription = Label(description, Assets.labelStyleSmall)
        labelDescription.wrap = true
        tableDescription.add(labelDescription).expand().fill().padLeft(5f)

        val tbContent = Table()
        tbContent.add(tableTitleBar).expandX().fill().colspan(2).padTop(8f)
        tbContent.row().colspan(2)
        tbContent.add(tableDescription).expandX().fill()
        tbContent.row()

        val auxTab = Table()
        auxTab.defaults().padLeft(5f)
        for (i in 0 until MAX_LEVEL) {
            arrLevel[i] = Image(Assets.buttonUpgradeOff)
            auxTab.add(arrLevel[i]).width(18f).height(25f)
        }

        tbContent.add(auxTab).center().expand()
        tbContent.add(button).right().padRight(10f).size(120f, 45f)

        tbContent.row().colspan(2)
        tbContent.add(Image(Assets.horizontalSeparator)).expandX().fill()
            .height(5f).padTop(15f)

        return tbContent
    }

    private fun initializeButtons() {
        buttonUpgradeBoostTime = TextButton(
            "Upgrade",
            Assets.textButtonStyleSelected
        )
        if (Settings.LEVEL_BOOST_TIME == MAX_LEVEL) buttonUpgradeBoostTime!!.isVisible = false
        addPressEffect(buttonUpgradeBoostTime!!)
        buttonUpgradeBoostTime!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (Settings.currentCoins >= calculatePrice(Settings.LEVEL_BOOST_TIME)) {
                    Settings.currentCoins -= calculatePrice(Settings.LEVEL_BOOST_TIME)
                    Settings.LEVEL_BOOST_TIME++
                    updateLabelPriceAndButton(
                        Settings.LEVEL_BOOST_TIME,
                        labelPriceBoostTime, buttonUpgradeBoostTime!!
                    )
                    setArrays()
                }
            }
        })

        buttonUpgradeSuperJump = TextButton(
            "Upgrade",
            Assets.textButtonStyleSelected
        )
        if (Settings.LEVEL_BOOST_SUPER_JUMP == MAX_LEVEL) buttonUpgradeSuperJump!!.isVisible = false
        addPressEffect(buttonUpgradeSuperJump!!)
        buttonUpgradeSuperJump!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (Settings.currentCoins >= calculatePrice(Settings.LEVEL_BOOST_SUPER_JUMP)) {
                    Settings.currentCoins -= calculatePrice(Settings.LEVEL_BOOST_SUPER_JUMP)
                    Settings.LEVEL_BOOST_SUPER_JUMP++
                    updateLabelPriceAndButton(
                        Settings.LEVEL_BOOST_SUPER_JUMP,
                        labelPriceSuperJump, buttonUpgradeSuperJump!!
                    )
                    setArrays()
                }
            }
        })

        buttonUpgradeIce = TextButton("Upgrade", Assets.textButtonStyleSelected)
        if (Settings.LEVEL_BOOST_ICE == MAX_LEVEL) buttonUpgradeIce!!.isVisible = false

        addPressEffect(buttonUpgradeIce!!)
        buttonUpgradeIce!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (Settings.currentCoins >= calculatePrice(Settings.LEVEL_BOOST_ICE)) {
                    Settings.currentCoins -= calculatePrice(Settings.LEVEL_BOOST_ICE)
                    Settings.LEVEL_BOOST_ICE++
                    updateLabelPriceAndButton(
                        Settings.LEVEL_BOOST_ICE,
                        labelPriceIce, buttonUpgradeIce!!
                    )
                    setArrays()
                }
            }
        })

        buttonUpgradeInvincible = TextButton(
            "Upgrade",
            Assets.textButtonStyleSelected
        )
        if (Settings.LEVEL_BOOST_INVINCIBLE == MAX_LEVEL) buttonUpgradeInvincible!!.isVisible = false

        addPressEffect(buttonUpgradeInvincible!!)
        buttonUpgradeInvincible!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (Settings.currentCoins >= calculatePrice(Settings.LEVEL_BOOST_INVINCIBLE)) {
                    Settings.currentCoins -= calculatePrice(Settings.LEVEL_BOOST_INVINCIBLE)
                    Settings.LEVEL_BOOST_INVINCIBLE++
                    updateLabelPriceAndButton(
                        Settings.LEVEL_BOOST_INVINCIBLE,
                        labelPriceInvincible, buttonUpgradeInvincible!!
                    )
                    setArrays()
                }
            }
        })

        buttonUpgradeCoins = TextButton(
            "Upgrade",
            Assets.textButtonStyleSelected
        )
        if (Settings.LEVEL_BOOST_COINS == MAX_LEVEL) buttonUpgradeCoins!!.isVisible = false

        addPressEffect(buttonUpgradeCoins!!)
        buttonUpgradeCoins!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (Settings.currentCoins >= calculatePrice(Settings.LEVEL_BOOST_COINS)) {
                    Settings.currentCoins -= calculatePrice(Settings.LEVEL_BOOST_COINS)
                    Settings.LEVEL_BOOST_COINS++
                    updateLabelPriceAndButton(
                        Settings.LEVEL_BOOST_COINS,
                        labelPriceCoins, buttonUpgradeCoins!!
                    )
                    setArrays()
                }
            }
        })
    }

    private fun setArrays() {
        for (i in 0 until Settings.LEVEL_BOOST_TIME) {
            arrBoostTime[i]!!.drawable = TextureRegionDrawable(
                Assets.buttonUpgradeOn
            )
        }

        for (i in 0 until Settings.LEVEL_BOOST_ICE) {
            arrBoostIce[i]!!.drawable = TextureRegionDrawable(
                Assets.buttonUpgradeOn
            )
        }

        for (i in 0 until Settings.LEVEL_BOOST_INVINCIBLE) {
            arrBoostInvincible[i]!!.drawable = TextureRegionDrawable(
                Assets.buttonUpgradeOn
            )
        }

        for (i in 0 until Settings.LEVEL_BOOST_SUPER_JUMP) {
            arrBoostSuperJump[i]!!.drawable = TextureRegionDrawable(
                Assets.buttonUpgradeOn
            )
        }

        for (i in 0 until Settings.LEVEL_BOOST_COINS) {
            arrBoostCoins[i]!!.drawable = TextureRegionDrawable(
                Assets.buttonUpgradeOn
            )
        }
    }

    private fun calculatePrice(level: Int): Int {
        return when (level) {
            0 -> level1Price

            1 -> level2Price

            2 -> level3Price

            3 -> level4Price

            4 -> level5Price
            5 -> level6Price

            else -> level6Price

        }
    }

    private fun updateLabelPriceAndButton(
        level: Int, label: Label?,
        button: TextButton
    ) {
        if (level < MAX_LEVEL) {
            label!!.setText(calculatePrice(level).toString() + "")
        } else {
            label!!.isVisible = false
            button.isVisible = false
        }
    }

    protected fun addPressEffect(actor: Actor) {
        actor.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent, x: Float, y: Float,
                pointer: Int, button: Int
            ): Boolean {
                actor.setPosition(actor.x, actor.y - 3)
                event.stop()
                return true
            }

            override fun touchUp(
                event: InputEvent, x: Float, y: Float,
                pointer: Int, button: Int
            ) {
                actor.setPosition(actor.x, actor.y + 3)
            }
        })
    }
}
