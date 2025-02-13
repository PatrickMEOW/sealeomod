package io.github.patrickmeow.sealeo.features.settings.impl

import io.github.patrickmeow.sealeo.features.settings.Setting
import kotlin.math.roundToInt

class NumberSetting (
    name: String,
    description: String,
    default: Number,
    val min: Number,
    val max: Number,
    val increment: Number
) : Setting<Number>(name, description) {
    override var value: Number = default
        set(newValue) {
            field = roundToIncrement(newValue.toFloat()).coerceIn(min.toFloat(), max.toFloat())
        }


    private fun roundToIncrement(value: Float): Float {
        val steps = ((value - min.toFloat()) / increment.toFloat()).roundToInt()
        return (min.toFloat() + steps * increment.toFloat())
    }
}