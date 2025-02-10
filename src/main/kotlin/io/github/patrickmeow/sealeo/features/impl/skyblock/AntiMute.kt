package io.github.patrickmeow.sealeo.features.impl.skyblock

import io.github.patrickmeow.sealeo.Sealeo.mc
import io.github.patrickmeow.sealeo.features.Category
import io.github.patrickmeow.sealeo.features.Module

import net.minecraft.util.ChatComponentText
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object AntiMute : Module(
    "Anti Mute",
    "Blocks bad words",
    Category.SKYBLOCK
) {

    var blockedStrings = mutableListOf<String>()


    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {
        val msg = getMessageAfterFirstWord(event.message.formattedText)
        val cleanMsg = removeFormatting(msg)
        println(cleanMsg)
        if(blockedStrings.contains(cleanMsg)) {
            mc.thePlayer.addChatMessage(ChatComponentText("§cno no no no"))
            event.isCanceled = true
        }
    }

    private fun getMessageAfterFirstWord(message: String): String {
        val parts = message.split(" ", limit = 2)
        return if (parts.size > 1) {
            parts[1].trim()
        } else {
            ""
        }
    }

    private fun removeFormatting(message: String): String {
        return message.replace(Regex("(?i)\\u00A7[0-9A-FK-OR]"), "")
    }
}