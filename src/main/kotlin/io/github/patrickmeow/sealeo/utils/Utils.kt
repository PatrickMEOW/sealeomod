@file:Suppress("FunctionName")
@file:JvmName("Utils")


package io.github.patrickmeow.sealeo.utils

import io.github.patrickmeow.sealeo.Sealeo.mc
import net.minecraft.event.ClickEvent
import net.minecraft.event.HoverEvent
import net.minecraft.util.ChatComponentText
import net.minecraft.util.ChatStyle
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.Event



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




fun Event.post(): Boolean {
    return try {
        MinecraftForge.EVENT_BUS.post(this)
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}



