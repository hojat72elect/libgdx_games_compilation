package com.nopalsoft.slamthebird.shop;

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

public class NoAdsSubMenu {

    int priceNoAds = 35000;

    TextButton buttonNoAds;
    Label labelNoAds;

    Table container;
    MainSlamBird game;

    public NoAdsSubMenu(final MainSlamBird game, Table container) {
        this.game = game;
        this.container = container;
        container.clear();

        if (!Settings.didBuyNoAds)
            labelNoAds = new Label(priceNoAds + "", Assets.labelStyleSmall);

        buttonNoAds = new TextButton("Buy", Assets.textButtonStyleBuy);
        if (Settings.didBuyNoAds)
            buttonNoAds.setVisible(false);
        addPressEffect(buttonNoAds);
        buttonNoAds.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.currentCoins >= priceNoAds) {
                    Settings.currentCoins -= priceNoAds;
                    Settings.didBuyNoAds = true;
                    labelNoAds.setVisible(false);
                    buttonNoAds.setVisible(false);
                }
            }
        });

        // Upgrade BoostTime
        container.add(new Image(Assets.horizontalSeparator)).expandX().fill().height(5);
        container.row();
        container
                .add(addCharacterTable(labelNoAds, Assets.buttonNoAds,
                        buttonNoAds)).expandX().fill();
        container.row();

    }

    private Table addCharacterTable(Label labelPrice, TextureRegionDrawable image,
                                    TextButton button) {

        Image imageCoin = new Image(Assets.coin);
        Image imagePlayer = new Image(image);

        if (labelPrice == null)
            imageCoin.setVisible(false);

        Table tableTitleBar = new Table();
        tableTitleBar.add(new Label("No more ads", Assets.labelStyleSmall)).expandX().left().padLeft(5);
        tableTitleBar.add(imageCoin).right();
        tableTitleBar.add(labelPrice).right().padRight(10);

        Table tableDescription = new Table();
        tableDescription.add(imagePlayer).left().pad(10).size(55, 45);
        Label labelDescription = new Label("Buy it and no more ads will appear in the app", Assets.labelStyleSmall);
        labelDescription.setWrap(true);
        tableDescription.add(labelDescription).expand().fill().padLeft(5);

        Table tableContent = new Table();
        tableContent.add(tableTitleBar).expandX().fill().colspan(2).padTop(8);
        tableContent.row().colspan(2);
        tableContent.add(tableDescription).expandX().fill();
        tableContent.row().colspan(2);

        tableContent.add(button).right().padRight(10).size(120, 45);

        tableContent.row().colspan(2);
        tableContent.add(new Image(Assets.horizontalSeparator)).expandX().fill().height(5).padTop(15);

        return tableContent;

    }

    protected void addPressEffect(final Actor actor) {
        actor.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                actor.setPosition(actor.getX(), actor.getY() - 3);
                event.stop();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                actor.setPosition(actor.getX(), actor.getY() + 3);
            }
        });
    }
}
