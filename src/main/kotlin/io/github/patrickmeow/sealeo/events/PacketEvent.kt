package io.github.patrickmeow.sealeo.events


import net.minecraft.network.Packet
import net.minecraftforge.fml.common.eventhandler.Cancelable
import net.minecraftforge.fml.common.eventhandler.Event

@Cancelable
abstract class PacketEvent(val packet: Packet<*>) : Event() {

    class PacketReceiveEvent(packet: Packet<*>) : PacketEvent(packet)

    class PacketSendEvent(packet: Packet<*>) : PacketEvent(packet)
}
