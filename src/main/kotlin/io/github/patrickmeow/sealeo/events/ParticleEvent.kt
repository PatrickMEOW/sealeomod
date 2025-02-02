package io.github.patrickmeow.sealeo.events

import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.Vec3
import net.minecraftforge.fml.common.eventhandler.Event

class ParticleEvent(
    val type: EnumParticleTypes,
    val location: Vec3,
    val offset: Vec3,
    private val longDistance: Boolean,
    private val particleArgs: IntArray

    ) : Event() {




}