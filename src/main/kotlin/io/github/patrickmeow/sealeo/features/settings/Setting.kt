package io.github.patrickmeow.sealeo.features.settings

import io.github.patrickmeow.sealeo.features.Module
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class Setting<T>(val name: String, val description: String = "") : ReadWriteProperty<Module, T>, PropertyDelegateProvider<Module, ReadWriteProperty<Module, T>> {

    abstract var value: T

    override operator fun provideDelegate(thisRef: Module, property: KProperty<*>) : ReadWriteProperty<Module, T> {
        return thisRef.register(this)
    }

    override operator fun getValue(thisRef: Module, property: KProperty<*>): T {
        return value
    }

    override operator fun setValue(thisRef: Module, property: KProperty<*>, value: T) {
        this.value = value
    }
}
