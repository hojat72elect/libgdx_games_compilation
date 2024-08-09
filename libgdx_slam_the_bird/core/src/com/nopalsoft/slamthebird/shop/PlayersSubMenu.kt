package com.nopalsoft.slamthebird.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.nopalsoft.slamthebird.Assets;
import com.nopalsoft.slamthebird.MainSlamBird;
import com.nopalsoft.slamthebird.Settings;

public class PlayersSubMenu {
    public static final int SKIN_DEFAULT = 0;
    public static final int SKIN_ANDROID_RED = 1;
    public static final int SKIN_ANDROID_BLUE = 2;
    private final static Preferences pref = Gdx.app
            .getPreferences("com.nopalsoft.slamthebird.personajes");
    final int PRICE_RED_ANDROID = 1500;
    final int PRICE_BLUE_ANDROID = 2000;
    boolean didBuyRed;
    boolean didBuyBlue;
    TextButton textButtonBuyDefault, textButtonBuyRedAndroid, textButtonBuyBlueAndroid;
    Array<TextButton> arrayTextButtons;
    Table container;
    MainSlamBird game;
    Label labelPriceRed, labelPriceBlue;

    public PlayersSubMenu(MainSlamBird game, Table container) {
        this.game = game;
        this.container = container;
        container.clear();

        didBuyRed = pref.getBoolean("didBuyRojo", false);
        didBuyBlue = pref.getBoolean("didBuyAzul", false);

        labelPriceRed = new Label(PRICE_RED_ANDROID + "",
                Assets.labelStyleSmall);
        labelPriceBlue = new Label(PRICE_BLUE_ANDROID + "",
                Assets.labelStyleSmall);

        initializeButtons();

        container.add(new Image(Assets.horizontalSeparator)).expandX().fill()
                .height(5);
        container.row();

        // Use Default
        container
                .add(addPlayerTable("Green robot", null,
                        Assets.playerShopDefault,
                        "Just a simple robot for slaming birds", textButtonBuyDefault))
                .expandX().fill();
        container.row();

        // Use red Player
        container
                .add(addPlayerTable(
                        "Red robot",
                        labelPriceRed,
                        Assets.playerShopRed,
                        "Do you like red color. Play with this amazing red robot and slam those birds",
                        textButtonBuyRedAndroid)).expandX().fill();
        container.row();

        // SKIN_ANDROID_BLUE
        container
                .add(addPlayerTable(
                        "Blue robot",
                        labelPriceBlue,
                        Assets.playerShopBlue,
                        "Do you like blue color. Play with this amazing blue robot and slam those birds",
                        textButtonBuyBlueAndroid)).expandX().fill();
        container.row();

        if (didBuyBlue)
            labelPriceBlue.remove();
        if (didBuyRed)
            labelPriceRed.remove();

    }

    private Table addPlayerTable(String title, Label labelPrice,
                                 AtlasRegion image, String description, TextButton button) {

        Image coin = new Image(Assets.coin);
        Image imagePlayer = new Image(image);

        if (labelPrice == null)
            coin.setVisible(false);

        Table tableTitleBar = new Table();
        tableTitleBar.add(new Label(title, Assets.labelStyleSmall)).expandX()
                .left();
        tableTitleBar.add(coin).right();
        tableTitleBar.add(labelPrice).right().padRight(10);

        Table tbContent = new Table();

        tbContent.add(tableTitleBar).expandX().fill().colspan(2).padTop(8);
        tbContent.row();
        tbContent.add(imagePlayer).left().pad(10).size(60);

        Label lblDescripcion = new Label(description, Assets.labelStyleSmall);
        lblDescripcion.setWrap(true);
        tbContent.add(lblDescripcion).expand().fill().padLeft(5);

        tbContent.row().colspan(2);
        tbContent.add(button).expandX().right().padRight(10).size(120, 45);
        tbContent.row().colspan(2);
        tbContent.add(new Image(Assets.horizontalSeparator)).expandX().fill()
                .height(5).padTop(15);

        return tbContent;

    }

    private void initializeButtons() {
        arrayTextButtons = new Array<>();

        // SKIN_DEFAULT
        textButtonBuyDefault = new TextButton("Select", Assets.textButtonStylePurchased);
        if (Settings.selectedSkin == SKIN_DEFAULT)
            textButtonBuyDefault.setVisible(false);

        addEfectoPress(textButtonBuyDefault);
        textButtonBuyDefault.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.selectedSkin = SKIN_DEFAULT;
                setSelected(textButtonBuyDefault);
                Assets.loadPlayer();

            }
        });

        // SKIN_ANDROID_RED
        if (didBuyRed)
            textButtonBuyRedAndroid = new TextButton("Select",
                    Assets.textButtonStylePurchased);
        else
            textButtonBuyRedAndroid = new TextButton("Buy", Assets.textButtonStyleBuy);

        if (Settings.selectedSkin == SKIN_ANDROID_RED)
            textButtonBuyRedAndroid.setVisible(false);

        addEfectoPress(textButtonBuyRedAndroid);
        textButtonBuyRedAndroid.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyRed) {
                    Settings.selectedSkin = SKIN_ANDROID_RED;
                    setSelected(textButtonBuyRedAndroid);
                    Assets.loadPlayer();
                } else if (Settings.currentCoins >= PRICE_RED_ANDROID) {
                    Settings.currentCoins -= PRICE_RED_ANDROID;
                    setButtonStylePurchased(textButtonBuyRedAndroid);
                    didBuyRed = true;
                    labelPriceRed.remove();
                    save();
                }
            }
        });

        // SKIN_ANDROID_BLUE
        if (didBuyBlue)
            textButtonBuyBlueAndroid = new TextButton("Select",
                    Assets.textButtonStylePurchased);
        else
            textButtonBuyBlueAndroid = new TextButton("Buy", Assets.textButtonStyleBuy);

        if (Settings.selectedSkin == SKIN_ANDROID_BLUE)
            textButtonBuyBlueAndroid.setVisible(false);

        addEfectoPress(textButtonBuyBlueAndroid);
        textButtonBuyBlueAndroid.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyBlue) {
                    Settings.selectedSkin = SKIN_ANDROID_BLUE;
                    setSelected(textButtonBuyBlueAndroid);
                    Assets.loadPlayer();
                } else if (Settings.currentCoins >= PRICE_BLUE_ANDROID) {
                    Settings.currentCoins -= PRICE_BLUE_ANDROID;
                    setButtonStylePurchased(textButtonBuyBlueAndroid);
                    didBuyBlue = true;
                    labelPriceBlue.remove();
                    save();
                }
            }
        });

        arrayTextButtons.add(textButtonBuyDefault);
        arrayTextButtons.add(textButtonBuyRedAndroid);
        arrayTextButtons.add(textButtonBuyBlueAndroid);

    }

    private void save() {
        pref.putBoolean("didBuyAzul", didBuyBlue);
        pref.putBoolean("didBuyRojo", didBuyRed);
        pref.flush();
    }

    private void setButtonStylePurchased(TextButton boton) {
        boton.setStyle(Assets.textButtonStylePurchased);
        boton.setText("Select");

    }

    private void setSelected(TextButton boton) {
        // I make them all visible and at the end the selected button is invisible.
        for (TextButton button : arrayTextButtons) {
            button.setVisible(true);
        }
        boton.setVisible(false);
    }

    protected void addEfectoPress(final Actor actor) {
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
