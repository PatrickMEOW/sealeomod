package io.github.patrickmeow.sealeo.features.settings.impl

import io.github.patrickmeow.sealeo.features.settings.Setting
import java.awt.Color

class ColorSetting(
    name: String,
    description: String,
    val color: Color, override var value: Color
) : Setting<Color>(name, description) {

    var red: Int
        get() = value.red
        set(value) {
            this.value = Color(value.coerceIn(0, 255), this.value.green, this.value.blue, this.value.alpha)
        }

    var green: Int
        get() = value.green
        set(value) {
            this.value = Color(this.value.red, value.coerceIn(0, 255), this.value.blue, this.value.alpha)
        }

    var blue: Int
        get() = value.blue
        set(value) {
            this.value = Color(this.value.red, this.value.green, value.coerceIn(0, 255), this.value.alpha)
        }

    var alpha: Int
        get() = value.alpha
        set(value) {
            this.value = Color(this.value.red, this.value.green, this.value.blue, value.coerceIn(0, 255))
        }
}
