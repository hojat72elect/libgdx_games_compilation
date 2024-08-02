package com.nopalsoft.ninjarunner.game

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.nopalsoft.ninjarunner.Assets
import com.nopalsoft.ninjarunner.Settings.bestScore
import com.nopalsoft.ninjarunner.Settings.isMusicEnabled
import com.nopalsoft.ninjarunner.Settings.setNewScore
import com.nopalsoft.ninjarunner.leaderboard.NextGoalFrame
import com.nopalsoft.ninjarunner.leaderboard.Person
import com.nopalsoft.ninjarunner.scene2d.GameUI
import com.nopalsoft.ninjarunner.scene2d.MenuUI
import com.nopalsoft.ninjarunner.screens.Screens

class GameScreen(game: Game, showMainMenu: Boolean) : Screens(game) {
    private var myWorld: WorldGame = WorldGame()
    var state: Int = 0
    private var gameUI: GameUI = GameUI()
    private var menuUI: MenuUI = MenuUI(this, myWorld)
    private var renderer: WorldGameRenderer = WorldGameRenderer(batcher!!, myWorld)

    private var nextGoalFrame: NextGoalFrame? = null

    init {
        if (showMainMenu) {
            state = STATE_MENU
            menuUI.show(stage!!, true)
        } else {
            setRunning(false)
        }
    }

    fun setRunning(removeMenu: Boolean) {
        val runAfterHideMenu = Runnable {
            val run = Runnable {
                state = STATE_RUNNING
                if (isMusicEnabled) {
                    Assets.music1?.play()
                }

                nextGoalFrame = NextGoalFrame(SCREEN_WIDTH.toFloat(), 400f)
                setNextGoalFrame(0)
            }
            gameUI.addAction(Actions.sequence(Actions.delay(GameUI.ANIMATION_TIME), Actions.run(run)))
            gameUI.show(stage!!)
        }

        if (removeMenu) {
            menuUI.addAction(Actions.sequence(Actions.delay(MenuUI.ANIMATION_TIME), Actions.run(runAfterHideMenu)))
            menuUI.removeWithAnimations()
        } else {
            stage?.addAction(Actions.run(runAfterHideMenu))
        }
    }

    override fun update(delta: Float) {
        if (state == STATE_MENU) {
            myWorld.myPlayer?.updateStateTime(delta)
            myWorld.myPet?.updateStateTime(delta)
        } else if (state == STATE_RUNNING) {
            myWorld.update(delta, gameUI.didJump, gameUI.didDash, gameUI.didSlide)

            gameUI.didJump = false
            gameUI.didDash = false

            if (myWorld.state == WorldGame.STATE_GAMEOVER) {
                setGameover()
            }

            setNextGoalFrame(myWorld.score)
            nextGoalFrame!!.updateScore(myWorld.score)
        } else if (state == STATE_GAME_OVER) {
            if (Gdx.input.justTouched()) {
                game.screen = GameScreen(game, true)
            }
        }
    }


    private fun setNextGoalFrame(puntos: Long) {
        //So that only the people you have not surpassed yet are shown.
        var puntos = puntos
        if (puntos < bestScore) puntos = bestScore

        game.arrayOfPersons.sort() // Acomoda de mayor puntuacion a menor puntuacion


        var oPersonAux: Person? = null
        // I calculate the position of the player who has more points than me. For example if I go in fifth place,
        // this must be the fourth place position.
        var positionAboveMe = game.arrayOfPersons.size - 1
        // The arrangement is ordered from largest to smallest.
        while (positionAboveMe >= 0) {
            val obj = game.arrayOfPersons[positionAboveMe]
            if (obj.isMe) {
                positionAboveMe--
                continue
            }

            if (obj.score > puntos) {
                oPersonAux = obj
                break
            }
            positionAboveMe--
        }

        val oPersona = oPersonAux ?: return

        if (oPersona == nextGoalFrame!!.myPerson) return


        val run = Runnable {
            nextGoalFrame!!.updatePerson(oPersona)
            nextGoalFrame!!.addAction(
                Actions.sequence(
                    Actions.moveTo(
                        SCREEN_WIDTH - NextGoalFrame.WIDTH,
                        nextGoalFrame!!.y,
                        1f
                    )
                )
            )
        }

        if (!nextGoalFrame!!.hasParent()) {
            stage?.addActor(nextGoalFrame)
            Gdx.app.postRunnable(run)
        } else if (!nextGoalFrame!!.hasActions()) {
            nextGoalFrame!!.addAction(
                Actions.sequence(
                    Actions.moveTo(SCREEN_WIDTH.toFloat(), nextGoalFrame!!.y, 1f),
                    Actions.run(run)
                )
            )
        }
    }

    private fun setGameover() {
        setNewScore(myWorld.score)
        state = STATE_GAME_OVER
        Assets.music1?.stop()
    }

    override fun right() {
        super.right()
        gameUI.didDash = true
    }

    override fun draw(delta: Float) {
        if (state == STATE_MENU) {
            Assets.backgroundNubes?.render(0f)
        } else {
            Assets.backgroundNubes?.render(delta)
        }

        renderer.render(delta)

        myCamera.update()
        batcher?.projectionMatrix = myCamera.combined

        batcher?.begin()
        Assets.fontSmall?.draw(batcher, "FPS GERA" + Gdx.graphics.framesPerSecond, 5f, 20f)
        Assets.fontSmall?.draw(batcher, "Bodies " + myWorld.myWorldBox.bodyCount, 5f, 40f)
        Assets.fontSmall?.draw(batcher, "Vidas " + myWorld.myPlayer?.lives, 5f, 60f)
        Assets.fontSmall?.draw(batcher, "Monedas " + myWorld.takenCoins, 5f, 80f)
        Assets.fontSmall?.draw(batcher, "Puntos " + myWorld.score, 5f, 100f)
        Assets.fontSmall?.draw(batcher, "Distancia " + myWorld.myPlayer?.position?.x, 5f, 120f)
        Assets.fontSmall?.draw(batcher, "Plataformas " + myWorld.arrayPlatforms.size, 5f, 140f)

        batcher?.end()
    }

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.R -> {
                game.screen = GameScreen(game, true)
                return true
            }

            Input.Keys.SPACE, Input.Keys.W, Input.Keys.UP -> {
                gameUI.didJump = true
                return true
            }

            Input.Keys.S, Input.Keys.DOWN -> {
                gameUI.didSlide = true
                return true
            }

            Input.Keys.BACK -> {
                Gdx.app.exit()
                return true
            }

            Input.Keys.P -> {
                setNextGoalFrame(0)
                return true
            }

            else -> return super.keyDown(keycode)
        }
    }

    override fun keyUp(keycode: Int): Boolean {
        if (keycode == Input.Keys.S || keycode == Input.Keys.DOWN) {
            gameUI.didSlide = false
            return true
        }
        return super.keyUp(keycode)
    }

    companion object {
        const val STATE_MENU: Int = 0
        const val STATE_RUNNING: Int = 1
        const val STATE_GAME_OVER: Int = 2
    }
}
