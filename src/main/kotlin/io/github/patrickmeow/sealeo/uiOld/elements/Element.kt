package io.github.patrickmeow.sealeo.uiOld.elements

open class Element() {




    open fun draw(mouseX: Int, mouseY: Int) { }

    open fun mouseClicked(mouseX: Int, mouseY: Int) { }

    open fun onRelease(mouseX: Int, mouseY: Int) { }

    open fun keyTyped(typedChar: Char, keyCode: Int) { }

    fun isMouseOver(mouseX: Int, mouseY: Int, x: Float, y: Float, width: Float, height: Float): Boolean {
        return (mouseX >= x && mouseX <= x + width) && (mouseY >= y && mouseY <= y + height)
    }


    open fun updatePosition(fl: Float, fl1: Float) {

    }


}