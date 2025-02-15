package io.github.patrickmeow.sealeo.uiOld.elements.impl

import io.github.patrickmeow.sealeo.features.Module
import io.github.patrickmeow.sealeo.features.settings.Setting
import io.github.patrickmeow.sealeo.features.settings.impl.NumberSetting
import io.github.patrickmeow.sealeo.uiOld.ClickGui
import io.github.patrickmeow.sealeo.uiOld.elements.Element
import io.github.patrickmeow.sealeo.utils.RenderUtils
import java.awt.Color
import kotlin.math.roundToInt

class SliderElement(var x: Float, var y: Float, var setting: NumberSetting) : Element() {

    private var draggingNow = false

    fun updatePosition(newX: Float, newY: Float) {
        x = newX
        y = newY
    }

    override fun draw(mouseX: Int, mouseY: Int) {
        super.draw(mouseX, mouseY)
        var sliderColor = Color(26, 27, 35)
        var progressColor = Color(48, 158, 233)
        if (draggingNow) {
            //println("dragging")
            setting.value = (mouseX - x) / 12f
        }
        RenderUtils.drawText(setting.name, x - 100f, y, -1, 1.2f)
        RenderUtils.roundedRectangle(x + 15, y + 5f, 120f, 8f, sliderColor, 1.5f)
        var sliderProgress = setting.value.toFloat() / setting.max.toFloat()
        RenderUtils.roundedRectangle(x + (sliderProgress * 120) + 10, y + 5f, 10, 8f, progressColor, 1.5f)
        //RenderUtils.roundedRectangle(x + 15, y + 5f, sliderProgress * 120f, 5f, progressColor, 3f)
        RenderUtils.drawText(setting.value.toString(), x + 95f, y - 15f, -1, 1f)
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int) {
        super.mouseClicked(mouseX, mouseY)
        if (isMouseOver(mouseX, mouseY, x, y, 120f, 5f)) {
            var tempValue = (mouseX - x) / 12f
            if (setting.value.toFloat() >= roundToIncrement(tempValue)) {
                draggingNow = true
            } else {
                setting.value = (mouseX - x) / 12f
            }
        }

    }


    override fun onRelease(mouseX: Int, mouseY: Int) {
        super.onRelease(mouseX, mouseY)
        draggingNow = false
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SliderElement) return false
        return setting == other.setting
    }

    override fun hashCode(): Int {
        return setting.hashCode()
    }

    private fun roundToIncrement(value: Float): Float {
        val steps = ((value - setting.min.toFloat()) / setting.increment.toFloat()).roundToInt()
        return (setting.min.toFloat() + steps * setting.increment.toFloat())
    }
}
