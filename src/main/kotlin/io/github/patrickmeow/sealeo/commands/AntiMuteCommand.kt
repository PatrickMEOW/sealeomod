package io.github.patrickmeow.sealeo.commands

import io.github.patrickmeow.sealeo.features.impl.skyblock.AntiMute.blockedStrings
import io.github.patrickmeow.sealeo.utils.chat
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

object AntiMuteCommand : CommandBase() {

    override fun getRequiredPermissionLevel(): Int {
        return 0
    }

    override fun getCommandName(): String {
        return "chat"
    }

    override fun getCommandUsage(sender: ICommandSender?): String {
        return "/chat"
    }

    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        if (args != null) {
            if(args.size > 1) {
                if(args[0] == "add") {
                    blockedStrings.add(args[1])
                    chat("Added " + args[1])
                }
            }
        }

    }
}