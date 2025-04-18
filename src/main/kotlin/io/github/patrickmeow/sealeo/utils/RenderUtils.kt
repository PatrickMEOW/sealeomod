package io.github.patrickmeow.sealeo.utils

import gg.essential.universal.UMatrixStack
import gg.essential.universal.shader.UShader
import io.github.patrickmeow.sealeo.Sealeo.mc
import io.github.patrickmeow.sealeo.SealeoMod
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.GlStateManager.*
import net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.WorldRenderer
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.Entity
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.*
import org.lwjgl.util.glu.Cylinder
import org.lwjgl.util.vector.Vector2f
import java.awt.Color

object RenderUtils {


    private val tessellator: Tessellator = Tessellator.getInstance()
    private val worldRenderer: WorldRenderer = tessellator.worldRenderer
    private val renderManager: RenderManager = mc.renderManager
    private val matrix = UMatrixStack.Compat


    fun measureTextWidth(text: String, scale: Float): Float {
        val fontRenderer = Minecraft.getMinecraft().fontRendererObj
        return fontRenderer.getStringWidth(text) * scale
    }

    fun roundedRectangle(
        x: Number, y: Number, w: Number, h: Number,
        color: Color, borderColor: Color, shadowColor: Color,
        borderThickness: Number, topL: Number, topR: Number, botL: Number, botR: Number, edgeSoftness: Number,
        color2: Color = color, gradientDir: Int = 0, shadowSoftness: Float = 0f
    ) {
        matrix.runLegacyMethod(matrix.get()) {
            RoundedRect.drawRectangle(
                matrix.get(),
                x.toFloat(),
                y.toFloat(),
                w.toFloat(),
                h.toFloat(),
                color,
                borderColor,
                shadowColor,
                borderThickness.toFloat(),
                topL.toFloat(),
                topR.toFloat(),
                botL.toFloat(),
                botR.toFloat(),
                edgeSoftness.toFloat(),
                color2,
                gradientDir,
                shadowSoftness
            )
        }
    }

    fun roundedRectangle(x: Number, y: Number, w: Number, h: Number, color: Color, radius: Number = 0f, edgeSoftness: Number = 0f) =
        roundedRectangle(x.toFloat(), y.toFloat(), w.toFloat(), h.toFloat(), color, color, color,
            0f, radius.toFloat(), radius.toFloat(), radius.toFloat(), radius.toFloat(), edgeSoftness)

    fun rectangleOutline(x: Number, y: Number, w: Number, h: Number, color: Color, radius: Number = 0f, thickness: Number, edgeSoftness: Number = 0.1f) {
        roundedRectangle(x, y, w, h, Color(0, 0, 0, 0), color, Color(0,0,0,0), thickness, radius, radius, radius, radius, edgeSoftness)
    }

    /**
     *
     * @param aabb AxisAlignedBoundingBox of the block/entity
     * @param color Vector3 with RGB values
     * @param alpha Alpha of the color
     * @param depth Enable/Disable depth
     * @param width Width of the outline
     */
    fun renderOutlineAABB(aabb: AxisAlignedBB?, color: Vector3, alpha: Float, depth: Boolean, width: Float) {
        if (aabb == null) return

        aabb.expand(0.0020000000949949026, 0.0020000000949949026, 0.0020000000949949026)
        pushMatrix()
        resetColor()
        enableAlpha()
        enableBlend()
        disableLighting()
        disableTexture2D()
        glEnable(GL_LINE_SMOOTH)
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST)
        glLineWidth(width)
        tryBlendFuncSeparate(770, 771, 1, 0)
        if (!depth) {
            disableDepth()
        } else {
            enableDepth()
        }
        depthMask(depth)

        color(color.x, color.y, color.z, alpha)

        val minX = aabb.minX - mc.renderManager.viewerPosX
        val minY = aabb.minY - mc.renderManager.viewerPosY
        val minZ = aabb.minZ - mc.renderManager.viewerPosZ
        val maxX = aabb.maxX - mc.renderManager.viewerPosX
        val maxY = aabb.maxY - mc.renderManager.viewerPosY
        val maxZ = aabb.maxZ - mc.renderManager.viewerPosZ

        worldRenderer.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION)

        worldRenderer.pos(minX, minY, minZ).endVertex()
        worldRenderer.pos(minX, minY, maxZ).endVertex()
        worldRenderer.pos(maxX, minY, maxZ).endVertex()
        worldRenderer.pos(maxX, minY, minZ).endVertex()
        worldRenderer.pos(minX, minY, minZ).endVertex()

        worldRenderer.pos(minX, maxY, minZ).endVertex()
        worldRenderer.pos(minX, maxY, maxZ).endVertex()
        worldRenderer.pos(maxX, maxY, maxZ).endVertex()
        worldRenderer.pos(maxX, maxY, minZ).endVertex()
        worldRenderer.pos(minX, maxY, minZ).endVertex()

        worldRenderer.pos(minX, maxY, maxZ).endVertex()
        worldRenderer.pos(minX, minY, maxZ).endVertex()
        worldRenderer.pos(maxX, minY, maxZ).endVertex()
        worldRenderer.pos(maxX, maxY, maxZ).endVertex()
        worldRenderer.pos(maxX, maxY, minZ).endVertex()
        worldRenderer.pos(maxX, minY, minZ).endVertex()

        tessellator.draw()
        glDisable(GL_LINE_SMOOTH)
        if (!depth) {
            enableDepth()
            depthMask(true)
        } else {
            disableDepth()
        }

        disableBlend()
        enableTexture2D()
        resetColor()
        popMatrix()
    }


    fun renderFilledAABB(aabb: AxisAlignedBB?, color: Vector3, alpha: Float, depth: Boolean) {
        if (aabb == null) return
        //aabb.expand(0.0020000000949949026, 0.0020000000949949026, 0.0020000000949949026)
        color(color.x, color.y, color.z, alpha)

        pushMatrix()

        disableTexture2D()
        if (!depth) {
            disableDepth()
        }

        disableCull()

        enableBlend()
        disableLighting()
        enableAlpha()

        tryBlendFuncSeparate(770, 771, 1, 0)

        val tessellator = Tessellator.getInstance()
        val worldRenderer = tessellator.worldRenderer

        worldRenderer.begin(7, DefaultVertexFormats.POSITION)

        val minX = aabb.minX - mc.renderManager.viewerPosX
        val minY = aabb.minY - mc.renderManager.viewerPosY
        val minZ = aabb.minZ - mc.renderManager.viewerPosZ
        val maxX = aabb.maxX - mc.renderManager.viewerPosX
        val maxY = aabb.maxY - mc.renderManager.viewerPosY
        val maxZ = aabb.maxZ - mc.renderManager.viewerPosZ

        // Front face
        worldRenderer.pos(minX, minY, minZ).endVertex()
        worldRenderer.pos(maxX, minY, minZ).endVertex()
        worldRenderer.pos(maxX, maxY, minZ).endVertex()
        worldRenderer.pos(minX, maxY, minZ).endVertex()

        // Back face
        worldRenderer.pos(minX, minY, maxZ).endVertex()
        worldRenderer.pos(minX, maxY, maxZ).endVertex()
        worldRenderer.pos(maxX, maxY, maxZ).endVertex()
        worldRenderer.pos(maxX, minY, maxZ).endVertex()

        // Bottom face
        worldRenderer.pos(minX, minY, minZ).endVertex()
        worldRenderer.pos(maxX, minY, minZ).endVertex()
        worldRenderer.pos(maxX, minY, maxZ).endVertex()
        worldRenderer.pos(minX, minY, maxZ).endVertex()

        // Top face
        worldRenderer.pos(minX, maxY, maxZ).endVertex()
        worldRenderer.pos(maxX, maxY, maxZ).endVertex()
        worldRenderer.pos(maxX, maxY, minZ).endVertex()
        worldRenderer.pos(minX, maxY, minZ).endVertex()

        // Left face
        worldRenderer.pos(minX, minY, maxZ).endVertex()
        worldRenderer.pos(minX, maxY, maxZ).endVertex()
        worldRenderer.pos(minX, maxY, minZ).endVertex()
        worldRenderer.pos(minX, minY, minZ).endVertex()

        // Right face
        worldRenderer.pos(maxX, minY, minZ).endVertex()
        worldRenderer.pos(maxX, maxY, minZ).endVertex()
        worldRenderer.pos(maxX, maxY, maxZ).endVertex()
        worldRenderer.pos(maxX, minY, maxZ).endVertex()

        tessellator.draw()

        // Restore depth and other states
        if (!depth) {
            enableDepth()
        }

        disableBlend()
        enableTexture2D()
        resetColor()
        enableCull()
        popMatrix()
    }



    fun renderLineBetweenBlockPos(start: BlockPos, end: BlockPos, color: Vector3, alpha: Float) {

        val tessellator = Tessellator.getInstance()
        val worldRenderer = tessellator.worldRenderer

        color(color.x, color.y, color.z, alpha)
        disableTexture2D()
        disableCull()
        enableBlend()
        disableLighting()
        enableAlpha()
        tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0)

        worldRenderer.begin(GL_LINES, DefaultVertexFormats.POSITION)

        val startX = start.x.toDouble() - mc.renderManager.viewerPosX
        val startY = start.y.toDouble() - mc.renderManager.viewerPosY
        val startZ = start.z.toDouble() - mc.renderManager.viewerPosZ

        val endX = end.x.toDouble() - mc.renderManager.viewerPosX
        val endY = end.y.toDouble() + 0.1 - mc.renderManager.viewerPosY
        val endZ = end.z.toDouble() - mc.renderManager.viewerPosZ

        worldRenderer.pos(startX, startY, startZ).endVertex()
        worldRenderer.pos(endX, endY, endZ).endVertex()

        tessellator.draw()

        enableTexture2D()
        enableCull()
        disableBlend()
        enableLighting()
    }

    fun trace(blockPos: BlockPos, color: Vector3, alpha: Float, lineWidth: Float = 2.5f) {
        val player = mc.thePlayer

        val x = blockPos.x.toDouble() - mc.renderManager.viewerPosX
        val y = blockPos.y.toDouble() - mc.renderManager.viewerPosY
        val z = blockPos.z.toDouble() - mc.renderManager.viewerPosZ

        if (player.isSneaking) {
            drawLineBetter(player.posX - mc.renderManager.viewerPosX, player.posY + 1.54 - mc.renderManager.viewerPosY, player.posZ - mc.renderManager.viewerPosZ, x, y, z, color.x, color.y, color.z, alpha, lineWidth)
        } else {
            drawLineBetter(player.posX - mc.renderManager.viewerPosX, player.posY + 1.62 - mc.renderManager.viewerPosY, player.posZ - mc.renderManager.viewerPosZ, x, y, z, color.x, color.y, color.z, alpha, lineWidth)
        }

    }

    private fun drawLineBetter(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double, red: Float, green: Float, blue: Float, alpha: Float, lineWidth: Float = 1f) {
        val tessellator = Tessellator.getInstance()
        val worldRenderer = tessellator.worldRenderer

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        glEnable(GL_BLEND)
        glLineWidth(lineWidth)
        glDisable(GL_TEXTURE_2D)
        glDisable(GL_DEPTH_TEST)
        glDepthMask(false)
        pushMatrix()

        worldRenderer.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR)
        worldRenderer.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex()
        worldRenderer.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex()
        tessellator.draw()

        popMatrix()
        glEnable(GL_TEXTURE_2D)
        glEnable(GL_DEPTH_TEST)
        glDepthMask(true)
        glDisable(GL_BLEND)
    }


    private fun preDraw(disableTexture2D: Boolean = true) {
        enableAlpha()
        enableBlend()
        disableLighting()
        if (disableTexture2D) disableTexture2D() else enableTexture2D()
        tryBlendFuncSeparate(770, 771, 1, 0)
        translate(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ)
    }

    fun depth(depth: Boolean) {
        if (depth) enableDepth() else disableDepth()
        depthMask(depth)
    }

    private fun postDraw() {
        disableBlend()
        enableTexture2D()
        resetColor()
    }

    inline operator fun WorldRenderer.invoke(block: WorldRenderer.() -> Unit) {
        block.invoke(this)
    }

    private fun resetDepth() {
        enableDepth()
        depthMask(true)
    }

    fun drawLines(points: Collection<Vec3>, color: Vector3, lineWidth: Float, depth: Boolean) {
        if (points.size < 2) return

        pushMatrix()
        color(color.x, color.y, color.z)
        preDraw()
        depth(depth)
        glEnable(GL11.GL_LINE_SMOOTH)
        GL11.glLineWidth(lineWidth)

        worldRenderer {
            begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION)
            for (point in points) {
                pos(point.xCoord, point.yCoord, point.zCoord).endVertex()
            }
        }
        tessellator.draw()

        if (!depth) resetDepth()
        glDisable(GL_LINE_SMOOTH)
        glLineWidth(1f)
        postDraw()
        popMatrix()
    }

    var partialTicks = 0f

    val Entity.renderX: Double
        get() = prevPosX + (posX - prevPosX ) * partialTicks

    /**
     * Gets the rendered y-coordinate of an entity based on its last tick and current tick positions.
     *
     * @receiver The entity for which to retrieve the rendered y-coordinate.
     * @return The rendered y-coordinate.
     */
    val Entity.renderY: Double
        get() = prevPosY + (posY - prevPosY) * partialTicks

    /**
     * Gets the rendered z-coordinate of an entity based on its last tick and current tick positions.
     *
     * @receiver The entity for which to retrieve the rendered z-coordinate.
     * @return The rendered z-coordinate.
     */
    val Entity.renderZ: Double
        get() = prevPosZ + (posZ - prevPosZ) * partialTicks

    /**
     * Gets the rendered position of an entity as a `Vec3`.
     *
     * @receiver The entity for which to retrieve the rendered position.
     * @return The rendered position as a `Vec3`.
     */
    val Entity.renderVec: Vec3
        get() = Vec3(renderX, renderY, renderZ)

    fun Vec3.addVec(x: Number = .0, y: Number = .0, z: Number = .0): Vec3 =
        this.addVector(x.toDouble(), y.toDouble(), z.toDouble())

    fun fastEyeHeight(): Float =
        if (mc.thePlayer?.isSneaking == true) 1.54f else 1.62f

    fun drawTracer(goal: Vec3, color: Vector3, lineWidth: Float = 3f, depth: Boolean = false) {
        drawLines(listOf(mc.thePlayer.renderVec.addVec(y = fastEyeHeight()), goal), color, lineWidth, depth)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onRenderWorld(event: RenderWorldLastEvent) {
        this.partialTicks = event.partialTicks
    }

    private fun drawLine(x1: Int, y1: Int, x2: Int, y2: Int) {
        val vec = Vector2f((x2 - x1).toFloat(), (y2 - y1).toFloat())
        vec.normalise()
        val side = Vector2f(vec.y, -vec.x)

        glLineWidth(1f)
        glEnable(GL_LINE_SMOOTH)

        val tessellator = Tessellator.getInstance()
        val worldRenderer = tessellator.worldRenderer

        val lines = 6
        for (i in 0..lines) {
            worldRenderer.begin(GL_LINES, DefaultVertexFormats.POSITION)
            worldRenderer.pos((x1 - side.x + side.x * i / lines).toDouble(),
                (y1 - side.y + side.y * i / lines).toDouble(), 0.0).endVertex()
            worldRenderer.pos((x2 - side.x + side.x * i / lines).toDouble(),
                (y2 - side.y + side.y * i / lines).toDouble(), 0.0).endVertex()
            tessellator.draw()
        }
    }

    fun drawLinkArrow(x1: Int, y1: Int, x2: Int, y2: Int) {
        color(0.2f, 0.8f, 0.2f, 1f)
        disableLighting()
        disableStandardItemLighting()
        disableTexture2D()
        enableBlend()
        tryBlendFuncSeparate(770, 771, 1, 0)

        translate(0f, 0f, 500f)
        drawLine(x1, y1, x2, y2)
        translate(0f, 0f, -500f)
        enableTexture2D()
    }



    fun drawCyl(pos: Vec3, radiusBase: Float, radiusTop: Float, height: Float, slices: Int, stacks: Int,
                rot1: Float, rot2: Float, rot3: Float) {
        disableDepth()
        pushMatrix()
        disableCull()
        glLineWidth(2.0f)
        enableAlpha()
        enableBlend()
        disableLighting()
        disableTexture2D()
        tryBlendFuncSeparate(770, 771, 1, 0)
        translate(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ)
        color(0.36f, 0.87f, 0.14f, 1.0f)
        translate(pos.xCoord, pos.yCoord, pos.zCoord)
        rotate(-90f, 1f, 0f, 0f)
        rotate(rot2, 0f, 0f, 1f)
        rotate(rot3, 0f, 1f, 0f)
        val cylin = Cylinder()
        cylin.draw(radiusBase, radiusTop, height, slices, stacks)

        disableBlend()
        enableTexture2D()
        resetColor()
        depthMask(true)
        glLineWidth(1f)
        enableCull()
        enableDepth()
        popMatrix()
    }


    fun createLegacyShader(vertName: String, fragName: String, blendState: gg.essential.universal.shader.BlendState) =
        UShader.fromLegacyShader(readShader(vertName, "vsh"), readShader(fragName, "fsh"), blendState)

    private fun readShader(name: String, ext: String): String =
        SealeoMod::class.java.getResource("/shaders/source/$name.$ext")?.readText() ?: ""


    fun drawText(text: String, x: Float, y: Float, color: Long, scaleText: Float) {
        SealeoFont.text(text, x, y, color, false, (scaleText * 18).toInt())
    }

    fun drawShadowText(text: String, x: Float, y: Float, color: Long, scaleText: Float) {
        SealeoFont.shadowText(text, x, y, color, true, (scaleText * 18).toInt())
    }

    fun drawHSBBox(x: Float, y: Float, w: Float, h: Float, color: HSBColor) {
        matrix.runLegacyMethod(matrix.get()) {
            RoundedRect.drawHSBBox(
                matrix.get(),
                x,
                y,
                w,
                h,
                color,
            )
        }
        rectangleOutline(x-1, y-1, w+2, h+2, Color(38, 38, 38), 3f, 2f)
    }

    private fun HSBColor.bind() {
        resetColor()
        color(r / 255f, g / 255f, b / 255f, a / 255f)
    }

    fun drawTexturedModalRect(
        x: Int, y: Int, width: Int, height: Int,
        u: Float = 0f, v: Float = 0f, uWidth: Int = 1, vHeight: Int = 1,
        tileWidth: Float = 1.0f, tileHeight: Float = 1.0f
    ) {
        val f = 1.0f / tileWidth
        val g = 1.0f / tileHeight
        HSBColor.WHITE.bind()
        worldRenderer {
            begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)
            pos(x.toDouble(), (y + height).toDouble(), 0.0).tex((u * f).toDouble(), ((v + vHeight.toFloat()) * g).toDouble()).endVertex()
            pos((x + width).toDouble(), (y + height).toDouble(), 0.0).tex(((u + uWidth.toFloat()) * f).toDouble(), ((v + vHeight.toFloat()) * g).toDouble()).endVertex()
            pos((x + width).toDouble(), y.toDouble(), 0.0).tex(((u + uWidth.toFloat()) * f).toDouble(), (v * g).toDouble()).endVertex()
            pos(x.toDouble(), y.toDouble(), 0.0).tex((u * f).toDouble(), (v * g).toDouble()).endVertex()
        }
        tessellator.draw()
    }


    fun drawDynamicTexture(dynamicTexture: DynamicTexture, x: Number, y: Number, w: Number, h: Number) {
        dynamicTexture.updateDynamicTexture()
        bindTexture(dynamicTexture.glTextureId)
        drawTexturedModalRect(x.toInt(), y.toInt(), w.toInt(), h.toInt())
    }

    fun gradientRect(x: Float, y: Float, w: Float, h: Float, color1: HSBColor, color2: HSBColor, radius: Float, direction: GradientDirection = GradientDirection.Right, borderColor: Color = Color(0f, 0f, 0f, 0f), borderThickness: Number = 0f) {
        roundedRectangle(
            x, y, w, h, color1.coerceAlpha(.1f, 1f).javaColor, borderColor, Color(0f, 0f, 0f, 0f), borderThickness, radius, radius, radius, radius, 0.1f, color2.coerceAlpha(.1f, 1f).javaColor, direction.ordinal
        )
    }

    fun HSBColor.coerceAlpha(min: Float, max: Float): HSBColor {
        return if (this.alpha < min) this.withAlpha(min)
        else if (this.alpha > max) this.withAlpha(max)
        else this
    }


    fun HSBColor.withAlpha(alpha: Float, newInstance: Boolean = true): HSBColor {
        if (!newInstance) {
            this.alpha = alpha
            return this
        }
        return HSBColor(r, g, b, alpha)
    }
    enum class GradientDirection {
        Right, Down, Left, Up
    }


    fun circle(x: Number, y: Number, radius: Number, color: Color, borderColor: Color = color, borderThickness: Number = 0f) {
        matrix.runLegacyMethod(matrix.get()) {
            RoundedRect.drawCircle(
                matrix.get(),
                x.toFloat(),
                y.toFloat(),
                radius.toFloat(),
                color,
                borderColor,
                borderThickness.toFloat()
            )
        }
    }
}


