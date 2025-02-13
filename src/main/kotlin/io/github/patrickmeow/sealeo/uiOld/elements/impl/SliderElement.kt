package io.github.patrickmeow.sealeo.uiOld.elements.impl

import io.github.patrickmeow.sealeo.features.Module
import io.github.patrickmeow.sealeo.features.settings.Setting
import io.github.patrickmeow.sealeo.features.settings.impl.NumberSetting
import io.github.patrickmeow.sealeo.uiOld.ClickGui
import io.github.patrickmeow.sealeo.uiOld.elements.Element
import io.github.patrickmeow.sealeo.utils.RenderUtils
import java.awt.Color

class SliderElement(var x: Float, var y: Float, var setting: NumberSetting) : Element() {

    var dragging = false


    fun updatePosition(newX: Float, newY: Float) {
        x = newX
        y = newY
    }


    override fun draw(mouseX: Int, mouseY: Int) {
        var sliderColor = Color(26,27,35)
        var progressColor = Color(48,158,233)

        RenderUtils.drawText(setting.name, x, y - 15f, -1, 1f)
        // Slider
        RenderUtils.roundedRectangle(x, y, 120f, 5f, Color(26,27,35), 3f)
        var sliderProgress = setting.value.toFloat() / setting.max.toFloat()
        RenderUtils.roundedRectangle(x, y, sliderProgress * 120f, 5f, Color(48,158,233), 3f)

        RenderUtils.drawText(setting.value.toString(), x + 95f, y -15f, -1, 1f)

    }


    override fun mouseClicked(mouseX: Int, mouseY: Int) {
        super.mouseClicked(mouseX, mouseY)
        if(isMouseOver(mouseX, mouseY, x, y, 120f, 5f)) {
            setting.value = (mouseX - x) / 12f
        }


    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SliderElement) return false
        return setting == other.setting
    }







}