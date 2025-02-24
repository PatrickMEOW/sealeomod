package io.github.patrickmeow.sealeo.uiOld

import io.github.patrickmeow.sealeo.features.Category
import io.github.patrickmeow.sealeo.uiOld.elements.impl.CategoriesElement
import io.github.patrickmeow.sealeo.uiOld.elements.impl.ModulesElement
import io.github.patrickmeow.sealeo.uiOld.elements.impl.SearchButton
import io.github.patrickmeow.sealeo.utils.RenderUtils
import io.github.patrickmeow.sealeo.utils.chat
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.ScaledResolution
import org.lwjgl.opengl.GL11
import java.awt.Color

object ClickGui : GuiScreen() {

    var x = 500f
    var y = 300f
    private var width: Float = 525f
    private var height: Float = 320f
    private var selectedCategory: Category? = null
    var sc: ScaledResolution? = null
    private val COLOR: Int = 0xFF444444.toInt()
    private var searchString: String = "Search.."
    private var rectColor: Color = Color(30, 31, 40)
    private var sideColor: Color = Color(26,27,35)


    private var scale: Float = 0.1f
    private var animationStartTime: Long = 0
    private val ANIMATION_DURATION: Long = 150

    // Elements
    private lateinit var categories: CategoriesElement
    private lateinit var modulesElement: ModulesElement


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
        categories = CategoriesElement()
        modulesElement = ModulesElement(categories)
        sc = ScaledResolution(mc)
        x = ((sc!!.scaledWidth - 525f) / 2)
        y = ((sc!!.scaledHeight - 320f) / 2)
        //x = sc!!.scaledWidth - (525f / 2)
        //y = sc!!.scaledHeight - (320f / 2)
        //chat("X $x")
        //chat("Y $y")

    }

    private fun updateAnimationState() {
        val elapsedTime = System.currentTimeMillis() - animationStartTime
        scale = 1.0f.coerceAtMost(elapsedTime / ANIMATION_DURATION.toFloat())
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawScreen(mouseX, mouseY, partialTicks)
        updateAnimationState()

        val x1 = x + 525f
        val y1 = y + 320f

        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS)
        GL11.glPushMatrix()
        val centerX = (x + x1) / 2.0f
        val centerY = (y + y1) / 2.0f
        GL11.glTranslatef(centerX, centerY, 0f)
        GL11.glScalef(scale, scale, 1.0f)
        GL11.glTranslatef(-centerX, -centerY, 0f)

        RenderUtils.roundedRectangle(x, y, width, height, rectColor, 8f)
        RenderUtils.roundedRectangle(x, y, 135f, height, sideColor, sideColor, sideColor, 0f, 8f, 0f, 8f, 0f, 0.1f)
        RenderUtils.drawText("Sealeo", x + 10, y + 3, 0xFF5A94DE, 2.5f)
        RenderUtils.drawText("by Patrick", x + 24, y + 22, -0xc0c879, 1.35f)



        categories.draw(mouseX, mouseY)
        modulesElement.draw(mouseX, mouseY)

        GL11.glPopAttrib()
        GL11.glPopMatrix()
    }




    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {

        categories.mouseClicked(mouseX, mouseY)
        modulesElement.mouseClicked(mouseX, mouseY)
        super.mouseClicked(mouseX, mouseY, mouseButton)
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        super.mouseReleased(mouseX, mouseY, state)
        modulesElement.onRelease(mouseX, mouseY)
    }


    override fun keyTyped(typedChar: Char, keyCode: Int) {
        super.keyTyped(typedChar, keyCode)
        modulesElement.keyTyped(typedChar, keyCode)
    }
}