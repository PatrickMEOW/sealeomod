package io.github.patrickmeow.sealeo.features

import io.github.patrickmeow.sealeo.events.ChatReceivedEvent
import io.github.patrickmeow.sealeo.events.InputEvent
import io.github.patrickmeow.sealeo.features.impl.movement.BetterBlink
import io.github.patrickmeow.sealeo.features.impl.movement.Blink
import io.github.patrickmeow.sealeo.features.impl.movement.Blink.settings
import io.github.patrickmeow.sealeo.features.impl.movement.Simulation
import io.github.patrickmeow.sealeo.features.impl.movement.ToggleSprint
import io.github.patrickmeow.sealeo.features.impl.render.ClickGuiModule
import io.github.patrickmeow.sealeo.features.impl.render.MobESP
import io.github.patrickmeow.sealeo.features.impl.skyblock.SlotBinding
import io.github.patrickmeow.sealeo.features.settings.impl.KeybindSetting
import net.minecraft.network.Packet
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import org.lwjgl.input.Keyboard


object ModuleManager {


    data class MessageFunction(val msg: String, val function: () -> Unit)
    data class PacketFunction<T: Packet<*>>(val packetType: Class<T>, val function: (T) -> Unit)

    val messageFunctions = mutableListOf<MessageFunction>()
    val packetFunctions = mutableListOf<PacketFunction<Packet<*>>>()
    val modules: ArrayList<Module> = arrayListOf(Blink, ToggleSprint, MobESP, SlotBinding, Simulation, ClickGuiModule)


    fun getModule(name : String): Module? = modules.firstOrNull {it.name.equals(name, true)}

    fun registerModule(module: Module) {
        modules.add(module)
    }


    init {
        for(module in modules) {
            module.register(KeybindSetting("Toggle", "toggles", Keyboard.KEY_L)).onPress {
                module.setToggled()
            }
        }
    }


    @SubscribeEvent
    fun onKey(event: InputEvent.Keyboard) {
        for(module in modules) {
            for(setting in module.settings) {
                if(setting is KeybindSetting ) {
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



}