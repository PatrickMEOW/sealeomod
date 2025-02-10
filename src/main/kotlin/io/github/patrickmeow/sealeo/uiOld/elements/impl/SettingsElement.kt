package io.github.patrickmeow.sealeo.uiOld.elements.impl

import io.github.patrickmeow.sealeo.features.Module
import io.github.patrickmeow.sealeo.features.settings.impl.KeybindSetting
import io.github.patrickmeow.sealeo.uiOld.elements.Element

class SettingsElement(val parent: Module) : Element() {

    override fun draw(mouseX: Int, mouseY: Int) {

        //TODO draw rectangle with settings and animations

        super.draw(mouseX, mouseY)
        for(setting in parent.settings) {
            if(setting is KeybindSetting) return
        }
    }



}