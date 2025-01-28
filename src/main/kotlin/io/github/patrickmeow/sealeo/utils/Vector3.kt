package io.github.patrickmeow.sealeo.utils

data class Vector3(var x: Float, var y: Float, var z: Float) {

    fun add(vector: Vector3): Vector3 {
        x += vector.x
        y += vector.y
        z += vector.z
        return this
    }
}