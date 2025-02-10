package io.github.patrickmeow.sealeo.utils
import gg.essential.elementa.font.FontRenderer
import gg.essential.elementa.font.data.Font
import io.github.patrickmeow.sealeo.uiOld.font.GlyphPageFontRenderer
import java.awt.Color


object SealeoFont {


    private lateinit var fontRenderer: FontRenderer
    private lateinit var customFont: GlyphPageFontRenderer
    private lateinit var size24: GlyphPageFontRenderer
    private lateinit var size20: GlyphPageFontRenderer
    private lateinit var size34: GlyphPageFontRenderer
    const val REGULAR = 1
    const val BOLD = 2

    fun init() {
        fontRenderer = FontRenderer(Font.fromResource("/assets/sealeo/font/Regular"), Font.fromResource("/assets/sealeo/font/SemiBold"))
        customFont = GlyphPageFontRenderer.create("Arial", 20, true, true, true)
        size24 = GlyphPageFontRenderer.create("Arial", 24, true, true, true)
        size20 = GlyphPageFontRenderer.create("Arial", 20, true, true, true)
        size34 = GlyphPageFontRenderer.create("Arial", 20, true, true, true)

    }

    fun text(text: String, x: Float, y: Float, size: Float, color: Color) {
        //fontRenderer.drawString(UMatrixStack.Compat.get(), text, color, x, y, 1f, size * 8f, false)
        customFont.drawString(text, x, y, 0xFFcfe2f3, false)
    }

    fun text24(text: String, x: Float, y: Float, size: Float, color: Color) {
        //fontRenderer.drawString(UMatrixStack.Compat.get(), text, color, x, y, 1f, size * 8f, false)
        size24.drawString(text, x, y, 0xFFcfe2f3, false)
    }

    fun text20(text: String, x: Float, y: Float, size: Float, color: Color) {
        //fontRenderer.drawString(UMatrixStack.Compat.get(), text, color, x, y, 1f, size * 8f, false)
        size20.drawString(text, x, y, 0xFFbcbcbc, false)
    }

    fun text34(text: String, x: Float, y: Float, size: Float, color: Long) {
        //fontRenderer.drawString(UMatrixStack.Compat.get(), text, color, x, y, 1f, size * 8f, false)
        size20.drawString(text, x, y, color, false)
    }




}