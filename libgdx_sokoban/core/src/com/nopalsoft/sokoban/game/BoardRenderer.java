package com.nopalsoft.sokoban.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.nopalsoft.sokoban.Assets;
import com.nopalsoft.sokoban.screens.Screens;

public class BoardRenderer {

    SpriteBatch batcher;
    OrthogonalTiledMapRenderer tiledRender;
    TiledMapTileLayer mapStaticLayer;
    OrthographicCamera myCamera;

    public BoardRenderer(SpriteBatch batch) {
        batcher = batch;
        myCamera = new OrthographicCamera(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT);
        myCamera.position.set(Screens.SCREEN_WIDTH / 2f, Screens.SCREEN_HEIGHT / 2f, 0);
        tiledRender = new OrthogonalTiledMapRenderer(Assets.map, Tablero.UNIT_SCALE);
        mapStaticLayer = (TiledMapTileLayer) tiledRender.getMap().getLayers().get("StaticMap");

    }

    public void render() {

        myCamera.update();
        tiledRender.setView(myCamera);
        tiledRender.getBatch().begin();
        tiledRender.renderTileLayer(mapStaticLayer);
        tiledRender.getBatch().end();
    }

}
