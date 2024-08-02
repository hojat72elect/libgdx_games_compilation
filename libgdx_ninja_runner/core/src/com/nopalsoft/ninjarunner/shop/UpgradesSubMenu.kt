package com.nopalsoft.ninjarunner.shop

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.I18NBundle
import com.nopalsoft.ninjarunner.Assets
import com.nopalsoft.ninjarunner.MainGame
import com.nopalsoft.ninjarunner.Settings.LEVEL_COINS
import com.nopalsoft.ninjarunner.Settings.LEVEL_ENERGY
import com.nopalsoft.ninjarunner.Settings.LEVEL_LIFE
import com.nopalsoft.ninjarunner.Settings.LEVEL_MAGNET
import com.nopalsoft.ninjarunner.Settings.LEVEL_TREASURE_CHEST
import com.nopalsoft.ninjarunner.Settings.totalCoins

class UpgradesSubMenu(var contenedor: Table, game: MainGame) {
    val MAX_LEVEL: Int = 6

    val PRECIO_NIVEL_1: Int = 500
    val PRECIO_NIVEL_2: Int = 1000
    val PRECIO_NIVEL_3: Int = 1750
    val PRECIO_NIVEL_4: Int = 2500
    val PRECIO_NIVEL_5: Int = 3000

    var btUpgradeMagnet: Button? = null
    var btUpgradeLife: Button? = null
    var btUpgradeEnergy: Button? = null
    var btUpgradeCoins: Button? = null
    var btUpgradeTreasureChest: Button? = null

    var lbPrecioMagnet: Label? = null
    var lbPrecioLife: Label? = null
    var lbPrecioEnergy: Label? = null
    var lbPrecioCoins: Label? = null
    var lbPrecioTreasureChest: Label? = null

    var arrMagnet: Array<Image?>
    var arrLife: Array<Image?>
    var arrEnergy: Array<Image?>
    var arrCoins: Array<Image?>
    var arrTreasureChest: Array<Image?>

    var idiomas: I18NBundle? = game.languages

    init {
        contenedor.clear()

        arrMagnet = arrayOfNulls(MAX_LEVEL)
        arrLife = arrayOfNulls(MAX_LEVEL)
        arrEnergy = arrayOfNulls(MAX_LEVEL)
        arrCoins = arrayOfNulls(MAX_LEVEL)
        arrTreasureChest = arrayOfNulls(MAX_LEVEL)

        if (LEVEL_MAGNET < MAX_LEVEL) lbPrecioMagnet =
            Label(calcularPrecio(LEVEL_MAGNET).toString() + "", Assets.labelStyleSmall)

        if (LEVEL_LIFE < MAX_LEVEL) lbPrecioLife =
            Label(calcularPrecio(LEVEL_LIFE).toString() + "", Assets.labelStyleSmall)

        if (LEVEL_ENERGY < MAX_LEVEL) lbPrecioEnergy =
            Label(calcularPrecio(LEVEL_ENERGY).toString() + "", Assets.labelStyleSmall)

        if (LEVEL_COINS < MAX_LEVEL) lbPrecioCoins =
            Label(calcularPrecio(LEVEL_COINS).toString() + "", Assets.labelStyleSmall)

        if (LEVEL_TREASURE_CHEST < MAX_LEVEL) lbPrecioTreasureChest =
            Label(calcularPrecio(LEVEL_TREASURE_CHEST).toString() + "", Assets.labelStyleSmall)

        inicializarBotones()

        contenedor.defaults().expand().fill().padLeft(10f).padRight(20f).padBottom(10f)

        // Upgrade MAGNET
        contenedor.add(
            agregarPersonajeTabla(
                idiomas!!["upgradeMagnet"], lbPrecioMagnet, Assets.magnet, 35f, 35f, idiomas!!["magnetDescription"],
                arrMagnet, btUpgradeMagnet
            )
        ).row()
        contenedor.add(
            agregarPersonajeTabla(
                "Upgrade Life",
                lbPrecioLife,
                Assets.hearth,
                38f,
                29f,
                idiomas!!["bombDescription"],
                arrLife,
                btUpgradeLife
            )
        )
            .row()
        contenedor.add(
            agregarPersonajeTabla(
                "Upgrade Eneergy", lbPrecioEnergy, Assets.energy, 25f, 35f, idiomas!!["bombDescription"], arrEnergy,
                btUpgradeEnergy
            )
        ).row()
        contenedor.add(
            agregarPersonajeTabla(
                "Upgrade coins",
                lbPrecioCoins,
                Assets.coinAnimation!!.getKeyFrame(0f),
                35f,
                35f,
                idiomas!!["bombDescription"],
                arrCoins,
                btUpgradeCoins
            )
        ).row()
        contenedor.add(
            agregarPersonajeTabla(
                idiomas!!["upgradeTreasureChest"], lbPrecioTreasureChest, Assets.magnet, 35f, 35f,
                idiomas!!["treasureChestDescription"], arrTreasureChest, btUpgradeTreasureChest
            )
        ).row()

        setArrays()
    }

    private fun agregarPersonajeTabla(
        titulo: String,
        lblPrecio: Label?,
        imagen: Sprite?,
        imagenWidth: Float,
        imagenHeight: Float,
        descripcion: String,
        arrLevel: Array<Image?>,
        btUpgrade: Button?
    ): Table {
        val moneda = Image(Assets.coinAnimation!!.getKeyFrame(0f))
        val imgPersonaje = Image(imagen)

        if (lblPrecio == null) moneda.isVisible = false

        val tbBarraTitulo = Table()
        tbBarraTitulo.add(Label(titulo, Assets.labelStyleSmall)).expandX().left()
        tbBarraTitulo.add(moneda).right().size(20f)
        tbBarraTitulo.add(lblPrecio).right().padRight(10f)

        val tbContent = Table()
        tbContent.background = Assets.backgroundItemShop
        tbContent.pad(5f)

        tbContent.add(tbBarraTitulo).expandX().fill().colspan(2)
        tbContent.row()

        tbContent.add(imgPersonaje).size(imagenWidth, imagenHeight)
        val lblDescripcion = Label(descripcion, Assets.labelStyleSmall)
        lblDescripcion.wrap = true
        tbContent.add(lblDescripcion).expand().fill()

        val auxTab = Table()
        auxTab.background = Assets.backgroundUpgradeBar
        auxTab.pad(5f)
        auxTab.defaults().padLeft(5f)
        for (i in 0 until MAX_LEVEL) {
            arrLevel[i] = Image()
            auxTab.add(arrLevel[i]).size(15f)
        }

        tbContent.row()
        tbContent.add(auxTab)
        tbContent.add(btUpgrade).left().size(40f)

        return tbContent
    }

    private fun inicializarBotones() {
        btUpgradeMagnet = Button(Assets.styleButtonUpgrade)
        btUpgradeMagnet!!.userObject = LEVEL_MAGNET
        initButton(btUpgradeMagnet!!, lbPrecioMagnet)

        btUpgradeLife = Button(Assets.styleButtonUpgrade)
        btUpgradeLife!!.userObject = LEVEL_LIFE
        initButton(btUpgradeLife!!, lbPrecioLife)

        btUpgradeEnergy = Button(Assets.styleButtonUpgrade)
        btUpgradeEnergy!!.userObject = LEVEL_ENERGY
        initButton(btUpgradeEnergy!!, lbPrecioEnergy)

        btUpgradeCoins = Button(Assets.styleButtonUpgrade)
        btUpgradeCoins!!.userObject = LEVEL_COINS
        initButton(btUpgradeCoins!!, lbPrecioCoins)

        btUpgradeTreasureChest = Button(Assets.styleButtonUpgrade)
        btUpgradeTreasureChest!!.userObject = LEVEL_TREASURE_CHEST
        initButton(btUpgradeTreasureChest!!, lbPrecioTreasureChest)
    }

    private fun initButton(btn: Button, lblPrecio: Label?) {
        if (btn.userObject as Int == MAX_LEVEL) btn.isVisible = false
        btn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                var levelActual = btn.userObject as Int

                if (totalCoins >= calcularPrecio(levelActual)) {
                    totalCoins = totalCoins - calcularPrecio(levelActual)

                    if (btn === btUpgradeMagnet) {
                        LEVEL_MAGNET = LEVEL_MAGNET + 1
                    } else if (btn === btUpgradeLife) {
                        LEVEL_LIFE = LEVEL_LIFE + 1
                    } else if (btn === btUpgradeEnergy) {
                        LEVEL_ENERGY = LEVEL_ENERGY + 1
                    } else if (btn === btUpgradeCoins) {
                        LEVEL_COINS = LEVEL_COINS + 1
                    } else if (btn === btUpgradeTreasureChest) {
                        LEVEL_TREASURE_CHEST = LEVEL_TREASURE_CHEST + 1
                    }

                    levelActual++
                    btn.userObject = levelActual

                    updateLabelPriceAndButton(levelActual, lblPrecio, btn)
                    setArrays()
                }
            }
        })
    }

    private fun calcularPrecio(nivel: Int): Int {
        return when (nivel) {
            0 -> PRECIO_NIVEL_1

            1 -> PRECIO_NIVEL_2

            2 -> PRECIO_NIVEL_3

            3 -> PRECIO_NIVEL_4

            4 -> PRECIO_NIVEL_5

            else -> PRECIO_NIVEL_5

        }
    }

    private fun updateLabelPriceAndButton(nivel: Int, label: Label?, boton: Button) {
        if (nivel < MAX_LEVEL) {
            label!!.setText(calcularPrecio(nivel).toString() + "")
        } else {
            label!!.isVisible = false
            boton.isVisible = false
        }
    }

    private fun setArrays() {
        for (i in 0 until LEVEL_MAGNET) {
            arrMagnet[i]!!.drawable = TextureRegionDrawable(Assets.buttonShare)
        }

        for (i in 0 until LEVEL_LIFE) {
            arrLife[i]!!.drawable = TextureRegionDrawable(Assets.buttonShare)
        }

        for (i in 0 until LEVEL_ENERGY) {
            arrEnergy[i]!!.drawable = TextureRegionDrawable(Assets.buttonShare)
        }

        for (i in 0 until LEVEL_COINS) {
            arrCoins[i]!!.drawable = TextureRegionDrawable(Assets.buttonShare)
        }

        for (i in 0 until LEVEL_TREASURE_CHEST) {
            arrTreasureChest[i]!!.drawable = TextureRegionDrawable(Assets.buttonShare)
        }
    }
}
