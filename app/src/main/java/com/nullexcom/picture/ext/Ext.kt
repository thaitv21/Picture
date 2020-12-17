package com.nullexcom.picture.ext

import kotlin.math.sqrt

typealias Vector = Array<Float>
typealias Matrix = Array<Vector>

fun Matrix.copyFrom(matrix: Matrix) {
    for (i in 0 until size) {
        val vector = this[i]
        for (j in vector.indices) {
            this[i][j] = matrix[i][j]
        }
    }
}

fun Vector.multiply(vector: Vector): Float {
    return mapIndexed { index, item -> item * vector[index] }.reduce { acc, right -> acc + right }
}

fun Matrix.row(i: Int): Vector {
    val vector = Array(size) { 0f }
    repeat(size) {
        vector[it] = this[i][it]
    }
    return vector
}

fun Matrix.col(i: Int): Vector {
    val vector = Array(size) { 0f }
    repeat(size) {
        vector[it] = this[it][i]
    }
    return vector
}

fun Matrix.multiply(matrix: Matrix): Matrix {
    val result: Matrix = emptyMatrix(size)
    for (i in 0 until size) {
        for (j in 0 until size) {
            val r = this.row(i)
            val c = matrix.col(j)
            result[i][j] = r.multiply(c)
        }
    }
    return result
}

fun emptyMatrix(size: Int): Matrix {
    return Array(size) { Array(size) { 0f } }
}

fun matrix(vararg vectors: Vector): Matrix {
    val arr = Array(vectors.size) { Array(vectors.size) { 0f } }
    vectors.forEachIndexed { i, vector ->
        vector.forEachIndexed { j, item ->
            arr[i][j] = item
        }
    }
    return arr
}

fun matrixOf(vararg values: Float): Matrix {
    val size = sqrt(values.size.toFloat()).toInt()
    val matrix = emptyMatrix(size)
    for (i in 0 until size) {
        for (j in 0 until size) {
            val index = size * i + j
            matrix[i][j] = values[index]
        }
    }
    return matrix
}

fun matrixOf(values: List<Float>): Matrix {
    val size = sqrt(values.size.toFloat()).toInt()
    val matrix = emptyMatrix(size)
    for (i in 0 until size) {
        for (j in 0 until size) {
            val index = size * i + j
            matrix[i][j] = values[index]
        }
    }
    return matrix
}

fun vector(vararg items: Float): Vector {
    return items.toTypedArray()
}

fun Matrix.stringify() : String {
    return this.joinToString(separator = ", ") { it.joinToString(separator = ", ") }
}