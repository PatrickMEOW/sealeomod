package io.github.patrickmeow.sealeo.features.impl.skyblock

import io.github.patrickmeow.sealeo.Sealeo.mc
import io.github.patrickmeow.sealeo.events.BlockChangeEvent
import io.github.patrickmeow.sealeo.events.PacketEvent.PacketReceiveEvent
import io.github.patrickmeow.sealeo.features.Category
import io.github.patrickmeow.sealeo.features.Module
import io.github.patrickmeow.sealeo.features.ModuleManager.rotateSmoothlyTo
import io.github.patrickmeow.sealeo.features.impl.skyblock.RiftHelper.BerberisPlot
import io.github.patrickmeow.sealeo.features.settings.impl.BooleanSetting
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

    val berberisPlots = mutableListOf<BerberisPlot>()
    var blockedStrings = mutableListOf<String>()
    var nearBerberis: Boolean = true
    private val spawnedBerberis = mutableListOf<BlockPos>()
    var targetYaw: Float = 0.0f
    var targetPitch: Float = 0.0f
    var isRotating = false
    var timeSinceLast = 0L
    var timeLast = 0L



    init {
        berberisPlots.add(BerberisPlot(-72f, -189f, -56f, -175f))
        println("added berberis plot")
    }

    //val spawnBerberis by KeybindSetting("Spawns", "spawns", Keyboard.KEY_N).onPress {
      //  spawnDeadBushes()
    //}

    private val rotationSpeed by NumberSetting("Rotation speed", "Rotation speed", 2f, 1f, 15f, 0.2f)
    private val antiTp by BooleanSetting("Anti teleport", "Prevents banning after tp")



    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if(mc.thePlayer == null) return
        if(System.currentTimeMillis()  <= timeLast + 400)  {
            return
        }
        val blockUnder = mc.theWorld.getBlockState(BlockPos(mc.thePlayer.posX.toInt(), (mc.thePlayer.posY -1).toInt(), mc.thePlayer.posZ.toInt()))
        if(blockUnder.block == Blocks.bedrock) return
        if(spawnedBerberis.size < 1) return


        //nearBerberis = false
        val playerPos = mc.thePlayer?.position
        val positions = BlockPos.getAllInBox(playerPos?.add(20, 0, 20), playerPos?.add(-20, 0, -20))



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
            if(berberisPlots[0].isPlayerInside(event.pos) && berberisPlots[0].isPlayerInside(mc.thePlayer.position)) {
                spawnedBerberis.add(event.pos)
                timeLast = System.currentTimeMillis()
            }



        }

        if(event.update.block == Blocks.air && event.old.block == Blocks.deadbush) {
            if(berberisPlots[0].isPlayerInside(event.pos) && berberisPlots[0].isPlayerInside(mc.thePlayer.position)) {
                spawnedBerberis.remove(event.pos)
            }

        }

    }




}




