package com.nopalsoft.slamthebird

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.slamthebird.shop.PlayersSubMenu

object Assets {
    @JvmField
    var title: AtlasRegion? = null

    @JvmField
    var tapToPlay: AtlasRegion? = null

    @JvmField
    var bestScore: AtlasRegion? = null

    @JvmField
    var score: AtlasRegion? = null
    var combo: AtlasRegion? = null

    @JvmField
    var coinsEarned: AtlasRegion? = null
    var shop: AtlasRegion? = null

    @JvmField
    var horizontalSeparator: NinePatchDrawable? = null
    var verticalSeparator: NinePatchDrawable? = null

    @JvmField
    var background: AtlasRegion? = null

    @JvmField
    var backgroundGameOver: AtlasRegion? = null

    @JvmField
    var player: AtlasRegion? = null

    @JvmField
    var playerShopDefault: AtlasRegion? = null

    @JvmField
    var playerShopRed: AtlasRegion? = null

    @JvmField
    var playerShopBlue: AtlasRegion? = null

    @JvmField
    var animationPlayerJump: Animation<TextureRegion>? = null

    @JvmField
    var animationPlayerSlam: Animation<TextureRegion>? = null

    @JvmField
    var animationPlayerHit: Animation<TextureRegion>? = null

    @JvmField
    var slam: Animation<TextureRegion>? = null

    @JvmField
    var blackPixel: NinePatchDrawable? = null

    @JvmField
    var platform: AtlasRegion? = null

    @JvmField
    var animationPlatformFire: Animation<TextureRegion>? = null

    @JvmField
    var breakablePlatform: Animation<TextureRegion>? = null

    @JvmField
    var buttonAchievements: TextureRegionDrawable? = null

    @JvmField
    var buttonLeaderBoard: TextureRegionDrawable? = null

    @JvmField
    var buttonMore: TextureRegionDrawable? = null

    @JvmField
    var buttonRate: TextureRegionDrawable? = null

    @JvmField
    var buttonShop: TextureRegionDrawable? = null

    @JvmField
    var buttonFacebook: TextureRegionDrawable? = null

    @JvmField
    var buttonTwitter: TextureRegionDrawable? = null
    var buttonBack: TextureRegionDrawable? = null

    @JvmField
    var buttonNoAds: TextureRegionDrawable? = null

    @JvmField
    var buttonStyleMusic: ButtonStyle? = null

    @JvmField
    var buttonStyleSound: ButtonStyle? = null

    @JvmField
    var buttonUpgradeOn: TextureRegionDrawable? = null

    @JvmField
    var buttonUpgradeOff: TextureRegionDrawable? = null

    @JvmField
    var buttonScores: TextureRegionDrawable? = null

    @JvmField
    var birdSpawn: AtlasRegion? = null

    @JvmField
    var birdBlue: AtlasRegion? = null

    @JvmField
    var animationBlueBirdFlap: Animation<TextureRegion>? = null

    @JvmField
    var animationRedBirdFlap: Animation<TextureRegion>? = null

    @JvmField
    var animationEvolving: Animation<TextureRegion?>? = null

    @JvmField
    var animationCoin: Animation<TextureRegion>? = null

    @JvmField
    var coin: AtlasRegion? = null
    var transparentPixel: AtlasRegion? = null

    @JvmField
    var invincibleBoost: AtlasRegion? = null

    @JvmField
    var coinRainBoost: AtlasRegion? = null

    @JvmField
    var IceBoost: AtlasRegion? = null

    @JvmField
    var superJumpBoost: AtlasRegion? = null

    @JvmField
    var boosts: AtlasRegion? = null

    @JvmField
    var animationInvincibleBoostEnd: Animation<TextureRegion?>? = null

    @JvmField
    var animationSuperJumpBoostEnd: Animation<TextureRegion?>? = null

    @JvmField
    var num0Big: AtlasRegion? = null

    @JvmField
    var num1Big: AtlasRegion? = null

    @JvmField
    var num2Big: AtlasRegion? = null

    @JvmField
    var num3Big: AtlasRegion? = null

    @JvmField
    var num4Big: AtlasRegion? = null

    @JvmField
    var num5Big: AtlasRegion? = null

    @JvmField
    var num6Big: AtlasRegion? = null

    @JvmField
    var num7Big: AtlasRegion? = null

    @JvmField
    var num8Big: AtlasRegion? = null

    @JvmField
    var num9Big: AtlasRegion? = null

    @JvmField
    var num0Small: AtlasRegion? = null

    @JvmField
    var num1Small: AtlasRegion? = null

    @JvmField
    var num2Small: AtlasRegion? = null

    @JvmField
    var num3Small: AtlasRegion? = null

    @JvmField
    var num4Small: AtlasRegion? = null

    @JvmField
    var num5Small: AtlasRegion? = null

    @JvmField
    var num6Small: AtlasRegion? = null

    @JvmField
    var num7Small: AtlasRegion? = null

    @JvmField
    var num8Small: AtlasRegion? = null

    @JvmField
    var num9Small: AtlasRegion? = null
    var font: BitmapFont? = null

    @JvmField
    var textButtonStyleBuy: TextButtonStyle? = null

    @JvmField
    var textButtonStylePurchased: TextButtonStyle? = null

    @JvmField
    var textButtonStyleSelected: TextButtonStyle? = null
    var scrollPaneStyle: ScrollPaneStyle? = null

    @JvmField
    var labelStyleSmall: LabelStyle? = null
    var soundJump: Sound? = null

    @JvmField
    var soundCoin: Sound? = null

    @JvmField
    var soundBoost: Sound? = null
    var textureAtlas: TextureAtlas? = null
    var music: Music? = null

    fun loadStyles() {
        font = BitmapFont()
        font!!.data.setScale(1.15f)

        horizontalSeparator = NinePatchDrawable(
            NinePatch(
                textureAtlas!!.findRegion("Shop/separadorHorizontal"), 0, 1, 0, 0
            )
        )
        verticalSeparator = NinePatchDrawable(
            NinePatch(
                textureAtlas!!.findRegion("Shop/separadorVertical"), 0, 1, 0, 0
            )
        )

        labelStyleSmall = LabelStyle(font, Color.WHITE)

        // Button Buy
        val buttonBuy = TextureRegionDrawable(
            textureAtlas!!.findRegion("Shop/btBuy")
        )
        textButtonStyleBuy = TextButtonStyle(buttonBuy, null, null, font)


        // Button Purchased
        val buttonPurchased = TextureRegionDrawable(
            textureAtlas!!.findRegion("Shop/btPurchased")
        )
        textButtonStylePurchased = TextButtonStyle(
            buttonPurchased, null, null,
            font
        )

        // Button Selected
        val buttonSelected = TextureRegionDrawable(
            textureAtlas!!.findRegion("Shop/btSelected")
        )
        textButtonStyleSelected = TextButtonStyle(
            buttonSelected, null, null,
            font
        )

        // Button Music
        val buttonMusicOn = TextureRegionDrawable(
            textureAtlas!!.findRegion("MenuPrincipal/btMusica")
        )
        val buttonMusicOff = TextureRegionDrawable(
            textureAtlas!!.findRegion("MenuPrincipal/btSinMusica")
        )
        buttonStyleMusic = ButtonStyle(buttonMusicOn, null, buttonMusicOff)

        // Button Sound
        val buttonSoundOn = TextureRegionDrawable(
            textureAtlas!!.findRegion("MenuPrincipal/btSonido")
        )
        val buttonSoundOff = TextureRegionDrawable(
            textureAtlas!!.findRegion("MenuPrincipal/btSinSonido")
        )
        buttonStyleSound = ButtonStyle(buttonSoundOn, null, buttonSoundOff)

        scrollPaneStyle = ScrollPaneStyle(
            null, null, null, null,
            verticalSeparator
        )
    }

    @JvmStatic
    fun loadPlayer() {
        var selectedPlayer = "AndroidBot"

        if (Settings.selectedSkin == PlayersSubMenu.SKIN_ANDROID_RED) {
            selectedPlayer = "AndroidBotRojo"
        } else if (Settings.selectedSkin == PlayersSubMenu.SKIN_ANDROID_BLUE) {
            selectedPlayer = "AndroidBotAzul"
        }

        player = textureAtlas!!.findRegion(
            "Personajes/" + selectedPlayer
                    + "/personajeFall"
        )

        var per1 = textureAtlas!!.findRegion(
            "Personajes/" + selectedPlayer
                    + "/personajeSlam1"
        )
        var per2 = textureAtlas!!.findRegion(
            "Personajes/" + selectedPlayer
                    + "/personajeSlam2"
        )
        var per3 = textureAtlas!!.findRegion(
            "Personajes/" + selectedPlayer
                    + "/personajeSlam3"
        )
        val per4 = textureAtlas!!.findRegion(
            "Personajes/" + selectedPlayer
                    + "/personajeSlam4"
        )
        animationPlayerSlam = Animation(.05f, per1, per2, per3, per4)

        per1 = textureAtlas!!.findRegion(
            "Personajes/" + selectedPlayer
                    + "/personajeJump1"
        )
        per2 = textureAtlas!!.findRegion(
            "Personajes/" + selectedPlayer
                    + "/personajeJump1"
        )
        per3 = textureAtlas!!.findRegion(
            "Personajes/" + selectedPlayer
                    + "/personajeJump1"
        )
        animationPlayerJump = Animation(.1f, per1, per2, per3)

        per1 = textureAtlas!!.findRegion(
            "Personajes/" + selectedPlayer
                    + "/personajeHit"
        )
        per2 = textureAtlas!!.findRegion(
            "Personajes/" + selectedPlayer
                    + "/personajeHit"
        )
        per3 = textureAtlas!!.findRegion(
            "Personajes/" + selectedPlayer
                    + "/personajeHit"
        )
        animationPlayerHit = Animation(.1f, per1, per2, per3)

        // Estos son los que aparecen en la tienda;
        playerShopDefault = textureAtlas!!
            .findRegion("Personajes/AndroidBot/personajeFall")
        playerShopRed = textureAtlas!!
            .findRegion("Personajes/AndroidBotRojo/personajeFall")
        playerShopBlue = textureAtlas!!
            .findRegion("Personajes/AndroidBotAzul/personajeFall")
    }

    fun load() {
        textureAtlas = TextureAtlas(Gdx.files.internal("data/atlasMap.txt"))

        loadStyles()

        title = textureAtlas!!.findRegion("MenuPrincipal/titulo")
        tapToPlay = textureAtlas!!.findRegion("MenuPrincipal/tapToPlay")
        bestScore = textureAtlas!!.findRegion("MenuPrincipal/bestScore")
        score = textureAtlas!!.findRegion("MenuPrincipal/score")
        combo = textureAtlas!!.findRegion("MenuPrincipal/combo")
        coinsEarned = textureAtlas!!.findRegion("MenuPrincipal/coinsEarned")

        background = textureAtlas!!.findRegion("fondo")
        buttonScores = TextureRegionDrawable(
            textureAtlas!!.findRegion("MenuPrincipal/fondoPuntuaciones")
        )
        backgroundGameOver = textureAtlas!!.findRegion("fondoGameover")

        transparentPixel = textureAtlas!!.findRegion("pixelTransparente")

        shop = textureAtlas!!.findRegion("Shop/Shop")

        buttonBack = TextureRegionDrawable(textureAtlas!!.findRegion("Shop/btAtras"))
        buttonNoAds = TextureRegionDrawable(textureAtlas!!.findRegion("Shop/btNoAds"))

        buttonUpgradeOff = TextureRegionDrawable(
            textureAtlas!!.findRegion("Shop/upgradeOff")
        )
        buttonUpgradeOn = TextureRegionDrawable(
            textureAtlas!!.findRegion("Shop/upgradeOn")
        )

        blackPixel = NinePatchDrawable(
            NinePatch(
                textureAtlas!!.findRegion("MenuPrincipal/pixelNegro"), 1, 1, 0, 0
            )
        )

        val per1 = textureAtlas!!.findRegion("moneda1")
        val per2 = textureAtlas!!.findRegion("moneda2")
        val per3 = textureAtlas!!.findRegion("moneda3")
        coin = per1
        animationCoin = Animation(.15f, per1, per2, per3, per2)

        birdBlue = textureAtlas!!.findRegion("InGame/flapAzul")
        birdSpawn = textureAtlas!!.findRegion("InGame/flapSpawn")

        var flap1 = textureAtlas!!.findRegion("InGame/flapAzulAlas1")
        var flap2 = textureAtlas!!.findRegion("InGame/flapAzulAlas2")
        var flap3 = textureAtlas!!.findRegion("InGame/flapAzulAlas3")
        animationBlueBirdFlap = Animation(.15f, flap1, flap2, flap3, flap2)

        flap1 = textureAtlas!!.findRegion("InGame/flapRojoAlas1")
        flap2 = textureAtlas!!.findRegion("InGame/flapRojoAlas2")
        flap3 = textureAtlas!!.findRegion("InGame/flapRojoAlas3")
        animationRedBirdFlap = Animation(.15f, flap1, flap2, flap3, flap2)
        animationEvolving = Animation(
            .075f, birdBlue, flap1, birdBlue, flap2,
            birdBlue, flap3
        )

        flap1 = textureAtlas!!.findRegion("InGame/plataformFire1")
        flap2 = textureAtlas!!.findRegion("InGame/plataformFire2")
        flap3 = textureAtlas!!.findRegion("InGame/plataformFire3")
        animationPlatformFire = Animation(.15f, flap1, flap2, flap3, flap2)

        flap1 = textureAtlas!!.findRegion("InGame/plataforma2")
        flap2 = textureAtlas!!.findRegion("InGame/plataforma3")
        flap3 = textureAtlas!!.findRegion("InGame/plataforma4")
        breakablePlatform = Animation(.1f, flap1, flap2, flap3)
        platform = textureAtlas!!.findRegion("InGame/plataforma1")

        flap1 = textureAtlas!!.findRegion("InGame/slam1")
        flap2 = textureAtlas!!.findRegion("InGame/slam2")
        flap3 = textureAtlas!!.findRegion("InGame/slam3")
        slam = Animation(.1f, flap1, flap2, flap3)

        invincibleBoost = textureAtlas!!.findRegion("InGame/boostInvencible")
        coinRainBoost = textureAtlas!!.findRegion("InGame/boostCoinRain")
        IceBoost = textureAtlas!!.findRegion("InGame/boostIce")
        superJumpBoost = textureAtlas!!.findRegion("InGame/boostSuperSalto")
        boosts = textureAtlas!!.findRegion("InGame/boosts")

        animationInvincibleBoostEnd = Animation(
            .15f, invincibleBoost,
            transparentPixel
        )
        animationSuperJumpBoostEnd = Animation(
            .15f, superJumpBoost,
            transparentPixel
        )

        buttonAchievements = TextureRegionDrawable(
            textureAtlas!!.findRegion("MenuPrincipal/btAchievements")
        )
        buttonLeaderBoard = TextureRegionDrawable(
            textureAtlas!!.findRegion("MenuPrincipal/btLeaderboard")
        )
        buttonMore = TextureRegionDrawable(
            textureAtlas!!.findRegion("MenuPrincipal/btMore")
        )
        buttonRate = TextureRegionDrawable(
            textureAtlas!!.findRegion("MenuPrincipal/btRate")
        )
        buttonShop = TextureRegionDrawable(
            textureAtlas!!.findRegion("MenuPrincipal/btShop")
        )
        buttonFacebook = TextureRegionDrawable(
            textureAtlas!!.findRegion("MenuPrincipal/btFacebook")
        )
        buttonTwitter = TextureRegionDrawable(
            textureAtlas!!.findRegion("MenuPrincipal/btTwitter")
        )

        num0Big = textureAtlas!!.findRegion("Numeros/num0")
        num1Big = textureAtlas!!.findRegion("Numeros/num1")
        num2Big = textureAtlas!!.findRegion("Numeros/num2")
        num3Big = textureAtlas!!.findRegion("Numeros/num3")
        num4Big = textureAtlas!!.findRegion("Numeros/num4")
        num5Big = textureAtlas!!.findRegion("Numeros/num5")
        num6Big = textureAtlas!!.findRegion("Numeros/num6")
        num7Big = textureAtlas!!.findRegion("Numeros/num7")
        num8Big = textureAtlas!!.findRegion("Numeros/num8")
        num9Big = textureAtlas!!.findRegion("Numeros/num9")

        num0Small = textureAtlas!!.findRegion("Numeros/0")
        num1Small = textureAtlas!!.findRegion("Numeros/1")
        num2Small = textureAtlas!!.findRegion("Numeros/2")
        num3Small = textureAtlas!!.findRegion("Numeros/3")
        num4Small = textureAtlas!!.findRegion("Numeros/4")
        num5Small = textureAtlas!!.findRegion("Numeros/5")
        num6Small = textureAtlas!!.findRegion("Numeros/6")
        num7Small = textureAtlas!!.findRegion("Numeros/7")
        num8Small = textureAtlas!!.findRegion("Numeros/8")
        num9Small = textureAtlas!!.findRegion("Numeros/9")

        Settings.load()

        // It should be called after loading settings
        loadPlayer()

        soundCoin = Gdx.audio.newSound(
            Gdx.files
                .internal("data/Sounds/pickCoin.mp3")
        )
        soundJump = Gdx.audio.newSound(
            Gdx.files
                .internal("data/Sounds/salto.mp3")
        )
        soundBoost = Gdx.audio.newSound(
            Gdx.files
                .internal("data/Sounds/pickBoost.mp3")
        )

        music = Gdx.audio.newMusic(
            Gdx.files
                .internal("data/Sounds/musica.mp3")
        )

        music?.setLooping(true)

        if (Settings.isMusicOn) playMusic()
    }

    @JvmStatic
    fun playSound(sound: Sound) {
        if (Settings.isSoundOn) sound.play(1f)
    }

    @JvmStatic
    fun playMusic() {
        music!!.play()
    }

    @JvmStatic
    fun pauseMusic() {
        music!!.pause()
    }
}
