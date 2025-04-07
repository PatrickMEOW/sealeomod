package io.github.patrickmeow.sealeo.features.impl.render

import io.github.patrickmeow.sealeo.Sealeo.mc
import io.github.patrickmeow.sealeo.events.ClickEvent
import io.github.patrickmeow.sealeo.features.Category
import io.github.patrickmeow.sealeo.features.Module
import io.github.patrickmeow.sealeo.utils.RenderUtils
import io.github.patrickmeow.sealeo.utils.TickDelays
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import java.awt.Color


object CpsCounter : Module(
    "CPS counter",
    "Counts CPS",
    Category.RENDER
) {
    private val buttonAnimLeft = ButtonAnim()
    private val buttonAnimRight = ButtonAnim()

    private var leftClicks = mutableListOf<Long>()
    private var rightClicks = mutableListOf<Long>()


    @SubscribeEvent
    fun onLeft(event: ClickEvent.Left) {
        leftClicks.add(System.currentTimeMillis())
        buttonAnimLeft.onClick()
    }

    @SubscribeEvent
    fun onRight(event: ClickEvent.Right) {
        rightClicks.add(System.currentTimeMillis())
        buttonAnimRight.onClick()
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        leftClicks.removeAll() { System.currentTimeMillis() >= it + 1000 }
        rightClicks.removeAll() { System.currentTimeMillis() > it + 1000 }
    }

    @SubscribeEvent
    fun onRenderOverlay(event: RenderGameOverlayEvent.Post) {
        val fontRenderer = mc.fontRendererObj
        if(event.type != RenderGameOverlayEvent.ElementType.ALL) return

        RenderUtils.roundedRectangle(20, 300, 35, 30, buttonAnimLeft.color, 6f)
        RenderUtils.roundedRectangle(57, 300, 35, 30, buttonAnimRight.color, 6f)
        RenderUtils.drawText("LMB", 26f, 306f, -1, 1.0f)
        RenderUtils.drawText("RMB", 63f, 306f, -1, 1.0f)
        val leftPos = if(leftClicks.size >= 10) {
            26f
        } else {
            28f
        }

        val rightPos = if(rightClicks.size >= 10) {
            63f
        } else {
            65f
        }

        RenderUtils.drawText(leftClicks.size.toString(), leftPos, 318f, -1, 0.7f)
        RenderUtils.drawText(rightClicks.size.toString(), rightPos, 318f, -1, 0.7f)
        RenderUtils.drawText("CPS", 32.9f, 318f, -1, 0.7f)
        RenderUtils.drawText("CPS", 69.9f, 318f, -1, 0.7f)
        fontRenderer?.drawString(leftClicks.size.toString() + " " + rightClicks.size.toString(), 10, 400, -1)
    }


    class ButtonAnim() {

        var color = Color(0,0,0,100)
        private var lastClick = 0L


        fun onClick() {
            reset()
            lastClick = System.currentTimeMillis()
            TickDelays.addDelayedTask(0) {
                color = Color(255, 255, 255, 100)
            }
            TickDelays.addDelayedTask(2) {
                reset()
            }

        }

        private fun reset() {
            if(System.currentTimeMillis() - 50 >= lastClick) {
                color = Color(0, 0, 0, 100)
            }
        }

    }
}