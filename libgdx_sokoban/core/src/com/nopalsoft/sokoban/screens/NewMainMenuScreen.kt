package com.nopalsoft.sokoban.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.sokoban.Assets
import com.nopalsoft.sokoban.MainSokoban
import com.nopalsoft.sokoban.scene2d.LevelSelector

class NewMainMenuScreen(game: MainSokoban):Screens(game ) {

    private val lvlSelector = LevelSelector(this)
    private val tbMenu = Table()
    private val btLeaderboard= Button(Assets.buttonLeaderboard, Assets.buttonLeaderboardPressed)
    private val btAchievements =Button(Assets.buttonAchievement, Assets.buttonAchievementPressed)
    private val btFacebook=Button(Assets.buttonFacebook, Assets.buttonFacebookPressed)
    private val btSettings =Button(Assets.buttonSettings, Assets.buttonSettingsPressed)
    private val btMore=Button(Assets.buttonMore, Assets.buttonMorePressed)
    private val btNextPage = Button(Assets.buttonRight, Assets.buttonRightPressed)
    private val btPreviousPage=Button(Assets.buttonLeft, Assets.buttonLeftPressed)





    init {
        btPreviousPage.setSize(75f, 75f)
        btPreviousPage.setPosition(65f, 220f)
        btPreviousPage.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                right()
            }
        })

        btNextPage.setSize(75f, 75f)
        btNextPage.setPosition(660f, 220f)
        btNextPage.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                left()
            }
        })

        btLeaderboard.addListener(object : ClickListener() {
        })
        btAchievements.addListener(object : ClickListener() {
        })
        btFacebook.addListener(object : ClickListener() {
        })
        btSettings.addListener(object : ClickListener() {
        })
        btMore.addListener(object : ClickListener() {
        })

        tbMenu.defaults().size(80f).pad(7.5f)

        tbMenu.add(btAchievements)
        tbMenu.add(btFacebook)
        tbMenu.add(btSettings)
        tbMenu.add(btMore)

        tbMenu.pack()
        tbMenu.setPosition(SCREEN_WIDTH / 2f - tbMenu.width / 2f, 20f)

        stage.addActor(lvlSelector)
        stage.addActor(tbMenu)
        stage.addActor(btPreviousPage)
        stage.addActor(btNextPage)
    }





    override fun update(delta: Float) {
       // TODO
    }

    override fun draw(delta: Float) {
        Assets.background.render(delta)
    }

    override fun right() {
        lvlSelector.previousPage()
    }

    override fun left() {
        lvlSelector.nextPage()
    }

    override fun keyDown(keycode: Int): Boolean {

        when (keycode) {
            Input.Keys.LEFT, Input.Keys.A -> {
                right()
            }
            Input.Keys.RIGHT, Input.Keys.D -> {
                left()
            }
            Input.Keys.ESCAPE, Input.Keys.BACK -> {
                Gdx.app.exit()
            }
        }

        return true
    }
}