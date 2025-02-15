package io.github.patrickmeow.sealeo.utils
import gg.essential.elementa.font.FontRenderer
import gg.essential.elementa.font.data.Font
import io.github.patrickmeow.sealeo.uiOld.font.GlyphPageFontRenderer
import java.awt.Color


object SealeoFont {


    private lateinit var fontRenderer: GlyphPageFontRenderer


    fun init() {
        fontRenderer = GlyphPageFontRenderer("C:/Users/Patryk/Desktop/sealeomod/src/main/resources/assets/sealeo/font/Nexa.ttf")


    }

    fun text(text: String, x: Float, y: Float, color: Long, shadow: Boolean = false, size: Int) {
        fontRenderer.drawString(text, x, y, color, shadow, size)
    }




}