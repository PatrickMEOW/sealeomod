package io.github.patrickmeow.sealeo.events


import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Slot
import net.minecraftforge.fml.common.eventhandler.Cancelable
import net.minecraftforge.fml.common.eventhandler.Event

@Cancelable
class SlotClickEvent(
    val container: GuiContainer,
    var slot: Slot,
    var slotId: Int
) : Event()


