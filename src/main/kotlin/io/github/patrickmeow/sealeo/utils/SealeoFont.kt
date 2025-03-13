package io.github.patrickmeow.sealeo.utils
import gg.essential.elementa.font.FontRenderer
import gg.essential.elementa.font.data.Font
import io.github.patrickmeow.sealeo.uiOld.font.GlyphPageFontRenderer
import java.awt.Color
import java.io.File


object SealeoFont {


    private lateinit var fontRenderer: GlyphPageFontRenderer


    fun init() {
        val fontStream = this::class.java.classLoader.getResourceAsStream("assets/sealeo/font/Nexa.ttf")
        if (fontStream != null) {
            val tempFile = File.createTempFile("Nexa", ".ttf")
            tempFile.deleteOnExit()
            fontStream.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            fontRenderer = GlyphPageFontRenderer(tempFile.absolutePath)
        } else {
            throw IllegalArgumentException("Font file not found!")
        }
    }



    fun text(text: String, x: Float, y: Float, color: Long, shadow: Boolean = false, size: Int) {
        fontRenderer.drawString(text, x, y, color, shadow, size)
    }

    fun shadowText(text: String, x: Float, y: Float, color: Long, shadow: Boolean = true, size: Int) {
        fontRenderer.drawString(text, x, y, color, shadow, size)
    }




}