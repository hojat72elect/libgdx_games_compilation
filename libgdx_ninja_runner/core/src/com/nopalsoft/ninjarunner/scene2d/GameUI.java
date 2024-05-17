package com.nopalsoft.ninjarunner.scene2d;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.ninjarunner.Assets;
import com.nopalsoft.ninjarunner.game.GameScreen;
import com.nopalsoft.ninjarunner.game.WorldGame;
import com.nopalsoft.ninjarunner.screens.Screens;

public class GameUI extends Group {
    public static final float ANIMATION_TIME = .35f;
    public boolean didJump, didSlide, didDash;
    GameScreen gameScreen;
    WorldGame oWorld;
    Table tbHeader;
    Label lbPuntuacion;
    Button btJump, btSlide;

    public GameUI(final GameScreen gameScreen, WorldGame oWorld) {
        setBounds(0, 0, Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT);
        this.gameScreen = gameScreen;
        this.oWorld = oWorld;

        init();

    }

    private void init() {

        btJump = new Button(new ButtonStyle(null, null, null));
        btJump.setSize(getWidth() / 2f, getHeight());
        btJump.setPosition(0, 0);
        btJump.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                didJump = true;
                return false;

            }
        });

        btSlide = new Button(new ButtonStyle(null, null, null));
        btSlide.setSize(getWidth() / 2f, getHeight());
        btSlide.setPosition(getWidth() / 2f + 1, 0);
        btSlide.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                didSlide = true;
                return true;

            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                didSlide = false;
            }
        });

        tbHeader = new Table();
        tbHeader.setSize(Screens.SCREEN_WIDTH, 50);
        tbHeader.setPosition(0, Screens.SCREEN_HEIGHT - tbHeader.getHeight());

        lbPuntuacion = new Label("0", Assets.labelStyleSmall);
        tbHeader.add(lbPuntuacion).fill();

        addActor(tbHeader);

        addActor(btJump);
        addActor(btSlide);


    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    private void addInActions() {
    }


    public void show(Stage stage) {
        addInActions();
        stage.addActor(this);
    }

}
