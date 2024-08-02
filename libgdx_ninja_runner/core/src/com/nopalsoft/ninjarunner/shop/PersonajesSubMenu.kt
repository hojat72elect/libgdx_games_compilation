package com.nopalsoft.ninjarunner.shop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.I18NBundle
import com.nopalsoft.ninjarunner.AnimationSprite
import com.nopalsoft.ninjarunner.Assets
import com.nopalsoft.ninjarunner.MainGame
import com.nopalsoft.ninjarunner.Settings.save
import com.nopalsoft.ninjarunner.Settings.selectedSkin
import com.nopalsoft.ninjarunner.Settings.totalCoins
import com.nopalsoft.ninjarunner.objects.Player
import com.nopalsoft.ninjarunner.scene2d.AnimatedSpriteActor

class PersonajesSubMenu(var contenedor: Table, game: MainGame) {
    val PRECIO_NINJA: Int = 1000
    var didBuyNinja: Boolean = false
    var lbPrecioNinja: Label? = null
    var btSelectShanti: TextButton? = null
    var btBuyNinja: TextButton? = null
    var arrBotones: Array<TextButton>? = null
    var idiomas: I18NBundle? = game.languages
    var textBuy: String
    var textSelect: String

    init {
        contenedor.clear()
        // TODO QUITAR ESTO PARA BORRAR PREF
        pref.clear()
        pref.flush()

        loadPurchases()

        textBuy = idiomas!!["buy"]
        textSelect = idiomas!!["select"]

        if (!didBuyNinja) lbPrecioNinja = Label(PRECIO_NINJA.toString() + "", Assets.labelStyleSmall)

        inicializarBotones()

        contenedor.defaults().expand().fill().padLeft(10f).padRight(20f).padBottom(10f)

        contenedor.add(
            agregarPersonaje(
                "Runner girl",
                null,
                Assets.playerRunAnimation,
                idiomas!!["bombDescription"],
                btSelectShanti
            )
        ).row()
        contenedor.add(
            agregarPersonaje(
                "Runner Ninja",
                lbPrecioNinja,
                Assets.ninjaRunAnimation,
                idiomas!!["bombDescription"],
                btBuyNinja
            )
        ).row()
    }

    fun agregarPersonaje(
        titulo: String?,
        lblPrecio: Label?,
        imagen: AnimationSprite?,
        descripcion: String?,
        boton: TextButton?
    ): Table {
        val moneda = Image(Assets.coinAnimation!!.getKeyFrame(0f))
        val imgPersonaje = AnimatedSpriteActor(imagen!!)

        if (lblPrecio == null) moneda.isVisible = false

        val tbBarraTitulo = Table()
        tbBarraTitulo.add(Label(titulo, Assets.labelStyleSmall)).expandX().left()
        tbBarraTitulo.add(moneda).right().size(20f)
        tbBarraTitulo.add(lblPrecio).right().padRight(10f)

        val tbContent = Table()
        tbContent.background = Assets.backgroundItemShop
        tbContent.pad(5f) //El ninepatch le mete un padding por default

        tbContent.defaults().padLeft(20f).padRight(20f)
        tbContent.add(tbBarraTitulo).expandX().fill().colspan(2)
        tbContent.row()

        tbContent.add(imgPersonaje).size(120f, 99f)
        val lblDescripcion = Label(descripcion, Assets.labelStyleSmall)
        lblDescripcion.wrap = true
        tbContent.add(lblDescripcion).expand().fill()

        tbContent.row().colspan(2)
        tbContent.add(boton).expandX().right().size(120f, 45f)
        tbContent.row().colspan(2)

        tbContent.debugAll()
        return tbContent
    }

    private fun inicializarBotones() {
        arrBotones = Array()

        // DEFAULT
        btSelectShanti = TextButton(textSelect, Assets.styleTextButtonPurchased)
        if (selectedSkin == Player.TYPE_GIRL) btSelectShanti!!.isVisible = false

        btSelectShanti!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                selectedSkin = Player.TYPE_GIRL
                setSelected(btSelectShanti!!)
            }
        })

        // SKIN_NINJA
        btBuyNinja = if (didBuyNinja) TextButton(textSelect, Assets.styleTextButtonPurchased)
        else TextButton(textBuy, Assets.styleTextButtonBuy)

        if (selectedSkin == Player.TYPE_NINJA) btBuyNinja!!.isVisible = false

        btBuyNinja!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuyNinja) {
                    selectedSkin = Player.TYPE_NINJA
                    setSelected(btBuyNinja!!)
                } else if (totalCoins >= PRECIO_NINJA) {
                    totalCoins = totalCoins - PRECIO_NINJA
                    setButtonStylePurchased(btBuyNinja!!)
                    lbPrecioNinja!!.remove()
                    didBuyNinja = true
                }
                savePurchases()
            }
        })

        arrBotones!!.add(btSelectShanti)
        arrBotones!!.add(btBuyNinja)
    }

    private fun loadPurchases() {
        didBuyNinja = pref.getBoolean("didBuyNinja", false)
    }

    private fun savePurchases() {
        pref.putBoolean("didBuyNinja", didBuyNinja)
        pref.flush()
        save()
    }

    private fun setButtonStylePurchased(boton: TextButton) {
        boton.style = Assets.styleTextButtonPurchased
        boton.setText(textSelect)
    }

    private fun setSelected(boton: TextButton) {
        // Pongo todos visibles y al final el boton seleccionado en invisible
        for (arrBotone in arrBotones!!) {
            arrBotone.isVisible = true
        }
        boton.isVisible = false
    }

    companion object {
        private val pref: Preferences = Gdx.app.getPreferences("com.tiar.shantirunner.shop")
    }
}
