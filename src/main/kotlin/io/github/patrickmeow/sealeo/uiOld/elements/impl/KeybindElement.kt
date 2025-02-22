package io.github.patrickmeow.sealeo.uiOld.elements.impl

import io.github.patrickmeow.sealeo.features.settings.impl.KeybindSetting
import io.github.patrickmeow.sealeo.uiOld.elements.Element
import io.github.patrickmeow.sealeo.utils.RenderUtils
import org.lwjgl.input.Keyboard
import java.awt.Color

class KeybindElement(var x: Float, var y: Float, var setting: KeybindSetting) : Element() {

    private var listening = false
    private var rectColor: Color = Color(30, 31, 40)
    private var buttonColor: Color = Color(26, 27, 35)
    private var keyName = if(setting.value == -1) {
            "Key: NONE"
    } else {
        "Key: " + Keyboard.getKeyName(setting.value).toString()
    }

    override fun updatePosition(fl: Float, fl1: Float) {
        x = fl
        y = fl1
    }


    override fun draw(mouseX: Int, mouseY: Int) {
        super.draw(mouseX, mouseY)
        var drawPos = if(listening) {
            x + 88f
        } else {
            x + 94f
        }
        if(keyName.length > 7 && !listening) {
            keyName = Keyboard.getKeyName(setting.value).toString()
            drawPos = x + 87f
        }
        RenderUtils.roundedRectangle(x - 110f, y - 8f, 280f, 30f, rectColor, 6f)
        RenderUtils.drawText(setting.name, x - 100f, y, -1, 1.2f)
        RenderUtils.roundedRectangle(x + 80f, y - 4f, 60f, 22f, buttonColor, 6f, .1f)
        RenderUtils.drawText(keyName, drawPos, y + 1f, -1, 0.8f)


    }

    override fun mouseClicked(mouseX: Int, mouseY: Int) {
        super.mouseClicked(mouseX, mouseY)
        if(isMouseOver(mouseX, mouseY, x + 80f, y - 4f, 50f, 22f)) {
            keyName = "Enter key.."
            listening = true
        }
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        super.keyTyped(typedChar, keyCode)
        if(listening) {
            setting.value = keyCode
            keyName = "Key: " + Keyboard.getKeyName(setting.value).toString()
            listening = false
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is KeybindElement) return false
        return setting == other.setting
    }

    override fun hashCode(): Int {
        return setting.hashCode()
    }
}