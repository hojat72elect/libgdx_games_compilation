package com.nopalsoft.slamthebird

import com.badlogic.gdx.backends.iosrobovm.IOSApplication
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration
import com.nopalsoft.slamthebird.handlers.GameServicesHandler
import org.robovm.apple.foundation.NSAutoreleasePool
import org.robovm.apple.uikit.UIApplication

class IOSLauncher : IOSApplication.Delegate(), GameServicesHandler {
    override fun createApplication(): IOSApplication {
        val config = IOSApplicationConfiguration()
        return IOSApplication(MainSlamBird(), config)
    }

    override fun submitScore(score: Long) {
    }

    override fun unlockAchievement(achievementId: String) {
    }

    override fun getLeaderboard() {
    }

    override fun getAchievements() {
    }

    override fun isSignedIn(): Boolean {
        return false
    }

    override fun signIn() {
    }

}

fun main(argv: Array<String>) {
    val pool = NSAutoreleasePool()
    UIApplication.main<UIApplication, IOSLauncher>(argv, null, IOSLauncher::class.java)
    pool.close()
}