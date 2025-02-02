package io.github.patrickmeow.sealeo.features

enum class Category {
    SKYBLOCK, DUNGEONS, RENDER, TERMINALS, MOVEMENT;

    fun getFormattedName(): String {
        val name = this.name
        return name.substring(0, 1).uppercase() + name.substring(1).lowercase()
    }
}