package io.github.patrickmeow.sealeo.events

import net.minecraftforge.fml.common.eventhandler.Event

abstract class InputEvent(val keycode: Int) : Event() {
    class Keyboard(keycode: Int) : InputEvent(keycode)
    class Mouse(button: Int) : InputEvent(button)
}