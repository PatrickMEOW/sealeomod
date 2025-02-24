package io.github.patrickmeow.sealeo.features

import io.github.patrickmeow.sealeo.features.settings.Setting
import io.github.patrickmeow.sealeo.features.settings.impl.HiddenModule
import net.minecraft.network.Packet
import net.minecraft.network.play.client.C01PacketChatMessage
import net.minecraft.network.play.server.S28PacketEffect
import net.minecraftforge.common.MinecraftForge
import org.lwjgl.input.Keyboard
import kotlin.reflect.full.hasAnnotation

abstract class Module(
    val name: String,
    val description: String,
    val category: Category,
    key: Int? = Keyboard.KEY_B,
    toggled: Boolean = false,
) {


    @Transient
    val hidden = this::class.hasAnnotation<HiddenModule>()

    val alwaysActive: Boolean = false

    val settings: ArrayList<Setting<*>> = ArrayList()

    var enabled: Boolean = toggled


    init {
        //MinecraftForge.EVENT_BUS.register(this)
    }

    open fun onEnable() {
        if(!alwaysActive) {
            MinecraftForge.EVENT_BUS.register(this)
        }
    }

    open fun onDisable() {
        if(!alwaysActive) {
            MinecraftForge.EVENT_BUS.unregister(this)
        }
    }

    open fun setToggled() {
        enabled = !enabled
        if(enabled) onEnable()
        else onDisable()
    }

    fun onPacket(packet: Packet<*>) {
        if(packet is C01PacketChatMessage) {
            //TODO handle chat messages
        }


    }

    fun <K : Setting<*>> register(setting: K): K {
        settings.add(setting)
        return setting
    }



}