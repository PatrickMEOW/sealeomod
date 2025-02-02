package io.github.patrickmeow.sealeo.ui.elements.impl

import io.github.patrickmeow.sealeo.features.Module
import io.github.patrickmeow.sealeo.features.ModuleManager
import io.github.patrickmeow.sealeo.ui.elements.Element
import io.github.patrickmeow.sealeo.utils.RenderUtils
import java.awt.Color


class SearchButton() : Element() {
    private var searchString: String = "Search.."
    private var isSearching: Boolean = false
    private var filteredModules: MutableList<Module> = mutableListOf()
    private var showUnderscore: Boolean = true
    private var lastToggleTime: Long = System.currentTimeMillis()
    private val TOGGLE_INTERVAL: Long = 500 // 500ms for flickering effect
    private val x = 650f
    private val y = 100f
    private val width = 80f
    private val height = 20f
    val color = Color(30,33,41)


    override fun draw(mouseX: Int, mouseY: Int) {
        updateUnderscore()
        val displayText = if (isSearching && showUnderscore) searchString + "_" else searchString
        //RoundedRect.drawRoundedRect(x, y, x + width, y + height, 8f, 0xFF1f2129.toInt())
        //RenderUtils.drawText(displayText, (x + 5).toInt(), (y + 7).toInt(), 0xFF687076.toInt(), 1F)

        RenderUtils.roundedRectangle(x, y, width, height, color, 6f, 0.1f)
        RenderUtils.drawText(displayText, x + 10f, y + 5f, 0xFF687076.toInt(), 1f)
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
            } else if (!isSearching) {
                isSearching = true
            }
        } else {
            //searchString = "Search.."
            //isSearching = false
            //filteredModules.clear()
        }
    }


    fun keyTyped(keyCode: Int, typedChar: Char) {
        if (!isSearching) return

        println(keyCode)

        if (keyCode == 14) { // Backspace
            if (searchString.isEmpty()) {
                // processSearch()
                return
            } else {
                searchString = searchString.substring(0, searchString.length - 1)
                processSearch()
                return
            }
        }

        if (searchString.length == 13) {
            return
        }

        if (searchString == "Search..") {
            searchString = ""
        }

        searchString += typedChar
        processSearch()
    }



    private fun processSearch() {
        val modules = ModuleManager.modules

        if (searchString == "_" || searchString.isEmpty()) {
            filteredModules.clear()
            return
        }
        val query = searchString.lowercase()

        for (module in modules) {
            if (module.name.lowercase().startsWith(query) && !filteredModules.contains(module)) {
                filteredModules.add(module)
            } else {
                if (filteredModules.contains(module) && !module.name.lowercase().startsWith(query)) {
                    filteredModules.remove(module)
                }
            }
        }
    }

    fun getFilteredModules(): List<Module> {
        return filteredModules
    }
}