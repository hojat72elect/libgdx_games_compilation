package com.nopalsoft.slamthebird.shop

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.slamthebird.Assets
import com.nopalsoft.slamthebird.MainSlamBird
import com.nopalsoft.slamthebird.Settings

class GetCoinsSubMenu(var game: MainSlamBird, var contenedor: Table) {
    var monedasLikeFacebook: Int = 1500

    // Comun
    var btLikeFacebook: TextButton

    // iOS
    var btBuy5milCoins: TextButton
    var btBuy15MilCoins: TextButton
    var btBuy30MilCoins: TextButton
    var btBuy50MilCoins: TextButton

    init {
        contenedor.clear()

        btLikeFacebook = TextButton("Like us", Assets.textButtonStyleBuy)
        if (Settings.didLikeFacebook) btLikeFacebook = TextButton(
            "Visit Us",
            Assets.textButtonStyleSelected
        )
        addEfectoPress(btLikeFacebook)
        btLikeFacebook.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (!Settings.didLikeFacebook) {
                    Settings.didLikeFacebook = true
                    game.stage!!.addAction(
                        Actions.sequence(
                            Actions.delay(1f),
                            Actions.run {
                                Settings.currentCoins += monedasLikeFacebook
                                btLikeFacebook.setText("Visit us")
                                btLikeFacebook.style = Assets.textButtonStyleSelected
                            })
                    )
                }
            }
        })

        btBuy5milCoins = TextButton("Buy", Assets.textButtonStyleBuy)
        addEfectoPress(btBuy5milCoins)
        btBuy5milCoins.addListener(object : ClickListener() {
        })

        btBuy15MilCoins = TextButton("Buy", Assets.textButtonStyleBuy)
        addEfectoPress(btBuy15MilCoins)
        btBuy15MilCoins.addListener(object : ClickListener() {
        })

        btBuy30MilCoins = TextButton("Buy", Assets.textButtonStyleBuy)
        addEfectoPress(btBuy30MilCoins)
        btBuy30MilCoins.addListener(object : ClickListener() {
        })

        btBuy50MilCoins = TextButton("Buy", Assets.textButtonStyleBuy)
        addEfectoPress(btBuy50MilCoins)
        btBuy50MilCoins.addListener(object : ClickListener() {
        })

        // Facebook Like
        contenedor.add(Image(Assets.horizontalSeparator)).expandX().fill()
            .height(5f)
        contenedor.row()
        contenedor
            .add(
                addCharacterTable(
                    monedasLikeFacebook,
                    Assets.buttonFacebook, "Like us on facebook and get "
                            + monedasLikeFacebook + " coins",
                    btLikeFacebook
                )
            ).expandX().fill()
        contenedor.row()

        val moneda = TextureRegionDrawable(Assets.coin)


        // Venta de monedas


        // Comprar 5mil
        contenedor
            .add(
                addCharacterTable(
                    5000,
                    moneda,
                    "Coin simple pack. A quick way to buy simple upgrades",
                    btBuy5milCoins
                )
            ).expandX().fill()
        contenedor.row()

        // Buy 15 thousand
        contenedor
            .add(
                addCharacterTable(
                    15000,
                    moneda,
                    "Coin super pack. Get some cash for upgrades and characters",
                    btBuy15MilCoins
                )
            ).expandX().fill()
        contenedor.row()

        contenedor
            .add(
                addCharacterTable(
                    30000,
                    moneda,
                    "Coin mega pack. You can buy a lot of characters and upgrades",
                    btBuy30MilCoins
                )
            ).expandX().fill()
        contenedor.row()

        contenedor
            .add(
                addCharacterTable(
                    50000,
                    moneda,
                    "Coin super mega pack. Get this pack and you will be slamming in cash",
                    btBuy50MilCoins
                )
            ).expandX().fill()
        contenedor.row()
    }

    private fun addCharacterTable(
        numCoinsToGet: Int,
        image: TextureRegionDrawable?, description: String, button: TextButton
    ): Table {
        val moneda = Image(Assets.coin)
        val imgPersonaje = Image(image)

        val tbBarraTitulo = Table()
        tbBarraTitulo
            .add(Label("Get $numCoinsToGet", Assets.labelStyleSmall))
            .left().padLeft(5f)
        tbBarraTitulo.add(moneda).left().expandX().padLeft(5f)

        val tbDescrip = Table()
        tbDescrip.add(imgPersonaje).left().pad(10f).size(55f, 45f)
        val lblDescripcion = Label(description, Assets.labelStyleSmall)
        lblDescripcion.wrap = true
        tbDescrip.add(lblDescripcion).expand().fill().padLeft(5f)

        val tbContent = Table()
        tbContent.add(tbBarraTitulo).expandX().fill().colspan(2).padTop(8f)
        tbContent.row().colspan(2)
        tbContent.add(tbDescrip).expandX().fill()
        tbContent.row().colspan(2)

        tbContent.add(button).right().padRight(10f).size(120f, 45f)

        tbContent.row().colspan(2)
        tbContent.add(Image(Assets.horizontalSeparator)).expandX().fill()
            .height(5f).padTop(15f)

        return tbContent
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
}
