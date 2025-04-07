package io.github.patrickmeow.sealeo.features.impl.render

import io.github.patrickmeow.sealeo.features.Category
import io.github.patrickmeow.sealeo.features.Module
import io.github.patrickmeow.sealeo.features.settings.impl.KeybindSetting
import io.github.patrickmeow.sealeo.features.settings.impl.NumberSetting
import io.github.patrickmeow.sealeo.utils.leftClick
import io.github.patrickmeow.sealeo.utils.rightClick
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse

object Autoclicker : Module(
    "Auto clicker",
    "Automatically clicks",
    Category.RENDER
) {

    private val keybind by KeybindSetting("Click bind", "starts clicking", Keyboard.KEY_Y).onPress {
        leftClick()
    }

    private var cps by NumberSetting("Cps", "Auto clicker speed", 5f, 1f, 20f, 1f)

    private var lastClick = 0L
    private var lastClickRight = 0L



    @SubscribeEvent
    fun onRender(event: RenderWorldLastEvent) {

        if(Mouse.isButtonDown(1)) {
            if(System.currentTimeMillis() - (1000 / cps.toInt()) + Math.random() * 10 >= lastClickRight) {
                rightClick()
                lastClickRight = System.currentTimeMillis()
            }
        }

        if(Mouse.isButtonDown(0)) {
            if(System.currentTimeMillis() - (1000 / cps.toInt()) + Math.random() * 10 >= lastClick) {
                leftClick()
                lastClick = System.currentTimeMillis()
            }
        }
    }
}