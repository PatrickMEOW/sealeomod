package io.github.patrickmeow.sealeo.uiOld.elements.impl

import io.github.patrickmeow.sealeo.Sealeo.mc
import io.github.patrickmeow.sealeo.features.Module
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
    private val sliderSettings = ArrayList<SliderElement>()
    private val initialPanelWidth = 0f
    private val finalPanelWidth = 390f
    var settingsOpened = true
    private val targetX = x + 525f - finalPanelWidth
    private var currentX: Float = targetX + finalPanelWidth - initialPanelWidth
    private var currentPanelWidth: Float = initialPanelWidth

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

        RenderUtils.drawText(moduleText, currentX + moduleTextPosX, y + 20f, -1, 1.5f)

        for (setting in module.settings) {
            if (setting is NumberSetting) {
                val sliderSetting = SliderElement(currentX + moduleTextPosX, y + 60f, setting)
                if(!sliderSettings.contains(sliderSetting)) {
                    sliderSettings.add(sliderSetting)
                    println("adding")
                } else {
                    sliderSettings.find { it.setting == setting }?.updatePosition(currentX + moduleTextPosX, y + 60f)
                }
                //sliderSetting.draw(mouseX, mouseY)
            }
        }

        for(slider in sliderSettings) {
            slider.draw(mouseX, mouseY)
        }

        RenderUtils.drawText("Back", currentX + 20f, y + 20f, 0xFF868686, 1f)


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

    override fun mouseClicked(mouseX: Int, mouseY: Int) {
        super.mouseClicked(mouseX, mouseY)
        if(isMouseOver(mouseX, mouseY, currentX + 50f, y + 20f, 20f, 10f)) {
            startClosingAnimation()
        }

        for(slider in sliderSettings) {
            slider.mouseClicked(mouseX, mouseY)
        }


    }

    override fun onRelease(mouseX: Int, mouseY: Int) {
        super.onRelease(mouseX, mouseY)
        for(slider in sliderSettings) {
            slider.onRelease(mouseX, mouseY)
        }

    }

}