package com.nopalsoft.slamthebird.shop;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.slamthebird.Assets;
import com.nopalsoft.slamthebird.MainSlamBird;
import com.nopalsoft.slamthebird.Settings;

public class UpgradesSubMenu {
    public final int MAX_LEVEL = 6;

    int level1Price = 500;
    int level2Price = 1000;
    int level3Price = 2500;
    int level4Price = 4000;
    int level5Price = 5000;
    int level6Price = 6000;

    TextButton buttonUpgradeBoostTime, buttonUpgradeIce, buttonUpgradeCoins,
            buttonUpgradeSuperJump, buttonUpgradeInvincible;
    Label labelPriceBoostTime, labelPriceIce, labelPriceCoins,
            labelPriceSuperJump, labelPriceInvincible;

    Image[] arrBoostTime;
    Image[] arrBoostIce;
    Image[] arrBoostCoins;
    Image[] arrBoostSuperJump;
    Image[] arrBoostInvincible;

    Table container;
    MainSlamBird game;

    public UpgradesSubMenu(MainSlamBird game, Table container) {
        this.game = game;
        this.container = container;
        container.clear();

        arrBoostTime = new Image[MAX_LEVEL];
        arrBoostIce = new Image[MAX_LEVEL];
        arrBoostCoins = new Image[MAX_LEVEL];
        arrBoostSuperJump = new Image[MAX_LEVEL];
        arrBoostInvincible = new Image[MAX_LEVEL];

        if (Settings.LEVEL_BOOST_TIME < MAX_LEVEL)
            labelPriceBoostTime = new Label(
                    calculatePrice(Settings.LEVEL_BOOST_TIME) + "",
                    Assets.labelStyleSmall);

        if (Settings.LEVEL_BOOST_ICE < MAX_LEVEL)
            labelPriceIce = new Label(calculatePrice(Settings.LEVEL_BOOST_ICE)
                    + "", Assets.labelStyleSmall);

        if (Settings.LEVEL_BOOST_COINS < MAX_LEVEL)
            labelPriceCoins = new Label(
                    calculatePrice(Settings.LEVEL_BOOST_COINS) + "",
                    Assets.labelStyleSmall);

        if (Settings.LEVEL_BOOST_SUPER_JUMP < MAX_LEVEL)
            labelPriceSuperJump = new Label(
                    calculatePrice(Settings.LEVEL_BOOST_SUPER_JUMP) + "",
                    Assets.labelStyleSmall);

        if (Settings.LEVEL_BOOST_INVINCIBLE < MAX_LEVEL)
            labelPriceInvincible = new Label(
                    calculatePrice(Settings.LEVEL_BOOST_INVINCIBLE) + "",
                    Assets.labelStyleSmall);

        initializeButtons();

        container.add(new Image(Assets.horizontalSeparator)).expandX().fill()
                .height(5);
        container.row();

        // Upgrade BoostTime
        container
                .add(addCharacterTable("More power-ups",
                        labelPriceBoostTime, Assets.boosts,
                        "Power-ups will appear more often in the game",
                        arrBoostTime, buttonUpgradeBoostTime)).expandX().fill();
        container.row();

        // Upgrade Super Jump
        container
                .add(addCharacterTable("Super jump", labelPriceSuperJump,
                        Assets.superJumpBoost,
                        "Super jump power up will last more time",
                        arrBoostSuperJump, buttonUpgradeSuperJump)).expandX()
                .fill();
        container.row();

        // Upgrade Ice
        container
                .add(addCharacterTable("Freeze enemies", labelPriceIce,
                        Assets.IceBoost, "Enemies will last more time frozen",
                        arrBoostIce, buttonUpgradeIce)).expandX().fill();
        container.row();

        // Upgrade invincible
        container
                .add(addCharacterTable("Invencible", labelPriceInvincible,
                        Assets.invincibleBoost,
                        "The invencible power-up will last more time",
                        arrBoostInvincible, buttonUpgradeInvincible)).expandX()
                .fill();
        container.row();

        // Upgrade Monedas
        container
                .add(addCharacterTable(
                        "Coin rain",
                        labelPriceCoins,
                        Assets.coinRainBoost,
                        "More coins will fall down when the coin rain power-up is taken",
                        arrBoostCoins, buttonUpgradeCoins)).expandX().fill();
        container.row();

        setArrays();

    }

    private Table addCharacterTable(String title, Label labelPrice,
                                    AtlasRegion image, String description, Image[] arrLevel,
                                    TextButton button) {

        Image coinImage = new Image(Assets.coin);
        Image playerImage = new Image(image);

        if (labelPrice == null)
            coinImage.setVisible(false);

        Table tableTitleBar = new Table();
        tableTitleBar.add(new Label(title, Assets.labelStyleSmall)).expandX()
                .left().padLeft(5);
        tableTitleBar.add(coinImage).right();
        tableTitleBar.add(labelPrice).right().padRight(10);

        Table tableDescription = new Table();
        tableDescription.add(playerImage).left().pad(10).size(55, 45);
        Label labelDescription = new Label(description, Assets.labelStyleSmall);
        labelDescription.setWrap(true);
        tableDescription.add(labelDescription).expand().fill().padLeft(5);

        Table tbContent = new Table();
        tbContent.add(tableTitleBar).expandX().fill().colspan(2).padTop(8);
        tbContent.row().colspan(2);
        tbContent.add(tableDescription).expandX().fill();
        tbContent.row();

        Table auxTab = new Table();
        auxTab.defaults().padLeft(5);
        for (int i = 0; i < MAX_LEVEL; i++) {
            arrLevel[i] = new Image(Assets.buttonUpgradeOff);
            auxTab.add(arrLevel[i]).width(18).height(25);
        }

        tbContent.add(auxTab).center().expand();
        tbContent.add(button).right().padRight(10).size(120, 45);

        tbContent.row().colspan(2);
        tbContent.add(new Image(Assets.horizontalSeparator)).expandX().fill()
                .height(5).padTop(15);

        return tbContent;

    }

    private void initializeButtons() {
        buttonUpgradeBoostTime = new TextButton("Upgrade",
                Assets.textButtonStyleSelected);
        if (Settings.LEVEL_BOOST_TIME == MAX_LEVEL)
            buttonUpgradeBoostTime.setVisible(false);
        addPressEffect(buttonUpgradeBoostTime);
        buttonUpgradeBoostTime.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.currentCoins >= calculatePrice(Settings.LEVEL_BOOST_TIME)) {
                    Settings.currentCoins -= calculatePrice(Settings.LEVEL_BOOST_TIME);
                    Settings.LEVEL_BOOST_TIME++;
                    updateLabelPriceAndButton(Settings.LEVEL_BOOST_TIME,
                            labelPriceBoostTime, buttonUpgradeBoostTime);
                    setArrays();
                }
            }
        });

        buttonUpgradeSuperJump = new TextButton("Upgrade",
                Assets.textButtonStyleSelected);
        if (Settings.LEVEL_BOOST_SUPER_JUMP == MAX_LEVEL)
            buttonUpgradeSuperJump.setVisible(false);
        addPressEffect(buttonUpgradeSuperJump);
        buttonUpgradeSuperJump.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.currentCoins >= calculatePrice(Settings.LEVEL_BOOST_SUPER_JUMP)) {
                    Settings.currentCoins -= calculatePrice(Settings.LEVEL_BOOST_SUPER_JUMP);
                    Settings.LEVEL_BOOST_SUPER_JUMP++;
                    updateLabelPriceAndButton(Settings.LEVEL_BOOST_SUPER_JUMP,
                            labelPriceSuperJump, buttonUpgradeSuperJump);
                    setArrays();
                }
            }
        });

        buttonUpgradeIce = new TextButton("Upgrade", Assets.textButtonStyleSelected);
        if (Settings.LEVEL_BOOST_ICE == MAX_LEVEL)
            buttonUpgradeIce.setVisible(false);

        addPressEffect(buttonUpgradeIce);
        buttonUpgradeIce.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.currentCoins >= calculatePrice(Settings.LEVEL_BOOST_ICE)) {
                    Settings.currentCoins -= calculatePrice(Settings.LEVEL_BOOST_ICE);
                    Settings.LEVEL_BOOST_ICE++;
                    updateLabelPriceAndButton(Settings.LEVEL_BOOST_ICE,
                            labelPriceIce, buttonUpgradeIce);
                    setArrays();
                }
            }
        });

        buttonUpgradeInvincible = new TextButton("Upgrade",
                Assets.textButtonStyleSelected);
        if (Settings.LEVEL_BOOST_INVINCIBLE == MAX_LEVEL)
            buttonUpgradeInvincible.setVisible(false);

        addPressEffect(buttonUpgradeInvincible);
        buttonUpgradeInvincible.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.currentCoins >= calculatePrice(Settings.LEVEL_BOOST_INVINCIBLE)) {
                    Settings.currentCoins -= calculatePrice(Settings.LEVEL_BOOST_INVINCIBLE);
                    Settings.LEVEL_BOOST_INVINCIBLE++;
                    updateLabelPriceAndButton(Settings.LEVEL_BOOST_INVINCIBLE,
                            labelPriceInvincible, buttonUpgradeInvincible);
                    setArrays();
                }
            }
        });

        buttonUpgradeCoins = new TextButton("Upgrade",
                Assets.textButtonStyleSelected);
        if (Settings.LEVEL_BOOST_COINS == MAX_LEVEL)
            buttonUpgradeCoins.setVisible(false);

        addPressEffect(buttonUpgradeCoins);
        buttonUpgradeCoins.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.currentCoins >= calculatePrice(Settings.LEVEL_BOOST_COINS)) {
                    Settings.currentCoins -= calculatePrice(Settings.LEVEL_BOOST_COINS);
                    Settings.LEVEL_BOOST_COINS++;
                    updateLabelPriceAndButton(Settings.LEVEL_BOOST_COINS,
                            labelPriceCoins, buttonUpgradeCoins);
                    setArrays();
                }
            }
        });

    }

    private void setArrays() {
        for (int i = 0; i < Settings.LEVEL_BOOST_TIME; i++) {
            arrBoostTime[i].setDrawable(new TextureRegionDrawable(
                    Assets.buttonUpgradeOn));
        }

        for (int i = 0; i < Settings.LEVEL_BOOST_ICE; i++) {
            arrBoostIce[i].setDrawable(new TextureRegionDrawable(
                    Assets.buttonUpgradeOn));
        }

        for (int i = 0; i < Settings.LEVEL_BOOST_INVINCIBLE; i++) {
            arrBoostInvincible[i].setDrawable(new TextureRegionDrawable(
                    Assets.buttonUpgradeOn));
        }

        for (int i = 0; i < Settings.LEVEL_BOOST_SUPER_JUMP; i++) {
            arrBoostSuperJump[i].setDrawable(new TextureRegionDrawable(
                    Assets.buttonUpgradeOn));
        }

        for (int i = 0; i < Settings.LEVEL_BOOST_COINS; i++) {
            arrBoostCoins[i].setDrawable(new TextureRegionDrawable(
                    Assets.buttonUpgradeOn));
        }
    }

    private int calculatePrice(int level) {
        switch (level) {
            case 0:
                return level1Price;

            case 1:
                return level2Price;

            case 2:
                return level3Price;

            case 3:
                return level4Price;

            case 4:
                return level5Price;
            default:
            case 5:
                return level6Price;

        }

    }

    private void updateLabelPriceAndButton(int level, Label label,
                                           TextButton button) {
        if (level < MAX_LEVEL) {
            label.setText(calculatePrice(level) + "");

        } else {
            label.setVisible(false);
            button.setVisible(false);
        }
    }

    protected void addPressEffect(final Actor actor) {
        actor.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                actor.setPosition(actor.getX(), actor.getY() - 3);
                event.stop();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                actor.setPosition(actor.getX(), actor.getY() + 3);
            }
        });
    }
}
