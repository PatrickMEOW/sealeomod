package io.github.patrickmeow.sealeo.ui.elements.impl

import io.github.patrickmeow.sealeo.features.Category
import io.github.patrickmeow.sealeo.features.Module
import io.github.patrickmeow.sealeo.features.ModuleManager.modules
import io.github.patrickmeow.sealeo.ui.elements.Element
import io.github.patrickmeow.sealeo.utils.RenderUtils
import java.awt.Color
import kotlin.math.max
import kotlin.math.min

class ModulesElement(val categoriesElement: CategoriesElement) : Element() {

    val moduleButtons = ArrayList<ToggleButton>()
    var previousCategory: Category? = null

    override fun draw(mouseX: Int, mouseY: Int) {
        super.draw(mouseX, mouseY)

        // Check if the category has changed
        if (previousCategory != categoriesElement.selectedCategory) {
            moduleButtons.clear()
            previousCategory = categoriesElement.selectedCategory
        }

        var offsetY = 0f
        val color = Color(33,35,44)
        val buttonColor = Color(30,33,41)


        // Shadow
        RenderUtils.roundedRectangle(352f, 132f, 334f, 254f, Color(28,28,35), 6f, .1f)
        RenderUtils.roundedRectangle(356f, 136f, 328f, 250f, Color(30,33,41), 6f, .1f)

        for (module in modules) {
            if (module.category == categoriesElement.selectedCategory) {
                RenderUtils.roundedRectangle(370f, 140f + offsetY, 300f, 60f, color, 6f, 0.1f)
                RenderUtils.drawText(module.name, 380f, 160f + offsetY, -1, 1.5f)
                RenderUtils.drawText(module.description, 380f, 180f + offsetY, 0xFF5b5b5b.toInt(), 1f)

                RenderUtils.roundedRectangle(580f, 160f + offsetY, 60f, 20f, buttonColor, 6f, 0.1f)
                val button = ToggleButton(580f, 161f + offsetY, module, false)
                if (!moduleButtons.contains(button)) {
                    moduleButtons.add(button)
                }
                offsetY += 80f
            }
        }

        for (button in moduleButtons) {
            button.draw()
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int) {
        super.mouseClicked(mouseX, mouseY)
        var offsetY = 0f
        for (module in modules) {
            if (module.category == categoriesElement.selectedCategory) {
                if (isMouseOver(mouseX, mouseY, 580f, 160f + offsetY, 60f, 20f)) {
                    println(module.name)
                }
                offsetY += 80f
            }
        }

        for (button in moduleButtons) {
            button.onClick(mouseX, mouseY)
        }
    }

    class ToggleButton(var x: Float, var y: Float, var parent: Module, var isAnimating: Boolean) {

        private var animationStartTime: Long = 0
        private val MAX_INCREMENT = 25
        private val ANIMATION_DURATION = 250

        private var increment = if(parent.enabled) {
            MAX_INCREMENT
        } else {
            0
        }

        fun updateAnimation() {
            if(!isAnimating) return
            val elapsedTime = System.currentTimeMillis() - animationStartTime

            if (parent.enabled) {
                increment = min(
                    ((elapsedTime * MAX_INCREMENT) / ANIMATION_DURATION).toInt(), MAX_INCREMENT)
            } else {
                increment = max(
                    MAX_INCREMENT - ((elapsedTime * MAX_INCREMENT) / ANIMATION_DURATION).toInt(), 0)
            }

            if (elapsedTime >= ANIMATION_DURATION) {
                isAnimating = false
                increment = if (parent.enabled) MAX_INCREMENT else 0
            }
        }

        fun draw() {
            updateAnimation()

            val color: Color = if (parent.enabled) {
                Color(18, 205, 107)
            } else {
                Color(250, 75, 41)
            }
            RenderUtils.roundedRectangle(x + increment, y, 35f, 18f, color, 5f, 0.2f)
        }

        fun onClick(mouseX: Int, mouseY: Int) {
            if (isMouseOver(mouseX, mouseY, x, y, 35f, 18f)) {
                println("Clicked " + parent.name)
                parent.setToggled()
                isAnimating = true
                animationStartTime = System.currentTimeMillis()
            }
        }

        private fun isMouseOver(mouseX: Int, mouseY: Int, x: Float, y: Float, width: Float, height: Float): Boolean {
            return (mouseX >= x && mouseX <= x + width) && (mouseY >= y && mouseY <= y + height)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is ToggleButton) return false
            return parent.name == other.parent.name
        }

    }

}
