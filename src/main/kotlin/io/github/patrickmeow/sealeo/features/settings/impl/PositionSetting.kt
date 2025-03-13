package io.github.patrickmeow.sealeo.features.settings.impl

import io.github.patrickmeow.sealeo.features.settings.Setting

class PositionSetting (
    name: String,
    description: String,
    default: Pair<Float, Float>
) : Setting<Pair<Float, Float>>(name, description) {

    override var value: Pair<Float, Float> = default

}