package io.github.patrickmeow.sealeo.uiOld.elements.impl

import io.github.patrickmeow.sealeo.Sealeo.mc
import io.github.patrickmeow.sealeo.features.Category
import io.github.patrickmeow.sealeo.features.Module
import io.github.patrickmeow.sealeo.features.ModuleManager

import io.github.patrickmeow.sealeo.uiOld.ClickGui
import io.github.patrickmeow.sealeo.uiOld.elements.Element
import io.github.patrickmeow.sealeo.utils.RenderUtils
import io.github.patrickmeow.sealeo.utils.SealeoFont
import java.awt.Color
import kotlin.math.max
import kotlin.math.min
import io.github.patrickmeow.sealeo.uiOld.ClickGui.x
import io.github.patrickmeow.sealeo.uiOld.ClickGui.y
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.GL11




class ModulesElement(val categoriesElement: CategoriesElement) : Element() {

    val moduleButtons = ArrayList<ToggleButton>()
    private var previousCategory: Category = Category.SKYBLOCK
    private var scrollOffsetY = 0f
    private val scrollSpeed = 15f
    private var settingsOpened = false
    private var settingsTab: SettingsPanel? = null
    private var searchButton: SearchButton = SearchButton(this)


    private var modules = searchButton.filteredModules.ifEmpty { ModuleManager.modules }

    override fun draw(mouseX: Int, mouseY: Int) {



        modules = searchButton.filteredModules.ifEmpty { ModuleManager.modules }
        if(searchButton.isEmpty) {
            modules = searchButton.filteredModules
            moduleButtons.clear()
        }
        super.draw(mouseX, mouseY)
        //println(categoriesElement.selectedCategory.toString())
        // Handle scrolling
        handleScrolling()
        var offsetY = scrollOffsetY

        var color = Color(33, 35, 44)
        searchButton.draw(mouseX, mouseY)

        val scissorX = x + 152.5f
        val scissorY = y + 40f
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
            if (module.category == categoriesElement.selectedCategory || searchButton.filteredModules.isNotEmpty()) {
                if (offsetY >= -600f && offsetY <= 2500f) {
                    if (isMouseOver(mouseX, mouseY, x + 152.5f, y + 40f + offsetY, 300f, 40f)) {
                        color = Color(37, 40, 51)
                    } else {
                        color = Color(33, 35, 44)
                    }
                    RenderUtils.roundedRectangle(x + 152.5f, y + 40f + offsetY, 300f, 40f, color, 6f, 0.1f)
                    RenderUtils.drawText(module.name, x + 162.5f, y + 48f + offsetY, -1, 1.4f)
                    RenderUtils.drawText(module.description, x + 162.5f, y + 65f + offsetY, 0xFF5b5b5b, 1f)
                    val button = ToggleButton(x + 362.5f, y + 51f + offsetY, module, false)
                    button.updatePosition(x + 362.5f, y + 51f + offsetY)
                    if (!moduleButtons.contains(button)) {
                        moduleButtons.add(button)
                    } else {
                        moduleButtons.find { it.parent == module }?.updatePosition(x + 362.5f, y + 51f + offsetY)
                    }

                    //button.draw()
                }
                offsetY += 50f
            }
        }
        for(button in moduleButtons) {
            button.draw()
        }

        // Disable scissor testing
        GL11.glDisable(GL11.GL_SCISSOR_TEST)

        if (settingsTab?.settingsOpened == true) {
            settingsTab?.draw(mouseX, mouseY)
        }
    }

    private fun handleScrolling() {
        val wheel = Mouse.getDWheel()
        if (wheel != 0) {
            val direction = if (wheel > 0) 1 else -1
            scrollOffsetY += direction * scrollSpeed
            scrollOffsetY = clampScrollOffset(scrollOffsetY)
            val totalHeight = modules.count { it.category == categoriesElement.selectedCategory } * 50f
            val viewHeight = 262f
            val maxScroll = max(0f, totalHeight - viewHeight)
            scrollOffsetY = max(viewHeight - totalHeight, scrollOffsetY)
            scrollOffsetY = min(0f, scrollOffsetY)
        }
    }

    private fun clampScrollOffset(offsetY: Float): Float {
        val totalHeight = modules.count { it.category == categoriesElement.selectedCategory } * 50f
        val viewHeight = 262f
        val maxScroll = max(0f, totalHeight - viewHeight)
        return max(viewHeight - totalHeight, min(0f, offsetY))
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int) {
        super.mouseClicked(mouseX, mouseY)

        if (previousCategory != categoriesElement.selectedCategory) {

            modules = ModuleManager.modules
            searchButton.resetSearch()
            searchButton.filteredModules.clear()
            moduleButtons.clear()
            settingsTab?.startClosingAnimation()
            scrollOffsetY = 0f
            previousCategory = categoriesElement.selectedCategory
        }
        if (settingsTab?.settingsOpened == true) {
            settingsTab?.mouseClicked(mouseX, mouseY)
            return
        }

        searchButton.mouseClicked(mouseX, mouseY)

        // Check if the click is within any toggle button
        for (button in moduleButtons) {
            if (button.isMouseOver(mouseX, mouseY)) {
                button.onClick(mouseX, mouseY)
                return // Skip the module click handling
            }
        }

        var offsetY = scrollOffsetY


        for (module in modules) {
            if (module.hidden) continue
            if (module.category == categoriesElement.selectedCategory || searchButton.filteredModules.isNotEmpty()) {
                if (isMouseOver(mouseX, mouseY, x + 152.5f, y + 30f + offsetY, 300f, 40f)) {
                    println("$mouseX $mouseY")
                    println("Clicked ${module.name}")
                    settingsTab = SettingsPanel(module, this)
                    //settingsOpened = true
                    settingsTab?.startAnimation()
                }
                offsetY += 50f
            }
        }
    }

    override fun onRelease(mouseX: Int, mouseY: Int) {
        super.onRelease(mouseX, mouseY)
        if(settingsTab?.settingsOpened == true) {
            settingsTab?.onRelease(mouseX, mouseY)
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
            return (mouseX >= x && mouseX <= x + 60f) && (mouseY >= y && mouseY <= y + 18f)
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

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        super.keyTyped(typedChar, keyCode)
        searchButton.keyTyped(typedChar, keyCode)
        if(settingsTab?.settingsOpened == true) {
            settingsTab?.keyTyped(typedChar, keyCode)
        }
    }
}
