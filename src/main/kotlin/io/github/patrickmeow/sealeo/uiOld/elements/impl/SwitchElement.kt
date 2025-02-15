package io.github.patrickmeow.sealeo.uiOld.elements.impl

import io.github.patrickmeow.sealeo.features.settings.impl.BooleanSetting
import io.github.patrickmeow.sealeo.uiOld.elements.Element

class SwitchElement(var x: Float, var y: Float, var setting: BooleanSetting) : Element() {

    fun updatePosition(newX: Float, newY: Float) {
        x = newX
        y = newY
    }


    override fun draw(mouseX: Int, mouseY: Int) {
        super.draw(mouseX, mouseY)
    }


}