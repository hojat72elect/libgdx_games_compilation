package com.nopalsoft.slamthebird.shop

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

class NoAdsSubMenu(var game: MainSlamBird, var container: Table) {
    var priceNoAds: Int = 35000

    var buttonNoAds: TextButton
    var labelNoAds: Label? = null

    init {
        container.clear()

        if (!Settings.didBuyNoAds) labelNoAds = Label(priceNoAds.toString() + "", Assets.labelStyleSmall)

        buttonNoAds = TextButton("Buy", Assets.textButtonStyleBuy)
        if (Settings.didBuyNoAds) buttonNoAds.isVisible = false
        addPressEffect(buttonNoAds)
        buttonNoAds.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (Settings.currentCoins >= priceNoAds) {
                    Settings.currentCoins -= priceNoAds
                    Settings.didBuyNoAds = true
                    labelNoAds!!.isVisible = false
                    buttonNoAds.isVisible = false
                }
            }
        })

        // Upgrade BoostTime
        container.add(Image(Assets.horizontalSeparator)).expandX().fill().height(5f)
        container.row()
        container
            .add(
                addCharacterTable(
                    labelNoAds, Assets.buttonNoAds,
                    buttonNoAds
                )
            ).expandX().fill()
        container.row()
    }

    private fun addCharacterTable(
        labelPrice: Label?, image: TextureRegionDrawable?,
        button: TextButton
    ): Table {
        val imageCoin = Image(Assets.coin)
        val imagePlayer = Image(image)

        if (labelPrice == null) imageCoin.isVisible = false

        val tableTitleBar = Table()
        tableTitleBar.add(Label("No more ads", Assets.labelStyleSmall)).expandX().left().padLeft(5f)
        tableTitleBar.add(imageCoin).right()
        tableTitleBar.add(labelPrice).right().padRight(10f)

        val tableDescription = Table()
        tableDescription.add(imagePlayer).left().pad(10f).size(55f, 45f)
        val labelDescription = Label("Buy it and no more ads will appear in the app", Assets.labelStyleSmall)
        labelDescription.wrap = true
        tableDescription.add(labelDescription).expand().fill().padLeft(5f)

        val tableContent = Table()
        tableContent.add(tableTitleBar).expandX().fill().colspan(2).padTop(8f)
        tableContent.row().colspan(2)
        tableContent.add(tableDescription).expandX().fill()
        tableContent.row().colspan(2)

        tableContent.add(button).right().padRight(10f).size(120f, 45f)

        tableContent.row().colspan(2)
        tableContent.add(Image(Assets.horizontalSeparator)).expandX().fill().height(5f).padTop(15f)

        return tableContent
    }

    protected fun addPressEffect(actor: Actor) {
        actor.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                actor.setPosition(actor.x, actor.y - 3)
                event.stop()
                return true
            }

            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                actor.setPosition(actor.x, actor.y + 3)
            }
        })
    }
}
