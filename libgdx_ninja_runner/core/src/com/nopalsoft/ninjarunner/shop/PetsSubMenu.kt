package com.nopalsoft.ninjarunner.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.nopalsoft.ninjarunner.AnimationSprite;
import com.nopalsoft.ninjarunner.Assets;
import com.nopalsoft.ninjarunner.MainGame;
import com.nopalsoft.ninjarunner.Settings;
import com.nopalsoft.ninjarunner.objects.Pet;
import com.nopalsoft.ninjarunner.scene2d.AnimatedSpriteActor;

public class PetsSubMenu {

    private final static Preferences pref = Gdx.app.getPreferences("com.tiar.shantirunner.shop");
    public final int MAX_LEVEL = 6;
    final int BOMB_PRICE = 5000;
    final int PRICE_LEVEL_1 = 350;
    final int PRICE_LEVEL_2 = 1000;
    final int PRICE_LEVEL_3 = 3000;
    final int PRICE_LEVEL_4 = 4500;
    final int PRICE_LEVEL_5 = 5000;
    final int PRICE_LEVEL_6 = 7500;
    boolean didBuyBomb;
    Label labelPriceBird, labelPriceBomb;
    TextButton buttonBuyOrSelectBird, buttonBuyBomb;
    Array<TextButton> arrayButtons;
    Button buttonUpgradeBird, buttonUpgradeBomb;
    Image[] arrayBird;
    Image[] arrayBomb;
    Table myContainer;
    I18NBundle languages;
    String textBuy;
    String textSelect;

    public PetsSubMenu(Table myContainer, MainGame game) {
        languages = game.languages;
        this.myContainer = myContainer;
        myContainer.clear();

        loadPurchases();

        textBuy = languages.get("buy");
        textSelect = languages.get("select");

        arrayBird = new Image[MAX_LEVEL];
        arrayBomb = new Image[MAX_LEVEL];

        if (Settings.getLEVEL_PET_BIRD() < MAX_LEVEL) {
            labelPriceBird = new Label(calculatePrice(Settings.getLEVEL_PET_BIRD()) + "", Assets.labelStyleSmall);
        }

        if (!didBuyBomb) {
            labelPriceBomb = new Label(BOMB_PRICE + "", Assets.labelStyleSmall);
        } else if (Settings.getLEVEL_PET_BOMB() < MAX_LEVEL) {
            labelPriceBomb = new Label(calculatePrice(Settings.getLEVEL_PET_BOMB()) + "", Assets.labelStyleSmall);
        }

        initializeButtons();

        myContainer.defaults().expand().fill().padLeft(10).padRight(20).padBottom(10);

        myContainer.add(
                addPet("Chicken", labelPriceBird, Assets.Pet1FlyAnimation, 60, 54, languages.get("pinkChikenDescription"), buttonBuyOrSelectBird, arrayBird,
                        buttonUpgradeBird)).row();
        myContainer.add(
                        addPet("Bomb", labelPriceBomb, Assets.PetBombFlyAnimation, 53, 64, languages.get("bombDescription"), buttonBuyBomb, arrayBomb, buttonUpgradeBomb))
                .row();

        setArrays();
    }

    public Table addPet(String title, Label labelPrice, AnimationSprite image, float imageWidth, float imageHeight, String description,
                        TextButton buttonBuy, Image[] arrayLevel, Button buttonUpgrade) {
        Image coin = new Image(Assets.coinAnimation.getKeyFrame(0));
        AnimatedSpriteActor playerImage = new AnimatedSpriteActor(image);

        if (labelPrice == null)
            coin.setVisible(false);

        Table tableTitleBar = new Table();
        tableTitleBar.add(new Label(title, Assets.labelStyleSmall)).expandX().left();
        tableTitleBar.add(coin).right().size(20);
        tableTitleBar.add(labelPrice).right().padRight(10);

        Table tableContent = new Table();
        tableContent.setBackground(Assets.backgroundItemShop);
        tableContent.pad(5);

        tableContent.add(tableTitleBar).expandX().fill().colspan(2);
        tableContent.row();

        tableContent.add(playerImage).size(imageWidth, imageHeight);
        Label labelDescription = new Label(description, Assets.labelStyleSmall);
        labelDescription.setWrap(true);
        tableContent.add(labelDescription).expand().fill();

        Table auxTab = new Table();
        auxTab.setBackground(Assets.backgroundUpgradeBar);
        auxTab.pad(5);
        auxTab.defaults().padLeft(5);
        for (int i = 0; i < MAX_LEVEL; i++) {
            arrayLevel[i] = new Image();
            auxTab.add(arrayLevel[i]).size(15);
        }

        tableContent.row();
        tableContent.add(auxTab);
        tableContent.add(buttonUpgrade).left().size(40);

        tableContent.row().colspan(2);
        tableContent.add(buttonBuy).expandX().right().size(120, 45);
        tableContent.row().colspan(2);

        return tableContent;

    }

    private void initializeButtons() {
        arrayButtons = new Array<>();

        {
            {
                buttonBuyOrSelectBird = new TextButton(textSelect, Assets.styleTextButtonPurchased);
                if (Settings.getSelectedPet() == Pet.PetType.PINK_BIRD)
                    buttonBuyOrSelectBird.setVisible(false);
                buttonBuyOrSelectBird.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Settings.setSelectedPet(Pet.PetType.PINK_BIRD);
                        setSelected(buttonBuyOrSelectBird);
                    }
                });
            }
            {
                buttonUpgradeBird = new Button(Assets.styleButtonUpgrade);
                if (Settings.getLEVEL_PET_BIRD() == MAX_LEVEL)
                    buttonUpgradeBird.setVisible(false);
                buttonUpgradeBird.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (Settings.getTotalCoins() >= calculatePrice(Settings.getLEVEL_PET_BIRD())) {
                            Settings.setTotalCoins(Settings.getTotalCoins() - calculatePrice(Settings.getLEVEL_PET_BIRD()));
                            Settings.setLEVEL_PET_BIRD(Settings.getLEVEL_PET_BIRD() + 1);

                            updateLabelPriceAndButton(Settings.getLEVEL_PET_BIRD(), labelPriceBird, buttonUpgradeBird);
                            setArrays();
                        }
                    }
                });
            }
        }

        {
            {
                if (didBuyBomb)
                    buttonBuyBomb = new TextButton(textSelect, Assets.styleTextButtonPurchased);
                else
                    buttonBuyBomb = new TextButton(textBuy, Assets.styleTextButtonBuy);

                if (Settings.getSelectedPet() == Pet.PetType.BOMB)
                    buttonBuyBomb.setVisible(false);

                buttonBuyBomb.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (didBuyBomb) {
                            Settings.setSelectedPet(Pet.PetType.BOMB);
                            setSelected(buttonBuyBomb);
                        } else if (Settings.getTotalCoins() >= BOMB_PRICE) {

                            Settings.setTotalCoins(Settings.getTotalCoins() - BOMB_PRICE);
                            setButtonStylePurchased(buttonBuyBomb);
                            didBuyBomb = true;
                            buttonUpgradeBomb.setVisible(true);
                            updateLabelPriceAndButton(Settings.getLEVEL_PET_BOMB(), labelPriceBomb, buttonUpgradeBomb);
                        }
                        savePurchases();
                    }
                });
            }
            {// UPGRADE
                buttonUpgradeBomb = new Button(Assets.styleButtonUpgrade);
                if (Settings.getLEVEL_PET_BOMB() == MAX_LEVEL || !didBuyBomb)
                    buttonUpgradeBomb.setVisible(false);
                buttonUpgradeBomb.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (Settings.getTotalCoins() >= calculatePrice(Settings.getLEVEL_PET_BOMB())) {
                            Settings.setTotalCoins(Settings.getTotalCoins() - calculatePrice(Settings.getLEVEL_PET_BOMB()));
                            Settings.setLEVEL_PET_BOMB(Settings.getLEVEL_PET_BOMB() + 1);
                            updateLabelPriceAndButton(Settings.getLEVEL_PET_BOMB(), labelPriceBomb, buttonUpgradeBomb);
                            setArrays();
                        }
                    }
                });
            }
        }

        arrayButtons.add(buttonBuyOrSelectBird);
        arrayButtons.add(buttonBuyBomb);

    }

    private void loadPurchases() {
        didBuyBomb = pref.getBoolean("didBuyBomb", false);
    }

    private void savePurchases() {
        pref.putBoolean("didBuyBomb", didBuyBomb);
        pref.flush();
        Settings.save();

    }

    private void setButtonStylePurchased(TextButton boton) {
        boton.setStyle(Assets.styleTextButtonPurchased);
        boton.setText(textSelect);

    }

    private void setSelected(TextButton button) {
        // I make them all visible and at the end the selected
        // button is invisible.
        for (TextButton textButton : arrayButtons) {
            textButton.setVisible(true);
        }
        button.setVisible(false);
    }

    private int calculatePrice(int level) {
        switch (level) {
            case 0:
                return PRICE_LEVEL_1;

            case 1:
                return PRICE_LEVEL_2;

            case 2:
                return PRICE_LEVEL_3;

            case 3:
                return PRICE_LEVEL_4;

            case 4:
                return PRICE_LEVEL_5;
            default:
            case 5:
                return PRICE_LEVEL_6;

        }

    }

    private void updateLabelPriceAndButton(int level, Label label, Button button) {
        if (level < MAX_LEVEL) {
            label.setText(calculatePrice(level) + "");

        } else {
            label.setVisible(false);
            button.setVisible(false);
        }
    }

    private void setArrays() {
        for (int i = 0; i < Settings.getLEVEL_PET_BIRD(); i++) {
            arrayBird[i].setDrawable(new TextureRegionDrawable(Assets.buttonShare));
        }

        for (int i = 0; i < Settings.getLEVEL_PET_BOMB(); i++) {
            arrayBomb[i].setDrawable(new TextureRegionDrawable(Assets.buttonShare));
        }

    }

}
