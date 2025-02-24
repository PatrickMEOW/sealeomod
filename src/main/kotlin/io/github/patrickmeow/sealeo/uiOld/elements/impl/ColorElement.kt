package io.github.patrickmeow.sealeo.uiOld.elements.impl

import io.github.patrickmeow.sealeo.Sealeo
import io.github.patrickmeow.sealeo.SealeoMod
import io.github.patrickmeow.sealeo.features.settings.impl.BooleanSetting
import io.github.patrickmeow.sealeo.features.settings.impl.ColorSetting
import io.github.patrickmeow.sealeo.uiOld.elements.Element
import io.github.patrickmeow.sealeo.utils.HSBColor
import io.github.patrickmeow.sealeo.utils.RenderUtils
import io.github.patrickmeow.sealeo.utils.RenderUtils.withAlpha
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.client.renderer.texture.TextureUtil
import java.awt.Color

class ColorElement(var x: Float, var y: Float, var setting: ColorSetting) : Element() {


    val bufferedImage = TextureUtil.readBufferedImage(Sealeo::class.java.getResourceAsStream("/assets/sealeo/textures/HueGradient.png"))
    private val hueGradient = DynamicTexture(bufferedImage)
    private var rectColor: Color = Color(30, 31, 40)
    var isOpened = false
    private var isDraging = false
    private var dragging: Int? = null
    private var draggedX = 0f
    private var draggedY = 0f
    var lastDraggedX = 0f
    var lastDraggedY = 0f
    val bgColor = Color(26, 27, 35)
    inline val color: HSBColor
        get() = setting.value
    override fun updatePosition(fl: Float, fl1: Float) {

        this.x = fl
        this.y = fl1
    }

    override fun draw(mouseX: Int, mouseY: Int) {
        super.draw(mouseX, mouseY)
        RenderUtils.roundedRectangle(x - 110f, y - 8f, 280f, 30f, rectColor, 6f)
        RenderUtils.drawText(setting.name, x - 100f, y - 6f, -1, 1.2f)
        RenderUtils.drawText(setting.description, x - 100f, y + 6f, 0xFF808080, 0.93f)
        RenderUtils.roundedRectangle(x + 100f, y - 3f, 30f, 20f, HSBColor(setting.hue, setting.saturation, setting.brightness).javaColor, 5f, 0.2f)
        //if(isOpened) {
            //RenderUtils.roundedRectangle(x + 108f, y - 2f, 52f, 52f, bgColor, 5f)

        //}



    }


    fun drawColorPicker(mouseX: Int, mouseY: Int) {
        println(draggedY)
        RenderUtils.roundedRectangle(x - 16f + draggedX, y - 30f + draggedY, 98f, 105f, bgColor, 5f, 0.1f)

        val boxColor = HSBColor(setting.hue, 1f, 1f)
        RenderUtils.drawHSBBox(x - 9f + draggedX, y - 20f + draggedY, 70f, 70f, boxColor)

        // hue
        RenderUtils.drawDynamicTexture(hueGradient, x - 9f + draggedX, y + 55f + draggedY, 70f, 15f)
        RenderUtils.rectangleOutline(x - 11f + draggedX, y + 54f + draggedY, 72f, 17f, Color(38, 38, 38), 1f, 2.5f)
        RenderUtils.drawText("X", x - 16f + draggedX, y - 29f + draggedY, -1, 0.8f)

        // alpha
        RenderUtils.gradientRect(x + 63f + draggedX, y - 20f + draggedY, 15f, 70f, HSBColor.TRANSPARENT, color.withAlpha(1f), 3f, RenderUtils.GradientDirection.Up, HSBColor.DARK_GRAY.javaColor, 1.5f)
        if(dragging == 1) {
            if(mouseY >= y - 20f + draggedY) {
                setting.alpha =  1 - ((mouseY - y + 20f - draggedY) / 70f)
                println(setting.alpha)
            }
        }

        if(dragging == 0) {
            if(mouseX >= x - 11f + draggedX) {
                setting.hue = (mouseX - x + 11f - draggedX) / 70f
            }
        }

        if(dragging == 2) {
            if(mouseX >= x - 9f + draggedX && mouseY >= y - 20f + draggedY) {
                setting.saturation =  (mouseX - x + 9f - draggedX) / 70f
                setting.brightness =  1 - ((mouseY - y + 20f - draggedY) / 70f)
                println(setting.hue)
                println(setting.saturation)
                println(setting.brightness)
            }
        }

        if(dragging == 3) {
            smoothDrag(mouseX, mouseY)
        }

        RenderUtils.roundedRectangle(x + 63f + draggedX, y - 20f + (1 - setting.alpha) * 70f + draggedY, 15f, 1f, Color.GRAY)
        RenderUtils.roundedRectangle(x - 11f + (setting.hue * 70f) + draggedX, y + 55f + draggedY, 1f, 15f, Color.GRAY)
        RenderUtils.circle(x - 9f + (setting.saturation * 70f) + draggedX, y - 20f + ((1 - setting.brightness) * 70f) + draggedY, 2f, Color(0, 0, 0, 0), Color.BLACK, 1f)
    }


    fun smoothDrag(mouseX: Int, mouseY: Int) {
        draggedX = (mouseX - lastDraggedX)
        draggedY = (mouseY - lastDraggedY)
        lastDraggedX = draggedX
        lastDraggedY = draggedY
    }


    override fun mouseClicked(mouseX: Int, mouseY: Int) {
        super.mouseClicked(mouseX, mouseY)
        if(isMouseOver(mouseX, mouseY, x + 100f, y - 3f, 30f, 20f)) {
            isOpened = true
        }
        //x - 16f, y - 30f, 98f, 105f
        dragging = when {
            isMouseOver(mouseX, mouseY, x - 9f, y + 55f, 70f, 15f) -> 0 // hue
            isMouseOver(mouseX, mouseY, x + 63f, y - 20f, 15f, 70f) -> 1 // alpha
            isMouseOver(mouseX, mouseY, x - 9f, y - 20f, 70f, 70f) -> 2 // sb
            isMouseOver(mouseX, mouseY, x - 16f + draggedX, y - 30f + draggedY, 89f, 20f) -> 3
            else -> null
        }
        //x - 16f, y - 29f
        if(isMouseOver(mouseX, mouseY, x - 16f, y - 29f, 6f, 6f)) {
            isOpened = false
            println("closed")
        }
    }


    override fun onRelease(mouseX: Int, mouseY: Int) {
        super.onRelease(mouseX, mouseY)
        dragging = null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ColorElement) return false
        return setting == other.setting
    }

    override fun hashCode(): Int {
        return setting.hashCode()
    }

}