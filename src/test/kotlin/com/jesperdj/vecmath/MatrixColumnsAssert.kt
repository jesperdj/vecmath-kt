package com.jesperdj.vecmath

import org.assertj.core.api.ClassBasedNavigableIterableAssert

class MatrixColumnsAssert(actual: MatrixColumnsView?) :
        ClassBasedNavigableIterableAssert<MatrixColumnsAssert, MatrixColumnsView, Vector, VectorAssert>(actual,
                MatrixColumnsAssert::class.java, VectorAssert::class.java) {

    override fun hasSize(size: Int): MatrixColumnsAssert {
        isNotNull()
        if (actual.size != size) {
            failWithMessage("Expected columns view of size <$size> but was <${actual.size}>")
        }
        return this
    }

    fun hasIndices(indices: IntRange): MatrixColumnsAssert {
        isNotNull()
        if (actual.indices != indices) {
            failWithMessage("Expected columns view with indices <$indices> but was <${actual.indices}>")
        }
        return this
    }
}

fun assertThat(columnsView: MatrixColumnsView) = MatrixColumnsAssert(columnsView)
