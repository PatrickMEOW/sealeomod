package io.github.patrickmeow.sealeo.uiOld.font

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.texture.DynamicTexture
import org.lwjgl.opengl.GL11.*
import java.awt.*
import java.awt.font.FontRenderContext
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.util.*

class GlyphPage(
    private val font: Font,
    private val antiAliasing: Boolean,
    private val fractionalMetrics: Boolean
) {
    private var imgSize: Int = 0
    var maxFontHeightGet = -1
    private val glyphCharacterMap = HashMap<Char, Glyph>()
    private var bufferedImage: BufferedImage? = null
    private var loadedTexture: DynamicTexture? = null

    fun generateGlyphPage(chars: CharArray) {
        // Calculate glyphPageSize
        var maxWidth = -1.0
        var maxHeight = -1.0

        val affineTransform = AffineTransform()
        val fontRenderContext = FontRenderContext(affineTransform, antiAliasing, fractionalMetrics)

        for (ch in chars) {
            val bounds = font.getStringBounds(ch.toString(), fontRenderContext)

            if (maxWidth < bounds.width) maxWidth = bounds.width
            if (maxHeight < bounds.height) maxHeight = bounds.height
        }

        // Leave some additional space
        maxWidth += 2
        maxHeight += 2

        imgSize = Math.ceil(Math.max(
            Math.ceil(Math.sqrt(maxWidth * maxWidth * chars.size) / maxWidth),
            Math.ceil(Math.sqrt(maxHeight * maxHeight * chars.size) / maxHeight)
        ) * Math.max(maxWidth, maxHeight)).toInt() + 1

        bufferedImage = BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_ARGB)
        val g = bufferedImage!!.graphics as Graphics2D

        g.font = font
        // Set Color to Transparent
        g.color = Color(255, 255, 255, 0)
        // Set the image background to transparent
        g.fillRect(0, 0, imgSize, imgSize)

        g.color = Color.white

        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, if (fractionalMetrics) RenderingHints.VALUE_FRACTIONALMETRICS_ON else RenderingHints.VALUE_FRACTIONALMETRICS_OFF)
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, if (antiAliasing) RenderingHints.VALUE_ANTIALIAS_OFF else RenderingHints.VALUE_ANTIALIAS_ON)
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, if (antiAliasing) RenderingHints.VALUE_TEXT_ANTIALIAS_ON else RenderingHints.VALUE_TEXT_ANTIALIAS_OFF)

        val fontMetrics = g.fontMetrics
        var currentCharHeight = 0
        var posX = 0
        var posY = 1

        for (ch in chars) {
            val glyph = Glyph()

            val bounds = fontMetrics.getStringBounds(ch.toString(), g)

            glyph.width = bounds.bounds.width + 8 // Leave some additional space
            glyph.height = bounds.bounds.height

            if (posY + glyph.height >= imgSize) {
                // Example: Cloud.INSTANCE.messageHelper.showMessage("Font", "Selected Font might not work as expected!")
            }

            if (posX + glyph.width >= imgSize) {
                posX = 0
                posY += currentCharHeight
                currentCharHeight = 0
            }

            glyph.x = posX
            glyph.y = posY

            if (glyph.height > maxFontHeightGet) maxFontHeightGet = glyph.height
            if (glyph.height > currentCharHeight) currentCharHeight = glyph.height

            g.drawString(ch.toString(), (posX + 2).toFloat(), (posY + fontMetrics.ascent).toFloat())

            posX += glyph.width

            glyphCharacterMap[ch] = glyph
        }
    }

    fun setupTexture() {
        loadedTexture = DynamicTexture(bufferedImage)
    }

    fun bindTexture() {
        GlStateManager.bindTexture(loadedTexture!!.glTextureId)
    }

    fun unbindTexture() {
        GlStateManager.bindTexture(0)
    }

    fun drawChar(ch: Char, x: Float, y: Float): Float {
        val glyph = glyphCharacterMap[ch]
            ?: throw IllegalArgumentException("'$ch' wasn't found")

        val pageX = glyph.x / imgSize.toFloat()
        val pageY = glyph.y / imgSize.toFloat()

        val pageWidth = glyph.width / imgSize.toFloat()
        val pageHeight = glyph.height / imgSize.toFloat()

        val width = glyph.width.toFloat()
        val height = glyph.height.toFloat()

        glBegin(GL_TRIANGLES)

        glTexCoord2f(pageX + pageWidth, pageY)
        glVertex2f(x + width, y)

        glTexCoord2f(pageX, pageY)
        glVertex2f(x, y)

        glTexCoord2f(pageX, pageY + pageHeight)
        glVertex2f(x, y + height)

        glTexCoord2f(pageX, pageY + pageHeight)
        glVertex2f(x, y + height)

        glTexCoord2f(pageX + pageWidth, pageY + pageHeight)
        glVertex2f(x + width, y + height)

        glTexCoord2f(pageX + pageWidth, pageY)
        glVertex2f(x + width, y)

        glEnd()

        return width - 8
    }

    fun getWidth(ch: Char): Float {
        return glyphCharacterMap[ch]?.width?.toFloat() ?: 0f
    }

    fun getMaxFontHeight(): Int {
        return maxFontHeightGet
    }

    fun isAntiAliasingEnabled(): Boolean {
        return antiAliasing
    }

    fun isFractionalMetricsEnabled(): Boolean {
        return fractionalMetrics
    }

    data class Glyph(
        var x: Int = 0,
        var y: Int = 0,
        var width: Int = 0,
        var height: Int = 0
    )
}
