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

class MainMenuScreen(game: MainSokoban) : Screens(game) {

    private val levelSelector = LevelSelector(this)
    private val tableMenu = Table()
    private val buttonLeaderboard =
        Button(Assets.buttonLeaderboard, Assets.buttonLeaderboardPressed)
    private val buttonAchievements =
        Button(Assets.buttonAchievement, Assets.buttonAchievementPressed)
    private val buttonFacebook = Button(Assets.buttonFacebook, Assets.buttonFacebookPressed)
    private val buttonSettings = Button(Assets.buttonSettings, Assets.buttonSettingsPressed)
    private val buttonMore = Button(Assets.buttonMore, Assets.buttonMorePressed)
    private val buttonNextPage = Button(Assets.buttonRight, Assets.buttonRightPressed)
    private val buttonPreviousPage = Button(Assets.buttonLeft, Assets.buttonLeftPressed)


    init {
        buttonPreviousPage.setSize(75f, 75f)
        buttonPreviousPage.setPosition(65f, 220f)
        buttonPreviousPage.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                right()
            }
        })

        buttonNextPage.setSize(75f, 75f)
        buttonNextPage.setPosition(660f, 220f)
        buttonNextPage.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                left()
            }
        })

        buttonLeaderboard.addListener(object : ClickListener() {
        })
        buttonAchievements.addListener(object : ClickListener() {
        })
        buttonFacebook.addListener(object : ClickListener() {
        })
        buttonSettings.addListener(object : ClickListener() {
        })
        buttonMore.addListener(object : ClickListener() {
        })

        tableMenu.defaults().size(80f).pad(7.5f)

        tableMenu.add(buttonAchievements)
        tableMenu.add(buttonFacebook)
        tableMenu.add(buttonSettings)
        tableMenu.add(buttonMore)

        tableMenu.pack()
        tableMenu.setPosition(SCREEN_WIDTH / 2f - tableMenu.width / 2f, 20f)

        stage.addActor(levelSelector)
        stage.addActor(tableMenu)
        stage.addActor(buttonPreviousPage)
        stage.addActor(buttonNextPage)
    }


    override fun update(delta: Float) {
        // TODO
    }

    override fun draw(delta: Float) {
        Assets.background?.render(delta)
    }

    override fun right() {
        levelSelector.previousPage()
    }

    override fun left() {
        levelSelector.nextPage()
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