package com.nopalsoft.slamthebird.scene2d

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.nopalsoft.slamthebird.Assets
import com.nopalsoft.slamthebird.MainSlamBird
import com.nopalsoft.slamthebird.screens.Screens

open class Window(@JvmField var screen: Screens) : Group() {
    @JvmField
    var game: MainSlamBird = screen.game

    private var isVisible = false

    fun setBackGround() {
        val img = Image(Assets.buttonScores)
        img.setSize(width, height)
        addActor(img)
    }

    fun show(stage: Stage) {
        setOrigin(width / 2f, height / 2f)
        x = Screens.SCREEN_WIDTH / 2f - width / 2f

        setScale(.5f)
        addAction(
            Actions.sequence(
                Actions.scaleTo(1f, 1f, DURATION_ANIMATION),
                Actions.run { this.endResize() })
        )

        isVisible = true
        stage.addActor(this)
    }

    override fun isVisible(): Boolean {
        return isVisible
    }

    fun hide() {
        isVisible = false
        remove()
    }

    protected fun endResize() {
    }

    companion object {
        const val DURATION_ANIMATION: Float = .3f
    }
}
