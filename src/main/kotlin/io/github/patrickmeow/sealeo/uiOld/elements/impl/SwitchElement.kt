package io.github.patrickmeow.sealeo.uiOld.elements.impl

import io.github.patrickmeow.sealeo.features.settings.impl.BooleanSetting
import io.github.patrickmeow.sealeo.uiOld.elements.Element
import io.github.patrickmeow.sealeo.utils.HSBColor
import io.github.patrickmeow.sealeo.utils.RenderUtils
import java.awt.Color
import kotlin.math.max
import kotlin.math.min

class SwitchElement(var x: Float, var y: Float, var setting: BooleanSetting) : Element() {

    private var toggleButton = ToggleButton(x - 50f, y, setting, false)
    private var rectColor: Color = Color(30, 31, 40)
    override fun updatePosition(fl: Float, fl1: Float) {
        toggleButton.updatePosition(fl + 85f, fl1)
        //println("$fl $fl1")
        this.x = fl
        this.y = fl1
    }


    override fun draw(mouseX: Int, mouseY: Int) {
        super.draw(mouseX, mouseY)

        RenderUtils.roundedRectangle(x - 110f, y - 8f, 280f, 30f, rectColor, 6f)
        RenderUtils.drawText(setting.name, x - 100f, y, -1, 1.2f)
        toggleButton.draw()
        RenderUtils.drawHSBBox(x + 50f, y + 10f, 50f, 50f, HSBColor(1f, 1f, 1f))
    }


    override fun mouseClicked(mouseX: Int, mouseY: Int) {
        super.mouseClicked(mouseX, mouseY)
        toggleButton.onClick(mouseX, mouseY)

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SwitchElement) return false
        return setting == other.setting
    }

    override fun hashCode(): Int {
        return setting.hashCode()
    }


    class ToggleButton(var x: Float, var y: Float, var setting: BooleanSetting, var isAnimating: Boolean) {

        private var animationStartTime: Long = 0
        private val MAX_INCREMENT = 25
        private val ANIMATION_DURATION = 250

        private var increment = if (setting.value) {
            MAX_INCREMENT
        } else {
            0
        }

        fun updatePosition(newX: Float, newY: Float) {
            x = newX
            y = newY
        }

        private fun updateAnimation() {
            if (!isAnimating) return
            val elapsedTime = System.currentTimeMillis() - animationStartTime

            if (setting.value) {
                increment = min(
                    ((elapsedTime * MAX_INCREMENT) / ANIMATION_DURATION).toInt(), MAX_INCREMENT
                )
            } else {
                increment = max(
                    MAX_INCREMENT - ((elapsedTime * MAX_INCREMENT) / ANIMATION_DURATION).toInt(), 0
                )
            }

            if (elapsedTime >= ANIMATION_DURATION) {
                isAnimating = false
                increment = if (setting.value) MAX_INCREMENT else 0
            }
        }

        fun draw() {
            updateAnimation()

            val color: Color = if (setting.value) {
                Color(18, 205, 107)
            } else {
                Color(250, 75, 41)
            }

            val buttonColor = Color(26, 27, 35)

            RenderUtils.roundedRectangle(x, y, 60f, 18f, buttonColor, 5f, 0.1f)
            RenderUtils.roundedRectangle(x + increment, y, 35f, 18f, color, 5f, 0.2f)
        }

        fun onClick(mouseX: Int, mouseY: Int) {
            if (isMouseOver(mouseX, mouseY)) {
                println("Clicked " + setting.name)
                setting.value = !setting.value
                isAnimating = true
                animationStartTime = System.currentTimeMillis()
            }
        }

        fun isMouseOver(mouseX: Int, mouseY: Int): Boolean {
            return (mouseX >= x && mouseX <= x + 60f) && (mouseY >= y && mouseY <= y + 18f)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is ToggleButton) return false
            return setting.name == other.setting.name
        }

        override fun hashCode(): Int {
            var result = x.hashCode()
            result = 31 * result + y.hashCode()
            result = 31 * result + setting.hashCode()
            result = 31 * result + isAnimating.hashCode()
            result = 31 * result + animationStartTime.hashCode()
            result = 31 * result + MAX_INCREMENT
            result = 31 * result + ANIMATION_DURATION
            result = 31 * result + increment
            return result
        }
    }





}