package io.github.patrickmeow.sealeo.ui

import io.github.patrickmeow.sealeo.features.Category
import io.github.patrickmeow.sealeo.utils.RenderUtils
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.ScaledResolution
import org.lwjgl.opengl.GL11
import java.awt.Color

object ClickGui : GuiScreen() {

    private var x = 0f
    private var y = 0f
    private var width: Float = 525f
    private var height: Float = 320f
    private var selectedCategory: Category? = null
    private var sc: ScaledResolution? = null
    private val COLOR: Int = 0xFF444444.toInt()
    private var searchString: String = "Search.."
    private var rectColor: Color = Color(33,35,44)
    private var scale: Float = 0.1f
    private var animationStartTime: Long = 0
    private val ANIMATION_DURATION: Long = 150

    init {
        selectedCategory = Category.DUNGEONS
    }

    override fun initGui() {
        super.initGui()
        onGuiOpened()

    }

    private fun onGuiOpened() {
        animationStartTime = System.currentTimeMillis()
        searchString = "Search.."
        sc = ScaledResolution(mc)
        x = ((sc!!.scaledWidth - width) / 2).toFloat()
        y = ((sc!!.scaledHeight - height) / 2).toFloat()
    }

    private fun updateAnimationState() {
        val elapsedTime = System.currentTimeMillis() - animationStartTime
        scale = Math.min(1.0f, elapsedTime / ANIMATION_DURATION.toFloat())
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawScreen(mouseX, mouseY, partialTicks)
        updateAnimationState()

        val x1 = x + width
        val y1 = y + height


        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS)
        GL11.glPushMatrix()
        val centerX = (x + x1) / 2.0f
        val centerY = (y + y1) / 2.0f
        GL11.glTranslatef(centerX, centerY, 0f)
        GL11.glScalef(scale, scale, 1.0f)
        GL11.glTranslatef(-centerX, -centerY, 0f)


        RenderUtils.roundedRectangle(x, y, width, height, rectColor, 8f)
        RenderUtils.drawText("Sealeo", x + 10, y + 10, 0xFF5A94DE.toInt(), 2.5f)
        RenderUtils.drawText("by Patrick", x + 24, y + 29, -0xc0c879, 1.35f)
        GL11.glPopAttrib()
        GL11.glPopMatrix()
    }




}