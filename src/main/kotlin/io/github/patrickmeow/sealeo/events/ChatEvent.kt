package io.github.patrickmeow.sealeo.events

import net.minecraftforge.fml.common.eventhandler.Cancelable
import net.minecraftforge.fml.common.eventhandler.Event

@Cancelable
data class ChatReceivedEvent(val message: String) : Event()
