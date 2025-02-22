package io.github.patrickmeow.sealeo.uiOld.elements.impl

import io.github.patrickmeow.sealeo.features.settings.impl.NumberSetting
import io.github.patrickmeow.sealeo.uiOld.elements.Element
import io.github.patrickmeow.sealeo.utils.RenderUtils
import java.awt.Color
import kotlin.math.roundToInt

class SliderElement(var x: Float, var y: Float, var setting: NumberSetting) : Element() {

    private var draggingNow = false
    private var rectColor: Color = Color(30, 31, 40)
    override fun updatePosition(fl: Float, fl1: Float) {
        x = fl
        y = fl1
    }

    override fun draw(mouseX: Int, mouseY: Int) {
        super.draw(mouseX, mouseY)
        val sliderColor = Color(26, 27, 35)
        val progressColor = Color(48, 158, 233)
        if (draggingNow) {
            setting.value = (mouseX - x - 20) / ( (10 / setting.max.toInt()) * 12f)
        }
        RenderUtils.roundedRectangle(x - 110f, y - 8f, 280f, 30f, rectColor, 6f)
        RenderUtils.drawText(setting.name, x - 100f, y, -1, 1.2f)
        RenderUtils.roundedRectangle(x + 22.5, y + 5f, 117.5f, 8f, sliderColor, 1.5f)

        //val sliderProgress = setting.value.toFloat() / setting.max.toFloat()
        val sliderProgress = (setting.value.toFloat() - setting.min.toFloat()) / ((setting.max).toFloat() - setting.min.toFloat())
        RenderUtils.roundedRectangle(x + (sliderProgress * 120) + 20, y + 4f, 10, 10f, progressColor, 2f)
        RenderUtils.drawText("%.1f".format(setting.value), x + 125f, y - 6.5f, -1, 1f)
    }



    override fun mouseClicked(mouseX: Int, mouseY: Int) {

        super.mouseClicked(mouseX, mouseY)
        val sliderProgress = (setting.value.toFloat() - setting.min.toFloat()) / ((setting.max).toFloat() - setting.min.toFloat())
        if (isMouseOver(mouseX, mouseY, x + (sliderProgress * 120) + 10, y + 4f, 20f, 10f)) {

            val tempValue = (mouseX - x - 15) / 12f
            /*
            if (setting.value.toFloat() >= roundToIncrement(tempValue)) {
                draggingNow = true
            } else {
                setting.value = (mouseX - x - 15) / 12f
            }
              */
            draggingNow = true
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
