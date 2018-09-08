package com.jesperdj.vecmath

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class MatrixTest {

    @Test
    fun constructWithDimension() {
        val m = Matrix(MatrixDimension(2, 3))

        assertThat(m)
                .hasDimension(2, 3)
                .hasRowIndices(0..1)
                .hasColumnIndices(0..2)
    }

    @Test
    fun constructWithRowsAndCols() {
        val m = Matrix(3, 5)

        assertThat(m)
                .hasDimension(3, 5)
                .hasRowIndices(0..2)
                .hasColumnIndices(0..4)
    }

    @Test
    fun constructWithDimensionAndInit() {
        val m = Matrix(MatrixDimension(2, 3)) { i, j -> (i * 3 + j + 1).toDouble() }

        assertThat(m)
                .hasDimension(2, 3)
                .hasRowIndices(0..1)
                .hasColumnIndices(0..2)

        assertThat(m).rows().containsExactly(
                Vector(1.0, 2.0, 3.0),
                Vector(4.0, 5.0, 6.0))

        assertThat(m).columns().containsExactly(
                Vector(1.0, 4.0),
                Vector(2.0, 5.0),
                Vector(3.0, 6.0))
    }

    @Test
    fun constructWithRowsAndColsAndInit() {
        val m = Matrix(2, 3) { i, j -> (i * 3 + j + 1).toDouble() }

        assertThat(m)
                .hasDimension(2, 3)
                .hasRowIndices(0..1)
                .hasColumnIndices(0..2)

        assertThat(m).rows().containsExactly(
                Vector(1.0, 2.0, 3.0),
                Vector(4.0, 5.0, 6.0))

        assertThat(m).columns().containsExactly(
                Vector(1.0, 4.0),
                Vector(2.0, 5.0),
                Vector(3.0, 6.0))
    }

    @Test
    fun copy() {
        val m1 = Matrix(2, 3) { i, j -> (i * 3 + j + 1).toDouble() }
        val m2 = m1.copy()

        assertThat(m1).rows().containsExactly(
                Vector(1.0, 2.0, 3.0),
                Vector(4.0, 5.0, 6.0))

        assertThat(m2).rows().containsExactly(
                Vector(1.0, 2.0, 3.0),
                Vector(4.0, 5.0, 6.0))

        m2 += 7.5

        assertThat(m1).rows().describedAs("Modifying m2 should not modify m1")
                .containsExactly(Vector(1.0, 2.0, 3.0), Vector(4.0, 5.0, 6.0))
    }

    @Test
    fun getChecksRowIndexBounds() {
        val m = Matrix(3, 5)

        for (j in 0 until 5) {
            assertThatThrownBy { m[-1, j] }
                    .isInstanceOf(IndexOutOfBoundsException::class.java)
                    .hasMessage("Index out of range: -1")

            assertThatThrownBy { m[3, j] }
                    .isInstanceOf(IndexOutOfBoundsException::class.java)
                    .hasMessage("Index out of range: 3")
        }
    }

    @Test
    fun getChecksColumnIndexBounds() {
        val m = Matrix(3, 5)

        for (i in 0 until 3) {
            assertThatThrownBy { m[i, -1] }
                    .isInstanceOf(IndexOutOfBoundsException::class.java)
                    .hasMessage("Index out of range: -1")

            assertThatThrownBy { m[i, 5] }
                    .isInstanceOf(IndexOutOfBoundsException::class.java)
                    .hasMessage("Index out of range: 5")
        }
    }

    @Test
    fun setChecksRowIndexBounds() {
        val m = Matrix(3, 5)

        for (j in 0 until 5) {
            assertThatThrownBy { m[-1, j] = 0.0 }
                    .isInstanceOf(IndexOutOfBoundsException::class.java)
                    .hasMessage("Index out of range: -1")

            assertThatThrownBy { m[3, j] = 0.0 }
                    .isInstanceOf(IndexOutOfBoundsException::class.java)
                    .hasMessage("Index out of range: 3")
        }
    }

    @Test
    fun setChecksColumnIndexBounds() {
        val m = Matrix(3, 5)

        for (i in 0 until 3) {
            assertThatThrownBy { m[i, -1] = 0.0 }
                    .isInstanceOf(IndexOutOfBoundsException::class.java)
                    .hasMessage("Index out of range: -1")

            assertThatThrownBy { m[i, 5] = 0.0 }
                    .isInstanceOf(IndexOutOfBoundsException::class.java)
                    .hasMessage("Index out of range: 5")
        }
    }

    @Test
    fun unaryPlus() {
        val m1 = Matrix(2, 3) { i, j -> (i * 3 + j + 1).toDouble() }
        val m2 = +m1

        assertThat(m2).isEqualTo(m1)
    }

    @Test
    fun unaryMinus() {
        val m1 = Matrix(2, 3) { i, j -> (i * 3 + j + 1).toDouble() }
        val m2 = -m1

        assertThat(m1).rows().containsExactly(
                Vector(1.0, 2.0, 3.0),
                Vector(4.0, 5.0, 6.0))

        assertThat(m2).rows().containsExactly(
                Vector(-1.0, -2.0, -3.0),
                Vector(-4.0, -5.0, -6.0))
    }

    @Test
    fun plusScalar() {
        val m1 = Matrix(2, 3) { i, j -> (i * 3 + j + 1).toDouble() }
        val m2 = m1 + 1.5
        val m3 = 1.5 + m1

        assertThat(m1).rows().containsExactly(
                Vector(1.0, 2.0, 3.0),
                Vector(4.0, 5.0, 6.0))

        assertThat(m2).rows().containsExactly(
                Vector(2.5, 3.5, 4.5),
                Vector(5.5, 6.5, 7.5))

        assertThat(m3).isEqualTo(m2)
    }

    @Test
    fun minusScalar() {
        val m1 = Matrix(2, 3) { i, j -> (i * 3 + j + 1).toDouble() }
        val m2 = m1 - 1.5

        assertThat(m1).rows().containsExactly(
                Vector(1.0, 2.0, 3.0),
                Vector(4.0, 5.0, 6.0))

        assertThat(m2).rows().containsExactly(
                Vector(-0.5, 0.5, 1.5),
                Vector(2.5, 3.5, 4.5))
    }

    @Test
    fun timesScalar() {
        val m1 = Matrix(2, 3) { i, j -> (i * 3 + j + 1).toDouble() }
        val m2 = m1 * 1.5
        val m3 = 1.5 * m1

        assertThat(m1).rows().containsExactly(
                Vector(1.0, 2.0, 3.0),
                Vector(4.0, 5.0, 6.0))

        assertThat(m2).rows().containsExactly(
                Vector(1.5, 3.0, 4.5),
                Vector(6.0, 7.5, 9.0))

        assertThat(m3).isEqualTo(m2)
    }

    @Test
    fun divScalar() {
        val m1 = Matrix(2, 3) { i, j -> (i * 3 + j + 1).toDouble() }
        val m2 = m1 / 2.0

        assertThat(m1).rows().containsExactly(
                Vector(1.0, 2.0, 3.0),
                Vector(4.0, 5.0, 6.0))

        assertThat(m2).rows().containsExactly(
                Vector(0.5, 1.0, 1.5),
                Vector(2.0, 2.5, 3.0))
    }

    @Test
    fun plusAssignScalar() {
        val m = Matrix(2, 3) { i, j -> (i * 3 + j + 1).toDouble() }
        m += 1.5

        assertThat(m).rows().containsExactly(
                Vector(2.5, 3.5, 4.5),
                Vector(5.5, 6.5, 7.5))
    }

    @Test
    fun minusAssignScalar() {
        val m = Matrix(2, 3) { i, j -> (i * 3 + j + 1).toDouble() }
        m -= 1.5

        assertThat(m).rows().containsExactly(
                Vector(-0.5, 0.5, 1.5),
                Vector(2.5, 3.5, 4.5))
    }

    @Test
    fun timesAssignScalar() {
        val m = Matrix(2, 3) { i, j -> (i * 3 + j + 1).toDouble() }
        m *= 1.5

        assertThat(m).rows().containsExactly(
                Vector(1.5, 3.0, 4.5),
                Vector(6.0, 7.5, 9.0))
    }

    @Test
    fun divAssignScalar() {
        val m = Matrix(2, 3) { i, j -> (i * 3 + j + 1).toDouble() }
        m /= 2.0

        assertThat(m).rows().containsExactly(
                Vector(0.5, 1.0, 1.5),
                Vector(2.0, 2.5, 3.0))
    }

    @Test
    fun timesVector() {
        val m = Matrix(2, 3) { i, j -> (i * 3 + j + 1).toDouble() }
        val v = m * Vector(-0.5, 1.5, 2.5)

        assertThat(v).hasElements(10.0, 20.5)
    }

    @Test
    fun timesVectorChecksDimension() {
        val m = Matrix(2, 3)

        assertThatThrownBy { m * Vector(-0.5, 1.5) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Cannot multiply 2 x 3 matrix by vector of dimension 2")

        assertThatThrownBy { m * Vector(-0.5, 1.5, 2.5, 3.0) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Cannot multiply 2 x 3 matrix by vector of dimension 4")
    }

    @Test
    fun timesMatrix() {
        val m1 = matrixOfRowsView(
                Vector(1.0, 2.0, 3.0),
                Vector(4.0, 5.0, 6.0))

        val m2 = matrixOfRowsView(
                Vector(-3.0, 1.5, 0.5, 2.5),
                Vector(7.5, 3.5, -2.0, 6.0),
                Vector(-5.0, 4.0, -1.5, 8.0))

        val m3 = m1 * m2

        assertThat(m1).rows().containsExactly(
                Vector(1.0, 2.0, 3.0),
                Vector(4.0, 5.0, 6.0))

        assertThat(m2).rows().containsExactly(
                Vector(-3.0, 1.5, 0.5, 2.5),
                Vector(7.5, 3.5, -2.0, 6.0),
                Vector(-5.0, 4.0, -1.5, 8.0))

        assertThat(m3).rows().containsExactly(
                Vector(-3.0, 20.5, -8.0, 38.5),
                Vector(-4.5, 47.5, -17.0, 88.0))
    }

    @Test
    fun timesMatrixChecksDimension() {
        val m = Matrix(2, 3)

        assertThatThrownBy { m * Matrix(2, 3) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Cannot multiply 2 x 3 matrix by 2 x 3 matrix")

        assertThatThrownBy { m * Matrix(4, 2) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Cannot multiply 2 x 3 matrix by 4 x 2 matrix")
    }

    @Test
    fun transposeView() {
        val m = Matrix(MatrixDimension(2, 3)) { i, j -> (i * 3 + j + 1).toDouble() }
        val view = m.transposeView()

        assertThat(view).rows()
                .containsExactly(Vector(1.0, 4.0), Vector(2.0, 5.0), Vector(3.0, 6.0))

        view += 2.5

        assertThat(m).rows().describedAs("Modifying the transpose view should be visible in the matrix")
                .containsExactly(Vector(3.5, 4.5, 5.5), Vector(6.5, 7.5, 8.5))

        m *= -1.5

        assertThat(view).rows().describedAs("Modifying the matrix should be visible in the transpose view")
                .containsExactly(Vector(-5.25, -9.75), Vector(-6.75, -11.25), Vector(-8.25, -12.75))
    }

    @Test
    fun transposed() {
        val m = Matrix(MatrixDimension(2, 3)) { i, j -> (i * 3 + j + 1).toDouble() }
        val t = m.transposed()

        assertThat(t).rows()
                .containsExactly(Vector(1.0, 4.0), Vector(2.0, 5.0), Vector(3.0, 6.0))

        t += 2.5

        assertThat(m).rows().describedAs("Modifying the transpose should not be visible in the matrix")
                .containsExactly(Vector(1.0, 2.0, 3.0), Vector(4.0, 5.0, 6.0))

        m *= -1.5

        assertThat(t).rows().describedAs("Modifying the matrix should not be visible in the transpose")
                .containsExactly(Vector(3.5, 6.5), Vector(4.5, 7.5), Vector(5.5, 8.5))
    }
}
