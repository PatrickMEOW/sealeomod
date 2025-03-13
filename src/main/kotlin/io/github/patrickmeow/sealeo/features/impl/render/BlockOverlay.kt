package io.github.patrickmeow.sealeo.features.impl.render

import io.github.patrickmeow.sealeo.Sealeo.mc
import io.github.patrickmeow.sealeo.features.Category
import io.github.patrickmeow.sealeo.features.Module
import io.github.patrickmeow.sealeo.features.impl.skyblock.RiftHelper
import io.github.patrickmeow.sealeo.features.settings.impl.ColorSetting
import io.github.patrickmeow.sealeo.features.settings.impl.ListSetting
import io.github.patrickmeow.sealeo.features.settings.impl.NumberSetting
import io.github.patrickmeow.sealeo.utils.HSBColor
import io.github.patrickmeow.sealeo.utils.RenderUtils
import io.github.patrickmeow.sealeo.utils.Vector3
import net.minecraft.util.MovingObjectPosition
import net.minecraft.util.MovingObjectPosition.MovingObjectType
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object BlockOverlay : Module("Block overlay", "Renders custom block overlay", Category.RENDER) {


    var renderType by ListSetting("Render type", "Choose render type", arrayListOf("Outline", "Overlay", "Both"), "Both")
    var outlineWidth by NumberSetting("Outline width", "Width of block outline", 1.5f, 0.2f, 5f, 0.1f)
    private var col by ColorSetting("Overlay color", "Color of the block overlay", HSBColor(0.28f, 0.84f, 0.87f, 0.5f))
    private var outlineColor by ColorSetting("Outline color", "Color of the block outline", HSBColor(0.28f, 0.84f, 0.87f, 0.8f))



    @SubscribeEvent
    fun onRender(event: RenderWorldLastEvent) {
        if(mc.thePlayer == null) return
        val mouseOver = mc.objectMouseOver
        if(mouseOver != null && mouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            val blockPos = mouseOver.blockPos
            val block = mc.theWorld.getBlockState(blockPos).block
            var aabb = block.getSelectedBoundingBox(mc.theWorld, blockPos)

            if(renderType == 0) {
                RenderUtils.renderOutlineAABB(aabb, Vector3(outlineColor.r.toFloat() / 255, outlineColor.g.toFloat() / 255, outlineColor.b.toFloat() / 255), outlineColor.alpha, false, outlineWidth.toFloat())
            }
            if(renderType == 1) {
                RenderUtils.renderFilledAABB(aabb, Vector3(col.r.toFloat() / 255, col.g.toFloat() / 255, col.b.toFloat() / 255), col.alpha, false)
            }
            if(renderType == 2) {
                RenderUtils.renderOutlineAABB(aabb, Vector3(outlineColor.r.toFloat() / 255, outlineColor.g.toFloat() / 255, outlineColor.b.toFloat() / 255), outlineColor.alpha, false,
                    outlineWidth.toFloat())
                RenderUtils.renderFilledAABB(aabb, Vector3(col.r.toFloat() / 255, col.g.toFloat() / 255, col.b.toFloat() / 255), col.alpha, false)
            }

        }
    }


}