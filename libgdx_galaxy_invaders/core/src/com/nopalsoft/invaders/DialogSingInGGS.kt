package com.nopalsoft.invaders

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener

class DialogSingInGGS(val game: MainInvaders, private var stage: Stage) {
    var dialogSignIn: Dialog? = null
    var dialogRate: Dialog? = null

    fun showDialogSignIn() {
        dialogSignIn = Dialog(Assets.languages["sign_in"], Assets.styleDialogPause)
        val labelContent = Label(
            Assets.languages["sign_in_with_google_to_share_your_scores_and_achievements_with_your_friends"],
            Assets.styleLabelDialog
        )
        labelContent.wrap = true

        dialogSignIn!!.contentTable.add(labelContent).width(300f).height(120f)

        val style =
            TextButtonStyle(Assets.buttonSignInUp, Assets.buttonSignInDown, null, Assets.font15)
        val buttonSignIn = TextButton(Assets.languages["sign_in"], style)
        buttonSignIn.label.wrap = true
        val buttonNotNow = TextButton(Assets.languages["not_now"], Assets.styleTextButton)

        buttonNotNow.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                dialogSignIn!!.hide()
            }
        })

        buttonSignIn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                // TODO: Handle login
                dialogSignIn!!.hide()
            }
        })

        dialogSignIn!!.buttonTable.add(buttonSignIn).minWidth(140f).fill()
        dialogSignIn!!.buttonTable.add(buttonNotNow).minWidth(140f).fill()
        dialogSignIn!!.show(stage)
    }

    fun showDialogRate() {
        dialogRate = Dialog(Assets.languages["please_rate_the_app"], Assets.styleDialogPause)
        val lblContenido = Label(
            Assets.languages["thank_you_for_playing_if_you_like_this_game_please"],
            Assets.styleLabelDialog
        )
        lblContenido.wrap = true

        dialogRate!!.contentTable.add(lblContenido).width(300f).height(150f)

        val rate = TextButton(Assets.languages["rate"], Assets.styleTextButton)
        val btNotNow = TextButton(Assets.languages["not_now"], Assets.styleTextButton)
        rate.height = 10f

        btNotNow.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                dialogRate!!.hide()
            }
        })

        rate.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                //TODO: Open appstore
                dialogRate!!.hide()
            }
        })

        dialogRate!!.buttonTable.add(rate).minWidth(140f).minHeight(40f).fill()
        dialogRate!!.buttonTable.add(btNotNow).minWidth(140f).minHeight(40f).fill()
        dialogRate!!.show(stage)
    }

    val isDialogShown: Boolean
        get() = stage.actors.contains(dialogRate, true) || stage.actors.contains(dialogSignIn, true)

    fun dismissAll() {
        if (stage.actors.contains(dialogRate, true)) dialogRate!!.hide()

        if (stage.actors.contains(dialogSignIn, true)) dialogSignIn!!.hide()
    }
}
