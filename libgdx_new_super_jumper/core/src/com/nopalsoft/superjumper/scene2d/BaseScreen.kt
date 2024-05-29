package com.nopalsoft.superjumper.scene2d

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.nopalsoft.superjumper.MainSuperJumper
import com.nopalsoft.superjumper.screens.Screens

open class BaseScreen(
    @JvmField val screen: Screens,
    width: Float,
    height: Float,
    positionY: Float
) :
    Group() {

    @JvmField
    protected var game: MainSuperJumper = screen.game
    private var isVisible = false


    init {
        setSize(width, height)
        y = positionY
    }


   open fun show(stage: Stage) {
        setOrigin(width / 2f, height / 2f)
        x = Screens.SCREEN_WIDTH / 2f - width / 2f

        setScale(.5f)
        addAction(
            Actions.sequence(
                Actions.scaleTo(1f, 1f, ANIMATION_DURATION),
                Actions.run { this.endResize() })
        )

        isVisible = true
        stage.addActor(this)
    }

   open fun hide() {
        isVisible = false
        remove()
    }

    private fun endResize() {
    }

    companion object {
        const val ANIMATION_DURATION = .3f

    }
}