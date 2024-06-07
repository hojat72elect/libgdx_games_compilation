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

    int precioNivel1 = 500;
    int precioNivel2 = 1000;
    int precioNivel3 = 2500;
    int precioNivel4 = 4000;
    int precioNivel5 = 5000;
    int precioNivel6 = 6000;

    TextButton btUpgradeBoostTime, btUpgradeIce, buttonUpgradeCoins,
            btUpgradeSuperSalto, btUpgradeInvencible;
    Label lblPrecioBoostTime, lblPrecioIce, lblPrecioMonedas,
            lblPrecioSuperSalto, lblPrecioInvencible;

    Image[] arrBoostTime;
    Image[] arrBoostIce;
    Image[] arrBoostMonedas;
    Image[] arrBoostSuperJump;
    Image[] arrBoostInvencible;

    Table contenedor;
    MainSlamBird game;

    public UpgradesSubMenu(MainSlamBird game, Table contenedor) {
        this.game = game;
        this.contenedor = contenedor;
        contenedor.clear();

        arrBoostTime = new Image[MAX_LEVEL];
        arrBoostIce = new Image[MAX_LEVEL];
        arrBoostMonedas = new Image[MAX_LEVEL];
        arrBoostSuperJump = new Image[MAX_LEVEL];
        arrBoostInvencible = new Image[MAX_LEVEL];

        if (Settings.LEVEL_BOOST_TIME < MAX_LEVEL)
            lblPrecioBoostTime = new Label(
                    calcularPrecio(Settings.LEVEL_BOOST_TIME) + "",
                    Assets.labelStyleSmall);

        if (Settings.LEVEL_BOOST_ICE < MAX_LEVEL)
            lblPrecioIce = new Label(calcularPrecio(Settings.LEVEL_BOOST_ICE)
                    + "", Assets.labelStyleSmall);

        if (Settings.LEVEL_BOOST_COINS < MAX_LEVEL)
            lblPrecioMonedas = new Label(
                    calcularPrecio(Settings.LEVEL_BOOST_COINS) + "",
                    Assets.labelStyleSmall);

        if (Settings.LEVEL_BOOST_SUPER_JUMP < MAX_LEVEL)
            lblPrecioSuperSalto = new Label(
                    calcularPrecio(Settings.LEVEL_BOOST_SUPER_JUMP) + "",
                    Assets.labelStyleSmall);

        if (Settings.LEVEL_BOOST_INVINCIBLE < MAX_LEVEL)
            lblPrecioInvencible = new Label(
                    calcularPrecio(Settings.LEVEL_BOOST_INVINCIBLE) + "",
                    Assets.labelStyleSmall);

        inicializarBotones();

        contenedor.add(new Image(Assets.horizontalSeparator)).expandX().fill()
                .height(5);
        contenedor.row();

        // Upgrade BoostTime
        contenedor
                .add(agregarPersonajeTabla("More power-ups",
                        lblPrecioBoostTime, Assets.boosts,
                        "Power-ups will appear more often in the game",
                        arrBoostTime, btUpgradeBoostTime)).expandX().fill();
        contenedor.row();

        // Upgrade Super Jump
        contenedor
                .add(agregarPersonajeTabla("Super jump", lblPrecioSuperSalto,
                        Assets.superJumpBoost,
                        "Super jump power up will last more time",
                        arrBoostSuperJump, btUpgradeSuperSalto)).expandX()
                .fill();
        contenedor.row();

        // Upgrade Ice
        contenedor
                .add(agregarPersonajeTabla("Freeze enemies", lblPrecioIce,
                        Assets.IceBoost, "Enemies will last more time frozen",
                        arrBoostIce, btUpgradeIce)).expandX().fill();
        contenedor.row();

        // Upgrade Invencible
        contenedor
                .add(agregarPersonajeTabla("Invencible", lblPrecioInvencible,
                        Assets.invincibleBoost,
                        "The invencible power-up will last more time",
                        arrBoostInvencible, btUpgradeInvencible)).expandX()
                .fill();
        contenedor.row();

        // Upgrade Monedas
        contenedor
                .add(agregarPersonajeTabla(
                        "Coin rain",
                        lblPrecioMonedas,
                        Assets.coinRainBoost,
                        "More coins will fall down when the coin rain power-up is taken",
                        arrBoostMonedas, buttonUpgradeCoins)).expandX().fill();
        contenedor.row();

        setArrays();

    }

    private Table agregarPersonajeTabla(String titulo, Label lblPrecio,
                                        AtlasRegion imagen, String descripcion, Image[] arrLevel,
                                        TextButton boton) {

        Image moneda = new Image(Assets.coin);
        Image imgPersonaje = new Image(imagen);

        if (lblPrecio == null)
            moneda.setVisible(false);

        Table tbBarraTitulo = new Table();
        tbBarraTitulo.add(new Label(titulo, Assets.labelStyleSmall)).expandX()
                .left().padLeft(5);
        tbBarraTitulo.add(moneda).right();
        tbBarraTitulo.add(lblPrecio).right().padRight(10);

        Table tbDescrip = new Table();
        tbDescrip.add(imgPersonaje).left().pad(10).size(55, 45);
        Label lblDescripcion = new Label(descripcion, Assets.labelStyleSmall);
        lblDescripcion.setWrap(true);
        tbDescrip.add(lblDescripcion).expand().fill().padLeft(5);

        Table tbContent = new Table();
        tbContent.add(tbBarraTitulo).expandX().fill().colspan(2).padTop(8);
        tbContent.row().colspan(2);
        tbContent.add(tbDescrip).expandX().fill();
        tbContent.row();

        Table auxTab = new Table();
        auxTab.defaults().padLeft(5);
        for (int i = 0; i < MAX_LEVEL; i++) {
            arrLevel[i] = new Image(Assets.buttonUpgradeOff);
            auxTab.add(arrLevel[i]).width(18).height(25);
        }

        tbContent.add(auxTab).center().expand();
        tbContent.add(boton).right().padRight(10).size(120, 45);

        tbContent.row().colspan(2);
        tbContent.add(new Image(Assets.horizontalSeparator)).expandX().fill()
                .height(5).padTop(15);

        return tbContent;

    }

    private void inicializarBotones() {
        btUpgradeBoostTime = new TextButton("Upgrade",
                Assets.textButtonStyleSelected);
        if (Settings.LEVEL_BOOST_TIME == MAX_LEVEL)
            btUpgradeBoostTime.setVisible(false);
        addEfectoPress(btUpgradeBoostTime);
        btUpgradeBoostTime.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.currentCoins >= calcularPrecio(Settings.LEVEL_BOOST_TIME)) {
                    Settings.currentCoins -= calcularPrecio(Settings.LEVEL_BOOST_TIME);
                    Settings.LEVEL_BOOST_TIME++;
                    updateLabelPriceAndButton(Settings.LEVEL_BOOST_TIME,
                            lblPrecioBoostTime, btUpgradeBoostTime);
                    setArrays();
                }
            }
        });

        btUpgradeSuperSalto = new TextButton("Upgrade",
                Assets.textButtonStyleSelected);
        if (Settings.LEVEL_BOOST_SUPER_JUMP == MAX_LEVEL)
            btUpgradeSuperSalto.setVisible(false);
        addEfectoPress(btUpgradeSuperSalto);
        btUpgradeSuperSalto.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.currentCoins >= calcularPrecio(Settings.LEVEL_BOOST_SUPER_JUMP)) {
                    Settings.currentCoins -= calcularPrecio(Settings.LEVEL_BOOST_SUPER_JUMP);
                    Settings.LEVEL_BOOST_SUPER_JUMP++;
                    updateLabelPriceAndButton(Settings.LEVEL_BOOST_SUPER_JUMP,
                            lblPrecioSuperSalto, btUpgradeSuperSalto);
                    setArrays();
                }
            }
        });

        btUpgradeIce = new TextButton("Upgrade", Assets.textButtonStyleSelected);
        if (Settings.LEVEL_BOOST_ICE == MAX_LEVEL)
            btUpgradeIce.setVisible(false);

        addEfectoPress(btUpgradeIce);
        btUpgradeIce.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.currentCoins >= calcularPrecio(Settings.LEVEL_BOOST_ICE)) {
                    Settings.currentCoins -= calcularPrecio(Settings.LEVEL_BOOST_ICE);
                    Settings.LEVEL_BOOST_ICE++;
                    updateLabelPriceAndButton(Settings.LEVEL_BOOST_ICE,
                            lblPrecioIce, btUpgradeIce);
                    setArrays();
                }
            }
        });

        btUpgradeInvencible = new TextButton("Upgrade",
                Assets.textButtonStyleSelected);
        if (Settings.LEVEL_BOOST_INVINCIBLE == MAX_LEVEL)
            btUpgradeInvencible.setVisible(false);

        addEfectoPress(btUpgradeInvencible);
        btUpgradeInvencible.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.currentCoins >= calcularPrecio(Settings.LEVEL_BOOST_INVINCIBLE)) {
                    Settings.currentCoins -= calcularPrecio(Settings.LEVEL_BOOST_INVINCIBLE);
                    Settings.LEVEL_BOOST_INVINCIBLE++;
                    updateLabelPriceAndButton(Settings.LEVEL_BOOST_INVINCIBLE,
                            lblPrecioInvencible, btUpgradeInvencible);
                    setArrays();
                }
            }
        });

        buttonUpgradeCoins = new TextButton("Upgrade",
                Assets.textButtonStyleSelected);
        if (Settings.LEVEL_BOOST_COINS == MAX_LEVEL)
            buttonUpgradeCoins.setVisible(false);

        addEfectoPress(buttonUpgradeCoins);
        buttonUpgradeCoins.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.currentCoins >= calcularPrecio(Settings.LEVEL_BOOST_COINS)) {
                    Settings.currentCoins -= calcularPrecio(Settings.LEVEL_BOOST_COINS);
                    Settings.LEVEL_BOOST_COINS++;
                    updateLabelPriceAndButton(Settings.LEVEL_BOOST_COINS,
                            lblPrecioMonedas, buttonUpgradeCoins);
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
            arrBoostInvencible[i].setDrawable(new TextureRegionDrawable(
                    Assets.buttonUpgradeOn));
        }

        for (int i = 0; i < Settings.LEVEL_BOOST_SUPER_JUMP; i++) {
            arrBoostSuperJump[i].setDrawable(new TextureRegionDrawable(
                    Assets.buttonUpgradeOn));
        }

        for (int i = 0; i < Settings.LEVEL_BOOST_COINS; i++) {
            arrBoostMonedas[i].setDrawable(new TextureRegionDrawable(
                    Assets.buttonUpgradeOn));
        }
    }

    private int calcularPrecio(int nivel) {
        switch (nivel) {
            case 0:
                return precioNivel1;

            case 1:
                return precioNivel2;

            case 2:
                return precioNivel3;

            case 3:
                return precioNivel4;

            case 4:
                return precioNivel5;
            default:
            case 5:
                return precioNivel6;

        }

    }

    private void updateLabelPriceAndButton(int nivel, Label label,
                                           TextButton boton) {
        if (nivel < MAX_LEVEL) {
            label.setText(calcularPrecio(nivel) + "");

        } else {
            label.setVisible(false);
            boton.setVisible(false);
        }
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
