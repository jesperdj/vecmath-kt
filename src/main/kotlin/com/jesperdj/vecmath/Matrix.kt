package com.jesperdj.vecmath

data class MatrixDimension(val rows: Int, val cols: Int) {
    val size
        get() = rows * cols
}

interface MatrixElements {
    val dimension: MatrixDimension

    operator fun get(i: Int, j: Int): Double
    operator fun set(i: Int, j: Int, e: Double)
}

class ArrayMatrixElements private constructor(override val dimension: MatrixDimension, private val array: DoubleArray) : MatrixElements {
    constructor(dim: MatrixDimension) : this(dim, DoubleArray(dim.size))
    constructor(dim: MatrixDimension, init: (Int, Int) -> Double) : this(dim, DoubleArray(dim.size) { init(it / dim.cols, it % dim.cols) })

    override fun get(i: Int, j: Int) = array[i * dimension.cols + j]

    override fun set(i: Int, j: Int, e: Double) {
        array[i * dimension.cols + j] = e
    }
}

class Matrix(private val elements: MatrixElements) {
    constructor(dim: MatrixDimension) : this(ArrayMatrixElements(dim))
    constructor(rows: Int, cols: Int) : this(MatrixDimension(rows, cols))
    constructor(dim: MatrixDimension, init: (Int, Int) -> Double) : this(ArrayMatrixElements(dim, init))
    constructor(rows: Int, cols: Int, init: (Int, Int) -> Double) : this(MatrixDimension(rows, cols), init)

    fun copy() = Matrix(dimension) { i, j -> this[i, j] }

    val dimension
        get() = elements.dimension

    val rowIndices
        get() = 0 until dimension.rows

    val columnIndices
        get() = 0 until dimension.cols

    operator fun get(i: Int, j: Int) = elements[checkIndex(i, dimension.rows), checkIndex(j, dimension.cols)]

    operator fun set(i: Int, j: Int, e: Double) {
        elements[checkIndex(i, dimension.rows), checkIndex(j, dimension.cols)] = e
    }

    operator fun unaryPlus() = this
    operator fun unaryMinus() = Matrix(dimension) { i, j -> -this[i, j] }

    operator fun plus(d: Double) = Matrix(dimension) { i, j -> this[i, j] + d }
    operator fun minus(d: Double) = Matrix(dimension) { i, j -> this[i, j] - d }
    operator fun times(d: Double) = Matrix(dimension) { i, j -> this[i, j] * d }
    operator fun div(d: Double) = Matrix(dimension) { i, j -> this[i, j] / d }

    operator fun plusAssign(d: Double) {
        for (i in rowIndices) for (j in columnIndices) this[i, j] += d
    }

    operator fun minusAssign(d: Double) {
        for (i in rowIndices) for (j in columnIndices) this[i, j] -= d
    }

    operator fun timesAssign(d: Double) {
        for (i in rowIndices) for (j in columnIndices) this[i, j] *= d
    }

    operator fun divAssign(d: Double) {
        for (i in rowIndices) for (j in columnIndices) this[i, j] /= d
    }

    operator fun times(v: Vector): Vector {
        require(v.dimension == dimension.cols) { "Cannot multiply ${dimension.rows} x ${dimension.cols} matrix by vector of dimension ${v.dimension}" }
        return Vector(dimension.rows) { k ->
            columnIndices.fold(0.0) { acc, j -> acc + this[k, j] * v[j] }
        }
    }

    operator fun times(m: Matrix): Matrix {
        require(m.dimension.rows == dimension.cols) { "Cannot multiply ${dimension.rows} x ${dimension.cols} matrix by ${m.dimension.rows} x ${m.dimension.cols} matrix" }
        return Matrix(dimension.rows, m.dimension.cols) { i, j ->
            columnIndices.fold(0.0) { acc, k -> acc + this[i, k] * m[k, j] }
        }
    }

    fun transposeView() = Matrix(object : MatrixElements {
        override val dimension = MatrixDimension(this@Matrix.dimension.cols, this@Matrix.dimension.rows)

        override fun get(i: Int, j: Int) = this@Matrix[j, i]

        override fun set(i: Int, j: Int, e: Double) {
            this@Matrix[j, i] = e
        }
    })

    fun transposed() = transposeView().copy()

    override fun equals(other: Any?) = when {
        other === this -> true
        other !is Matrix -> false
        other.dimension != this.dimension -> false
        else -> rowIndices.all { i -> columnIndices.all { j -> other[i, j] == this[i, j] } }
    }

    override fun hashCode() =
            rowIndices.fold(0) { acc1, i -> acc1 + i * columnIndices.fold(0) { acc2, j -> acc2 + this[i, j].hashCode() } }

    override fun toString(): String {
        val sb = StringBuilder()
        for (i in rowIndices) {
            if (i > 0) sb.append('\n')
            sb.append('[')
            for (j in columnIndices) {
                if (j > 0) sb.append(' ')
                sb.append(this[i, j])
            }
            sb.append(']')
        }
        return sb.toString()
    }
}

operator fun Double.plus(m: Matrix) = m + this
operator fun Double.times(m: Matrix) = m * this
