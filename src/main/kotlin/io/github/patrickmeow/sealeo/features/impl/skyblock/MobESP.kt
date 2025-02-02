package io.github.patrickmeow.sealeo.features.impl.skyblock

import io.github.patrickmeow.sealeo.Sealeo.mc
import io.github.patrickmeow.sealeo.features.Category
import io.github.patrickmeow.sealeo.features.Module
import io.github.patrickmeow.sealeo.utils.RenderUtils
import io.github.patrickmeow.sealeo.utils.Vector3
import net.minecraft.entity.monster.EntityZombie
import net.minecraft.util.AxisAlignedBB
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent


import net.minecraft.entity.monster.EntityMob
import net.minecraft.tileentity.TileEntityChest
import net.minecraft.util.BlockPos

object MobESP : Module(
    "Mob ESP",
    "Highlights mobs through walls",
    Category.SKYBLOCK
) {

    @SubscribeEvent
    fun onRenderWorldLast(event: RenderWorldLastEvent) {
        val partialTicks = event.partialTicks

        for (entity in mc.theWorld.loadedEntityList) {
            if (entity is EntityZombie || entity is EntityMob) {
                // Interpolate position
                val interpolatedX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks
                val interpolatedY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks
                val interpolatedZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks

                val boundingBox = AxisAlignedBB(
                    interpolatedX - entity.width / 2,
                    interpolatedY,
                    interpolatedZ - entity.width / 2,
                    interpolatedX + entity.width / 2,
                    interpolatedY + entity.height,
                    interpolatedZ + entity.width / 2
                )

                RenderUtils.renderOutlineAABB(
                    boundingBox.expand(0.1, 0.1, 0.1),  // Slightly expand the bounding box
                    color = Vector3(0.15f, 0.5f, 0.7f),
                    alpha = 1.0f,
                    depth = false,
                    width = 2.0f
                )
            }
        }

        for(tileEntity in mc.theWorld.loadedTileEntityList) {
            if(tileEntity is TileEntityChest) {
                val chestPos: BlockPos = tileEntity.pos
                val block = mc.theWorld.getBlockState(chestPos).block
                val chestAABB = block.getSelectedBoundingBox(mc.theWorld, chestPos)

                RenderUtils.renderFilledAABB(chestAABB, color = Vector3(0.36f, 0.87f, 0.14f), 0.15f, false)
                RenderUtils.renderOutlineAABB(chestAABB, color = Vector3(0.36f, 0.87f, 0.14f), 1f, false, 1.5f)

            }
        }
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (mc.thePlayer == null) return
    }
}

