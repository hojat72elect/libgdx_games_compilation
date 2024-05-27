package com.nopalsoft.ninjarunner.leaderboard

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.ninjarunner.Assets

class NextGoalFrame(x: Float, y: Float) : Group() {

    @JvmField
    var myPerson: Person? = null
    private val labelName = Label("", Assets.labelStyleSmall)
    private val labelPersonScore = Label("", Assets.labelStyleSmall)
    private val labelRemainingPointsToOvercome = Label("", Assets.labelStyleSmall)

    init {
        setBounds(x, y, WIDTH, HEIGHT)

        labelName.setFontScale(.5f)
        labelName.setPosition(60f, 60f)

        labelPersonScore.setFontScale(.5f)
        labelPersonScore.setPosition(60f, 40f)

        labelRemainingPointsToOvercome.setFontScale(.5f)
        labelRemainingPointsToOvercome.setPosition(60f, 20f)

        addActor(labelName)
        addActor(labelPersonScore)
        addActor(labelRemainingPointsToOvercome)

        debug()
    }

    /**
     * Puts a new person in the frame.
     */
    fun updatePerson(person: Person) {

        this.myPerson = person

        labelName.setText(myPerson?.name)
        labelPersonScore.setText(myPerson?.getScoreWithFormat())

        if (myPerson?.image != null) {
            setPicture(myPerson!!.image)
        } else {
            myPerson?.downloadImage(object : Person.DownloadImageCompleteListener {
                override fun imageDownloaded() {
                    setPicture(myPerson!!.image)
                }

                override fun imageDownloadFail() {
                    setPicture(Assets.photoNA)
                }
            })
        }

    }

    private fun setPicture(drawable: TextureRegionDrawable?) {
        /*
         * I use an image button because it can have a background and an image.
         */
        val personImage =
            ImageButton(ImageButtonStyle(drawable, null, null, Assets.photoFrame, null, null))
        personImage.setSize(50f, 50f)
        personImage.imageCell.size(50f)
        personImage.setPosition(5f, HEIGHT / 2f - personImage.height / 2f)
        addActor(personImage)
    }

    fun updateScore(score: Long) {
        if (myPerson != null) {
            labelRemainingPointsToOvercome.setText("Pa Ganar" + (myPerson!!.score - score))
        }
    }


    companion object {
        const val WIDTH = 170f
        const val HEIGHT = 80f
    }
}