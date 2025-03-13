package io.github.patrickmeow.sealeo.utils

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

object SkyblockUtils {

    val ItemStack?.extraAttributes: NBTTagCompound?
        get() = this?.getSubCompound("ExtraAttributes", false)


    val ItemStack?.skyblockID: String
        get() = this?.extraAttributes?.getString("id") ?: ""







}