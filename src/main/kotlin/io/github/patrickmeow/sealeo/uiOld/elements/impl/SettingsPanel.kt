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
    private val sliderSettings = ArrayList<SliderElement>()
    private val initialPanelWidth = 100f
    private val finalPanelWidth = 390f

    private val targetX = x + 525f - finalPanelWidth
    private var currentX: Float = targetX + finalPanelWidth - initialPanelWidth
    private var currentPanelWidth: Float = initialPanelWidth

    override fun draw(mouseX: Int, mouseY: Int) {
        if (animating) updateAnimation()

        super.draw(mouseX, mouseY)
        val color = Color(33, 35, 44)
        val moduleText = module.name
        val textWidth = RenderUtils.measureTextWidth(moduleText, 1.5f)

        val moduleTextPosX = (currentPanelWidth - textWidth) / 2

        RenderUtils.roundedRectangle(currentX, ClickGui.y, currentPanelWidth, 320f, color, color, color, 0f, 0f, 8f, 0f, 8f, 0.1f)

        // Enable scissor testing
        GL11.glEnable(GL11.GL_SCISSOR_TEST)
        // Set the scissor box to the bounds of the panel
        GL11.glScissor(
            (currentX.toInt() * mc.displayWidth / ClickGui.sc!!.scaledWidth),
            (mc.displayHeight - (ClickGui.y.toInt() + 320) * mc.displayHeight / ClickGui.sc!!.scaledHeight),
            (currentPanelWidth.toInt() * mc.displayWidth / ClickGui.sc!!.scaledWidth),
            (320 * mc.displayHeight / ClickGui.sc!!.scaledHeight)
        )

        // Draw the text within the scissor box
        RenderUtils.drawText(moduleText, currentX + moduleTextPosX, ClickGui.y + 20f, -1, 1.5f)

        for (setting in module.settings) {
            if (setting is NumberSetting) {
                /*
                RenderUtils.drawText(setting.name, currentX + moduleTextPosX - 100f, y + 80f, -1, 1f)
                RenderUtils.drawText(setting.min.toString(), currentX + moduleTextPosX - 100f, y + 100f, -1, 1f)
                // Slider
                RenderUtils.roundedRectangle(currentX + moduleTextPosX - 70f, y + 100f, 80f, 5f, Color(26,27,35), 3f)
                var sliderProgress = setting.value.toFloat() / setting.max.toFloat()
                RenderUtils.roundedRectangle(currentX + moduleTextPosX - 70f, y + 100f, sliderProgress * 80f, 5f, Color(48,158,233), 3f)

                RenderUtils.drawText(setting.value.toString(), currentX + moduleTextPosX, y + 100f, -1, 1f)


                 */

                val sliderSetting = SliderElement(currentX + moduleTextPosX - 100f, y + 80f, setting)
                if(!sliderSettings.contains(sliderSetting)) {
                    //println("adding")
                    sliderSettings.add(sliderSetting)
                } else {
                    sliderSettings.find { it.setting == setting }?.updatePosition(currentX + moduleTextPosX - 100f, y + 80f)
                }
                sliderSetting.draw(mouseX, mouseY)

            }
        }

        // Disable scissor testing
        GL11.glDisable(GL11.GL_SCISSOR_TEST)
    }



    fun updateAnimation() {
        // Get the current time
        val elapsedTime = System.currentTimeMillis() - animationStartTime

        // Calculate the progress (normalized value between 0 and 1)
        val progress = (elapsedTime.toFloat() / ANIMATION_DURATION).coerceIn(0f, 1f)

        // Calculate the new width based on the progress
        currentPanelWidth = initialPanelWidth + progress * (finalPanelWidth - initialPanelWidth)

        // Calculate the new position based on the new width
        currentX = targetX + finalPanelWidth - currentPanelWidth

        // If the animation is complete, stop animating
        if (progress >= 1f) {
            animating = false
            currentX = targetX
            currentPanelWidth = finalPanelWidth
        }
    }

    // Call this function to start the animation
    fun startAnimation() {
        animating = true
        animationStartTime = System.currentTimeMillis()
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int) {
        super.mouseClicked(mouseX, mouseY)
        for(slider in sliderSettings) {
            slider.mouseClicked(mouseX, mouseY)
        }
    }


}
