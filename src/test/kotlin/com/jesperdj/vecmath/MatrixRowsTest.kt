package com.jesperdj.vecmath

import org.assertj.core.api.Assertions.*
import org.junit.Test

class MatrixRowsTest {

    @Test
    fun constructWithMatrix() {
        val m = Matrix(MatrixDimension(2, 3)) { i, j -> (i * 3 + j + 1).toDouble() }
        val view = m.rowsView()

        assertThat(view)
                .hasSize(2)
                .hasIndices(0..1)
                .containsExactly(Vector(1.0, 2.0, 3.0), Vector(4.0, 5.0, 6.0))
    }

    @Test
    fun getChecksIndexBounds() {
        val view = Matrix(2, 3).rowsView()

        assertThatThrownBy { view[-1] }
                .isInstanceOf(IndexOutOfBoundsException::class.java)
                .hasMessage("Index out of range: -1")

        assertThatThrownBy { view[2] }
                .isInstanceOf(IndexOutOfBoundsException::class.java)
                .hasMessage("Index out of range: 2")
    }

    @Test
    fun setChecksIndexBounds() {
        val view = Matrix(2, 3).rowsView()

        assertThatThrownBy { view[-1] = Vector(0.0, 0.0, 0.0) }
                .isInstanceOf(IndexOutOfBoundsException::class.java)
                .hasMessage("Index out of range: -1")

        assertThatThrownBy { view[2] = Vector(0.0, 0.0, 0.0) }
                .isInstanceOf(IndexOutOfBoundsException::class.java)
                .hasMessage("Index out of range: 2")
    }

    @Test
    fun iterator() {
        val m = Matrix(MatrixDimension(2, 3)) { i, j -> (i * 3 + j + 1).toDouble() }
        val view = m.rowsView()

        var count = 0
        for (e in view) {
            when (count++) {
                0 -> assertThat(e).isEqualTo(Vector(1.0, 2.0, 3.0))
                1 -> assertThat(e).isEqualTo(Vector(4.0, 5.0, 6.0))
                else -> fail("Unexpected element: $e")
            }
        }
        assertThat(count).isEqualTo(2)

        val iter = view.iterator()
        assertThat(iter.hasNext()).isTrue()
        assertThat(iter.next()).isEqualTo(Vector(1.0, 2.0, 3.0))
        assertThat(iter.hasNext()).isTrue()
        assertThat(iter.next()).isEqualTo(Vector(4.0, 5.0, 6.0))
        assertThat(iter.hasNext()).isFalse()
        assertThatThrownBy { iter.next() }
                .isInstanceOf(IndexOutOfBoundsException::class.java)
                .hasMessage("Index out of range: 2")
    }

    @Test
    fun rowsView() {
        val m = Matrix(MatrixDimension(2, 3)) { i, j -> (i * 3 + j + 1).toDouble() }
        val view = m.rowsView()

        m *= -1.5

        assertThat(view).describedAs("Modifying the matrix should be visible in the rows view")
                .containsExactly(Vector(-1.5, -3.0, -4.5), Vector(-6.0, -7.5, -9.0))

        view[1][2] = 5.0

        assertThat(m).rows().describedAs("Modifying the rows view should be visible in the matrix")
                .containsExactly(Vector(-1.5, -3.0, -4.5), Vector(-6.0, -7.5, 5.0))
    }

    @Test
    fun rows() {
        val m = Matrix(MatrixDimension(2, 3)) { i, j -> (i * 3 + j + 1).toDouble() }
        val rows = m.rows()

        m *= -1.5

        assertThat(rows).describedAs("Modifying the matrix should be not visible in the rows copy")
                .containsExactly(Vector(1.0, 2.0, 3.0), Vector(4.0, 5.0, 6.0))
    }

    @Test
    fun matrixOfRowsView() {
        val v1 = Vector(1.0, 2.0)
        val v2 = Vector(3.0, 4.0)
        val v3 = Vector(5.0, 6.0)
        val m = matrixOfRowsView(v1, v2, v3)

        assertThat(m)
                .hasDimension(3, 2)
                .columns().containsExactly(Vector(1.0, 3.0, 5.0), Vector(2.0, 4.0, 6.0))

        v1 += 0.5
        v2 *= -1.5
        v3 -= 2.5

        assertThat(m).columns().describedAs("Modyfing the rows view should be visible in the matrix")
                .containsExactly(Vector(1.5, -4.5, 2.5), Vector(2.5, -6.0, 3.5))

        m[1, 1] = 8.0

        assertThat(v2).describedAs("Modifying the matrix should be visible in the rows view")
                .hasElements(-4.5, 8.0)
    }

    @Test
    fun matrixOfRows() {
        val v1 = Vector(1.0, 2.0)
        val v2 = Vector(3.0, 4.0)
        val v3 = Vector(5.0, 6.0)
        val m = matrixOfRows(v1, v2, v3)

        assertThat(m)
                .hasDimension(3, 2)
                .columns().containsExactly(Vector(1.0, 3.0, 5.0), Vector(2.0, 4.0, 6.0))

        v1 += 0.5
        v2 *= -1.5
        v3 -= 2.5

        assertThat(m).columns().describedAs("Modyfing the rows copy should not be visible in the matrix")
                .containsExactly(Vector(1.0, 3.0, 5.0), Vector(2.0, 4.0, 6.0))

        m[1, 1] = 8.0

        assertThat(v2).describedAs("Modifying the matrix should not be visible in the rows copy")
                .hasElements(-4.5, -6.0)
    }
}
