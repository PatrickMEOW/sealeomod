package io.github.patrickmeow.sealeo.features.impl.movement

import io.github.patrickmeow.sealeo.Sealeo.mc
import io.github.patrickmeow.sealeo.events.ChatReceivedEvent
import io.github.patrickmeow.sealeo.events.PacketEvent
import io.github.patrickmeow.sealeo.features.Category
import io.github.patrickmeow.sealeo.features.Module
import io.github.patrickmeow.sealeo.features.settings.impl.KeybindSetting
import io.github.patrickmeow.sealeo.utils.*
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook
import net.minecraft.network.play.server.S08PacketPlayerPosLook
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.lwjgl.input.Keyboard
import kotlin.math.abs
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.lang.reflect.Type

object Blink : Module(
    "Blink",
    "Performs blink in p3",
    Category.RENDER
) {

    // TODO - hardcode routes for relics
    private val recordedPackets = mutableListOf<BlinkPacket>()
    private val routes = mutableListOf<BlinkRoute>()
    private var numPackets: Int = 0
    private var isRecording: Boolean = false
    private var inP3: Boolean = false
    private var firstPos: BlockPos? = null
    private var secondPos: BlockPos? = null
    private var inside: Boolean = false
    private val cancelTimes = mutableListOf<Long>()
    private var lastBlinkTime: Long = 0
    private var receivedS08: Boolean = false


    private val keybind by KeybindSetting("Recording", "record", Keyboard.KEY_B).onPress {
        if(!enabled) return@onPress
        if (isRecording) {
            secondPos = mc.thePlayer?.position
            isRecording = false
            chat("Recorded ${recordedPackets.size} packets")
            val routePackets = recordedPackets.toMutableList()
            routes.add(BlinkRoute(routePackets.size, "Blink", firstPos!!, secondPos!!, routePackets))
            saveRoutes()
            /*
                for(packet in routePackets) {
                println(packet.x.toString() + " " + packet.y.toString() + " " + packet.z.toString())
            }
             */
            recordedPackets.clear()
        } else {
            firstPos = mc.thePlayer?.position
            isRecording = true
        }
    }


    private val undoRoute by KeybindSetting("Delete route", "Deletes route", Keyboard.KEY_C).onPress {
        routes.removeLastOrNull()
        saveRoutes()
    }

    private val gson = Gson()
    private val routesFile = File(mc.mcDataDir, "config/sealeo/blink-routes.json")

    @SubscribeEvent(receiveCanceled = true)
    fun onMessage(event: ChatReceivedEvent) {
        when (event.message) {
            equals(Regex("\\[BOSS] Goldor: Who dares trespass into my domain\\?")).toString() -> inP3 = true
            equals(Regex("The Core entrance is opening!")).toString() -> inP3 = false
        }
    }

    @SubscribeEvent
    fun onPacketReceive(event: PacketEvent.PacketReceiveEvent) {
        if(event.packet is S08PacketPlayerPosLook) {
            receivedS08 = true
            TickDelays.addDelayedTask(1) {
                receivedS08 = false
            }
        }
    }

    @SubscribeEvent
    fun onPacketSend(event: PacketEvent.PacketSendEvent) {
        if(event.packet !is C03PacketPlayer) return
        if(event.packet is C06PacketPlayerPosLook) return
        if(!mc.thePlayer.onGround) return
        val currentTime = System.currentTimeMillis()

        cancelTimes.removeIf { currentTime - it > 20000 }

        event.isCanceled = true
        cancelTimes.add(currentTime)
        numPackets = cancelTimes.size
    }

    @SubscribeEvent
    fun onRender(event: RenderWorldLastEvent) {
        if (mc.thePlayer == null) return
        for (route in routes) {
            val fPos = Vec3(route.first.x.toDouble(), route.first.y.toDouble(), route.first.z.toDouble())
            val sPos = Vec3(route.second.x.toDouble(), route.second.y.toDouble(), route.second.z.toDouble())
            RenderUtils.drawCyl(fPos, 1.0f, 1.0f - 0.05f, -0.01f, 120, 1, 0f, 0f, 0f)
            RenderUtils.renderOutlineAABB(AxisAlignedBB(sPos.xCoord, sPos.yCoord, sPos.zCoord, sPos.xCoord + 1, sPos.yCoord + 1, sPos.zCoord + 1), Vector3(0.36f, 0.87f, 0.14f), 1.0f, false, 2.0f)
            RenderUtils.renderFilledAABB(AxisAlignedBB(sPos.xCoord, sPos.yCoord, sPos.zCoord, sPos.xCoord + 1, sPos.yCoord + 1, sPos.zCoord + 1), Vector3(0.36f, 0.87f, 0.14f), 0.15f, false)
        }
    }

    @SubscribeEvent
    fun onRenderOverlay(event: RenderGameOverlayEvent.Post) {




        val fontRenderer = mc.fontRendererObj
        if(event.type != RenderGameOverlayEvent.ElementType.ALL) return
        val screenWidth = event.resolution.scaledWidth
        val screenHeight = event.resolution.scaledHeight
        val itemStack = mc.thePlayer?.inventory?.mainInventory!![0]
        //println(itemStack?.item?.registryName.toString())
        //mc.renderItem?.renderItemAndEffectIntoGUI(itemStack, 300, 300)

        if (isRecording) {
            fontRenderer?.drawString("Recording", 10, 400, -1)
        }
        fontRenderer?.drawString(numPackets.toString(), (screenWidth / 2) + 10, screenHeight / 2, -1)
        //RoundedRect.drawRectangle(UMatrixStack.Compat.get(), 200f, 200f, 80f, 40f, Color.BLACK, Color.BLUE, Color.BLACK, 1.0f, 2.0f, 2.0f, 2.0f, 2.0f, 1.0f, Color.BLACK, 1, 0f)
        //RenderUtils.roundedRectangle(200, 200, 100, 50, Color.BLACK, 8f)
    }

    private fun performBlink(packets: MutableList<BlinkPacket>) {
        val currentTime = System.currentTimeMillis()
        if(currentTime - lastBlinkTime < 300) return
        if(packets.size > numPackets) return
        for(packet in packets) {
            PacketUtils.sendPacket(
                C03PacketPlayer.C04PacketPlayerPosition(
                    packet.x,
                    packet.y,
                    packet.z,
                    packet.onGround
                )
            )
            numPackets--
        }
        chat("Blinking with " + packets.size + " packets")
        lastBlinkTime = currentTime
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if(mc.thePlayer == null) return
        for (route in routes) {
            if (isPlayerInsideCircle(1.0f, route)) {
                if (!inside) {
                    inside = true
                    if (route.recordedPackets.size > numPackets) {
                        chat("Not enough packets to perform blink")
                        continue
                    }
                    performBlink(route.recordedPackets)

                    if (route.recordedPackets.isNotEmpty()) {
                        val lastPacket = route.recordedPackets.last()
                        mc.thePlayer?.setPosition(lastPacket.x, lastPacket.y, lastPacket.z)
                        mc.thePlayer?.setVelocity(0.0, 0.0, 0.0)
                    }
                    inside = true
                }
            } else {
                inside = false
            }
        }

        if (!isRecording) return
        if (recordedPackets.isEmpty()) {
            recordedPackets.add(BlinkPacket(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround))
        } else {
            val lastRecorded = recordedPackets.last()
            if (mc.thePlayer.posX != lastRecorded.x || mc.thePlayer.posY != lastRecorded.y || mc.thePlayer.posZ != lastRecorded.z || mc.thePlayer.onGround != lastRecorded.onGround) {
                recordedPackets.add(BlinkPacket(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround))
            }
        }
    }

    private fun isPlayerInsideCircle(size: Float, route: BlinkRoute): Boolean {
        val xp = mc.thePlayer.posX
        val yp = mc.thePlayer.posY
        val zp = mc.thePlayer.posZ
        val distX = abs(xp - route.first.x)
        val distZ = abs(zp - route.first.z)

        val playerPos = Vec3(xp, yp, zp)
        val circleCenter = Vec3(route.first.x.toDouble(), route.first.y.toDouble(), route.first.z.toDouble())
        if (circleCenter.yCoord + 0.2F <= playerPos.yCoord || playerPos.yCoord <= circleCenter.yCoord - 0.6F) {
            return false
        }
        return distX < size / 2 && distZ < size / 2
    }
    private var isWorldLoaded: Boolean = true

    private fun saveRoutes() {
        ensureDirectoryExists(routesFile)
        val routeType: Type = object : TypeToken<List<BlinkRoute>>() {}.type
        val json = gson.toJson(routes, routeType)
        FileWriter(routesFile).use { it.write(json) }
    }

    private fun ensureDirectoryExists(file: File) {
        val parentDir = file.parentFile
        if (!parentDir.exists()) {
            parentDir.mkdirs()
        }
    }

    fun loadRoutes() {
        if (!routesFile.exists()) return

        val routeType: Type = object : TypeToken<List<BlinkRoute>>() {}.type
        val json = FileReader(routesFile).use { it.readText() }
        routes.clear()
        routes.addAll(gson.fromJson(json, routeType))
        println("Routes loaded from ${routesFile.absolutePath}")
    }

    data class BlinkPacket(var x: Double, var y: Double, var z: Double, var onGround: Boolean)

    data class BlinkRoute(var numOfPackets: Int, var name: String, var first: BlockPos, var second: BlockPos, var recordedPackets: MutableList<BlinkPacket>)
}
