package io.github.patrickmeow.sealeo

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import io.github.patrickmeow.sealeo.Sealeo.mc
import io.github.patrickmeow.sealeo.features.ModuleManager.modules
import io.github.patrickmeow.sealeo.features.settings.Setting
import java.io.File

object Config {

    private val gson = GsonBuilder().setPrettyPrinting().create()
    private val configFile = File(mc.mcDataDir, "config/sealeo/sealeo-config.json")


    fun saveConfig() {
        if(!configFile.exists()) {
            val fileDIr = configFile.parentFile
            fileDIr.mkdirs()
        }
        val settingsMap = modules.associate { module ->
            module.name to mapOf(
                "enabled" to module.enabled,
                "settings" to module.settings.associate { setting ->
                    setting.name to setting.value
                }
            )
        }


        val jsonString = gson.toJson(settingsMap)


        configFile.writeText(jsonString)
    }

    fun loadConfig() {

    }

}