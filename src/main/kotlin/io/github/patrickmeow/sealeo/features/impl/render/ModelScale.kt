package io.github.patrickmeow.sealeo.features.impl.render

import io.github.patrickmeow.sealeo.Sealeo.mc
import io.github.patrickmeow.sealeo.features.Category
import io.github.patrickmeow.sealeo.features.Module
import io.github.patrickmeow.sealeo.features.settings.impl.BooleanSetting
import io.github.patrickmeow.sealeo.features.settings.impl.NumberSetting
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.renderer.GlStateManager

object ModelScale : Module(
  "Model scale",
    "Scales players' models",
    Category.RENDER
) {

    private var scaleX by NumberSetting("X Scale", "X", 1f, 0.1f, 5f, 0.1f)
    private var scaleY by NumberSetting("Y Scale", "Y", 1f, 0.1f, 5f, 0.1f)
    private var scaleZ by NumberSetting("Z Scale", "Z", 1f, 0.1f, 5f, 0.1f)
    private var onlyYour by BooleanSetting("Scale only your model", "Only scales your model")

    fun preRenderCallbackScaleHook(entity: AbstractClientPlayer) {
        if(mc.thePlayer.name != entity.name && onlyYour) return
        GlStateManager.scale(scaleX.toFloat() * 0.9375f, scaleY.toFloat() * 0.9375f, scaleZ.toFloat() * 0.9375f)
    }
    
}