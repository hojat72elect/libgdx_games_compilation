package com.nopalsoft.sokoban.scene2d

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.sokoban.Assets
import com.nopalsoft.sokoban.screens.Screens

open class Window(currentScreen: Screens, width: Float, height: Float, positionY: Float) : Group() {

    protected val screen = currentScreen
    private val dim = Image(Assets.blackPixel)
    var isShown = false

    init {
        setSize(width, height)
        y = positionY
        dim.setSize(Screens.SCREEN_WIDTH.toFloat(), Screens.SCREEN_HEIGHT.toFloat())
        setBackGround(Assets.backgroundWindow)
    }


    protected fun setCloseButton() {
        val buttonClose = Button(Assets.buttonClose, Assets.buttonClosePressed)
        buttonClose.setSize(60f, 60f)
        buttonClose.setPosition(290f, 250f)
        buttonClose.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                hide()
            }
        })
        addActor(buttonClose)
    }

    protected fun setTitle(text: String, fontScale: Float) {
        val tableTitle = Table()

        tableTitle.setSize(180f, 50f)
        tableTitle.setPosition(width / 2f - tableTitle.width / 2f, height - tableTitle.height)

        val labelTitle = Label(text, LabelStyle(Assets.font, Color.WHITE))
        labelTitle.setFontScale(fontScale)
        tableTitle.add(labelTitle)
        addActor(tableTitle)
    }

    private fun setBackGround(imageBackground: TextureRegionDrawable) {
        val img = Image(imageBackground)
        img.setSize(width, height)
        addActor(img)
    }

    fun show(stage: Stage) {
        setOrigin(width / 2f, height / 2f)
        x = Screens.SCREEN_WIDTH / 2f - width / 2f

        setScale(.35f)
        addAction(Actions.scaleTo(1f, 1f, ANIMATION_DURATION))

        dim.color.a = 0f
        dim.addAction(Actions.alpha(.7f, ANIMATION_DURATION))

        isShown = true
        stage.addActor(dim)
        stage.addActor(this)
    }

    fun hide() {
        isShown = false

        val run = Runnable { this.hideCompleted() }
        addAction(
            Actions
                .sequence(
                    Actions.scaleTo(.35f, .35f, ANIMATION_DURATION),
                    Actions.run(run),
                    Actions.removeActor(dim),
                    Actions.removeActor()
                )
        )
        dim.addAction(Actions.alpha(0f, ANIMATION_DURATION))
    }

    /**
     * Called when hiding is finished.
     */
    open fun hideCompleted() {
    }


    companion object {
        const val ANIMATION_DURATION = .3f
    }
}