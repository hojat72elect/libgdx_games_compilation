package com.nopalsoft.slamthebird.scene2d

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.nopalsoft.slamthebird.Assets

class LabelScore(x: Float, y: Float, var puntuacion: Int) : Actor() {
    init {
        this.setPosition(x, y)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        drawNumGrandeCentradoX(batch, this.x, this.y, puntuacion)
    }

    fun drawNumGrandeCentradoX(batcher: Batch, x: Float, y: Float, puntuacion: Int) {
        val score = puntuacion.toString()

        val len = score.length
        val charWidth = 42f
        val textWidth = len * charWidth
        for (i in 0 until len) {
            var keyFrame: AtlasRegion?

            val character = score[i]

            keyFrame = if (character == '0') {
                Assets.num0Big
            } else if (character == '1') {
                Assets.num1Big
            } else if (character == '2') {
                Assets.num2Big
            } else if (character == '3') {
                Assets.num3Big
            } else if (character == '4') {
                Assets.num4Big
            } else if (character == '5') {
                Assets.num5Big
            } else if (character == '6') {
                Assets.num6Big
            } else if (character == '7') {
                Assets.num7Big
            } else if (character == '8') {
                Assets.num8Big
            } else { // 9
                Assets.num9Big
            }
            batcher.draw(keyFrame, x + ((charWidth - 1f) * i) - textWidth / 2f, y, charWidth, 64f)
        }
    }
}
