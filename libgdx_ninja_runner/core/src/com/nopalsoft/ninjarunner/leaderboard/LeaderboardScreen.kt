package com.nopalsoft.ninjarunner.leaderboard

import com.badlogic.gdx.Game
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.ninjarunner.Assets
import com.nopalsoft.ninjarunner.game.GameScreen
import com.nopalsoft.ninjarunner.screens.Screens

/**
 * Shows all the top scoring users of the game. It doesn't matter which social network they have
 * used for logging in to the game; here they are shown if they are leader board users of the game.
 */
class LeaderboardScreen(_game: Game?) : Screens(_game) {
    var tableMenu: Table
    var buttonLeaderboard: Button? = null
    var buttonFacebook: Button? = null
    var buttonInviteFriend: Button? = null
    var buttonGoogle: Button? = null

    var scroll: ScrollPane
    var table: Table


    init {
        val labelShop = Label("Leaderboards", Assets.labelStyleLarge)

        val tableTitle = Table()
        tableTitle.setSize(400f, 100f)
        tableTitle.setPosition(SCREEN_WIDTH / 2f - tableTitle.width / 2f, SCREEN_HEIGHT - tableTitle.height)
        tableTitle.background = Assets.backgroundTitleShop
        tableTitle.padTop(20f).padBottom(5f)

        tableTitle.row().colspan(2)
        tableTitle.add(labelShop).expand()
        tableTitle.row()

        val imgGem = Image(Assets.coinAnimation.getKeyFrame(0f))
        imgGem.setSize(20f, 20f)


        initButtons()

        tableMenu = Table()
        tableMenu.defaults().size(58f).padBottom(8f)

        tableMenu.row()
        tableMenu.add(buttonLeaderboard)

        tableMenu.row()
        tableMenu.add(buttonFacebook)

        tableMenu.row()
        tableMenu.add(buttonGoogle)

        tableMenu.row()
        tableMenu.add(buttonInviteFriend)


        val tbShop = Table()
        tbShop.setSize(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT - tableTitle.height)
        tbShop.background = Assets.backgroundShop
        tbShop.pad(25f, 5f, 15f, 5f)

        // Contenedor
        table = Table()
        table.defaults().expand().fill().padLeft(10f).padRight(20f).padBottom(10f)

        scroll = ScrollPane(table, ScrollPaneStyle(null, null, null, null, null))
        scroll.fadeScrollBars = false
        scroll.setSize(SCREEN_WIDTH - tableMenu.width, 420f)
        scroll.setPosition(tableMenu.width + 1, 0f)
        scroll.variableSizeKnobs = false

        tbShop.add(tableMenu).expandY().width(122f)
        tbShop.add(scroll).expand().fill()

        stage.addActor(tableTitle)
        stage.addActor(tbShop)


        if (game.arrayOfPersons != null) updateLeaderboard()


        buttonLeaderboard!!.isChecked = true
    }

    fun initButtons() {
        buttonLeaderboard = Button(Assets.buttonShop, Assets.buttonShopPressed, Assets.buttonShopPressed)
        buttonFacebook = Button(Assets.buttonFacebook, Assets.buttonFacebookPressed, Assets.buttonFacebookPressed)
        buttonGoogle =
            Button(Assets.buttonAchievement, Assets.buttonAchievementPressed, Assets.buttonLeaderboardPressed)
        buttonInviteFriend =
            Button(Assets.buttonSettings, Assets.buttonSettingsPressed, Assets.buttonLeaderboardPressed)

        buttonLeaderboard!!.addListener(object : ClickListener() {
        })

        buttonFacebook!!.addListener(object : ClickListener() {
        })

        buttonGoogle!!.addListener(object : ClickListener() {
        })

        buttonInviteFriend!!.addListener(object : ClickListener() {
        })

        val radioGroup = ButtonGroup<Button>()
        radioGroup.add(buttonLeaderboard, buttonFacebook, buttonGoogle, buttonInviteFriend)
    }

    override fun draw(delta: Float) {
        Assets.backgroundNubes.render(0f)
    }

    override fun update(delta: Float) {
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            changeScreenWithFadeOut(GameScreen::class.java, game)
            return true
        }
        return super.keyUp(keycode)
    }

    fun updateLeaderboard() {
        table.clear()
        for (player in game.arrayOfPersons) {
            val frame = LeaderBoardFrame(player!!)
            table.add(frame).expandX().fill()
            table.row()
        }
    }
}
