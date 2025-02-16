package io.github.patrickmeow.sealeo.features.settings.impl

import io.github.patrickmeow.sealeo.features.settings.Setting
import io.github.patrickmeow.sealeo.utils.HSBColor
import java.awt.Color

class ColorSetting(
    name: String,
    description: String,
    override var value: HSBColor
) : Setting<HSBColor>(name, description) {

    var hue: Float
        get() = value.hue
        set(value) {
            this.value.hue = value.coerceIn(0f, 1f)
        }

    var saturation: Float
        get() = value.saturation
        set(value) {
            this.value.saturation = value.coerceIn(0f, 1f)
        }

    var brightness: Float
        get() = value.brightness
        set(value) {
            this.value.brightness = value.coerceIn(0f, 1f)
        }

    var alpha: Float
        get() = value.alpha
        set(value) {
            this.value.alpha = value.coerceIn(0f, 1f)
        }
}
