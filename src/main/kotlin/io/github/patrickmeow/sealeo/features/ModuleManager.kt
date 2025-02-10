package io.github.patrickmeow.sealeo.features

import io.github.patrickmeow.sealeo.Sealeo.mc
import io.github.patrickmeow.sealeo.events.ChatReceivedEvent
import io.github.patrickmeow.sealeo.events.InputEvent
import io.github.patrickmeow.sealeo.features.impl.movement.Blink
import io.github.patrickmeow.sealeo.features.impl.movement.Simulation
import io.github.patrickmeow.sealeo.features.impl.movement.ToggleSprint
import io.github.patrickmeow.sealeo.features.impl.render.ClickGuiModule
import io.github.patrickmeow.sealeo.features.impl.render.HitColor
import io.github.patrickmeow.sealeo.features.impl.skyblock.*
import io.github.patrickmeow.sealeo.features.settings.impl.KeybindSetting
import net.minecraft.network.Packet
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.input.Keyboard

object ModuleManager {

    data class MessageFunction(val msg: String, val function: () -> Unit)
    data class PacketFunction<T: Packet<*>>(val packetType: Class<T>, val function: (T) -> Unit)

    val messageFunctions = mutableListOf<MessageFunction>()
    val packetFunctions = mutableListOf<PacketFunction<Packet<*>>>()
    val modules: ArrayList<Module> = arrayListOf(ToggleSprint, SlotBinding, Simulation, ClickGuiModule, RiftHelper, MobESP, AutoTNT, HitColor, BerberisMacro)
    var targetYaw = 0.0f
    var targetPitch = 0.0f
    var isRotating = false

    fun getModule(name: String): Module? = modules.firstOrNull { it.name.equals(name, true) }

    fun registerModule(module: Module) {
        modules.add(module)
    }

    init {
        for (module in modules) {
            module.register(KeybindSetting("Toggle", "toggles", Keyboard.KEY_L)).onPress {
                module.setToggled()
            }
        }
    }

    @SubscribeEvent
    fun onKey(event: InputEvent.Keyboard) {
        for (module in modules) {
            for (setting in module.settings) {
                if (setting is KeybindSetting) {
                    setting.checkAndExecute()
                }
            }
        }
    }

    fun addModule(module: Module) {
        modules.add(module)
    }

    @SubscribeEvent(receiveCanceled = true)
    fun onMessage(event: ChatReceivedEvent) {
        messageFunctions.forEach {
            if (event.message == it.msg) it.function.invoke()
        }
    }

    fun onChat(message: String, func: () -> Unit) {
        messageFunctions.add(MessageFunction(message, func))
    }

    fun rotateSmoothlyTo(yaw: Float, pitch: Float) {
        isRotating = true
        targetYaw = yaw
        targetPitch = pitch
    }

    @SubscribeEvent
    fun onRender(event: RenderWorldLastEvent) {
        if (mc.thePlayer == null) return

        if (isRotating) {
            val currentYaw = mc.thePlayer.rotationYaw
            val currentPitch = mc.thePlayer.rotationPitch

            if (currentYaw != targetYaw) {
                val yawDifference = targetYaw - currentYaw
                val yawChange = if (Math.abs(yawDifference) < 3f) yawDifference else 3f * Math.signum(yawDifference)
                mc.thePlayer.rotationYaw += yawChange
            }

            if (currentPitch != targetPitch) {
                val pitchDifference = targetPitch - currentPitch
                val pitchChange = if (Math.abs(pitchDifference) < 3f) pitchDifference else 3f * Math.signum(pitchDifference)
                mc.thePlayer.rotationPitch += pitchChange
            }

            if (mc.thePlayer.rotationYaw == targetYaw && mc.thePlayer.rotationPitch == targetPitch) {
                isRotating = false
            }
        }
    }
}
