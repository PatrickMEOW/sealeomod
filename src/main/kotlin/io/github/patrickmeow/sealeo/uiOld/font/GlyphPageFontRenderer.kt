package io.github.patrickmeow.sealeo.uiOld.font

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.opengl.GL11.*
import java.awt.Font
import java.io.File
import java.util.*

class GlyphPageFontRenderer(
    private val ttfFilePath: String
) {
    private val fontCache = HashMap<Int, GlyphPage>()

    private var posX: Float = 0f
    private var posY: Float = 0f
    private val colorCode = IntArray(32)
    private var red: Float = 0f
    private var blue: Float = 0f
    private var green: Float = 0f
    private var alpha: Float = 0f
    private var randomStyle: Boolean = false
    private var boldStyle: Boolean = false
    private var italicStyle: Boolean = false
    private var underlineStyle: Boolean = false
    private var strikethroughStyle: Boolean = false

    init {
        for (i in 0 until 32) {
            var j = (i shr 3 and 1) * 85
            var k = (i shr 2 and 1) * 170 + j
            var l = (i shr 1 and 1) * 170 + j
            var i1 = (i and 1) * 170 + j

            if (i == 6) {
                k += 85
            }

            if (i >= 16) {
                k /= 4
                l /= 4
                i1 /= 4
            }

            colorCode[i] = (k and 255 shl 16) or (l and 255 shl 8) or (i1 and 255)
        }
    }

    private fun getOrCreateGlyphPage(size: Int): GlyphPage {
        return fontCache.getOrPut(size) {
            val font = Font.createFont(Font.TRUETYPE_FONT, File(ttfFilePath)).deriveFont(size.toFloat())
            GlyphPage(font, true, true).apply {
                generateGlyphPage(CharArray(256) { it.toChar() })
                setupTexture()
            }
        }
    }

    fun drawString(text: String?, x: Float, y: Float, color: Long, dropShadow: Boolean, fontSize: Int): Int {
        GlStateManager.enableAlpha()
        resetStyles()
        var i = 0

        val glyphPage = getOrCreateGlyphPage(fontSize)

        if (dropShadow) {
            i = renderString(text, x + 1f, y + 1f, color, true, glyphPage)
            i = maxOf(i, renderString(text, x, y, color, false, glyphPage))
        } else {
            i = renderString(text, x, y, color, false, glyphPage)
        }

        return i
    }

    private fun renderString(text: String?, x: Float, y: Float, color: Long, dropShadow: Boolean, glyphPage: GlyphPage): Int {
        if (text == null) {
            return 0
        } else {
            var color = color
            if ((color and -67108864).toInt() == 0) {
                color = color or -16777216
            }

            if (dropShadow) {
                color = (color and 16579836 shr 2) or (color and -16777216)
            }

            red = (color shr 16 and 255) / 255f
            blue = (color shr 8 and 255) / 255f
            green = (color and 255) / 255f
            alpha = (color shr 24 and 255) / 255f

            GlStateManager.color(red, blue, green, alpha)
            posX = x * 2f
            posY = y * 2f
            renderStringAtPos(text, dropShadow, glyphPage)
            return (posX / 4f).toInt()
        }
    }

    private fun renderStringAtPos(text: String, shadow: Boolean, glyphPage: GlyphPage) {
        glPushMatrix()
        glScaled(0.5, 0.5, 0.5)

        GlStateManager.enableBlend()
        GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        GlStateManager.enableTexture2D()

        glyphPage.bindTexture()

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        var i = 0
        while (i < text.length) {
            val c0 = text[i]

            if (c0 == '§' && i + 1 < text.length) {
                var i1 = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH)[i + 1])

                when {
                    i1 < 16 -> {
                        randomStyle = false
                        boldStyle = false
                        strikethroughStyle = false
                        underlineStyle = false
                        italicStyle = false
                        if (shadow) {
                            i1 += 16
                        }

                        val j1 = colorCode[i1]
                        GlStateManager.color(
                            (j1 shr 16 and 255) / 255f,
                            (j1 shr 8 and 255) / 255f,
                            (j1 and 255) / 255f,
                            alpha
                        )
                    }

                    i1 == 16 -> randomStyle = true
                    i1 == 17 -> boldStyle = true
                    i1 == 18 -> strikethroughStyle = true
                    i1 == 19 -> underlineStyle = true
                    i1 == 20 -> italicStyle = true
                    else -> {
                        randomStyle = false
                        boldStyle = false
                        strikethroughStyle = false
                        underlineStyle = false
                        italicStyle = false
                        GlStateManager.color(red, blue, green, alpha)
                    }
                }
                i++  // Skip the next character as it's part of the color code
            } else {
                val f = glyphPage.drawChar(c0, posX, posY)

                doDraw(f, glyphPage)
            }

            i++  // Proceed to the next character
        }

        glyphPage.unbindTexture()
        glPopMatrix()
    }

    private fun doDraw(f: Float, glyphPage: GlyphPage) {
        if (strikethroughStyle) {
            val tessellator = Tessellator.getInstance()
            val worldrenderer = tessellator.worldRenderer
            GlStateManager.disableTexture2D()

            worldrenderer.begin(7, DefaultVertexFormats.POSITION)
            worldrenderer.pos(posX.toDouble(), (posY + glyphPage.getMaxFontHeight() / 2).toDouble(), 0.0).endVertex()
            worldrenderer.pos((posX + f).toDouble(), (posY + glyphPage.getMaxFontHeight() / 2).toDouble(), 0.0).endVertex()
            worldrenderer.pos((posX + f).toDouble(), (posY + glyphPage.getMaxFontHeight() / 2 - 1f).toDouble(), 0.0).endVertex()
            worldrenderer.pos(posX.toDouble(), (posY + glyphPage.getMaxFontHeight() / 2 - 1f).toDouble(), 0.0).endVertex()
            tessellator.draw()
            GlStateManager.enableTexture2D()
        }

        if (underlineStyle) {
            val tessellator1 = Tessellator.getInstance()
            val worldrenderer1 = tessellator1.worldRenderer
            GlStateManager.disableTexture2D()

            worldrenderer1.begin(7, DefaultVertexFormats.POSITION)
            val l = if (underlineStyle) -1 else 0
            worldrenderer1.pos((posX + l).toDouble(), (posY + glyphPage.getMaxFontHeight()).toDouble(), 0.0).endVertex()
            worldrenderer1.pos((posX + f).toDouble(), (posY + glyphPage.getMaxFontHeight()).toDouble(), 0.0).endVertex()
            worldrenderer1.pos((posX + f).toDouble(), (posY + glyphPage.getMaxFontHeight() - 1f).toDouble(), 0.0).endVertex()
            worldrenderer1.pos((posX + l).toDouble(), (posY + glyphPage.getMaxFontHeight() - 1f).toDouble(), 0.0).endVertex()
            tessellator1.draw()
            GlStateManager.enableTexture2D()
        }

        posX += f
    }

    private fun resetStyles() {
        randomStyle = false
        boldStyle = false
        italicStyle = false
        underlineStyle = false
        strikethroughStyle = false
    }
}
