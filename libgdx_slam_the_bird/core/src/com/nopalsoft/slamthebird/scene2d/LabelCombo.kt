package com.nopalsoft.slamthebird.scene2d

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.nopalsoft.slamthebird.Assets
import com.nopalsoft.slamthebird.game.WorldGame

class LabelCombo(x: Float, y: Float, var comboActual: Int) : Actor() {
    init {
        this.setPosition(x, y)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(Assets.combo, x, y, 65f, 27f)
        if (comboActual >= WorldGame.COMBO_TO_START_GETTING_COINS) {
            batch.draw(Assets.coin, x + 20, y + 35, 23f, 26f)
        }
        drawPuntuacionChicoOrigenIzq(
            batch, this.x + 70, this.y + 2,
            comboActual
        )
    }

    fun drawPuntuacionChicoOrigenIzq(
        batcher: Batch, x: Float, y: Float,
        comboActual: Int
    ) {
        val score = comboActual.toString()

        val len = score.length
        var charWidth: Float
        var textWidth = 0f
        for (i in 0 until len) {
            var keyFrame: AtlasRegion?

            charWidth = 22f
            val character = score[i]

            if (character == '0') {
                keyFrame = Assets.num0Small
            } else if (character == '1') {
                keyFrame = Assets.num1Small
                charWidth = 11f
            } else if (character == '2') {
                keyFrame = Assets.num2Small
            } else if (character == '3') {
                keyFrame = Assets.num3Small
            } else if (character == '4') {
                keyFrame = Assets.num4Small
            } else if (character == '5') {
                keyFrame = Assets.num5Small
            } else if (character == '6') {
                keyFrame = Assets.num6Small
            } else if (character == '7') {
                keyFrame = Assets.num7Small
            } else if (character == '8') {
                keyFrame = Assets.num8Small
            } else { // 9
                keyFrame = Assets.num9Small
            }

            batcher.draw(keyFrame, x + textWidth, y, charWidth, 22f)
            textWidth += charWidth
        }
    }
}
