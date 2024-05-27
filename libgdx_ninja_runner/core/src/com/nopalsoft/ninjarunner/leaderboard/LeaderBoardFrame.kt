package com.nopalsoft.ninjarunner.leaderboard

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.ninjarunner.Assets
import com.nopalsoft.ninjarunner.leaderboard.Person.AccountType
import com.nopalsoft.ninjarunner.leaderboard.Person.DownloadImageCompleteListener

class LeaderBoardFrame(player: Person) : Table() {
    private val labelName = Label(player.name, Assets.labelStyleSmall)
    private val labelScore =
        Label(player.scoreWithFormat, Label.LabelStyle(Assets.fontSmall, Color.RED))
    private val tableAux =
        Table()//It is necessary because on the left side there is a photo and on the right
    // side there are several textFields in lines

    // I use an image button because it can have a background and an image.
    private var personImage: ImageButton? = null

    init {
        background = Assets.backgroundItemShop
        pad(5f)

        tableAux.left()
        tableAux.defaults().left()
        tableAux.add(labelName).row()
        tableAux.add(labelScore).row()

        val imageSocialNetwork: Image? = when (player.accountType) {
            AccountType.GOOGLE_PLAY -> Image(Assets.imageGoogle)
            AccountType.AMAZON -> Image(Assets.imageAmazon)
            AccountType.FACEBOOK -> Image(Assets.imageFacebook)
        }
        tableAux.add(imageSocialNetwork).size(25f).row()


        if (player.image != null) {
            setPicture(player.image)
        } else {

            player.downloadImage(object : DownloadImageCompleteListener {
                override fun imageDownloaded() {
                    setPicture(player.image)
                }

                override fun imageDownloadFail() {
                    setPicture(Assets.photoNA)
                }
            })
        }
        refresh() //So that I can put the info later. If I delete it until the photo is posted, the info is posted.
    }

    fun setPicture(drawable: TextureRegionDrawable) {
        personImage =
            ImageButton(ImageButtonStyle(drawable, null, null, Assets.photoFrame, null, null))
        refresh()
    }

    private fun refresh() {
        clear()
        val size = 100f
        if (personImage != null) {
            personImage!!.imageCell.size(size)
            add(personImage).size(size)
        } else {
            add().size(size)
        }

        add(tableAux).padLeft(20f).expandX().fill()
    }

}
