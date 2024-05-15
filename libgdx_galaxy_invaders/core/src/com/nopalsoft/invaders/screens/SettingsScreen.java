package com.nopalsoft.invaders.screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.invaders.Assets;
import com.nopalsoft.invaders.MainInvaders;

public class SettingsScreen extends Screens {

    public final com.nopalsoft.invaders.frame.Ship oNave;
    ImageButton tiltControl;
    ImageButton onScreenControl;
    Slider aceletometerSlider;
    TextButton buttonBack;
    Table menuControls;
    ImageButton buttonLeft, buttonRight, buttonMissile, buttonFire;
    Label touchLeft, touchRight;
    OrthographicCamera myCameraRenderer;
    float accel;

    public SettingsScreen(final MainInvaders game) {
        super(game);

        // Accelerometer Slider
        aceletometerSlider = new Slider(1, 20, 1f, false, Assets.styleSlider);
        aceletometerSlider.setPosition(70, 295);
        aceletometerSlider.setValue(21 - com.nopalsoft.invaders.Settings.getAccelerometerSensitivity());
        aceletometerSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                com.nopalsoft.invaders.Settings.setAccelerometerSensitivity(21 - (int) ((Slider) actor).getValue());

            }
        });

        menuControls = new Table();
        menuControls.setPosition(SCREEN_WIDTH / 2f - 30, 380);// half minus 30

        onScreenControl = new ImageButton(Assets.styleImageButtonStyleCheckBox);
        if (!com.nopalsoft.invaders.Settings.getTiltControlEnabled())
            onScreenControl.setChecked(true);
        onScreenControl.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                com.nopalsoft.invaders.Settings.setTiltControlEnabled(false);
                onScreenControl.setChecked(true);
                tiltControl.setChecked(false);
                setOptions();

            }
        });

        tiltControl = new ImageButton(Assets.styleImageButtonStyleCheckBox);
        if (com.nopalsoft.invaders.Settings.getTiltControlEnabled())
            tiltControl.setChecked(true);
        tiltControl.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                com.nopalsoft.invaders.Settings.setTiltControlEnabled(true);
                onScreenControl.setChecked(false);
                tiltControl.setChecked(true);
                setOptions();
            }
        });

        /* OnScreenControls */

        buttonLeft = new ImageButton(Assets.btLeft);
        buttonLeft.setSize(65, 50);
        buttonLeft.setPosition(10, 5);
        buttonLeft.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                accel = 5;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                accel = 0;
                super.exit(event, x, y, pointer, toActor);
            }

        });
        buttonRight = new ImageButton(Assets.btRight);
        buttonRight.setSize(65, 50);
        buttonRight.setPosition(85, 5);
        buttonRight.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                accel = -5;

            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                accel = 0;
                super.exit(event, x, y, pointer, toActor);
            }

        });

        buttonMissile = new ImageButton(Assets.btMissil, Assets.btMissilDown);
        buttonMissile.setSize(60, 60);
        buttonMissile.setPosition(SCREEN_WIDTH - 5 - 60 - 20 - 60, 5);
        buttonFire = new ImageButton(Assets.btFire, Assets.btFireDown);
        buttonFire.setSize(60, 60);
        buttonFire.setPosition(SCREEN_WIDTH - 60 - 5, 5);

        menuControls.add(new Label(Assets.languages.get("on_screen_control"), Assets.styleLabel)).left();
        menuControls.add(onScreenControl).size(25);
        menuControls.row().padTop(10);
        menuControls.add(new Label(Assets.languages.get("tilt_control"), Assets.styleLabel)).left();
        menuControls.add(tiltControl).size(25);

        buttonBack = new TextButton(Assets.languages.get("back"), Assets.styleTextButtonBack);
        buttonBack.pad(0, 15, 35, 0);
        buttonBack.setSize(63, 63);
        buttonBack.setPosition(SCREEN_WIDTH - 63, SCREEN_HEIGHT - 63);
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
                game.setScreen(new MainMenuScreen(game));
            }
        });

        touchLeft = new Label(Assets.languages.get("touch_left_side_to_fire_missils"), Assets.styleLabel);
        touchLeft.setWrap(true);
        touchLeft.setWidth(160);
        touchLeft.setAlignment(Align.center);
        touchLeft.setPosition(0, 50);

        touchRight = new Label(Assets.languages.get("touch_right_side_to_fire"), Assets.styleLabel);
        touchRight.setWrap(true);
        touchRight.setWidth(160);
        touchRight.setAlignment(Align.center);
        touchRight.setPosition(165, 50);

        setOptions();

        // Voy a poner a la nave aqui que se mueva tambien;
        oNave = new com.nopalsoft.invaders.frame.Ship(WORLD_SCREEN_WIDTH / 2.0f, WORLD_SCREEN_HEIGHT / 3.0f); // Coloco la nave en posicion
        this.myCameraRenderer = new OrthographicCamera(WORLD_SCREEN_WIDTH, WORLD_SCREEN_HEIGHT);
        myCameraRenderer.position.set(WORLD_SCREEN_WIDTH / 2.0f, WORLD_SCREEN_HEIGHT / 2.0f, 0);
        // menuControls.debug();

    }

    protected void setOptions() {
        stage.clear();
        accel = 0;// porque a veces se quedaba moviendo la nave cuando se pasaba de tilt a control
        stage.addActor(buttonBack);
        stage.addActor(menuControls);
        stage.addActor(aceletometerSlider);
        if (com.nopalsoft.invaders.Settings.getTiltControlEnabled())
            setTiltControls();
        else
            setOnScreenControl();

    }

    private void setTiltControls() {
        stage.addActor(touchLeft);
        stage.addActor(touchRight);
    }

    protected void setOnScreenControl() {
        stage.addActor(buttonLeft);
        stage.addActor(buttonRight);
        stage.addActor(buttonMissile);
        stage.addActor(buttonFire);
    }

    @Override
    public void update(float delta) {

        if (com.nopalsoft.invaders.Settings.getTiltControlEnabled()) {
            accel = Gdx.input.getAccelerometerX();

        } else {
            if (Gdx.app.getType() == ApplicationType.Applet || Gdx.app.getType() == ApplicationType.Desktop || Gdx.app.getType() == ApplicationType.WebGL) {
                accel = 0;
                if (Gdx.input.isKeyPressed(Keys.DPAD_LEFT) || Gdx.input.isKeyPressed(Keys.A))
                    accel = 5f;
                if (Gdx.input.isKeyPressed(Keys.DPAD_RIGHT) || Gdx.input.isKeyPressed(Keys.D))
                    accel = -5f;
            }
        }
        oNave.velocity.x = -accel / com.nopalsoft.invaders.Settings.getAccelerometerSensitivity() * com.nopalsoft.invaders.frame.Ship.NAVE_MOVE_SPEED;

        oNave.update(delta);
    }

    @Override
    public void draw(float delta) {

        myCamera.update();
        batcher.setProjectionMatrix(myCamera.combined);

        batcher.disableBlending();
        Assets.parallaxBackground.render(delta);

        stage.act(delta);
        stage.draw();

        batcher.enableBlending();
        batcher.begin();
        Assets.font45.draw(batcher, Assets.languages.get("control_options"), 10, 460);

        if (com.nopalsoft.invaders.Settings.getTiltControlEnabled()) {
            String tiltSensitive = Assets.languages.get("tilt_sensitive");
            float textWidth = Assets.getTextWidth(Assets.font15, tiltSensitive);
            Assets.font15.draw(batcher, tiltSensitive, SCREEN_WIDTH / 2f - textWidth / 2f, 335);
            batcher.draw(Assets.clickHelp, 155, 0, 10, 125);
        } else {
            String speed = Assets.languages.get("speed");
            float textWidth = Assets.getTextWidth(Assets.font15, speed);
            Assets.font15.draw(batcher, speed, SCREEN_WIDTH / 2f - textWidth / 2f, 335);

        }
        Assets.font15.draw(batcher, (int) aceletometerSlider.getValue() + "", 215, 313);
        batcher.end();

        myCameraRenderer.update();
        batcher.setProjectionMatrix(myCameraRenderer.combined);
        batcher.begin();
        renderNave();
        batcher.end();

    }

    private void renderNave() {
        TextureRegion keyFrame;
        if (oNave.velocity.x < -3)
            keyFrame = Assets.shipLeft;
        else if (oNave.velocity.x > 3)
            keyFrame = Assets.shipRight;
        else
            keyFrame = Assets.ship;

        batcher.draw(keyFrame, oNave.position.x - com.nopalsoft.invaders.frame.Ship.DRAW_WIDTH / 2f, oNave.position.y - com.nopalsoft.invaders.frame.Ship.DRAW_HEIGHT / 2f, com.nopalsoft.invaders.frame.Ship.DRAW_WIDTH, com.nopalsoft.invaders.frame.Ship.DRAW_HEIGHT);
    }

    @Override
    public boolean keyDown(int tecleada) {
        if (tecleada == Keys.BACK || tecleada == Keys.ESCAPE) {
            Assets.playSound(Assets.clickSound);
            game.setScreen(new MainMenuScreen(game));
            return true;
        }
        return false;
    }
}
