package io.github.patrickmeow.sealeo.features.impl.movement

import io.github.patrickmeow.sealeo.Sealeo.mc
import io.github.patrickmeow.sealeo.features.Category
import io.github.patrickmeow.sealeo.features.Module
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent

object Simulation : Module(
    "Simulation",
    "Simulates 500 speed and lava bounce",
    Category.SKYBLOCK
) {

    private var onSinglePlayer = false

    @SubscribeEvent
    fun onServer(event: FMLNetworkEvent.ClientConnectedToServerEvent) {
        onSinglePlayer = event.isLocal
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (!onSinglePlayer) return
        if (mc.thePlayer == null) return
        mc.thePlayer.getEntityAttribute(SharedMonsterAttributes.movementSpeed).baseValue = 0.12000
        mc.thePlayer.capabilities.setPlayerWalkSpeed(0.1200F)


    }


}