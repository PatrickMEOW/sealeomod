package io.github.patrickmeow.sealeo.features.impl.movement

import io.github.patrickmeow.sealeo.Sealeo.mc
import io.github.patrickmeow.sealeo.features.Category
import io.github.patrickmeow.sealeo.features.Module
import io.github.patrickmeow.sealeo.utils.calcYawAndPitch
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

object Scaffold : Module(
    "Scaffold",
    "Automatically bridges",
    Category.MOVEMENT
) {


    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if(mc.thePlayer == null) return

        val vc = calcYawAndPitch(mc.thePlayer.position, 0.5)
        //println("Yaw:  ${vc.x} Pitch: ${vc.y}")
        println(mc.thePlayer.horizontalFacing.toString())

    }

}