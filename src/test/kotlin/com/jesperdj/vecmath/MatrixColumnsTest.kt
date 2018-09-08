package com.jesperdj.vecmath

import org.assertj.core.api.Assertions.*
import org.junit.Test

class MatrixColumnsTest {

    @Test
    fun constructWithMatrix() {
        val m = Matrix(MatrixDimension(2, 3)) { i, j -> (i * 3 + j + 1).toDouble() }
        val view = m.columnsView()

        assertThat(view)
                .hasSize(3)
                .hasIndices(0..2)
                .containsExactly(Vector(1.0, 4.0), Vector(2.0, 5.0), Vector(3.0, 6.0))
    }

    @Test
    fun getChecksIndexBounds() {
        val view = Matrix(2, 3).columnsView()

        assertThatThrownBy { view[-1] }
                .isInstanceOf(IndexOutOfBoundsException::class.java)
                .hasMessage("Index out of range: -1")

        assertThatThrownBy { view[3] }
                .isInstanceOf(IndexOutOfBoundsException::class.java)
                .hasMessage("Index out of range: 3")
    }

    @Test
    fun setChecksIndexBounds() {
        val view = Matrix(2, 3).columnsView()

        assertThatThrownBy { view[-1] = Vector(0.0, 0.0, 0.0) }
                .isInstanceOf(IndexOutOfBoundsException::class.java)
                .hasMessage("Index out of range: -1")

        assertThatThrownBy { view[3] = Vector(0.0, 0.0, 0.0) }
                .isInstanceOf(IndexOutOfBoundsException::class.java)
                .hasMessage("Index out of range: 3")
    }

    @Test
    fun iterator() {
        val m = Matrix(MatrixDimension(2, 3)) { i, j -> (i * 3 + j + 1).toDouble() }
        val view = m.columnsView()

        var count = 0
        for (e in view) {
            when (count++) {
                0 -> assertThat(e).isEqualTo(Vector(1.0, 4.0))
                1 -> assertThat(e).isEqualTo(Vector(2.0, 5.0))
                2 -> assertThat(e).isEqualTo(Vector(3.0, 6.0))
                else -> fail("Unexpected element: $e")
            }
        }
        assertThat(count).isEqualTo(3)

        val iter = view.iterator()
        assertThat(iter.hasNext()).isTrue()
        assertThat(iter.next()).isEqualTo(Vector(1.0, 4.0))
        assertThat(iter.hasNext()).isTrue()
        assertThat(iter.next()).isEqualTo(Vector(2.0, 5.0))
        assertThat(iter.hasNext()).isTrue()
        assertThat(iter.next()).isEqualTo(Vector(3.0, 6.0))
        assertThat(iter.hasNext()).isFalse()
        assertThatThrownBy { iter.next() }
                .isInstanceOf(IndexOutOfBoundsException::class.java)
                .hasMessage("Index out of range: 3")
    }

    @Test
    fun columnsView() {
        val m = Matrix(MatrixDimension(2, 3)) { i, j -> (i * 3 + j + 1).toDouble() }
        val view = m.columnsView()

        m *= -1.5

        assertThat(view).describedAs("Modifying the matrix should be visible in the columns view")
                .containsExactly(Vector(-1.5, -6.0), Vector(-3.0, -7.5), Vector(-4.5, -9.0))

        view[2][1] = 5.0

        assertThat(m).columns().describedAs("Modifying the columns view should be visible in the matrix")
                .containsExactly(Vector(-1.5, -6.0), Vector(-3.0, -7.5), Vector(-4.5, 5.0))
    }

    @Test
    fun columns() {
        val m = Matrix(MatrixDimension(2, 3)) { i, j -> (i * 3 + j + 1).toDouble() }
        val columns = m.columns()

        m *= -1.5

        assertThat(columns).describedAs("Modifying the matrix should be not visible in the columns copy")
                .containsExactly(Vector(1.0, 4.0), Vector(2.0, 5.0), Vector(3.0, 6.0))
    }

    @Test
    fun matrixOfColumnsView() {
        val v1 = Vector(1.0, 2.0, 3.0)
        val v2 = Vector(4.0, 5.0, 6.0)
        val m = matrixOfColumnsView(v1, v2)

        assertThat(m)
                .hasDimension(3, 2)
                .rows().containsExactly(Vector(1.0, 4.0), Vector(2.0, 5.0), Vector(3.0, 6.0))

        v1 += 0.5
        v2 *= -1.5

        assertThat(m).rows().describedAs("Modyfing the columns view should be visible in the matrix")
                .containsExactly(Vector(1.5, -6.0), Vector(2.5, -7.5), Vector(3.5, -9.0))

        m[1, 1] = 8.0

        assertThat(v2).describedAs("Modifying the matrix should be visible in the columns view")
                .hasElements(-6.0, 8.0, -9.0)
    }

    @Test
    fun matrixOfColumns() {
        val v1 = Vector(1.0, 2.0, 3.0)
        val v2 = Vector(4.0, 5.0, 6.0)
        val m = matrixOfColumns(v1, v2)

        assertThat(m)
                .hasDimension(3, 2)
                .rows().containsExactly(Vector(1.0, 4.0), Vector(2.0, 5.0), Vector(3.0, 6.0))

        v1 += 0.5
        v2 *= -1.5

        assertThat(m).rows().describedAs("Modyfing the columns copy should not be visible in the matrix")
                .containsExactly(Vector(1.0, 4.0), Vector(2.0, 5.0), Vector(3.0, 6.0))

        m[1, 1] = 8.0

        assertThat(v2).describedAs("Modifying the matrix should not be visible in the columns copy")
                .hasElements(-6.0, -7.5, -9.0)
    }
}
