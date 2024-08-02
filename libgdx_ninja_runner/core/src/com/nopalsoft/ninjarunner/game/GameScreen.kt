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

class GameScreen(_game: Game?, showMainMenu: Boolean) : Screens(_game) {
    var myWorld: WorldGame = WorldGame()
    var state: Int = 0
    var gameUI: GameUI = GameUI()
    var menuUI: MenuUI = MenuUI(this, myWorld)
    var renderer: WorldGameRenderer = WorldGameRenderer(batcher, myWorld)

    var nextGoalFrame: NextGoalFrame? = null

    init {
        if (showMainMenu) {
            state = STATE_MENU
            menuUI.show(stage, true)
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
            gameUI.show(stage)
        }

        if (removeMenu) {
            menuUI.addAction(Actions.sequence(Actions.delay(MenuUI.ANIMATION_TIME), Actions.run(runAfterHideMenu)))
            menuUI.removeWithAnimations()
        } else {
            stage.addAction(Actions.run(runAfterHideMenu))
        }
    }

    override fun update(delta: Float) {
        if (state == STATE_MENU) {
            myWorld.myPlayer.updateStateTime(delta)
            myWorld.myPet.updateStateTime(delta)
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


    fun setNextGoalFrame(puntos: Long) {
        //Para que solo se muestren las personas que no haya superado aun
        var puntos = puntos
        if (puntos < bestScore) puntos = bestScore

        game.arrayOfPersons.sort() // Acomoda de mayor puntuacion a menor puntuacion


        var oPersonAux: Person? = null
        //Calculo la posicion del jugador que tenga mas puntos que yo. Por ejemplo si yo voy en quinto lugar
        //esta debe ser la pocion del cuarto lugar.
        var posicionArribaDeMi = game.arrayOfPersons.size - 1
        //El arreglo esta ordenado de mayor a menor
        while (posicionArribaDeMi >= 0) {
            val obj = game.arrayOfPersons[posicionArribaDeMi]
            if (obj.isMe) {
                posicionArribaDeMi--
                continue
            }

            if (obj.score > puntos) {
                oPersonAux = obj
                break
            }
            posicionArribaDeMi--
        }

        val oPersona = oPersonAux ?: return

        if (oPersona.equals(nextGoalFrame!!.myPerson)) return


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
            stage.addActor(nextGoalFrame)
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
        batcher.projectionMatrix = myCamera.combined

        batcher.begin()
        Assets.fontSmall?.draw(batcher, "FPS GERA" + Gdx.graphics.framesPerSecond, 5f, 20f)
        Assets.fontSmall?.draw(batcher, "Bodies " + myWorld.myWorldBox.bodyCount, 5f, 40f)
        Assets.fontSmall?.draw(batcher, "Vidas " + myWorld.myPlayer.lives, 5f, 60f)
        Assets.fontSmall?.draw(batcher, "Monedas " + myWorld.takenCoins, 5f, 80f)
        Assets.fontSmall?.draw(batcher, "Puntos " + myWorld.score, 5f, 100f)
        Assets.fontSmall?.draw(batcher, "Distancia " + myWorld.myPlayer.position.x, 5f, 120f)
        Assets.fontSmall?.draw(batcher, "Plataformas " + myWorld.arrayPlatforms.size, 5f, 140f)

        batcher.end()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.R) {
            game.screen = GameScreen(game, true)
            return true
        } else if (keycode == Input.Keys.SPACE || keycode == Input.Keys.W || keycode == Input.Keys.UP) {
            gameUI.didJump = true
            return true
        } else if (keycode == Input.Keys.S || keycode == Input.Keys.DOWN) {
            gameUI.didSlide = true
            return true
        } else if (keycode == Input.Keys.BACK) {
            Gdx.app.exit()
            return true
        } else if (keycode == Input.Keys.P) {
            if (game.arrayOfPersons != null) {
                setNextGoalFrame(0)
            }
            return true
        }
        return super.keyDown(keycode)
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
