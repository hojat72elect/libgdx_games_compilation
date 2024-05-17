package com.nopalsoft.sokoban.objects

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.nopalsoft.sokoban.Assets
import kotlin.math.abs

class Box(position: Int, color: String) : Tiles(position) {
    @JvmField
    var isInRightEndPoint = false
    private var numColor = when (color) {
        "brown" -> COLOR_BROWN
        "gray" -> COLOR_GRAY
        "purple" -> COLOR_PURPLE
        "blue" -> COLOR_BLUE
        "black" -> COLOR_BLACK
        "beige" -> COLOR_BEIGE
        "yellow" -> COLOR_YELLOW
        "red" -> COLOR_RED
        else -> {
            throw Exception("The provided color is not known")
        }
    }
    private lateinit var keyFrame: AtlasRegion

    init {
        setTextureColor(numColor)
    }

    private fun setTextureColor(numColor: Int) {
        keyFrame = when (numColor) {
            COLOR_BEIGE -> Assets.beigeBox
            COLOR_DARK_BEIGE -> Assets.darkBeigeBox
            COLOR_BLACK -> Assets.blackBox
            COLOR_DARK_BLACK -> Assets.darkBlackBox
            COLOR_BLUE -> Assets.blueBox
            COLOR_DARK_BLUE -> Assets.darkBlueBox
            COLOR_BROWN -> Assets.brownBox
            COLOR_DARK_BROWN -> Assets.darkBrownBox
            COLOR_GRAY -> Assets.grayBox
            COLOR_DARK_GRAY -> Assets.darkGrayBox
            COLOR_RED -> Assets.redBox
            COLOR_DARK_RED -> Assets.darkRedBox
            COLOR_YELLOW -> Assets.yellowBox
            COLOR_DARK_YELLOW -> Assets.darkYellowBox
            COLOR_PURPLE -> Assets.purpleBox
            COLOR_DARK_PURPLE -> Assets.darkPurpleBox
            else -> {
                throw Exception("The provided color is not known")
            }
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(keyFrame, x, y, SIZE, SIZE)
    }

    fun setIsInEndPoint(endPoint: EndPoint) {
        numColor = abs(numColor)
        isInRightEndPoint = false
        if (endPoint.numColor == numColor) {
            numColor = -numColor
            isInRightEndPoint = true
        }
        setTextureColor(numColor)
    }

    companion object {
        const val COLOR_BEIGE = 1
        const val COLOR_DARK_BEIGE = -1
        const val COLOR_BLACK = 2
        const val COLOR_DARK_BLACK = -2
        const val COLOR_BLUE = 3
        const val COLOR_DARK_BLUE = -3
        const val COLOR_BROWN = 4
        const val COLOR_DARK_BROWN = -4
        const val COLOR_GRAY = 5
        const val COLOR_DARK_GRAY = -5
        const val COLOR_RED = 6
        const val COLOR_DARK_RED = -6
        const val COLOR_YELLOW = 7
        const val COLOR_DARK_YELLOW = -7
        const val COLOR_PURPLE = 8
        const val COLOR_DARK_PURPLE = -8
    }
}