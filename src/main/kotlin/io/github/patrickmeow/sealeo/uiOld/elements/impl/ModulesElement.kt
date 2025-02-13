package io.github.patrickmeow.sealeo.uiOld.elements.impl

import io.github.patrickmeow.sealeo.Sealeo.mc
import io.github.patrickmeow.sealeo.features.Category
import io.github.patrickmeow.sealeo.features.Module
import io.github.patrickmeow.sealeo.features.ModuleManager.modules
import io.github.patrickmeow.sealeo.uiOld.ClickGui
import io.github.patrickmeow.sealeo.uiOld.elements.Element
import io.github.patrickmeow.sealeo.utils.RenderUtils
import java.awt.Color
import kotlin.math.max
import kotlin.math.min

import org.lwjgl.input.Mouse
import org.lwjgl.opengl.GL11

class ModulesElement(val categoriesElement: CategoriesElement) : Element() {

    private val moduleButtons = ArrayList<ToggleButton>()
    private var previousCategory: Category? = null
    private var scrollOffsetY = 0f
    private val scrollSpeed = 15f
    private var settingsOpened = false
    private lateinit var settingsTab: SettingsPanel
    private lateinit var searchButton: SearchButton

    init {
        searchButton = SearchButton()
    }

    override fun draw(mouseX: Int, mouseY: Int) {
        super.draw(mouseX, mouseY)

        // Handle scrolling
        handleScrolling()

        var offsetY = scrollOffsetY
        var color = Color(33, 35, 44)
        searchButton.draw(mouseX, mouseY)
        // Set up the scissor box to only render within the ClickGUI box
        val scissorX = 370f
        val scissorY = 150f
        val scissorWidth = 300f
        val scissorHeight = 262f

        // Enable scissor testing



        GL11.glEnable(GL11.GL_SCISSOR_TEST)
        GL11.glScissor(
            (scissorX.toInt() * mc.displayWidth / ClickGui.sc!!.scaledWidth),
            ((mc.displayHeight - (scissorY.toInt() + scissorHeight) * mc.displayHeight / ClickGui.sc!!.scaledHeight).toInt()),
            (scissorWidth.toInt() * mc.displayWidth / ClickGui.sc!!.scaledWidth),
            (scissorHeight.toInt() * mc.displayHeight / ClickGui.sc!!.scaledHeight)
        )

        for (module in modules) {
            if (module.hidden) continue
            if (module.category == categoriesElement.selectedCategory) {
                if (offsetY >= -600f && offsetY <= 2500f) {
                    if (isMouseOver(mouseX, mouseY, 370f, 150f + offsetY, 300f, 40f)) {
                        color = Color(37, 40, 51)
                    } else {
                        color = Color(33, 35, 44)
                    }
                    RenderUtils.roundedRectangle(370f, 150f + offsetY, 300f, 40f, color, 6f, 0.1f)
                    RenderUtils.drawText(module.name, 380f, 160f + offsetY, -1, 1.35f)
                    RenderUtils.drawText(module.description, 380f, 175f + offsetY, 0xFF5b5b5b.toInt(), 1f)
                    val button = ToggleButton(580f, 161f + offsetY, module, false)
                    button.updatePosition(580f, 161f + offsetY)
                    if (!moduleButtons.contains(button)) {
                        moduleButtons.add(button)
                    } else {
                        moduleButtons.find { it.parent == module }?.updatePosition(580f, 151f + offsetY)
                    }

                    button.draw()
                }
                offsetY += 50f
            }
        }

        // Disable scissor testing
        GL11.glDisable(GL11.GL_SCISSOR_TEST)

        if (settingsOpened) {
            settingsTab.draw(mouseX, mouseY)
        }
    }

    private fun handleScrolling() {
        val wheel = Mouse.getDWheel()
        if (wheel != 0) {
            val direction = if (wheel > 0) 1 else -1
            scrollOffsetY += direction * scrollSpeed
            scrollOffsetY = clampScrollOffset(scrollOffsetY)
        }
    }

    private fun clampScrollOffset(offsetY: Float): Float {
        val totalHeight = modules.count { it.category == categoriesElement.selectedCategory } * 80f
        val maxScroll = Math.max(0f, totalHeight - 250f)
        return Math.max(-maxScroll, Math.min(0f, offsetY))
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int) {
        super.mouseClicked(mouseX, mouseY)
        if (settingsOpened) {
            settingsTab.mouseClicked(mouseX, mouseY)
            return
        }
        for (button in moduleButtons) {
            if (button.isMouseOver(mouseX, mouseY)) {
                button.onClick(mouseX, mouseY)
            }
        }
        if (previousCategory != categoriesElement.selectedCategory) {
            moduleButtons.clear()
            previousCategory = categoriesElement.selectedCategory
            scrollOffsetY = 0f
        }
        var offsetY = scrollOffsetY
        for (module in modules) {
            if (module.hidden) continue
            if (module.category == categoriesElement.selectedCategory) {
                if (isMouseOver(mouseX, mouseY, 370f, 140f + offsetY, 300f, 40f)) {
                    println("Clicked ${module.name}")
                    settingsTab = SettingsPanel(module, this)
                    settingsOpened = true
                    settingsTab.startAnimation()
                }
                offsetY += 50f
            }
        }
    }

    class ToggleButton(var x: Float, var y: Float, var parent: Module, var isAnimating: Boolean) {

        private var animationStartTime: Long = 0
        private val MAX_INCREMENT = 25
        private val ANIMATION_DURATION = 250

        private var increment = if (parent.enabled) {
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

            if (parent.enabled) {
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

            val buttonColor = Color(30, 33, 41)

            RenderUtils.roundedRectangle(x, y, 60f, 18f, buttonColor, 5f, 0.1f)
            RenderUtils.roundedRectangle(x + increment, y, 35f, 18f, color, 5f, 0.2f)
        }

        fun onClick(mouseX: Int, mouseY: Int) {
            if (isMouseOver(mouseX, mouseY)) {
                println("Clicked " + parent.name)
                parent.setToggled()
                isAnimating = true
                animationStartTime = System.currentTimeMillis()
            }
        }

        fun isMouseOver(mouseX: Int, mouseY: Int): Boolean {
            return (mouseX >= x && mouseX <= x + 35f) && (mouseY >= y && mouseY <= y + 18f)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is ToggleButton) return false
            return parent.name == other.parent.name
        }

        override fun hashCode(): Int {
            var result = x.hashCode()
            result = 31 * result + y.hashCode()
            result = 31 * result + parent.hashCode()
            result = 31 * result + isAnimating.hashCode()
            result = 31 * result + animationStartTime.hashCode()
            result = 31 * result + MAX_INCREMENT
            result = 31 * result + ANIMATION_DURATION
            result = 31 * result + increment
            return result
        }
    }
}
