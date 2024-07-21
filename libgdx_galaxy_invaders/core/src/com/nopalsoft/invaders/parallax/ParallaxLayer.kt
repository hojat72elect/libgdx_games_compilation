package com.nopalsoft.invaders.parallax

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2

/**
 * @param region The TextureRegion to draw , this can be of any width/height.
 * @param parallaxRatio The relative speed of x,y [ParallaxBackground].
 * @param startPosition The initial position of x,y.
 * @param padding The padding of the region at x,y.
 */
class ParallaxLayer(
    @JvmField var region: TextureRegion,
    @JvmField var parallaxRatio: Vector2?,
    @JvmField var startPosition: Vector2,
    @JvmField var padding: Vector2?
) {
    @JvmField
    var width: Float

    @JvmField
    var height: Float


    constructor(
        region: TextureRegion,
        parallaxRatio: Vector2?,
        padding: Vector2?
    ) : this(
        region,
        parallaxRatio,
        Vector2(0f, 0f),
        padding
    ) {
        this.width = region.regionWidth.toFloat()
        this.height = region.regionHeight.toFloat()
    }

    init {
        this.width = region.regionWidth.toFloat()
        this.height = region.regionHeight.toFloat()
    }
}
