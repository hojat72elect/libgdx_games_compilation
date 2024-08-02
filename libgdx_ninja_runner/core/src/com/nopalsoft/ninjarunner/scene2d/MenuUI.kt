package com.nopalsoft.ninjarunner.scene2d;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.ninjarunner.Assets;
import com.nopalsoft.ninjarunner.game.GameScreen;
import com.nopalsoft.ninjarunner.game.WorldGame;
import com.nopalsoft.ninjarunner.leaderboard.LeaderboardScreen;
import com.nopalsoft.ninjarunner.screens.Screens;
import com.nopalsoft.ninjarunner.screens.SettingsScreen;
import com.nopalsoft.ninjarunner.shop.ShopScreen;


public class MenuUI extends Group {
    public static final float ANIMATION_TIME = .35f;

    GameScreen gameScreen;
    WorldGame myWorld;
    Image title;

    Table tableMenu;

    Button buttonPlay;
    Button buttonShop, buttonLeaderboard, buttonAchievements, buttonSettings, buttonRate, buttonShare;

    boolean showMainMenu;

    public MenuUI(final GameScreen gameScreen, WorldGame myWorld) {
        setBounds(0, 0, Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT);
        this.gameScreen = gameScreen;
        this.myWorld = myWorld;

        initialize();

    }

    private void initialize() {
        title = new Image(Assets.title);
        title.setScale(1f);
        title.setPosition(getWidth() / 2f - title.getWidth() * title.getScaleX() / 2f, Screens.SCREEN_HEIGHT + title.getHeight());

        tableMenu = new Table();
        tableMenu.setSize(122, getHeight());
        tableMenu.setBackground(Assets.backgroundMenu);

        initButtons();

        tableMenu.pad(25, 20, 10, 0);
        tableMenu.defaults().size(80).padBottom(15);


        tableMenu.row().colspan(2);
        tableMenu.add(buttonShop);

        tableMenu.row().colspan(2);
        tableMenu.add(buttonLeaderboard);

        tableMenu.row().colspan(2);
        tableMenu.add(buttonAchievements);

        tableMenu.row().colspan(2);
        tableMenu.add(buttonSettings);

        tableMenu.row().size(40).padRight(5).padLeft(5);
        tableMenu.add(buttonRate);
        tableMenu.add(buttonShare);

        tableMenu.setPosition(Screens.SCREEN_WIDTH + tableMenu.getWidth(), 0);

        addActor(tableMenu);
        addActor(buttonPlay);

    }

    void initButtons() {

        buttonShop = new Button(Assets.buttonShop, Assets.buttonShopPressed);
        buttonLeaderboard = new Button(Assets.buttonLeaderboard, Assets.buttonLeaderboardPressed);
        buttonAchievements = new Button(Assets.buttonAchievement, Assets.buttonAchievementPressed);
        buttonSettings = new Button(Assets.buttonSettings, Assets.buttonSettingsPressed);
        buttonRate = new Button(Assets.buttonRate, Assets.buttonRatePressed);
        buttonShare = new Button(Assets.buttonShare, Assets.buttonSharePressed);

        buttonPlay = new Button(new ButtonStyle(null, null, null));
        buttonPlay.setSize(getWidth() - tableMenu.getWidth(), getHeight());
        buttonPlay.setPosition(0, 0);
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (showMainMenu)
                    gameScreen.setRunning(true);
                else {
                    gameScreen.game.setScreen(new GameScreen(gameScreen.game, false));
                }
            }
        });

        buttonShop.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.changeScreenWithFadeOut(ShopScreen.class, gameScreen.game);
            }
        });

        buttonRate.addListener(new ClickListener() {
        });

        buttonShare.addListener(new ClickListener() {
        });

        buttonLeaderboard.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                gameScreen.game.setScreen(new LeaderboardScreen(gameScreen.game));
            }
        });

        buttonAchievements.addListener(new ClickListener() {
        });

        buttonSettings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.changeScreenWithFadeOut(SettingsScreen.class, gameScreen.game);
            }
        });

    }

    private void addInActions() {
        title.addAction(Actions.moveTo(getWidth() / 2f - title.getWidth() * title.getScaleX() / 2f, 300, ANIMATION_TIME));
        tableMenu.addAction(Actions.moveTo(Screens.SCREEN_WIDTH - tableMenu.getWidth(), 0, ANIMATION_TIME));

    }

    private void addOutActions() {
        title.addAction(Actions.moveTo(getWidth() / 2f - title.getWidth() * title.getScaleX() / 2f, Screens.SCREEN_HEIGHT + title.getHeight(),
                ANIMATION_TIME));

        tableMenu.addAction(Actions.moveTo(Screens.SCREEN_WIDTH + tableMenu.getWidth(), 0, ANIMATION_TIME));

    }

    public void show(Stage stage, final boolean showMainMenu) {
        addInActions();
        stage.addActor(this);

        title.remove();
        addActor(title);
        this.showMainMenu = showMainMenu;

    }

    public void removeWithAnimations() {
        addOutActions();
        addAction(Actions.sequence(Actions.delay(ANIMATION_TIME), Actions.removeActor()));
    }

}
