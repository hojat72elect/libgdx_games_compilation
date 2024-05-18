package com.nopalsoft.sokoban.game;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.sokoban.Assets;
import com.nopalsoft.sokoban.MainSokoban;
import com.nopalsoft.sokoban.Settings;
import com.nopalsoft.sokoban.scene2d.ControlsNoPad;
import com.nopalsoft.sokoban.scene2d.CounterBar;
import com.nopalsoft.sokoban.scene2d.WindowPause;
import com.nopalsoft.sokoban.screens.Screens;

public class GameScreen extends Screens {
    static final int STATE_RUNNING = 0;
    static final int STATE_PAUSED = 1;
    static final int STATE_GAME_OVER = 2;
    private final Stage stageGame;
    public int state;
    public int level;
    BoardRenderer renderer;
    Board myBoard;
    ControlsNoPad myControl;
    Button buttonUndo;
    Button buttonPause;
    CounterBar barTime;
    CounterBar barMoves;
    WindowPause myWindowPause;

    public GameScreen(final MainSokoban game, int level) {
        super(game);
        this.level = level;

        stageGame = new Stage(new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT));
        myBoard = new Board();

        renderer = new BoardRenderer();

        myControl = new ControlsNoPad(this);

        barTime = new CounterBar(Assets.backgroundTime, 5, 430);
        barMoves = new CounterBar(Assets.backgroundMoves, 5, 380);

        myWindowPause = new WindowPause(this);

        Label lbNivel = new Label("Level " + (level + 1), new LabelStyle(Assets.fontRed, Color.WHITE));
        lbNivel.setWidth(barTime.getWidth());
        lbNivel.setPosition(5, 330);
        lbNivel.setAlignment(Align.center);

        buttonUndo = new Button(Assets.buttonRefresh, Assets.buttonRefreshPressed);
        buttonUndo.setSize(80, 80);
        buttonUndo.setPosition(700, 20);
        buttonUndo.getColor().a = myControl.getColor().a;// Have the same alpha color
        buttonUndo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                myBoard.undo = true;
            }
        });

        buttonPause = new Button(Assets.buttonPause, Assets.buttonPausePressed);
        buttonPause.setSize(60, 60);
        buttonPause.setPosition(730, 410);

        buttonPause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPause();
            }

        });

        stageGame.addActor(myBoard);
        stageGame.addActor(barTime);
        stageGame.addActor(barMoves);
        stage.addActor(lbNivel);
        stage.addActor(myControl);
        stage.addActor(buttonUndo);
        stage.addActor(buttonPause);

        setRunning();
    }

    @Override
    public void draw(float delta) {
        Assets.background.render(delta);

        // Render el tileMap
        renderer.render();

        // Render el tablero
        stageGame.draw();

    }

    @Override
    public void update(float delta) {

        if (state != STATE_PAUSED) {
            stageGame.act(delta);
            barMoves.updateActualNum(myBoard.moves);
            barTime.updateActualNum((int) myBoard.time);

            if (state == STATE_RUNNING && myBoard.state == Board.STATE_GAMEOVER) {
                setGameover();
            }
        }

    }

    private void setGameover() {
        state = STATE_GAME_OVER;
        Settings.levelCompeted(level, myBoard.moves, (int) myBoard.time);
        stage.addAction(Actions.sequence(Actions.delay(.35f), Actions.run(() -> {
            level += 1;
            if (level >= Settings.getNUM_MAPS())
                changeScreenWithFadeOut(com.nopalsoft.sokoban.screens.MainMenuScreen.class, game);
            else
                changeScreenWithFadeOut(GameScreen.class, level, game);

        })));
    }

    public void setRunning() {
        if (state != STATE_GAME_OVER) {
            state = STATE_RUNNING;
        }
    }

    private void setPause() {
        if (state == STATE_RUNNING) {
            state = STATE_PAUSED;
            myWindowPause.show(stage);
        }
    }

    @Override
    public void up() {
        myBoard.moveUp = true;
        super.up();
    }

    @Override
    public void down() {
        myBoard.moveDown = true;
        super.down();
    }

    @Override
    public void right() {
        myBoard.moveRight = true;
        super.right();
    }

    @Override
    public void left() {
        myBoard.moveLeft = true;
        super.left();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (state == STATE_RUNNING) {
            if (keycode == Keys.LEFT || keycode == Keys.A) {
                myBoard.moveLeft = true;

            } else if (keycode == Keys.RIGHT || keycode == Keys.D) {
                myBoard.moveRight = true;

            } else if (keycode == Keys.UP || keycode == Keys.W) {
                myBoard.moveUp = true;

            } else if (keycode == Keys.DOWN || keycode == Keys.S) {
                myBoard.moveDown = true;

            } else if (keycode == Keys.Z) {
                myBoard.undo = true;

            } else if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
                setPause();
            }
        } else if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
            if (myWindowPause.isShown())
                myWindowPause.hide();
        }

        return true;
    }

}
