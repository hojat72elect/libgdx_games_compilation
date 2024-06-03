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

    private lateinit var fontSmall: BitmapFont
    private lateinit var fontBig: BitmapFont

    lateinit var background: AtlasRegion
    lateinit var settings: TextureRegionDrawable

    // Player
    lateinit var characterJump: AtlasRegion
    lateinit var characterStand: AtlasRegion

    lateinit var coin: AtlasRegion
    lateinit var gun: AtlasRegion
    lateinit var bullet: AtlasRegion

    lateinit var bubbleSmall: AtlasRegion
    lateinit var jetpackSmall: AtlasRegion
    lateinit var bubble: AtlasRegion
    lateinit var jetpack: AtlasRegion
    lateinit var jetpackFire: Animation<AtlasRegion>

    lateinit var enemy: Animation<AtlasRegion>

    lateinit var cloudHappy: AtlasRegion
    lateinit var cloudAngry: AtlasRegion

    lateinit var ray: Animation<AtlasRegion>
    lateinit var cloudWind: AtlasRegion

    // Platforms
    lateinit var platformBeige: AtlasRegion
    lateinit var platformBeigeLight: AtlasRegion
    lateinit var platformBeigeBroken: AtlasRegion
    lateinit var platformBeigeLeft: AtlasRegion
    lateinit var platformBeigeRight: AtlasRegion
    lateinit var platformBlue: AtlasRegion
    lateinit var platformBlueLight: AtlasRegion
    lateinit var platformBlueBroken: AtlasRegion
    lateinit var platformBlueLeft: AtlasRegion
    lateinit var platformBlueRight: AtlasRegion

    lateinit var platformGray: AtlasRegion
    lateinit var platformGrayLight: AtlasRegion
    lateinit var platformGrayBroken: AtlasRegion
    lateinit var platformGrayLeft: AtlasRegion
    lateinit var platformGrayRight: AtlasRegion
    lateinit var platformGreen: AtlasRegion
    lateinit var platformGreenLight: AtlasRegion
    lateinit var platformGreenBroken: AtlasRegion
    lateinit var platformGreenLeft: AtlasRegion
    lateinit var platformGreenRight: AtlasRegion

    lateinit var platformMulticolor: AtlasRegion
    lateinit var platformMulticolorLight: AtlasRegion
    lateinit var platformMulticolorBroken: AtlasRegion
    lateinit var platformMulticolorLeft: AtlasRegion
    lateinit var platformMulticolorRight: AtlasRegion

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
        Animation(.5f, walk1, walk2)

        coin = atlas.findRegion("Coin")
        gun = atlas.findRegion("Pistol")
        bullet = atlas.findRegion("Bullet")
        atlas.findRegion("Spring")

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