package io.github.patrickmeow.sealeo.uiOld.elements.impl

import io.github.patrickmeow.sealeo.features.settings.impl.ListSetting
import io.github.patrickmeow.sealeo.uiOld.animations.Animation
import io.github.patrickmeow.sealeo.uiOld.elements.Element
import io.github.patrickmeow.sealeo.utils.RenderUtils
import java.awt.Color

class ListElement(var x: Float, var y: Float, var setting: ListSetting) : Element() {

    var listOpened = false
    var isAnimating = false
    var isClosing = false
    private var rectColor: Color = Color(30, 31, 40)
    private var hoverColor: Long = -1
    private val buttonColor = Color(26, 27, 35)
    private var animation = Animation(200)
    private var closingAnimation = Animation(200)

    init {
        closingAnimation.setPercent(1f)
    }

    override fun updatePosition(fl: Float, fl1: Float) {
        this.x = fl
        this.y = fl1
    }

    override fun draw(mouseX: Int, mouseY: Int) {
        isAnimating = animation.animating
        isClosing = closingAnimation.animating
        super.draw(mouseX, mouseY)

        var offsetY = 0f
        if (listOpened) {
            RenderUtils.roundedRectangle(x - 110f, y - 8f, 280f, 30 + setting.list.size * 16 * animation.getPercent(), rectColor, 6f)
            RenderUtils.roundedRectangle(x + 80f, y - 2, 65f, 20f, buttonColor, 6f)
            RenderUtils.drawText(setting.name, x - 100f, y, -1, 1.2f)
            RenderUtils.roundedRectangle(x + 80f, y - 2f + 23f, 65, 45f * animation.getPercent(), buttonColor, 6f)

            // Create a new list with the selected option at the start


            for (option in setting.list) {
                hoverColor = if (isMouseOver(mouseX, mouseY, x + 90f, y + offsetY + 20f, 50f, 10f)) {
                    0xFF808080
                } else {
                    -1
                }
                RenderUtils.drawText(option, x + 90f, y + offsetY + 20f, hoverColor, 1f)
                offsetY += 15f * animation.getPercent()
            }
        } else {
            RenderUtils.roundedRectangle(x - 110f, y - 8f, 280f, 30f + setting.list.size * 16 - setting.list.size * 16 * closingAnimation.getPercent(), rectColor, 6f)
            RenderUtils.drawText(setting.name, x - 100f, y, -1, 1.2f)
            RenderUtils.roundedRectangle(x + 80f, y - 2, 65f, 20f, buttonColor, 6f)

        }
        RenderUtils.drawText(setting.selected, x + 90f, y, -1, 1f)
    }


    override fun mouseClicked(mouseX: Int, mouseY: Int) {
        var offsetY = 0f
        super.mouseClicked(mouseX, mouseY)
        if(isMouseOver(mouseX, mouseY, x + 80f, y - 2f, 65f, 20f))  {
            listOpened = true
            animation.start()
            isAnimating = true
        }

        if(listOpened) {

            for(option in setting.list) {
                if(isMouseOver(mouseX, mouseY, x + 90f, y + offsetY + 20f, 50f, 10f)) {
                    setting.selected = option
                    //setting.value = setting.getIndex(option)
                    println("Selected $option")
                    println(setting.selected)
                    listOpened = false
                    closingAnimation.start()

                }
                offsetY += 15f * animation.getPercent()
            }
        }
    }



    fun openAnimation() {
        isAnimating = true
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ListElement) return false
        return setting == other.setting
    }

    override fun hashCode(): Int {
        return setting.hashCode()
    }


}