package io.github.patrickmeow.sealeo.features.impl.skyblock

import io.github.patrickmeow.sealeo.Sealeo.mc
import io.github.patrickmeow.sealeo.events.BlockChangeEvent
import io.github.patrickmeow.sealeo.events.PacketEvent.PacketReceiveEvent
import io.github.patrickmeow.sealeo.features.Category
import io.github.patrickmeow.sealeo.features.Module
import io.github.patrickmeow.sealeo.features.ModuleManager.rotateSmoothlyTo
import io.github.patrickmeow.sealeo.features.settings.impl.KeybindSetting
import io.github.patrickmeow.sealeo.features.settings.impl.NumberSetting
import io.github.patrickmeow.sealeo.utils.calcYawAndPitch
import io.github.patrickmeow.sealeo.utils.leftClick
import io.github.patrickmeow.sealeo.utils.rotateTo
import io.github.patrickmeow.sealeo.utils.sendCommand
import net.minecraft.client.settings.KeyBinding
import net.minecraft.init.Blocks
import net.minecraft.network.play.server.S02PacketChat
import net.minecraft.util.BlockPos
import net.minecraft.util.ChatComponentText
import net.minecraft.util.MovingObjectPosition.MovingObjectType
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import org.lwjgl.input.Keyboard
import java.util.*

object BerberisMacro : Module(
    "Berberis macro",
    "Macro for farming berberis",
    Category.SKYBLOCK
) {

    var blockedStrings = mutableListOf<String>()
    var nearBerberis: Boolean = true
    private val spawnedBerberis = mutableListOf<BlockPos>()
    var targetYaw: Float = 0.0f
    var targetPitch: Float = 0.0f
    var isRotating = false


    val spawnBerberis by KeybindSetting("Spawns", "spawns", Keyboard.KEY_N).onPress {
        spawnDeadBushes()
    }

    private val rotationSpeed by NumberSetting("Rotation speed", "Rotation speed", 2f, 1f, 10f, 0.5f)

    private fun spawnDeadBushes() {
        val player = mc.thePlayer ?: return
        val rand = Random()
        val playerX = player.posX
        val playerY = player.posY
        val playerZ = player.posZ

        val numberOfBushes = 5

        for (i in 1..numberOfBushes) {

            val randomX = playerX + (rand.nextInt(9) - 4)
            val randomZ = playerZ + (rand.nextInt(9) - 4)


            sendCommand("setblock $randomX $playerY $randomZ minecraft:deadbush")
        }
    }


    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if(mc.thePlayer == null) return
        val blockUnder = mc.theWorld.getBlockState(BlockPos(mc.thePlayer.posX.toInt(), (mc.thePlayer.posY -1).toInt(), mc.thePlayer.posZ.toInt()))
        if(blockUnder.block == Blocks.bedrock) return
        if(spawnedBerberis.size < 1) return


        //nearBerberis = false
        val playerPos = mc.thePlayer?.position
        val positions = BlockPos.getAllInBox(playerPos?.add(10, 0, 10), playerPos?.add(-10, 0, -10))



        for(position in positions) {
            val blockState = mc.theWorld.getBlockState(position)
            if(blockState.block == Blocks.deadbush) nearBerberis = true
        }


        val target = calcYawAndPitch(spawnedBerberis[0], 0.5)

        //rotateTo(mc.thePlayer.rotationYaw + 0.1f, mc.thePlayer.rotationPitch)


        // Teleport check



        if(nearBerberis) rotateSmoothlyTo(target.y, target.z, rotationSpeed.toFloat())
        if(target.x <= 4.5) {

            // Player check
            val movingObjectPosition = mc.objectMouseOver
            if(movingObjectPosition.typeOfHit == MovingObjectType.ENTITY) return

            leftClick()
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.keyCode, false)
            //KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.keyCode, false)
        } else {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.keyCode, true)
            //KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.keyCode, true)
        }
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




}




