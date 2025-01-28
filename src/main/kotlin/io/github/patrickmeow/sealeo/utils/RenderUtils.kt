package io.github.patrickmeow.sealeo.utils

import gg.essential.universal.UMatrixStack
import gg.essential.universal.shader.UShader
import io.github.patrickmeow.sealeo.Sealeo.mc
import io.github.patrickmeow.sealeo.SealeoMod
import net.minecraft.client.renderer.GlStateManager.*
import net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.WorldRenderer
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3
import org.lwjgl.opengl.GL11.*
import org.lwjgl.util.glu.Cylinder
import org.lwjgl.util.vector.Vector2f
import java.awt.Color

object RenderUtils {


    private val tessellator: Tessellator = Tessellator.getInstance()
    private val worldRenderer: WorldRenderer = tessellator.worldRenderer
    private val renderManager: RenderManager = mc.renderManager
    val matrix = UMatrixStack.Compat


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

    fun roundedRectangle(x: Number, y: Number, w: Number, h: Number, color: Color, radius: Number = 0f, edgeSoftness: Number = 2f) =
        roundedRectangle(x.toFloat(), y.toFloat(), w.toFloat(), h.toFloat(), color, color, color,
            0f, radius.toFloat(), radius.toFloat(), radius.toFloat(), radius.toFloat(), edgeSoftness)


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


    fun drawText(text: String, x: Float, y: Float, color: Int, scaleText: Float) {
        glPushMatrix()
        glScalef(scaleText, scaleText, scaleText)

        val adjustedX = (x / scaleText).toInt()
        val adjustedY = (y / scaleText).toInt()

       mc.fontRendererObj.drawStringWithShadow(text, adjustedX.toFloat(), adjustedY.toFloat(), color)

        glPopMatrix()
        color(1.0F, 1.0F, 1.0F, 1.0F) // Reset color state
    }

}


