/* This file is part of Rectball
 * Copyright (C) 2015-2024  Dani Rodríguez
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package es.danirod.rectball.scene2d.game

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Scaling
import es.danirod.rectball.model.Ball
import es.danirod.rectball.model.BallColor
import java.util.Locale

class BallActor(val ball: Ball, atlas: TextureAtlas) : Image() {
    private val grayDrawable = TextureRegionDrawable(atlas.findRegion("ball_gray"))

    private val colorDrawables = BallColor.values().associateWith {
        val region = "ball_${it.toString().lowercase(Locale.ROOT)}"
        TextureRegionDrawable(atlas.findRegion(region))
    }

    init {
        setScaling(Scaling.fit)
        drawable = grayDrawable
    }

    fun setColoured(coloured: Boolean) {
        drawable = if (coloured) {
            colorDrawables[ball.color]
        } else {
            grayDrawable
        }
    }

    fun syncColor() {
        drawable = colorDrawables[ball.color]
    }

    override fun sizeChanged() {
        super.sizeChanged()
        setOrigin(width / 2, height / 2)
    }
}
