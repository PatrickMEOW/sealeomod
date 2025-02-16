package io.github.patrickmeow.sealeo.uiOld.elements.impl

import io.github.patrickmeow.sealeo.Sealeo.mc
import io.github.patrickmeow.sealeo.features.Module
import io.github.patrickmeow.sealeo.features.settings.impl.BooleanSetting
import io.github.patrickmeow.sealeo.features.settings.impl.KeybindSetting
import io.github.patrickmeow.sealeo.features.settings.impl.NumberSetting
import io.github.patrickmeow.sealeo.uiOld.ClickGui
import io.github.patrickmeow.sealeo.uiOld.ClickGui.height
import io.github.patrickmeow.sealeo.uiOld.ClickGui.x
import io.github.patrickmeow.sealeo.uiOld.ClickGui.y
import io.github.patrickmeow.sealeo.uiOld.elements.Element
import io.github.patrickmeow.sealeo.utils.RenderUtils
import org.lwjgl.opengl.GL11
import java.awt.Color

class SettingsPanel(var module: Module, var modulesElement: ModulesElement) : Element() {

    private var animationStartTime: Long = 0
    private val ANIMATION_DURATION = 250
    var animating: Boolean = false
    var closing: Boolean = false
    private val settingsElements = ArrayList<Element>()
    private val initialPanelWidth = 0f
    private val finalPanelWidth = 390f
    var settingsOpened = true
    private val targetX = x + 525f - finalPanelWidth
    private var currentX: Float = targetX + finalPanelWidth - initialPanelWidth
    private var currentPanelWidth: Float = initialPanelWidth
    private var rectColor: Color = Color(26, 27, 35)
    var toggleButton = ModulesElement.ToggleButton(currentX, y + 20f, module, false)

    override fun draw(mouseX: Int, mouseY: Int) {
        super.draw(mouseX, mouseY)
        if (animating) updateAnimation()

        val color = Color(33, 35, 44)
        val moduleText = module.name
        val textWidth = RenderUtils.measureTextWidth(moduleText, 1.5f)
        val moduleTextPosX = (currentPanelWidth - textWidth) / 2

        RenderUtils.roundedRectangle(currentX, y, currentPanelWidth, 320f, color, color, color, 0f, 0f, 8f, 0f, 8f, 0.1f)

        GL11.glEnable(GL11.GL_SCISSOR_TEST)
        GL11.glScissor(
            (currentX.toInt() * mc.displayWidth / ClickGui.sc!!.scaledWidth),
            (mc.displayHeight - (y.toInt() + 320) * mc.displayHeight / ClickGui.sc!!.scaledHeight),
            (currentPanelWidth.toInt() * mc.displayWidth / ClickGui.sc!!.scaledWidth),
            (320 * mc.displayHeight / ClickGui.sc!!.scaledHeight)
        )

        RenderUtils.roundedRectangle(currentX + 136.5f - 100f, y + 10f, 310f, 40f, rectColor, 6f)
        RenderUtils.drawText(moduleText, currentX + moduleTextPosX - 80f, y + 20f, -1, 1.5f)
        RenderUtils.roundedRectangle(currentX + 5f, y + 10f, 25, 20f, rectColor, 4f)
        RenderUtils.drawText("X", currentX + 10.5f, y + 8f, 0xFFeb4034, 1.8f)
        toggleButton.updatePosition(currentX + moduleTextPosX + 110f, y + 20f)
        toggleButton.draw()

        var offsetY = 0f

        for (setting in module.settings) {
            if (setting is KeybindSetting) continue
            val element = when (setting) {
                is NumberSetting -> SliderElement(currentX + moduleTextPosX + 20f, y + 70f + offsetY, setting)
                is BooleanSetting -> SwitchElement(currentX + moduleTextPosX + 20f, y + 70f + offsetY, setting)
                else -> null
            }
            element?.let {
                if (!settingsElements.contains(it)) {
                    settingsElements.add(it)
                    println("adding $offsetY")
                } else {
                    // Update the position of the existing element
                    val existingElement = settingsElements.find { e -> e == it }
                    existingElement?.updatePosition(currentX + moduleTextPosX + 25f, y + 70f + offsetY)
                }
                offsetY += 35f
            }
        }

        for (element in settingsElements) {
            element.draw(mouseX, mouseY)
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST)
    }

    fun updateAnimation() {
        val elapsedTime = System.currentTimeMillis() - animationStartTime
        val progress = (elapsedTime.toFloat() / ANIMATION_DURATION).coerceIn(0f, 1f)

        if (closing) {
            currentPanelWidth = finalPanelWidth - progress * (finalPanelWidth - initialPanelWidth)
            currentX = targetX + finalPanelWidth - currentPanelWidth

            if (progress >= 1f) {
                animating = false
                settingsOpened = false
                currentPanelWidth = initialPanelWidth
                currentX = targetX + finalPanelWidth - initialPanelWidth
            }
        } else {
            currentPanelWidth = initialPanelWidth + progress * (finalPanelWidth - initialPanelWidth)
            currentX = targetX + finalPanelWidth - currentPanelWidth

            if (progress >= 1f) {
                animating = false
                currentX = targetX
                currentPanelWidth = finalPanelWidth
            }
        }
    }

    fun startAnimation() {
        animating = true
        closing = false
        animationStartTime = System.currentTimeMillis()
        settingsOpened = true
    }

    fun startClosingAnimation() {
        animating = true
        closing = true
        animationStartTime = System.currentTimeMillis()
    }
    //currentX + 10.5f, y + 8f
    override fun mouseClicked(mouseX: Int, mouseY: Int) {
        super.mouseClicked(mouseX, mouseY)
        if (isMouseOver(mouseX, mouseY, currentX + 10.5f, y + 8f, 30f, 30f)) {
            startClosingAnimation()
        }

        for (element in settingsElements) {
            element.mouseClicked(mouseX, mouseY)
        }

        toggleButton.onClick(mouseX, mouseY)
    }

    override fun onRelease(mouseX: Int, mouseY: Int) {
        super.onRelease(mouseX, mouseY)
        for (element in settingsElements) {
            element.onRelease(mouseX, mouseY)
        }
    }
}


