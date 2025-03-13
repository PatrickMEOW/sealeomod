package io.github.patrickmeow.sealeo.features.impl.render

import io.github.patrickmeow.sealeo.features.Category
import io.github.patrickmeow.sealeo.features.Module
import io.github.patrickmeow.sealeo.features.settings.impl.ColorSetting
import io.github.patrickmeow.sealeo.utils.HSBColor
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object HitColor : Module(
    "Hit color",
    "Sets custom hit color",
    Category.RENDER
) {

    var hitColor by ColorSetting("Hit color", "Set hit color", HSBColor(0.2f, 0.5f, 0.3f, 0.8f))
    var colorRed = hitColor.redFloat
    var colorGreen = hitColor.greenFloat
    var colorBlue = hitColor.blueFloat
    var alpha = hitColor.alpha


}