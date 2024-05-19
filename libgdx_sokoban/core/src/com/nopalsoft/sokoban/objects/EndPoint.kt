package com.nopalsoft.sokoban.objects

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.nopalsoft.sokoban.Assets

class EndPoint(position: Int, color: String) : Tiles(position) {
    @JvmField
    var numColor: Int = 0

    private lateinit var keyFrame: AtlasRegion

    init {
        when (color) {
            "brown" -> numColor = Box.COLOR_BROWN
            "gray" -> numColor = Box.COLOR_GRAY
            "purple" -> numColor = Box.COLOR_PURPLE
            "blue" -> numColor = Box.COLOR_BLUE
            "black" -> numColor = Box.COLOR_BLACK
            "beige" -> numColor = Box.COLOR_BEIGE
            "yellow" -> numColor = Box.COLOR_YELLOW
            "red" -> numColor = Box.COLOR_RED
        }
        setTextureColor(numColor)
    }

    private fun setTextureColor(numColor: Int) {
        when (numColor) {
            Box.COLOR_BEIGE -> keyFrame = Assets.endPointBeige!!
            Box.COLOR_BLACK -> keyFrame = Assets.endPointBlack!!
            Box.COLOR_BLUE -> keyFrame = Assets.endPointBlue!!
            Box.COLOR_BROWN -> keyFrame = Assets.endPointBrown!!
            Box.COLOR_GRAY -> keyFrame = Assets.endPointGray!!
            Box.COLOR_RED -> keyFrame = Assets.endPointRed!!
            Box.COLOR_YELLOW -> keyFrame = Assets.endPointYellow!!
            Box.COLOR_PURPLE -> keyFrame = Assets.endPointPurple!!
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(keyFrame, x, y, SIZE, SIZE)
    }
}
