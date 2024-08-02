package com.nopalsoft.ninjarunner.shop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.I18NBundle
import com.nopalsoft.ninjarunner.AnimationSprite
import com.nopalsoft.ninjarunner.Assets
import com.nopalsoft.ninjarunner.MainGame
import com.nopalsoft.ninjarunner.Settings.LEVEL_PET_BIRD
import com.nopalsoft.ninjarunner.Settings.LEVEL_PET_BOMB
import com.nopalsoft.ninjarunner.Settings.save
import com.nopalsoft.ninjarunner.Settings.selectedPet
import com.nopalsoft.ninjarunner.Settings.totalCoins
import com.nopalsoft.ninjarunner.objects.Pet
import com.nopalsoft.ninjarunner.scene2d.AnimatedSpriteActor

class PetsSubMenu(var myContainer: Table, game: MainGame) {
    val MAX_LEVEL: Int = 6
    val BOMB_PRICE: Int = 5000
    val PRICE_LEVEL_1: Int = 350
    val PRICE_LEVEL_2: Int = 1000
    val PRICE_LEVEL_3: Int = 3000
    val PRICE_LEVEL_4: Int = 4500
    val PRICE_LEVEL_5: Int = 5000
    val PRICE_LEVEL_6: Int = 7500
    var didBuyBomb: Boolean = false
    var labelPriceBird: Label? = null
    var labelPriceBomb: Label? = null
    var buttonBuyOrSelectBird: TextButton? = null
    var buttonBuyBomb: TextButton? = null
    var arrayButtons: com.badlogic.gdx.utils.Array<TextButton?>? = null
    var buttonUpgradeBird: Button? = null
    var buttonUpgradeBomb: Button? = null
    var arrayBird: Array<Image?>
    var arrayBomb: Array<Image?>
    var languages: I18NBundle? = game.languages
    var textBuy: String
    var textSelect: String

    init {
        myContainer.clear()

        loadPurchases()

        textBuy = languages!!["buy"]
        textSelect = languages!!["select"]

        arrayBird = arrayOfNulls(MAX_LEVEL)
        arrayBomb = arrayOfNulls(MAX_LEVEL)

        if (LEVEL_PET_BIRD < MAX_LEVEL) {
            labelPriceBird = Label(calculatePrice(LEVEL_PET_BIRD).toString() + "", Assets.labelStyleSmall)
        }

        if (!didBuyBomb) {
            labelPriceBomb = Label(BOMB_PRICE.toString() + "", Assets.labelStyleSmall)
        } else if (LEVEL_PET_BOMB < MAX_LEVEL) {
            labelPriceBomb = Label(calculatePrice(LEVEL_PET_BOMB).toString() + "", Assets.labelStyleSmall)
        }

        initializeButtons()

        myContainer.defaults().expand().fill().padLeft(10f).padRight(20f).padBottom(10f)

        myContainer.add(
            addPet(
                "Chicken",
                labelPriceBird,
                Assets.Pet1FlyAnimation,
                60f,
                54f,
                languages!!["pinkChikenDescription"],
                buttonBuyOrSelectBird,
                arrayBird,
                buttonUpgradeBird
            )
        ).row()
        myContainer.add(
            addPet(
                "Bomb",
                labelPriceBomb,
                Assets.PetBombFlyAnimation,
                53f,
                64f,
                languages!!["bombDescription"],
                buttonBuyBomb,
                arrayBomb,
                buttonUpgradeBomb
            )
        )
            .row()

        setArrays()
    }

    fun addPet(
        title: String?,
        labelPrice: Label?,
        image: AnimationSprite?,
        imageWidth: Float,
        imageHeight: Float,
        description: String?,
        buttonBuy: TextButton?,
        arrayLevel: Array<Image?>,
        buttonUpgrade: Button?
    ): Table {
        val coin = Image(Assets.coinAnimation!!.getKeyFrame(0f))
        val playerImage = AnimatedSpriteActor(image!!)

        if (labelPrice == null) coin.isVisible = false

        val tableTitleBar = Table()
        tableTitleBar.add(Label(title, Assets.labelStyleSmall)).expandX().left()
        tableTitleBar.add(coin).right().size(20f)
        tableTitleBar.add(labelPrice).right().padRight(10f)

        val tableContent = Table()
        tableContent.background = Assets.backgroundItemShop
        tableContent.pad(5f)

        tableContent.add(tableTitleBar).expandX().fill().colspan(2)
        tableContent.row()

        tableContent.add(playerImage).size(imageWidth, imageHeight)
        val labelDescription = Label(description, Assets.labelStyleSmall)
        labelDescription.wrap = true
        tableContent.add(labelDescription).expand().fill()

        val auxTab = Table()
        auxTab.background = Assets.backgroundUpgradeBar
        auxTab.pad(5f)
        auxTab.defaults().padLeft(5f)
        for (i in 0 until MAX_LEVEL) {
            arrayLevel[i] = Image()
            auxTab.add(arrayLevel[i]).size(15f)
        }

        tableContent.row()
        tableContent.add(auxTab)
        tableContent.add(buttonUpgrade).left().size(40f)

        tableContent.row().colspan(2)
        tableContent.add(buttonBuy).expandX().right().size(120f, 45f)
        tableContent.row().colspan(2)

        return tableContent
    }

    private fun initializeButtons() {
        arrayButtons = com.badlogic.gdx.utils.Array()

        run {
            run {
                buttonBuyOrSelectBird = TextButton(textSelect, Assets.styleTextButtonPurchased)
                if (selectedPet == Pet.PetType.PINK_BIRD) buttonBuyOrSelectBird!!.isVisible = false
                buttonBuyOrSelectBird!!.addListener(object : ClickListener() {
                    override fun clicked(event: InputEvent, x: Float, y: Float) {
                        selectedPet = Pet.PetType.PINK_BIRD
                        setSelected(buttonBuyOrSelectBird!!)
                    }
                })
            }
            run {
                buttonUpgradeBird = Button(Assets.styleButtonUpgrade)
                if (LEVEL_PET_BIRD == MAX_LEVEL) buttonUpgradeBird!!.isVisible = false
                buttonUpgradeBird!!.addListener(object : ClickListener() {
                    override fun clicked(event: InputEvent, x: Float, y: Float) {
                        if (totalCoins >= calculatePrice(LEVEL_PET_BIRD)) {
                            totalCoins = totalCoins - calculatePrice(LEVEL_PET_BIRD)
                            LEVEL_PET_BIRD = LEVEL_PET_BIRD + 1

                            updateLabelPriceAndButton(LEVEL_PET_BIRD, labelPriceBird, buttonUpgradeBird)
                            setArrays()
                        }
                    }
                })
            }
        }

        run {
            run {
                buttonBuyBomb = if (didBuyBomb) TextButton(textSelect, Assets.styleTextButtonPurchased)
                else TextButton(textBuy, Assets.styleTextButtonBuy)
                if (selectedPet == Pet.PetType.BOMB) buttonBuyBomb!!.isVisible = false
                buttonBuyBomb!!.addListener(object : ClickListener() {
                    override fun clicked(event: InputEvent, x: Float, y: Float) {
                        if (didBuyBomb) {
                            selectedPet = Pet.PetType.BOMB
                            setSelected(buttonBuyBomb!!)
                        } else if (totalCoins >= BOMB_PRICE) {
                            totalCoins = totalCoins - BOMB_PRICE
                            setButtonStylePurchased(buttonBuyBomb!!)
                            didBuyBomb = true
                            buttonUpgradeBomb!!.isVisible = true
                            updateLabelPriceAndButton(LEVEL_PET_BOMB, labelPriceBomb, buttonUpgradeBomb)
                        }
                        savePurchases()
                    }
                })
            }
            run {
                // UPGRADE
                buttonUpgradeBomb = Button(Assets.styleButtonUpgrade)
                if (LEVEL_PET_BOMB == MAX_LEVEL || !didBuyBomb) buttonUpgradeBomb!!.isVisible = false
                buttonUpgradeBomb!!.addListener(object : ClickListener() {
                    override fun clicked(event: InputEvent, x: Float, y: Float) {
                        if (totalCoins >= calculatePrice(LEVEL_PET_BOMB)) {
                            totalCoins = totalCoins - calculatePrice(LEVEL_PET_BOMB)
                            LEVEL_PET_BOMB = LEVEL_PET_BOMB + 1
                            updateLabelPriceAndButton(LEVEL_PET_BOMB, labelPriceBomb, buttonUpgradeBomb)
                            setArrays()
                        }
                    }
                })
            }
        }

        arrayButtons!!.add(buttonBuyOrSelectBird)
        arrayButtons!!.add(buttonBuyBomb)
    }

    private fun loadPurchases() {
        didBuyBomb = pref.getBoolean("didBuyBomb", false)
    }

    private fun savePurchases() {
        pref.putBoolean("didBuyBomb", didBuyBomb)
        pref.flush()
        save()
    }

    private fun setButtonStylePurchased(boton: TextButton) {
        boton.style = Assets.styleTextButtonPurchased
        boton.setText(textSelect)
    }

    private fun setSelected(button: TextButton) {
        // I make them all visible and at the end the selected
        // button is invisible.
        for (textButton in arrayButtons!!) {
            textButton!!.isVisible = true
        }
        button.isVisible = false
    }

    private fun calculatePrice(level: Int): Int {
        return when (level) {
            0 -> PRICE_LEVEL_1

            1 -> PRICE_LEVEL_2

            2 -> PRICE_LEVEL_3

            3 -> PRICE_LEVEL_4

            4 -> PRICE_LEVEL_5
            5 -> PRICE_LEVEL_6

            else -> PRICE_LEVEL_6

        }
    }

    private fun updateLabelPriceAndButton(level: Int, label: Label?, button: Button?) {
        if (level < MAX_LEVEL) {
            label!!.setText(calculatePrice(level).toString() + "")
        } else {
            label!!.isVisible = false
            button!!.isVisible = false
        }
    }

    private fun setArrays() {
        for (i in 0 until LEVEL_PET_BIRD) {
            arrayBird[i]!!.drawable = TextureRegionDrawable(Assets.buttonShare)
        }

        for (i in 0 until LEVEL_PET_BOMB) {
            arrayBomb[i]!!.drawable = TextureRegionDrawable(Assets.buttonShare)
        }
    }

    companion object {
        private val pref: Preferences = Gdx.app.getPreferences("com.tiar.shantirunner.shop")
    }
}
