package io.github.patrickmeow.sealeo.commands

import io.github.patrickmeow.sealeo.Sealeo
import io.github.patrickmeow.sealeo.SealeoMod
import io.github.patrickmeow.sealeo.features.impl.render.ProfileViewer
import net.minecraft.client.Minecraft
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

object pvCommand : CommandBase() {

    var pvGui: ProfileViewer? = null
    val mc: Minecraft = Minecraft.getMinecraft()

    override fun getRequiredPermissionLevel(): Int {
        return 0
    }

    override fun getCommandName(): String {
        return "pv"
    }

    override fun getCommandUsage(sender: ICommandSender?): String {
        return "/pv"
    }

    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        if(pvGui == null) {
            pvGui = ProfileViewer
        }
        SealeoMod.screen = pvGui
    }

}
