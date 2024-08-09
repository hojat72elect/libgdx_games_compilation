package com.nopalsoft.slamthebird.shop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Array
import com.nopalsoft.slamthebird.Assets
import com.nopalsoft.slamthebird.Assets.loadPlayer
import com.nopalsoft.slamthebird.MainSlamBird
import com.nopalsoft.slamthebird.Settings

class PlayersSubMenu(var game: MainSlamBird, var container: Table) {
    val PRICE_RED_ANDROID: Int = 1500
    val PRICE_BLUE_ANDROID: Int = 2000
    var didBuyRed: Boolean
    var didBuyBlue: Boolean
    var textButtonBuyDefault: TextButton? = null
    var textButtonBuyRedAndroid: TextButton? = null
    var textButtonBuyBlueAndroid: TextButton? = null
    var arrayTextButtons: Array<TextButton>? = null
    var labelPriceRed: Label
    var labelPriceBlue: Label

    init {
        container.clear()

        didBuyRed = pref.getBoolean("didBuyRojo", false)
        didBuyBlue = pref.getBoolean("didBuyAzul", false)

        labelPriceRed = Label(
            PRICE_RED_ANDROID.toString() + "",
            Assets.labelStyleSmall
        )
        labelPriceBlue = Label(
            PRICE_BLUE_ANDROID.toString() + "",
            Assets.labelStyleSmall
        )

        initializeButtons()

        container.add(Image(Assets.horizontalSeparator)).expandX().fill()
            .height(5f)
        container.row()

        // Use Default
        container
            .add(
                addPlayerTable(
                    "Green robot", null,
                    Assets.playerShopDefault,
                    "Just a simple robot for slaming birds", textButtonBuyDefault
                )
            )
            .expandX().fill()
        container.row()

        // Use red Player
        container
            .add(
                addPlayerTable(
                    "Red robot",
                    labelPriceRed,
                    Assets.playerShopRed,
                    "Do you like red color. Play with this amazing red robot and slam those birds",
                    textButtonBuyRedAndroid
                )
            ).expandX().fill()
        container.row()

        // SKIN_ANDROID_BLUE
        container
            .add(
                addPlayerTable(
                    "Blue robot",
                    labelPriceBlue,
                    Assets.playerShopBlue,
                    "Do you like blue color. Play with this amazing blue robot and slam those birds",
                    textButtonBuyBlueAndroid
                )
            ).expandX().fill()
        container.row()

        if (didBuyBlue) labelPriceBlue.remove()
        if (didBuyRed) labelPriceRed.remove()
    }

    private fun addPlayerTable(
        title: String, labelPrice: Label?,
        image: AtlasRegion?, description: String, button: TextButton?
    ): Table {
        val coin = Image(Assets.coin)
        val imagePlayer = Image(image)

        if (labelPrice == null) coin.isVisible = false

        val tableTitleBar = Table()
        tableTitleBar.add(Label(title, Assets.labelStyleSmall)).expandX()
            .left()
        tableTitleBar.add(coin).right()
        tableTitleBar.add(labelPrice).right().padRight(10f)

        val tbContent = Table()

        tbContent.add(tableTitleBar).expandX().fill().colspan(2).padTop(8f)
        tbContent.row()
        tbContent.add(imagePlayer).left().pad(10f).size(60f)

        val lblDescripcion = Label(description, Assets.labelStyleSmall)
        lblDescripcion.wrap = true
        tbContent.add(lblDescripcion).expand().fill().padLeft(5f)

        tbContent.row().colspan(2)
        tbContent.add(button).expandX().right().padRight(10f).size(120f, 45f)
        tbContent.row().colspan(2)
        tbContent.add(Image(Assets.horizontalSeparator)).expandX().fill()
            .height(5f).padTop(15f)

        return tbContent
    }

    private fun initializeButtons() {
        arrayTextButtons = Array()

        // SKIN_DEFAULT
        textButtonBuyDefault = TextButton("Select", Assets.textButtonStylePurchased)
        if (Settings.selectedSkin == SKIN_DEFAULT) textButtonBuyDefault!!.isVisible = false

        addEfectoPress(textButtonBuyDefault!!)
        textButtonBuyDefault!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                Settings.selectedSkin = SKIN_DEFAULT
                setSelected(textButtonBuyDefault!!)
                loadPlayer()
            }
        })

        // SKIN_ANDROID_RED
        textButtonBuyRedAndroid = if (didBuyRed) TextButton(
            "Select",
            Assets.textButtonStylePurchased
        )
        else TextButton("Buy", Assets.textButtonStyleBuy)

        if (Settings.selectedSkin == SKIN_ANDROID_RED) textButtonBuyRedAndroid!!.isVisible = false

        addEfectoPress(textButtonBuyRedAndroid!!)
        textButtonBuyRedAndroid!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuyRed) {
                    Settings.selectedSkin = SKIN_ANDROID_RED
                    setSelected(textButtonBuyRedAndroid!!)
                    loadPlayer()
                } else if (Settings.currentCoins >= PRICE_RED_ANDROID) {
                    Settings.currentCoins -= PRICE_RED_ANDROID
                    setButtonStylePurchased(textButtonBuyRedAndroid!!)
                    didBuyRed = true
                    labelPriceRed.remove()
                    save()
                }
            }
        })

        // SKIN_ANDROID_BLUE
        textButtonBuyBlueAndroid = if (didBuyBlue) TextButton(
            "Select",
            Assets.textButtonStylePurchased
        )
        else TextButton("Buy", Assets.textButtonStyleBuy)

        if (Settings.selectedSkin == SKIN_ANDROID_BLUE) textButtonBuyBlueAndroid!!.isVisible = false

        addEfectoPress(textButtonBuyBlueAndroid!!)
        textButtonBuyBlueAndroid!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuyBlue) {
                    Settings.selectedSkin = SKIN_ANDROID_BLUE
                    setSelected(textButtonBuyBlueAndroid!!)
                    loadPlayer()
                } else if (Settings.currentCoins >= PRICE_BLUE_ANDROID) {
                    Settings.currentCoins -= PRICE_BLUE_ANDROID
                    setButtonStylePurchased(textButtonBuyBlueAndroid!!)
                    didBuyBlue = true
                    labelPriceBlue.remove()
                    save()
                }
            }
        })

        arrayTextButtons!!.add(textButtonBuyDefault)
        arrayTextButtons!!.add(textButtonBuyRedAndroid)
        arrayTextButtons!!.add(textButtonBuyBlueAndroid)
    }

    private fun save() {
        pref.putBoolean("didBuyAzul", didBuyBlue)
        pref.putBoolean("didBuyRojo", didBuyRed)
        pref.flush()
    }

    private fun setButtonStylePurchased(boton: TextButton) {
        boton.style = Assets.textButtonStylePurchased
        boton.setText("Select")
    }

    private fun setSelected(boton: TextButton) {
        // I make them all visible and at the end the selected button is invisible.
        for (button in arrayTextButtons!!) {
            button.isVisible = true
        }
        boton.isVisible = false
    }

    protected fun addEfectoPress(actor: Actor) {
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

    companion object {
        const val SKIN_DEFAULT: Int = 0
        const val SKIN_ANDROID_RED: Int = 1
        const val SKIN_ANDROID_BLUE: Int = 2
        private val pref: Preferences = Gdx.app
            .getPreferences("com.nopalsoft.slamthebird.personajes")
    }
}
