package io.github.patrickmeow.sealeo.features.impl.movement

import io.github.patrickmeow.sealeo.Sealeo.mc
import io.github.patrickmeow.sealeo.events.PacketEvent
import io.github.patrickmeow.sealeo.utils.PacketUtils
import io.github.patrickmeow.sealeo.utils.chat
import net.minecraft.client.settings.KeyBinding
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent
import org.lwjgl.input.Keyboard

object BetterBlink {

    private val performBlink = KeyBinding("Perform Blink", Keyboard.KEY_X, "Sealeo")
    private val cancelTimes = mutableListOf<Long>()
    private val simulatedPackets = mutableListOf<Blink.BlinkPacket>()
    private var numPackets: Int = 0

    init {
        ClientRegistry.registerKeyBinding(performBlink)
    }

    @SubscribeEvent
    fun onPacketSend(event: PacketEvent.PacketSendEvent) {
        val currentTime = System.currentTimeMillis()
        cancelTimes.removeIf { currentTime - it > 20000 }
        if (event.packet.javaClass.name == "net.minecraft.network.play.client.C03PacketPlayer" || event.packet is C03PacketPlayer.C05PacketPlayerLook) {
            // event.isCanceled = true
            cancelTimes.add(currentTime)
            // chat("Canceled")
            numPackets = cancelTimes.size
        }
    }

    @SubscribeEvent
    fun onKeybind(event: KeyInputEvent) {
        if (performBlink.isPressed) {
            // Get player's yaw and pitch
            val yaw = mc.thePlayer.rotationYaw
            val pitch = mc.thePlayer.rotationPitch
            // Convert yaw and pitch to radians
            val yawRad = Math.toRadians(yaw.toDouble())
            val pitchRad = Math.toRadians(pitch.toDouble())
            // Calculate direction vector
            val xz = Math.cos(pitchRad)
            val dx = -xz * Math.sin(yawRad)
            val dy = -Math.sin(pitchRad)
            val dz = xz * Math.cos(yawRad)
            val speed = 0.215 // Walking speed in blocks per tick
            var posX = mc.thePlayer.posX
            var posY = mc.thePlayer.posY
            var posZ = mc.thePlayer.posZ
            // Simulate packets for movement in the direction
            for (i in 0..30) {
                posX += dx * speed
                //posY += dy * speed
                posZ += dz * speed
                simulatedPackets.add(Blink.BlinkPacket(posX, posY, posZ, mc.thePlayer.onGround))
            }
            for (packet in simulatedPackets) {
                PacketUtils.sendPacket(
                    C03PacketPlayer.C04PacketPlayerPosition(
                        packet.x,
                        packet.y,
                        packet.z,
                        packet.onGround
                    )
                )
                //chat("Sent ${packet.x} ${packet.y} ${packet.z} ${packet.onGround}")
            }
            val lastPacket = simulatedPackets.last()
            mc.thePlayer?.setPosition(lastPacket.x, lastPacket.y, lastPacket.z)
            mc.thePlayer?.setVelocity(0.0, 0.0, 0.0)
            simulatedPackets.clear()
        }
    }
}

