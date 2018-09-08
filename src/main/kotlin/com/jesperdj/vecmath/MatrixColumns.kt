package com.jesperdj.vecmath

class MatrixColumnsView(val matrix: Matrix) : Iterable<Vector> {
    val size
        get() = matrix.dimension.cols

    val indices
        get() = 0 until size

    operator fun get(j: Int): Vector {
        checkIndex(j, size)
        return Vector(object : VectorElements {
            override val dimension: Int
                get() = matrix.dimension.rows

            override fun get(k: Int) = matrix[k, j]

            override fun set(k: Int, e: Double) {
                matrix[k, j] = e
            }
        })
    }

    operator fun set(j: Int, v: Vector) {
        for (k in matrix.rowIndices) matrix[k, j] = v[k]
    }

    override fun iterator(): Iterator<Vector> = object : Iterator<Vector> {
        private var j = 0

        override fun hasNext() = j < size
        override fun next() = this@MatrixColumnsView[j++]
    }
}

fun Matrix.columnsView() = MatrixColumnsView(this)

fun Matrix.columns(): Array<Vector> {
    val view = columnsView()
    return Array(view.size) { view[it].copy() }
}

fun matrixOfColumnsView(vararg columns: Vector) = Matrix(object : MatrixElements {
    override val dimension = MatrixDimension(if (columns.isNotEmpty()) columns[0].dimension else 0, columns.size)

    override fun get(i: Int, j: Int) = columns[j][i]

    override fun set(i: Int, j: Int, e: Double) {
        columns[j][i] = e
    }
})

fun matrixOfColumns(vararg columns: Vector) = matrixOfColumnsView(*columns).copy()
