package com.jesperdj.vecmath

class MatrixRowsView(val matrix: Matrix) : Iterable<Vector> {
    val size
        get() = matrix.dimension.rows

    val indices
        get() = 0 until size

    operator fun get(i: Int): Vector {
        checkIndex(i, size)
        return Vector(object : VectorElements {
            override val dimension: Int
                get() = matrix.dimension.cols

            override fun get(k: Int) = matrix[i, k]

            override fun set(k: Int, e: Double) {
                matrix[i, k] = e
            }
        })
    }

    operator fun set(i: Int, v: Vector) {
        for (k in matrix.columnIndices) matrix[i, k] = v[k]
    }

    override fun iterator(): Iterator<Vector> = object : Iterator<Vector> {
        private var i = 0

        override fun hasNext() = i < size
        override fun next() = this@MatrixRowsView[i++]
    }
}

fun Matrix.rowsView() = MatrixRowsView(this)

fun Matrix.rows(): Array<Vector> {
    val view = rowsView()
    return Array(view.size) { view[it].copy() }
}

fun matrixOfRowsView(vararg rows: Vector) = Matrix(object : MatrixElements {
    override val dimension = MatrixDimension(rows.size, if (rows.isNotEmpty()) rows[0].dimension else 0)

    override fun get(i: Int, j: Int) = rows[i][j]

    override fun set(i: Int, j: Int, e: Double) {
        rows[i][j] = e
    }
})

fun matrixOfRows(vararg rows: Vector) = matrixOfRowsView(*rows).copy()
