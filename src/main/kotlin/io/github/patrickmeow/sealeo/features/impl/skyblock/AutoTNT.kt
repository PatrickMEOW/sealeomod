package io.github.patrickmeow.sealeo.features.impl.skyblock

import io.github.patrickmeow.sealeo.Sealeo.mc
import io.github.patrickmeow.sealeo.features.Category
import io.github.patrickmeow.sealeo.features.Module
import io.github.patrickmeow.sealeo.features.settings.impl.BooleanSetting
import io.github.patrickmeow.sealeo.utils.TickDelays
import io.github.patrickmeow.sealeo.utils.chat
import io.github.patrickmeow.sealeo.utils.leftClick
import net.minecraft.block.Block
import net.minecraft.util.BlockPos
import net.minecraft.util.MovingObjectPosition.MovingObjectType
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import java.util.*

object AutoTNT: Module(
    "Auto TNT",
    "Automatically uses superboom",
    Category.SKYBLOCK
) {

    var blocksCorrect: Int = 0
    var foundTNT = false
    var currentItem = 0


    var swapBack by BooleanSetting("Swap back", "Automatically swaps back to previous item")

    private fun swapBack() {
        mc.thePlayer.inventory.currentItem = currentItem
    }

    private fun switchToTNT() {
        for (i in 0 until 9) {
            val stack = mc.thePlayer.inventory.mainInventory[i]
            if (stack != null && stack.displayName.lowercase(Locale.getDefault()).contains("tnt")) {
                foundTNT = true
                mc.thePlayer.inventory.currentItem = i
                chat("Using TNT")

                TickDelays.addDelayedTask(1) {
                    leftClick()
                }
                TickDelays.addDelayedTask(2) {
                    swapBack()
                }

                break
            }
        }
        if (!foundTNT) {
            chat("No TNT in hotbar")
            //CooldownManager.setCooldown("TNT", 2000L)
        }
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if(mc.thePlayer == null) return
        blocksCorrect = 0

        val objectMouseOver = mc.objectMouseOver ?: return

        if (objectMouseOver.typeOfHit != MovingObjectType.BLOCK) return
        val blockPos = objectMouseOver.blockPos ?: return
        val block = mc.theWorld.getBlockState(blockPos).block ?: return
        val metadata = block.getMetaFromState(mc.theWorld.getBlockState(blockPos))
        if (metadata != 2 || block.registryName != "minecraft:stonebrick") return

        val directions = listOf(
            BlockPos(0, 1, 0),
            BlockPos(0, -1, 0),
            BlockPos(1, 0, 0),
            BlockPos(-1, 0, 0),
            BlockPos(0, 0, 1),
            BlockPos(0, 0, -1)
        )

        directions.forEach { direction ->
            for (i in 1..5) {
                val newPos = blockPos.add(direction.x * i, direction.y * i, direction.z * i)
                if (checkBlock(newPos)) {
                    blocksCorrect += 1
                } else {
                    break
                }
            }
        }

        if (blocksCorrect > 5) switchToTNT()
    }



    private fun checkBlock(blockPos: BlockPos) : Boolean {

        val block = mc.theWorld.getBlockState(blockPos).block
        val metadata = block.getMetaFromState(mc.theWorld.getBlockState(blockPos))
        return !(metadata != 2 || block.registryName != "minecraft:stonebrick")
    }





}