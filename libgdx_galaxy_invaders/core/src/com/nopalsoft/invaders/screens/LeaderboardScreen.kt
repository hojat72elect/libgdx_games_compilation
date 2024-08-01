package com.nopalsoft.invaders.screens

import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.invaders.Assets.buttonSignInDown
import com.nopalsoft.invaders.Assets.buttonSignInUp
import com.nopalsoft.invaders.Assets.clickSound
import com.nopalsoft.invaders.Assets.ellipseMenuLeft
import com.nopalsoft.invaders.Assets.font15
import com.nopalsoft.invaders.Assets.languages
import com.nopalsoft.invaders.Assets.parallaxBackground
import com.nopalsoft.invaders.Assets.playSound
import com.nopalsoft.invaders.Assets.styleTextButtonBack
import com.nopalsoft.invaders.Assets.styleTextButtonMenu
import com.nopalsoft.invaders.MainInvaders

class LeaderboardScreen(game: MainInvaders) : Screens(game) {
    var btLeaderBoard: TextButton
    var btAchievements: TextButton
    var btBack: TextButton = TextButton(languages!!["back"], styleTextButtonBack)
    var btSignOut: TextButton
    var ellipseLeft: Image

    init {
        btBack.pad(0f, 15f, 35f, 0f)
        btBack.setSize(63f, 63f)
        btBack.setPosition((SCREEN_WIDTH - 63).toFloat(), (SCREEN_HEIGHT - 63).toFloat())
        btBack.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                playSound(clickSound!!)
                game.screen = MainMenuScreen(game)
            }
        })

        btLeaderBoard = TextButton(languages!!["leaderboard"], styleTextButtonMenu)
        btLeaderBoard.height = 50f // Height 50
        btLeaderBoard.setSize(50f, 0f) // We add 50 to the current width.
        btLeaderBoard.setPosition(0f, 245f)
        btLeaderBoard.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                playSound(clickSound!!)
            }
        })

        btAchievements = TextButton(languages!!["achievements"], styleTextButtonMenu)
        btAchievements.height = 50f // Height 50
        btAchievements.setSize(50f, 0f) //  We add 50 to the current width.
        btAchievements.setPosition(0f, 150f)
        btAchievements.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                playSound(clickSound!!)
            }
        })

        btSignOut = TextButton(languages!!["sign_out"], TextButtonStyle(buttonSignInUp, buttonSignInDown, null, font15))
        btSignOut.label.wrap = true
        btSignOut.width = 140f
        btSignOut.setPosition(2f, 2f)
        btSignOut.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                playSound(clickSound!!)
                game.screen = MainMenuScreen(game)
            }
        })

        ellipseLeft = Image(ellipseMenuLeft)
        ellipseLeft.setSize(18.5f, 250.5f)
        ellipseLeft.setPosition(0f, 105f)

        stage!!.addActor(btSignOut)
        stage.addActor(btAchievements)
        stage.addActor(btLeaderBoard)
        stage.addActor(btBack)
        stage.addActor(ellipseLeft)
    }

    override fun draw(delta: Float) {
        myCamera.update()
        batcher!!.projectionMatrix = myCamera.combined

        batcher.disableBlending()
        parallaxBackground!!.render(delta)
    }

    override fun update(delta: Float) {
    }

    override fun keyDown(keyPressed: Int): Boolean {
        if (keyPressed == Input.Keys.BACK || keyPressed == Input.Keys.ESCAPE) {
            playSound(clickSound!!)
            game.screen = MainMenuScreen(game)
            return true
        }
        return false
    }
}
