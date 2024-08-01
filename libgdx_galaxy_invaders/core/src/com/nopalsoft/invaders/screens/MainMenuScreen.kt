package com.nopalsoft.invaders.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.nopalsoft.invaders.Assets.buttonMusicOff
import com.nopalsoft.invaders.Assets.buttonMusicOn
import com.nopalsoft.invaders.Assets.buttonSoundOff
import com.nopalsoft.invaders.Assets.buttonSoundOn
import com.nopalsoft.invaders.Assets.clickSound
import com.nopalsoft.invaders.Assets.ellipseMenuLeft
import com.nopalsoft.invaders.Assets.font10
import com.nopalsoft.invaders.Assets.font60
import com.nopalsoft.invaders.Assets.languages
import com.nopalsoft.invaders.Assets.music
import com.nopalsoft.invaders.Assets.parallaxBackground
import com.nopalsoft.invaders.Assets.playSound
import com.nopalsoft.invaders.Assets.styleTextButtonFacebook
import com.nopalsoft.invaders.Assets.styleTextButtonMenu
import com.nopalsoft.invaders.Assets.titleMenuBox
import com.nopalsoft.invaders.MainInvaders
import com.nopalsoft.invaders.Settings.highScores
import com.nopalsoft.invaders.Settings.musicEnabled
import com.nopalsoft.invaders.Settings.numberOfTimesGameHasBeenPlayed
import com.nopalsoft.invaders.Settings.soundEnabled
import com.nopalsoft.invaders.game.GameScreen


class MainMenuScreen(game: MainInvaders) : Screens(game) {
    private var buttonPlay: TextButton
    private var buttonSettings: TextButton
    private var buttonLeaderBoard: TextButton
    private var buttonMore: TextButton
    private var buttonFacebook: TextButton

    private var labelHighestScore: Label

    var buttonSound: ImageButton
    var buttonMusic: ImageButton
    private var ellipseLeft: Image

    init {
        val titleTable = Table()
        titleTable.background = titleMenuBox
        val qualification = Label(languages!!["titulo_app"], LabelStyle(font60, Color.GREEN))
        qualification.setAlignment(Align.center)
        titleTable.setSize(265f, 100f)
        titleTable.setPosition((SCREEN_WIDTH - 265) / 2f, (SCREEN_HEIGHT - 110).toFloat())
        titleTable.add(qualification).expand().center()

        // I put the text in the update
        labelHighestScore = Label("", LabelStyle(font10, Color.GREEN))
        labelHighestScore.width = SCREEN_WIDTH.toFloat()
        labelHighestScore.setAlignment(Align.center)
        labelHighestScore.setPosition(0f, (SCREEN_HEIGHT - 120).toFloat())

        buttonPlay = TextButton(languages!!["play"], styleTextButtonMenu)
        buttonPlay.setSize(250f, 50f)
        buttonPlay.setPosition(0f, 280f)
        buttonPlay.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                playSound(clickSound!!)
                game.screen = GameScreen(game)
            }
        })

        buttonSettings = TextButton(languages!!["settings"], styleTextButtonMenu)
        buttonSettings.setSize(300f, 50f)
        buttonSettings.setPosition(0f, 210f)
        buttonSettings.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                playSound(clickSound!!)
                game.screen = SettingsScreen(game)
            }
        })

        buttonLeaderBoard = TextButton(languages!!["leaderboard"], styleTextButtonMenu)
        buttonLeaderBoard.setSize(310f, 50f)
        buttonLeaderBoard.setPosition(0f, 140f)
        buttonLeaderBoard.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                playSound(clickSound!!)
                game.screen = LeaderboardScreen(game)
            }
        })

        buttonMore = TextButton(languages!!["more"], styleTextButtonMenu)
        buttonMore.setSize(250f, 50f)
        buttonMore.setPosition(0f, 70f)
        buttonMore.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                playSound(clickSound!!)
            }
        })

        buttonFacebook = TextButton(languages!!["like_us_to_get_lastest_news"], styleTextButtonFacebook)
        buttonFacebook.label.wrap = true
        buttonFacebook.width = 170f
        buttonFacebook.setPosition(SCREEN_WIDTH - buttonFacebook.width - 2, 2f)
        buttonFacebook.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                playSound(clickSound!!)
                Gdx.net.openURI("https://www.facebook.com/yayo28")
            }
        })

        buttonSound = ImageButton(buttonSoundOn, buttonSoundOff, buttonSoundOff)
        buttonSound.setSize(40f, 40f)
        buttonSound.setPosition(2f, 2f)
        if (!soundEnabled) buttonSound.isChecked = true
        buttonSound.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                soundEnabled = !soundEnabled
                playSound(clickSound!!)
                buttonSound.isChecked = !soundEnabled
            }
        })

        buttonMusic = ImageButton(buttonMusicOn, buttonMusicOff, buttonMusicOff)
        buttonMusic.setSize(40f, 40f)
        buttonMusic.setPosition(44f, 2f)
        if (!musicEnabled) buttonMusic.isChecked = true
        buttonMusic.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                musicEnabled = !musicEnabled
                playSound(clickSound!!)
                if (!musicEnabled) {
                    buttonMusic.isChecked = true
                    music!!.pause()
                } else {
                    buttonMusic.isChecked = false
                    music!!.play()
                }
            }
        })

        // Las medidas se sacaron con una formual de 3 si 480 / 960 x 585 donde 585 es el tamano,
        // 960 es el tamano para lo que se hicieron y 480 es el tamano de la camara
        ellipseLeft = Image(ellipseMenuLeft)
        ellipseLeft.setSize(18.5f, 292.5f)
        ellipseLeft.setPosition(0f, 60f)

        stage!!.addActor(titleTable)
        stage.addActor(labelHighestScore)

        stage.addActor(buttonPlay)
        stage.addActor(buttonSettings)
        stage.addActor(buttonLeaderBoard)
        stage.addActor(buttonMore)
        stage.addActor(ellipseLeft)
        stage.addActor(buttonSound)
        stage.addActor(buttonMusic)
        stage.addActor(buttonFacebook)


        if (numberOfTimesGameHasBeenPlayed == 0) {
            game.dialogs!!.showDialogSignIn()
        }
    }

    override fun update(delta: Float) {
        labelHighestScore.setText(languages!!.format("local_highest_score", highScores[0].toString()))
    }

    override fun draw(delta: Float) {
        myCamera.update()
        batcher!!.projectionMatrix = myCamera.combined

        batcher.disableBlending()
        parallaxBackground!!.render(delta)
    }

    override fun keyDown(tecleada: Int): Boolean {
        if (tecleada == Input.Keys.BACK || tecleada == Input.Keys.ESCAPE) {
            playSound(clickSound!!)
            if (game.dialogs!!.isDialogShown) {
                game.dialogs!!.dismissAll()
            } else {
                Gdx.app.exit()
            }
            return true
        }
        return false
    }
}
