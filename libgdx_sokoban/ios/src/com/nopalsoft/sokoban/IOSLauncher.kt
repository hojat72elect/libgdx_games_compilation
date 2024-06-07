package com.nopalsoft.sokoban

import com.badlogic.gdx.backends.iosrobovm.IOSApplication
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration
import org.robovm.apple.foundation.NSAutoreleasePool
import org.robovm.apple.uikit.UIApplication

class IOSLauncher : IOSApplication.Delegate() {
    override fun createApplication() = IOSApplication(MainSokoban(), IOSApplicationConfiguration())
}

fun main(args: Array<String>) {
    val pool = NSAutoreleasePool()
    UIApplication.main<UIApplication, IOSLauncher>(args, null, IOSLauncher::class.java)
    pool.close()
}