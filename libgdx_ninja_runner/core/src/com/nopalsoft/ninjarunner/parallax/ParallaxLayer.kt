package com.nopalsoft.ninjarunner.parallax

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2

/**
 * @param region the TextureRegion to draw, it can be any width/height.
 * @param parallaxRatio the relative speed of x and y.
 * @param startPosition the initial position of x and y.
 * @param padding The padding of the region at x and y.
 * @param width N/A.
 * @param height N/A.
 */

data class ParallaxLayer(
    val region: TextureRegion,
    val parallaxRatio: Vector2,
    val startPosition: Vector2,
    val padding: Vector2,
    val width: Float,
    val height: Float,
)
