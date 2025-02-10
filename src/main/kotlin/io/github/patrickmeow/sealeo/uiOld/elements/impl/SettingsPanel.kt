package io.github.patrickmeow.sealeo.uiOld.elements.impl

import io.github.patrickmeow.sealeo.features.Module
import io.github.patrickmeow.sealeo.uiOld.ClickGui
import io.github.patrickmeow.sealeo.uiOld.ClickGui.height
import io.github.patrickmeow.sealeo.uiOld.ClickGui.x
import io.github.patrickmeow.sealeo.uiOld.ClickGui.y
import io.github.patrickmeow.sealeo.uiOld.elements.Element
import io.github.patrickmeow.sealeo.utils.RenderUtils
import java.awt.Color

class SettingsPanel(var module: Module, var modulesElement: ModulesElement) : Element() {

    private var animationStartTime: Long = 0
    private val MAX_INCREMENT = 25
    private val ANIMATION_DURATION = 250
    var animating: Boolean = false

    // Panel's width is fixed
    private val panelWidth = 390f
    // The right edge of the panel should always be at x + 525
    private val targetX = x + 525f - panelWidth  // This will be the left edge of the panel

    private var currentX: Float = ClickGui.width.toFloat()  // Start off-screen to the right

    override fun draw(mouseX: Int, mouseY: Int) {
        if (animating) updateAnimation()

        super.draw(mouseX, mouseY)
        val color = Color(33, 35, 44)
        // Use currentX for the left position, while the right edge is always at x + 525
        RenderUtils.roundedRectangle(currentX, y, panelWidth, 320f, color, color, color, 0f, 0f, 8f, 0f, 8f, 0.1f)
    }

    fun updateAnimation() {
        // Get the current time
        val elapsedTime = System.currentTimeMillis() - animationStartTime

        // Calculate the progress (normalized value between 0 and 1)
        val progress = (elapsedTime.toFloat() / ANIMATION_DURATION).coerceIn(0f, 1f)

        // Calculate the new position based on the progress
        // Panel should move from off-screen (ClickGui.width) to targetX
        currentX = (ClickGui.width.toFloat() - panelWidth) - (progress * (ClickGui.width.toFloat() - panelWidth))

        // If the current position reaches or goes past targetX, stop the animation
        if (currentX <= targetX) {
            animating = false
            currentX = targetX // Ensure it stops exactly at targetX
        }
    }

    // Call this function to start the animation
    fun startAnimation() {
        animating = true
        animationStartTime = System.currentTimeMillis()
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int) {
        super.mouseClicked(mouseX, mouseY)
    }
}
