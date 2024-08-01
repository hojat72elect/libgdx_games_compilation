package com.nopalsoft.invaders.frame

import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

open class GameObject {
    val position: Vector2
    val boundsRectangle: Rectangle?
    var boundsCircle: Circle?


    constructor(x: Float, y: Float, width: Float, height: Float) {
        this.position = Vector2(x, y)
        this.boundsRectangle = Rectangle(x - width / 2, y - height / 2, width, height)
        boundsCircle = null
    }

    constructor(x: Float, y: Float, radio: Float) {
        this.position = Vector2(x, y)
        this.boundsRectangle = null
        boundsCircle = Circle(x, y, radio)
    }
}
