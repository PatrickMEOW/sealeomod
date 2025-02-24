package io.github.patrickmeow.sealeo.uiOld.elements.impl

import io.github.patrickmeow.sealeo.features.Category
import io.github.patrickmeow.sealeo.uiOld.elements.Element
import io.github.patrickmeow.sealeo.utils.RenderUtils
import io.github.patrickmeow.sealeo.utils.SealeoFont
import java.awt.Color
import io.github.patrickmeow.sealeo.uiOld.ClickGui.x
import io.github.patrickmeow.sealeo.uiOld.ClickGui.y

class CategoriesElement : Element() {

    var selectedCategory = Category.SKYBLOCK
    private var rectColor: Color = Color(33, 35, 44)

    override fun draw(mouseX: Int, mouseY: Int) {
        super.draw(mouseX, mouseY)
        var color: Color
        val backgroundColor = Color(28, 28, 35)
        var offsetY = 0f

        for (category in Category.entries) {
            if (isMouseOver(mouseX, mouseY, x + 27.5f, y + 45f + offsetY, 55f, 24f) || selectedCategory == category) {
                if (isMouseOver(mouseX, mouseY, x + 27.5f, y + 50f + offsetY, 55f, 24f)) {
                    color = Color(37,40,51)
                    if (selectedCategory == category) {
                        color = Color(39, 45, 62)
                    }
                } else {
                    color = rectColor
                    if (selectedCategory == category) {
                        color = Color(39, 45, 62)
                    }
                }

                RenderUtils.roundedRectangle(x + 12.5f, y + 42f + offsetY, 105f, 25f, color, 6f, 0.1f)
            }
            RenderUtils.drawText(category.getFormattedName(), x + 27.5f, y + 46f + offsetY, -1, 1f)

            offsetY += 40f
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int) {
        super.mouseClicked(mouseX, mouseY)
        var offset = 0f
        for (category in Category.entries) {
            if (isMouseOver(mouseX, mouseY, x + 27.5f, y + 50f + offset, 55f, 24f)) {
                selectedCategory = category
                println("selected $category")
            }
            offset += 40f
        }
    }
}
