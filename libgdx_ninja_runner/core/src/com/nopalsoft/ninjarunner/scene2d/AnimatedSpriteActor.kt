package com.nopalsoft.ninjarunner.scene2d

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Actor
import com.nopalsoft.ninjarunner.AnimationSprite

class AnimatedSpriteActor(val animation: AnimationSprite) : Actor() {
    private var stateTime = 0f

    override fun act(delta: Float) {
        super.act(delta)
        stateTime += delta
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        val sprite: Sprite = animation.getKeyFrame(stateTime, true)
        sprite.setSize(width, height)
        sprite.setPosition(x, y)
        sprite.draw(batch)
    }
}