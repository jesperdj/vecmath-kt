package com.jesperdj.vecmath

import org.assertj.core.api.AbstractAssert

class MatrixAssert(actual: Matrix?) : AbstractAssert<MatrixAssert, Matrix>(actual, MatrixAssert::class.java) {

    fun hasDimension(rows: Int, cols: Int): MatrixAssert {
        isNotNull()
        if (actual.dimension.rows != rows || actual.dimension.cols != cols) {
            failWithMessage("Expected matrix of dimension <$rows x $cols> but was <${actual.dimension.rows} x ${actual.dimension.cols}>")
        }
        return this
    }

    fun hasRowIndices(indices: IntRange): MatrixAssert {
        isNotNull()
        if (actual.rowIndices != indices) {
            failWithMessage("Expected matrix with row indices <$indices> but was <${actual.rowIndices}>")
        }
        return this
    }

    fun hasColumnIndices(indices: IntRange): MatrixAssert {
        isNotNull()
        if (actual.columnIndices != indices) {
            failWithMessage("Expected matrix with column indices <$indices> but was <${actual.columnIndices}>")
        }
        return this
    }

    fun rows() = assertThat(actual.rowsView())

    fun columns() = assertThat(actual.columnsView())
}

fun assertThat(m: Matrix) = MatrixAssert(m)
