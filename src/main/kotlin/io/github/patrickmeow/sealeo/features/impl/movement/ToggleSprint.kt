package io.github.patrickmeow.sealeo.features.impl.movement

import io.github.patrickmeow.sealeo.Sealeo.mc
import io.github.patrickmeow.sealeo.features.Category
import io.github.patrickmeow.sealeo.features.Module
import net.minecraft.client.settings.KeyBinding.setKeyBindState
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

object ToggleSprint : Module(
    "Toggle Sprint",
    "Automatically sprints",
    Category.MOVEMENT
) {

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        setKeyBindState(mc.gameSettings.keyBindSprint.keyCode, true)
    }

}