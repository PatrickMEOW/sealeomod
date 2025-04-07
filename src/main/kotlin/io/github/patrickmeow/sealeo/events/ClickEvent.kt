package io.github.patrickmeow.sealeo.events

import net.minecraftforge.fml.common.eventhandler.Cancelable
import net.minecraftforge.fml.common.eventhandler.Event

abstract class ClickEvent : Event() {
    @Cancelable
    class Left : ClickEvent()

    @Cancelable
    class Right : ClickEvent()
}