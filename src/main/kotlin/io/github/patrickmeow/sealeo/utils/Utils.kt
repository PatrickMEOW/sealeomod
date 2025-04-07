@file:Suppress("FunctionName")
@file:JvmName("Utils")


package io.github.patrickmeow.sealeo.utils

import io.github.patrickmeow.sealeo.Sealeo.mc
import net.minecraft.client.settings.KeyBinding
import net.minecraft.event.ClickEvent
import net.minecraft.event.HoverEvent
import net.minecraft.util.BlockPos
import net.minecraft.util.ChatComponentText
import net.minecraft.util.ChatStyle
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.Event
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt


fun delayTask(ticks : Int, func: () -> Unit) {
    if(ticks <= 0)
    { func()
        return
    }

}

fun chat(message: String) {
    val player = mc.thePlayer
    val prefix = "§9[§bSealeo§9]"
    player.addChatMessage(ChatComponentText("$prefix §7$message"))
}


fun leftClick() {
    KeyBinding.onTick(mc.gameSettings.keyBindAttack.keyCode)
}

fun rightClick() {
    KeyBinding.onTick(mc.gameSettings.keyBindUseItem.keyCode)
}


fun Event.post(): Boolean {
    return try {
        MinecraftForge.EVENT_BUS.post(this)
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}


fun sendCommand(command: String) {
    mc.thePlayer?.sendChatMessage("/$command")
}


fun rotateTo(yaw: Float, pitch: Float) {
    mc.thePlayer.rotationYaw = yaw
    mc.thePlayer.rotationPitch = pitch
}

fun calcYawAndPitch(pos: BlockPos, yOffset: Double): Vector3 {
    val posX = mc.thePlayer.posX
    val posY = mc.thePlayer.posY + mc.thePlayer.eyeHeight
    val posZ = mc.thePlayer.posZ

    val dist = sqrt((pos.x + 0.5 - posX).pow(2) + (pos.y + yOffset - posY).pow(2) + (pos.z + 0.5 - posZ).pow(2)).toFloat()
    val yaw = -atan2((pos.x + 0.5 - posX), (pos.z + 0.5 - posZ)) * (180 / Math.PI)
    val pitch = -atan2((pos.y + yOffset - posY), sqrt((pos.x + 0.5 - posX).pow(2) + (pos.z + 0.5 - posZ).pow(2))) * (180 / Math.PI)

    return Vector3(dist, (yaw % 360).toFloat(), (pitch % 360).toFloat())
}







