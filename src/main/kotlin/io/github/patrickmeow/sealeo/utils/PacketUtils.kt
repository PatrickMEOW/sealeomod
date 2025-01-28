package io.github.patrickmeow.sealeo.utils

import net.minecraft.client.Minecraft
import net.minecraft.network.Packet

object PacketUtils {
    fun sendPacket(packet: Packet<*>) {
        Minecraft.getMinecraft().netHandler.networkManager.sendPacket(packet)
    }
}
