package com.nopalsoft.invaders.screens

import com.badlogic.gdx.Application.ApplicationType
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.nopalsoft.invaders.Assets.btFire
import com.nopalsoft.invaders.Assets.btFireDown
import com.nopalsoft.invaders.Assets.btLeft
import com.nopalsoft.invaders.Assets.btMissil
import com.nopalsoft.invaders.Assets.btMissilDown
import com.nopalsoft.invaders.Assets.btRight
import com.nopalsoft.invaders.Assets.clickHelp
import com.nopalsoft.invaders.Assets.clickSound
import com.nopalsoft.invaders.Assets.font15
import com.nopalsoft.invaders.Assets.font45
import com.nopalsoft.invaders.Assets.getTextWidth
import com.nopalsoft.invaders.Assets.languages
import com.nopalsoft.invaders.Assets.parallaxBackground
import com.nopalsoft.invaders.Assets.playSound
import com.nopalsoft.invaders.Assets.ship
import com.nopalsoft.invaders.Assets.shipLeft
import com.nopalsoft.invaders.Assets.shipRight
import com.nopalsoft.invaders.Assets.styleImageButtonStyleCheckBox
import com.nopalsoft.invaders.Assets.styleLabel
import com.nopalsoft.invaders.Assets.styleSlider
import com.nopalsoft.invaders.Assets.styleTextButtonBack
import com.nopalsoft.invaders.MainInvaders
import com.nopalsoft.invaders.Settings.accelerometerSensitivity
import com.nopalsoft.invaders.Settings.tiltControlEnabled
import com.nopalsoft.invaders.frame.Ship

class SettingsScreen(game: MainInvaders) : Screens(game) {
    val oNave: Ship
    var tiltControl: ImageButton? = null
    var onScreenControl: ImageButton

    // Accelerometer Slider
    var aceletometerSlider: Slider = Slider(1f, 20f, 1f, false, styleSlider)
    var buttonBack: TextButton
    var menuControls: Table
    var buttonLeft: ImageButton
    var buttonRight: ImageButton
    var buttonMissile: ImageButton
    var buttonFire: ImageButton
    var touchLeft: Label
    var touchRight: Label
    var myCameraRenderer: OrthographicCamera
    var accel: Float = 0f

    init {
        aceletometerSlider.setPosition(70f, 295f)
        aceletometerSlider.setValue((21 - accelerometerSensitivity).toFloat())
        aceletometerSlider.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                accelerometerSensitivity = 21 - (actor as Slider).value.toInt()
            }
        })

        menuControls = Table()
        menuControls.setPosition(SCREEN_WIDTH / 2f - 30, 380f) // half minus 30

        onScreenControl = ImageButton(styleImageButtonStyleCheckBox)
        if (!tiltControlEnabled) onScreenControl.isChecked = true
        onScreenControl.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                tiltControlEnabled = false
                onScreenControl.isChecked = true
                tiltControl?.isChecked = false
                setOptions()
            }
        })

        tiltControl = ImageButton(styleImageButtonStyleCheckBox)
        if (tiltControlEnabled) tiltControl?.isChecked = true
        tiltControl!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                tiltControlEnabled = true
                onScreenControl.isChecked = false
                tiltControl?.isChecked = true
                setOptions()
            }
        })

        /* OnScreenControls */
        buttonLeft = ImageButton(btLeft)
        buttonLeft.setSize(65f, 50f)
        buttonLeft.setPosition(10f, 5f)
        buttonLeft.addListener(object : ClickListener() {
            override fun enter(event: InputEvent, x: Float, y: Float, pointer: Int, fromActor: Actor) {
                accel = 5f
            }

            override fun exit(event: InputEvent, x: Float, y: Float, pointer: Int, toActor: Actor) {
                accel = 0f
                super.exit(event, x, y, pointer, toActor)
            }
        })
        buttonRight = ImageButton(btRight)
        buttonRight.setSize(65f, 50f)
        buttonRight.setPosition(85f, 5f)
        buttonRight.addListener(object : ClickListener() {
            override fun enter(event: InputEvent, x: Float, y: Float, pointer: Int, fromActor: Actor) {
                accel = -5f
            }

            override fun exit(event: InputEvent, x: Float, y: Float, pointer: Int, toActor: Actor) {
                accel = 0f
                super.exit(event, x, y, pointer, toActor)
            }
        })

        buttonMissile = ImageButton(btMissil, btMissilDown)
        buttonMissile.setSize(60f, 60f)
        buttonMissile.setPosition((SCREEN_WIDTH - 5 - 60 - 20 - 60).toFloat(), 5f)
        buttonFire = ImageButton(btFire, btFireDown)
        buttonFire.setSize(60f, 60f)
        buttonFire.setPosition((SCREEN_WIDTH - 60 - 5).toFloat(), 5f)

        menuControls.add(Label(languages!!["on_screen_control"], styleLabel)).left()
        menuControls.add(onScreenControl).size(25f)
        menuControls.row().padTop(10f)
        menuControls.add(Label(languages!!["tilt_control"], styleLabel)).left()
        menuControls.add(tiltControl).size(25f)

        buttonBack = TextButton(languages!!["back"], styleTextButtonBack)
        buttonBack.pad(0f, 15f, 35f, 0f)
        buttonBack.setSize(63f, 63f)
        buttonBack.setPosition((SCREEN_WIDTH - 63).toFloat(), (SCREEN_HEIGHT - 63).toFloat())
        buttonBack.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                playSound(clickSound!!)
                game.screen = MainMenuScreen(game)
            }
        })

        touchLeft = Label(languages!!["touch_left_side_to_fire_missils"], styleLabel)
        touchLeft.wrap = true
        touchLeft.width = 160f
        touchLeft.setAlignment(Align.center)
        touchLeft.setPosition(0f, 50f)

        touchRight = Label(languages!!["touch_right_side_to_fire"], styleLabel)
        touchRight.wrap = true
        touchRight.width = 160f
        touchRight.setAlignment(Align.center)
        touchRight.setPosition(165f, 50f)

        setOptions()

        // Voy a poner a la nave aqui que se mueva tambien;
        oNave = Ship(WORLD_SCREEN_WIDTH / 2.0f, WORLD_SCREEN_HEIGHT / 3.0f) // Coloco la nave en posicion
        this.myCameraRenderer = OrthographicCamera(WORLD_SCREEN_WIDTH.toFloat(), WORLD_SCREEN_HEIGHT.toFloat())
        myCameraRenderer.position[WORLD_SCREEN_WIDTH / 2.0f, WORLD_SCREEN_HEIGHT / 2.0f] = 0f

        // menuControls.debug();
    }

    protected fun setOptions() {
        stage!!.clear()
        accel = 0f // porque a veces se quedaba moviendo la nave cuando se pasaba de tilt a control
        stage.addActor(buttonBack)
        stage.addActor(menuControls)
        stage.addActor(aceletometerSlider)
        if (tiltControlEnabled) setTiltControls()
        else setOnScreenControl()
    }

    private fun setTiltControls() {
        stage!!.addActor(touchLeft)
        stage.addActor(touchRight)
    }

    protected fun setOnScreenControl() {
        stage!!.addActor(buttonLeft)
        stage.addActor(buttonRight)
        stage.addActor(buttonMissile)
        stage.addActor(buttonFire)
    }

    override fun update(delta: Float) {
        if (tiltControlEnabled) {
            accel = Gdx.input.accelerometerX
        } else {
            if (Gdx.app.type == ApplicationType.Applet || Gdx.app.type == ApplicationType.Desktop || Gdx.app.type == ApplicationType.WebGL) {
                accel = 0f
                if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) accel = 5f
                if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) accel = -5f
            }
        }
        oNave.velocity.x = -accel / accelerometerSensitivity * Ship.NAVE_MOVE_SPEED

        oNave.update(delta)
    }

    override fun draw(delta: Float) {
        myCamera.update()
        batcher!!.projectionMatrix = myCamera.combined

        batcher.disableBlending()
        parallaxBackground!!.render(delta)

        stage!!.act(delta)
        stage.draw()

        batcher.enableBlending()
        batcher.begin()
        font45!!.draw(batcher, languages!!["control_options"], 10f, 460f)

        if (tiltControlEnabled) {
            val tiltSensitive = languages!!["tilt_sensitive"]
            val textWidth = getTextWidth(font15, tiltSensitive)
            font15!!.draw(batcher, tiltSensitive, SCREEN_WIDTH / 2f - textWidth / 2f, 335f)
            batcher.draw(clickHelp, 155f, 0f, 10f, 125f)
        } else {
            val speed = languages!!["speed"]
            val textWidth = getTextWidth(font15, speed)
            font15!!.draw(batcher, speed, SCREEN_WIDTH / 2f - textWidth / 2f, 335f)
        }
        font15!!.draw(batcher, aceletometerSlider.value.toInt().toString() + "", 215f, 313f)
        batcher.end()

        myCameraRenderer.update()
        batcher.projectionMatrix = myCameraRenderer.combined
        batcher.begin()
        renderNave()
        batcher.end()
    }

    private fun renderNave() {
        val keyFrame: TextureRegion? = if (oNave.velocity.x < -3) shipLeft
        else if (oNave.velocity.x > 3) shipRight
        else ship

        batcher!!.draw(
            keyFrame,
            oNave.position.x - Ship.DRAW_WIDTH / 2f,
            oNave.position.y - Ship.DRAW_HEIGHT / 2f,
            Ship.DRAW_WIDTH,
            Ship.DRAW_HEIGHT
        )
    }

    override fun keyDown(tecleada: Int): Boolean {
        if (tecleada == Input.Keys.BACK || tecleada == Input.Keys.ESCAPE) {
            playSound(clickSound!!)
            game.screen = MainMenuScreen(game)
            return true
        }
        return false
    }
}
