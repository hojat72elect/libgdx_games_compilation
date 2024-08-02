package com.nopalsoft.ninjarunner.scene2d

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.ninjarunner.Assets
import com.nopalsoft.ninjarunner.game.GameScreen
import com.nopalsoft.ninjarunner.game.WorldGame
import com.nopalsoft.ninjarunner.leaderboard.LeaderboardScreen
import com.nopalsoft.ninjarunner.screens.Screens
import com.nopalsoft.ninjarunner.screens.SettingsScreen
import com.nopalsoft.ninjarunner.shop.ShopScreen

class MenuUI(gameScreen: GameScreen, myWorld: WorldGame) : Group() {
    var gameScreen: GameScreen
    var myWorld: WorldGame
    var title: Image? = null

    var tableMenu: Table? = null

    var buttonPlay: Button? = null
    var buttonShop: Button? = null
    var buttonLeaderboard: Button? = null
    var buttonAchievements: Button? = null
    var buttonSettings: Button? = null
    var buttonRate: Button? = null
    var buttonShare: Button? = null

    var showMainMenu: Boolean = false

    init {
        setBounds(0f, 0f, Screens.SCREEN_WIDTH.toFloat(), Screens.SCREEN_HEIGHT.toFloat())
        this.gameScreen = gameScreen
        this.myWorld = myWorld

        initialize()
    }

    private fun initialize() {
        title = Image(Assets.title)
        title!!.setScale(1f)
        title!!.setPosition(width / 2f - title!!.width * title!!.scaleX / 2f, Screens.SCREEN_HEIGHT + title!!.height)

        tableMenu = Table()
        tableMenu!!.setSize(122f, height)
        tableMenu!!.background = Assets.backgroundMenu

        initButtons()

        tableMenu!!.pad(25f, 20f, 10f, 0f)
        tableMenu!!.defaults().size(80f).padBottom(15f)


        tableMenu!!.row().colspan(2)
        tableMenu!!.add(buttonShop)

        tableMenu!!.row().colspan(2)
        tableMenu!!.add(buttonLeaderboard)

        tableMenu!!.row().colspan(2)
        tableMenu!!.add(buttonAchievements)

        tableMenu!!.row().colspan(2)
        tableMenu!!.add(buttonSettings)

        tableMenu!!.row().size(40f).padRight(5f).padLeft(5f)
        tableMenu!!.add(buttonRate)
        tableMenu!!.add(buttonShare)

        tableMenu!!.setPosition(Screens.SCREEN_WIDTH + tableMenu!!.width, 0f)

        addActor(tableMenu)
        addActor(buttonPlay)
    }

    fun initButtons() {
        buttonShop = Button(Assets.buttonShop, Assets.buttonShopPressed)
        buttonLeaderboard = Button(Assets.buttonLeaderboard, Assets.buttonLeaderboardPressed)
        buttonAchievements = Button(Assets.buttonAchievement, Assets.buttonAchievementPressed)
        buttonSettings = Button(Assets.buttonSettings, Assets.buttonSettingsPressed)
        buttonRate = Button(Assets.buttonRate, Assets.buttonRatePressed)
        buttonShare = Button(Assets.buttonShare, Assets.buttonSharePressed)

        buttonPlay = Button(ButtonStyle(null, null, null))
        buttonPlay!!.setSize(width - tableMenu!!.width, height)
        buttonPlay!!.setPosition(0f, 0f)
        buttonPlay!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (showMainMenu) gameScreen.setRunning(true)
                else {
                    gameScreen.game.screen = GameScreen(gameScreen.game, false)
                }
            }
        })

        buttonShop!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                gameScreen.changeScreenWithFadeOut(ShopScreen::class.java, gameScreen.game)
            }
        })

        buttonRate!!.addListener(object : ClickListener() {
        })

        buttonShare!!.addListener(object : ClickListener() {
        })

        buttonLeaderboard!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                gameScreen.game.screen = LeaderboardScreen(gameScreen.game)
            }
        })

        buttonAchievements!!.addListener(object : ClickListener() {
        })

        buttonSettings!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                gameScreen.changeScreenWithFadeOut(SettingsScreen::class.java, gameScreen.game)
            }
        })
    }

    private fun addInActions() {
        title!!.addAction(Actions.moveTo(width / 2f - title!!.width * title!!.scaleX / 2f, 300f, ANIMATION_TIME))
        tableMenu!!.addAction(Actions.moveTo(Screens.SCREEN_WIDTH - tableMenu!!.width, 0f, ANIMATION_TIME))
    }

    private fun addOutActions() {
        title!!.addAction(
            Actions.moveTo(
                width / 2f - title!!.width * title!!.scaleX / 2f, Screens.SCREEN_HEIGHT + title!!.height,
                ANIMATION_TIME
            )
        )

        tableMenu!!.addAction(Actions.moveTo(Screens.SCREEN_WIDTH + tableMenu!!.width, 0f, ANIMATION_TIME))
    }

    fun show(stage: Stage, showMainMenu: Boolean) {
        addInActions()
        stage.addActor(this)

        title!!.remove()
        addActor(title)
        this.showMainMenu = showMainMenu
    }

    fun removeWithAnimations() {
        addOutActions()
        addAction(Actions.sequence(Actions.delay(ANIMATION_TIME), Actions.removeActor()))
    }

    companion object {
        const val ANIMATION_TIME: Float = .35f
    }
}
