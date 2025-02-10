package io.github.patrickmeow.sealeo.features.impl.render

import io.github.patrickmeow.sealeo.features.Category
import io.github.patrickmeow.sealeo.features.Module
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object HitColor : Module(
    "Hit color",
    "Sets custom hit color",
    Category.RENDER
) {

    var colorRed = 0.43f
    var colorGreen = 0.629f
    var colorBlue = 0.854f
    var alpha = 0.76f


}