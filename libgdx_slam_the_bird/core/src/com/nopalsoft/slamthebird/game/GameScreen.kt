package com.nopalsoft.slamthebird.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.slamthebird.Achievements.unlockCoins
import com.nopalsoft.slamthebird.Assets
import com.nopalsoft.slamthebird.Assets.pauseMusic
import com.nopalsoft.slamthebird.Assets.playMusic
import com.nopalsoft.slamthebird.MainSlamBird
import com.nopalsoft.slamthebird.Settings
import com.nopalsoft.slamthebird.Settings.setBestScores
import com.nopalsoft.slamthebird.scene2d.LabelCoins
import com.nopalsoft.slamthebird.scene2d.LabelCombo
import com.nopalsoft.slamthebird.scene2d.LabelScore
import com.nopalsoft.slamthebird.scene2d.WindowPause
import com.nopalsoft.slamthebird.scene2d.WindowRate
import com.nopalsoft.slamthebird.screens.Screens
import com.nopalsoft.slamthebird.shop.ShopScreen

class GameScreen(game: MainSlamBird?) : Screens(game!!) {
    var worldGame: WorldGame
    var renderer: WorldGameRender
    var imageGameOver: Image? = null
    var groupTryAgain: Group
    var groupButtons: Group? = null
    var imageAppTitle: Image? = null
    var sideComboText: Boolean = false // Create new scratch file from selection.

    var windowRate: WindowRate
    var windowPause: WindowPause
    var combo: Int = 0

    init {
        worldGame = WorldGame()
        renderer = WorldGameRender(batcher!!, worldGame)

        groupTryAgain = Group()
        windowRate = WindowRate(this)
        windowPause = WindowPause(this)

        setUpButtons()
        setUpGameover()
        setReady()
    }

    private fun setUpButtons() {
        groupButtons = Group()
        groupButtons!!.setSize(stage!!.width, stage!!.height)
        groupButtons!!.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent, x: Float, y: Float,
                pointer: Int, button: Int
            ): Boolean {
                if (windowRate.isVisible) return false

                setRunning()
                Settings.numberOfTimesPlayed++
                return true
            }
        })

        val bestScore = Image(Assets.bestScore)
        bestScore.setSize(170f, 25f)
        bestScore.setPosition(SCREEN_WIDTH / 2f - bestScore.width / 2f, 770f)
        bestScore.addAction(
            Actions.repeat(
                Int.MAX_VALUE,
                Actions.sequence(
                    Actions.alpha(.6f, .75f),
                    Actions.alpha(1f, .75f)
                )
            )
        )

        val buttonShop = Button(Assets.buttonShop)
        buttonShop.setSize(90f, 70f)
        buttonShop.setPosition(0f, 730f)
        addEfectoPress(buttonShop)
        buttonShop.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                changeScreenWithFadeOut(ShopScreen::class.java, game)
            }
        })

        val buttonMore = Button(Assets.buttonMore)
        buttonMore.setSize(90f, 70f)
        buttonMore.setPosition(390f, 730f)
        addEfectoPress(buttonMore)
        buttonMore.addListener(object : ClickListener() {
        })

        val buttonLeaderboard = Button(Assets.buttonLeaderBoard)
        buttonLeaderboard.setSize(110f, 75f)
        buttonLeaderboard.setPosition((230 - 110).toFloat(), 310f)
        addEfectoPress(buttonLeaderboard)
        buttonLeaderboard.addListener(object : ClickListener() {
        })

        val buttonAchievements = Button(Assets.buttonAchievements)
        buttonAchievements.setSize(110f, 75f)
        buttonAchievements.setPosition(250f, 310f)
        addEfectoPress(buttonAchievements)
        buttonAchievements.addListener(object : ClickListener() {
        })

        val buttonRate = Button(Assets.buttonRate)
        buttonRate.setSize(110f, 75f)

        buttonRate.setPosition(
            SCREEN_WIDTH / 2f - buttonRate.width / 2f - 25,
            220f
        ) // Con el boton face y twitter cambia la pos
        addEfectoPress(buttonRate)
        buttonRate.addListener(object : ClickListener() {
        })

        val buttonShareFacebook = Button(
            TextureRegionDrawable(
                Assets.buttonFacebook
            )
        )
        buttonShareFacebook.setSize(40f, 40f)
        buttonShareFacebook.setPosition(280f, 257f)
        addEfectoPress(buttonShareFacebook)
        buttonShareFacebook.addListener(object : ClickListener() {
        })

        val buttonShareTwitter = Button(TextureRegionDrawable(Assets.buttonTwitter))
        buttonShareTwitter.setSize(40f, 40f)
        buttonShareTwitter.setPosition(280f, 212f)
        addEfectoPress(buttonShareTwitter)
        buttonShareTwitter.addListener(object : ClickListener() {
        })

        val btMusica = Button(Assets.buttonStyleMusic)
        btMusica.setPosition(5f, 100f)
        btMusica.isChecked = !Settings.isMusicOn
        btMusica.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent, x: Float, y: Float,
                pointer: Int, button: Int
            ): Boolean {
                event.stop()
                return true
            }
        })
        btMusica.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                event.stop()
                Settings.isMusicOn = !Settings.isMusicOn
                btMusica.isChecked = !Settings.isMusicOn
                if (Settings.isMusicOn) playMusic()
                else pauseMusic()
                Gdx.app.log("Muscia", Settings.isMusicOn.toString() + "")
            }
        })

        val btSonido = Button(Assets.buttonStyleSound)
        btSonido.setPosition(5f, 180f)
        btSonido.isChecked = !Settings.isSoundOn
        btSonido.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent, x: Float, y: Float,
                pointer: Int, button: Int
            ): Boolean {
                event.stop()
                return true
            }
        })
        btSonido.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                Settings.isSoundOn = !Settings.isSoundOn
                btSonido.isChecked = !Settings.isSoundOn
            }
        })

        val tapToPlay = Image(Assets.tapToPlay)
        tapToPlay.setSize(333f, 40f)
        tapToPlay
            .setPosition(SCREEN_WIDTH / 2f - tapToPlay.width / 2f, 140f)
        tapToPlay.setOrigin(
            tapToPlay.width / 2f,
            tapToPlay.height / 2f
        )
        val scaleTime = .75f
        tapToPlay.addAction(
            Actions.repeat(
                Int.MAX_VALUE, Actions.sequence(
                    Actions.scaleTo(.95f, .95f, scaleTime),
                    Actions.scaleTo(1f, 1f, scaleTime)
                )
            )
        )


        groupButtons!!.addActor(tapToPlay)
        groupButtons!!.addActor(bestScore)
        groupButtons!!.addActor(buttonShop)
        groupButtons!!.addActor(buttonMore)
        groupButtons!!.addActor(buttonLeaderboard)
        groupButtons!!.addActor(buttonAchievements)
        groupButtons!!.addActor(buttonRate)
        groupButtons!!.addActor(btMusica)
        groupButtons!!.addActor(btSonido)
        groupButtons!!.addActor(buttonShareFacebook)
        groupButtons!!.addActor(buttonShareTwitter)
    }

    private fun setUpGameover() {
        imageGameOver = Image(Assets.backgroundGameOver)
        imageGameOver!!.setSize(SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        imageGameOver!!.setOrigin(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f)
        imageGameOver!!.setScale(2f)
        imageGameOver!!.addAction(
            Actions.sequence(
                Actions.scaleTo(1.1f, 1.1f, .25f), Actions.delay(1f),
                Actions.run {
                    imageGameOver!!.remove()
                    imageGameOver!!.setScale(2f)
                    setTryAgain()
                })
        )
    }

    override fun update(delta: Float) {
        when (state) {
            STATE_RUNNING -> updateRunning(delta)
            STATE_READY, STATE_TRY_AGAIN -> updateReady(delta)
            else -> {}
        }
    }

    private fun updateReady(delta: Float) {
        var acelX = Gdx.input.accelerometerX * -1 / 5f

        if (Gdx.input.isKeyPressed(Input.Keys.A)) acelX = -1f
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) acelX = 1f

        worldGame.updateReady(delta, acelX)
    }

    private fun updateRunning(delta: Float) {
        var slam = false
        var acelX = Gdx.input.accelerometerX * -1 / 5f

        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) acelX = -1f
        else if (Gdx.input.isKeyPressed(Input.Keys.D)
            || Gdx.input.isKeyPressed(Input.Keys.RIGHT)
        ) acelX = 1f
        Gdx.app.log("Slam is", " " + false)

        if (Gdx.input.isTouched || Gdx.input.isKeyPressed(Input.Keys.SPACE)
            || Gdx.input.isKeyPressed(Input.Keys.DOWN)
        ) {
            slam = true
            Gdx.app.log("Slam is", " " + true)
        }

        worldGame.update(delta, acelX, slam)

        if (worldGame.state == WorldGame.STATE_GAME_OVER) {
            setGameover()
        }

        if (worldGame.combo == 0) combo = 0

        if (worldGame.combo > combo) {
            stage!!.batch.setColor(1f, 1f, 1f, 1f) // Un BUG que no pone el alpha en 1 otra vez

            combo = worldGame.combo
            val lblCombo = LabelCombo(
                worldGame.robot!!.position.x * 100,
                worldGame.robot!!.position.y * 100 - 50, combo
            )

            val sideToMove: Float
            if (sideComboText) {
                sideToMove = 0f
                sideComboText = false
            } else {
                sideComboText = true
                sideToMove = 380f
            }

            lblCombo.addAction(
                Actions.sequence(
                    Actions.moveTo(
                        sideToMove, 400f,
                        2.5f, Interpolation.exp10Out
                    ), Actions.removeActor()
                )
            )
            stage!!.addActor(lblCombo)
        }
    }

    override fun draw(delta: Float) {
        renderer.render()

        oCam.update()
        batcher!!.projectionMatrix = oCam.combined

        batcher!!.begin()

        when (state) {
            STATE_RUNNING -> drawRunning()
            STATE_READY, STATE_TRY_AGAIN -> drawReady()
            else -> {}
        }
        batcher!!.end()
    }

    private fun drawRunning() {
        drawNumGrandeCentradoX(SCREEN_WIDTH / 2f, 700f, worldGame.scoreSlammed)

        batcher!!.draw(Assets.coin, 449f, 764f, 30f, 34f)
        drawPuntuacionChicoOrigenDerecha(445f, 764f, worldGame.takenCoins)
    }

    private fun drawReady() {
        drawNumChicoCentradoX(SCREEN_WIDTH / 2f, 730f, Settings.bestScore)
    }

    private fun setPaused() {
        if (state == STATE_RUNNING) {
            state = STATE_PAUSED
            windowPause.show(stage!!)
        }
    }

    fun setRunningFromPaused() {
        if (state == STATE_PAUSED) {
            state = STATE_RUNNING
        }
    }

    private fun setReady() {
        imageAppTitle = Image(Assets.title)
        imageAppTitle!!.setSize(400f, 290f)
        imageAppTitle!!.setPosition(
            SCREEN_WIDTH / 2f - imageAppTitle!!.width / 2f,
            415f
        )
        state = STATE_READY
        stage!!.addActor(groupButtons)
        stage!!.addActor(imageAppTitle)
    }

    private fun setRunning() {
        groupTryAgain.addAction(
            Actions.sequence(
                Actions.fadeOut(.5f),
                Actions.removeActor()
            )
        )
        imageAppTitle!!.addAction(
            Actions.sequence(
                Actions.fadeOut(.5f),
                Actions.removeActor()
            )
        )
        groupButtons!!.addAction(
            Actions.sequence(
                Actions.fadeOut(.5f),
                Actions.run {
                    groupButtons!!.remove()
                    groupTryAgain.remove() // POr el bug
                    state = STATE_RUNNING
                })
        )
    }

    private fun setGameover() {
        setBestScores(worldGame.scoreSlammed)
        state = STATE_GAME_OVER
        stage!!.addActor(imageGameOver)
    }

    private fun setTryAgain() {
        state = STATE_TRY_AGAIN
        setUpGameover()

        groupTryAgain = Group()
        groupTryAgain.setSize(420f, 300f)
        groupTryAgain.setPosition(
            SCREEN_WIDTH / 2f - (groupTryAgain.width
                    / 2), 800f
        )
        groupTryAgain.addAction(
            Actions.sequence(
                Actions.moveTo(
                    groupTryAgain.x, 410f, 1f, Interpolation.bounceOut
                ), Actions
                    .run {
                        groupButtons!!.addAction(Actions.fadeIn(.5f))
                        stage!!.addActor(groupButtons)
                        if (Settings.numberOfTimesPlayed % 7 == 0
                            && !Settings.isQualified
                        ) {
                            windowRate.show(stage!!)
                        }
                    })
        )

        val background = Image(Assets.buttonScores)
        background.setSize(groupTryAgain.width, groupTryAgain.height)
        groupTryAgain.addActor(background)

        /*
         * Aqui voy a agregar lo de mas del try agai
         */
        val score = Image(Assets.score)
        score.setSize(225f, 70f)
        score.setPosition(420 / 2f - score.width / 2f, 200f)

        val coinsEarned = Image(Assets.coinsEarned)
        coinsEarned.setSize(243f, 25f)
        coinsEarned.setPosition(25f, 47f)

        val labelScore = LabelScore(420 / 2f, 120f, worldGame.scoreSlammed)
        val labelCoins = LabelCoins(385f, 45f, worldGame.takenCoins)

        unlockCoins()

        groupTryAgain.addActor(score)
        groupTryAgain.addActor(labelScore)
        groupTryAgain.addActor(labelCoins)
        groupTryAgain.addActor(coinsEarned)

        worldGame = WorldGame()
        renderer = WorldGameRender(batcher!!, worldGame)

        stage!!.addActor(groupTryAgain)
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
            if (state == STATE_READY) Gdx.app.exit()
            else if (state == STATE_TRY_AGAIN) changeScreenWithFadeOut(
                GameScreen::class.java, game
            )
            setPaused()
            return true
        }
        return false
    }

    companion object {
        const val STATE_READY: Int = 1
        const val STATE_RUNNING: Int = 2
        const val STATE_PAUSED: Int = 3
        const val STATE_GAME_OVER: Int = 4
        const val STATE_TRY_AGAIN: Int = 5

        var state: Int = 0
    }
}
