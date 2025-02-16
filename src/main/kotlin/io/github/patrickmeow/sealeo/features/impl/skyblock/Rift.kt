package io.github.patrickmeow.sealeo.features.impl.skyblock


import io.github.patrickmeow.sealeo.Sealeo.mc
import io.github.patrickmeow.sealeo.events.BlockChangeEvent
import io.github.patrickmeow.sealeo.events.PacketEvent
import io.github.patrickmeow.sealeo.features.Category
import io.github.patrickmeow.sealeo.features.Module
import io.github.patrickmeow.sealeo.features.settings.impl.BooleanSetting
import io.github.patrickmeow.sealeo.features.settings.impl.ColorSetting
import io.github.patrickmeow.sealeo.features.settings.impl.KeybindSetting
import io.github.patrickmeow.sealeo.utils.HSBColor
import io.github.patrickmeow.sealeo.utils.RenderUtils
import io.github.patrickmeow.sealeo.utils.Vector3
import net.minecraft.init.Blocks
import net.minecraft.network.play.server.S2APacketParticles
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.input.Keyboard
import kotlin.math.sqrt

object RiftHelper : Module(
    "Rift helper",
    "QOL features for rift",
    Category.SKYBLOCK
) {

    val spawnedBerberis = mutableListOf<BlockPos>()
    var renderNext by BooleanSetting("Render next", "Renders next")
    var renderEnable by KeybindSetting("xd", "xd", Keyboard.KEY_H).onPress {
        renderNext = true
    }

    private var col by ColorSetting("Overlay color", "yes", HSBColor(1f, 1f, 1f, 1f))
    //var col = HSBColor(1f, 1f, 0.2f, 0.6f)
    @SubscribeEvent
    fun onParticle(event: PacketEvent.PacketReceiveEvent) {
        if(event.packet !is S2APacketParticles) return
        println(event.packet.particleType.toString())
        if(event.packet.particleType == EnumParticleTypes.FIREWORKS_SPARK) return
    }



    @SubscribeEvent
    fun onBlockChange(event: BlockChangeEvent) {

        if(event.update.block == Blocks.deadbush && event.old.block == Blocks.air) {
            if(spawnedBerberis.contains(event.pos)) return
            spawnedBerberis.add(event.pos)
        }

        if(event.update.block == Blocks.air && event.old.block == Blocks.deadbush) {
            spawnedBerberis.remove(event.pos)
            println("removed")
        }

    }

    @SubscribeEvent
    fun onRender(event: RenderWorldLastEvent) {
        if(spawnedBerberis.size >= 1) {
            var aabb = Blocks.deadbush.getSelectedBoundingBox(mc.theWorld, spawnedBerberis[0])
            //aabb = aabb.expand(0.0, 0.1, 0.0)

            RenderUtils.renderOutlineAABB(aabb, Vector3(0.36f, 0.87f, 0.14f), 1f, false, 1.5f)
            RenderUtils.renderFilledAABB(aabb, Vector3(col.r.toFloat(), col.g.toFloat(), col.b.toFloat()), 0.6f, false)

            RenderUtils.drawTracer(Vec3(spawnedBerberis[0].x.toDouble(), spawnedBerberis[0].y.toDouble(), spawnedBerberis[0].z.toDouble()), Vector3(0.36f, 0.87f, 0.14f), 2.5f)
        }

        if(spawnedBerberis.size >= 2) {
            var aabbNext = Blocks.deadbush.getSelectedBoundingBox(mc.theWorld, spawnedBerberis[1])
            //aabbNext = aabbNext.expand(0.0, 0.1, 0.0)
            RenderUtils.renderOutlineAABB(aabbNext, Vector3(0.73f, 0.73f, 0.73f), 1f, false, 1.5f)
            RenderUtils.renderFilledAABB(aabbNext, Vector3(0.73f, 0.73f, 0.73f), 0.6f, false)
        }

       // if(spawnedBerberis.size >= 2) {
        //    RenderUtils.renderLineBetweenBlockPos(spawnedBerberis[0], spawnedBerberis[1], Vector3(0.36f, 0.87f, 0.14f), 1.0f)
       // }
    }


    fun calcDist(particlePos: Vec3): Double {
        val dx = mc.thePlayer.posX - particlePos.xCoord
        val dy = mc.thePlayer.posY - particlePos.yCoord
        val dz = mc.thePlayer.posZ - particlePos.zCoord

        return sqrt(dx * dx + dy * dy + dz * dz)
    }








}
