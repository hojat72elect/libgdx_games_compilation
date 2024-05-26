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
    @JvmField
    val region: TextureRegion,
    @JvmField
    val parallaxRatio: Vector2,
    @JvmField
    val startPosition: Vector2,
    @JvmField
    val padding: Vector2,
    @JvmField
    val width: Float,
    @JvmField
    val height: Float,
)
