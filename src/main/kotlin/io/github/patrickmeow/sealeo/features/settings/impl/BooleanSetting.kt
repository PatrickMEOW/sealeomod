package io.github.patrickmeow.sealeo.features.settings.impl

import io.github.patrickmeow.sealeo.features.settings.Setting

class BooleanSetting (
    name: String,
    description: String,
) : Setting<Boolean>(name, description) {


    override var value: Boolean = false
    var enabled: Boolean by this::value
}