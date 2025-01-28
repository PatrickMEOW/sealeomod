package io.github.patrickmeow.sealeo

import io.github.patrickmeow.sealeo.features.ModuleManager
import io.github.patrickmeow.sealeo.features.ModuleManager.modules
import io.github.patrickmeow.sealeo.features.impl.movement.BetterBlink
import io.github.patrickmeow.sealeo.features.impl.movement.Blink
import io.github.patrickmeow.sealeo.features.impl.movement.Simulation
import io.github.patrickmeow.sealeo.features.impl.movement.ToggleSprint
import io.github.patrickmeow.sealeo.features.impl.render.ClickGuiModule
import io.github.patrickmeow.sealeo.features.impl.render.MobESP
import io.github.patrickmeow.sealeo.features.impl.skyblock.SlotBinding
import io.github.patrickmeow.sealeo.utils.RoundedRect
import io.github.patrickmeow.sealeo.utils.TickDelays
import net.minecraft.client.Minecraft
import net.minecraft.init.Blocks
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraft.client.renderer.GlStateManager
import net.minecraftforge.common.MinecraftForge

@Mod(modid = "sealeomod", useMetadata = true)
class SealeoMod {


    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        //Blink.loadRoutes()
        try {
            val resource: net.minecraft.client.resources.IResource = Minecraft.getMinecraft().getResourceManager()
                .getResource(net.minecraft.util.ResourceLocation("test:test.txt"))
            org.apache.commons.io.IOUtils.copy(resource.getInputStream(), java.lang.System.out)
        } catch (e: java.io.IOException) {
            throw java.lang.RuntimeException(e)
        }

        println("Dirt: ${Blocks.dirt.unlocalizedName}")
	    // Below is a demonstration of an access-transformed class access.
	    println("Color State: " + GlStateManager.Color());
        MinecraftForge.EVENT_BUS.register(this)
        MinecraftForge.EVENT_BUS.register(TickDelays())
        MinecraftForge.EVENT_BUS.register(ModuleManager)
        MinecraftForge.EVENT_BUS.register(ClickGuiModule)
        RoundedRect.initShaders()
    }

}
