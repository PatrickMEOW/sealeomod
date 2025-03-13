package io.github.patrickmeow.sealeo.features.settings.impl

import io.github.patrickmeow.sealeo.features.settings.Setting


class ListSetting(
    name: String,
    description: String,
    var list: ArrayList<String>,
    default: String

) :Setting<Int>(name, description){

    override var value: Int
        get() = index
        set(value) {
            index = value
        }

    var index: Int = getIndex(default)


    var selected: String
        get() = list[index]
        set(value) {
            index = getIndex(value)
        }


    fun getIndex(option: String): Int {
        return list.map { it.lowercase() }.indexOf(option.lowercase()).coerceIn(0, list.size - 1)
   }

}