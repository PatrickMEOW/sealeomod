package io.github.patrickmeow.sealeo.features.impl.render

import io.github.patrickmeow.sealeo.Sealeo.mc
import io.github.patrickmeow.sealeo.features.Category
import io.github.patrickmeow.sealeo.features.Module
import io.github.patrickmeow.sealeo.features.settings.impl.HiddenModule
import io.github.patrickmeow.sealeo.features.settings.impl.KeybindSetting
import io.github.patrickmeow.sealeo.uiOld.ClickGui
import org.lwjgl.input.Keyboard

@HiddenModule
object ClickGuiModule : Module(
  "Click Gui",
    "Click gui",
    Category.RENDER
) {

    private var clickGui: ClickGui? = null
    val openGui by KeybindSetting("open gui", "opens gui", Keyboard.KEY_X).onPress {
        onEnable()
    }

    override fun onEnable() {
        if(clickGui == null) {
            clickGui = ClickGui
        }
        mc.displayGuiScreen(clickGui)
    }
}