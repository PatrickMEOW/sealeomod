package io.github.patrickmeow.sealeo

import io.github.patrickmeow.sealeo.Sealeo.mc
import io.github.patrickmeow.sealeo.commands.AntiMuteCommand
import io.github.patrickmeow.sealeo.commands.pvCommand
import io.github.patrickmeow.sealeo.features.ModuleManager
import io.github.patrickmeow.sealeo.features.impl.render.ClickGuiModule
import io.github.patrickmeow.sealeo.utils.RenderUtils
import io.github.patrickmeow.sealeo.utils.RoundedRect
import io.github.patrickmeow.sealeo.utils.SealeoFont
import io.github.patrickmeow.sealeo.utils.TickDelays
import io.github.patrickmeow.sealeo.utils.apiutils.GetData
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.GuiScreen
import net.minecraft.init.Blocks
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.resources.IResource
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import org.apache.commons.io.IOUtils
import java.io.IOException
import java.lang.RuntimeException

@Mod(modid = "sealeomod", useMetadata = true)
class SealeoMod {


    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        //Blink.loadRoutes()
        try {
            val resource: IResource = Minecraft.getMinecraft().getResourceManager()
                .getResource(ResourceLocation("test:test.txt"))
            IOUtils.copy(resource.getInputStream(), System.out)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

        println("Dirt: ${Blocks.dirt.unlocalizedName}")
	    // Below is a demonstration of an access-transformed class access.
        ClientCommandHandler.instance.registerCommand(pvCommand)
        ClientCommandHandler.instance.registerCommand(AntiMuteCommand)
	    println("Color State: " + GlStateManager.Color());
        MinecraftForge.EVENT_BUS.register(this)
        MinecraftForge.EVENT_BUS.register(TickDelays())
        MinecraftForge.EVENT_BUS.register(ModuleManager)
        MinecraftForge.EVENT_BUS.register(ClickGuiModule)
        MinecraftForge.EVENT_BUS.register(RenderUtils)
        RoundedRect.initShaders()
        SealeoFont.init()
        GetData.parseHypixelData("f6b21a65-85a1-47f3-9d8e-ac342d011066")
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if(screen == null) return
        mc.displayGuiScreen(screen)
        screen = null
    }

    companion object {
        var screen: GuiScreen? = null
    }

}
