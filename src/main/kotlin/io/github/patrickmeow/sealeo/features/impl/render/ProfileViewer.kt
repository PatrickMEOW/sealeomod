package io.github.patrickmeow.sealeo.features.impl.render

import io.github.patrickmeow.sealeo.utils.RenderUtils
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.ScaledResolution
import org.lwjgl.opengl.GL11
import java.awt.Color

object ProfileViewer : GuiScreen() {

    private var scale: Float = 0.1f
    private var animationStartTime: Long = 0
    private val ANIMATION_DURATION: Long = 150
    private var x = 500f
    private var y = 300f
    private var width = 525f
    private var height = 320f
    private var rectColor: Color = Color(30, 31, 40)
    private var sc: ScaledResolution? = null



    private fun onGuiOpened() {
        animationStartTime = System.currentTimeMillis()
        sc = ScaledResolution(mc)
        x = ((sc!!.scaledWidth - width) / 2).toFloat()
        y = ((sc!!.scaledHeight - height) / 2).toFloat()
    }

    override fun initGui() {
        super.initGui()
        onGuiOpened()
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawScreen(mouseX, mouseY, partialTicks)
        updateAnimationState()
        println("drawing")
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
        GL11.glPopAttrib()
        GL11.glPopMatrix()
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClicked(mouseX, mouseY, mouseButton)
    }

    fun updateAnimationState() {
        val elapsedTime = System.currentTimeMillis() - animationStartTime
        scale = 1.0f.coerceAtMost(elapsedTime / ANIMATION_DURATION.toFloat())
    }
}
