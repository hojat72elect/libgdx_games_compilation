package com.nopalsoft.ninjarunner.leaderboard;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.ninjarunner.Assets;
import com.nopalsoft.ninjarunner.game.GameScreen;
import com.nopalsoft.ninjarunner.screens.Screens;

/**
 * Shows all the top scoring users of the game. It doesn't matter which social network they have
 * used for logging in to the game; here they are shown if they are leader board users of the game.
 */
public class LeaderboardScreen extends Screens {

    Table tableMenu;
    Button buttonLeaderboard, buttonFacebook, buttonInviteFriend;
    Button buttonGoogle;

    ScrollPane scroll;
    Table table;


    public LeaderboardScreen(Game _game) {
        super(_game);

        Label labelShop = new Label("Leaderboards", Assets.labelStyleLarge);

        Table tableTitle = new Table();
        tableTitle.setSize(400, 100);
        tableTitle.setPosition(SCREEN_WIDTH / 2f - tableTitle.getWidth() / 2f, SCREEN_HEIGHT - tableTitle.getHeight());
        tableTitle.setBackground(Assets.backgroundTitleShop);
        tableTitle.padTop(20).padBottom(5);

        tableTitle.row().colspan(2);
        tableTitle.add(labelShop).expand();
        tableTitle.row();

        Image imgGem = new Image(Assets.coinAnimation.getKeyFrame(0));
        imgGem.setSize(20, 20);


        initButtons();

        tableMenu = new Table();
        tableMenu.defaults().size(58).padBottom(8);

        tableMenu.row();
        tableMenu.add(buttonLeaderboard);

        tableMenu.row();
        tableMenu.add(buttonFacebook);

        tableMenu.row();
        tableMenu.add(buttonGoogle);

        tableMenu.row();
        tableMenu.add(buttonInviteFriend);


        Table tbShop = new Table();
        tbShop.setSize(SCREEN_WIDTH, SCREEN_HEIGHT - tableTitle.getHeight());
        tbShop.setBackground(Assets.backgroundShop);
        tbShop.pad(25, 5, 15, 5);

        // Contenedor
        table = new Table();
        table.defaults().expand().fill().padLeft(10).padRight(20).padBottom(10);

        scroll = new ScrollPane(table, new ScrollPaneStyle(null, null, null, null, null));
        scroll.setFadeScrollBars(false);
        scroll.setSize(SCREEN_WIDTH - tableMenu.getWidth(), 420);
        scroll.setPosition(tableMenu.getWidth() + 1, 0);
        scroll.setVariableSizeKnobs(false);

        tbShop.add(tableMenu).expandY().width(122);
        tbShop.add(scroll).expand().fill();

        stage.addActor(tableTitle);
        stage.addActor(tbShop);


        if (game.arrayOfPersons != null)
            updateLeaderboard();


        buttonLeaderboard.setChecked(true);


    }

    void initButtons() {
        buttonLeaderboard = new Button(Assets.buttonShop, Assets.buttonShopPressed, Assets.buttonShopPressed);
        buttonFacebook = new Button(Assets.buttonFacebook, Assets.buttonFacebookPressed, Assets.buttonFacebookPressed);
        buttonGoogle = new Button(Assets.buttonAchievement, Assets.buttonAchievementPressed, Assets.buttonLeaderboardPressed);
        buttonInviteFriend = new Button(Assets.buttonSettings, Assets.buttonSettingsPressed, Assets.buttonLeaderboardPressed);

        buttonLeaderboard.addListener(new ClickListener() {

        });

        buttonFacebook.addListener(new ClickListener() {
        });

        buttonGoogle.addListener(new ClickListener() {
        });

        buttonInviteFriend.addListener(new ClickListener() {
        });

        ButtonGroup<Button> radioGroup = new ButtonGroup<>();
        radioGroup.add(buttonLeaderboard, buttonFacebook, buttonGoogle, buttonInviteFriend);

    }

    @Override
    public void draw(float delta) {
        Assets.backgroundNubes.render(0);

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
            changeScreenWithFadeOut(GameScreen.class, game);
            return true;
        }
        return super.keyUp(keycode);
    }

    public void updateLeaderboard() {
        table.clear();
        for (Person player : game.arrayOfPersons) {
            LeaderBoardFrame frame = new LeaderBoardFrame(player);
            table.add(frame).expandX().fill();
            table.row();
        }
    }

}
