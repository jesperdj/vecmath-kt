package com.jesperdj.vecmath

import kotlin.math.sqrt

interface VectorElements {
    val dimension: Int

    operator fun get(k: Int): Double
    operator fun set(k: Int, e: Double)
}

class ArrayVectorElements private constructor(private val array: DoubleArray) : VectorElements {
    constructor(dim: Int) : this(DoubleArray(dim))
    constructor(dim: Int, init: (Int) -> Double) : this(DoubleArray(dim, init))

    override val dimension
        get() = array.size

    override fun get(k: Int) = array[k]

    override fun set(k: Int, e: Double) {
        array[k] = e
    }
}

class Vector(private val elements: VectorElements) {
    constructor(dim: Int) : this(ArrayVectorElements(dim))
    constructor(dim: Int, init: (Int) -> Double) : this(ArrayVectorElements(dim, init))
    constructor(vararg elements: Double) : this(elements.size, { elements[it] })

    fun copy() = Vector(dimension) { this[it] }

    val dimension
        get() = elements.dimension

    val indices
        get() = 0 until dimension

    operator fun get(k: Int) = elements[checkIndex(k, dimension)]

    operator fun set(k: Int, e: Double) {
        elements[checkIndex(k, dimension)] = e
    }

    operator fun iterator() = object : DoubleIterator() {
        private var k = 0

        override fun hasNext() = k < dimension
        override fun nextDouble() = this@Vector[k++]
    }

    operator fun unaryPlus() = this
    operator fun unaryMinus() = Vector(dimension) { -this[it] }

    operator fun plus(d: Double) = Vector(dimension) { this[it] + d }
    operator fun minus(d: Double) = Vector(dimension) { this[it] - d }
    operator fun times(d: Double) = Vector(dimension) { this[it] * d }
    operator fun div(d: Double) = Vector(dimension) { this[it] / d }

    operator fun plusAssign(d: Double) {
        for (k in indices) this[k] += d
    }

    operator fun minusAssign(d: Double) {
        for (k in indices) this[k] -= d
    }

    operator fun timesAssign(d: Double) {
        for (k in indices) this[k] *= d
    }

    operator fun divAssign(d: Double) {
        for (k in indices) this[k] /= d
    }

    operator fun plus(v: Vector) = Vector(dimension) { this[it] + v[it] }
    operator fun minus(v: Vector) = Vector(dimension) { this[it] - v[it] }

    operator fun plusAssign(v: Vector) {
        for (k in indices) this[k] += v[k]
    }

    operator fun minusAssign(v: Vector) {
        for (k in indices) this[k] -= v[k]
    }

    operator fun times(m: Matrix) = Vector(m.dimension.cols) { k ->
        indices.fold(0.0) { acc, i -> acc + this[i] * m[i, k] }
    }

    infix fun dot(v: Vector) = indices.fold(0.0) { acc, k -> acc + this[k] * v[k] }

    val magnitude
        get() = sqrt(dot(this))

    fun normalized() = this / magnitude

    override fun equals(other: Any?) = when {
        other === this -> true
        other !is Vector -> false
        other.dimension != this.dimension -> false
        else -> indices.all { other[it] == this[it] }
    }

    override fun hashCode() = indices.fold(0) { acc, k -> acc + this[k].hashCode() }

    override fun toString(): String {
        val sb = StringBuilder().append('[')
        for (k in indices) {
            if (k > 0) sb.append(' ')
            sb.append(this[k])
        }
        return sb.append(']').toString()
    }
}

operator fun Double.plus(v: Vector) = v + this
operator fun Double.times(v: Vector) = v * this
