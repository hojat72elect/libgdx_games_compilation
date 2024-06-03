package com.nopalsoft.superjumper

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

object Assets {

    private var fontSmall: BitmapFont? = null
    private var fontBig: BitmapFont? = null

    var background: AtlasRegion? = null
    var settings: TextureRegionDrawable? = null

    // Player
    var characterJump: AtlasRegion? = null
    var characterStand: AtlasRegion? = null
    private var characterWalk: Animation<AtlasRegion>? = null

    var coin: AtlasRegion? = null
    var gun: AtlasRegion? = null
    var bullet: AtlasRegion? = null
    private var spring: AtlasRegion? = null
    var bubbleSmall: AtlasRegion? = null
    var jetpackSmall: AtlasRegion? = null
    var bubble: AtlasRegion? = null
    var jetpack: AtlasRegion? = null
    var jetpackFire: Animation<AtlasRegion>? = null

    var enemy: Animation<AtlasRegion>? = null

    var cloudHappy: AtlasRegion? = null
    var cloudAngry: AtlasRegion? = null

    var ray: Animation<AtlasRegion>? = null
    var cloudWind: AtlasRegion? = null

    // Platforms
    var platformBeige: AtlasRegion? = null
    var platformBeigeLight: AtlasRegion? = null
    var platformBeigeBroken: AtlasRegion? = null
    var platformBeigeLeft: AtlasRegion? = null
    var platformBeigeRight: AtlasRegion? = null

    var platformBlue: AtlasRegion? = null
    var platformBlueLight: AtlasRegion? = null
    var platformBlueBroken: AtlasRegion? = null
    var platformBlueLeft: AtlasRegion? = null
    var platformBlueRight: AtlasRegion? = null

    var platformGray: AtlasRegion? = null
    var platformGrayLight: AtlasRegion? = null
    var platformGrayBroken: AtlasRegion? = null
    var platformGrayLeft: AtlasRegion? = null
    var platformGrayRight: AtlasRegion? = null
    var platformGreen: AtlasRegion? = null
    var platformGreenLight: AtlasRegion? = null
    var platformGreenBroken: AtlasRegion? = null
    var platformGreenLeft: AtlasRegion? = null
    var platformGreenRight: AtlasRegion? = null

    var platformMulticolor: AtlasRegion? = null
    var platformMulticolorLight: AtlasRegion? = null
    var platformMulticolorBroken: AtlasRegion? = null
    var platformMulticolorLeft: AtlasRegion? = null
    var platformMulticolorRight: AtlasRegion? = null

    var platformPink: AtlasRegion? = null
    var platformPinkLight: AtlasRegion? = null
    var platformPinkBroken: AtlasRegion? = null
    var platformPinkLeft: AtlasRegion? = null
    var platformPinkRight: AtlasRegion? = null

    var buttonPause: TextureRegionDrawable? = null

    var labelStyleSmall: LabelStyle? = null
    var labelStyleBig: LabelStyle? = null
    var textButtonStyleBig: TextButtonStyle? = null

    var pixelBlack: NinePatchDrawable? = null


    private fun loadStyles(atlas: TextureAtlas) {
        // Label Style
        labelStyleSmall = LabelStyle(fontSmall, Color.WHITE)
        labelStyleBig = LabelStyle(fontBig, Color.WHITE)

        val button = TextureRegionDrawable(atlas.findRegion("button"))
        textButtonStyleBig = TextButtonStyle(button, button, null, fontBig)

        pixelBlack = NinePatchDrawable(NinePatch(atlas.findRegion("pixelNegro"), 1, 1, 0, 0))
    }


    fun load() {
        val atlas = TextureAtlas(Gdx.files.internal("data/atlasMap.txt"))

        fontSmall =
            BitmapFont(Gdx.files.internal("data/fontGrande.fnt"), atlas.findRegion("fontGrande"))
        fontBig =
            BitmapFont(Gdx.files.internal("data/fontGrande.fnt"), atlas.findRegion("fontGrande"))

        loadStyles(atlas)

        buttonPause = TextureRegionDrawable(atlas.findRegion("btPause"))

        background = atlas.findRegion("Background")
        settings = TextureRegionDrawable(atlas.findRegion("titulo"))


        characterJump = atlas.findRegion("personajeJump")
        characterStand = atlas.findRegion("personajeStand")

        val walk1 = atlas.findRegion("personajeWalk1")
        val walk2 = atlas.findRegion("personajeWalk2")
        characterWalk = Animation(.5f, walk1, walk2)

        coin = atlas.findRegion("Coin")
        gun = atlas.findRegion("Pistol")
        bullet = atlas.findRegion("Bullet")
        spring = atlas.findRegion("Spring")
        bubbleSmall = atlas.findRegion("Bubble_Small")
        jetpackSmall = atlas.findRegion("Jetpack_Small")
        bubble = atlas.findRegion("Bubble_Big")
        jetpack = atlas.findRegion("Jetpack_Big")

        val jetpackFire1 = atlas.findRegion("JetFire1")
        val jetpackFire2 = atlas.findRegion("JetFire2")
        jetpackFire = Animation(.085f, jetpackFire1, jetpackFire2)

        val enemigo1 = atlas.findRegion("HearthEnemy1")
        val enemigo2 = atlas.findRegion("HearthEnemy2")
        enemy = Animation(.2f, enemigo1, enemigo2)

        cloudHappy = atlas.findRegion("HappyCloud")
        cloudAngry = atlas.findRegion("AngryCloud")
        cloudWind = atlas.findRegion("CloudWind")

        val lightning1 = atlas.findRegion("Lightning1")
        val lightning2 = atlas.findRegion("Lightning2")
        ray = Animation(.08f, lightning1, lightning2)


        platformBeige = atlas.findRegion("LandPiece_DarkBeige")
        platformBeigeLight = atlas.findRegion("LandPiece_LightBeige")
        platformBeigeBroken = atlas.findRegion("BrokenLandPiece_Beige")
        platformBeigeLeft = atlas.findRegion("HalfLandPiece_Left_Beige")
        platformBeigeRight = atlas.findRegion("HalfLandPiece_Right_Beige")

        platformBlue = atlas.findRegion("LandPiece_DarkBlue")
        platformBlueLight = atlas.findRegion("LandPiece_LightBlue")
        platformBlueBroken = atlas.findRegion("BrokenLandPiece_Blue")
        platformBlueLeft = atlas.findRegion("HalfLandPiece_Left_Blue")
        platformBlueRight = atlas.findRegion("HalfLandPiece_Right_Blue")

        platformGray = atlas.findRegion("LandPiece_DarkGray")
        platformGrayLight = atlas.findRegion("LandPiece_LightGray")
        platformGrayBroken = atlas.findRegion("BrokenLandPiece_Gray")
        platformGrayLeft = atlas.findRegion("HalfLandPiece_Left_Gray")
        platformGrayRight = atlas.findRegion("HalfLandPiece_Right_Gray")

        platformGreen = atlas.findRegion("LandPiece_DarkGreen")
        platformGreenLight = atlas.findRegion("LandPiece_LightGreen")
        platformGreenBroken = atlas.findRegion("BrokenLandPiece_Green")
        platformGreenLeft = atlas.findRegion("HalfLandPiece_Left_Green")
        platformGreenRight = atlas.findRegion("HalfLandPiece_Right_Green")

        platformMulticolor = atlas.findRegion("LandPiece_DarkMulticolored")
        platformMulticolorLight = atlas.findRegion("LandPiece_LightMulticolored")
        platformMulticolorBroken = atlas.findRegion("BrokenLandPiece_Multicolored")
        platformMulticolorLeft = atlas.findRegion("HalfLandPiece_Left_Multicolored")
        platformMulticolorRight = atlas.findRegion("HalfLandPiece_Right_Multicolored")

        platformPink = atlas.findRegion("LandPiece_DarkPink")
        platformPinkLight = atlas.findRegion("LandPiece_LightPink")
        platformPinkBroken = atlas.findRegion("BrokenLandPiece_Pink")
        platformPinkLeft = atlas.findRegion("HalfLandPiece_Left_Pink")
        platformPinkRight = atlas.findRegion("HalfLandPiece_Right_Pink")
    }

}