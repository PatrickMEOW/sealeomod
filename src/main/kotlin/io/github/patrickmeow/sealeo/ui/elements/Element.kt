package io.github.patrickmeow.sealeo.ui.elements

import io.github.patrickmeow.sealeo.features.Module
import io.github.patrickmeow.sealeo.features.settings.Setting

open class Element() {




    open fun draw(mouseX: Int, mouseY: Int) {

    }

    open fun mouseClicked(mouseX: Int, mouseY: Int) {



    }

    fun isMouseOver(mouseX: Int, mouseY: Int, x: Float, y: Float, width: Float, height: Float): Boolean {
        return (mouseX >= x && mouseX <= x + width) && (mouseY >= y && mouseY <= y + height)
    }



}