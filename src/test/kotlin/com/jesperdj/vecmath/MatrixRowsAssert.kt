package com.jesperdj.vecmath

import org.assertj.core.api.ClassBasedNavigableIterableAssert

class MatrixRowsAssert(actual: MatrixRowsView?) :
        ClassBasedNavigableIterableAssert<MatrixRowsAssert, MatrixRowsView, Vector, VectorAssert>(actual,
                MatrixRowsAssert::class.java, VectorAssert::class.java) {

    override fun hasSize(size: Int): MatrixRowsAssert {
        isNotNull()
        if (actual.size != size) {
            failWithMessage("Expected rows view of size <$size> but was <${actual.size}>")
        }
        return this
    }

    fun hasIndices(indices: IntRange): MatrixRowsAssert {
        isNotNull()
        if (actual.indices != indices) {
            failWithMessage("Expected rows view with indices <$indices> but was <${actual.indices}>")
        }
        return this
    }
}

fun assertThat(rowsView: MatrixRowsView) = MatrixRowsAssert(rowsView)
