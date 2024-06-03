package com.nopalsoft.superjumper.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.superjumper.Assets
import com.nopalsoft.superjumper.MainSuperJumper
import com.nopalsoft.superjumper.Settings
import com.nopalsoft.superjumper.game.GameScreen

class MainMenuScreen(game: MainSuperJumper) : BasicScreen(game) {

    private val title = Image(Assets.settings)
    private val buttonShop = TextButton("Shop", Assets.textButtonStyleBig)
    private val buttonPlay = TextButton("Play", Assets.textButtonStyleBig)
    private val buttonLeaderboard = TextButton("Leaderboard", Assets.textButtonStyleBig)
    private val buttonRate = TextButton("Rate", Assets.textButtonStyleBig)
    private val labelBestScore = Label("Best score " + Settings.bestScore, Assets.labelStyleSmall)


    init {
        title.setPosition(SCREEN_WIDTH / 2f - title.width / 2f, 800f)
        title.addAction(
            Actions.sequence(
                Actions.moveTo(title.x, 600f, 1f, Interpolation.bounceOut),
                Actions.run {
                    stage.addActor(labelBestScore)
                })
        )

        labelBestScore.setPosition(SCREEN_WIDTH / 2f - labelBestScore.width / 2f, 570f)
        labelBestScore.color.a = 0f
        labelBestScore.addAction(Actions.alpha(1f, .25f))

        buttonPlay.setPosition(SCREEN_WIDTH / 2f - buttonPlay.width / 2f, 440f)
        buttonPlay.pad(10f)
        buttonPlay.pack()
        addPressEffect(buttonPlay)
        buttonPlay.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                changeScreenWithFadeOut(GameScreen::class.java, game)
            }
        })

        buttonShop.setPosition(SCREEN_WIDTH / 2f - buttonShop.width / 2f, 340f)
        buttonShop.pad(10f)
        buttonShop.pack()
        addPressEffect(buttonShop)

        buttonRate.setPosition(SCREEN_WIDTH / 2f - buttonRate.width / 2f, 340f)
        buttonRate.pad(10f)
        buttonRate.pack()
        addPressEffect(buttonRate)

        buttonLeaderboard.pad(10f)
        buttonLeaderboard.pack()
        buttonLeaderboard.setPosition(SCREEN_WIDTH / 2f - buttonLeaderboard.width / 2f, 240f)

        addPressEffect(buttonLeaderboard)

        stage.addActor(title)
        stage.addActor(buttonPlay)
        stage.addActor(buttonRate)
        stage.addActor(buttonLeaderboard)
    }

    override fun update(delta: Float) {
        // Nothing happening in here.
    }

    override fun draw(delta: Float) {
        batcher.begin()
        batcher.draw(Assets.background, 0f, 0f, SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        batcher.draw(Assets.platformBeigeBroken, 100f, 100f, 125f, 45f)
        batcher.draw(Assets.platformBlue, 350f, 280f, 125f, 45f)
        batcher.draw(Assets.platformMulticolor, 25f, 430f, 125f, 45f)
        batcher.draw(Assets.characterJump, 25f, 270f, 75f, 80f)
        batcher.draw(Assets.cloudHappy, 350f, 500f, 95f, 60f)
        batcher.end()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            Gdx.app.exit()
        }
        return super.keyDown(keycode)
    }
}