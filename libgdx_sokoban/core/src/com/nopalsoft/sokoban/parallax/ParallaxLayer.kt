package com.nopalsoft.sokoban.parallax

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2

/**
 * @param region The TextureRegion to draw, this can of be any width/height.
 * @param parallaxRatio The relative speed of x,y.
 * @param startPosition The initial position of x, y.
 * @param padding The padding of the region at x, y.
 */
data class ParallaxLayer(
    val region: TextureRegion,
    val parallaxRatio: Vector2,
    val startPosition: Vector2,
    val padding: Vector2,
    val width: Float,
    val height: Float,
)
