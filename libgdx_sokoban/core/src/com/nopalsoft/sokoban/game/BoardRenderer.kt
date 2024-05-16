package com.nopalsoft.sokoban.game

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.nopalsoft.sokoban.Assets
import com.nopalsoft.sokoban.screens.Screens

class BoardRenderer {

    private var myCamera = OrthographicCamera(
        Screens.SCREEN_WIDTH.toFloat(),
        Screens.SCREEN_HEIGHT.toFloat()
    )
    private val tiledRender = OrthogonalTiledMapRenderer(
        Assets.map,
        Board.UNIT_SCALE
    )
    private val mapStaticLayer = tiledRender.map.layers["StaticMap"] as TiledMapTileLayer;


    init {
        myCamera.position.set(
            Screens.SCREEN_WIDTH / 2f,
            Screens.SCREEN_HEIGHT / 2f,
            0f
        )
    }

    fun render() {
        myCamera.update()
        tiledRender.setView(myCamera)
        tiledRender.batch.begin()
        tiledRender.renderTileLayer(mapStaticLayer)
        tiledRender.batch.end()
    }


}