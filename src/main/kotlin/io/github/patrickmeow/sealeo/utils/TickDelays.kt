package io.github.patrickmeow.sealeo.utils

import kotlinx.coroutines.delay
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

class TickDelays {



    companion object {
        private var delayedTasks = ArrayList<TickDelay>()

        fun addDelayedTask(ticks : Int, func : () -> Unit) {
            delayedTasks.add(TickDelay(ticks, func))
        }
    }


    private fun updateDelays() {
        delayedTasks.removeIf { task ->
            if (task.delay <= 0) {
                task.func.invoke()
                true
            } else {
                task.updateDelay()
                false
            }
        }
    }


    class TickDelay(var delay : Int, var func : () -> Unit) {
        fun updateDelay() {
            delay--
        }

    }

    @SubscribeEvent
    fun onTick(event : ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START) return
        updateDelays()
    }

}