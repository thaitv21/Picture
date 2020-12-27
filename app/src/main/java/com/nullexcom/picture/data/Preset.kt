package com.nullexcom.editor.data

data class Preset(
        val matrix: IntArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Preset

        if (!matrix.contentEquals(other.matrix)) return false

        return true
    }

    override fun hashCode(): Int {
        return matrix.contentHashCode()
    }
}