package com.nopalsoft.sokoban.game

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.nopalsoft.sokoban.Assets
import com.nopalsoft.sokoban.GameState
import com.nopalsoft.sokoban.MainSokoban
import com.nopalsoft.sokoban.Movement
import com.nopalsoft.sokoban.Settings
import com.nopalsoft.sokoban.scene2d.ControlsNoPad
import com.nopalsoft.sokoban.scene2d.CounterBar
import com.nopalsoft.sokoban.scene2d.WindowPause
import com.nopalsoft.sokoban.screens.MainMenuScreen
import com.nopalsoft.sokoban.screens.Screens

class GameScreen(game: MainSokoban, var level: Int) : Screens(game) {

    private val stageGame = Stage(StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT))
    private val myBoard = Board()
    private val myControl = ControlsNoPad(this)
    private val buttonUndo = Button(Assets.buttonRefresh, Assets.buttonRefreshPressed)
    private val buttonPause = Button(Assets.buttonPause, Assets.buttonPausePressed)
    private val barTime = CounterBar(Assets.backgroundTime!!, 5f, 430f)
    private val barMoves = CounterBar(Assets.backgroundMoves!!, 5f, 380f)
    private val myWindowPause = WindowPause(this)
    private var state = GameState.STATE_RUNNING
    private val renderer = BoardRenderer()
    private val lbNivel = Label(
        "Level " + (level + 1),
        Label.LabelStyle(Assets.fontRed, Color.WHITE)
    )


    init {
        lbNivel.width = barTime.width
        lbNivel.setPosition(5f, 330f)
        lbNivel.setAlignment(Align.center)

        buttonUndo.setSize(80f, 80f)
        buttonUndo.setPosition(700f, 20f)
        buttonUndo.color.a = myControl.color.a // Have the same alpha color
        buttonUndo.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                myBoard.undo = true
            }
        })

        buttonPause.setSize(60f, 60f)
        buttonPause.setPosition(730f, 410f)

        buttonPause.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                setPause()
            }
        })

        stageGame.addActor(myBoard)
        stageGame.addActor(barTime)
        stageGame.addActor(barMoves)
        stage.addActor(lbNivel)
        stage.addActor(myControl)
        stage.addActor(buttonUndo)
        stage.addActor(buttonPause)

        setRunning()

    }


    override fun draw(delta: Float) {
        Assets.background?.render(delta)

        // Render el tileMap
        renderer.render()

        // Render el tablero
        stageGame.draw()
    }

    override fun update(delta: Float) {


        if (state != GameState.STATE_PAUSED) {
            stageGame.act(delta)
            barMoves.updateActualNum(myBoard.moves)
            barTime.updateActualNum(myBoard.time.toInt())

            if (state == GameState.STATE_RUNNING && myBoard.state == GameState.STATE_GAMEOVER) {
                setGameover()
            }
        }
    }

    override fun up() {
        myBoard.playerMovement = Movement.UP
        super.up()
    }

    override fun down() {
        myBoard.playerMovement = Movement.DOWN
        super.down()
    }

    override fun right() {
        myBoard.playerMovement = Movement.RIGHT
        super.right()
    }

    override fun left() {
        myBoard.playerMovement = Movement.LEFT
        super.left()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (state == GameState.STATE_RUNNING) {
            when (keycode) {
                Input.Keys.LEFT, Input.Keys.A -> {
                    myBoard.playerMovement = Movement.LEFT
                }

                Input.Keys.RIGHT, Input.Keys.D -> {
                    myBoard.playerMovement = Movement.RIGHT
                }

                Input.Keys.UP, Input.Keys.W -> {
                    myBoard.playerMovement = Movement.UP
                }

                Input.Keys.DOWN, Input.Keys.S -> {
                    myBoard.playerMovement = Movement.DOWN
                }

                Input.Keys.Z -> {
                    myBoard.undo = true
                }

                Input.Keys.ESCAPE, Input.Keys.BACK -> {
                    setPause()
                }
            }
        } else if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            if (myWindowPause.isShown)
                myWindowPause.hide()
        }

        return true
    }

    private fun setGameover() {
        state = GameState.STATE_GAMEOVER
        Settings.levelCompeted(level, myBoard.moves, myBoard.time.toInt())
        stage.addAction(Actions.sequence(Actions.delay(.35f), Actions.run {
            level += 1
            if (level >= Settings.NUM_MAPS)
                changeScreenWithFadeOut(MainMenuScreen::class.java, game)
            else
                changeScreenWithFadeOut(GameScreen::class.java, level, game)
        }))
    }

    fun setRunning() {
        if (state != GameState.STATE_GAMEOVER) {
            state = GameState.STATE_RUNNING
        }
    }

    private fun setPause() {
        if (state == GameState.STATE_RUNNING) {
            state = GameState.STATE_PAUSED
            myWindowPause.show(stage)
        }
    }
}