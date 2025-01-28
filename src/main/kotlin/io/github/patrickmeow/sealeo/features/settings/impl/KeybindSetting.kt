package io.github.patrickmeow.sealeo.features.settings.impl


import io.github.patrickmeow.sealeo.features.settings.Setting
import org.lwjgl.input.Keyboard

class KeybindSetting(
    name: String,
    description: String,
    key: Int
) : Setting<Int>(name, description) {

    private var onPressAction: (() -> Unit)? = null


    override var value = key

    fun onPress(action: () -> Unit): KeybindSetting {
        onPressAction = action
        return this
    }


    fun checkAndExecute() {
        if (Keyboard.isKeyDown(value)) {
            onPressAction?.invoke()
        }
    }
}
