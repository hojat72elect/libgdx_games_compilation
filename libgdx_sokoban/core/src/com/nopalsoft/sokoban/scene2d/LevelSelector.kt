package com.nopalsoft.sokoban.scene2d

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.sokoban.Assets
import com.nopalsoft.sokoban.Settings
import com.nopalsoft.sokoban.screens.MainMenuScreen
import com.nopalsoft.sokoban.screens.Screens

class LevelSelector(currentScreen: Screens) : Group() {

    private val menuScreen = currentScreen as MainMenuScreen
    private val labelTitle = Label("Niveles", Label.LabelStyle(Assets.font, Color.WHITE))
    private val container = Table()
    private val scrollPane = ScrollPane(container)

    // Current page (each page has 15 levels).
    private var actualPage = 0
    private var totalStars = 0

    private val windowLevel = WindowLevel(currentScreen)


    init {
        setSize(600f, 385f)
        setPosition(Screens.SCREEN_WIDTH / 2f - width / 2f, 70f)
        setBackGround(Assets.backgroundWindow!!)

        val tableTitle = Table()
        tableTitle.setSize(300f, 50f)
        tableTitle.setPosition(width / 2f - tableTitle.width / 2f, 324f)
        tableTitle.add(labelTitle)

        scrollPane.setSize(width - 100, height - 100)
        scrollPane.setPosition(width / 2f - scrollPane.width / 2f, 30f)
        scrollPane.setScrollingDisabled(false, true)

        container.defaults().padLeft(5f).padRight(5f)

        for (i in Settings.arrayLevel.indices) {
            totalStars += Settings.arrayLevel[i]!!.numStars
        }
        totalStars += 2 // By default I already have 3 stars

        var numberOfPages = Settings.NUM_MAPS / 15
        if (Settings.NUM_MAPS % 15f != 0f)
            numberOfPages++

        for (column in 0 until numberOfPages) {
            container.add(getListLevel(column))
        }


        addActor(tableTitle)
        addActor(scrollPane)
        scrollToPage(0)

    }

    private fun setBackGround(imageBackground: TextureRegionDrawable) {
        val img = Image(imageBackground)
        img.setSize(width, height)
        addActor(img)
    }

    /**
     * Each list has 15 items.
     */
    private fun getListLevel(list: Int): Table {
        val content = Table()

        var level = list * 15
        content.defaults().pad(7f, 12f, 7f, 12f)
        for (col in 0..14) {
            val oButton = getLevelButton(level)
            content.add(oButton).size(76f, 83f)
            level++

            if (level % 5 == 0)
                content.row()

            // To hide worlds that do not exist
            if (level > Settings.NUM_MAPS)
                oButton.isVisible = false
        }
        return content
    }

    private fun scrollToPage(page: Int) {
        val tabToScrollTo = container.children[page] as Table
        scrollPane.scrollTo(
            tabToScrollTo.x,
            tabToScrollTo.y,
            tabToScrollTo.width,
            tabToScrollTo.height
        )
    }

    fun nextPage() {
        actualPage++
        if (actualPage >= container.children.size)
            actualPage = container.children.size - 1
        scrollToPage(actualPage)
    }

    fun previousPage() {
        actualPage--
        if (actualPage < 0)
            actualPage = 0
        scrollToPage(actualPage)
    }

    private fun getLevelButton(level: Int): Button {
        val button: TextButton

        val skullsToNextLevel = level * 1 // I only need 1 star to unlock the next level.

        if (totalStars < skullsToNextLevel) {
            button = TextButton("", Assets.styleTextButtonLevelLocked)
            button.isDisabled = true
        } else {
            button = TextButton((level + 1).toString(), Assets.styleTextButtonLevel)

            // I am adding worlds that do not exist to be able to fill the table that is why I put this fix.
            var completed = false
            if (level < Settings.arrayLevel.size) {
                if (Settings.arrayLevel[level]!!.numStars == 1)
                    completed = true
            }

            val imageLevel = if (completed) {
                Image(Assets.levelStar)
            } else {
                Image(Assets.levelOff)
            }

            button.row()
            button.add(imageLevel).size(10f).padBottom(2f)
        }

        button.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (!button.isDisabled) {
                    windowLevel.show(
                        stage,
                        level,
                        Settings.arrayLevel[level]!!.bestMoves,
                        Settings.arrayLevel[level]!!.bestTime
                    )
                }
            }
        })

        menuScreen.addEffectPress(button)

        return button
    }

}