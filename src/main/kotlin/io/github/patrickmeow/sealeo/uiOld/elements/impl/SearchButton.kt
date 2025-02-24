package io.github.patrickmeow.sealeo.uiOld.elements.impl

import io.github.patrickmeow.sealeo.features.Module
import io.github.patrickmeow.sealeo.features.ModuleManager
import io.github.patrickmeow.sealeo.uiOld.ClickGui
import io.github.patrickmeow.sealeo.uiOld.elements.Element
import io.github.patrickmeow.sealeo.utils.RenderUtils
import java.awt.Color


class SearchButton(var modulesElement: ModulesElement) : Element() {
    private var searchString: String = "Search.."
    private var isSearching: Boolean = false
    var filteredModules: MutableList<Module> = mutableListOf()
    private var showUnderscore: Boolean = true
    private var lastToggleTime: Long = System.currentTimeMillis()
    private val TOGGLE_INTERVAL: Long = 500 // 500ms for flickering effect
    private var x = ClickGui.x + 382.5f
    private var y = ClickGui.y + 5f
    //private val x = 600f
    //private val y = 115f
    private val width = 120f
    private val height = 25f
    val color = Color(33, 35, 44)
    var isEmpty = false



    fun resetSearch() {
        searchString = "Search.."
    }

    override fun draw(mouseX: Int, mouseY: Int) {
        isEmpty = searchString.length >= 2 && filteredModules.isEmpty() && searchString != "Search.."
        x = ClickGui.x + 382.5f
        y = ClickGui.y + 5f
        updateUnderscore()
        val displayText = if (isSearching && showUnderscore) searchString + "_" else searchString
        RenderUtils.roundedRectangle(x, y, width, height, color, 6f, 0.1f)
        RenderUtils.drawText(displayText, x + 10f, y + 5f, 0xFF687076, 1f)
    }

    private fun updateUnderscore() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastToggleTime >= TOGGLE_INTERVAL) {
            showUnderscore = !showUnderscore
            lastToggleTime = currentTime
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int) {
        super.mouseClicked(mouseX, mouseY)
        if (isMouseOver(mouseX, mouseY, x, y, width, height)) {
            if (searchString == "Search..") {
                searchString = ""
                isSearching = true
                println("searching")
            } else if (!isSearching) {
                isSearching = true
            }
        }
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (!isSearching) return

        // Handle backspace
        if (keyCode == 14) { // Backspace
            if (searchString.isNotEmpty()) {
                searchString = searchString.substring(0, searchString.length - 1)
                processSearch()
            }
            return
        }

        // Check if the entered character is valid
        if (typedChar.isLetterOrDigit() || typedChar.isWhitespace()) {
            searchString += typedChar
            println(typedChar)
            println(searchString)
            processSearch()
        }
    }

    private fun processSearch() {
        val modules = ModuleManager.modules

        modulesElement.moduleButtons.clear()
        if (searchString == "_" || searchString.isEmpty()) {
            filteredModules.clear()

            return
        }
        val query = searchString.lowercase()

        for (module in modules) {
            if (module.name.lowercase().startsWith(query) && !filteredModules.contains(module)) {
                println("adding")
                filteredModules.add(module)
            } else {
                if (filteredModules.contains(module) && !module.name.lowercase().startsWith(query)) {
                    filteredModules.remove(module)
                }
            }
        }
    }

}