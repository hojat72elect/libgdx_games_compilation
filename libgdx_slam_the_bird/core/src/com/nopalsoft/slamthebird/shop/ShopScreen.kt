package com.nopalsoft.slamthebird.shop

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.slamthebird.Assets
import com.nopalsoft.slamthebird.MainSlamBird
import com.nopalsoft.slamthebird.Settings
import com.nopalsoft.slamthebird.game.GameScreen
import com.nopalsoft.slamthebird.screens.Screens

class ShopScreen(game: MainSlamBird?) : Screens(game!!) {
    var btPersonajes: Button? = null
    var btPowerUps: Button? = null
    var buttonCoins: Button? = null
    var btNoAds: Button? = null
    var btAtras: Button? = null

    var scroll: ScrollPane
    var contenedor: Table

    init {
        val shop = Image(Assets.shop)
        shop.setSize(135f, 50f)
        shop.setPosition(3f, 747f)

        val separadorH = Image(Assets.horizontalSeparator)
        separadorH.setSize(SCREEN_WIDTH.toFloat(), 5f)
        separadorH.color = Color.LIGHT_GRAY
        separadorH.setPosition(0f, 740f)

        val separadorV = Image(Assets.verticalSeparator)
        separadorV.setSize(5f, 745f)
        separadorV.color = Color.LIGHT_GRAY
        separadorV.setPosition(90f, 0f)

        initButtons()

        contenedor = Table()
        scroll = ScrollPane(contenedor, Assets.scrollPaneStyle)
        scroll.setSize((SCREEN_WIDTH - 95).toFloat(), (SCREEN_HEIGHT - 62).toFloat())
        scroll.setPosition(95f, 0f)

        stage!!.addActor(shop)
        stage!!.addActor(separadorV)
        stage!!.addActor(separadorH)
        stage!!.addActor(btPersonajes)
        stage!!.addActor(btPowerUps)
        stage!!.addActor(buttonCoins)
        stage!!.addActor(btNoAds)
        stage!!.addActor(btAtras)
        stage!!.addActor(scroll)

        PlayersSubMenu(game!!, contenedor)

        buttonCoins!!.remove()
    }

    private fun initButtons() {
        btPersonajes = Button(
            TextureRegionDrawable(
                Assets.playerShopDefault
            )
        )
        btPersonajes!!.setSize(55f, 55f)
        btPersonajes!!.setPosition(17f, 660f)
        addEfectoPress(btPersonajes!!)
        btPersonajes!!.addListener(object : ClickListener() {
            override fun clicked(
                event: InputEvent, x: Float,
                y: Float
            ) {
                PlayersSubMenu(game, contenedor)
            }
        })

        btPowerUps = Button(TextureRegionDrawable(Assets.boosts))
        btPowerUps!!.setSize(55f, 55f)
        btPowerUps!!.setPosition(17f, 570f)
        addEfectoPress(btPowerUps!!)
        btPowerUps!!.addListener(object : ClickListener() {
            override fun clicked(
                event: InputEvent, x: Float,
                y: Float
            ) {
                UpgradesSubMenu(game, contenedor)
            }
        })

        buttonCoins = Button(TextureRegionDrawable(Assets.coin))
        buttonCoins!!.setSize(55f, 55f)
        buttonCoins!!.setPosition(17f, 480f)
        addEfectoPress(buttonCoins!!)
        buttonCoins!!.addListener(object : ClickListener() {
            override fun clicked(
                event: InputEvent, x: Float,
                y: Float
            ) {
                GetCoinsSubMenu(game, contenedor)
            }
        })

        btNoAds = Button(TextureRegionDrawable(Assets.buttonNoAds))
        btNoAds!!.setSize(55f, 55f)
        btNoAds!!.setPosition(17f, 390f)
        addEfectoPress(btNoAds!!)
        btNoAds!!.addListener(object : ClickListener() {
            override fun clicked(
                event: InputEvent, x: Float,
                y: Float
            ) {
                NoAdsSubMenu(game, contenedor)
            }
        })

        btAtras = Button(TextureRegionDrawable(Assets.buttonBack))
        btAtras!!.setSize(55f, 55f)
        btAtras!!.setPosition(17f, 10f)
        addEfectoPress(btAtras!!)
        btAtras!!.addListener(object : ClickListener() {
            override fun clicked(
                event: InputEvent, x: Float,
                y: Float
            ) {
                changeScreenWithFadeOut(GameScreen::class.java, game)
            }
        })
    }

    override fun draw(delta: Float) {
        oCam.update()
        batcher!!.projectionMatrix = oCam.combined

        batcher!!.begin()
        batcher!!.draw(Assets.coin, 449f, 764f, 30f, 34f)
        drawPuntuacionChicoOrigenDerecha(445f, 764f, Settings.currentCoins)
        batcher?.end()
    }

    override fun update(delta: Float) {
    }

    override fun keyDown(tecleada: Int): Boolean {
        if (tecleada == Input.Keys.BACK || tecleada == Input.Keys.ESCAPE) {
            changeScreenWithFadeOut(GameScreen::class.java, game)
            return true
        }
        return false
    }
}
