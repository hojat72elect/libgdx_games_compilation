package com.nopalsoft.sokoban

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.AtlasTmxMapLoader
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.sokoban.Settings.isTest
import com.nopalsoft.sokoban.parallax.ParallaxBackground
import com.nopalsoft.sokoban.parallax.ParallaxLayer

object Assets {
    var font: BitmapFont? = null
    var fontRed: BitmapFont? = null

    var background: ParallaxBackground? = null
    var blackPixel: NinePatchDrawable? = null

    var map: TiledMap? = null

    var backgroundMoves: TextureRegionDrawable? = null
    var backgroundTime: TextureRegionDrawable? = null

    var buttonRight: TextureRegionDrawable? = null
    var buttonRightPressed: TextureRegionDrawable? = null
    var buttonLeft: TextureRegionDrawable? = null
    var buttonLeftPressed: TextureRegionDrawable? = null
    var buttonUp: TextureRegionDrawable? = null
    var buttonUpPressed: TextureRegionDrawable? = null
    var buttonDown: TextureRegionDrawable? = null
    var buttonDownPressed: TextureRegionDrawable? = null
    var buttonRefresh: TextureRegionDrawable? = null
    var buttonRefreshPressed: TextureRegionDrawable? = null
    var buttonPause: TextureRegionDrawable? = null
    var buttonPausePressed: TextureRegionDrawable? = null
    var buttonLeaderboard: TextureRegionDrawable? = null
    var buttonLeaderboardPressed: TextureRegionDrawable? = null
    var buttonAchievement: TextureRegionDrawable? = null
    var buttonAchievementPressed: TextureRegionDrawable? = null
    var buttonFacebook: TextureRegionDrawable? = null
    var buttonFacebookPressed: TextureRegionDrawable? = null
    var buttonSettings: TextureRegionDrawable? = null
    var buttonSettingsPressed: TextureRegionDrawable? = null
    var buttonMore: TextureRegionDrawable? = null
    var buttonMorePressed: TextureRegionDrawable? = null
    var buttonClose: TextureRegionDrawable? = null
    var buttonClosePressed: TextureRegionDrawable? = null
    var buttonHome: TextureRegionDrawable? = null
    var buttonHomePressed: TextureRegionDrawable? = null
    var buttonOff: TextureRegionDrawable? = null
    var buttonOn: TextureRegionDrawable? = null
    var buttonPlay: TextureRegionDrawable? = null
    var buttonPlayPressed: TextureRegionDrawable? = null

    var levelOff: TextureRegionDrawable? = null
    var levelStar: TextureRegionDrawable? = null

    var clock: TextureRegionDrawable? = null

    var beigeBox: AtlasRegion? = null
    var darkBeigeBox: AtlasRegion? = null
    var blackBox: AtlasRegion? = null
    var darkBlackBox: AtlasRegion? = null
    var blueBox: AtlasRegion? = null
    var darkBlueBox: AtlasRegion? = null
    var brownBox: AtlasRegion? = null
    var darkBrownBox: AtlasRegion? = null
    var grayBox: AtlasRegion? = null
    var darkGrayBox: AtlasRegion? = null
    var purpleBox: AtlasRegion? = null
    var darkPurpleBox: AtlasRegion? = null
    var redBox: AtlasRegion? = null
    var darkRedBox: AtlasRegion? = null
    var yellowBox: AtlasRegion? = null
    var darkYellowBox: AtlasRegion? = null

    var endPointBeige: AtlasRegion? = null
    var endPointBlack: AtlasRegion? = null
    var endPointBlue: AtlasRegion? = null
    var endPointBrown: AtlasRegion? = null
    var endPointGray: AtlasRegion? = null
    var endPointPurple: AtlasRegion? = null
    var endPointRed: AtlasRegion? = null
    var endPointYellow: AtlasRegion? = null

    var characterGoingUp: Animation<TextureRegion>? = null
    var characterGoingDown: Animation<TextureRegion>? = null
    var characterGoingLeft: Animation<TextureRegion>? = null
    var characterGoingRight: Animation<TextureRegion>? = null
    var characterStand: AtlasRegion? = null
    var backgroundWindow: TextureRegionDrawable? = null
    var styleTextButtonLevel: TextButtonStyle? = null
    var styleTextButtonLevelLocked: TextButtonStyle? = null
    var atlas: TextureAtlas? = null

    fun load() {
        atlas = TextureAtlas(Gdx.files.internal("data/atlasMap.txt"))

        font = BitmapFont(Gdx.files.internal("data/font32.fnt"), atlas!!.findRegion("UI/font32"))
        fontRed =
            BitmapFont(Gdx.files.internal("data/font32Red.fnt"), atlas!!.findRegion("UI/font32Red"))

        loadUI()

        blackPixel = NinePatchDrawable(
            NinePatch(
                atlas!!.findRegion("pixelNegro"), 1, 1, 0, 0
            )
        )

        backgroundWindow = TextureRegionDrawable(atlas!!.findRegion("UI/backgroundVentana"))

        beigeBox = atlas!!.findRegion("cajaBeige")
        darkBeigeBox = atlas!!.findRegion("cajaDarkBeige")
        blackBox = atlas!!.findRegion("cajaBlack")
        darkBlackBox = atlas!!.findRegion("cajaDarkBlack")
        blueBox = atlas!!.findRegion("cajaBlue")
        darkBlueBox = atlas!!.findRegion("cajaDarkBlue")
        brownBox = atlas!!.findRegion("cajaBrown")
        darkBrownBox = atlas!!.findRegion("cajaDarkBrown")
        grayBox = atlas!!.findRegion("cajaGray")
        darkGrayBox = atlas!!.findRegion("cajaDarkGray")
        purpleBox = atlas!!.findRegion("cajaPurple")
        darkPurpleBox = atlas!!.findRegion("cajaDarkPurple")
        redBox = atlas!!.findRegion("cajaRed")
        darkRedBox = atlas!!.findRegion("cajaDarkRed")
        yellowBox = atlas!!.findRegion("cajaYellow")
        darkYellowBox = atlas!!.findRegion("cajaDarkYellow")

        endPointBeige = atlas!!.findRegion("endPointBeige")
        endPointBlack = atlas!!.findRegion("endPointBlack")
        endPointBlue = atlas!!.findRegion("endPointBlue")
        endPointBrown = atlas!!.findRegion("endPointBrown")
        endPointGray = atlas!!.findRegion("endPointGray")
        endPointPurple = atlas!!.findRegion("endPointPurple")
        endPointRed = atlas!!.findRegion("endPointRed")
        endPointYellow = atlas!!.findRegion("endPointYellow")

        characterStand = atlas!!.findRegion("Character4")

        val up1 = atlas!!.findRegion("Character7")
        val up2 = atlas!!.findRegion("Character8")
        val up3 = atlas!!.findRegion("Character9")
        characterGoingUp = Animation(.09f, up2, up3, up1)

        val down1 = atlas!!.findRegion("Character4")
        val down2 = atlas!!.findRegion("Character5")
        val down3 = atlas!!.findRegion("Character6")
        characterGoingDown = Animation(.09f, down2, down3, down1)

        val right1 = atlas!!.findRegion("Character2")
        val right2 = atlas!!.findRegion("Character3")
        characterGoingRight = Animation(.09f, right1, right2, right1)

        val left1 = atlas!!.findRegion("Character1")
        val left2 = atlas!!.findRegion("Character10")
        characterGoingLeft = Animation(.09f, left1, left2, left1)

        val royalBackgroundFlip = atlas!!.findRegion("backgroundFlip")
        royalBackgroundFlip.flip(true, false)
        val background = ParallaxLayer(
            atlas!!.findRegion("background"),
            Vector2(1f, 0f),
            Vector2(0f, 0f),
            Vector2(798f, 480f),
            800f,
            480f
        )
        val backgroundFlip = ParallaxLayer(
            royalBackgroundFlip,
            Vector2(1f, 0f),
            Vector2(799f, 0f),
            Vector2(798f, 480f),
            800f,
            480f
        )
        Assets.background =
            ParallaxBackground(arrayOf(background, backgroundFlip), 800f, 480f, Vector2(20f, 0f))
    }

    private fun loadUI() {
        buttonRight = TextureRegionDrawable(atlas!!.findRegion("UI/btDer"))
        buttonRightPressed = TextureRegionDrawable(atlas!!.findRegion("UI/btDerPress"))
        buttonLeft = TextureRegionDrawable(atlas!!.findRegion("UI/btIzq"))
        buttonLeftPressed = TextureRegionDrawable(atlas!!.findRegion("UI/btIzqPress"))
        buttonUp = TextureRegionDrawable(atlas!!.findRegion("UI/btUp"))
        buttonUpPressed = TextureRegionDrawable(atlas!!.findRegion("UI/btUpPress"))
        buttonDown = TextureRegionDrawable(atlas!!.findRegion("UI/btDown"))
        buttonDownPressed = TextureRegionDrawable(atlas!!.findRegion("UI/btDownPress"))
        buttonRefresh = TextureRegionDrawable(atlas!!.findRegion("UI/btRefresh"))
        buttonRefreshPressed = TextureRegionDrawable(atlas!!.findRegion("UI/btRefreshPress"))
        buttonPause = TextureRegionDrawable(atlas!!.findRegion("UI/btPausa"))
        buttonPausePressed = TextureRegionDrawable(atlas!!.findRegion("UI/btPausaPress"))
        buttonLeaderboard = TextureRegionDrawable(atlas!!.findRegion("UI/btLeaderboard"))
        buttonLeaderboardPressed =
            TextureRegionDrawable(atlas!!.findRegion("UI/btLeaderboardPress"))
        buttonAchievement = TextureRegionDrawable(atlas!!.findRegion("UI/btAchievement"))
        buttonAchievementPressed =
            TextureRegionDrawable(atlas!!.findRegion("UI/btAchievementPress"))
        buttonFacebook = TextureRegionDrawable(atlas!!.findRegion("UI/btFacebook"))
        buttonFacebookPressed = TextureRegionDrawable(atlas!!.findRegion("UI/btFacebookPress"))
        buttonSettings = TextureRegionDrawable(atlas!!.findRegion("UI/btSettings"))
        buttonSettingsPressed = TextureRegionDrawable(atlas!!.findRegion("UI/btSettingsPress"))
        buttonMore = TextureRegionDrawable(atlas!!.findRegion("UI/btMas"))
        buttonMorePressed = TextureRegionDrawable(atlas!!.findRegion("UI/btMasPress"))
        buttonClose = TextureRegionDrawable(atlas!!.findRegion("UI/btClose"))
        buttonClosePressed = TextureRegionDrawable(atlas!!.findRegion("UI/btClosePress"))
        buttonHome = TextureRegionDrawable(atlas!!.findRegion("UI/btHome"))
        buttonHomePressed = TextureRegionDrawable(atlas!!.findRegion("UI/btHomePress"))
        buttonOff = TextureRegionDrawable(atlas!!.findRegion("UI/btOff"))
        buttonOn = TextureRegionDrawable(atlas!!.findRegion("UI/btOn"))

        buttonPlay = TextureRegionDrawable(atlas!!.findRegion("UI/btPlay"))
        buttonPlayPressed = TextureRegionDrawable(atlas!!.findRegion("UI/btPlayPress"))
        clock = TextureRegionDrawable(atlas!!.findRegion("UI/clock"))

        levelOff = TextureRegionDrawable(atlas!!.findRegion("UI/levelOff"))
        levelStar = TextureRegionDrawable(atlas!!.findRegion("UI/levelStar"))

        // Button level
        val btLevel = TextureRegionDrawable(atlas!!.findRegion("UI/btLevel"))
        styleTextButtonLevel = TextButtonStyle(btLevel, null, null, font)

        // Button level
        val btLevelLocked = TextureRegionDrawable(atlas!!.findRegion("UI/btLevelLocked"))
        styleTextButtonLevelLocked = TextButtonStyle(btLevelLocked, null, null, font)

        backgroundMoves = TextureRegionDrawable(atlas!!.findRegion("UI/backgroundMoves"))
        backgroundTime = TextureRegionDrawable(atlas!!.findRegion("UI/backgroundTime"))
    }

    fun loadTiledMap(numMap: Int) {
        if (map != null) {
            map!!.dispose()
            map = null
        }
        if (isTest) {
            map = TmxMapLoader().load("data/mapsTest/map$numMap.tmx")
        } else {
            map = AtlasTmxMapLoader().load("data/maps/map$numMap.tmx")
        }
    }
}
