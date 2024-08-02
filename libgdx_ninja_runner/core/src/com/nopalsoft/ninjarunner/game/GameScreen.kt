package com.nopalsoft.ninjarunner.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.nopalsoft.ninjarunner.Assets;
import com.nopalsoft.ninjarunner.Settings;
import com.nopalsoft.ninjarunner.leaderboard.NextGoalFrame;
import com.nopalsoft.ninjarunner.leaderboard.Person;
import com.nopalsoft.ninjarunner.scene2d.GameUI;
import com.nopalsoft.ninjarunner.scene2d.MenuUI;
import com.nopalsoft.ninjarunner.screens.Screens;


public class GameScreen extends Screens {
    static final int STATE_MENU = 0;
    static final int STATE_RUNNING = 1;
    static final int STATE_GAME_OVER = 2;
    public WorldGame myWorld;
    int state;
    GameUI gameUI;
    MenuUI menuUI;
    WorldGameRenderer renderer;

    NextGoalFrame nextGoalFrame;

    public GameScreen(Game _game, boolean showMainMenu) {
        super(_game);
        myWorld = new WorldGame();
        renderer = new WorldGameRenderer(batcher, myWorld);
        gameUI = new GameUI();
        menuUI = new MenuUI(this, myWorld);

        if (showMainMenu) {
            state = STATE_MENU;
            menuUI.show(stage, true);
        } else {
            setRunning(false);
        }

    }

    public void setRunning(boolean removeMenu) {
        Runnable runAfterHideMenu = () -> {
            Runnable run = () -> {
                state = STATE_RUNNING;
                if (Settings.isMusicEnabled()) {
                    Assets.music1.play();
                }

                nextGoalFrame = new NextGoalFrame(SCREEN_WIDTH, 400);
                setNextGoalFrame(0);


            };
            gameUI.addAction(Actions.sequence(Actions.delay(GameUI.ANIMATION_TIME), Actions.run(run)));
            gameUI.show(stage);
        };

        if (removeMenu) {
            menuUI.addAction(Actions.sequence(Actions.delay(MenuUI.ANIMATION_TIME), Actions.run(runAfterHideMenu)));
            menuUI.removeWithAnimations();
        } else {
            stage.addAction(Actions.run(runAfterHideMenu));
        }

    }

    @Override
    public void update(float delta) {

        if (state == STATE_MENU) {
            myWorld.myPlayer.updateStateTime(delta);
            myWorld.myPet.updateStateTime(delta);

        } else if (state == STATE_RUNNING) {

            myWorld.update(delta, gameUI.didJump, gameUI.didDash, gameUI.didSlide);

            gameUI.didJump = false;
            gameUI.didDash = false;

            if (myWorld.state == WorldGame.STATE_GAMEOVER) {
                setGameover();
            }

            setNextGoalFrame(myWorld.score);
            nextGoalFrame.updateScore(myWorld.score);

        } else if (state == STATE_GAME_OVER) {
            if (Gdx.input.justTouched()) {
                game.setScreen(new GameScreen(game, true));
            }
        }


    }


    public void setNextGoalFrame(long puntos) {
        //Para que solo se muestren las personas que no haya superado aun
        if (puntos < Settings.getBestScore())
            puntos = Settings.getBestScore();

        game.arrayOfPersons.sort(); // Acomoda de mayor puntuacion a menor puntuacion


        Person oPersonAux = null;
        //Calculo la posicion del jugador que tenga mas puntos que yo. Por ejemplo si yo voy en quinto lugar
        //esta debe ser la pocion del cuarto lugar.
        int posicionArribaDeMi = game.arrayOfPersons.size - 1;
        //El arreglo esta ordenado de mayor a menor
        for (; posicionArribaDeMi >= 0; posicionArribaDeMi--) {
            Person obj = game.arrayOfPersons.get(posicionArribaDeMi);
            if (obj.isMe)
                continue;

            if (obj.score > puntos) {
                oPersonAux = obj;
                break;
            }
        }

        final Person oPersona = oPersonAux;

        if (oPersona == null)
            return;

        if (oPersona.equals(nextGoalFrame.myPerson))
            return;


        Runnable run = () -> {
            nextGoalFrame.updatePerson(oPersona);
            nextGoalFrame.addAction(Actions.sequence(Actions.moveTo(SCREEN_WIDTH - NextGoalFrame.WIDTH, nextGoalFrame.getY(), 1)));

        };

        if (!nextGoalFrame.hasParent()) {
            stage.addActor(nextGoalFrame);
            Gdx.app.postRunnable(run);
        } else if (!nextGoalFrame.hasActions()) {
            nextGoalFrame.addAction(Actions.sequence(Actions.moveTo(SCREEN_WIDTH, nextGoalFrame.getY(), 1), Actions.run(run)));
        }
    }

    private void setGameover() {
        Settings.setNewScore(myWorld.score);
        state = STATE_GAME_OVER;
        Assets.music1.stop();

    }

    @Override
    public void right() {
        super.right();
        gameUI.didDash = true;
    }

    @Override
    public void draw(float delta) {

        if (state == STATE_MENU) {
            Assets.backgroundNubes.render(0);
        } else {
            Assets.backgroundNubes.render(delta);
        }

        renderer.render(delta);

        myCamera.update();
        batcher.setProjectionMatrix(myCamera.combined);

        batcher.begin();
        Assets.fontSmall.draw(batcher, "FPS GERA" + Gdx.graphics.getFramesPerSecond(), 5, 20);
        Assets.fontSmall.draw(batcher, "Bodies " + myWorld.myWorldBox.getBodyCount(), 5, 40);
        Assets.fontSmall.draw(batcher, "Vidas " + myWorld.myPlayer.lives, 5, 60);
        Assets.fontSmall.draw(batcher, "Monedas " + myWorld.takenCoins, 5, 80);
        Assets.fontSmall.draw(batcher, "Puntos " + myWorld.score, 5, 100);
        Assets.fontSmall.draw(batcher, "Distancia " + myWorld.myPlayer.position.x, 5, 120);
        Assets.fontSmall.draw(batcher, "Plataformas " + myWorld.arrayPlatforms.size, 5, 140);

        batcher.end();

    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.R) {
            game.setScreen(new GameScreen(game, true));
            return true;
        } else if (keycode == Keys.SPACE || keycode == Keys.W || keycode == Keys.UP) {
            gameUI.didJump = true;
            return true;
        } else if (keycode == Keys.S || keycode == Keys.DOWN) {
            gameUI.didSlide = true;
            return true;
        } else if (keycode == Keys.BACK) {
            Gdx.app.exit();
            return true;
        } else if (keycode == Keys.P) {
            if (game.arrayOfPersons != null) {
                setNextGoalFrame(0);
            }
            return true;
        }
        return super.keyDown(keycode);

    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Keys.S || keycode == Keys.DOWN) {
            gameUI.didSlide = false;
            return true;
        }
        return super.keyUp(keycode);
    }
}
