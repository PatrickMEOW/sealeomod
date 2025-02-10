package io.github.patrickmeow.sealeo.uiOld.font

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.opengl.GL11.*
import java.awt.Font
import java.util.*

class GlyphPageFontRenderer(
    private val regularGlyphPage: GlyphPage,
    private val boldGlyphPage: GlyphPage,
    private val italicGlyphPage: GlyphPage,
    private val boldItalicGlyphPage: GlyphPage
) {
    private val fontRandom = Random()

    private var posX: Float = 0f
    private var posY: Float = 0f
    private val colorCode = IntArray(32)
    private var red: Float = 0f
    private var blue: Float = 0f
    private var green: Float = 0f
    private var alpha: Float = 0f
    private var textColor: Int = 0

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

    companion object {
        fun create(
            fontName: String,
            size: Int,
            bold: Boolean,
            italic: Boolean,
            boldItalic: Boolean
        ): GlyphPageFontRenderer {
            val chars = CharArray(256) { it.toChar() }
            val regularPage = GlyphPage(Font(fontName, Font.PLAIN, size), true, true).apply {
                generateGlyphPage(chars)
                setupTexture()
            }

            var boldPage = regularPage
            var italicPage = regularPage
            var boldItalicPage = regularPage

            if (bold) {
                boldPage = GlyphPage(Font(fontName, Font.BOLD, size), true, true).apply {
                    generateGlyphPage(chars)
                    setupTexture()
                }
            }

            if (italic) {
                italicPage = GlyphPage(Font(fontName, Font.ITALIC, size), true, true).apply {
                    generateGlyphPage(chars)
                    setupTexture()
                }
            }

            if (boldItalic) {
                boldItalicPage = GlyphPage(Font(fontName, Font.BOLD or Font.ITALIC, size), true, true).apply {
                    generateGlyphPage(chars)
                    setupTexture()
                }
            }

            return GlyphPageFontRenderer(regularPage, boldPage, italicPage, boldItalicPage)
        }
    }

    fun drawString(text: String?, x: Float, y: Float, color: Long, dropShadow: Boolean): Int {
        GlStateManager.enableAlpha()
        resetStyles()
        var i = 0

        if (dropShadow) {
            i = renderString(text, x + 1f, y + 1f, color, true)
            i = maxOf(i, renderString(text, x, y, color, false))
        } else {
            i = renderString(text, x, y, color, false)
        }

        return i
    }

    private fun renderString(text: String?, x: Float, y: Float, color: Long, dropShadow: Boolean): Int {
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
            renderStringAtPos(text, dropShadow)
            return (posX / 4f).toInt()
        }
    }

    private fun renderStringAtPos(text: String, shadow: Boolean) {
        var glyphPage = getCurrentGlyphPage()

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
                        textColor = j1
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
                glyphPage = getCurrentGlyphPage()
                glyphPage.bindTexture()

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
            worldrenderer.pos(posX.toDouble(), (posY + glyphPage.maxFontHeightGet / 2).toDouble(), 0.0).endVertex()
            worldrenderer.pos((posX + f).toDouble(), (posY + glyphPage.maxFontHeightGet / 2).toDouble(), 0.0).endVertex()
            worldrenderer.pos((posX + f).toDouble(), (posY + glyphPage.maxFontHeightGet / 2 - 1f).toDouble(), 0.0)
                .endVertex()
            worldrenderer.pos(posX.toDouble(), (posY + glyphPage.maxFontHeightGet / 2 - 1f).toDouble(), 0.0).endVertex()
            tessellator.draw()
            GlStateManager.enableTexture2D()
        }

        if (underlineStyle) {
            val tessellator1 = Tessellator.getInstance()
            val worldrenderer1 = tessellator1.worldRenderer
            GlStateManager.disableTexture2D()

            worldrenderer1.begin(7, DefaultVertexFormats.POSITION)
            val l = if (underlineStyle) -1 else 0
            worldrenderer1.pos((posX + l).toDouble(), (posY + glyphPage.maxFontHeightGet).toDouble(), 0.0).endVertex()
            worldrenderer1.pos((posX + f).toDouble(), (posY + glyphPage.maxFontHeightGet).toDouble(), 0.0).endVertex()
            worldrenderer1.pos((posX + f).toDouble(), (posY + glyphPage.maxFontHeightGet - 1f).toDouble(), 0.0).endVertex()
            worldrenderer1.pos((posX + l).toDouble(), (posY + glyphPage.maxFontHeightGet - 1f).toDouble(), 0.0).endVertex()
            tessellator1.draw()
            GlStateManager.enableTexture2D()
        }

        posX += f
    }

    private fun getCurrentGlyphPage(): GlyphPage {
        return when {
            boldStyle && italicStyle -> boldItalicGlyphPage
            boldStyle -> boldGlyphPage
            italicStyle -> italicGlyphPage
            else -> regularGlyphPage
        }
    }

    private fun resetStyles() {
        randomStyle = false
        boldStyle = false
        italicStyle = false
        underlineStyle = false
    }

    fun getFontHeightMax(): Int {
        return regularGlyphPage.getMaxFontHeight() / 2
    }

    fun getStringWidth(text: String?): Int {
        if (text == null) {
            return 0
        }
        var width = 0
        var currentPage: GlyphPage
        val size = text.length
        var on = false

        var i = 0
        while (i < size) {
            var character = text[i]
            if (character == '§') on = true
            else if (on && character >= '0' && character <= 'r') {
                val colorIndex = "0123456789abcdefklmnor".indexOf(character)
                if (colorIndex < 16) {
                    boldStyle = false
                    italicStyle = false
                } else if (colorIndex == 17) {
                    boldStyle = true
                } else if (colorIndex == 20) {
                    italicStyle = true
                } else if (colorIndex == 21) {
                    boldStyle = false
                    italicStyle = false
                }
                i++
                on = false
            } else {
                if (on) i--
                character = text[i]
                currentPage = getCurrentGlyphPage()
                width = (width + (currentPage.getWidth(character) - 8)).toInt()
            }
            i++
        }

        return width / 2
    }

    /**
     * Trims a string to fit a specified Width.
     */
    fun trimStringToWidth(text: String, width: Int): String {
        return this.trimStringToWidth(text, width, false)
    }

    /**
     * Trims a string to a specified width, and will reverse it if par3 is set.
     */
    fun trimStringToWidth(text: String, maxWidth: Int, reverse: Boolean): String {
        val stringbuilder = StringBuilder()
        var on = false
        val j = if (reverse) text.length - 1 else 0
        val k = if (reverse) -1 else 1
        var width = 0
        var currentPage: GlyphPage

        var i = j
        while (i >= 0 && i < text.length && i < maxWidth) {
            var character = text[i]
            if (character == '§') on = true
            else if (on && character >= '0' && character <= 'r') {
                val colorIndex = "0123456789abcdefklmnor".indexOf(character)
                if (colorIndex < 16) {
                    boldStyle = false
                    italicStyle = false
                } else if (colorIndex == 17) {
                    boldStyle = true
                } else if (colorIndex == 20) {
                    italicStyle = true
                } else if (colorIndex == 21) {
                    boldStyle = false
                    italicStyle = false
                }
                i++
                on = false
            } else {
                if (on) i--
                character = text[i]
                currentPage = getCurrentGlyphPage()
                width = (width + (currentPage.getWidth(character) - 8) / 2).toInt()
            }

            if (i > width) {
                break
            }

            if (reverse) {
                stringbuilder.insert(0, character)
            } else {
                stringbuilder.append(character)
            }
            i += k
        }

        return stringbuilder.toString()
    }
}