package io.github.patrickmeow.sealeo.features.impl.skyblock

import io.github.patrickmeow.sealeo.events.ChatReceivedEvent
import io.github.patrickmeow.sealeo.features.Category
import io.github.patrickmeow.sealeo.features.Module
import io.github.patrickmeow.sealeo.features.settings.impl.BooleanSetting
import io.github.patrickmeow.sealeo.utils.RenderUtils
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object Timers : Module(
    "Timers",
    "Timers for various items in skyblock",
    Category.SKYBLOCK
) {

    private var gyroTimer by BooleanSetting("Gyro timer", "Enables timer for the Gyrokinetic Wand")
    private var lastUsed = 0L



    @SubscribeEvent
    fun onMessage(event: ChatReceivedEvent) {
        if(event.message.matches(Regex("(?s)(.*(-\\d+ Mana \\(Gravity Storm\\)).*)"))) {
            lastUsed = System.currentTimeMillis()
        }
    }

    @SubscribeEvent
    fun onRender(event: RenderGameOverlayEvent.Post) {
        if(System.currentTimeMillis() - 3000 >= lastUsed) {
            RenderUtils.drawShadowText("Gyro: Ready", 100f, 100f, 0xFF58cf22, 1.5f)
        } else {
            var time = System.currentTimeMillis() - lastUsed
            RenderUtils.drawText((time / 100).toString(), 100f, 100f, -1, 1.2f)
        }
    }

}