package io.github.patrickmeow.sealeo.features.impl.skyblock

import io.github.patrickmeow.sealeo.Sealeo.mc
import io.github.patrickmeow.sealeo.events.SlotClickEvent
import io.github.patrickmeow.sealeo.features.Category
import io.github.patrickmeow.sealeo.features.Module
import io.github.patrickmeow.sealeo.mixin.AccessorGuiContainer
import io.github.patrickmeow.sealeo.utils.PacketUtils
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Slot
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse
import io.github.patrickmeow.sealeo.utils.RenderUtils.drawLinkArrow
import io.github.patrickmeow.sealeo.utils.TickDelays
import net.minecraft.network.play.server.S28PacketEffect

import net.minecraftforge.fml.common.eventhandler.Event

object SlotBinding : Module(
    "Slot Binding",
    "Binds slot",
    Category.SKYBLOCK
) {

    private var initialSlot: Slot? = null
    private var initialX = -1
    private var initialY = -1
    private var firstSlot: Slot? = null
    private var secondSlot: Slot? = null
    private var firstDraw: Slot? = null
    private var secondDraw: Slot? = null

    private val slotsMap = HashMap<Slot, Slot>()

    @SubscribeEvent
    fun onGuiClick(event: SlotClickEvent) {

        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            handleItemSwap(event)
        }

    }

    @SubscribeEvent(priority = EventPriority.LOW)
    fun onRender(event: GuiScreenEvent.DrawScreenEvent.Post) {
        if (mc.currentScreen !is GuiContainer) return

        val container = mc.currentScreen as AccessorGuiContainer
        val scaledResolution = ScaledResolution(mc)
        val scaledWidth = scaledResolution.scaledWidth
        val scaledHeight = scaledResolution.scaledHeight
        val mouseX = Mouse.getX() * scaledWidth / mc.displayWidth
        val mouseY = scaledHeight - Mouse.getY() * scaledHeight / mc.displayHeight - 1
        val slot = container.doGetSlotAtPosition(mouseX, mouseY) ?: return
        if (Keyboard.isKeyDown(Keyboard.KEY_L)) {

            handleClick(slot)
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            if(slotsMap.containsKey(slot)) {
                firstDraw = slot
                secondDraw = slotsMap.get(slot)
            } else if(slotsMap.containsValue(slot)) {
                firstDraw = slot
                secondDraw = findKeyByValue(slot)
            } else {
                resetDraws()
            }
        } else {
            resetDraws()
        }

        drawArrow(container, event)
    }


    private fun handleClick(slotClicked: Slot) {
        if (slotsMap.containsValue(slotClicked) || slotsMap.containsKey(slotClicked)) return

        when {
            firstSlot == null -> firstSlot = slotClicked
            slotClicked != firstSlot -> {
                secondSlot = slotClicked
                slotsMap[firstSlot!!] = secondSlot!!
                println("Bound slot ${firstSlot?.slotIndex} to ${secondSlot?.slotIndex}")
                resetSlots()
            }
        }
    }


    private fun handleItemSwap(event: SlotClickEvent) {
        if(slotsMap.containsKey(event.slot)) {
            event.isCanceled = true
            if(event.slotId >= 36) {
                slotsMap.get(event.slot)?.let { swapSlots(it, event.slot) }
            } else {
                slotsMap.get(event.slot)?.let { swapSlots(event.slot, it) }
            }
        }
        println(event.slotId)
        if(slotsMap.containsValue(event.slot)) {
            event.isCanceled = true
            if(event.slotId >= 36) {
                findKeyByValue(event.slot)?.let { swapSlots(it, event.slot) }
            } else {
                findKeyByValue(event.slot)?.let { swapSlots(event.slot, it) }
            }
        }
    }

    private fun swapSlots(inventorySlot: Slot, hotbarSlot: Slot) {
        val windowId = mc.thePlayer.openContainer.windowId
        val mode = 2
        mc.playerController.windowClick(windowId, inventorySlot.slotIndex, hotbarSlot.slotIndex, mode, mc.thePlayer)
        println("Swapping")
    }


    private fun drawArrow(container: AccessorGuiContainer, event: GuiScreenEvent.DrawScreenEvent.Post) {
        val firstSlotCoords = getSlotCoordinates(container, firstSlot)
        val secondSlotCoords = getSlotCoordinates(container, secondSlot)
        val firstDrawCoords = getSlotCoordinates(container, firstDraw)
        val secondDrawCoords = getSlotCoordinates(container, secondDraw)

        if (firstSlotCoords != null) {
            val secondCoords = secondSlotCoords ?: Pair(event.mouseX, event.mouseY)
            drawLinkArrow(firstSlotCoords.first, firstSlotCoords.second, secondCoords.first, secondCoords.second)
        }
        if (firstDrawCoords != null && secondDrawCoords != null) {
            drawLinkArrow(firstDrawCoords.first, firstDrawCoords.second, secondDrawCoords.first, secondDrawCoords.second)
        }
    }

    private fun getSlotCoordinates(container: AccessorGuiContainer, slot: Slot?): Pair<Int, Int>? {
        if (slot == null) return null
        val x = container.guiLeft + slot.xDisplayPosition + 8
        val y = container.guiTop + slot.yDisplayPosition + 8
        return Pair(x, y)
    }


    private fun resetSlots() {

        firstSlot = null
        secondSlot = null
    }

    private fun resetDraws() {
        firstDraw = null
        secondDraw = null
    }

    private fun findKeyByValue(value: Slot): Slot? { return slotsMap.entries.find { it.value == value }?.key }

}
