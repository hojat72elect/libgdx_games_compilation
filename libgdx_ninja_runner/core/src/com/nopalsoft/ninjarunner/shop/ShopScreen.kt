package com.nopalsoft.ninjarunner.shop

import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.ninjarunner.Assets
import com.nopalsoft.ninjarunner.MainGame
import com.nopalsoft.ninjarunner.Settings.totalCoins
import com.nopalsoft.ninjarunner.game.GameScreen
import com.nopalsoft.ninjarunner.screens.Screens

class ShopScreen(game: MainGame) : Screens(game) {
    var tbMenu: Table
    var btPersonajes: Button? = null
    var btMascota: Button? = null
    var btUpgrades: Button? = null
    var btNoAds: Button? = null
    var btMore: Button? = null

    var scroll: ScrollPane
    var contenedor: Table

    var lbCoins: Label

    init {
        val lbShop = Label("Shop", Assets.labelStyleLarge)

        val tbTitle = Table()
        tbTitle.setSize(400f, 100f)
        tbTitle.setPosition(SCREEN_WIDTH / 2f - tbTitle.width / 2f, SCREEN_HEIGHT - tbTitle.height)
        tbTitle.background = Assets.backgroundTitleShop
        tbTitle.padTop(20f).padBottom(5f)

        tbTitle.row().colspan(2)
        tbTitle.add(lbShop).expand()
        tbTitle.row()

        val imgGem = Image(Assets.coinAnimation!!.getKeyFrame(0f))
        imgGem.setSize(20f, 20f)

        lbCoins = Label("x0", Assets.labelStyleSmall)

        tbTitle.add(imgGem).size(20f).right()
        tbTitle.add(lbCoins).padLeft(5f).left()

        initButtons()

        tbMenu = Table()
        tbMenu.defaults().size(58f).padBottom(8f)

        tbMenu.row()
        tbMenu.add(btPersonajes)

        tbMenu.row()
        tbMenu.add(btMascota)

        tbMenu.row()
        tbMenu.add(btUpgrades)

        tbMenu.row()
        tbMenu.add(btNoAds)

        tbMenu.row()
        tbMenu.add(btMore)

        val tbShop = Table()
        tbShop.setSize(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT - tbTitle.height)
        tbShop.background = Assets.backgroundShop
        tbShop.pad(25f, 5f, 15f, 5f)

        // Contenedor
        contenedor = Table()

        scroll = ScrollPane(contenedor, ScrollPaneStyle(null, null, null, null, null))
        scroll.fadeScrollBars = false
        scroll.setSize(SCREEN_WIDTH - tbMenu.width, 420f)
        scroll.setPosition(tbMenu.width + 1, 0f)
        scroll.variableSizeKnobs = false

        tbShop.add(tbMenu).expandY().width(122f)
        tbShop.add(scroll).expand().fill()

        stage?.addActor(tbTitle)
        stage?.addActor(tbShop)

        PersonajesSubMenu(contenedor, game)
        btPersonajes!!.isChecked = true
    }

    fun initButtons() {
        btPersonajes = Button(Assets.buttonShop, Assets.buttonShopPressed, Assets.buttonShopPressed)
        btMascota = Button(Assets.buttonLeaderboard, Assets.buttonLeaderboardPressed, Assets.buttonLeaderboardPressed)
        btUpgrades = Button(Assets.buttonAchievement, Assets.buttonAchievementPressed, Assets.buttonLeaderboardPressed)
        btNoAds = Button(Assets.buttonSettings, Assets.buttonSettingsPressed, Assets.buttonLeaderboardPressed)
        btMore = Button(Assets.buttonRate, Assets.buttonSettingsPressed)

        btPersonajes!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                PersonajesSubMenu(contenedor, game)
            }
        })

        btMascota!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                PetsSubMenu(contenedor, game)
            }
        })

        btUpgrades!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                UpgradesSubMenu(contenedor, game)
            }
        })

        btNoAds!!.addListener(object : ClickListener() {
        })

        btMore!!.addListener(object : ClickListener() {
        })

        val radioGroup = ButtonGroup<Button>()
        radioGroup.add(btPersonajes, btMascota, btUpgrades, btNoAds)
    }

    override fun draw(delta: Float) {
        Assets.backgroundNubes!!.render(0f)
    }

    override fun update(delta: Float) {
        lbCoins.setText("x" + totalCoins)
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            changeScreenWithFadeOut(GameScreen::class.java, game)
            return true
        }
        return super.keyUp(keycode)
    }
}
