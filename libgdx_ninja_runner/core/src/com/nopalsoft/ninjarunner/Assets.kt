package com.nopalsoft.ninjarunner

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.ninjarunner.Settings.isSoundEnabled
import com.nopalsoft.ninjarunner.parallax.ParallaxBackground
import com.nopalsoft.ninjarunner.parallax.ParallaxLayer

object Assets {
    var fontSmall: BitmapFont? = null
    var fontLarge: BitmapFont? = null
    var playerRunAnimation: AnimationSprite? = null
    var playerDashAnimation: AnimationSprite? = null
    var playerIdleAnimation: AnimationSprite? = null
    var playerDeadAnimation: AnimationSprite? = null
    var playerJumpAnimation: AnimationSprite? = null
    var playerHurtAnimation: AnimationSprite? = null
    var playerDizzyAnimation: AnimationSprite? = null
    var playerSlideAnimation: AnimationSprite? = null
    var ninjaRunAnimation: AnimationSprite? = null
    var ninjaDashAnimation: AnimationSprite? = null
    var ninjaIdleAnimation: AnimationSprite? = null
    var ninjaDeadAnimation: AnimationSprite? = null
    var ninjaJumpAnimation: AnimationSprite? = null
    var ninjaHurtAnimation: AnimationSprite? = null
    var ninjaDizzyAnimation: AnimationSprite? = null
    var ninjaSlideAnimation: AnimationSprite? = null
    var Pet1FlyAnimation: AnimationSprite? = null
    var Pet1DashAnimation: AnimationSprite? = null
    var PetBombFlyAnimation: AnimationSprite? = null
    var coinAnimation: AnimationSprite? = null
    var pickAnimation: AnimationSprite? = null
    var magnet: Sprite? = null
    var energy: Sprite? = null
    var hearth: Sprite? = null
    var jellyRed: Sprite? = null
    var beanRed: Sprite? = null
    var candyCorn: Sprite? = null
    var candyExplosionRed: AnimationSprite? = null
    var platform: Sprite? = null
    var wall: Sprite? = null

    // Obstacles
    var boxes4: Sprite? = null
    var boxes7: Sprite? = null
    var missileAnimation: AnimationSprite? = null
    var explosion: AnimationSprite? = null
    var backgroundNubes: ParallaxBackground? = null
    var music1: Music? = null
    var pixelNegro: NinePatchDrawable? = null
    var jump: Sound? = null
    var coin: Sound? = null
    var popCandy: Sound? = null
    var boxesEffectPool: ParticleEffectPool? = null

    // UI STUFF
    var title: TextureRegionDrawable? = null
    var backgroundMenu: NinePatchDrawable? = null
    var backgroundShop: NinePatchDrawable? = null
    var backgroundTitleShop: NinePatchDrawable? = null
    var backgroundItemShop: NinePatchDrawable? = null
    var backgroundUpgradeBar: NinePatchDrawable? = null
    var buttonShop: TextureRegionDrawable? = null
    var buttonShopPressed: TextureRegionDrawable? = null
    var buttonLeaderboard: TextureRegionDrawable? = null
    var buttonLeaderboardPressed: TextureRegionDrawable? = null
    var buttonAchievement: TextureRegionDrawable? = null
    var buttonAchievementPressed: TextureRegionDrawable? = null

    var buttonSettings: TextureRegionDrawable? = null
    var buttonSettingsPressed: TextureRegionDrawable? = null
    var buttonRate: TextureRegionDrawable? = null
    var buttonRatePressed: TextureRegionDrawable? = null
    var buttonShare: TextureRegionDrawable? = null
    var buttonSharePressed: TextureRegionDrawable? = null
    var buttonUpgrade: TextureRegionDrawable? = null
    var buttonUpgradePressed: TextureRegionDrawable? = null
    var buttonFacebook: TextureRegionDrawable? = null
    var buttonFacebookPressed: TextureRegionDrawable? = null
    var photoFrame: TextureRegionDrawable? = null
    var photoNA: TextureRegionDrawable? = null

    var imageGoogle: TextureRegionDrawable? = null
    var imageAmazon: TextureRegionDrawable? = null
    var imageFacebook: TextureRegionDrawable? = null
    var labelStyleSmall: LabelStyle? = null
    var labelStyleLarge: LabelStyle? = null
    var styleTextButtonPurchased: TextButtonStyle? = null
    var styleTextButtonBuy: TextButtonStyle? = null
    var styleButtonUpgrade: ButtonStyle? = null

    fun load() {
        val atlas = TextureAtlas(Gdx.files.internal("data/atlasMap.txt"))

        fontLarge = BitmapFont(Gdx.files.internal("data/fontGrande.fnt"), atlas.findRegion("Font/fontGrande"))
        fontSmall = BitmapFont(Gdx.files.internal("data/fontChico.fnt"), atlas.findRegion("Font/fontChico"))
        fontSmall!!.setUseIntegerPositions(false)


        loadUI(atlas)
        loadShanti(atlas)
        loadNinja(atlas)

        var fly1 = atlas.createSprite("Mascota1/fly1")
        var fly2 = atlas.createSprite("Mascota1/fly2")
        var fly3 = atlas.createSprite("Mascota1/fly3")
        var fly4 = atlas.createSprite("Mascota1/fly4")
        var fly5 = atlas.createSprite("Mascota1/fly5")
        var fly6 = atlas.createSprite("Mascota1/fly6")
        var fly7 = atlas.createSprite("Mascota1/fly7")
        var fly8 = atlas.createSprite("Mascota1/fly8")
        Pet1FlyAnimation = AnimationSprite(.075f, fly1, fly2, fly3, fly4, fly5, fly6, fly7, fly8)

        fly1 = atlas.createSprite("Mascota2/fly1")
        fly2 = atlas.createSprite("Mascota2/fly2")
        fly3 = atlas.createSprite("Mascota2/fly3")
        fly4 = atlas.createSprite("Mascota2/fly4")
        fly5 = atlas.createSprite("Mascota2/fly5")
        fly6 = atlas.createSprite("Mascota2/fly6")
        fly7 = atlas.createSprite("Mascota2/fly7")
        fly8 = atlas.createSprite("Mascota2/fly8")
        PetBombFlyAnimation = AnimationSprite(.075f, fly1, fly2, fly3, fly4, fly5, fly6, fly7, fly8)

        val dash1 = atlas.createSprite("Mascota1/dash1")
        val dash2 = atlas.createSprite("Mascota1/dash2")
        val dash3 = atlas.createSprite("Mascota1/dash3")
        val dash4 = atlas.createSprite("Mascota1/dash4")
        Pet1DashAnimation = AnimationSprite(.04f, dash1, dash2, dash3, dash4)

        val moneda1 = atlas.createSprite("moneda1")
        val moneda2 = atlas.createSprite("moneda2")
        val moneda3 = atlas.createSprite("moneda3")
        val moneda4 = atlas.createSprite("moneda4")
        val moneda5 = atlas.createSprite("moneda5")
        val moneda6 = atlas.createSprite("moneda6")
        val moneda7 = atlas.createSprite("moneda7")
        val moneda8 = atlas.createSprite("moneda8")
        coinAnimation = AnimationSprite(.075f, moneda1, moneda2, moneda3, moneda4, moneda5, moneda6, moneda7, moneda8)

        val pick1 = atlas.createSprite("pick1")
        val pick2 = atlas.createSprite("pick2")
        val pick3 = atlas.createSprite("pick3")
        pickAnimation = AnimationSprite(.1f, pick1, pick2, pick3)

        val missile1 = atlas.createSprite("misil1")
        val missile2 = atlas.createSprite("misil2")
        val missile3 = atlas.createSprite("misil3")
        val missile4 = atlas.createSprite("misil4")
        missileAnimation = AnimationSprite(.05f, missile1, missile2, missile3, missile4)

        var explosion1 = atlas.createSprite("explosion1")
        var explosion2 = atlas.createSprite("explosion2")
        var explosion3 = atlas.createSprite("explosion3")
        var explosion4 = atlas.createSprite("explosion4")
        var explosion5 = atlas.createSprite("explosion5")
        explosion = AnimationSprite(.05f, explosion1, explosion2, explosion3, explosion4, explosion5)

        platform = atlas.createSprite("plataforma")
        wall = atlas.createSprite("pared")
        boxes4 = atlas.createSprite("cajas4")
        boxes7 = atlas.createSprite("cajas7")
        magnet = atlas.createSprite("magnet")
        energy = atlas.createSprite("energy")
        hearth = atlas.createSprite("hearth")

        // Candies
        explosion1 = atlas.createSprite("Candy/explosionred01")
        explosion2 = atlas.createSprite("Candy/explosionred02")
        explosion3 = atlas.createSprite("Candy/explosionred03")
        explosion4 = atlas.createSprite("Candy/explosionred04")
        explosion5 = atlas.createSprite("Candy/explosionred05")
        candyExplosionRed = AnimationSprite(.05f, explosion1, explosion2, explosion3, explosion4, explosion5)

        jellyRed = atlas.createSprite("Candy/jelly_red")
        beanRed = atlas.createSprite("Candy/bean_red")
        candyCorn = atlas.createSprite("Candy/candycorn")

        // Particles
        val boxesEffect = ParticleEffect()
        boxesEffect.load(Gdx.files.internal("data/Particulas/Cajas"), atlas)
        boxesEffectPool = ParticleEffectPool(boxesEffect, 1, 10)

        // the smaller the number, the further back.
        val sol = ParallaxLayer(
            atlas.findRegion("Background/sol"), Vector2(.5f, 0f), Vector2(600f, 300f), Vector2(800f, 800f),
            170f, 170f
        )

        val clouds1 = ParallaxLayer(
            atlas.findRegion("Background/nubesLayer1"), Vector2(1f, 0f), Vector2(0f, 50f), Vector2(
                690f,
                500f
            ), 557f, 159f
        )
        val clouds2 = ParallaxLayer(
            atlas.findRegion("Background/nubesLayer2"), Vector2(3f, 0f), Vector2(0f, 50f), Vector2(
                -1f,
                500f
            ), 1250f, 198f
        )
        val clouds3 = ParallaxLayer(
            atlas.findRegion("Background/nubesLayer3"), Vector2(5f, 0f), Vector2(0f, 50f), Vector2(
                -1f,
                100f
            ), 1250f, 397f
        )

        val trees1 = ParallaxLayer(
            atlas.findRegion("Background/arbolesLayer1"), Vector2(7f, 0f), Vector2(0f, 50f), Vector2(
                -1f, 500f
            ), 1048f, 159f
        )
        val trees2 = ParallaxLayer(
            atlas.findRegion("Background/arbolesLayer2"), Vector2(8f, 0f), Vector2(0f, 50f), Vector2(
                1008f, 500f
            ), 554f, 242f
        )

        val groundBlue = ParallaxLayer(
            atlas.findRegion("Background/sueloAzul"), Vector2(0f, 0f), Vector2(0f, -1f), Vector2(
                -1f,
                500f
            ), 800f, 50f
        )

        backgroundNubes = ParallaxBackground(
            arrayOf(sol, clouds1, clouds2, clouds3, trees1, trees2, groundBlue), 800f, 480f,
            Vector2(20f, 0f)
        )

        jump = Gdx.audio.newSound(Gdx.files.internal("data/Sonidos/salto.mp3"))
        coin = Gdx.audio.newSound(Gdx.files.internal("data/Sonidos/pickCoin.mp3"))
        popCandy = Gdx.audio.newSound(Gdx.files.internal("data/Sonidos/popBubble.mp3"))

        music1 = Gdx.audio.newMusic(Gdx.files.internal("data/Sonidos/Happy.mp3"))
        music1?.setLooping(true)
    }

    private fun loadShanti(atlas: TextureAtlas) {
        val dash1 = atlas.createSprite("dash1")
        val dash2 = atlas.createSprite("dash2")
        val dash3 = atlas.createSprite("dash3")
        playerDashAnimation = AnimationSprite(.085f, dash1, dash2, dash3)

        val idle1 = atlas.createSprite("idle1")
        val idle2 = atlas.createSprite("idle2")
        val idle3 = atlas.createSprite("idle3")
        val idle4 = atlas.createSprite("idle4")
        playerIdleAnimation = AnimationSprite(.25f, idle1, idle2, idle3, idle4)

        val dead1 = atlas.createSprite("dead1")
        val dead2 = atlas.createSprite("dead2")
        val dead3 = atlas.createSprite("dead3")
        val dead4 = atlas.createSprite("dead4")
        val dead5 = atlas.createSprite("dead5")
        playerDeadAnimation = AnimationSprite(.085f, dead1, dead2, dead3, dead4, dead5)

        val hurt1 = atlas.createSprite("hurt1")
        val hurt2 = atlas.createSprite("hurt2")
        playerHurtAnimation = AnimationSprite(.085f, hurt1, hurt2)

        val dizzy1 = atlas.createSprite("dizzy1")
        val dizzy2 = atlas.createSprite("dizzy2")
        val dizzy3 = atlas.createSprite("dizzy3")
        playerDizzyAnimation = AnimationSprite(.18f, dizzy1, dizzy2, dizzy3)

        val jump1 = atlas.createSprite("jump1")
        val jump2 = atlas.createSprite("jump2")
        val jump3 = atlas.createSprite("jump3")
        val jump4 = atlas.createSprite("jump4")
        val jump5 = atlas.createSprite("jump5")
        val jump6 = atlas.createSprite("jump6")
        playerJumpAnimation = AnimationSprite(.18f, jump1, jump2, jump3, jump4, jump5, jump6)

        val run1 = atlas.createSprite("run1")
        val run2 = atlas.createSprite("run2")
        val run3 = atlas.createSprite("run3")
        val run4 = atlas.createSprite("run4")
        val run5 = atlas.createSprite("run5")
        val run6 = atlas.createSprite("run6")
        playerRunAnimation = AnimationSprite(.1f, run1, run2, run3, run4, run5, run6)

        val slide1 = atlas.createSprite("slide1")
        val slide2 = atlas.createSprite("slide2")
        val slide3 = atlas.createSprite("slide3")
        playerSlideAnimation = AnimationSprite(.1f, slide1, slide2, slide3)
    }

    private fun loadNinja(atlas: TextureAtlas) {
        val run1 = atlas.createSprite("Ninja/run1")
        val run2 = atlas.createSprite("Ninja/run2")
        val run3 = atlas.createSprite("Ninja/run3")
        val run4 = atlas.createSprite("Ninja/run4")
        val run5 = atlas.createSprite("Ninja/run5")
        val run6 = atlas.createSprite("Ninja/run6")
        ninjaRunAnimation = AnimationSprite(.1f, run1, run2, run3, run4, run5, run6)
        ninjaDashAnimation = AnimationSprite(.05f, run1, run2, run3, run4, run5, run6)

        val jump1 = atlas.createSprite("Ninja/jump1")
        val jump2 = atlas.createSprite("Ninja/jump2")
        val jump3 = atlas.createSprite("Ninja/jump3")
        val jump4 = atlas.createSprite("Ninja/jump4")
        val jump5 = atlas.createSprite("Ninja/jump5")
        val jump6 = atlas.createSprite("Ninja/jump6")
        val jump7 = atlas.createSprite("Ninja/jump7")
        val jump8 = atlas.createSprite("Ninja/jump8")
        ninjaJumpAnimation = AnimationSprite(.075f, jump1, jump2, jump3, jump4, jump5, jump6, jump7, jump8)

        val slide1 = atlas.createSprite("Ninja/slide1")
        val slide2 = atlas.createSprite("Ninja/slide2")
        val slide3 = atlas.createSprite("Ninja/slide3")
        ninjaSlideAnimation = AnimationSprite(.1f, slide1, slide2, slide3)

        val idle1 = atlas.createSprite("Ninja/idle1")
        val idle2 = atlas.createSprite("Ninja/idle2")
        val idle3 = atlas.createSprite("Ninja/idle3")
        val idle4 = atlas.createSprite("Ninja/idle4")
        ninjaIdleAnimation = AnimationSprite(.25f, idle1, idle2, idle3, idle4)

        val dead1 = atlas.createSprite("Ninja/dead1")
        val dead2 = atlas.createSprite("Ninja/dead2")
        val dead3 = atlas.createSprite("Ninja/dead3")
        val dead4 = atlas.createSprite("Ninja/dead4")
        val dead5 = atlas.createSprite("Ninja/dead5")
        ninjaDeadAnimation = AnimationSprite(.085f, dead1, dead2, dead3, dead4, dead5)

        val hurt1 = atlas.createSprite("Ninja/hurt1")
        val hurt2 = atlas.createSprite("Ninja/hurt2")
        ninjaHurtAnimation = AnimationSprite(.085f, hurt1, hurt2)

        val dizzy1 = atlas.createSprite("Ninja/dizzy1")
        val dizzy2 = atlas.createSprite("Ninja/dizzy2")
        val dizzy3 = atlas.createSprite("Ninja/dizzy3")
        ninjaDizzyAnimation = AnimationSprite(.18f, dizzy1, dizzy2, dizzy3)
    }

    private fun loadUI(atlas: TextureAtlas) {
        title = TextureRegionDrawable(atlas.findRegion("UI/titulo"))

        pixelNegro = NinePatchDrawable(NinePatch(atlas.findRegion("UI/pixelNegro"), 1, 1, 0, 0))

        backgroundMenu = NinePatchDrawable(NinePatch(atlas.findRegion("UI/backgroundMenu"), 40, 40, 40, 40))
        backgroundShop = NinePatchDrawable(NinePatch(atlas.findRegion("UI/backgroundShop"), 140, 40, 40, 40))
        backgroundTitleShop = NinePatchDrawable(NinePatch(atlas.findRegion("UI/backgroundTitleShop"), 40, 40, 40, 30))
        backgroundItemShop = NinePatchDrawable(NinePatch(atlas.findRegion("UI/backgroundItemShop"), 50, 50, 25, 15))
        backgroundUpgradeBar = NinePatchDrawable(NinePatch(atlas.findRegion("UI/backgroundUpgradeBar"), 15, 15, 9, 10))

        buttonShop = TextureRegionDrawable(atlas.findRegion("UI/btShop"))
        buttonShopPressed = TextureRegionDrawable(atlas.findRegion("UI/btShopPress"))
        buttonLeaderboard = TextureRegionDrawable(atlas.findRegion("UI/btLeaderboard"))
        buttonLeaderboardPressed = TextureRegionDrawable(atlas.findRegion("UI/btLeaderboardPress"))
        buttonAchievement = TextureRegionDrawable(atlas.findRegion("UI/btAchievement"))
        buttonAchievementPressed = TextureRegionDrawable(atlas.findRegion("UI/btAchievementPress"))
        buttonSettings = TextureRegionDrawable(atlas.findRegion("UI/btSettings"))
        buttonSettingsPressed = TextureRegionDrawable(atlas.findRegion("UI/btSettingsPress"))
        buttonRate = TextureRegionDrawable(atlas.findRegion("UI/btFacebook"))
        buttonRatePressed = TextureRegionDrawable(atlas.findRegion("UI/btFacebookPress"))
        buttonFacebook = TextureRegionDrawable(atlas.findRegion("UI/btFacebook"))
        buttonFacebookPressed = TextureRegionDrawable(atlas.findRegion("UI/btFacebookPress"))
        buttonShare = TextureRegionDrawable(atlas.findRegion("UI/btShare"))
        buttonSharePressed = TextureRegionDrawable(atlas.findRegion("UI/btSharePress"))
        buttonUpgrade = TextureRegionDrawable(atlas.findRegion("UI/btUpgrade"))
        buttonUpgradePressed = TextureRegionDrawable(atlas.findRegion("UI/btUpgradePress"))
        photoFrame = TextureRegionDrawable(atlas.findRegion("UI/photoFrame"))
        photoNA = TextureRegionDrawable(atlas.findRegion("UI/fotoNA"))

        imageAmazon = TextureRegionDrawable(atlas.findRegion("UI/imAmazon"))
        imageGoogle = TextureRegionDrawable(atlas.findRegion("UI/imGoogle"))
        imageFacebook = TextureRegionDrawable(atlas.findRegion("UI/imFacebook"))

        labelStyleSmall = LabelStyle(fontSmall, Color.WHITE)
        labelStyleLarge = LabelStyle(fontLarge, Color.WHITE)

        val txtButton = TextureRegionDrawable(atlas.findRegion("UI/txtButton"))
        val txtButtonDisabled = TextureRegionDrawable(atlas.findRegion("UI/txtButtonDisabled"))
        val txtButtonPress = TextureRegionDrawable(atlas.findRegion("UI/txtButtonPress"))

        styleTextButtonPurchased = TextButtonStyle(txtButton, txtButtonPress, null, fontSmall)

        styleTextButtonBuy = TextButtonStyle(txtButtonDisabled, txtButtonPress, null, fontSmall)

        styleButtonUpgrade = ButtonStyle(buttonUpgrade, buttonUpgradePressed, null)
    }

    fun playSound(sound: Sound, volume: Int) {
        if (isSoundEnabled) {
            sound.play(volume.toFloat())
        }
    }
}
