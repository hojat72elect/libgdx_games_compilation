package com.nopalsoft.invaders

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.I18NBundle
import com.nopalsoft.invaders.Settings.soundEnabled
import com.nopalsoft.invaders.parallax.ParallaxBackground
import com.nopalsoft.invaders.parallax.ParallaxLayer

object Assets {

    private val glyphLayout = GlyphLayout()
    @JvmStatic
    var languages: I18NBundle? = null
    var background: AtlasRegion? = null
    @JvmStatic
    var parallaxBackground: ParallaxBackground? = null

    @JvmStatic
    var shipLeft: AtlasRegion? = null
    @JvmStatic
    var shipRight: AtlasRegion? = null
    @JvmStatic
    var ship: AtlasRegion? = null

    // Fonts
    @JvmStatic
    var font60: BitmapFont? = null // Mainly for the title of the app
    @JvmStatic
    var font45: BitmapFont? = null
    @JvmStatic
    var font15: BitmapFont? = null
    @JvmStatic
    var font10: BitmapFont? = null

    // Menu
    @JvmStatic
    var ellipseMenuLeft: AtlasRegion? = null
    @JvmStatic
    var buttonSignInUp: NinePatchDrawable? = null
    @JvmStatic
    var buttonSignInDown: NinePatchDrawable? = null
    @JvmStatic
    var titleMenuBox: NinePatchDrawable? = null

    // Game
    @JvmStatic
    var recuadroInGameStatus: NinePatchDrawable? = null
    @JvmStatic
    var btLeft: TextureRegionDrawable? = null
    @JvmStatic
    var btRight: TextureRegionDrawable? = null
    @JvmStatic
    var btFire: TextureRegionDrawable? = null
    @JvmStatic
    var btFireDown: TextureRegionDrawable? = null
    @JvmStatic
    var btMissil: TextureRegionDrawable? = null
    @JvmStatic
    var btMissilDown: TextureRegionDrawable? = null

    // Aid
    @JvmStatic
    var help1: AtlasRegion? = null
    @JvmStatic
    var clickHelp: AtlasRegion? = null

    // Buttons
    @JvmStatic
    var buttonSoundOn: TextureRegionDrawable? = null
    @JvmStatic
    var buttonSoundOff: TextureRegionDrawable? = null
    @JvmStatic
    var buttonMusicOn: TextureRegionDrawable? = null
    @JvmStatic
    var buttonMusicOff: TextureRegionDrawable? = null

    // Ammunition
    var balaNormal: AtlasRegion? = null
    @JvmStatic
    var missile: Animation<TextureRegion>? = null
    var bulletLevel1: AtlasRegion? = null
    var bulletLevel2: AtlasRegion? = null
    var bulletLevel3: AtlasRegion? = null
    var bulletLevel4: AtlasRegion? = null

    @JvmStatic
    var boost1: AtlasRegion? = null
    @JvmStatic
    var boost2: AtlasRegion? = null
    @JvmStatic
    var boost3: AtlasRegion? = null
    @JvmStatic
    var upgLife: AtlasRegion? = null

    var explosionFuego: Animation<TextureRegion>? = null
    var shield: Animation<TextureRegion>? = null

    var bulletNormalEnemy: AtlasRegion? = null
    var alien1: AtlasRegion? = null
    var alien2: AtlasRegion? = null
    var alien3: AtlasRegion? = null
    var alien4: AtlasRegion? = null

    // Sounds
    @JvmStatic
    var music: Music? = null
    @JvmStatic
    var coinSound: Sound? = null
    @JvmStatic
    var clickSound: Sound? = null
    @JvmStatic
    var explosionSound: Sound? = null
    @JvmStatic
    var missileFire: Sound? = null

    // Styles
    @JvmStatic
    var styleTextButtonMenu: TextButtonStyle? = null
    @JvmStatic
    var styleTextButtonFacebook: TextButtonStyle? = null
    @JvmStatic
    var styleTextButtonBack: TextButtonStyle? = null
    @JvmStatic
    var styleTextButton: TextButtonStyle? = null

    @JvmStatic
    var styleDialogPause: WindowStyle? = null
    @JvmStatic
    var styleLabel: LabelStyle? = null
    @JvmStatic
    var styleLabelDialog: LabelStyle? = null
    @JvmStatic
    var styleSlider: SliderStyle? = null
    @JvmStatic
    var styleImageButtonPause: ImageButtonStyle? = null
    @JvmStatic
    var styleImageButtonStyleCheckBox: ImageButtonStyle? = null


    private fun loadFont(atlas: TextureAtlas) {
        font60 = BitmapFont(Gdx.files.internal("data/font35.fnt"), atlas.findRegion("font35"), false)
        font45 = BitmapFont(Gdx.files.internal("data/font35.fnt"), atlas.findRegion("font35"), false)
        font15 = BitmapFont(Gdx.files.internal("data/font15.fnt"), atlas.findRegion("font15"), false)
        font10 = BitmapFont(Gdx.files.internal("data/font15.fnt"), atlas.findRegion("font15"), false)

        font60?.color = Color.GREEN
        font45?.color = Color.GREEN
        font15?.color = Color.GREEN
        font10?.color = Color.GREEN
    }


    private fun loadSceneStyles(atlas: TextureAtlas) {

        // Dialog
        val recuadroLogIn = NinePatchDrawable(atlas.createPatch("recuadroLogIn"))
        val dialogDim = atlas.findRegion("fondoNegro")
        styleDialogPause = WindowStyle(font45, Color.GREEN, recuadroLogIn)
        styleDialogPause?.stageBackground = NinePatchDrawable(NinePatch(dialogDim))
        styleLabelDialog = LabelStyle(font15, Color.GREEN)

        /* */
        val default_round_down = NinePatchDrawable(atlas.createPatch("botonDown"))
        val default_round = NinePatchDrawable(atlas.createPatch("boton"))
        styleTextButton = TextButtonStyle(default_round, default_round_down, null, font15)
        styleTextButton?.fontColor = Color.GREEN

        /* Menu */
        val botonMenu = NinePatchDrawable(atlas.createPatch("botonMenu"))
        val botonMenuDown = NinePatchDrawable(atlas.createPatch("botonMenuPresionado"))
        styleTextButtonMenu = TextButtonStyle(botonMenu, botonMenuDown, null, font45)
        styleTextButtonMenu?.fontColor = Color.GREEN

        styleLabel = LabelStyle(font15, Color.GREEN)

        /* Slider */
        val default_slider = NinePatchDrawable(atlas.createPatch("default-slider"))
        val default_slider_knob = TextureRegionDrawable(atlas.findRegion("default-slider-knob"))

        styleSlider = SliderStyle(default_slider, default_slider_knob)

        val btBackUp = TextureRegionDrawable(atlas.findRegion("btBack"))
        val btBackDown = TextureRegionDrawable(atlas.findRegion("btBackDown"))

        styleTextButtonBack = TextButtonStyle(btBackUp, btBackDown, null, font15)
        styleTextButtonBack?.fontColor = Color.GREEN

        val buttonPauseUp = TextureRegionDrawable(atlas.findRegion("btPause"))
        val buttonPauseDown = TextureRegionDrawable(atlas.findRegion("btPauseDown"))
        styleImageButtonPause = ImageButtonStyle(buttonPauseUp, buttonPauseDown, null, null, null, null)

        val botonFacebook = NinePatchDrawable(atlas.createPatch("btShareFacebookUp"))
        val botonFacebookDown = NinePatchDrawable(atlas.createPatch("btShareFacebookDown"))
        styleTextButtonFacebook = TextButtonStyle(botonFacebook, botonFacebookDown, null, font10)

        val checked = TextureRegionDrawable(atlas.findRegion("checkBoxDown"))
        val uncheked = TextureRegionDrawable(atlas.findRegion("checkBox"))

        styleImageButtonStyleCheckBox = ImageButtonStyle(uncheked, checked, checked, null, null, null)
    }


    fun load() {
        languages = I18NBundle.createBundle(Gdx.files.internal("strings/strings"))


        val atlas1 = TextureAtlas(Gdx.files.internal("data/atlasMap.txt"))

        loadFont(atlas1)
        loadSceneStyles(atlas1)

        // Menu
        ellipseMenuLeft = atlas1.findRegion("elipseMenuIzq")
        titleMenuBox = NinePatchDrawable(atlas1.createPatch("tituloMenuRecuadro"))

        // Game
        recuadroInGameStatus = NinePatchDrawable(atlas1.createPatch("recuadroInGameStatus"))
        btLeft = TextureRegionDrawable(atlas1.findRegion("btLeft"))
        btRight = TextureRegionDrawable(atlas1.findRegion("btRight"))
        btFire = TextureRegionDrawable(atlas1.findRegion("btFire"))
        btFireDown = TextureRegionDrawable(atlas1.findRegion("btFire"))
        btMissil = TextureRegionDrawable(atlas1.findRegion("btMissil"))
        btMissilDown = TextureRegionDrawable(atlas1.findRegion("btMissil"))

        val background = atlas1.findRegion("fondo")

        buttonSignInUp = NinePatchDrawable(NinePatch(atlas1.createPatch("btSignInUp")))
        buttonSignInDown = NinePatchDrawable(NinePatch(atlas1.createPatch("btSignInDown")))

        // Aid
        help1 = atlas1.findRegion("help1")
        clickHelp = atlas1.findRegion("ayudaClick")

        // Buttons
        buttonMusicOn = TextureRegionDrawable(atlas1.findRegion("btMusica"))
        buttonMusicOff = TextureRegionDrawable(atlas1.findRegion("btSinMusica"))
        buttonSoundOn = TextureRegionDrawable(atlas1.findRegion("btSonido"))
        buttonSoundOff = TextureRegionDrawable(atlas1.findRegion("btSinSonido"))

        // Ship
        shipRight = atlas1.findRegion("naveRight")
        shipLeft = atlas1.findRegion("naveLeft")
        ship = atlas1.findRegion("nave")

        val shield0 = atlas1.findRegion("shield0")
        val shield1 = atlas1.findRegion("shield1")
        val shield2 = atlas1.findRegion("shield2")
        val shield3 = atlas1.findRegion("shield3")
        val shield4 = atlas1.findRegion("shield4")
        val shield5 = atlas1.findRegion("shield5")
        val shield6 = atlas1.findRegion("shield6")
        val shield7 = atlas1.findRegion("shield7")
        val shield8 = atlas1.findRegion("shield9")
        val shield9 = atlas1.findRegion("shield9")
        val shield10 = atlas1.findRegion("shield10")
        val shield11 = atlas1.findRegion("shield11")
        shield = Animation(
            .1f,
            shield0,
            shield1,
            shield2,
            shield3,
            shield4,
            shield5,
            shield6,
            shield7,
            shield8,
            shield9,
            shield10,
            shield11
        )

        // UFOs
        alien1 = atlas1.findRegion("alien1")
        alien2 = atlas1.findRegion("alien2")
        alien3 = atlas1.findRegion("alien3")
        alien4 = atlas1.findRegion("alien4")

        boost1 = atlas1.findRegion("upgLaser")
        boost2 = atlas1.findRegion("upgBomb")
        boost3 = atlas1.findRegion("upgShield")
        upgLife = atlas1.findRegion("upgLife")

        // Ammunition
        balaNormal = atlas1.findRegion("balaNormal")
        bulletNormalEnemy = atlas1.findRegion("balaNormalEnemigo")

        val missile1 = atlas1.findRegion("misil1")
        val missile2 = atlas1.findRegion("misil2")
        missile = Animation(0.2f, missile1, missile2)


        bulletLevel1 = atlas1.findRegion("disparoA1")
        bulletLevel2 = atlas1.findRegion("disparoA2")
        bulletLevel3 = atlas1.findRegion("disparoA3")
        bulletLevel4 = atlas1.findRegion("disparoA4")

        // explosion fire
        val newExplosion1 = atlas1.findRegion("newExplosion1")
        val newExplosion2 = atlas1.findRegion("newExplosion2")
        val newExplosion3 = atlas1.findRegion("newExplosion3")
        val newExplosion4 = atlas1.findRegion("newExplosion4")
        val newExplosion5 = atlas1.findRegion("newExplosion5")
        val newExplosion6 = atlas1.findRegion("newExplosion6")
        val newExplosion7 = atlas1.findRegion("newExplosion7")
        val newExplosion8 = atlas1.findRegion("newExplosion8")
        val newExplosion9 = atlas1.findRegion("newExplosion9")
        val newExplosion10 = atlas1.findRegion("newExplosion10")
        val newExplosion11 = atlas1.findRegion("newExplosion11")
        val newExplosion12 = atlas1.findRegion("newExplosion12")
        val newExplosion13 = atlas1.findRegion("newExplosion13")
        val newExplosion14 = atlas1.findRegion("newExplosion14")
        val newExplosion15 = atlas1.findRegion("newExplosion15")
        val newExplosion16 = atlas1.findRegion("newExplosion16")
        val newExplosion17 = atlas1.findRegion("newExplosion17")
        val newExplosion18 = atlas1.findRegion("newExplosion18")
        val newExplosion19 = atlas1.findRegion("newExplosion19")
        explosionFuego = Animation(
            0.05f,
            newExplosion1,
            newExplosion2,
            newExplosion3,
            newExplosion4,
            newExplosion5,
            newExplosion6,
            newExplosion7,
            newExplosion8,
            newExplosion9,
            newExplosion10,
            newExplosion11,
            newExplosion12,
            newExplosion13,
            newExplosion14,
            newExplosion15,
            newExplosion16,
            newExplosion17,
            newExplosion18,
            newExplosion19
        )

        val para1 = ParallaxLayer(background, Vector2(0f, 50f), Vector2(0f, 0f))
        val arr = arrayOf(para1)
        parallaxBackground = ParallaxBackground(arr, 320f, 480f, Vector2(0f, 1f))

        music = Gdx.audio.newMusic(Gdx.files.internal("data/sonidos/musica.mp3"))
        music?.isLooping = true
        music?.volume = 0.1f
        coinSound = Gdx.audio.newSound(Gdx.files.internal("data/sonidos/coin.ogg"))
        clickSound = Gdx.audio.newSound(Gdx.files.internal("data/sonidos/click.ogg"))
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("data/sonidos/sound_explode.ogg"))
        missileFire = Gdx.audio.newSound(Gdx.files.internal("data/sonidos/missilFire3.ogg"))

        Settings.load()
        if (Settings.musicEnabled) music?.play()
    }

    fun playSound(sound: Sound, volumen: Float) {
        if (Settings.soundEnabled) sound.play(volumen)
    }

    @JvmStatic
    fun playSound(sound: Sound) {
        if (soundEnabled) sound.play(1f)
    }

    @JvmStatic
    fun getTextWidth(font: BitmapFont?, text: String?): Float {
        glyphLayout.setText(font, text)
        return glyphLayout.width
    }

}