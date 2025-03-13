package io.github.patrickmeow.sealeo.features.impl.render

import io.github.patrickmeow.sealeo.Sealeo.mc
import io.github.patrickmeow.sealeo.events.PacketEvent.PacketReceiveEvent
import io.github.patrickmeow.sealeo.features.Category
import io.github.patrickmeow.sealeo.features.Module
import io.github.patrickmeow.sealeo.utils.chat
import net.minecraft.network.play.server.S2APacketParticles
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.Util
import net.minecraft.util.Vec3
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.math.sqrt

object MageBeam : Module (
    "Mage beam",
    "Customize mage beam in dungeons",
    Category.RENDER
) {

    private var lastTime = System.currentTimeMillis()
    private var closest : Vec3? = null
    private var mageBeams = mutableListOf<Pair<Vec3, Vec3>>()




    @SubscribeEvent
    fun onParticle(event: PacketReceiveEvent) {
        println(event.packet.toString())

        if(event.packet !is S2APacketParticles) return
        if(event.packet.particleType != EnumParticleTypes.FIREWORKS_SPARK) return
        val posX = event.packet.xCoordinate
        val posY = event.packet.yCoordinate
        val posZ = event.packet.zCoordinate
        chat(event.packet.particleType.toString())

        if(closest == null) {
            closest = Vec3(posX, posY, posZ)
            if(calcDist(Vec3(posX, posY, posZ)) < 2) {
                chat(event.packet.particleType.toString())
                println(event.packet.particleType)

                val existingPair = mageBeams.find { it.first.yCoord == posY }

                if (existingPair == null) {
                    mageBeams.add(Pair(closest!!, Vec3(posX, posY, posZ)))
                    chat("added")
                }
            }
        } else {
            if(calcDist(closest!!) > calcDist(Vec3(posX, posY, posZ))) {
               closest = Vec3(posX, posY, posZ)
                chat(calcDist(Vec3(posX, posY, posZ)).toString())
                if(calcDist(Vec3(posX, posY, posZ)) < 2) {
                    chat(event.packet.particleType.toString())
                    println(event.packet.particleType)

                    val existingPair = mageBeams.find { it.first.yCoord == posY }

                    if (existingPair == null) {
                        mageBeams.add(Pair(closest!!, Vec3(posX, posY, posZ)))
                        chat("added")
                    }
                }
            }
        }

        if(calcDist(Vec3(posX, posY, posZ)) < 2) {
            chat(event.packet.particleType.toString())
            println(event.packet.particleType)
        }
    }

    private fun calcDist(particlePos: Vec3): Double {
        val dx = mc.thePlayer.posX - particlePos.xCoord
        val dy = mc.thePlayer.posY - particlePos.yCoord
        val dz = mc.thePlayer.posZ - particlePos.zCoord

        return sqrt(dx * dx + dy * dy + dz * dz)
    }


}